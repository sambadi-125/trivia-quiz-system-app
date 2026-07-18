package com.open.trivia;

import com.open.trivia.dtos.PlayerAnswerDto;
import com.open.trivia.dtos.PlayerAnswerValidationResponse;
import com.open.trivia.dtos.QuizQuestionDto;
import com.open.trivia.service.TriviaQuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.List;

@SpringBootApplication
@EnableFeignClients
public class TriviaApplication implements CommandLineRunner {

    @Autowired
    private TriviaQuizService triviaQuizService;

    public static void main(String[] args) {
        SpringApplication.run(TriviaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        List<QuizQuestionDto> quizQuestions = triviaQuizService.fetchQuizQuestions();
        quizQuestions.forEach(System.out::println);
        List<PlayerAnswerValidationResponse> playerAnswerValidationResponses = triviaQuizService.checkAnswers(List.of(
                new PlayerAnswerDto(0, "False"),
                new PlayerAnswerDto(1, "True"),
                new PlayerAnswerDto(2, "False"),
                new PlayerAnswerDto(3, "False")
        ));
        System.out.println(playerAnswerValidationResponses);
    }
}
