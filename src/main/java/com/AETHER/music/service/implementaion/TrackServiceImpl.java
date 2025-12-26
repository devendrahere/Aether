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
        dto.id = track.getId();
        dto.title = track.getTitle();
        dto.durationSec = track.getDurationSec();

        // Artist
        Artist artist = track.getArtist();
        ArtistDTO artistDto = new ArtistDTO();
        artistDto.id = artist.getId();
        artistDto.name = artist.getName();
        artistDto.country = artist.getCountry();
        dto.artist = artistDto;

        // Album (optional)
        if (track.getAlbum() != null) {
            Album album = track.getAlbum();
            AlbumSummaryDTO albumDto = new AlbumSummaryDTO();
            albumDto.id = album.getId();
            albumDto.title = album.getTitle();
            albumDto.releaseYear = album.getReleaseYear();
            albumDto.artist = artistDto;
            dto.album = albumDto;
        }

        // Available files (qualities)
        dto.availableFiles = trackFileRepository.findByTrackId(trackId)
                .stream()
                .map(tf -> {
                    TrackFileDTO f = new TrackFileDTO();
                    f.quality = tf.getQuality();
                    f.codec = tf.getCodec();
                    f.fileSizeBytes = tf.getFileSizeBytes();
                    return f;
                })
                .toList();

        return dto;
    }

    @Override
    public List<TrackSummaryDTO> search(String query) {

        return trackRepository.search(query)
                .stream()
                .map(track -> {
                    TrackSummaryDTO dto = new TrackSummaryDTO();
                    dto.id = track.getId();
                    dto.title = track.getTitle();
                    dto.durationSec = track.getDurationSec();

                    Artist artist = track.getArtist();
                    ArtistDTO artistDto = new ArtistDTO();
                    artistDto.id = artist.getId();
                    artistDto.name = artist.getName();
                    artistDto.country = artist.getCountry();
                    dto.artist = artistDto;

                    return dto;
                })
                .toList();
    }
}
