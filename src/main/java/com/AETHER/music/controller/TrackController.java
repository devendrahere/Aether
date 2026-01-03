package com.AETHER.music.controller;

import com.AETHER.music.DTO.track.TrackDetailDTO;
import com.AETHER.music.DTO.track.TrackSummaryDTO;
import com.AETHER.music.service.TrackService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tracks")
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
    @GetMapping
    public List<TrackSummaryDTO> getAllTracks() {
        return trackService.getAllTracks();
    }

}
