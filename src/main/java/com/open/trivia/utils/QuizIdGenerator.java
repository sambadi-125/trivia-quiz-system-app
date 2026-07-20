package com.open.trivia.utils;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public final class QuizIdGenerator {

    public static String generateQuizId() {
        return UUID.randomUUID().toString();
    }
}

