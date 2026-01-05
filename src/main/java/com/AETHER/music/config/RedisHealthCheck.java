package com.AETHER.music.config;

import jakarta.annotation.PostConstruct;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisHealthCheck {

    private final RedisTemplate<String, Long> redisTemplate;

    public RedisHealthCheck(RedisTemplate<String, Long> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void check() {
        redisTemplate.opsForValue().set("aether:test", 1L);
        System.out.println("Redis says: " +
                redisTemplate.opsForValue().get("aether:test"));
    }
}
