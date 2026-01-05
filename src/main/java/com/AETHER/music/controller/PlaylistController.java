package com.AETHER.music.controller;

import com.AETHER.music.DTO.playlist.PlaylistCreateRequestDTO;
import com.AETHER.music.DTO.playlist.PlaylistDetailDTO;
import com.AETHER.music.DTO.playlist.PlaylistSummaryDTO;
import com.AETHER.music.auth.CustomUserDetails;
import com.AETHER.music.service.PlaylistService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @PostMapping
    public ResponseEntity<?> createPlaylist(
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody PlaylistCreateRequestDTO dto
    ) {
        playlistService.createPlaylist(user.getId(), dto);
        return ResponseEntity.status(201)
                .body(Map.of("status", "created"));
    }

    @GetMapping("/{id}")
    public PlaylistDetailDTO getPlaylist(@PathVariable Long id) {
        return playlistService.getPlaylist(id);
    }

    @GetMapping
    public List<PlaylistSummaryDTO> myPlaylists(
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        return playlistService.getUserPlaylists(user.getId());
    }

    @PostMapping("/{playlistId}/tracks/{trackId}")
    public ResponseEntity<?> addTrack(
            @PathVariable Long playlistId,
            @PathVariable Long trackId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        playlistService.addTrack(
                user.getId(),
                playlistId,
                trackId
        );

        return ResponseEntity.ok(
                Map.of(
                        "status", "added",
                        "playlistId", playlistId,
                        "trackId", trackId
                )
        );
    }

    @DeleteMapping("/{playlistId}/tracks/{trackId}")
    public ResponseEntity<?> removeTrack(
            @PathVariable Long playlistId,
            @PathVariable Long trackId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        playlistService.removeTrack(
                user.getId(),
                playlistId,
                trackId
        );

        return ResponseEntity.ok(
                Map.of(
                        "status", "removed",
                        "playlistId", playlistId,
                        "trackId", trackId
                )
        );
    }

    @DeleteMapping("/{playlistId}")
    public ResponseEntity<?> deletePlaylist(
            @PathVariable Long playlistId,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        playlistService.deletePlaylist(
                user.getId(),
                playlistId
        );

        return ResponseEntity.ok(
                Map.of(
                        "status", "deleted",
                        "playlistId", playlistId
                )
        );
    }

}