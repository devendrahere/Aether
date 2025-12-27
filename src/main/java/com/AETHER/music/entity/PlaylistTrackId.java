package com.AETHER.music.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PlaylistTrackId implements Serializable {
    private Long playlist;
    private Long track;

    @Override
    public boolean equals(Object o){
        if(this==o)return true;
        if(!(o instanceof PlaylistTrackId that)) return true;
        return Objects.equals(playlist,that.playlist) && Objects.equals(track,that.track);
    }

    @Override
    public int hashCode(){
        return Objects.hash(playlist,track);
    }
}
