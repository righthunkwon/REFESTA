package com.a601.refesta.member.repository;

import com.a601.refesta.member.domain.join.FestivalLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FestivalLikeRepository extends JpaRepository<FestivalLike, Integer> {

    Optional<FestivalLike> findByMember_IdAndFestival_Id(Integer memberId, Integer festivalId);
}
