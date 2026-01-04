package com.AETHER.music.DTO.playlist;

public class PlaylistTrackDTO {

    public Long id;
    public String title;
    public Integer durationSec;
    public Artist artist;

    public PlaylistTrackDTO(
            Long id,
            String title,
            Integer durationSec,
            Long artistId,
            String artistName
    ) {
        this.id = id;
        this.title = title;
        this.durationSec = durationSec;
        this.artist = new Artist(artistId, artistName);
    }

    public static class Artist {
        public Long id;
        public String name;

        public Artist(Long id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
