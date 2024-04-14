package com.a601.refesta.festival.repository;

import com.a601.refesta.festival.domain.FestivalDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FestivalDetailRepository extends JpaRepository<FestivalDetail, Integer> {

    Optional<FestivalDetail> findByFestival_Id(Integer festivalId);
}
