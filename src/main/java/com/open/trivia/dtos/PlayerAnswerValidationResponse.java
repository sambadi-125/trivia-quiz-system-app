package com.open.trivia.dtos;

public record PlayerAnswerValidationResponse(
        int questionId,
        String correctAnswer,
        boolean isPlayerAnswerCorrect
) {
}
