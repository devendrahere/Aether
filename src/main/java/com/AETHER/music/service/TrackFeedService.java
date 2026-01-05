package com.AETHER.music.service;

import java.util.List;

public interface TrackFeedService {

    public List<Long> getRecentTrackIds(Long userId);

    public List<Long> getTrendingTrackIds();
}
