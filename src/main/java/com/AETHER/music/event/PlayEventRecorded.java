package com.AETHER.music.event;

public record PlayEventRecorded(
        Long userId,
        Long trackId,
        Long playlistId,
        Long albumId
) {}