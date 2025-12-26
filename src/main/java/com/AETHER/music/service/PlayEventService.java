package com.AETHER.music.service;

import com.AETHER.music.DTO.playevent.PlayEventRequestDTO;

public interface PlayEventService {

    void record(PlayEventRequestDTO dto, Long userId);

}
