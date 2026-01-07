package com.AETHER.music.service.implementaion;

import com.AETHER.music.DTO.playevent.PlayEventRequestDTO;
import com.AETHER.music.entity.PlayEvent;
import com.AETHER.music.entity.PlayEventType;
import com.AETHER.music.entity.Playlist;
import com.AETHER.music.event.PlayEventRecorded;
import com.AETHER.music.mapper.ArtistMapper;
import com.AETHER.music.repository.PlayEventRepository;
import com.AETHER.music.repository.PlaylistRepository;
import com.AETHER.music.repository.TrackRepository;
import com.AETHER.music.service.PlayEventService;
import com.AETHER.music.service.UserService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

import org.springframework.context.ApplicationEventPublisher;

@Service
@Transactional
public class PlayEventServiceImpl implements PlayEventService {

    private final ApplicationEventPublisher eventPublisher;
    private final PlayEventRepository playEventRepository;
    private final TrackRepository trackRepository;
    private final UserService userService;
    private final PlaylistRepository playlistRepository;

    public PlayEventServiceImpl(ApplicationEventPublisher eventPublisher, PlayEventRepository playEventRepository, TrackRepository trackRepository, UserService userService, PlaylistRepository playlistRepository) {
        this.eventPublisher = eventPublisher;
        this.playEventRepository = playEventRepository;
        this.trackRepository = trackRepository;
        this.userService = userService;
        this.playlistRepository = playlistRepository;
    }

    @Override
    public void record(PlayEventRequestDTO dto, Long userId) {

        PlayEvent event = new PlayEvent();
        var track=trackRepository.getReferenceById(dto.trackId);

        // Track (mandatory)
        event.setTrack(trackRepository.getReferenceById(dto.trackId));

        // Session (mandatory)
        event.setSessionId(dto.sessionId);

        // Event type
        event.setEventType(dto.eventType);

        // User (optional)
        if (userId != null) {
            event.setUser(userService.getById(userId));
        }

        // Playlist context (optional)
        if (dto.playlistId != null) {
            event.setPlaylist(
                    playlistRepository.getReferenceById(dto.playlistId)
            );
        }

        playEventRepository.save(event);
        Long albumId=dto.albumId != null
                ? dto.albumId
                : track.getAlbum() != null
                ? track.getAlbum().getId()
                : null;
        // Publish only meaningful signal
        if (userId != null && dto.eventType == PlayEventType.PLAY) {
            eventPublisher.publishEvent(
                    new PlayEventRecorded(
                            userId,
                            dto.trackId,
                            dto.playlistId,
                            albumId
                    )
            );
        }
    }

}