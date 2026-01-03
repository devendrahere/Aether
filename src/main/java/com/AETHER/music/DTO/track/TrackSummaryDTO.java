package com.AETHER.music.DTO.track;

import com.AETHER.music.DTO.artist.ArtistDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackSummaryDTO {
    public Long id;
    public String title;
    public Integer durationSec;
    public ArtistDTO artist;
}
