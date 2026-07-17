package com.open.trivia.service;

import com.open.trivia.service.dto.QuizQuestionDto;
import com.open.trivia.service.feign.TriviaApiFeignClient;
import com.open.trivia.service.feign.response.TriviaApiResponseItem;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class TriviaQuizService {
    private final TriviaApiFeignClient triviaApiFeignClient;

    public TriviaQuizService(TriviaApiFeignClient triviaApiFeignClient) {
        this.triviaApiFeignClient = triviaApiFeignClient;
    }

    private static Map<Integer, TriviaApiResponseItem> QUIZ_QUESTIONS_DTO_MAP = new HashMap<>();
    private static Map<Integer, String> CORRECT_ANSWERS_MAP = new HashMap<>();

    public List<QuizQuestionDto> fetchQuizQuestions() {
        List<TriviaApiResponseItem> triviaApiResponses = triviaApiFeignClient.getQuizQuestions().results();
        loadQuizQuestionsDtoMap(triviaApiResponses);
        loadCorrectAnswersMap(triviaApiResponses);

        // start of temporal part for logging:
        QUIZ_QUESTIONS_DTO_MAP.forEach((x, y) -> System.out.println("key: " + x + "; value: " + y));
        // end of temporal part for logging:

        return QUIZ_QUESTIONS_DTO_MAP.entrySet().stream()
                .map(triviaApiResponseItem -> QuizQuestionDto.fromTriviaApiResponse(
                        triviaApiResponseItem.getValue(),
                        triviaApiResponseItem.getKey())
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

    public Map<Integer, Boolean> checkAnswers(Map<Integer, String> answers) {
        return answers.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().equals(CORRECT_ANSWERS_MAP.get(entry.getKey())))
                );
    }
}
