package com.open.trivia.service;

import com.open.trivia.dtos.PlayerAnswerDto;
import com.open.trivia.dtos.PlayerAnswerValidationResponse;
import com.open.trivia.dtos.QuizQuestionDto;
import com.open.trivia.service.feign.TriviaApiFeignClient;
import com.open.trivia.service.feign.response.TriviaApiResponseItem;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.open.trivia.utils.DataOptimizationUtilities.compareTwoStrings;
import static com.open.trivia.utils.DataOptimizationUtilities.removeHtmlSymbols;

@Service
public class TriviaQuizService {
    private final TriviaApiFeignClient triviaApiFeignClient;

    public TriviaQuizService(TriviaApiFeignClient triviaApiFeignClient) {
        this.triviaApiFeignClient = triviaApiFeignClient;
    }

    private static Map<Integer, TriviaApiResponseItem> QUIZ_QUESTIONS_DTO_MAP = new HashMap<>();
    private static Map<Integer, String> CORRECT_ANSWERS_MAP = new HashMap<>();

    public List<QuizQuestionDto> fetchQuizQuestions() {
        var triviaApiResponseItems = triviaApiFeignClient.getQuizQuestions().results();
        loadQuizQuestionsDtoMap(triviaApiResponseItems);
        loadCorrectAnswersMap(triviaApiResponseItems);

        return QUIZ_QUESTIONS_DTO_MAP.entrySet().stream()
                .map(triviaApiResponseMap -> QuizQuestionDto.fromTriviaApiResponse(
                        triviaApiResponseMap.getValue(),
                        triviaApiResponseMap.getKey())
                )
                .toList();
    }


    private void loadQuizQuestionsDtoMap(List<TriviaApiResponseItem> triviaApiResponseItems) {
        QUIZ_QUESTIONS_DTO_MAP = IntStream
                .range(0, triviaApiResponseItems.size())
                .boxed()
                .collect(Collectors.toMap(questionId -> questionId, triviaApiResponseItems::get));
    }

    private void loadCorrectAnswersMap(List<TriviaApiResponseItem> triviaApiResponseItems) {
        CORRECT_ANSWERS_MAP = IntStream
                .range(0, triviaApiResponseItems.size())
                .boxed()
                .collect(Collectors.toMap(
                        questionId -> questionId,
                        questionId -> triviaApiResponseItems.get(questionId).correct_answer())
                );
    }

    public List<PlayerAnswerValidationResponse> checkAnswers(List<PlayerAnswerDto> playerAnswers) {
        return playerAnswers.stream()
                .map(playerAnswer -> new PlayerAnswerValidationResponse(
                        playerAnswer.questionId(),
                        removeHtmlSymbols(CORRECT_ANSWERS_MAP.get(playerAnswer.questionId())),
                        compareTwoStrings(
                                playerAnswer.playerAnswer(),
                                CORRECT_ANSWERS_MAP.get(playerAnswer.questionId())
                        )
                ))
                .toList();
    }
}
