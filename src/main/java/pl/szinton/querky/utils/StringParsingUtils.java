package pl.szinton.querky.utils;

import pl.szinton.querky.exception.StringParsingException;

public class StringParsingUtils {

    public static int toInt(String str, String exMsg) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException ex) {
            throw new StringParsingException(exMsg);
        }
    }
}
