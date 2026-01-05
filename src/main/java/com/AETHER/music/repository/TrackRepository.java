package com.AETHER.music.repository;

import com.AETHER.music.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.AETHER.music.DTO.track.TrackSummaryDTO;


import java.util.List;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {

    List<Track> findByAlbumId(Long albumId);

    List<Track> findByArtists_Id(Long artistId);

    @Query("""
        select distinct t
        from Track t
        join fetch t.artists
        where lower(t.title) like lower(concat('%', :q, '%'))
    """)
    List<Track> search(@Param("q") String query);

    @Query("""
    select distinct t
    from Track t
    join fetch t.artists
    where t.id in :ids
""")
    List<Track> findWithArtistsByIdIn(@Param("ids") List<Long> ids);
}