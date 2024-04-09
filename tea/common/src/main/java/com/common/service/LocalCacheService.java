package com.common.service;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

/**
 * 本地缓存实现
 */
@Slf4j
@Service
@SuppressWarnings({"unchecked", "all"})
public class LocalCacheService {


    private static Cache<String, String> cache = null;

    static {
        cache = CacheBuilder.newBuilder()
                .initialCapacity(5)  // 初始容量
                .maximumSize(5000)   // 缓存的最大数量，超过该数量将被淘汰
                .expireAfterWrite(1, TimeUnit.DAYS) // 过期时间
                .build();
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    public void del(String... keys) {
        if (keys != null && keys.length > 0) {
            for (String key : keys) {
                cache.invalidate(key);
            }
            log.info("删除缓存：{}", keys);
        }
    }

    /**
     * 获取缓存数据
     * @param key
     * @return
     */
    public String get(String key) {
        return key == null ? null : cache.getIfPresent(key);
    }

    /**
     * 存入缓存
     */
    public boolean set(String key, String value) {
        try {
            cache.put(key, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

}
