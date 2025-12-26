package com.AETHER.music.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "playlist_tracks",
        uniqueConstraints = @UniqueConstraint(columnNames = {"playlist_id", "position"})
)
@Getter
@Setter
@IdClass(PlaylistTrackId.class)
public class PlaylistTrack {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id")
    private Track track;

    @Column(nullable = false)
    private Integer position;

    @Column(name = "added_at", nullable = false)
    private OffsetDateTime addedAt = OffsetDateTime.now();
}