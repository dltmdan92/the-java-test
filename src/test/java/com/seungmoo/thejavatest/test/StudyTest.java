package com.seungmoo.thejavatest.test;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class StudyTest {

    /**
     * JUnit5 부터는 public 안붙여도 된다.
     * reflection을 사용하기 때문에 굳이 public이 필요없기 때문이다.
     */
    @Test
    void create() {
        Study study = new Study();
        assertNotNull(study);
        System.out.println("create");
    }

    @Test
    @Disabled // disable 처리
    void create1() {
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