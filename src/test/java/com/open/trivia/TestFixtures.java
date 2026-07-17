package com.open.trivia;

import com.open.trivia.service.feign.response.TriviaApiResponse;
import com.open.trivia.service.feign.response.TriviaApiResponseItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Map.entry;

public class TestFixtures {
    public static TriviaApiResponse TRIVIA_API_RESPONSE = new TriviaApiResponse(
            1,
            List.of(
                    new TriviaApiResponseItem(
                            "multiple", "easy", "Entertainment: Television",
                            "In season one of the Netflix political drama \"House of Cards\", what government position does Frank Underwood hold?",
                            "House Majority Whip",
                            List.of("Attorney General", "President", "Chief of Staff")
                    ),
                    new TriviaApiResponseItem(
                            "multiple", "medium", "Entertainment: Film",
                            "Which movie sequel had improved box office results compared to its original film?",
                            "Toy Story 2",
                            List.of("Sin City: A Dame to Kill For", "Speed 2: Cruise Control", "Son of the Mask")
                    ),
                    new TriviaApiResponseItem(
                            "multiple", "medium", "General Knowledge",
                            "Which American manufactured submachine gun was informally known by the American soldiers that used it as \"Grease Gun\"?",
                            "M3",
                            List.of("Colt 9mm", "Thompson", "MAC-10")
                    ))
    );

    public static Map<Integer, String> QUIZ_ANSWERS = Map.ofEntries(
            entry(0, "House Majority Whip"),
            entry(1, "Toy Story 2"),
            entry(2, "Wrong Answer")
    );
}
