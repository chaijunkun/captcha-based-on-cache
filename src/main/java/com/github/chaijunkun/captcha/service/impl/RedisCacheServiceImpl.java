package com.github.chaijunkun.captcha.service.impl;

import com.github.chaijunkun.captcha.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * 基于Redis的缓存实现
 *
 * @author chaijunkun
 */
public class RedisCacheServiceImpl implements CacheService {

    private static final Logger logger = LoggerFactory.getLogger(RedisCacheServiceImpl.class);

    private StringRedisTemplate redisTemplate;

    public StringRedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public RedisCacheServiceImpl() {
        logger.info("正在构建Redis缓存操作服务");
    }

    @Override
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public void set(String key, String value, int expire) {
        redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

}
