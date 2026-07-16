package com.open.trivia;

import com.open.trivia.feign.QuizQuestion;
import com.open.trivia.feign.TriviaDataSourceClientImpl;
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
    private TriviaDataSourceClientImpl triviaDataSourceClient;

	public static void main(String[] args) {
		SpringApplication.run(TriviaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		List<QuizQuestion> quizQuestions = triviaDataSourceClient.fetchQuizQuestions();
		quizQuestions.forEach(System.out::println);
	}
}
