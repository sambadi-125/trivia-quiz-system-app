package com.open.trivia;

import com.open.trivia.dtos.QuizQuestionDto;
import com.open.trivia.service.TriviaQuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.util.List;
import java.util.Map;

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
        Map<Integer, Boolean> integerBooleanMap = triviaQuizService.checkAnswers(Map.of(
                0, "False",
                1, "True",
                2, "False",
                3, "False"
        ));
        System.out.println(integerBooleanMap);
    }
}
