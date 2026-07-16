package com.open.trivia.feign;

import java.util.Collection;

public record QuizQuestion(
        String type,
        String difficulty,
        String category,
        String question,
        String correct_answer,
        Collection<String> incorrect_answers
) {
}
