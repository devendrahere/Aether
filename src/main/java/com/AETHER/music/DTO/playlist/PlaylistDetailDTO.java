package com.AETHER.music.DTO.playlist;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class PlaylistDetailDTO {
    public Long id;
    public String name;
    public String description;
    public boolean isPublic;
    public List<PlaylistTrackDTO> tracks;
}
