package com.AETHER.music.DTO.home;

import java.util.List;

public record HomeResponseDTO(
        HomeHeroDTO hero,
        List<HomeSectionDTO> sections
) {}
