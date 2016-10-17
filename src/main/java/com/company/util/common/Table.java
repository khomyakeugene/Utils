package com.company.util.common;

/**
 * Created by Yevgen on 23.01.2016 as a part of the project "Unit8_Homework".
 */
public class Table {
    private final static Character ROW_CHARACTER = '-';
    private final static Character COLUMN_CHARACTER = '|';
    private final static Character CORNER_CHARACTER = '+';

    private static String line(int length) {
        return Util.repeatChar(ROW_CHARACTER, length);
    }

    private static String tableLine(int width) {
        return CORNER_CHARACTER + line(width - 2) + CORNER_CHARACTER;
    }

    private static String tableRow(String rowData, int width) {
        int dataLength = width - 2;

        return String.format(String.format("%c%%-%ds%c", COLUMN_CHARACTER, dataLength, COLUMN_CHARACTER),
                Util.stringStart(rowData, dataLength));
    }

    public static void printTableRow(String rowData, int width) {
        Util.printMessage(tableRow(rowData, width));
    }

    public static void printTableRowBorder(int width) {
        Util.printMessage(tableLine(width));
    }

    public static void printTableHeader(String header, int width) {
        printTableRowBorder(width);
        printTableRow(header, width);
        printTableRowBorder(width);
    }
}
