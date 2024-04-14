package com.a601.refesta.member.repository;

import com.a601.refesta.member.domain.Member;
import com.a601.refesta.member.domain.join.MemberGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface MemberGenreRepository extends JpaRepository<MemberGenre, Integer> {

    void deleteAllByMember(Member member);
}
