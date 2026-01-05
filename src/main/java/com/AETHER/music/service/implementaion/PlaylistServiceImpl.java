package com.AETHER.music.service.implementaion;

import com.AETHER.music.DTO.playlist.PlaylistCreateRequestDTO;
import com.AETHER.music.DTO.playlist.PlaylistDetailDTO;
import com.AETHER.music.DTO.playlist.PlaylistSummaryDTO;
import com.AETHER.music.DTO.playlist.PlaylistTrackDTO;
import com.AETHER.music.DTO.track.TrackSummaryDTO;
import com.AETHER.music.entity.Playlist;
import com.AETHER.music.entity.PlaylistTrack;
import com.AETHER.music.entity.Track;
import com.AETHER.music.entity.User;
import com.AETHER.music.exception.ResourceNotFoundException;
import com.AETHER.music.exception.UnauthorizedException;
import com.AETHER.music.repository.PlaylistRepository;
import com.AETHER.music.repository.PlaylistTrackRepository;
import com.AETHER.music.service.PlaylistService;
import com.AETHER.music.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;

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
    @Transactional(readOnly = true)
    public PlaylistDetailDTO getPlaylist(Long playlistId) {

        Playlist playlist = playlistRepository.findByIdAndDeletedAtIsNull(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found"));

        PlaylistDetailDTO dto = new PlaylistDetailDTO();
        dto.setId(playlist.getId());
        dto.setName(playlist.getName());
        dto.setDescription(playlist.getDescription());
        dto.setPublic(playlist.isPublic());

        dto.setTracks(
                playlistTrackRepository
                        .findWithTrackAndArtistsByPlaylistId(playlistId)
                        .stream()
                        .map(pt -> {
                            Track t = pt.getTrack();

                            PlaylistTrackDTO trackDto = new PlaylistTrackDTO();
                            trackDto.setId(t.getId());
                            trackDto.setTitle(t.getTitle());
                            trackDto.setDurationSec(t.getDurationSec());
                            trackDto.setPosition(pt.getPosition());

                            // 🔥 MULTIPLE ARTISTS (FIX)
                            trackDto.setArtists(
                                    t.getArtists()
                                            .stream()
                                            .map(a -> new com.AETHER.music.DTO.artist.ArtistDTO(
                                                    a.getId(),
                                                    a.getName(),
                                                    a.getCountry()
                                            ))
                                            .toList()
                            );

                            return trackDto;
                        })
                        .toList()
        );

        return dto;
    }


    @Override
    @Transactional(readOnly = true)
    public List<PlaylistSummaryDTO> getUserPlaylists(Long userId) {
        return playlistRepository.findPlaylistSummaries(userId);
    }

    @Override
    public void addTrack(Long userId, Long playlistId, Long trackId) {

        Playlist playlist = playlistRepository
                .findByIdAndOwnerIdAndDeletedAtIsNull(playlistId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found"));

        if (playlistTrackRepository.existsByPlaylistIdAndTrackId(playlistId, trackId)) {
            return; // already exists, idempotent
        }

        int nextPosition =
                playlistTrackRepository.countByPlaylistId(playlistId) + 1;

        PlaylistTrack pt = new PlaylistTrack();
        pt.setPlaylist(playlist);
        pt.setTrack(new Track(trackId));
        pt.setPosition(nextPosition);
        pt.setAddedAt(OffsetDateTime.now());

        playlistTrackRepository.save(pt);
    }



    @Override
    public void removeTrack(Long userId, Long playlistId, Long trackId) {

        playlistRepository
                .findByIdAndOwnerIdAndDeletedAtIsNull(playlistId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found"));

        playlistTrackRepository.deleteByPlaylistIdAndTrackId(playlistId, trackId);
    }

    @Override
    @Transactional
    public void deletePlaylist(Long userId, Long playlistId) {

        Playlist playlist = playlistRepository.findByIdAndDeletedAtIsNull(playlistId)
                .orElseThrow(() -> new ResourceNotFoundException("Playlist not found"));

        if (!playlist.getOwner().getId().equals(userId)) {
            throw new UnauthorizedException("You do not own this playlist");
        }

        playlist.setDeletedAt(OffsetDateTime.now());
        playlistRepository.save(playlist);
    }

}
