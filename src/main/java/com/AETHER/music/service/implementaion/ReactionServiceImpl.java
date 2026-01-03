package com.AETHER.music.service.implementaion;

import com.AETHER.music.DTO.track.TrackSummaryDTO;
import com.AETHER.music.entity.ReactionType;
import com.AETHER.music.entity.UserTrackReaction;
import com.AETHER.music.entity.UserTrackReactionId;
import com.AETHER.music.repository.TrackRepository;
import com.AETHER.music.repository.UserRepository;
import com.AETHER.music.repository.UserTrackReactionRepository;
import com.AETHER.music.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReactionServiceImpl implements ReactionService {

    private final UserTrackReactionRepository reactionRepo;
    private final UserRepository userRepo;
    private final TrackRepository trackRepo;

    @Override
    @Transactional
    public void likeTrack(Long userId, Long trackId) {

        UserTrackReactionId id = new UserTrackReactionId();
        id.setUserId(userId);
        id.setTrackId(trackId);

        UserTrackReaction reaction = reactionRepo.findById(id)
                .orElseGet(() -> {
                    UserTrackReaction r = new UserTrackReaction();
                    r.setId(id); // 🔥 THIS WAS MISSING
                    r.setUser(userRepo.getReferenceById(userId));
                    r.setTrack(trackRepo.getReferenceById(trackId));
                    return r;
                });

        reaction.setReactionType(ReactionType.LIKE);
        reactionRepo.save(reaction);
    }

    @Override
    public void unlikeTrack(Long userId, Long trackId) {
        reactionRepo.findByUserIdAndTrackId(userId, trackId)
                .ifPresent(reactionRepo::delete);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrackSummaryDTO> getLikedTracks(Long userId) {
        return reactionRepo.findLikedTrackSummaries(userId, ReactionType.LIKE);
    }

}
