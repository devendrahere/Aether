package com.AETHER.music.service;

import com.AETHER.music.entity.Track;

import java.util.List;

public interface ReactionService {
    void likeTrack(Long userId, Long trackId);
    void unlikeTrack(Long userId, Long trackId);
    List<Track> getLikedTracks(Long userId);
}