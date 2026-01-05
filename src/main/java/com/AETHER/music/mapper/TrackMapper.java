package com.AETHER.music.mapper;

import com.AETHER.music.DTO.track.TrackSummaryDTO;
import com.AETHER.music.entity.Track;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrackMapper {

    private final ArtistMapper artistMapper;

    public TrackSummaryDTO toSummaryDTO(Track track) {
        if (track == null) return null;

        TrackSummaryDTO dto = new TrackSummaryDTO();
        dto.setId(track.getId());
        dto.setTitle(track.getTitle());
        dto.setDurationSec(track.getDurationSec());

        dto.setArtists(
                track.getArtists()
                        .stream()
                        .map(artistMapper::toDTO)
                        .toList()
        );

        return dto;
    }
}