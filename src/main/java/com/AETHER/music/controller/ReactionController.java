package com.AETHER.music.controller;

import com.AETHER.music.auth.CustomUserDetails;
import com.AETHER.music.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tracks/reaction")
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionService reactionService;

    @PostMapping("/{trackId}/like")
    public ResponseEntity<Void> like(
            @PathVariable Long trackId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        reactionService.likeTrack(user.getId(), trackId);
        return ResponseEntity.ok().build();
    }



    @DeleteMapping("/{trackId}/like")
    public ResponseEntity<Void> unlike(
            @PathVariable Long trackId,
            org.springframework.security.core.Authentication authentication
    ) {
        CustomUserDetails user =
                (CustomUserDetails) authentication.getPrincipal();

        reactionService.unlikeTrack(user.getId(), trackId);
        return ResponseEntity.noContent().build();
    }
}
