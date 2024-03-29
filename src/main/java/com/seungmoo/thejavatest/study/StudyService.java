package com.seungmoo.thejavatest.study;

import com.seungmoo.thejavatest.domain.Member;
import com.seungmoo.thejavatest.member.MemberService;
import com.seungmoo.thejavatest.domain.Study;

import java.util.Optional;

public class StudyService {
    private final MemberService memberService;
    private final StudyRepository repository;

    public StudyService(MemberService memberService, StudyRepository repository) {
        assert memberService != null;
        assert repository != null;
        this.memberService = memberService;
        this.repository = repository;
    }

    public Study createNewStudy(Long memberId, Study study) {
        Optional<Member> member = memberService.findById(memberId);
        if (member.isEmpty()) {
            throw new IllegalArgumentException("Member doesn't exist for id: '" + memberId +"'");
        }
        study.setOwnerId(memberId);
        Study newStudy = repository.save(study);
        memberService.notify(newStudy);
        memberService.notify(member.get());
        return newStudy;
    }

    public Study openStudy(Study study) {
        study.open();
        Study openedStudy = repository.save(study);
        memberService.notify(openedStudy);
        return openedStudy;
    }

}
