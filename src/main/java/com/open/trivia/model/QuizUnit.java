package com.open.trivia.model;

import java.util.List;

public record QuizUnit(
        String quizId,
        List<QuizUnitItem> quizQuestions
) {
}
