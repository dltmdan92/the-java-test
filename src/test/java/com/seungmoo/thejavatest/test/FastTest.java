package com.seungmoo.thejavatest.test;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Composed Annotation
 * - 다른 여러 개의 애노테이션을 조합해서 하나의 Composed Custom Annotation을 만들었다.
 */
// 이 애노테이션을 사용하는 코드가 이 애노테이션 정보를 RUNTIME할 때 까지 들고 있어야 한다.
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Test
@Tag("fast")
public @interface FastTest {
}
