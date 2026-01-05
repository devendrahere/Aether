package com.AETHER.music.service.implementaion;

import com.AETHER.music.DTO.album.AlbumDetailDTO;
import com.AETHER.music.DTO.album.AlbumSummaryDTO;
import com.AETHER.music.DTO.artist.ArtistDTO;
import com.AETHER.music.DTO.track.TrackSummaryDTO;
import com.AETHER.music.entity.Album;
import com.AETHER.music.exception.ResourceNotFoundException;
import com.AETHER.music.repository.AlbumRepository;
import com.AETHER.music.repository.TrackRepository;
import com.AETHER.music.service.AlbumService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepository albumRepository;
    private final TrackRepository trackRepository;

    public AlbumServiceImpl(AlbumRepository albumRepository,
                            TrackRepository trackRepository) {
        this.albumRepository = albumRepository;
        this.trackRepository = trackRepository;
    }

    @Override
    public AlbumDetailDTO getAlbum(Long albumId) {

        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new ResourceNotFoundException("Album not found"));

        AlbumDetailDTO dto = new AlbumDetailDTO();
        dto.setId(album.getId());
        dto.setTitle(album.getTitle());
        dto.setReleaseYear(album.getReleaseYear());

        // Album artist (still singular – correct)
        ArtistDTO albumArtist = new ArtistDTO(
                album.getArtist().getId(),
                album.getArtist().getName(),
                album.getArtist().getCountry()
        );
        dto.setArtist(albumArtist);
        dto.setTracks(
                trackRepository.findByAlbumId(albumId)
                        .stream()
                        .map(track -> {
                            TrackSummaryDTO ts = new TrackSummaryDTO();
                            ts.setId(track.getId());
                            ts.setTitle(track.getTitle());
                            ts.setDurationSec(track.getDurationSec());

                            ts.setArtists(
                                    track.getArtists()
                                            .stream()
                                            .map(a -> new ArtistDTO(
                                                    a.getId(),
                                                    a.getName(),
                                                    a.getCountry()
                                            ))
                                            .toList()
                            );

                            return ts;
                        })
                        .toList()
        );

        return dto;
    }


    @Override
    public List<AlbumSummaryDTO> getAllAlbums() {

        return albumRepository.findAll()
                .stream()
                .map(album -> {

                    AlbumSummaryDTO dto = new AlbumSummaryDTO();
                    dto.setId(album.getId());
                    dto.setTitle(album.getTitle());
                    dto.setReleaseYear(album.getReleaseYear());

                    if (album.getArtist() != null) {
                        dto.setArtist(
                                new ArtistDTO(
                                        album.getArtist().getId(),
                                        album.getArtist().getName(),
                                        album.getArtist().getCountry()
                                )
                        );
                    }

                    return dto;
                })
                .toList();
    }
}