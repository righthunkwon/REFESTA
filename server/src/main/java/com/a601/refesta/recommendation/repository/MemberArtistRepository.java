package com.a601.refesta.recommendation.repository;

import com.a601.refesta.recommendation.domain.MemberArtist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberArtistRepository extends JpaRepository<MemberArtist, Integer> {

    Page<MemberArtist> findAllByMember_Id(Integer memberId, Pageable pageable);
}
