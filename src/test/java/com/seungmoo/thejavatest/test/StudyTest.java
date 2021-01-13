package com.seungmoo.thejavatest.test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.*;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Convert;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

// 클래스 내 모든 메소드에 displayName 생성 strategy를 적용한다.
// ReplaceUnderscores.class : underscore를 공백으로
// 이거 보다는 @DisplayName 방식을 추천한다. 이거 보다는 @DisplauName이 더 우선순위를 가짐.
// junit-platform.properties 에서 junit.jupiter.displayname.generator.default = org.junit.jupiter.api.DisplayNameGenerator$ReplaceUnderscores로 대체 할 수 있다.
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
// JUnit5 부터 테스트 인스턴스를 class 당 만들 수 있다.
// junit-platform.properties에서도 셋팅 해줄 수 있음
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
// @Order라는 annotation을 통해 Test 코드의 실행 순서를 정해줄 수 있다.
// PER_CLASS와 같이 사용하게 되면 "상태값을 유지"하면서 순서대로 테스트 할 수 있다.
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudyTest {

    // 테스트 클래스에서 멤버변수를 선언하고 각 @Test 코드에서 멤버변수를 변경해도
    // 각각 의 테스트는 별개 인스턴스가 생성되어 실행되므로, 의존되지 않고 독립적으로 실행된다. (각 테스트 코드 간 영향 X)
    // But, JUnit5에서는 @TestInstance(Lifecycle.PER_CLASS) --> 이렇게 테스트 메소드가 하나의 인스턴스를 공유할 수 있게 해준다.
    int value = 10;

    /**
     * @TestInstance(TestInstance.Lifecycle.PER_CLASS) 가 적용된 테스트 클래스의 경우
     * @BeforeAll, @AfterAll 코드는 더이상 static이 필요가 없게 된다.
     * 첫 인스턴스가 생성 후 공유 되기 때문
     */
    @BeforeAll
    void beforeAll2() {
        System.out.println("before Test");
    }

    /**
     * JUnit5 부터는 public 안붙여도 된다.
     * reflection을 사용하기 때문에 굳이 public이 필요없기 때문이다.
     */
    @Test
    @DisplayName("스터디 만들기 \uD83D\uDE31") // 메소드에 DisplayName을 직접 설정 가능하다.
    @Tag("slow")
    @Order(1) // junit의 @Order를 써야 함.
    void create_new_study() {
        // 테스트 클래스 내의 각각의 this들은 모두 다르다.
        // 테스트 간의 의존성을 없애기 위해서
        // But JUnit5에서
        System.out.println(this);
        Study study = new Study(value++);
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
            new Study(value++);
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
            new Study(value);
            Thread.sleep(300);
        });

        System.out.println("create");

    }

    @Test
    //@Disabled // disable 처리
    @DisplayName("스터기 또 만들기 ╯°□°）╯")
    @Tag("slow")
    @Order(2)
    void create_new_study_again() {
        System.out.println(this);
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
            Study actual = new Study(value++);
            assertThat(actual.getLimit()).isGreaterThan(0);
        });

        Study actual = new Study(10);
        assertThat(actual.getLimit()).isGreaterThan(0);
        System.out.println("create1");
    }

    @Test
    @DisplayName("환경_별_enable_처리")
    @EnabledOnOs({OS.WINDOWS, OS.LINUX, OS.WINDOWS}) // 맥, 윈도우, 리눅스에서 활성화
    @EnabledOnJre({JRE.JAVA_8, JRE.JAVA_9, JRE.JAVA_10, JRE.JAVA_11}) // 자바 버전 별 enable 처리
    @EnabledIfEnvironmentVariable(named = "TEST_ENV", matches = "LOCAL")
    @Tag("slow")
    @Order(3)
    void create_new_study_again2() {
        System.out.println(this);
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
     *
     * 메서드에 인자 여러개 넣으려면 @CvsSource
     * @param message
     */
    @DisplayName("파라미터 갯수 만큼 반복해서 스터디 만들기")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @ValueSource(strings = {"날씨가", "많이", "추워지고", "있네요."})
    @EmptySource // 비어 있는 문자열 파라미터로 테스트 한번 더 추가
    @NullSource // null 파라미터로 테스트 한번 더 추가
    @NullAndEmptySource // @EmptySource + @NullSource Componsed Annotation
    void parameterizedTest(String message) {
        System.out.println(message);
    }

    //@DisplayName("인자 변환 테스트")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @ValueSource(ints = {10, 20, 40})
    void 인자_변환_테스트(@ConvertWith(StudyConveter.class) Study study) {
        // 인자 값을 변환 해주는 interface가 존재하기 때문에 이렇게 사용가능하다.
        System.out.println(study.getLimit());
    }

    static class StudyConveter extends SimpleArgumentConverter {
        @Override
        protected Object convert(Object o, Class<?> aClass) throws ArgumentConversionException {
            assertEquals(Study.class, aClass, "Can only convert to Study");
            return new Study(Integer.parseInt(o.toString()));
        }
    }

    @DisplayName("여러 인자 테스트")
    @ParameterizedTest(name = "{index} {displayName} message={0}")
    @CsvSource({"10, '자바 스터디'", "20, 스프링"})
    //void 여러_인자_테스트(Integer limit, String name) {
    void 여러_인자_테스트(@AggregateWith(StudyAggregator.class) Study study) {
        // 인자 값을 변환 해주는 interface가 존재하기 때문에 이렇게 사용가능하다.
        System.out.println(study);
    }

    // aggregator는 반드시 public class 또는 static inner class 이어야 한다.
    static class StudyAggregator implements ArgumentsAggregator {
        @Override
        public Object aggregateArguments(ArgumentsAccessor argumentsAccessor, ParameterContext parameterContext) throws ArgumentsAggregationException {
            return new Study(argumentsAccessor.getInteger(0), argumentsAccessor.getString(1));
        }
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