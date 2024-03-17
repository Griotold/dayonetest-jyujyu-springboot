package com.jyujyu.dayonetest.controller.response;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class ExamFailStudentResponse {
    private final String studentName;
    private final Double avgScore;
}
