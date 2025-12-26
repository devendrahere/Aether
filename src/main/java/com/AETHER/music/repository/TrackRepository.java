package com.AETHER.music.repository;

import com.AETHER.music.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {

    List<Track> findByAlbumId(Long albumId);

    List<Track> findByArtistId(Long artistId);

    @Query("""
        select t from Track t
        join fetch t.artist
        where lower(t.title) like lower(concat('%', :q, '%'))
    """)
    List<Track> search(@Param("q") String query);
}
