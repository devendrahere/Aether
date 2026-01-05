package com.AETHER.music.DTO.track;

import com.AETHER.music.DTO.artist.ArtistDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrackSummaryDTO {

    private Long id;
    private String title;
    private Integer durationSec;

    private List<ArtistDTO> artists;

    public TrackSummaryDTO(Long id, String title, Integer durationSec) {
        this.id = id;
        this.title = title;
        this.durationSec = durationSec;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TrackSummaryDTO)) return false;
        TrackSummaryDTO that = (TrackSummaryDTO) o;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
