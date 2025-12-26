package com.AETHER.music.repository;

import com.AETHER.music.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    Optional<Artist> findByDiscogsId(Long discogsId);

    List<Artist> findByNameContainingIgnoreCase(String name);
}

