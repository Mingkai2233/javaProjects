package com.sky.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class CacheUpdateRunner implements CommandLineRunner {
    private final RedisTemplate redisTemplate;
    @Autowired
    public CacheUpdateRunner(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }
    @Override
    public void run(String... args){
        log.info("开始清理缓存...");
        redisTemplate.execute((RedisCallback<Object>) connection -> {
            connection.flushDb();
            return null;
        });
    }
}
