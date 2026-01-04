package com.AETHER.music.repository;

import com.AETHER.music.DTO.playlist.PlaylistSummaryDTO;
import com.AETHER.music.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    List<Playlist> findByOwnerIdAndDeletedAtIsNull(Long ownerId);

    Optional<Playlist> findByIdAndDeletedAtIsNull(Long id);

    Optional<Playlist> findByIdAndOwnerIdAndDeletedAtIsNull(Long id, Long ownerId);

    @Query("""
        SELECT new com.AETHER.music.DTO.playlist.PlaylistSummaryDTO(
            p.id,
            p.name,
            p.isPublic,
            COUNT(pt.track.id)
        )
        FROM Playlist p
        LEFT JOIN PlaylistTrack pt ON pt.playlist.id = p.id
        WHERE p.owner.id = :userId
          AND p.deletedAt IS NULL
        GROUP BY p.id
    """)
    List<PlaylistSummaryDTO> findPlaylistSummaries(Long userId);
}
