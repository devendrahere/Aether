package com.AETHER.music.repository;

import com.AETHER.music.DTO.playlist.PlaylistSummaryDTO;
import com.AETHER.music.entity.PlayEvent;
import com.AETHER.music.entity.Playlist;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface PlayEventRepository extends JpaRepository<PlayEvent, Long> {

    @Query("""
    select pe.track.id
    from PlayEvent pe
    where pe.user.id = :userId
      and pe.eventType = com.AETHER.music.entity.PlayEventType.PLAY
    group by pe.track.id
    order by max(pe.eventTime) desc
""")
    List<Long> findRecentDistinctTrackIdsByUser(
            @Param("userId") Long userId,
            Pageable pageable
    );

    @Query("""
        select pe.track.id
        from PlayEvent pe
        where pe.eventType = com.AETHER.music.entity.PlayEventType.PLAY
        group by pe.track.id
        order by count(pe.id) desc
    """)
    List<Long> findTopPlayedTrackIds(Pageable pageable);

    @Query("""
    select pe.track.id
    from PlayEvent pe
    where pe.eventType = com.AETHER.music.entity.PlayEventType.PLAY
      and pe.eventTime >= :since
    group by pe.track.id
    order by count(pe.id) desc
""")
    List<Long> findTrendingTrackIdsSince(
            @Param("since") OffsetDateTime since,
            Pageable pageable
    );

    @Query("""
    select new com.AETHER.music.DTO.playlist.PlaylistSummaryDTO(
        p.id,
        p.name,
        p.isPublic,
        count(distinct pe.track.id)
    )
    from PlayEvent pe
    join pe.playlist p
    where pe.user.id = :userId
      and pe.eventType = com.AETHER.music.entity.PlayEventType.PLAY
      and pe.playlist is not null
    group by p.id, p.name, p.isPublic
    order by max(pe.eventTime) desc
""")
    List<PlaylistSummaryDTO> findRecentlyPlayedPlaylistSummary(
            @Param("userId") Long userId,
            Pageable pageable
    );
}