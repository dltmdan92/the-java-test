package com.seungmoo.thejavatest.study;

import com.seungmoo.thejavatest.member.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class StudyServiceTest2 {
    @Mock
    MemberService memberService;

    @Autowired
    StudyRepository studyRepository;

    @Test
    void createNewStudy() {

    }
}
