package com.a601.refesta.artist.repository;

import com.a601.refesta.artist.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Integer> {
}
