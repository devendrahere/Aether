package com.AETHER.music.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "artists")
@Getter
@Setter
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "discogs_id", unique = true)
    private Long discogsId;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 100)
    private String country;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

    // getters/setters
}