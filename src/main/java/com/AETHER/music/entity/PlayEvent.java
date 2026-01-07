package com.AETHER.music.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "play_events",
        indexes = {
                @Index(
                        name = "idx_play_events_user_time",
                        columnList = "user_id, event_time desc"
                ),
                @Index(
                        name = "idx_play_events_user_playlist_time",
                        columnList = "user_id, playlist_id, event_time desc"
                ),
                @Index(
                        name = "idx_play_events_user_album_time",
                        columnList = "user_id, album_id, event_time desc"
                )
        }
)
@Getter
@Setter
public class PlayEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", nullable = false)
    private UUID sessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id", nullable = false)
    private Track track;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id")
    private Playlist playlist; // nullable

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 20)
    private PlayEventType eventType;

    @Column(name = "event_time", nullable = false, updatable = false)
    private OffsetDateTime eventTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private Album album; // nullable


    @PrePersist
    void onCreate() {
        this.eventTime = OffsetDateTime.now();
    }
}

