package com.AETHER.music.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserTrackReactionId implements Serializable {
    private Long user;
    private Long track;

    @Override
    public boolean equals(Object o){
        if(this==o) return true;
        if(!(o instanceof UserTrackReactionId that )) return false;
        return Objects.equals(user,that.user) && Objects.equals(track,that.track);
    }

    @Override
    public int hashCode(){
        return Objects.hash(user,track);
    }
}
