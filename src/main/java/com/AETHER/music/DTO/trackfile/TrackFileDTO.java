package com.AETHER.music.DTO.trackfile;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrackFileDTO {
    public String quality;
    public String codec;
    public Long fileSizeBytes;
}
