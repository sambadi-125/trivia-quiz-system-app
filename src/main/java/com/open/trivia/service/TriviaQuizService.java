package com.open.trivia.service;

import com.open.trivia.dtos.*;
import com.open.trivia.model.QuizUnitItem;
import com.open.trivia.service.feign.TriviaApiFeignClient;
import com.open.trivia.service.feign.response.TriviaApiResponseItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import static com.open.trivia.utils.DataOptimizationUtilities.compareTwoStrings;
import static com.open.trivia.utils.QuizIdGenerator.generateQuizId;

@Service
@Slf4j
public class TriviaQuizService {
    private final TriviaApiFeignClient triviaApiFeignClient;

    public TriviaQuizService(TriviaApiFeignClient triviaApiFeignClient) {
        this.triviaApiFeignClient = triviaApiFeignClient;
    }

    private static final Map<String, List<QuizUnitItem>> QUIZ_UNITS_MAP = new HashMap<>();

    public List<PlayerAnswerValidationResponse> checkAnswers(PlayerAnswerDto playerAnswerDto) {
        log.info("CHECKING PLAYER QUIZ ANSWERS");
        return playerAnswerDto.playerAnswers().stream()
                .map(playerAnswer -> {
                    String correctAnswer = extractCorrectAnswer(playerAnswer, playerAnswerDto.quizId());
                    return new PlayerAnswerValidationResponse(
                            playerAnswer.questionId(),
                            correctAnswer,
                            compareTwoStrings(
                                    playerAnswer.playerAnswer(),
                                    correctAnswer)
                    );
                })
                .peek(validationResponse -> log.debug("Validation response: {}", validationResponse))
                .toList();
    }

    public QuizDto fetchNewQuiz() {
        log.info("[START] FETCHING QUIZ QUESTIONS");
        String quizId = "";
        try {
            var triviaApiResponseItems = triviaApiFeignClient.getQuizQuestions().results();
            quizId = createQuiz(triviaApiResponseItems);
        } catch (Exception e) {
            log.error("Unexpected error while fetching quiz questions. {}", e.getMessage());
        }
        log.info("[END] QUIZ QUESTIONS FETCHED");

        List<QuizUnitItem> quizUnitItems = QUIZ_UNITS_MAP.get(quizId);
        return new QuizDto(
                quizId,
                quizUnitItems.stream()
                        .map(QuizQuestionDto::fromQuizUnitItem)
                        .toList());
    }

    private String createQuiz(List<TriviaApiResponseItem> triviaApiResponseItems) {
        List<QuizUnitItem> quizUnitItems = IntStream
                .range(0, triviaApiResponseItems.size())
                .boxed()
                .map(questionId -> QuizUnitItem.fromTriviaApiResponseItem(questionId, triviaApiResponseItems.get(questionId)))
                .toList();
        String quizId = generateQuizId();

        QUIZ_UNITS_MAP.put(quizId, quizUnitItems);
        return quizId;
    }

    private String extractCorrectAnswer(PlayerAnswerItem playerAnswer, String quizId) {
        return QUIZ_UNITS_MAP.get(quizId).stream()
                .filter(questionItem -> questionItem.quizQuestionId() == playerAnswer.questionId())
                .map(QuizUnitItem::correct_answer)
                .findFirst()
                .orElse("");
    }
}
