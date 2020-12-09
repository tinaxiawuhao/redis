package com.example.redisstudy.template;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author wuhao
 * @desc ...
 * @date 2020-12-09 14:57:30
 */
@Component
public class RedissonLock {
    @Value("${spring.redis.address}")
    public String address;

    public RLock lock(){
        Config config = new Config();
        config.useSingleServer().setAddress(address);
//        config.useSingleServer().setPassword("redis1234");
        final RedissonClient client = Redisson.create(config);
        RLock lock = client.getLock("redis:lock");
        return lock;
    }
}
