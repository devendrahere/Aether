package com.AETHER.music.event;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

import java.time.Duration;

@Component
public class PlayEventRedisListener {

    private static final int TRACK_LIMIT = 10;
    private static final int CONTEXT_LIMIT = 4;

    private static final Duration TRACK_TTL = Duration.ofMinutes(10);
    private static final Duration CONTEXT_TTL = Duration.ofMinutes(30);

    private final RedisTemplate<String, Long> redisTemplate;

    public PlayEventRedisListener(RedisTemplate<String, Long> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void updateRedis(PlayEventRecorded event) {

        Long userId = event.userId();

        // ===== TRACKS =====
        String trackKey = "recent:tracks:user:" + userId;
        redisTemplate.opsForList().remove(trackKey, 0, event.trackId());
        redisTemplate.opsForList().leftPush(trackKey, event.trackId());
        redisTemplate.opsForList().trim(trackKey, 0, TRACK_LIMIT - 1);
        redisTemplate.expire(trackKey, TRACK_TTL);

        // ===== PLAYLISTS =====
        if (event.playlistId() != null) {
            String playlistKey = "recent:playlists:user:" + userId;

            redisTemplate.opsForList().remove(playlistKey, 0, event.playlistId());
            redisTemplate.opsForList().leftPush(playlistKey, event.playlistId());
            redisTemplate.opsForList().trim(playlistKey, 0, CONTEXT_LIMIT - 1);
            redisTemplate.expire(playlistKey, CONTEXT_TTL);
        }

        // ===== ALBUMS =====
        if (event.albumId() != null) {
            String albumKey = "recent:albums:user:" + userId;

            redisTemplate.opsForList().remove(albumKey, 0, event.albumId());
            redisTemplate.opsForList().leftPush(albumKey, event.albumId());
            redisTemplate.opsForList().trim(albumKey, 0, CONTEXT_LIMIT - 1);
            redisTemplate.expire(albumKey, CONTEXT_TTL);
        }
    }
}
