package com.AETHER.music.repository;

import com.AETHER.music.entity.ReactionType;
import com.AETHER.music.entity.UserTrackReaction;
import com.AETHER.music.entity.UserTrackReactionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface UserTrackReactionRepository
        extends JpaRepository<UserTrackReaction, UserTrackReactionId> {

    boolean existsByUserIdAndTrackId(Long userId, Long trackId);

    Optional<UserTrackReaction> findByUserIdAndTrackId(Long userId, Long trackId);

    List<UserTrackReaction> findByUserIdAndReactionType(Long userId, ReactionType reactionType);

    long countByTrackIdAndReactionType(Long trackId, ReactionType reactionType);
}
