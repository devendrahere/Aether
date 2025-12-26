package com.AETHER.music.controller;

import com.AETHER.music.DTO.album.AlbumDetailDTO;
import com.AETHER.music.service.AlbumService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/album")
public class AlbumController {
    private final AlbumService albumService;

    public AlbumController(AlbumService albumService){
        this.albumService=albumService;
    }
    @GetMapping("/{id}")
    public AlbumDetailDTO getAlbum(@PathVariable Long id){
        return albumService.getAlbum(id);
    }
}
