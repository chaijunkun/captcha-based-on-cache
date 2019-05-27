package com.github.chaijunkun.captcha.service;

/**
 * 缓存服务
 * @author chaijunkun
 */
public interface CacheService {

    boolean hasKey(String key);

    void set(String key, String value, int expire);

}
