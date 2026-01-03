package com.AETHER.music.controller;

import com.AETHER.music.DTO.playevent.PlayEventRequestDTO;
import com.AETHER.music.auth.CustomUserDetails;
import com.AETHER.music.service.PlayEventService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/play-event")
public class PlayEventController {
    private final PlayEventService playEventService;

    public PlayEventController(PlayEventService playEventService){
        this.playEventService=playEventService;
    }

    @PostMapping
    public void record(
            Authentication authentication,
            @RequestBody PlayEventRequestDTO dto
    ){
        Long userId = null;

        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails user =
                    (CustomUserDetails) authentication.getPrincipal();
            userId = user.getUser().getId();
        }

        playEventService.record(dto, userId);
    }

}
