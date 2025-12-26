package com.AETHER.music.service;

import com.AETHER.music.DTO.album.AlbumDetailDTO;

public interface AlbumService {

    AlbumDetailDTO getAlbum(Long albumId);
}