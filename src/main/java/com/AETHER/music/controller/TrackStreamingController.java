package com.AETHER.music.controller;

import com.AETHER.music.service.implementaion.TrackStreamingService;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/tracks")
public class TrackStreamingController {

    private final TrackStreamingService streamingService;

    public TrackStreamingController(TrackStreamingService streamingService) {
        this.streamingService = streamingService;
    }

    @GetMapping("/{trackId}/stream")
    public ResponseEntity<ResourceRegion> stream(
            @PathVariable Long trackId,
            @RequestParam(defaultValue = "HIGH") String quality,
            @RequestHeader(value = "Range", required = false) String range
    ) throws IOException {
        return streamingService.stream(trackId, quality, range);
    }
}