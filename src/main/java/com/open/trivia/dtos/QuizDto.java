package com.open.trivia.dtos;

import java.util.List;

public record QuizDto(
        String quizId,
        List<QuizQuestionDto> quizQuestions
) {
}
