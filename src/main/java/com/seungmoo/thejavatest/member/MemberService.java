package com.seungmoo.thejavatest.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberService {
    Optional<Member> findById(Long memberId);

    void validate(Long memberId);
}
