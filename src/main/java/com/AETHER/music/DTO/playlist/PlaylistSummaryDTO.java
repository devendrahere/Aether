package com.AETHER.music.DTO.playlist;

public class PlaylistSummaryDTO {

    public Long id;
    public String name;
    public boolean isPublic;
    public int trackCount;

    // ✅ REQUIRED FOR HOME HERO QUERY
    public PlaylistSummaryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
        this.isPublic = false;   // safe default
        this.trackCount = 0;     // safe default
    }

    public PlaylistSummaryDTO(Long id, String name, boolean isPublic, long trackCount) {
        this.id = id;
        this.name = name;
        this.isPublic = isPublic;
        this.trackCount = (int) trackCount;
    }
}
