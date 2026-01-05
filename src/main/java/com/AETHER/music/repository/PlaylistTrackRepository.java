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

    boolean existsByPlaylistIdAndTrackId(Long playlistId, Long trackId);

    void deleteByPlaylistIdAndTrackId(Long playlistId, Long trackId);

    int countByPlaylistId(Long playlistId);

    @Query("""
        select distinct pt
        from PlaylistTrack pt
        join fetch pt.track t
        join fetch t.artists
        where pt.playlist.id = :playlistId
        order by pt.position asc
    """)
    List<PlaylistTrack> findWithTrackAndArtistsByPlaylistId(Long playlistId);
}
