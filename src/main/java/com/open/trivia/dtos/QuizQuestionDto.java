package com.open.trivia.dtos;

import com.open.trivia.service.feign.response.TriviaApiResponseItem;
import com.open.trivia.utils.DataOptimizationUtilities;
import org.apache.commons.collections4.ListUtils;

import java.util.List;

import static com.open.trivia.utils.DataOptimizationUtilities.removeHtmlSymbols;

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
                removeHtmlSymbols(triviaApiResponseItem.type()),
                removeHtmlSymbols(triviaApiResponseItem.category()),
                removeHtmlSymbols(triviaApiResponseItem.question()),
                extractAnswerChoices(triviaApiResponseItem).stream()
                        .map(DataOptimizationUtilities::removeHtmlSymbols)
                        .toList()
        );
    }

    private static List<String> extractAnswerChoices(TriviaApiResponseItem triviaApiResponseItem) {
        return ListUtils.union(
                triviaApiResponseItem.incorrect_answers(),
                List.of(triviaApiResponseItem.correct_answer())
        );
    }
}
