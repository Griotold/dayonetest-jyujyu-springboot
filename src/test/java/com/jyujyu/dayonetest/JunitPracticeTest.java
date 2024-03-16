package com.jyujyu.dayonetest;


import org.junit.jupiter.api.*;

import java.util.List;
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class JunitPracticeTest {

    // 언더스코어가 띄어쓰기가 된다.
    @Test
    public void assert_equals_test() {
        String expect = "something";
        String actual = "something";

        Assertions.assertEquals(expect, actual);
    }
    @DisplayName("Assert Not Equals 메소드 테스트")
    @Test
    public void assertNotEqualsTest() {
        String expect = "something";
        String actual = "Hello";

        Assertions.assertNotEquals(expect, actual);
    }

    @Test
    public void assertTrueTest() {
        Integer a = 10;
        Integer b = 10;

        Assertions.assertEquals(a, b);
    }

    @Test
    public void assertFalseTest() {
        Integer a = 10;
        Integer b = 20;

        Assertions.assertNotEquals(a, b);
    }

    @Test
    public void assertThrowsTest() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            throw new RuntimeException("임의로 발생시킨 에러");
        });
    }

    @Test
    public void assertNotNullTest() {
        String value = "Hello";
        Assertions.assertNotNull(value);
    }

    @Test
    public void assertNullTest() {
        String value = null;
        Assertions.assertNull(value);
    }

    @Test
    public void assertIterableEquals() {
        List<Integer> list1 = List.of(1, 2);
        List<Integer> list2 = List.of(1, 2);

        Assertions.assertIterableEquals(list1, list2);
    }

    @Test
    public void assertAllTest() {
        String expect = "something";
        String actual = "something";

        List<Integer> list1 = List.of(1, 2);
        List<Integer> list2 = List.of(1, 2);

        Assertions.assertIterableEquals(list1, list2);

        Assertions.assertAll("Assert All", List.of(
                () -> {
                    Assertions.assertEquals(expect, actual);
                },
                () -> {
                    Assertions.assertIterableEquals(list1, list2);
                }
        ));
    }
}
