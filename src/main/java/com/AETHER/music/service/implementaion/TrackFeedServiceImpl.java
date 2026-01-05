package com.AETHER.music.service.implementaion;

import com.AETHER.music.repository.PlayEventRepository;
import com.AETHER.music.service.TrackFeedService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TrackFeedServiceImpl implements TrackFeedService {

    private static final int LIMIT = 10;
    private static final Duration TRENDING_TTL = Duration.ofMinutes(5);

    private final RedisTemplate<String, Long> redisTemplate;
    private final PlayEventRepository playEventRepository;

    public TrackFeedServiceImpl(
            RedisTemplate<String, Long> redisTemplate,
            PlayEventRepository playEventRepository
    ) {
        this.redisTemplate = redisTemplate;
        this.playEventRepository = playEventRepository;
    }

    @Override
    public List<Long> getRecentTrackIds(Long userId) {
        if (userId == null) {
            return List.of();
        }

        // 🔥 FIX: USE THE SAME KEY AS PLAY-EVENT WRITES
        String key = "recent:tracks:user:" + userId;

        List<Long> cached =
                redisTemplate.opsForList().range(key, 0, -1);

        if (cached != null && !cached.isEmpty()) {
            return cached;
        }

        // Fallback ONLY if Redis is empty
        List<Long> trackIds =
                playEventRepository.findRecentDistinctTrackIdsByUser(
                        userId,
                        Pageable.ofSize(LIMIT)
                );

        if (!trackIds.isEmpty()) {
            redisTemplate.delete(key);
            redisTemplate.opsForList().rightPushAll(key, trackIds);
        }

        return trackIds;
    }

    @Override
    public List<Long> getTrendingTrackIds() {

        String key = "home:tracks:trending";

        List<Long> cached = redisTemplate.opsForList().range(key, 0, -1);
        if (cached != null && !cached.isEmpty()) {
            return cached;
        }

        OffsetDateTime since =
                OffsetDateTime.now(ZoneOffset.UTC).minusHours(24);

        List<Long> trackIds =
                playEventRepository.findTrendingTrackIdsSince(
                        since,
                        Pageable.ofSize(LIMIT)
                );

        if (!trackIds.isEmpty()) {
            redisTemplate.delete(key);
            redisTemplate.opsForList().rightPushAll(key, trackIds);
            redisTemplate.expire(key, TRENDING_TTL);
        }

        return trackIds;
    }
}

