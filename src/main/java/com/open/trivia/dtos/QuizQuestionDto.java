package com.open.trivia.dtos;

import com.open.trivia.model.QuizUnitItem;
import org.apache.commons.collections4.ListUtils;

import java.util.List;

public record QuizQuestionDto(
        int id,
        String type,
        String category,
        String question,
        List<String> answerChoices
) {

    public static QuizQuestionDto fromQuizUnitItem(QuizUnitItem quizUnitItem) {
        return new QuizQuestionDto(
                quizUnitItem.quizQuestionId(),
                quizUnitItem.type(),
                quizUnitItem.category(),
                quizUnitItem.question(),
                extractAnswerChoices(quizUnitItem)
        );
    }

    private static List<String> extractAnswerChoices(QuizUnitItem quizUnitItem) {
        return ListUtils.union(
                quizUnitItem.incorrect_answers(),
                List.of(quizUnitItem.correct_answer())
        );
    }
}
