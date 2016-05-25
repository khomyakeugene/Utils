package com.company.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Created by Yevgen on 06.01.2016.
 */

public class Util {
    // public final static int LETS_THINK_THIS_IS_THE_NUMBER_OF_CURRENT_ELEMENT_OF_STACK = 1; // Reserve it for future time
    private final static int LETS_THINK_THIS_IS_THE_NUMBER_OF_CALLING_ELEMENT_OF_STACK = 2;

    private static final String DONE_MESSAGE = "done";
    private final static String MESSAGE_WITH_PREFIX_PATTERN = "<%s>: %s";
    private final static String CLASS_NAME_PATTERN = "Class Name: %s";
    private final static String FULL_METHOD_NAME_PATTERN = "%s.%s";
    private final static String EXCEPTION_MESSAGE_PATTERN = "<%s>: %s";
    private final static String PLEASE_ENTER_A_PROPERTY_NAME_PATTERN =
            "Please, enter name of a property of class %s (or just push <Enter> to stop):";
    private final static String PROPERTY_DESCRIPTION_PATTERN = "A %s \"%s\" is presented in class \"%s\"";
    private final static String PLEASE_REPEAT_ENTER =
            "%s was generated with data \"%s\". Please, repeat enter action";

    public static String toString(Object object) {
        String result;
        if (object == null) {
            result = "null";
        } else {
            result = object.toString();
            if (object instanceof String) {
                result =  "\"" + result + "\"";
            }
        }

        return result;
    }

    public static String toStringMaskNullAsEmpty(Object object) {
        return (object == null) ? "" : object.toString();
    }

    static void printLine(String message) {
        System.out.print(message);
    }

    public static void rePrintLine(String message) {
        printLine("\r" + message);
    }

    public static void printMessage(String message) {
        printLine(message + "\n");
    }

    public static void printMessage(Object object, String message) {
        printMessage(String.format(MESSAGE_WITH_PREFIX_PATTERN, object.getClass().getName(), message));
    }

    public static void printDoneMessage() {
        printMessage(DONE_MESSAGE);
    }

    public static String getLongestString(String[] data) {
        Optional<String> stringOptional = Arrays.stream(data).max((f1, f2) -> new Integer(f1.length()).compareTo(f2.length()));

        return stringOptional.isPresent() ? stringOptional.get() : "";
    }

    public static int getLengthOfLongestString(String[] data) {
        String s = getLongestString(data);
        return s.length();
    }

    public static String[] convertColumnToStringArray (String[][] tableData, int columnNumber) {
        String[] result = new String[tableData.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = tableData[i][columnNumber];
        }

        return result;
    }

    public static String getProgramName() {
        return "stub";
    }

    public static String getClassNameMessage(Object object) {
        return String.format(CLASS_NAME_PATTERN, object.getClass().getName());
    }

    public static void printLine(Object object, String message) {
        printLine(String.format(MESSAGE_WITH_PREFIX_PATTERN, object.getClass().getName(), message));
    }

    public static String getClassName() {
        return Thread.currentThread().getStackTrace()[
                LETS_THINK_THIS_IS_THE_NUMBER_OF_CALLING_ELEMENT_OF_STACK].getClassName();
    }

    public static String getMethodName() {
        return Thread.currentThread().getStackTrace()[
                LETS_THINK_THIS_IS_THE_NUMBER_OF_CALLING_ELEMENT_OF_STACK].getMethodName();
    }

    public static String getFullMethodName() {
        StackTraceElement[] stackTraceElement = Thread.currentThread().getStackTrace();

        return String.format(
                FULL_METHOD_NAME_PATTERN,
                stackTraceElement[LETS_THINK_THIS_IS_THE_NUMBER_OF_CALLING_ELEMENT_OF_STACK].getClassName(),
                stackTraceElement[LETS_THINK_THIS_IS_THE_NUMBER_OF_CALLING_ELEMENT_OF_STACK].getMethodName());
    }

    public static String getFullMethodName(String className, String methodName) {
        return String.format(FULL_METHOD_NAME_PATTERN, className, methodName);
    }

    public static void throwTextException(String methodName, String text) throws Exception {
        throw new Exception(String.format(EXCEPTION_MESSAGE_PATTERN, methodName, text));
    }

