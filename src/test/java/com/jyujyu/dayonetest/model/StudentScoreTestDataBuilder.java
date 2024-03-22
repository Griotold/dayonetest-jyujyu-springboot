package com.jyujyu.dayonetest.model;

/**
 * 테스트 데이터 빌더 패턴 -> 빌더를 리턴
 * 테스트시 자유롭게 오버라이드해서 사용할 수 있어서 유연하다
 */
public class StudentScoreTestDataBuilder {

    public static StudentScore.StudentScoreBuilder passed() {
        return StudentScore
                .builder()
                .korScore(80)
                .englishScore(100)
                .mathScore(90)
                .studentName("defaultName")
                .exam("defaultExam");
    }

    public static StudentScore.StudentScoreBuilder failed() {
        return StudentScore
                .builder()
                .korScore(50)
                .englishScore(40)
                .mathScore(30)
                .studentName("defaultName")
                .exam("defaultExam");
    }


}
