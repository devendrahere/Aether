package com.AETHER.music.controller;

import com.AETHER.music.DTO.album.AlbumDetailDTO;
import com.AETHER.music.DTO.album.AlbumSummaryDTO;
import com.AETHER.music.service.AlbumService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/albums")
public class AlbumController {
    private final AlbumService albumService;

    public AlbumController(AlbumService albumService){
        this.albumService=albumService;
    }
    @GetMapping("/{id}")
    public AlbumDetailDTO getAlbum(@PathVariable Long id){
        return albumService.getAlbum(id);
    }
    @GetMapping
    public List<AlbumSummaryDTO> getAlbums() {
        return albumService.getAllAlbums();
    }

}
