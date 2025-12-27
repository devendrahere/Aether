package com.AETHER.music.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ArtistGenreId implements Serializable {
    private Long artist;
    private Long genre;

    @Override
    public boolean equals(Object o){
        if(this==o) return true;
        if(!(o instanceof ArtistGenreId that))return false;
        return Objects.equals(artist,that.artist) && Objects.equals(genre,that.genre);
    }

    @Override
    public int hashCode(){
        return Objects.hash(artist,genre);
    }
}