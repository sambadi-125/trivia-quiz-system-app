package com.open.trivia.dtos;

public record PlayerAnswerValidationResponse(
        int questionId,
        boolean isPlayerAnswerCorrect
) {
}
