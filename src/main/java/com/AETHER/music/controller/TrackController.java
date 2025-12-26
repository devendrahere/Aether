package com.AETHER.music.controller;

import com.AETHER.music.DTO.track.TrackDetailDTO;
import com.AETHER.music.DTO.track.TrackSummaryDTO;
import com.AETHER.music.service.TrackService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/track")
public class TrackController {
    private final TrackService trackService;
    public TrackController(TrackService trackService){
        this.trackService=trackService;
    }

    @GetMapping("/{id}")
    public TrackDetailDTO getTrack(@PathVariable Long id){
        return  trackService.getTrack(id);
    }

    @GetMapping("/search")
    public List<TrackSummaryDTO> search(@RequestParam String name){
        return trackService.search(name);
    }
}
