package com.jyujyu.dayonetest.controller;

import com.jyujyu.dayonetest.controller.request.SaveExamScoreRequest;
import com.jyujyu.dayonetest.controller.response.ExamFailStudentResponse;
import com.jyujyu.dayonetest.controller.response.ExamPassStudentResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ScoreApi {

    @PostMapping("/exam/{exam}/score")
    public Object save(@PathVariable("exam") String exam,
                       @RequestBody SaveExamScoreRequest request) {
        return request;
    }

    @GetMapping("/exam{exam}/pass")
    public List<ExamPassStudentResponse> pass(@PathVariable("exam") String exam) {
        return List.of(
                new ExamPassStudentResponse("jyujyu", 60.0)
        );
    }

    @GetMapping("/exam/{exam}/fail")
    public List<ExamFailStudentResponse> fail(@PathVariable("exam") String exam) {
        return List.of(new ExamFailStudentResponse("jyujyu", 40.0));
    }


}
