package com.AETHER.music.DTO.home;

import java.util.List;

public record HomeHeroDTO(
        String greeting,
        List<HomeHeroContextDTO> context
) {}
