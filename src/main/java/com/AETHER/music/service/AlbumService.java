package com.AETHER.music.service;

import com.AETHER.music.DTO.album.AlbumDetailDTO;
import com.AETHER.music.DTO.album.AlbumSummaryDTO;

import java.util.List;

public interface AlbumService {

    AlbumDetailDTO getAlbum(Long albumId);

    List<AlbumSummaryDTO> getAllAlbums();
}