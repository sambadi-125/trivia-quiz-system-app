package com.open.trivia.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@FeignClient(name = "open-trivia-data-source", url = "https://opentdb.com/api.php?amount=10&difficulty=easy")
public interface TriviaDataSourceFeignClient {
    @GetMapping
    TriviaApiResponse getQuizQuestions();
}
