package com.AETHER.music.event;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

import java.time.Duration;

@Component
public class PlayEventRedisListener {

    private static final int LIMIT = 10;
    private final RedisTemplate<String, Long> redisTemplate;

    public PlayEventRedisListener(RedisTemplate<String, Long> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void updateRedis(PlayEventRecorded event) {

        // ================= TRACKS =================
        String trackKey = "recent:tracks:user:" + event.userId();

        redisTemplate.opsForList().remove(trackKey, 0, event.trackId());
        redisTemplate.opsForList().leftPush(trackKey, event.trackId());
        redisTemplate.opsForList().trim(trackKey, 0, LIMIT - 1);
        redisTemplate.expire(trackKey, Duration.ofMinutes(10));

        // ================= PLAYLISTS =================
        if (event.playlistId() != null) {
            String playlistKey = "recent:playlists:user:" + event.userId();

            redisTemplate.opsForList().remove(playlistKey, 0, event.playlistId());
            redisTemplate.opsForList().leftPush(playlistKey, event.playlistId());
            redisTemplate.opsForList().trim(playlistKey, 0, LIMIT - 1);
            redisTemplate.expire(playlistKey, Duration.ofMinutes(30));
        }
    }
}
