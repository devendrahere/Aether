package com.AETHER.music.service;

import com.AETHER.music.DTO.home.HomeResponseDTO;

public interface HomeService {
    HomeResponseDTO getHome(Long userId);
}
