package com.AETHER.music.controller;

import com.AETHER.music.DTO.track.TrackSummaryDTO;
import com.AETHER.music.auth.CustomUserDetails;
import com.AETHER.music.mapper.TrackMapper;
import com.AETHER.music.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class UserLibraryController {

    private final ReactionService reactionService;
    private final TrackMapper trackMapper;

    @GetMapping("/liked-tracks")
    public List<TrackSummaryDTO> likedTracks(
            @AuthenticationPrincipal CustomUserDetails user) {

        return reactionService.getLikedTracks(user.getId());
    }
}