package com.seungmoo.thejavatest.study;

import com.seungmoo.thejavatest.member.Member;
import com.seungmoo.thejavatest.member.MemberService;
import com.seungmoo.thejavatest.test.Study;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@Testcontainers
public class StudyServiceTest2 {
    @Mock
    MemberService memberService;

    @Mock
    StudyRepository studyRepository;

    // static으로 안만들 경우, 클래스 내의 모든 테스트마다 container를 생성하게 된다.
    // dockerImageName 넣는 부분은 좀 더 봐야함. 기존에 도커에 생성된 image 이름을 넣어줘야 하나봄. (호환되게 끔)
    @Container
    static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres").withDatabaseName("springdata_test");

   // 테스트 시작할 때 컨테이너를 띄움.
   @BeforeAll
   static void beforeAll() {
       /**
        * 주의 할 점. postgresContainer를 띄울 때 컨테이너의 엔드포인트를 명시해줘야 한다. (DB url 등 properties에 있는 DB 정보 명시)
        * 명시 안해주면 자기 맘대로 아무데서나 띄워지기 때문에 테스트 수행이 불가함.
        */
       postgreSQLContainer.start();
       System.out.println(postgreSQLContainer.getJdbcUrl());
   }

   // 테스트 종료될 때 컨테이너를 내림.
   @AfterAll
   static void afterAll() {
       postgreSQLContainer.stop();
   }

   @BeforeEach
   void beforeEach() {
       // 각각의 테스트 시작할 떄마다 데이터 delete 해준다.
       studyRepository.deleteAll();
   }

    @Test
    void createNewStudy() {
        System.out.println("testcontainer를 사용해서 도커 컨테이너 자동으로 띄우기 성공!!!");
        // Given
        StudyService studyService = new StudyService(memberService, studyRepository);
        assertNotNull(studyService);

        Member member = new Member();
        member.setId(1L);
        member.setEmail("seungmoo@email.com");

        Study study = new Study(10, "테스트");

        when(memberService.findById(1L)).thenReturn(Optional.of(member));
        when(studyRepository.save(study)).thenReturn(study);

        // when 대신에 BDD의 given을 사용할 수도 있다. (BDDMockito의 given을 사용하면 간단하게 작업할 수 있다.)
        given(memberService.findById(1L)).willReturn(Optional.of(member));
        given(studyRepository.save(study)).willReturn(study);

        // When
        studyService.createNewStudy(1L, study);

        // Then
        assertEquals(member.getId(), study.getOwnerId());
        //verify(memberService, times(1)).notify(study);
        // verify를 BDD의 then으로 만들어보자, BDDMockito의 then을 사용한다.
        then(memberService).should(times(1)).notify(study);
        then(memberService).should(times(1)).notify(member);

        //verifyNoMoreInteractions(memberService);
        // verifyNoMoreInteractions --> BDD의 then으로 사용
        then(memberService).shouldHaveNoMoreInteractions();
    }
}
