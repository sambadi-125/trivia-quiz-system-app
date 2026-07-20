package com.open.trivia.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.text.StringEscapeUtils;

@UtilityClass
public final class DataOptimizationUtilities {

    public static boolean compareTwoStrings(String one, String two) {
        return one.toLowerCase().trim()
                .equals(two.toLowerCase().trim());
    }

    public static String removeHtmlSymbols(String textWithHtmSymbols) {
        return StringEscapeUtils.unescapeHtml4(textWithHtmSymbols);
    }
}
