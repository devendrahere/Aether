package com.AETHER.music.service.implementaion;

import com.AETHER.music.DTO.home.HomeHeroDTO;
import com.AETHER.music.DTO.home.HomeResponseDTO;
import com.AETHER.music.DTO.home.HomeSectionDTO;
import com.AETHER.music.DTO.home.HomeSectionType;
import com.AETHER.music.DTO.playlist.PlaylistSummaryDTO;
import com.AETHER.music.DTO.track.TrackSummaryDTO;
import com.AETHER.music.mapper.TrackMapper;
import com.AETHER.music.repository.PlayEventRepository;
import com.AETHER.music.repository.PlaylistRepository;
import com.AETHER.music.repository.TrackRepository;
import com.AETHER.music.service.HomeService;
import com.AETHER.music.service.TrackFeedService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
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
    private final TrackFeedService trackFeedService;
    private final TrackMapper trackMapper;
    private final PlaylistRepository playlistRepository;


    public HomeServiceImpl(
            RedisTemplate<String, Long> redisTemplate,
            PlayEventRepository playEventRepository,
            TrackRepository trackRepository, TrackFeedService trackFeedService, TrackMapper trackMapper, PlaylistRepository playlistRepository
    ) {
        this.redisTemplate = redisTemplate;
        this.playEventRepository = playEventRepository;
        this.trackRepository = trackRepository;
        this.trackFeedService = trackFeedService;
        this.trackMapper = trackMapper;
        this.playlistRepository = playlistRepository;
    }

    @Override
    public HomeResponseDTO getHome(Long userId) {

        List<HomeSectionDTO> sections = new ArrayList<>();

        if (userId != null) {
            List<Long> recentIds = trackFeedService.getRecentTrackIds(userId);
            sections.add(
                    buildTrackSection(
                            HomeSectionType.RECENT_TRACKS,
                            "Recently Played",
                            recentIds
                    )
            );
        }

        List<Long> trendingIds = trackFeedService.getTrendingTrackIds();
        sections.add(
                buildTrackSection(
                        HomeSectionType.TRENDING_TRACKS,
                        "Trending Now",
                        trendingIds
                )
        );

        return new HomeResponseDTO(
                buildHero(userId),
                sections
        );
    }


    private HomeSectionDTO buildTrackSection(
            HomeSectionType type,
            String title,
            List<Long> trackIds
    ) {
        if (trackIds.isEmpty()) {
            return new HomeSectionDTO(type, title, List.of());
        }

        // ✅ 1️⃣ DEDUPLICATE while preserving Redis order
        List<Long> uniqueIds = trackIds.stream()
                .distinct()
                .toList();

        // ✅ 2️⃣ Fetch entities with artists
        var tracks = trackRepository.findWithArtistsByIdIn(uniqueIds);

        // ✅ 3️⃣ Map entity → DTO
        Map<Long, TrackSummaryDTO> map =
                tracks.stream()
                        .map(trackMapper::toSummaryDTO)
                        .collect(Collectors.toMap(
                                TrackSummaryDTO::getId,
                                dto -> dto
                        ));

        // ✅ 4️⃣ Preserve Redis order EXACTLY
        List<TrackSummaryDTO> ordered =
                uniqueIds.stream()
                        .map(map::get)
                        .filter(Objects::nonNull)
                        .toList();

        return new HomeSectionDTO(type, title, ordered);
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

    private HomeHeroDTO buildHero(Long userId) {

        if (userId == null) {
            return new HomeHeroDTO("Welcome to Aether", null);
        }

        String key = "recent:playlists:user:" + userId;

        List<Long> playlistIds =
                redisTemplate.opsForList().range(key, 0, -1);

        if (playlistIds == null || playlistIds.isEmpty()) {
            return new HomeHeroDTO("Good to see you again", null);
        }

        Long latestPlaylistId = playlistIds.get(0);

        PlaylistSummaryDTO playlist =
                playlistRepository.findSummaryById(latestPlaylistId);

        return new HomeHeroDTO(
                "Good to see you again",
                playlist
        );
    }

}
