package com.AETHER.music.repository;

import com.AETHER.music.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    List<Playlist> findByOwnerIdAndDeletedAtIsNull(Long ownerId);

    Optional<Playlist> findByIdAndDeletedAtIsNull(Long id);
}
