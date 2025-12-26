package com.AETHER.music.controller;

import com.AETHER.music.DTO.artist.ArtistDTO;
import com.AETHER.music.service.ArtistService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@RequestMapping("/artist")
public class ArtistController {
    private final ArtistService artistService;

    public ArtistController(ArtistService artistService){
        this.artistService = artistService;
    }

    @GetMapping("/search")
    public List<ArtistDTO> search(@RequestParam String s){
        return artistService.search(s);
    }
}
