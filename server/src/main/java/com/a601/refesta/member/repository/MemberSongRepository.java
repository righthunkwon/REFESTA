package com.a601.refesta.member.repository;

import com.a601.refesta.member.domain.join.MemberSongPreference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Transactional
public interface MemberSongRepository extends JpaRepository<MemberSongPreference, Long> {

    Optional<MemberSongPreference> findByMember_IdAndSong_Id(int memberId, int songId);

    void deleteAllByMemberId(int memberId);
}
