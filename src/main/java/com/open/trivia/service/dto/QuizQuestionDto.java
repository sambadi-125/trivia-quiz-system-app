package com.open.trivia.service.dto;

import com.open.trivia.service.feign.response.TriviaApiResponseItem;
import org.apache.commons.collections4.ListUtils;

import java.util.List;

public record QuizQuestionDto(
        int id,
        String type,
        String category,
        String question,
        List<String> answerChoices
) {

    public static QuizQuestionDto fromTriviaApiResponse(TriviaApiResponseItem triviaApiResponseItem, int id) {
        return new QuizQuestionDto(
                id,
                triviaApiResponseItem.type(),
                triviaApiResponseItem.category(),
                triviaApiResponseItem.question(),
                extractAnswerChoices(triviaApiResponseItem)
        );
    }

    private static List<String> extractAnswerChoices(TriviaApiResponseItem triviaApiResponseItem) {
        return ListUtils.union(triviaApiResponseItem.incorrect_answers(), List.of(triviaApiResponseItem.correct_answer()));
    }
}
