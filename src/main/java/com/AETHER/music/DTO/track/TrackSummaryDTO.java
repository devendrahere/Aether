package com.AETHER.music.DTO.track;

import com.AETHER.music.DTO.artist.ArtistDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrackSummaryDTO {

    private Long id;
    private String title;
    private Integer durationSec;
    private ArtistDTO artist;

    // 🔥 REQUIRED for JPQL projection
    public TrackSummaryDTO(Long id, String title, Integer durationSec) {
        this.id = id;
        this.title = title;
        this.durationSec = durationSec;
    }
}
