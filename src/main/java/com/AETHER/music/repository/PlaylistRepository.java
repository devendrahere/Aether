package com.AETHER.music.repository;

import com.AETHER.music.DTO.playlist.PlaylistSummaryDTO;
import com.AETHER.music.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    Optional<Playlist> findByIdAndDeletedAtIsNull(Long id);

    Optional<Playlist> findByIdAndOwnerIdAndDeletedAtIsNull(Long id, Long ownerId);

    @Query("""
    select new com.AETHER.music.DTO.playlist.PlaylistSummaryDTO(
        p.id,
        p.name,
        p.isPublic,
        count(pt.track.id)
    )
    from Playlist p
    left join PlaylistTrack pt on pt.playlist.id = p.id
    where p.owner.id = :userId
      and p.deletedAt is null
    group by p.id, p.name, p.isPublic
    order by p.createdAt desc
""")
    List<PlaylistSummaryDTO> findPlaylistSummaries(@Param("userId") Long userId);

    @Query("""
        select new com.AETHER.music.DTO.playlist.PlaylistSummaryDTO(
            p.id,
            p.name
        )
        from Playlist p
        where p.id = :playlistId
    """)
    PlaylistSummaryDTO findSummaryById(Long playlistId);
}