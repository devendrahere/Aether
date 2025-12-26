package com.AETHER.music.repository;

import com.AETHER.music.entity.ArtistGenre;
import com.AETHER.music.entity.ArtistGenreId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistGenreRepository
        extends JpaRepository<ArtistGenre, ArtistGenreId> {

    List<ArtistGenre> findByArtistId(Long artistId);
}