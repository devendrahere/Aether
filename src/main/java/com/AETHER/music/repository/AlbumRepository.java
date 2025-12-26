package com.AETHER.music.repository;

import com.AETHER.music.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    List<Album> findByArtistId(Long artistId);

    Optional<Album> findByDiscogsId(Long discogsId);
}