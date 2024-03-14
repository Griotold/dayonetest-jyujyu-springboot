package com.jyujyu.dayonetest;

import lombok.Getter;

@Getter
public class MyCalculator {
    private Double result;

    public MyCalculator() {
        this.result = 0.0;
    }

    public MyCalculator(Double initResult) {
        this.result = initResult;
    }

    public MyCalculator add(Double number) {
        this.result += number;
        return this;
    }

    public MyCalculator minus(Double number) {
        this.result -= number;
        return this;
    }

    public MyCalculator multiply(Double number) {
        this.result *= number;
        return this;
    }

    public MyCalculator divide(Double number) {
        if (number == 0.0) {
            throw new ZeroDivisionException();
        }
        this.result /= number;
        return this;
    }

    public static class ZeroDivisionException extends RuntimeException {

    }

}

