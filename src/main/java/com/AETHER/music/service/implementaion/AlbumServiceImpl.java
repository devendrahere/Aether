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
        dto.id = album.getId();
        dto.title = album.getTitle();
        dto.releaseYear = album.getReleaseYear();

        ArtistDTO artistDto = new ArtistDTO();
        artistDto.id = album.getArtist().getId();
        artistDto.name = album.getArtist().getName();
        artistDto.country = album.getArtist().getCountry();
        dto.artist = artistDto;

        dto.tracks = trackRepository.findByAlbumId(albumId)
                .stream()
                .map(t -> {
                    TrackSummaryDTO ts = new TrackSummaryDTO();
                    ts.id = t.getId();
                    ts.title = t.getTitle();
                    ts.durationSec = t.getDurationSec();
                    ts.artist = artistDto;
                    return ts;
                })
                .toList();

        return dto;
    }

    @Override
    public List<AlbumSummaryDTO> getAllAlbums() {
        return albumRepository.findAll()
                .stream()
                .map(album -> {
                    AlbumSummaryDTO dto = new AlbumSummaryDTO();
                    dto.id = album.getId();
                    dto.title = album.getTitle();
                    dto.releaseYear = album.getReleaseYear();
                    if (album.getArtist() != null) {
                        ArtistDTO artistDTO = new ArtistDTO();
                        artistDTO.id = album.getArtist().getId();
                        artistDTO.name = album.getArtist().getName();
                        artistDTO.country = album.getArtist().getCountry();

                        dto.artist = artistDTO;
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }
}