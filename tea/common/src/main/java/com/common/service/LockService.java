package com.common.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

// 锁服务
@Slf4j
@Service
public class LockService {

    @Resource
    private LocalCacheService localCacheService;

    /**
     * 保证只有一个请求来处理（缓存获取数据）
     * @param key
     * @param value
     * @return
     */
    public boolean tryLock(String key, String value) {
        try {
            return localCacheService.set(key, value);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 释放锁（缓存删除数据）
     * @param key
     * @return
     */
    public boolean releaseLock(String key) {
       localCacheService.del(key);
       return true;
    }

}
