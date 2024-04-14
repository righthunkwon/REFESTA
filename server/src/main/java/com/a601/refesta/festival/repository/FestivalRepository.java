package com.a601.refesta.festival.repository;

import com.a601.refesta.festival.domain.Festival;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FestivalRepository extends JpaRepository<Festival, Integer> {
}
