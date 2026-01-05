package com.AETHER.music.DTO.playevent;

import com.AETHER.music.entity.PlayEventType;

import java.util.UUID;

public class PlayEventRequestDTO {
    public UUID sessionId;
    public Long trackId;
    public PlayEventType eventType;
}
