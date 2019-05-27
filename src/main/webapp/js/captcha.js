var __CONTEXT_ROOT = "";
var Captcha = {
	//公共方法,用来将对象转化为URL参数
	parseParam:function(param, key){
		var paramStr="";
		if(param instanceof String||param instanceof Number||param instanceof Boolean){
			paramStr+="&"+key+"="+encodeURIComponent(param);
		}else{
			jQuery.each(param,function(i){
				var k=key==null?i:key+(param instanceof Array?"["+i+"]":"."+i);
				paramStr+='&'+Captcha.parseParam(this, k);
			});
		}
		return paramStr.substr(1);
	},
	captchaInputSelector:"#captcha",
	tokenInputSelector:"#token",
	captchaImgSelector:"#captchaImg",
	clear:function(){
		$(this.captchaInputSelector).val("");
		$(this.tokenInputSelector).val("");
		$(this.captchaImgSelector).attr("src", "");
		this.getToken();
	},
	getToken:function(){
		$.ajax({
			type:"get",
			url:__CONTEXT_ROOT+"/captcha/captcha.do",
			success:function(data){
				var token= data;
				$(Captcha.tokenInputSelector).val(token);
				var param = new Object();
				param.token = token;
				param.random= Math.random();//增加随机数参数防止缓存
				$(Captcha.captchaImgSelector).attr("src", __CONTEXT_ROOT+"/captcha/captcha.do?"+Captcha.parseParam(param));
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				alert("验证码刷新失败");
				if(console){
					console.log(errorThrown);
				}
			}
		});
	},
	doVerify:function(){
		var param = new Object();
		param.captcha = $(Captcha.captchaInputSelector).val();
		param.token = $(Captcha.tokenInputSelector).val();
		$.ajax({
			type:"post",
			url:__CONTEXT_ROOT+"/captcha/verify.do",
			data:param,
			success:function(data){
				if (data && !data.code){
					alert(data.msg);
				}else{
					alert("校验失败:"+data.msg);
					Captcha.getToken();
				}
			},
			error:function(XMLHttpRequest, textStatus, errorThrown){
				alert("验证码刷新失败");
				if(console){
					console.log(errorThrown);
				}
			}
		});
	}
}