package com.seungmoo.thejavatest.member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberService {
    Optional<Member> findById(Long memberId);
}
