package com.AETHER.music.DTO.album;

import com.AETHER.music.DTO.artist.ArtistDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlbumSummaryDTO {

    public Long id;
    public String title;
    public Integer releaseYear;
    public ArtistDTO artist;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AlbumSummaryDTO)) return false;
        AlbumSummaryDTO that = (AlbumSummaryDTO) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

