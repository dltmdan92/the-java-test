package com.seungmoo.thejavatest.test;

import org.junit.jupiter.api.*;

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
        Study study = new Study();
        assertNotNull(study);
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