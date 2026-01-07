package com.AETHER.music.controller;

import com.AETHER.music.DTO.playevent.PlayEventRequestDTO;
import com.AETHER.music.auth.CustomUserDetails;
import com.AETHER.music.service.PlayEventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void record(
            Authentication authentication,
            @Valid @RequestBody PlayEventRequestDTO dto
    ) {
        System.out.println(">>> PlayEventController HIT");

        Long userId = null;

        if (authentication != null &&
                authentication.getPrincipal() instanceof CustomUserDetails user) {

            userId = user.getId();
        }

        playEventService.record(dto, userId);
    }

}
