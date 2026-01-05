package com.AETHER.music.event;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

import java.time.Duration;

@Component
public class PlayEventRedisListener {

    private final RedisTemplate<String, Long> redisTemplate;

    public PlayEventRedisListener(RedisTemplate<String, Long> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void updateRedis(PlayEventRecorded event) {
        System.out.println(">>> AFTER_COMMIT LISTENER FIRED");

        String key = "recent:tracks:user:" + event.userId();

        redisTemplate.opsForList().leftPush(key, event.trackId());
        redisTemplate.opsForList().trim(key, 0, 9);
        redisTemplate.expire(key, Duration.ofMinutes(10));
    }
}