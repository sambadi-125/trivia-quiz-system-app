package com.open.trivia.controller;

import com.open.trivia.dtos.PlayerAnswerDto;
import com.open.trivia.dtos.PlayerAnswerValidationResponse;
import com.open.trivia.dtos.QuizDto;
import com.open.trivia.service.TriviaQuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api")
public class TriviaQuizController {

    private final TriviaQuizService triviaQuizService;

    public TriviaQuizController(TriviaQuizService triviaQuizService) {
        this.triviaQuizService = triviaQuizService;
    }

    @GetMapping("/questions")
    ResponseEntity<QuizDto> getQuestions() {
        return ResponseEntity.ok(triviaQuizService.fetchNewQuiz());
    }

    @PostMapping("/checkanswers")
    public ResponseEntity<List<PlayerAnswerValidationResponse>> validatePlayerAnswers(
            @RequestBody PlayerAnswerDto playerAnswers) {
        return ResponseEntity.ok(triviaQuizService.checkAnswers(playerAnswers));
    }
}
