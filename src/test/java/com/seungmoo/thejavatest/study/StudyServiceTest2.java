package com.seungmoo.thejavatest.study;

import com.seungmoo.thejavatest.member.Member;
import com.seungmoo.thejavatest.member.MemberService;
import com.seungmoo.thejavatest.test.Study;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@Slf4j
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
    //@Container
    //static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres").withDatabaseName("springdata_test");

    // 일반적인 컨테이너 만들기
    // 위의 DB Container의 DB 특화된 메서드는 제공되지 않는다. withEnv로 충분히 확장 사용 가능하다.
    @Container
    static GenericContainer postgreSQLContainer = new GenericContainer("postgres")
            .withEnv("POSTGRES_DB", "springdata_test")
            // 지금 버전에서는 아래에 postgres_password가 설정되지 않으면 컨테이너가 띄워지지 않음.
            .withEnv("POSTGRES_PASSWORD", "1568919am!")
            // 컨테이너 포트를 선언한다. (testcontainer는 host post, 즉 mapping 해주는 포트는 선언할 수 없다.)
            // mapping port를 참조할 수는 있다.
            .withExposedPorts(5432)
            ;

   // 테스트 시작할 때 컨테이너를 띄움.
   @BeforeAll
   static void beforeAll() {
       /**
        * 주의 할 점. postgresContainer를 띄울 때 컨테이너의 엔드포인트를 명시해줘야 한다. (DB url 등 properties에 있는 DB 정보 명시)
        * 명시 안해주면 자기 맘대로 아무데서나 띄워지기 때문에 테스트 수행이 불가함.
        */
       postgreSQLContainer.start();
       //System.out.println(postgreSQLContainer.getJdbcUrl());

       Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(log);
       postgreSQLContainer.followOutput(logConsumer);
   }

   // 테스트 종료될 때 컨테이너를 내림.
   @AfterAll
   static void afterAll() {
       postgreSQLContainer.stop();
   }

   @BeforeEach
   void beforeEach() {
       System.out.println("==========");
       System.out.println(postgreSQLContainer.getMappedPort(5432));
       System.out.println(postgreSQLContainer.getLogs());

       // 각각의 테스트 시작할 떄마다 데이터 delete 해준다.
       studyRepository.deleteAll();
   }

   @DisplayName("testContainers를 사용해서 도커 자동으로 띄워봅시다.")
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
