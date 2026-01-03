package com.AETHER.music.DTO.playlist;

import com.AETHER.music.DTO.track.TrackSummaryDTO;

public class PlaylistTrackDTO {

    public int position;
    public TrackSummaryDTO track;

    public PlaylistTrackDTO(int position, TrackSummaryDTO track) {
        this.position = position;
        this.track = track;
    }
}
