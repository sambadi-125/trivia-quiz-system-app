package com.open.trivia.service.feign.response;

import java.util.List;

public record TriviaApiResponse(
        int response_code,
        List<TriviaApiResponseItem> results
) {
}
