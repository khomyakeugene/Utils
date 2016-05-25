package com.company.utils;

import java.util.Arrays;

/**
 * Created by Yevgen on 20.03.2016 as a part of the project "JEE_Homework_1".
 */
public class TableBuilder {
    private final static Character ROW_CHARACTER = '-';
    private final static Character COLUMN_CHARACTER = '|';
    private final static Character CORNER_CHARACTER = '+';
    private final static String COLUMN_FIELD = " ";

    private static int getColumnWidth(String[][] tableData, int columnNumber) {
        return Util.getLengthOfLongestString(Util.convertColumnToStringArray(tableData, columnNumber));
    }

    private static int[] getColumnWidth(String[][] tableData) {
        // Calc column width supposing that all the lines have the same length
        int[] result = new int[tableData[0].length];
        for (int i = 0; i < result.length; i++) {
            result[i] = getColumnWidth(tableData, i);
        }

        return result;
    }

    private static String tableLine(int[] columnWidth) {
        int columnFieldLength = COLUMN_FIELD.length();
        int lineLength = Arrays.stream(columnWidth).sum() + ((columnFieldLength << 1) + 1) * columnWidth.length + 1;
        char[] charLine = new char[lineLength];
        charLine[0] = CORNER_CHARACTER;
        for (int i = 0, j = 1; i < columnWidth.length; i++) {
            for (int k = -(columnFieldLength<<1); k < columnWidth[i]; k++) {
                charLine[j++] = ROW_CHARACTER;
            }
            charLine[j++] = CORNER_CHARACTER;
        }

        return new String(charLine);
    }

    public static String[] buildTable(String[][] tableData, AlignmentType alignmentType, boolean useRowDelimiter) {
        String[] result = new String[useRowDelimiter ? ((tableData.length<<1) + 1) : tableData.length + 3];

        // Calc column width supposing that all the lines have the same length
        int[] columnWidth = getColumnWidth(tableData);
        // By rows
        String tabLine = tableLine(columnWidth);
        int k = 0;
        for (int i = 0; i < tableData.length; i++, k++) {
            if (useRowDelimiter || i < 2) {
                result[k++] = tabLine;
            }
            result[k] = COLUMN_CHARACTER.toString();
            // By columns
            for (int j = 0; j < tableData[i].length; j++) {
                result[k] += COLUMN_FIELD + ((i == 0 || j == 0) ? AlignmentType.LEFT : alignmentType).
                        alignString(Util.toStringMaskNullAsEmpty(tableData[i][j]), columnWidth[j]) + COLUMN_FIELD +
                        COLUMN_CHARACTER;
            }
        }
        result[k] = tabLine;

        return result;
    }

    public static String[] buildTable(String[][] tableData, AlignmentType alignmentType) {
        return buildTable(tableData, alignmentType, true);
    }
}
