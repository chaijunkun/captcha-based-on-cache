# 项目背景
项目中要求加入一个验证码功能，相信大家都不陌生，偷懒的方式基本上完全使用开源的框架，例如java中使用的jcaptcha和kaptcha。阅读了源代码之后发现验证码的存储都放在了session中。如果你要做的项目是一个小型站点（可能只有一台或者很少的几台服务器）那很容易做到，例如session同步或者将请求按照某种哈希始终重写到特定机器上。但是一个大型网站，具有几十甚至上百台服务器，让它们之间同步session，开销是巨大的，甚至是不可行的。于是我就开始研究新的实现方法。
# 思路
首先我想到了缓存。互联网项目开发目前比较流行的缓存服务分别是memcache和redis。两种都是非关系型缓存，即按照<key, value>方式存取，操作速度是传统关系型数据库查询的几十甚至上百倍。性能上面没问题，二者的区别这里不谈，我利用的是它们的共性——缓存时效性。我们知道，验证码也是有时效的。那么现在要解决的两个问题：
1. 如何生成和判别验证码
2. 生成了验证码字符后如何转换为相应的图片。
# 解决
1. 首先有一个接口，调用之后返回一个加密的字符串，这个字符串的明文是一个精心设计的数据结构，包含了“验证码的内容”和“请求该密文的时间”，我们把这个加密的字符串称之为token。在返回这串密文的同时，该接口将动态生成的数据结构取哈希作为key，而数据结构本身作为value放入到缓存当中，有效时间可以设置为需要的值，假设30秒；
2. 得到token之后，我们利用这个token去请求另外一个接口来获取相应的图片。这个接口能够对token进行解密，从而从中获取到验证码内容和原始的请求时间。如果发现token中原始的请求时间距离现在超过了预设值（上面提到的30秒），则不予返回任何东西；如果距离现在仍在有效时间内，则从缓存的图片黑名单中去寻找该token是否请求过验证码图片。例如可以采用key格式为：img_black_hash(token)，如果命中了，也什么都不返回，如果没命中，那就按照token中的验证码字符生成相应的图片，并将该token放入到图片黑名单中，注意放入黑名单中的缓存有效期一定要比验证码有效期明显长一些（例如60秒）。下次客户端再拿token来请求验证码图片时先从图片黑名单里查找，命中就不返回，即便没命中，也由于token中的时间已经过了有效期而什么也得不到。
3. 客户端拿到了token，用户也通过被扰乱涂鸦图片输入了相应的验证码，客户端就把明文和token一起提交到服务器。

先解密，解密失败了当然就是数据被篡改了，鉴定失败；

解密成功，同样去缓存中查询该token是否被验证过，如果命中了，说明被验证过，鉴定失败；这里使用的黑名单与步骤2中的黑名单不是同一个，可以采用key格式为verified_hash(token)，缓存有效期要求和步骤2一样，也要比验证码有效期明显长一些；

如果没命中，抽取解密后的数据，匹配当前时间和原始请求token的时间，时间尚在有效期内，则尝试匹配用户输入的验证码和之前请求的验证码内容。匹配成功则鉴定成功，匹配失败则鉴定失败。无论匹配是否成功，都将该token放入黑名单，防止再次验证。
# 分析
通过以上步骤，可以很好地实现验证码脱离session。而因为引入了黑名单和超时机制，可以很好地抵御重放攻击。

memcache不支持集群怎么办？这个很好解决。举个例子：你有8台缓存服务器分别cache[0]...cache[7]，通过hash(token)来得到一个哈希值，然后模8取余，结果肯定是介于0~7之间的整数，那接下来就去操作相应的cache[n]就可以了。

memcache和redis的PK。说到优劣，很多人其实已经想到了。memcache作为一个纯内存缓存，当掉电或者程序崩溃时，所有缓存的数据都将消失。redis可以将缓存的数据写入到文件，即便是掉电或者程序崩溃，在恢复之后仍然可以继续提供服务。（尽管验证码服务对时间很敏感，有效期就那么短短的30秒，但如果你操作速度足够够快，也许能不至于数据全丢）

存在的问题。确实，这个架构对于时间是很敏感的。实现token生成、图片生成、验证码鉴定这三个接口可以不位于同一台机器，可以拆分也可负载均衡，但对时间的要求稍高，几台机器的系统时间相差不宜太大。最好定时同步标准时间，或者在内网搭建一台时间服务器来给它们授时。不管怎样，这要比同步session的开销要小很多了。

token数据结构的设计。我做的设计很简单，验证码宽度是4位，字符空间是阿拉伯数字和英文大小写。标记token生成的时间采用的是距离1970年1月1日0时的毫秒数。为了高并发下的不重复，后面还可以加入几位随机数。最终格式可以是：[captcha]_[timestamp]_[random]。例如：5Ais_1369930314548_2137。不同字段以下划线分隔。

关于加密算法。这里我使用了XXTEA算法+Base64编码。XXTEA算法非常简洁高效，运算速度快，是一种实用的对称加密算法。为了保证加密后的结果能以字符串的形式传输，在加密之后进行了Base64编码。在解密时将密文先经过Base64去编码，然后再解密。

图片如何生成。我们一直在讨论如何生成token和鉴定token，图片的生成是一个难点，通过比较，我将kaptcha的一部分功能单独拿出来使用，将字符转图片的代码提取出来了，再加上自己写了点字符旋转代码:)