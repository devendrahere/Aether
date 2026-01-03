package com.AETHER.music.repository;

import com.AETHER.music.DTO.track.TrackSummaryDTO;
import com.AETHER.music.entity.ReactionType;
import com.AETHER.music.entity.UserTrackReaction;
import com.AETHER.music.entity.UserTrackReactionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTrackReactionRepository
        extends JpaRepository<UserTrackReaction, UserTrackReactionId> {

    // ===== write / existence checks (entities are OK here) =====
    boolean existsByUserIdAndTrackId(Long userId, Long trackId);

    Optional<UserTrackReaction> findByUserIdAndTrackId(Long userId, Long trackId);

    long countByTrackIdAndReactionType(Long trackId, ReactionType reactionType);

    // ===== read API: DTO projection (THIS FIXES YOUR CRASH) =====
    @Query("""
        select new com.AETHER.music.DTO.track.TrackSummaryDTO(
            t.id,
            t.title,
            t.durationSec
        )
        from UserTrackReaction r
        join r.track t
        where r.user.id = :userId
          and r.reactionType = :reaction
    """)
    List<TrackSummaryDTO> findLikedTrackSummaries(
            @Param("userId") Long userId,
            @Param("reaction") ReactionType reaction
    );
}
