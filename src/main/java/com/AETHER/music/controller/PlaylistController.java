package com.AETHER.music.controller;

import com.AETHER.music.DTO.playlist.PlaylistCreateRequestDTO;
import com.AETHER.music.DTO.playlist.PlaylistDetailDTO;
import com.AETHER.music.service.PlaylistService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/playlists")
public class PlaylistController {
    private final PlaylistService playlistService;
    public PlaylistController(PlaylistService playlistService){
        this.playlistService=playlistService;
    }

    @PostMapping
    public void creatplaylist(
            @RequestParam Long userId,
            @Valid @RequestBody PlaylistCreateRequestDTO dto
    ){
        playlistService.createPlaylist(userId,dto);
    }

    @GetMapping("/{id}")
    public PlaylistDetailDTO gtePlaylist(@PathVariable Long id){
        return playlistService.getPlaylist(id);
    }
}
