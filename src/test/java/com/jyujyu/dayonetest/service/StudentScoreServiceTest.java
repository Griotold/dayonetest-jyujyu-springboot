package com.jyujyu.dayonetest.service;

import com.jyujyu.dayonetest.MyCalculator;
import com.jyujyu.dayonetest.controller.response.ExamFailStudentResponse;
import com.jyujyu.dayonetest.controller.response.ExamPassStudentResponse;
import com.jyujyu.dayonetest.model.*;
import com.jyujyu.dayonetest.repository.StudentFailRepository;
import com.jyujyu.dayonetest.repository.StudentPassRepository;
import com.jyujyu.dayonetest.repository.StudentScoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class StudentScoreServiceTest {
    private StudentScoreService studentScoreService;
    private StudentScoreRepository studentScoreRepository;
    private StudentPassRepository studentPassRepository;
    private StudentFailRepository studentFailRepository;

    @BeforeEach
    public void beforeEach() {
        studentScoreRepository = Mockito.mock(StudentScoreRepository.class);
        studentPassRepository = Mockito.mock(StudentPassRepository.class);
        studentFailRepository = Mockito.mock(StudentFailRepository.class);
        studentScoreService = new StudentScoreService(
                studentScoreRepository,
                studentPassRepository,
                studentFailRepository
        );
    }

    @Test
    @DisplayName("첫번째 Mock 테스트")
    public void firstSaveScoreMockTest() {
        // given
        String givenStudentName = "jyujyu";
        String givenExam = "testexam";
        Integer givenKorScore = 80;
        Integer givenEnglishScore = 100;
        Integer givenMathScore = 60;

        // when
        studentScoreService.saveScore(
                givenStudentName,
                givenExam,
                givenKorScore,
                givenEnglishScore,
                givenMathScore
        );
    }

    @Test
    @DisplayName("성적 저장 로직 검증 / 60점 이상인 경우")
    void saveScoreMockTest() throws Exception {
        // given
        String givenStudentName = "jyujyu";
        String givenExam = "testexam";
        Integer givenKorScore = 80;
        Integer givenEnglishScore = 100;
        Integer givenMathScore = 60;

        // when
        studentScoreService.saveScore(
                givenStudentName,
                givenExam,
                givenKorScore,
                givenEnglishScore,
                givenMathScore
        );

        // then
        // 우선, studentScoreRepository 는 무조건 save 가 1번 호출되어야 하고
        // 평균 점수가 60점 이상이니까 studentPassRepository 의 save 가 1번 호출되어야 한다.
        // studentFailRepository 의 save 는 호출되어선 안된다.
        Mockito.verify(studentScoreRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(studentPassRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(studentFailRepository, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    @DisplayName("성적 저장 로직 검증 / 60점 미만인 경우")
    void saveScoreMockTest_under60() throws Exception {
        // given
        String givenStudentName = "jyujyu";
        String givenExam = "testexam";
        Integer givenKorScore = 60;
        Integer givenEnglishScore = 50;
        Integer givenMathScore = 30;

        // when
        studentScoreService.saveScore(
                givenStudentName,
                givenExam,
                givenKorScore,
                givenEnglishScore,
                givenMathScore
        );

        // then
        // 우선, studentScoreRepository 는 무조건 save 가 1번 호출되어야 하고
        // 평균 점수가 60점 이하이니까 studentFailRepository 의 save 가 1번 호출되어야 한다.
        // studentPasRepository 의 save 는 호출되어선 안된다.
        Mockito.verify(studentScoreRepository, Mockito.times(1)).save(Mockito.any());
        Mockito.verify(studentPassRepository, Mockito.times(0)).save(Mockito.any());
        Mockito.verify(studentFailRepository, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    @DisplayName("합격자 명단 가져오기 검증")
    void getPassStudentsTest() throws Exception {
        // given
        String givenTestExam = "testexam";

        // Fixture Object 패턴 적용
        StudentPass expected1 = StudentPassFixture.create("jyujyu", givenTestExam);
        StudentPass expected2 = StudentPassFixture.create("griotold", givenTestExam);
        StudentPass notExpected1 = StudentPassFixture.create("iamNot", "secondExam");

        // studentPassRepository.findAll()을 수행할 때 아래 지정한 List 를 반환하라는 뜻
        Mockito.when(studentPassRepository.findAll()).thenReturn(List.of(
                expected1,
                expected2,
                notExpected1
        ));

        // when
        List<ExamPassStudentResponse> expectedResponses = List.of(expected1, expected2)
                .stream()
                .map(pass -> new ExamPassStudentResponse(pass.getStudentName(), pass.getAvgScore()))
                .collect(Collectors.toList());

        List<ExamPassStudentResponse> responses = studentScoreService.getPassStudents(givenTestExam);

        responses.forEach((response) -> System.out.println(response.getStudentName() + "/" + response.getAvgScore()));

        // then
        // 파라미터로 전달한 givenTestExam 의 값들만 가져오는지를 검증
//        assertIterableEquals(expectedResponses, responses);

        // org.assertj.core.api.Assertions 로 검증하려면 아래를 사용
        assertThat(responses).containsExactlyElementsOf(expectedResponses);
    }

    @Test
    @DisplayName("불합격자 명단 가져오기 검증")
    void getFailStudentsTest() throws Exception {
        // given
        String givenTestExam = "testexam";

        // Fixture Object 패턴 적용
        StudentFail expected1 = StudentFailFixture.create("jyujyu", givenTestExam);
        StudentFail expected2 = StudentFailFixture.create("griotold", givenTestExam);
        StudentFail notExpected1 = StudentFailFixture.create("iamnot", "secondExam");

        // studentPassRepository.findAll()을 수행할 때 아래 지정한 List 를 반환하라는 뜻
        Mockito.when(studentFailRepository.findAll()).thenReturn(List.of(
                expected1,
                expected2,
                notExpected1
        ));

        // when
        List<ExamFailStudentResponse> expectedResponses = List.of(expected1, expected2)
                .stream()
                .map(pass -> new ExamFailStudentResponse(pass.getStudentName(), pass.getAvgScore()))
                .collect(Collectors.toList());

        List<ExamFailStudentResponse> responses = studentScoreService.getFailStudents(givenTestExam);

        responses.forEach((response) -> System.out.println(response.getStudentName() + "/" + response.getAvgScore()));

        // then
        // 파라미터로 전달한 givenTestExam 의 값들만 가져오는지를 검증
//        assertIterableEquals(expectedResponses, responses);

        // org.assertj.core.api.Assertions 로 검증하려면 아래를 사용
        assertThat(responses).containsExactlyElementsOf(expectedResponses);
    }

    @DisplayName("평균 60점 이상 저장시 인자값 갭쳐")
    @Test
    void saveScoreMockTest_over60_argumentCaptor() throws Exception {
        // given

        // Test Data Builder 패턴 적용
        StudentScore expectedStudentScore = StudentScoreTestDataBuilder.passed().build();

        StudentPass expectedStudentPass = StudentPassFixture.create(expectedStudentScore);

        // ArgumentCaptor -> 인자값 캡쳐해서 검증
        ArgumentCaptor<StudentScore> studentScoreArgumentCaptor = ArgumentCaptor.forClass(StudentScore.class);
        ArgumentCaptor<StudentPass> studentPassArgumentCaptor = ArgumentCaptor.forClass(StudentPass.class);

        // when
        studentScoreService.saveScore(
                expectedStudentScore.getStudentName(),
                expectedStudentScore.getExam(),
                expectedStudentScore.getKorScore(),
                expectedStudentScore.getEnglishScore(),
                expectedStudentScore.getMathScore()
        );

        // then
        Mockito.verify(studentScoreRepository, Mockito.times(1)).save(studentScoreArgumentCaptor.capture());
        StudentScore capturedStudentScore = studentScoreArgumentCaptor.getValue();
        assertThat(capturedStudentScore.getStudentName()).isEqualTo(expectedStudentScore.getStudentName());
        assertThat(capturedStudentScore.getKorScore()).isEqualTo(expectedStudentScore.getKorScore());
        assertThat(capturedStudentScore.getEnglishScore()).isEqualTo(expectedStudentScore.getEnglishScore());
        assertThat(capturedStudentScore.getMathScore()).isEqualTo(expectedStudentScore.getMathScore());

        Mockito.verify(studentPassRepository, Mockito.times(1)).save(studentPassArgumentCaptor.capture());
        Mockito.verify(studentFailRepository, Mockito.times(0)).save(Mockito.any());
        StudentPass capturedStudentPass = studentPassArgumentCaptor.getValue();
        assertThat(capturedStudentPass.getStudentName()).isEqualTo(expectedStudentPass.getStudentName());
        assertThat(capturedStudentPass.getExam()).isEqualTo(expectedStudentPass.getExam());
        assertThat(capturedStudentPass.getAvgScore()).isEqualTo(expectedStudentPass.getAvgScore());
    }

    @DisplayName("평균 60점 미만시 저장시 인자값 캡쳐")
    @Test
    void saveScoreMockTest_under60_argumentCaptor() throws Exception {
        // given
        // FixtureObject 패턴 적용
        StudentScore expectedStudentScore = StudentScoreFixture.failed();

        StudentFail expectedStudentFail = StudentFailFixture.create(expectedStudentScore);


        // ArgumentCaptor -> 인자값 캡쳐해서 검증
        ArgumentCaptor<StudentScore> studentScoreArgumentCaptor = ArgumentCaptor.forClass(StudentScore.class);
        ArgumentCaptor<StudentFail> studentFailArgumentCaptor = ArgumentCaptor.forClass(StudentFail.class);

        // when
        studentScoreService.saveScore(
                expectedStudentScore.getStudentName(),
                expectedStudentScore.getExam(),
                expectedStudentScore.getKorScore(),
                expectedStudentScore.getEnglishScore(),
                expectedStudentScore.getMathScore()
        );

        // then
        Mockito.verify(studentScoreRepository, Mockito.times(1)).save(studentScoreArgumentCaptor.capture());
        StudentScore capturedStudentScore = studentScoreArgumentCaptor.getValue();
        assertThat(capturedStudentScore.getStudentName()).isEqualTo(expectedStudentScore.getStudentName());
        assertThat(capturedStudentScore.getKorScore()).isEqualTo(expectedStudentScore.getKorScore());
        assertThat(capturedStudentScore.getEnglishScore()).isEqualTo(expectedStudentScore.getEnglishScore());
        assertThat(capturedStudentScore.getMathScore()).isEqualTo(expectedStudentScore.getMathScore());

        Mockito.verify(studentPassRepository, Mockito.times(0)).save(Mockito.any());
        Mockito.verify(studentFailRepository, Mockito.times(1)).save(studentFailArgumentCaptor.capture());
        StudentFail capturedStudentFail = studentFailArgumentCaptor.getValue();
        assertThat(capturedStudentFail.getStudentName()).isEqualTo(expectedStudentFail.getStudentName());
        assertThat(capturedStudentFail.getExam()).isEqualTo(expectedStudentFail.getExam());
        assertThat(capturedStudentFail.getAvgScore()).isEqualTo(expectedStudentFail.getAvgScore());
    }
}