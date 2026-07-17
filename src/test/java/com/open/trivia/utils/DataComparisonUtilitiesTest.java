package com.open.trivia.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.open.trivia.utils.DataComparisonUtilities.compareTwoStrings;
import static org.assertj.core.api.Assertions.assertThat;

class DataComparisonUtilitiesTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("provideValuesToCompare")
    void compareTwoStrings_whenTwoStringsProvided_thenReturnsCorrectComparisonResult(String description, String one, String two, boolean expected) {
        assertThat(compareTwoStrings(one, two)).isEqualTo(expected);
    }

    private static Stream<Arguments> provideValuesToCompare() {
        return Stream.of(
                Arguments.of("Lower vs upper casing", "one", "ONE", true),
                Arguments.of("Different casing", "one", "oNe", true),
                Arguments.of("Casing and space-left", "one", " OnE", true),
                Arguments.of("Casing and space-both", "one", " ONE ", true),
                Arguments.of("Excessive symbol", "one", "ONE-", false),
                Arguments.of("Unequal words", "one", "two", false)
        );
    }
}
