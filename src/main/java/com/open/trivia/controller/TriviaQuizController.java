package com.open.trivia.controller;

import com.open.trivia.dtos.PlayerAnswerDto;
import com.open.trivia.dtos.PlayerAnswerValidationResponse;
import com.open.trivia.dtos.QuizQuestionDto;
import com.open.trivia.service.TriviaQuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping("/api")
public class TriviaQuizController {

    private final TriviaQuizService triviaQuizService;

    public TriviaQuizController(TriviaQuizService triviaQuizService) {
        this.triviaQuizService = triviaQuizService;
    }

    @GetMapping("/questions")
    ResponseEntity<List<QuizQuestionDto>> getQuestions() {
        return ResponseEntity.ok(triviaQuizService.fetchQuizQuestions());
    }

    @PostMapping("/checkanswers")
    public ResponseEntity<List<PlayerAnswerValidationResponse>> validatePlayerAnswers(List<PlayerAnswerDto> playerAnswers) {
        return ResponseEntity.ok(triviaQuizService.checkAnswers(playerAnswers));
    }
}
