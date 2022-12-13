package pl.szinton.querky.utils;

import pl.szinton.querky.exception.StringParsingException;

public class StringParsingUtils {

    protected static final String DATA_INT_PARSE_EXCEPTION_MESSAGE =
            "Failed to parse WebSockets message content - first argument must by a number.";

    public static int toInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException ex) {
            throw new StringParsingException(DATA_INT_PARSE_EXCEPTION_MESSAGE + " Instead was: " + str);
        }
    }
}
