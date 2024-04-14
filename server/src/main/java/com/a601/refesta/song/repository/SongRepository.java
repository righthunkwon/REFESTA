package com.a601.refesta.song.repository;

import com.a601.refesta.song.domain.Song;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SongRepository extends JpaRepository<Song, Integer> {
    @Query(value = "SELECT s FROM Song s WHERE s.audioUrl IS NOT NULL AND s.audioUrl LIKE :videoId")
    Optional<Song> findByVideoId(@Param("videoId") String videoId);
}
