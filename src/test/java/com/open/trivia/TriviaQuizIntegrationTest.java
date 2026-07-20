package com.open.trivia;

import com.open.trivia.dtos.PlayerAnswerDto;
import com.open.trivia.service.feign.TriviaApiFeignClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tools.jackson.databind.ObjectMapper;

import static com.open.trivia.TestFixtures.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TriviaQuizIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TriviaApiFeignClient triviaApiFeignClient;

    @Test
    void getQuestions_whenCalled_thenRetrievesQuizQuestions() throws Exception {
        when(triviaApiFeignClient.getQuizQuestions()).thenReturn(TRIVIA_API_RESPONSE);

        mvc.perform(get("/api/questions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quizQuestions", hasSize(3)))
                .andExpect(jsonPath("$.quizQuestions[0].category", is(QUIZ_QUESTION_DTOS.get(0).category())));
    }

    @Test
    void validatePlayerAnswers_whenRequested_thenValidatesPlayerResponses() throws Exception {
        when(triviaApiFeignClient.getQuizQuestions()).thenReturn(TRIVIA_API_RESPONSE);

        String responseBody = mvc.perform(get("/api/questions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String quizId = objectMapper.readTree(responseBody).get("quizId").asText();

        PlayerAnswerDto playerAnswer = new PlayerAnswerDto(quizId, PLAYER_ANSWERS);

        mvc.perform(MockMvcRequestBuilders
                        .post("/api/checkanswers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(playerAnswer))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].questionId", is(playerAnswer.playerAnswers().get(0).questionId())))
                .andExpect(jsonPath("$[0].correctAnswer", is(PLAYER_ANSWERS_VALIDATION_RESPONSES.get(0).correctAnswer())))
                .andExpect(jsonPath("$[0].isPlayerAnswerCorrect", is(PLAYER_ANSWERS_VALIDATION_RESPONSES.get(0).isPlayerAnswerCorrect())));
    }
}
