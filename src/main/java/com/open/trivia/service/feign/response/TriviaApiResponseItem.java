package com.open.trivia.service.feign.response;

import java.util.List;

public record TriviaApiResponseItem(
        String type,
        String difficulty,
        String category,
        String question,
        String correct_answer,
        List<String> incorrect_answers
) {
}
