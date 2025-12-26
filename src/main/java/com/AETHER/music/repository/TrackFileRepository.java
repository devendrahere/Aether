package com.AETHER.music.repository;

import com.AETHER.music.entity.TrackFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrackFileRepository extends JpaRepository<TrackFile, Long> {

    List<TrackFile> findByTrackId(Long trackId);

    Optional<TrackFile> findByTrackIdAndQuality(Long trackId, String quality);
}
