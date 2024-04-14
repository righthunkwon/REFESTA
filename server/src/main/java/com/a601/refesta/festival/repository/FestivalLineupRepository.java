package com.a601.refesta.festival.repository;

import com.a601.refesta.festival.domain.join.FestivalLineup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FestivalLineupRepository extends JpaRepository<FestivalLineup, Integer> {
}
