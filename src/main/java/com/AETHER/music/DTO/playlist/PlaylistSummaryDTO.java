package com.AETHER.music.DTO.playlist;

public class PlaylistSummaryDTO {
    public Long id;
    public String name;
    public boolean isPublic;
    public int trackCount;

    public PlaylistSummaryDTO(Long id, String name, boolean isPublic, long trackCount) {
        this.id = id;
        this.name = name;
        this.isPublic = isPublic;
        this.trackCount = (int) trackCount;
    }
}