    public static void throwTextException(Object object, String text) throws Exception {
        throw new Exception(String.format(EXCEPTION_MESSAGE_PATTERN, object.getClass().getName(), text));
    }

    public static int getIndexInStringArray(String[] array, String key, boolean ignoreCase) {
        int result = -1;

        for (int i = 0; i < array.length && result == -1; i++) {
            if (ignoreCase ? key.equalsIgnoreCase(array[i]) : key.equals(array[i])) {
                result = i;
            }
        }

        return result;
    }

    public static String readInputString(String enterMessageInvitation) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        printMessage(enterMessageInvitation);

        do {
            try {
                return bufferedReader.readLine();
            } catch (IOException e) {
                printMessage(String.format(PLEASE_REPEAT_ENTER, e.getClass().getName(), e.getMessage()));
            }
        } while (true);
    }

    public static int readInputInt(String enterMessageInvitation) {
        final Scanner scanner = new Scanner(System.in);

        printMessage(enterMessageInvitation);
        return scanner.nextInt();
    }

    public static double readInputDouble(String enterMessageInvitation) {
        final Scanner scanner = new Scanner(System.in);

        printMessage(enterMessageInvitation);
        return scanner.nextDouble();
    }

    public static void inspectObjectProperties(SelfDescribingObject object) {
        String className = object.getClass().getName();

        do {
            String propertyName = readInputString(String.format(PLEASE_ENTER_A_PROPERTY_NAME_PATTERN, className));
            if (propertyName.isEmpty()) break;

            // Check property name correctness
            ObjectProperty objectProperty = object.checkProperty(propertyName);
            String errorMessage = objectProperty.getErrorMessage();
            if (errorMessage.isEmpty()) {
                printMessage(String.format(PROPERTY_DESCRIPTION_PATTERN, objectProperty.getPropertyType(),
                        objectProperty.getRealPropertyName(), className));
            } else {
                printMessage(errorMessage);
            }
        } while (true);
    }

    public static String repeatString(String string, int times) {
        // Some variant BEFORE Java 8 - store here just as an example of an alternative way
        // return String.format(String.format("%%%ds", times), "").replace(" ", string);
        return String.join("", java.util.Collections.nCopies(times, string));
    }

    public static String repeatChar(Character character, int times) {
        return repeatString(character.toString(), times);
    }

    public static String stringStart(String string, int length) {
        return (string.length() > length) ? string.substring(0, length) : string;
    }

    public static Integer parseInt(String data) {
        Integer result;

        try {
            result = Integer.parseInt(data);

        } catch (NullPointerException | NumberFormatException e) {
            result = null;
        }

        return result;
    }

    public static LocalDate DateToLocalDate(Date date) {
        Instant instant = date.toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());

        return zonedDateTime.toLocalDate();
    }

    public static Date dateAdd(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, days);

        return calendar.getTime();
    }

    public static long dateSub(Date date1, Date date2) {
        return Util.DateToLocalDate(date2).until(Util.DateToLocalDate(date1), DAYS);
    }

    public static String getApplicationName() {
        String result;

        try {
            result = getApplicationMainClass().getProtectionDomain().getCodeSource().getLocation().getFile();
        } catch (NullPointerException e) {
            // Unfortunately, it is difficult to recognize why when it is executed "under IntelliJ IDEA",
            // class.getProtectionDomain() could return null, but, in this case, there is kind of compromise -
            // just let return the name of the main class
            result = getApplicationMainClassName();
        }

        return result;
    }

    public static String getResourceFilePath(String fileName) {
        URL url = Util.class.getClassLoader().getResource(fileName);

        return (url != null) ? url.getFile() : null;
    }

    public static String getApplicationMainClassName() {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        StackTraceElement main = stack[stack.length - 1];

        return main.getClassName();
    }

    public static Class getApplicationMainClass() {
        Class result;

        try {
            result = Class.forName(getApplicationMainClassName());
        } catch (ClassNotFoundException e) {
            // Unfortunately, it is difficult to recognize what could be the reason of such situation, but
            // now try to get it from stack directly
            StackTraceElement[] stack = Thread.currentThread().getStackTrace();
            StackTraceElement main = stack[stack.length - 1];
            result = main.getClass();
        }

        return result;
    }

    public static long getNanoTime() {
        return System.nanoTime();
    }
}