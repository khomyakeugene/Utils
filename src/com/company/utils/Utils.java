package com.company.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.*;

/**
 * Created by Yevgen on 06.01.2016.
 */

public class Utils {
    // public final static int LETS_THINK_THIS_IS_THE_NUMBER_OF_CURRENT_ELEMENT_OF_STACK = 1; // Reserve it for future time
    public final static int LETS_THINK_THIS_IS_THE_NUMBER_OF_CALLING_ELEMENT_OF_STACK = 2;

    public final static String CLASS_NAME_PATTERN = "Class Name: {0}";
    public final static String FULL_METHOD_NAME_PATTERN = "{0}.{1}";
    public final static String EXCEPTION_MESSAGE_PATTERN = "{0}: {1}";
    public final static String PLEASE_ENTER_A_PROPERTY_NAME_PATTERN =
            "Please, enter name of a property of class {0} (or just push <Enter> to stop):";
    public final static String PROPERTY_DESCRIPTION_PATTERN = "A {0} \"{1}\" is presented in class \"{2}\"";
    public final static String PLEASE_REPEAT_ENTER =
            "{0} was generated with data \"{1}\". Please, repeat enter action";

    public static String getProgramName() {
        return "stub";
    }

    public static String getClassNameMessage(Object object) {
        return MessageFormat.format(CLASS_NAME_PATTERN, object.getClass().getName());
    }

    public static void printMessage(String message) {
        System.out.println(message);
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

        return MessageFormat.format(
                FULL_METHOD_NAME_PATTERN,
                stackTraceElement[LETS_THINK_THIS_IS_THE_NUMBER_OF_CALLING_ELEMENT_OF_STACK].getClassName(),
                stackTraceElement[LETS_THINK_THIS_IS_THE_NUMBER_OF_CALLING_ELEMENT_OF_STACK].getMethodName());
    }

    public static String getFullMethodName(String className, String methodName) {
        return MessageFormat.format(FULL_METHOD_NAME_PATTERN, className, methodName);
    }

    public static void throwTextException(String methodName, String text) throws Exception {
        throw new Exception(MessageFormat.format(EXCEPTION_MESSAGE_PATTERN, methodName, text));
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
                printMessage(MessageFormat.format(PLEASE_REPEAT_ENTER, e.getClass().getName(), e.getMessage()));
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
            String propertyName = readInputString(MessageFormat.format(PLEASE_ENTER_A_PROPERTY_NAME_PATTERN, className));
            if (propertyName.isEmpty()) break;

            // Check property name correctness
            ObjectProperty objectProperty = object.checkProperty(propertyName);
            String errorMessage = objectProperty.getErrorMessage();
            if (errorMessage.isEmpty()) {
                printMessage(MessageFormat.format(PROPERTY_DESCRIPTION_PATTERN, objectProperty.getPropertyType(),
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
}