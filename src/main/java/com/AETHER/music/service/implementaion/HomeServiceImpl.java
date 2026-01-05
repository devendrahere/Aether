package com.AETHER.music.service.implementaion;

import com.AETHER.music.DTO.home.HomeResponseDTO;
import com.AETHER.music.DTO.track.TrackSummaryDTO;
import com.AETHER.music.repository.PlayEventRepository;
import com.AETHER.music.repository.TrackRepository;
import com.AETHER.music.service.HomeService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class HomeServiceImpl implements HomeService {

    private static final String GLOBAL_HOME_KEY = "home:top:tracks";
    private static final Duration GLOBAL_HOME_TTL = Duration.ofMinutes(5);
    private static final int HOME_LIMIT = 10;

    private final RedisTemplate<String, Long> redisTemplate;
    private final PlayEventRepository playEventRepository;
    private final TrackRepository trackRepository;

    public HomeServiceImpl(
            RedisTemplate<String, Long> redisTemplate,
            PlayEventRepository playEventRepository,
            TrackRepository trackRepository
    ) {
        this.redisTemplate = redisTemplate;
        this.playEventRepository = playEventRepository;
        this.trackRepository = trackRepository;
    }

    @Override
    public HomeResponseDTO getHome(Long userId) {
        if (userId == null) {

            List<Long> trackIds = getGlobalTopTracksFromCache();

            if (!trackIds.isEmpty()) {
                return buildResponse(trackIds);
            }

            // Cache miss → DB
            trackIds = playEventRepository
                    .findRecentDistinctTrackIdsByUser(userId, Pageable.ofSize(10));

            cacheGlobalTopTracks(trackIds);
            return buildResponse(trackIds);
        }

        String key = "recent:tracks:user:" + userId;

        List<Long> trackIds = getRecentTrackIdsFromCache(key);

        if (!trackIds.isEmpty()) {
            return buildResponse(trackIds);
        }

        // Cache miss → DB
        trackIds = playEventRepository
                .findRecentDistinctTrackIdsByUser(userId, Pageable.ofSize(10));
        return buildResponse(trackIds);
    }

    private HomeResponseDTO buildResponse(List<Long> trackIds) {

        if (trackIds.isEmpty()) {
            HomeResponseDTO empty = new HomeResponseDTO();
            empty.recentTracks = List.of();
            return empty;
        }

        List<TrackSummaryDTO> tracks =
                trackRepository.findTrackSummariesByIds(trackIds);

        Map<Long, TrackSummaryDTO> map = tracks.stream()
                .collect(Collectors.toMap(TrackSummaryDTO::getId, t -> t));

        List<TrackSummaryDTO> ordered =
                trackIds.stream()
                        .map(map::get)
                        .filter(Objects::nonNull)
                        .toList();

        HomeResponseDTO response = new HomeResponseDTO();
        response.recentTracks = ordered;
        return response;
    }

    @SuppressWarnings("unchecked")
    private List<Long> getRecentTrackIdsFromCache(String key) {
        List<Long> raw = redisTemplate.opsForList().range(key, 0, -1);
        return raw == null ? List.of() : raw;
    }

    private List<Long> getGlobalTopTracksFromCache() {
        List<Long> raw =
                redisTemplate.opsForList().range(GLOBAL_HOME_KEY, 0, -1);

        return raw == null ? List.of() : raw;
    }

    private void cacheGlobalTopTracks(List<Long> trackIds) {
        if (trackIds.isEmpty()) return;

        redisTemplate.delete(GLOBAL_HOME_KEY);
        redisTemplate.opsForList().rightPushAll(GLOBAL_HOME_KEY, trackIds);
        redisTemplate.expire(GLOBAL_HOME_KEY, GLOBAL_HOME_TTL);
    }

}
