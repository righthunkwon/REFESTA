package com.a601.refesta.member.repository;

import com.a601.refesta.member.domain.join.ArtistLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistLikeRepository extends JpaRepository<ArtistLike, Integer> {

    Optional<ArtistLike> findByMember_IdAndArtist_Id(Integer memberId, Integer artistId);
}
