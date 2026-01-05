package com.AETHER.music.controller;

import com.AETHER.music.DTO.home.HomeResponseDTO;
import com.AETHER.music.auth.CustomUserDetails;
import com.AETHER.music.service.HomeService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class HomeController {

    private final HomeService homeService;

    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping
    public HomeResponseDTO getHome(Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return homeService.getHome(null);
        }

        CustomUserDetails user =
                (CustomUserDetails) authentication.getPrincipal();

        return homeService.getHome(user.getId());
    }
}