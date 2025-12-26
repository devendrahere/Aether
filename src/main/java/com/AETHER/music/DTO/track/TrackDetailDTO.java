package com.AETHER.music.DTO.track;

import com.AETHER.music.DTO.album.AlbumSummaryDTO;
import com.AETHER.music.DTO.artist.ArtistDTO;
import com.AETHER.music.DTO.trackfile.TrackFileDTO;

import java.util.List;

public class TrackDetailDTO {
    public Long id;
    public String title;
    public Integer durationSec;
    public ArtistDTO artist;
    public AlbumSummaryDTO album;
    public List<TrackFileDTO> availableFiles;
}
