package com.open.trivia.feign;

import java.util.List;

public record TriviaApiResponse(
        int response_code,
        List<QuizQuestion> results
) {
}
