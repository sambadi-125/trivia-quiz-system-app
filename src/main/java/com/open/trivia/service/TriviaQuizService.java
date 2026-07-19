package com.open.trivia.service;

import com.open.trivia.dtos.PlayerAnswerDto;
import com.open.trivia.dtos.PlayerAnswerValidationResponse;
import com.open.trivia.dtos.QuizQuestionDto;
import com.open.trivia.service.feign.TriviaApiFeignClient;
import com.open.trivia.service.feign.response.TriviaApiResponseItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.open.trivia.utils.DataOptimizationUtilities.compareTwoStrings;
import static com.open.trivia.utils.DataOptimizationUtilities.removeHtmlSymbols;

@Service
@Slf4j
public class TriviaQuizService {
    private final TriviaApiFeignClient triviaApiFeignClient;

    public TriviaQuizService(TriviaApiFeignClient triviaApiFeignClient) {
        this.triviaApiFeignClient = triviaApiFeignClient;
    }

    private static Map<Integer, TriviaApiResponseItem> QUIZ_QUESTIONS_DTO_MAP = new HashMap<>();
    private static Map<Integer, String> CORRECT_ANSWERS_MAP = new HashMap<>();

    public List<PlayerAnswerValidationResponse> checkAnswers(List<PlayerAnswerDto> playerAnswers) {
        log.info("CHECKING PLAYER QUIZ ANSWERS");
        return playerAnswers.stream()
                .map(playerAnswer -> new PlayerAnswerValidationResponse(
                        playerAnswer.questionId(),
                        removeHtmlSymbols(CORRECT_ANSWERS_MAP.get(playerAnswer.questionId())),
                        compareTwoStrings(
                                playerAnswer.playerAnswer(),
                                CORRECT_ANSWERS_MAP.get(playerAnswer.questionId())
                        )
                ))
                .peek(validationResponse -> log.debug("Validation response: {}", validationResponse))
                .toList();
    }

    public List<QuizQuestionDto> fetchQuizQuestions() {
        log.info("[START] FETCHING QUIZ QUESTIONS");
        try {
            var triviaApiResponseItems = triviaApiFeignClient.getQuizQuestions().results();
            loadQuizQuestionsDtoMap(triviaApiResponseItems);
            loadCorrectAnswersMap(triviaApiResponseItems);
        } catch (Exception e) {
            log.error("Unexpected error while fetching quiz questions. {}", e.getMessage());
        }
        log.info("[END] QUIZ QUESTIONS FETCHED");

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
}
