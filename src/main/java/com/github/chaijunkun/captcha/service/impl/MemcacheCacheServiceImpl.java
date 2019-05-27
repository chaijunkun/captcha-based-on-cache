package com.github.chaijunkun.captcha.service.impl;

import com.github.chaijunkun.captcha.service.CacheService;
import com.github.chaijunkun.captcha.util.ObjectUtil;
import net.spy.memcached.MemcachedClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

/**
 * 基于Memcache的缓存实现
 *
 * @author chaijunkun
 */
public class MemcacheCacheServiceImpl implements CacheService {

    private static final Logger logger = LoggerFactory.getLogger(MemcacheCacheServiceImpl.class);

    private MemcachedClient memcachedClient;

    public MemcachedClient getMemcachedClient() {
        return memcachedClient;
    }

    public void setMemcachedClient(MemcachedClient memcachedClient) {
        this.memcachedClient = memcachedClient;
    }

    public MemcacheCacheServiceImpl() {
        logger.info("正在构建Memcache缓存操作服务");
    }

    @Override
    public boolean hasKey(String key) {
        Object obj = this.memcachedClient.get(key);
        return ObjectUtil.isNotEmpty(obj);
    }

    @Override
    public void set(String key, String value, int expire) {
        try {
            this.memcachedClient.set(key, expire, value).get();
        } catch (InterruptedException intEx) {
            throw new RuntimeException(String.format("缓存%s时被中断操作 ", key), intEx);
        } catch (ExecutionException exeEx) {
            throw new RuntimeException(String.format("缓存%s时执行异常,请检查缓存服务器", key));
        }
    }
}
