package com.AETHER.music.DTO.playlist;

import java.util.List;

public class PlaylistDetailDTO {
    public Long id;
    public String name;
    public String description;
    public boolean isPublic;
    public List<PlaylistTrackDTO> tracks;
}
