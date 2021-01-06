package com.seungmoo.thejavatest.test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

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
    @Tag("slow")
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
    @Tag("slow")
    void create_new_study_again() {
        // System 통해서 환경 변수 꺼내기
        String test_env = System.getenv("TEST_ENV");
        System.out.println(test_env);
        // 여기서 test 실패할 경우 아래 assert는 실행이 안되고 Test가 끝난다.
        assumeTrue("LOCAL".equalsIgnoreCase(test_env));

        assumingThat("LOCAL".equalsIgnoreCase(test_env), () -> {
            System.out.println("local");
            Study actual = new Study(100);
            assertThat(actual.getLimit()).isGreaterThan(0);
        });

        assumingThat("seungmoo".equalsIgnoreCase(test_env), () -> {
            System.out.println("seungmoo");
            Study actual = new Study(10);
            assertThat(actual.getLimit()).isGreaterThan(0);
        });

        Study actual = new Study(10);
        assertThat(actual.getLimit()).isGreaterThan(0);
        System.out.println("create1");
    }

    @Test
    @DisplayName("환경 별 enable 처리")
    @EnabledOnOs({OS.WINDOWS, OS.LINUX, OS.WINDOWS}) // 맥, 윈도우, 리눅스에서 활성화
    @EnabledOnJre({JRE.JAVA_8, JRE.JAVA_9, JRE.JAVA_10, JRE.JAVA_11}) // 자바 버전 별 enable 처리
    @EnabledIfEnvironmentVariable(named = "TEST_ENV", matches = "LOCAL")
    @Tag("slow")
    void create_new_study_again2() {
        String test_env = System.getenv("TEST_ENV");
        System.out.println(test_env);
        Study actual = new Study(100);
        assertThat(actual.getLimit()).isGreaterThan(0);
    }

    /**
     * Enabled --> 요 처리는 Test class의 전체 @Test들을 돌릴 때 적용되는 거임.
     */
    @Test
    @DisplayName("얘는 실행 안함")
    @EnabledOnOs(OS.OTHER)
    @EnabledOnJre(JRE.OTHER)
    @EnabledIfEnvironmentVariable(named = "TEST_ENV", matches = "seungmoo")
    @Tag("slow")
    void do_not_run() {
        String test_env = System.getenv("TEST_ENV");
        System.out.println(test_env);
        Study actual = new Study(100);
        assertThat(actual.getLimit()).isGreaterThan(0);
    }

    /**
     * intellij 에서 edit configuration에서 tags에 실행할 tag name을 셋팅해주면
     * 그것만 실행해준다.
     */
    //@Test
    @DisplayName("스터디 만들기 with fast tag")
    //@Tag("fast") // 태깅
    @FastTest // Composed 애노테이션을 사용해주는 것이 좋다.
    void create_new_study_with_fast_tag() {
        String test_env = System.getenv("TEST_ENV");
        System.out.println(test_env);
        Study actual = new Study(100);
        assertThat(actual.getLimit()).isGreaterThan(0);
    }

    //@Test
    @DisplayName("스터디 만들기 with slow tag")
    //@Tag("slow") // 태깅
    @SlowTest
    void create_new_study_with_slow_tag() {
        String test_env = System.getenv("TEST_ENV");
        System.out.println(test_env);
        Study actual = new Study(100);
        assertThat(actual.getLimit()).isGreaterThan(0);
    }

    /**
     * 테스트 반복하기
     */
    @DisplayName("반복해서 스터디 만들기")
    // @RepeatedTest 을 통해 총 반복 횟수, 각 반복 스텝 별 Name을 만들어 준다.
    @RepeatedTest(value = 10, name = "{displayName}, {currentRepetition} / {totalRepetition}")
    void repeatTest(RepetitionInfo repetitionInfo) {
        // RepetitionInfo 를 파라미터로 받아서
        // 현재 반복 횟수와 총 반복 횟수를 구할 수 있다.
        System.out.println("test " + repetitionInfo.getCurrentRepetition() + "/" + repetitionInfo.getTotalRepetitions());
    }

    /**
     * 파라미터의 갯수 만큼 실행되는 테스트
     * 정의된 ValueSource의 파라미터 별로, Test 메서드의 파라미터가 달라진다.
     * @param message
     */
    @DisplayName("파라미터 갯수 만큼 반복해서 스터디 만들기")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @ValueSource(strings = {"날씨가", "많이", "추워지고", "있네요."})
    void parameterizedTest(String message) {
        System.out.println(message);
    }

    /**R
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