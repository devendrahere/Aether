package com.AETHER.music.service;

import com.AETHER.music.DTO.playlist.PlaylistCreateRequestDTO;
import com.AETHER.music.DTO.playlist.PlaylistDetailDTO;
import com.AETHER.music.DTO.playlist.PlaylistSummaryDTO;

import java.util.List;

public interface PlaylistService {

    PlaylistDetailDTO getPlaylist(Long playlistId);

    void createPlaylist(Long userId, PlaylistCreateRequestDTO dto);

    List<PlaylistSummaryDTO> getUserPlaylists(Long userId);

    void addTrack(Long userId, Long playlistId, Long trackId);

    void removeTrack(Long userId, Long playlistId, Long trackId);

    void deletePlaylist(Long userId, Long playlistId);
}