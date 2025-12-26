package com.AETHER.music.service.implementaion;

import com.AETHER.music.DTO.playevent.PlayEventRequestDTO;
import com.AETHER.music.entity.PlayEvent;
import com.AETHER.music.repository.PlayEventRepository;
import com.AETHER.music.repository.TrackRepository;
import com.AETHER.music.service.PlayEventService;
import com.AETHER.music.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PlayEventServiceImpl implements PlayEventService {

    private final PlayEventRepository playEventRepository;
    private final TrackRepository trackRepository;
    private final UserService userService;

    public PlayEventServiceImpl(PlayEventRepository playEventRepository,
                                TrackRepository trackRepository,
                                UserService userService) {
        this.playEventRepository = playEventRepository;
        this.trackRepository = trackRepository;
        this.userService = userService;
    }

    @Override
    public void record(PlayEventRequestDTO dto, Long userId) {

        PlayEvent event = new PlayEvent();
        event.setSessionId(dto.sessionId);
        event.setEventType(dto.eventType);
        event.setTrack(trackRepository.getReferenceById(dto.trackId));

        if (userId != null) {
            event.setUser(userService.getById(userId));
        }

        playEventRepository.save(event);
    }
}
