package com.AETHER.music.repository;

import com.AETHER.music.entity.PlaylistTrack;
import com.AETHER.music.entity.PlaylistTrackId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaylistTrackRepository
        extends JpaRepository<PlaylistTrack, PlaylistTrackId> {

    List<PlaylistTrack> findByPlaylistIdOrderByPositionAsc(Long playlistId);

    void deleteByPlaylistIdAndTrackId(Long playlistId, Long trackId);
}
