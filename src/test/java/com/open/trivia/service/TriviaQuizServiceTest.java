package com.open.trivia.service;

import com.open.trivia.dtos.PlayerAnswerValidationResponse;
import com.open.trivia.dtos.QuizQuestionDto;
import com.open.trivia.service.feign.TriviaApiFeignClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.open.trivia.TestFixtures.QUIZ_ANSWERS;
import static com.open.trivia.TestFixtures.TRIVIA_API_RESPONSE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TriviaQuizServiceTest {

    @Mock
    private TriviaApiFeignClient triviaApiFeignClient;

    @InjectMocks
    private TriviaQuizService triviaQuizService;

    @Test
    void fetchQuizQuestions_whenCalled_thenReturnsQuizQuestionDtoList() {
        when(triviaApiFeignClient.getQuizQuestions()).thenReturn(TRIVIA_API_RESPONSE);

        List<QuizQuestionDto> quizQuestionDtos = triviaQuizService.fetchQuizQuestions();

        assertThat(quizQuestionDtos)
                .hasSize(3)
                .first()
                .satisfies(quizQuestionDto -> {
                    assertThat(quizQuestionDto.question()).contains("House of Cards");
                    assertThat(quizQuestionDto.category()).contains("Entertainment: Television");
                    assertThat(quizQuestionDto.type()).contains("multiple");
                });
    }

    @Test
    void checkAnswers_whenCalled_thenReturnComparisonResults() {
        when(triviaApiFeignClient.getQuizQuestions()).thenReturn(TRIVIA_API_RESPONSE);

        triviaQuizService.fetchQuizQuestions();
        List<PlayerAnswerValidationResponse> expected = List.of(
                new PlayerAnswerValidationResponse(0, "House Majority Whip", true),
                new PlayerAnswerValidationResponse(1, "Toy Story 2", true),
                new PlayerAnswerValidationResponse(2, "M3",false)
        );
        var result = triviaQuizService.checkAnswers(QUIZ_ANSWERS);

        assertThat(result).isEqualTo(expected);
    }
}
