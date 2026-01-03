package com.AETHER.music.mapper;

import com.AETHER.music.DTO.artist.ArtistDTO;
import com.AETHER.music.entity.Artist;
import org.springframework.stereotype.Component;

@Component
public class ArtistMapper {

    public ArtistDTO toDTO(Artist artist) {
        if (artist == null) return null;

        ArtistDTO dto = new ArtistDTO();
        dto.id = artist.getId();
        dto.name = artist.getName();
        dto.country = artist.getCountry();

        return dto;
    }
}
