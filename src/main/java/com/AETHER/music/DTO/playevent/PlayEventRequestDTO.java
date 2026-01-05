package com.AETHER.music.DTO.playevent;

import com.AETHER.music.entity.PlayEventType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class PlayEventRequestDTO {
    @NotNull
    public UUID sessionId;

    @NotNull
    public Long trackId;

    public Long playlistId;

    @NotNull
    public PlayEventType eventType;
}

