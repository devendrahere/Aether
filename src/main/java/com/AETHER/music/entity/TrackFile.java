package com.AETHER.music.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;

@Entity
@Table(
        name = "track_files",
        uniqueConstraints = @UniqueConstraint(columnNames = {"track_id", "quality"})
)
public class TrackFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id", nullable = false)
    private Track track;

    @Column(nullable = false, length = 20)
    private String quality;

    @Column(nullable = false, length = 20)
    private String codec;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    // getters/setters
}
