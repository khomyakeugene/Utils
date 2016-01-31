package com.company.utils;

import java.util.AbstractCollection;

/**
 * Created by Yevgen on 17.01.2016 as a part of the project "Unit8_Homework".
 */
public class Collections {
    public static void printList(AbstractCollection<? extends Object> list) {
        list
                .stream()
                .forEach(p -> Utils.printMessage(p.toString()));
    }

    public static void printListAsTable(AbstractCollection<? extends Object> list, String header, int width) {
        // Header
        Table.printTableHeader(header, width);

        // The data
        list
                .stream()
                .forEach(p -> Table.printTableRow(p.toString(), width));

        // Footer
        Table.printTableRowBorder(width);
    }
}
