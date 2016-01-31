package com.company.utils;

/**
 * Created by Yevgen on 23.01.2016 as a part of the project "Unit8_Homework".
 */
public class Table {
    public final static Character ROW_CHARACTER = '-';
    public final static Character COLUMN_CHARACTER = '|';
    public final static Character CORNER_CHARACTER = '+';

    public static String line(int length) {
        return Utils.repeatChar(ROW_CHARACTER, length);
    }

    public static String tableLine(int width) {
        return CORNER_CHARACTER + line(width - 2) + CORNER_CHARACTER;
    }

    public static String tableRow(String rowData, int width) {
        int dataLength = width - 2;

        return String.format(String.format("%c%%-%ds%c", COLUMN_CHARACTER, dataLength, COLUMN_CHARACTER),
                Utils.stringStart(rowData, dataLength));
    }

    public static void printTableRow(String rowData, int width) {
        Utils.printMessage(tableRow(rowData, width));
    }

    public static void printTableRowBorder(int width) {
        Utils.printMessage(tableLine(width));
    }

    public static void printTableHeader(String header, int width) {
        printTableRowBorder(width);
        printTableRow(header, width);
        printTableRowBorder(width);
    }
}
