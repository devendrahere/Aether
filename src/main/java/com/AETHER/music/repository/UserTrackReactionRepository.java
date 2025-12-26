package com.AETHER.music.repository;

import com.AETHER.music.entity.UserTrackReaction;
import com.AETHER.music.entity.UserTrackReactionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTrackReactionRepository
        extends JpaRepository<UserTrackReaction, UserTrackReactionId> {

    Optional<UserTrackReaction> findByUserIdAndTrackId(Long userId, Long trackId);

    long countByTrackIdAndReactionType(Long trackId, String reactionType);
}
