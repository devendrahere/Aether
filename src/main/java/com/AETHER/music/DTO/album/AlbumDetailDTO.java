package com.AETHER.music.DTO.album;

import com.AETHER.music.DTO.artist.ArtistDTO;
import com.AETHER.music.DTO.track.TrackSummaryDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AlbumDetailDTO {
    public Long id;
    public String title;
    public Integer releaseYear;
    public ArtistDTO artist;
    public List<TrackSummaryDTO> tracks;
}
