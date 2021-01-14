package com.seungmoo.thejavatest.test;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit vintage 엔진으로 실행된다.
 *
 * junit-vintage-engine을 의존성으로 추가하면, JUnit 5의 junit-platform으로 JUnit 3과 4로 작성된 테스트를 실행할 수 있다.
 * •	@Rule은 기본적으로 지원하지 않지만, junit-jupiter-migrationsupport 모듈이 제공하는 @EnableRuleMigrationSupport를 사용하면 다음 타입의 Rule을 지원한다.
 * o	ExternalResource
 * o	Verifier
 * o	ExpectedException
 *
 * JUnit5으로 바꾸고 나면 확실히 Tool에서 보기 좋다. (@DisplayName 덕분에 한글 지원도 괜춘)
 * 그리고 더이상 @RunWith을 안써도 된다.
 *
*/
public class StudyJUnit4Test {
    @Before
    public void before() {
        System.out.println("before");
    }

    @Test
    public void createTest() {
        System.out.println("test");
    }

    @Test
    public void createTest2() {
        System.out.println("test2");
    }
}
