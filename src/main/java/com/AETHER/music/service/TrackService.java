package com.AETHER.music.service;

import com.AETHER.music.DTO.track.TrackDetailDTO;
import com.AETHER.music.DTO.track.TrackSummaryDTO;

import java.util.List;

public interface TrackService {
    TrackDetailDTO getTrack(Long trackId);
    List<TrackSummaryDTO> search(String query);
}
