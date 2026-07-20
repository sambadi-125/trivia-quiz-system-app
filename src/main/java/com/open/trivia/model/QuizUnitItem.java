package com.open.trivia.model;

import com.open.trivia.service.feign.response.TriviaApiResponseItem;
import com.open.trivia.utils.DataOptimizationUtilities;

import java.util.List;

import static com.open.trivia.utils.DataOptimizationUtilities.removeHtmlSymbols;

public record QuizUnitItem(
        int quizQuestionId,
        String type,
        String category,
        String question,
        String correct_answer,
        List<String> incorrect_answers
) {

    public QuizUnitItem {
        type = removeHtmlSymbols(type);
        category = removeHtmlSymbols(category);
        question = removeHtmlSymbols(question);
        correct_answer = removeHtmlSymbols(correct_answer);
        incorrect_answers = incorrect_answers.stream()
                .map(DataOptimizationUtilities::removeHtmlSymbols)
                .toList();
    }

    public static QuizUnitItem fromTriviaApiResponseItem(int quizQuestionId, TriviaApiResponseItem triviaApiResponseItem) {
        return new QuizUnitItem(
                quizQuestionId,
                triviaApiResponseItem.type(),
                triviaApiResponseItem.category(),
                triviaApiResponseItem.question(),
                triviaApiResponseItem.correct_answer(),
                triviaApiResponseItem.incorrect_answers()
        );
    }
}
