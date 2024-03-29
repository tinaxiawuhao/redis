package com.example.redisstudy.template;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Repository
public class RedisLock {

    /**
     * 解锁脚本，原子操作
     */
    private static final String unlockScript =
            "if redis.call(\"get\",KEYS[1]) == ARGV[1]\n"
                    + "then\n"
                    + "    return redis.call(\"del\",KEYS[1])\n"
                    + "else\n"
                    + "    return 0\n"
                    + "end";

    private StringRedisTemplate redisTemplate;

    public RedisLock(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 加锁，有阻塞
     *
     * @param name
     * @param expire
     * @param timeout
     * @return
     */
    public String lock(String name, long expire, long timeout) {
        long startTime = System.currentTimeMillis();
        String token;
        do {
            token = tryLock(name, expire);
            if (token == null) {
                if ((System.currentTimeMillis() - startTime) > (timeout - 50))
                    break;
                try {
                    Thread.sleep(50); //try 50 per sec
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        } while (token == null);

        return token;
    }

    /**
     * 加锁，无阻塞
     *
     * @param name
     * @param expire
     * @return
     */
    public String tryLock(String name, long expire) {
        String token = UUID.randomUUID().toString();
        if (redisTemplate.opsForValue().setIfAbsent(name, token, expire, TimeUnit.SECONDS)) {
            return token;
        }
        return null;
    }

    /**
     * 解锁
     *
     * @param name
     * @param token
     * @return
     */
    public boolean unlock(String name, String token) {
        byte[][] keysAndArgs = new byte[2][];
        keysAndArgs[0] = name.getBytes(StandardCharsets.UTF_8);
        keysAndArgs[1] = token.getBytes(StandardCharsets.UTF_8);
        RedisConnectionFactory factory = redisTemplate.getConnectionFactory();
        assert factory != null;
        RedisConnection conn = factory.getConnection();
        try {
            Long result = (Long) conn.scriptingCommands().eval(unlockScript.getBytes(StandardCharsets.UTF_8), ReturnType.INTEGER, 1, keysAndArgs);
            if (result != null && result > 0)
                return true;
        } finally {
            RedisConnectionUtils.releaseConnection(conn, factory);
        }

        return false;
    }
}