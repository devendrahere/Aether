package com.AETHER.music.DTO.home;

public record HomeHeroContextDTO(
        HomeHeroContextType type,
        Long id,
        String title
//        Integer trackCount
) {}
