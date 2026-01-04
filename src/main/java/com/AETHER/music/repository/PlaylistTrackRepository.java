package com.AETHER.music.repository;

import com.AETHER.music.DTO.playlist.PlaylistSummaryDTO;
import com.AETHER.music.DTO.playlist.PlaylistTrackDTO;
import com.AETHER.music.entity.PlaylistTrack;
import com.AETHER.music.entity.PlaylistTrackId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistTrackRepository
        extends JpaRepository<PlaylistTrack, PlaylistTrackId> {

    boolean existsByPlaylistIdAndTrackId(Long playlistId, Long trackId);

    void deleteByPlaylistIdAndTrackId(Long playlistId, Long trackId);

    @Query("""
        SELECT new com.AETHER.music.DTO.playlist.PlaylistTrackDTO(
            t.id,
            t.title,
            t.durationSec,
            a.id,
            a.name
        )
        FROM PlaylistTrack pt
        JOIN pt.track t
        JOIN t.artist a
        WHERE pt.playlist.id = :playlistId
    """)
    List<PlaylistTrackDTO> findPlaylistTrackDTOs(Long playlistId);

    int countByPlaylistId(Long playlistId);
}


