package com.jyujyu.dayonetest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test; // 스프링부트 3점대 버전 // 이하 버전은 AssertJ인데 jupiter 사용 권장

public class LombokTestDataTest {

    @Test
    public void testDataTest() {
        TestData testData = new TestData();

        testData.setName("jyujyu");
        Assertions.assertEquals("jyujyu", testData.getName());
    }
}
