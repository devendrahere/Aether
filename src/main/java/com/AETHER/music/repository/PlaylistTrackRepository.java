package com.AETHER.music.repository;

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

    void deleteByPlaylistIdAndTrackId(Long playlistId, Long trackId);

    @Query("""
        select new com.AETHER.music.DTO.playlist.PlaylistTrackDTO(
            pt.position,
            new com.AETHER.music.DTO.track.TrackSummaryDTO(
                t.id,
                t.title,
                t.durationSec
            )
        )
        from PlaylistTrack pt
        join pt.track t
        where pt.playlist.id = :playlistId
        order by pt.position asc
    """)
    List<PlaylistTrackDTO> findPlaylistTrackDTOs(Long playlistId);
}

