package com.open.trivia.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.open.trivia.utils.DataOptimizationUtilities.compareTwoStrings;
import static com.open.trivia.utils.DataOptimizationUtilities.removeHtmlSymbols;
import static org.assertj.core.api.Assertions.assertThat;

class DataOptimizationUtilitiesTest {

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

    @Test
    void removeHtmlSymbols_whenStringWithHtmlSymbolsProvided_thenReturnsSanitizedResult() {
        var textWithHtmlSymbols = "Who created the Cartoon Network series &quot;Regular Show&quot;?";
        var expected = "Who created the Cartoon Network series \"Regular Show\"?";

        assertThat(removeHtmlSymbols(textWithHtmlSymbols)).isEqualTo(expected);
    }
}
