package com.AETHER.music.DTO.playlist;

import com.AETHER.music.DTO.artist.ArtistDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PlaylistTrackDTO {

    private Long id;
    private String title;
    private Integer durationSec;

    // REQUIRED for playlists
    private int position;

    // MULTIPLE ARTISTS (truthful model)
    private List<ArtistDTO> artists;
}
