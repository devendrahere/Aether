package com.AETHER.music.controller;

import com.AETHER.music.DTO.playlist.PlaylistCreateRequestDTO;
import com.AETHER.music.DTO.playlist.PlaylistDetailDTO;
import com.AETHER.music.auth.CustomUserDetails;
import com.AETHER.music.service.PlaylistService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/playlists")
public class PlaylistController {
    private final PlaylistService playlistService;
    public PlaylistController(PlaylistService playlistService){
        this.playlistService=playlistService;
    }

    @PostMapping
    public void createPlaylist(
            @AuthenticationPrincipal CustomUserDetails user,
            @Valid @RequestBody PlaylistCreateRequestDTO dto
    ) {
        playlistService.createPlaylist(user.getUser().getId(), dto);
    }


    @GetMapping("/{id}")
    public PlaylistDetailDTO getPlaylist(@PathVariable Long id){
        return playlistService.getPlaylist(id);
    }
}
