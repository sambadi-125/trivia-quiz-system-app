package com.open.trivia.service.feign;

import com.open.trivia.config.FeignConfig;
import com.open.trivia.service.feign.response.TriviaApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "trivia-api",
        url = "https://opentdb.com/api.php?amount=10",
        configuration = FeignConfig.class)
public interface TriviaApiFeignClient {
    @GetMapping
    TriviaApiResponse getQuizQuestions();
}
