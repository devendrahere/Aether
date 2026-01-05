package com.AETHER.music.DTO.home;

import com.AETHER.music.DTO.track.TrackSummaryDTO;

import java.util.List;

public record HomeSectionDTO(
        HomeSectionType type,
        String title,
        List<TrackSummaryDTO> items
) {}
