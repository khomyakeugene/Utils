package com.company.util.common;

import java.util.Random;

/**
 * Created by Yevgen on 18.03.2016 as a part of the project "JEE_Homework_1".
 */
public class RandomDataGenerator {
    private static final String GENERATE_RANDOM_INTEGER_ELEMENTS_PATTERN = "Generate random %d <Integer> elements ...";

    private static final int DEFAULT_UPPER_INT_LIMIT = 1000;
    private static final int DEFAULT_UPPER_INT_LIMIT_MULTIPLIER = 10;
    private static final int DEFAULT_STRING_LENGTH = 32;

    private int upperIntLimit;
    static private Random random = new Random();

    public RandomDataGenerator(int upperLimit) {
        this.upperIntLimit = upperLimit;
    }

    public RandomDataGenerator() {
        this (DEFAULT_UPPER_INT_LIMIT);
    }

    public void setUpperIntLimit(int upperIntLimit) {
        this.upperIntLimit = upperIntLimit;
    }

    public int generateRandomInt() {
        return generateRandomInt(this.upperIntLimit);
    }

    public int generateRandomInt(int upperIntLimit) {
        return (int)(Math.random() * upperIntLimit);
    }

    public Integer[] generateIntegerArray(int size) {
        Integer[] result = new Integer[size];

        setUpperIntLimit(size * DEFAULT_UPPER_INT_LIMIT_MULTIPLIER);

        Util.printLine(String.format(GENERATE_RANDOM_INTEGER_ELEMENTS_PATTERN, size));
        for (int i = 0; i < size; i++) {
            result[i] = generateRandomInt();
        }
        Util.printDoneMessage();

        return result;
    }

    public static String getRandomString(int length) {
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }

        return sb.toString();
    }

    public static String getRandomString() {
        return getRandomString(DEFAULT_STRING_LENGTH);
    }
}
