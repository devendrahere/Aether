package com.AETHER.music.service;

import com.AETHER.music.DTO.playlist.PlaylistCreateRequestDTO;
import com.AETHER.music.DTO.playlist.PlaylistDetailDTO;

public interface PlaylistService {

    PlaylistDetailDTO getPlaylist(Long playlistId);

    void createPlaylist(Long userId, PlaylistCreateRequestDTO dto);
}