package com.github.chaijunkun.captcha.misc;

import com.google.code.kaptcha.Producer;

/**
 * 验证码配置
 *
 * @author chaijunkun
 */
public class CaptchaConfig {

    /**
     * XXTEA加密解密的密钥
     */
    private String secKey = "captcha";

    /**
     * 验证码超时门限(秒)
     */
    private long expire = 30;

    /**
     * 缓存前缀
     */
    private String cachePrefix = "captcha";

    /**
     * 验证码字符数
     */
    private int charCount = 4;

    /**
     * 验证码生成器
     */
    private Producer producer;

    public String getSecKey() {
        return secKey;
    }

    public void setSecKey(String secKey) {
        this.secKey = secKey;
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    public String getCachePrefix() {
        return cachePrefix;
    }

    public void setCachePrefix(String cachePrefix) {
        this.cachePrefix = cachePrefix;
    }

    public int getCharCount() {
        return charCount;
    }

    public void setCharCount(int charCount) {
        this.charCount = charCount;
    }

    public Producer getProducer() {
        return producer;
    }

    public void setProducer(Producer producer) {
        this.producer = producer;
    }
}