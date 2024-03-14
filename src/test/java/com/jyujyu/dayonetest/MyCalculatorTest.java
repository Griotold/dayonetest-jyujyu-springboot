package com.jyujyu.dayonetest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyCalculatorTest {

    @Test
    void addTest() {
        // Arrange - 준비
        MyCalculator myCalculator = new MyCalculator();

        // Act - 활동
        myCalculator.add(10.0);

        // Assert - 검증(단언)
        Assertions.assertEquals(10.0, myCalculator.getResult());
    }

    @Test
    void minus() {
        // Given - 준비 - Arrange
        MyCalculator myCalculator = new MyCalculator(10.0);

        // When - 행동 - Act
        myCalculator.minus(5.0);

        // Then - 검증/비교 - Assert
        Assertions.assertEquals(5.0, myCalculator.getResult());
    }

    @Test
    void multiply() {
        MyCalculator myCalculator = new MyCalculator(2.0);

        myCalculator.multiply(2.0);

        Assertions.assertEquals(4.0, myCalculator.getResult());
    }

    @Test
    void divide() {
        MyCalculator myCalculator = new MyCalculator(10.0);

        myCalculator.divide(2.0);

        Assertions.assertEquals(5.0, myCalculator.getResult());
    }

    @Test
    void complicatedCalculateTest() {
        // Given
        MyCalculator myCalculator = new MyCalculator(0.0);

        // When
        Double result = myCalculator
                .add(10.0)
                .minus(4.0)
                .multiply(2.0)
                .divide(3.0)
                .getResult();

        // Then
        Assertions.assertEquals(4.0, result);
    }

    @Test
    void divideZeroTest() {
        // given
        MyCalculator myCalculator = new MyCalculator(10.0);

        // when & then
        Assertions.assertThrows(MyCalculator.ZeroDivisionException.class,
                () -> myCalculator.divide(0.0));

    }
}