package com.jyujyu.dayonetest.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class SaveExamScoreRequest {
    private final String studentName;
    private final Integer korScore;
    private final Integer englishScore;
    private final Integer mathScore;
}
