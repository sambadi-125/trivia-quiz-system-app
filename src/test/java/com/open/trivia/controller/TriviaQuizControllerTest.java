package com.open.trivia.controller;

import com.open.trivia.service.TriviaQuizService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static com.open.trivia.TestFixtures.QUIZ_QUESTION_DTOS;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TriviaQuizController.class)
class TriviaQuizControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private TriviaQuizService triviaQuizService;

    @Test
    void getQuestions_whenCalled_thenReturnListOfQuestions() throws Exception {
        when(triviaQuizService.fetchQuizQuestions()).thenReturn(QUIZ_QUESTION_DTOS);

        mvc.perform(get("/api/questions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect((ResultMatcher) jsonPath("$[0].category", is(QUIZ_QUESTION_DTOS.get(0).category())));
    }
}
