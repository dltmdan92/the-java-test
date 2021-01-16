package com.seungmoo.thejavatest.member;

import com.seungmoo.thejavatest.test.Study;

import java.util.Optional;

public interface MemberService {
    Optional<Member> findById(Long memberId);

    void validate(Long memberId);

    void notify(Study newStudy);

    void notify(Member member);
}
