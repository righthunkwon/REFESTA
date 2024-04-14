package com.a601.refesta.member.repository;

import com.a601.refesta.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    Member findByGoogleId(String googleId);
}
