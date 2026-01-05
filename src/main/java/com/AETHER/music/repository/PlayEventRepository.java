package com.AETHER.music.repository;

import com.AETHER.music.entity.PlayEvent;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}