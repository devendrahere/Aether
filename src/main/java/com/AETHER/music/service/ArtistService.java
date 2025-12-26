package com.AETHER.music.service;

import com.AETHER.music.DTO.artist.ArtistDTO;

import java.util.List;

public interface ArtistService {

    List<ArtistDTO> search(String query);
}
