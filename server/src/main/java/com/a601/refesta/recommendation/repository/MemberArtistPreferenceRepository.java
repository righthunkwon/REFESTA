package com.a601.refesta.recommendation.repository;

import com.a601.refesta.recommendation.domain.MemberArtistPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberArtistPreferenceRepository extends JpaRepository<MemberArtistPreference, Integer> {

    Optional<MemberArtistPreference> findByMember_IdAndArtist_Id(int memberId, int festivalId);
}
