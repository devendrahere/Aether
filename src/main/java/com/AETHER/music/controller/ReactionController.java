package com.AETHER.music.controller;

import com.AETHER.music.auth.CustomUserDetails;
import com.AETHER.music.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/tracks/reaction")
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionService reactionService;

    @PostMapping("/{trackId}/like")
    public ResponseEntity<?> like(
            @PathVariable Long trackId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        if (user == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Unauthorized"));
        }

        reactionService.likeTrack(user.getId(), trackId);

        return ResponseEntity.ok(
                Map.of("status", "liked")
        );
    }

    @DeleteMapping("/{trackId}/like")
    public ResponseEntity<?> unlike(
            @PathVariable Long trackId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        if (user == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Unauthorized"));
        }

        reactionService.unlikeTrack(user.getId(), trackId);

        return ResponseEntity.ok(
                Map.of("status", "unliked")
        );
    }
}
