package com.AETHER.music.service.implementaion;

import com.AETHER.music.DTO.album.AlbumSummaryDTO;
import com.AETHER.music.DTO.artist.ArtistDTO;
import com.AETHER.music.DTO.track.TrackDetailDTO;
import com.AETHER.music.DTO.track.TrackSummaryDTO;
import com.AETHER.music.DTO.trackfile.TrackFileDTO;
import com.AETHER.music.entity.Album;
import com.AETHER.music.entity.Artist;
import com.AETHER.music.entity.Track;
import com.AETHER.music.exception.ResourceNotFoundException;
import com.AETHER.music.repository.TrackFileRepository;
import com.AETHER.music.repository.TrackRepository;
import com.AETHER.music.service.TrackService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class TrackServiceImpl implements TrackService {

    private final TrackRepository trackRepository;
    private final TrackFileRepository trackFileRepository;

    public TrackServiceImpl(
            TrackRepository trackRepository,
            TrackFileRepository trackFileRepository) {

        this.trackRepository = trackRepository;
        this.trackFileRepository = trackFileRepository;
    }

    @Override
    public TrackDetailDTO getTrack(Long trackId) {

        Track track = trackRepository.findById(trackId)
                .orElseThrow(() -> new ResourceNotFoundException("Track not found"));

        TrackDetailDTO dto = new TrackDetailDTO();
        dto.setId(track.getId());
        dto.setTitle(track.getTitle());
        dto.setDurationSec(track.getDurationSec());

        // Artists (FIX)
        dto.setArtists(
                track.getArtists()
                        .stream()
                        .map(a -> new ArtistDTO(
                                a.getId(),
                                a.getName(),
                                a.getCountry()
                        ))
                        .toList()
        );

        // Album (optional)
        if (track.getAlbum() != null) {
            Album album = track.getAlbum();

            AlbumSummaryDTO albumDto = new AlbumSummaryDTO();
            albumDto.setId(album.getId());
            albumDto.setTitle(album.getTitle());
            albumDto.setReleaseYear(album.getReleaseYear());

            dto.setAlbum(albumDto);
        }

        // Available files
        dto.setAvailableFiles(
                trackFileRepository.findByTrackId(trackId)
                        .stream()
                        .map(tf -> {
                            TrackFileDTO f = new TrackFileDTO();
                            f.setQuality(tf.getQuality());
                            f.setCodec(tf.getCodec());
                            f.setFileSizeBytes(tf.getFileSizeBytes());
                            return f;
                        })
                        .toList()
        );

        return dto;
    }

    @Override
    public List<TrackSummaryDTO> search(String query) {

        return trackRepository.search(query)
                .stream()
                .map(track -> {

                    TrackSummaryDTO dto = new TrackSummaryDTO();
                    dto.setId(track.getId());
                    dto.setTitle(track.getTitle());
                    dto.setDurationSec(track.getDurationSec());

                    dto.setArtists(
                            track.getArtists()
                                    .stream()
                                    .map(a -> new ArtistDTO(
                                            a.getId(),
                                            a.getName(),
                                            a.getCountry()
                                    ))
                                    .toList()
                    );

                    return dto;
                })
                .toList();
    }

    @Override
    public List<TrackSummaryDTO> getAllTracks() {

        return trackRepository.findAll()
                .stream()
                .map(track -> {

                    TrackSummaryDTO dto = new TrackSummaryDTO();
                    dto.setId(track.getId());
                    dto.setTitle(track.getTitle());
                    dto.setDurationSec(track.getDurationSec());

                    // Artists (FIX)
                    dto.setArtists(
                            track.getArtists()
                                    .stream()
                                    .map(a -> new ArtistDTO(
                                            a.getId(),
                                            a.getName(),
                                            a.getCountry()
                                    ))
                                    .toList()
                    );

                    return dto;
                })
                .toList();
    }
}