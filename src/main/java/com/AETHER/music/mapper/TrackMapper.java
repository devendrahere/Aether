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

        return new TrackSummaryDTO(
                track.getId(),
                track.getTitle(),
                track.getDurationSec(),
                artistMapper.toDTO(track.getArtist())
        );
    }
}