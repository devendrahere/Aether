package com.AETHER.music.service.implementaion;

import com.AETHER.music.DTO.playlist.PlaylistCreateRequestDTO;
import com.AETHER.music.DTO.playlist.PlaylistDetailDTO;
import com.AETHER.music.DTO.playlist.PlaylistTrackDTO;
import com.AETHER.music.DTO.track.TrackSummaryDTO;
import com.AETHER.music.entity.Playlist;
import com.AETHER.music.entity.Track;
import com.AETHER.music.entity.User;
import com.AETHER.music.repository.PlaylistRepository;
import com.AETHER.music.repository.PlaylistTrackRepository;
import com.AETHER.music.service.PlaylistService;
import com.AETHER.music.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PlaylistServiceImpl implements PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final PlaylistTrackRepository playlistTrackRepository;
    private final UserService userService;

    public PlaylistServiceImpl(PlaylistRepository playlistRepository,
                               PlaylistTrackRepository playlistTrackRepository,
                               UserService userService) {
        this.playlistRepository = playlistRepository;
        this.playlistTrackRepository = playlistTrackRepository;
        this.userService = userService;
    }

    @Override
    public void createPlaylist(Long userId, PlaylistCreateRequestDTO dto) {

        User owner = userService.getById(userId);

        Playlist playlist = new Playlist();
        playlist.setOwner(owner);
        playlist.setName(dto.name);
        playlist.setDescription(dto.description);
        playlist.setPublic(dto.isPublic);

        playlistRepository.save(playlist);
    }

    @Override
    public PlaylistDetailDTO getPlaylist(Long playlistId) {

        Playlist playlist = playlistRepository.findByIdAndDeletedAtIsNull(playlistId)
                .orElseThrow(() -> new IllegalArgumentException("Playlist not found"));

        PlaylistDetailDTO dto = new PlaylistDetailDTO();
        dto.id = playlist.getId();
        dto.name = playlist.getName();
        dto.description = playlist.getDescription();
        dto.isPublic = playlist.isPublic();

        dto.tracks = playlistTrackRepository
                .findByPlaylistIdOrderByPositionAsc(playlistId)
                .stream()
                .map(pt -> {
                    PlaylistTrackDTO ptd = new PlaylistTrackDTO();
                    ptd.position = pt.getPosition();

                    Track t = pt.getTrack();
                    TrackSummaryDTO ts = new TrackSummaryDTO();
                    ts.id = t.getId();
                    ts.title = t.getTitle();
                    ts.durationSec = t.getDurationSec();

                    ptd.track = ts;
                    return ptd;
                })
                .toList();

        return dto;
    }
}
