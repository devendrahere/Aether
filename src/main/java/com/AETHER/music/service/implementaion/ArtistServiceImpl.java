package com.AETHER.music.service.implementaion;

import com.AETHER.music.DTO.artist.ArtistDTO;
import com.AETHER.music.repository.ArtistRepository;
import com.AETHER.music.service.ArtistService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ArtistServiceImpl implements ArtistService {

    private final ArtistRepository artistRepository;

    public ArtistServiceImpl(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    public List<ArtistDTO> search(String query) {

        return artistRepository.findByNameContainingIgnoreCase(query)
                .stream()
                .map(a -> {
                    ArtistDTO dto = new ArtistDTO();
                    dto.id = a.getId();
                    dto.name = a.getName();
                    dto.country = a.getCountry();
                    return dto;
                })
                .toList();
    }
}

