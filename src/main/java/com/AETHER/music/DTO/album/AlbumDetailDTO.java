package com.AETHER.music.DTO.album;

import com.AETHER.music.DTO.artist.ArtistDTO;
import com.AETHER.music.DTO.track.TrackSummaryDTO;

import java.util.List;

public class AlbumDetailDTO {
    public Long id;
    public String title;
    public Integer releaseYear;
    public ArtistDTO artist;
    public List<TrackSummaryDTO> tracks;
}
