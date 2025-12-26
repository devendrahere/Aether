package com.AETHER.music.repository;

import com.AETHER.music.entity.PlayEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayEventRepository extends JpaRepository<PlayEvent, Long> {
}
