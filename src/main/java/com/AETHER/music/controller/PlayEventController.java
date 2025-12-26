package com.AETHER.music.controller;

import com.AETHER.music.DTO.playevent.PlayEventRequestDTO;
import com.AETHER.music.service.PlayEventService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/play-event")
public class PlayEventController {
    private final PlayEventService playEventService;

    public PlayEventController(PlayEventService playEventService){
        this.playEventService=playEventService;
    }

    @PostMapping
    public void record(
            @RequestParam(required = false) Long userId,
            @RequestBody PlayEventRequestDTO dto
    ){
        playEventService.record(dto,userId);
    }
}
