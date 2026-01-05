package com.AETHER.music.DTO.track;

import com.AETHER.music.DTO.album.AlbumSummaryDTO;
import com.AETHER.music.DTO.artist.ArtistDTO;
import com.AETHER.music.DTO.trackfile.TrackFileDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TrackDetailDTO {

    private Long id;
    private String title;
    private Integer durationSec;
    private List<ArtistDTO> artists;
    private AlbumSummaryDTO album;
    private List<TrackFileDTO> availableFiles;
}
