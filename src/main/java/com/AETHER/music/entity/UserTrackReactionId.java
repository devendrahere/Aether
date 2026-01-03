package com.AETHER.music.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Setter
public class UserTrackReactionId implements Serializable {

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "track_id")
    private Long trackId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserTrackReactionId that)) return false;
        return Objects.equals(userId, that.userId)
                && Objects.equals(trackId, that.trackId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, trackId);
    }
}