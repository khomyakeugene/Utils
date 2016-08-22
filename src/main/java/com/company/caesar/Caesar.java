package com.company.caesar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yevgen on 23.01.2016 as a part of the project "Unit9_Homework".
 */
public class Caesar {
    public final static int LOWER_BOUND = 32;
    public final static int UPPER_BOUND = 127;
    public final static int DIVIDER = UPPER_BOUND - LOWER_BOUND + 1;
    public final static int DEFAULT_SHIFT = 10;

    public static char[] encodeChars(char[] chars, int shift) {
        for (int i = 0; i < chars.length; i++) {
            // Just for the lower part of the ASCII code table; intentionally do not use "Character.isLetter.. and so on"
            // static methods family (at least, because of "Unicode approach" is using in methods like these)
            if (chars[i] >= LOWER_BOUND && chars[i] <= UPPER_BOUND) {
                // Also "manipulate with base" to make life easier ...
                chars[i] = (char) ((((chars[i] - LOWER_BOUND) + shift) % DIVIDER) + LOWER_BOUND);
            }
        }

        return chars;
    }

    public static String encodeString(String string, int shift) {
        char[] chars = string.toCharArray();

        return new String(encodeChars(chars, shift));
    }


    public static char[] decodeChars(char[] chars, int shift) {
        for (int i = 0; i < chars.length; i++) {
            // Just for the lower part of the ASCII code table; intentionally do not use "Character.isLetter.. and so on"
            // static methods family (at least, because of "Unicode approach" is using in methods like these)
            if (chars[i] >= LOWER_BOUND && chars[i] <= UPPER_BOUND) {
                // Also "manipulate with base" to make life easier ...
                int x = ((chars[i] - LOWER_BOUND) - shift);
                if (x < 0) x += DIVIDER; // "round" "character set"
                chars[i] = (char) ((x % DIVIDER) + LOWER_BOUND);
            }
        }

        return chars;
    }

    public static String decodeString(String string, int shift) {
        char[] chars = string.toCharArray();

        return new String(decodeChars(chars, shift));
    }

    // To have the possibility to manipulate the "shift-parameter" from outside ...
    public static ArrayList<String> encodeList(List<? extends Object> sourceList, int shift) {
        ArrayList<String> resultList = new ArrayList<>();

        if (sourceList != null) {
            sourceList
                    .stream()
                    .forEach(f -> resultList.add(encodeString(f.toString(), shift)));
        }

        return resultList;
    }

    // To have the possibility to hide the "shift-parameter"
    public static ArrayList<String> encodeListUsingDefaultShift(List<? extends Object> sourceData) {
        return encodeList(sourceData, DEFAULT_SHIFT);
    }

    // To have the possibility to manipulate the "shift-parameter" from outside ...
    public static ArrayList<String> decodeList(List<? extends Object> sourceList, int shift) {
        ArrayList<String> resultList = new ArrayList<>();

        if (sourceList != null) {
            sourceList
                    .stream()
                    .forEach(f -> resultList.add(decodeString(f.toString(), shift)));
        }

        return resultList;
    }

    // To have the possibility to hide the "shift-parameter"
    public static ArrayList<String> decodeListUsingDefaultShift(List<? extends Object> sourceData) {
        return decodeList(sourceData, DEFAULT_SHIFT);
    }
}
