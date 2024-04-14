package com.a601.refesta.recommendation.repository;

import com.a601.refesta.recommendation.domain.MemberFestivalPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberFestivalPreferenceRepository extends JpaRepository<MemberFestivalPreference, Integer> {

    Optional<MemberFestivalPreference> findByMember_IdAndFestival_Id(int memberId, int festivalId);
}
