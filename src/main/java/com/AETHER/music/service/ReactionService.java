package com.AETHER.music.service;

import com.AETHER.music.DTO.track.TrackSummaryDTO;

import java.util.List;

public interface ReactionService {
    void likeTrack(Long userId, Long trackId);
    void unlikeTrack(Long userId, Long trackId);
    List<TrackSummaryDTO> getLikedTracks(Long userId);
}
