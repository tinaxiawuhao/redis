package com.example.redisstudy.template;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
* @Description //直接使用Redis进行分布式锁 
* 这是简易版本  如果要使用Redis原生锁记得加过期时间，防止死锁 最好使用Redisson操作简单更加方便
* @Date
* @Author wuhao
**/
 
@Component
public class RedisLockCommon {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private Integer EXPIRE_TIME=3000;
    /**
     * Redis加锁的操作
     *
     * @param key
     * @param value
     * @return
     */
    public Boolean tryLock(String key, String value) {
        if (stringRedisTemplate.opsForValue().setIfAbsent(key, value, EXPIRE_TIME, TimeUnit.SECONDS)) {
            return true;
        }
        return false;
    }
 
 
    /**
     * Redis解锁的操作
     *
     * @param key
     * @param value
     */
    public void unlock(String key, String value) {
        String currentValue = stringRedisTemplate.opsForValue().get(key);
        try {
            if (StringUtils.isNotEmpty(currentValue) && currentValue.equals(value)) {
                stringRedisTemplate.opsForValue().getOperations().delete(key);
            }
        } catch (Exception e) {
        }
    }
}