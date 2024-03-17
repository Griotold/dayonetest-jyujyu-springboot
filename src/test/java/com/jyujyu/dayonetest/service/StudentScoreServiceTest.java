package com.jyujyu.dayonetest.service;

import com.jyujyu.dayonetest.controller.response.ExamFailStudentResponse;
import com.jyujyu.dayonetest.controller.response.ExamPassStudentResponse;
import com.jyujyu.dayonetest.model.StudentFail;
import com.jyujyu.dayonetest.model.StudentPass;
import com.jyujyu.dayonetest.repository.StudentFailRepository;
import com.jyujyu.dayonetest.repository.StudentPassRepository;
import com.jyujyu.dayonetest.repository.StudentScoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class StudentScoreServiceTest {

    @Test
    @DisplayName("첫번째 Mock 테스트")
    public void firstSaveScoreMockTest() {
        // given
        StudentScoreService studentScoreService = new StudentScoreService(
                Mockito.mock(StudentScoreRepository.class),
                Mockito.mock(StudentPassRepository.class),
                Mockito.mock(StudentFailRepository.class)
        );
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
        StudentScoreRepository studentScoreRepository = Mockito.mock(StudentScoreRepository.class);
        StudentPassRepository studentPassRepository = Mockito.mock(StudentPassRepository.class);
        StudentFailRepository studentFailRepository = Mockito.mock(StudentFailRepository.class);

        StudentScoreService studentScoreService
                = new StudentScoreService(studentScoreRepository, studentPassRepository, studentFailRepository);

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
        StudentScoreRepository studentScoreRepository = Mockito.mock(StudentScoreRepository.class);
        StudentPassRepository studentPassRepository = Mockito.mock(StudentPassRepository.class);
        StudentFailRepository studentFailRepository = Mockito.mock(StudentFailRepository.class);

        StudentScoreService studentScoreService
                = new StudentScoreService(studentScoreRepository, studentPassRepository, studentFailRepository);

        String givenStudentName = "jyujyu";
        String givenExam = "testexam";
        Integer givenKorScore = 60;
        Integer givenEnglishScore = 50;
        Integer givenMathScore = 3;

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
        StudentScoreRepository studentScoreRepository = Mockito.mock(StudentScoreRepository.class);
        StudentPassRepository studentPassRepository = Mockito.mock(StudentPassRepository.class);
        StudentFailRepository studentFailRepository = Mockito.mock(StudentFailRepository.class);

        String givenTestExam = "testexam";

        StudentPass expected1 = StudentPass.builder().id(1L).studentName("jyujyu").exam(givenTestExam).avgScore(70.0).build();
        StudentPass expected2 = StudentPass.builder().id(2L).studentName("griotold").exam(givenTestExam).avgScore(80.0).build();
        StudentPass notExpected1 = StudentPass.builder().id(3L).studentName("iamnot").exam("secondexam").avgScore(90.0).build();

        // studentPassRepository.findAll()을 수행할 때 아래 지정한 List 를 반환하라는 뜻
        Mockito.when(studentPassRepository.findAll()).thenReturn(List.of(
                expected1,
                expected2,
                notExpected1
        ));

        StudentScoreService studentScoreService = new StudentScoreService(studentScoreRepository,
                studentPassRepository,
                studentFailRepository);


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
        StudentScoreRepository studentScoreRepository = Mockito.mock(StudentScoreRepository.class);
        StudentPassRepository studentPassRepository = Mockito.mock(StudentPassRepository.class);
        StudentFailRepository studentFailRepository = Mockito.mock(StudentFailRepository.class);

        String givenTestExam = "testexam";

        StudentFail expected1 = StudentFail.builder().id(1L).studentName("jyujyu").exam(givenTestExam).avgScore(30.0).build();
        StudentFail expected2 = StudentFail.builder().id(2L).studentName("griotold").exam(givenTestExam).avgScore(40.0).build();
        StudentFail notExpected1 = StudentFail.builder().id(3L).studentName("iamnot").exam("secondexam").avgScore(50.0).build();

        // studentPassRepository.findAll()을 수행할 때 아래 지정한 List 를 반환하라는 뜻
        Mockito.when(studentFailRepository.findAll()).thenReturn(List.of(
                expected1,
                expected2,
                notExpected1
        ));

        StudentScoreService studentScoreService = new StudentScoreService(studentScoreRepository,
                studentPassRepository,
                studentFailRepository);


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
}