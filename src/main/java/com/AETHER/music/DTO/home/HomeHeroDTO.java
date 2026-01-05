package com.AETHER.music.DTO.home;

import com.AETHER.music.DTO.playlist.PlaylistSummaryDTO;
public record HomeHeroDTO(
        String greeting,
        PlaylistSummaryDTO featuredPlaylist
) {}

