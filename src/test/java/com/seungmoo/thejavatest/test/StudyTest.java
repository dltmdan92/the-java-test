package com.seungmoo.thejavatest.test;

import org.junit.jupiter.api.*;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

// 클래스 내 모든 메소드에 displayName 생성 strategy를 적용한다.
// ReplaceUnderscores.class : underscore를 공백으로
// 이거 보다는 @DisplayName 방식을 추천한다.
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class StudyTest {

    /**
     * JUnit5 부터는 public 안붙여도 된다.
     * reflection을 사용하기 때문에 굳이 public이 필요없기 때문이다.
     */
    @Test
    @DisplayName("스터디 만들기 \uD83D\uDE31") // 메소드에 DisplayName을 직접 설정 가능하다.
    void create_new_study() {

        Study study = new Study(10);
        study.setStatus(StudyStatus.DRAFT);

        // 코드를 실행했을 때, 내가 기대하는 Exception이 발생하는지 테스트
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new Study(-10));
        String message = exception.getMessage();

        // assert에는 message를 정의해줄 수 있다.
        //assertEquals(StudyStatus.DRAFT, study.getStatus(), "스터디를 처음 만들면 상태값이 DRAFT여야 한다.");
        // (디버깅할 때 편리), Supplier 기반 람다식으로 정의해줄 수 있다.
        // 람다식으로 넣어주면 문자열 연산을 최적화 해준다. (테스트 성공했을 때는 문자열 연산 안함.)

        // assert문들을 람다표현식으로 assertAll에 묶어 주면, 안에 있는 assert들을 한번에 실행해주기 때문에
        // Test fail 여부를 한 눈에 알아볼 수 있다.
        assertAll(
                () -> assertNotNull(study),
                () -> assertEquals(StudyStatus.DRAFT, study.getStatus(),
                        () -> "스터디를 처음 만들면 상태값이 "+ StudyStatus.DRAFT +"여야 한다."),
                () -> assertTrue(study.getLimit() > 0, "스터디 최대 참석 가능 인원은 0보다 커야 한다."),
                () -> assertEquals(message, "limit는 0보다 커야 한다.")
        );

        /**
         * 타임아웃 체크를 할 수 있다.
         * but Test내 다른 assert에서도 assertTimeout이 끝나길 기다리게 된다.
         * Thread.sleep(3000)을 걸어놨으면 3000 millis가 끝날 때 까지 테스트가 대기함.
         */
        assertTimeout(Duration.ofMillis(100), () -> {
            new Study(10);
            Thread.sleep(300);
        });

        /**
         * 위의 assertTimeout과는 다르게 테스트가 기다리지 않음.
         * - 별도의 쓰레드에서 실행함.
         * [Warning!!!] ThreadLocal을 사용하는 부분이 있으면, 예상치 못한 결과가 발생하게 된다.
         * 스프링 트랜잭션 설정이 제대로 적용안될 수 있다.
         * 트랜잭션이 설정된 쓰레드와는 별개의 쓰레드에서 실행될 수 있다.
         */
        assertTimeoutPreemptively(Duration.ofMillis(100), () -> {
            new Study(10);
            Thread.sleep(300);
        });

        System.out.println("create");

    }

    @Test
    //@Disabled // disable 처리
    @DisplayName("스터기 또 만들기 ╯°□°）╯")
    void create_new_study_again() {
        System.out.println("create1");
    }

    /**
     * @BeforeAll 클래스 안에 있는 테스트가 실행되기 전에 딱 한번 실행
     * static void로 작성해야 됨.
     */
    @BeforeAll
    static void beforeAll() {
        System.out.println("before all");
    }

    /**
     * @AfterAll 모든 테스트가 실행된 후에 딱 한번 실행
     * static void로 작성해야 됨.
     */
    @AfterAll
    static void afterAll() {
        System.out.println("after all");
    }

    @BeforeEach
    void beforeEach() {
        System.out.println("before each");
    }

    @AfterEach
    void afterEach() {
        System.out.println("after each");
    }
}