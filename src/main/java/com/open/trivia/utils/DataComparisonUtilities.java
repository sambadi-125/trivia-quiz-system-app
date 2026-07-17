package com.open.trivia.utils;

public final class DataComparisonUtilities {

    public static boolean compareTwoStrings(String one, String two) {
        return one.toLowerCase().trim()
                .equals(two.toLowerCase().trim());
    }
}
