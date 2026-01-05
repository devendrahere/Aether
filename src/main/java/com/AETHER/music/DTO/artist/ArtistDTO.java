package com.AETHER.music.DTO.artist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ArtistDTO {
    private Long id;
    private String name;
    private String country;
}
