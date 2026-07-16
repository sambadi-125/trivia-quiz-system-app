package com.open.trivia.feign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TriviaDataSourceClientImpl {
    @Autowired
    private TriviaDataSourceFeignClient triviaDataSourceFeignClient;

    public List<QuizQuestion> fetchQuizQuestions() {
        return triviaDataSourceFeignClient.getQuizQuestions().results();
    }
}
