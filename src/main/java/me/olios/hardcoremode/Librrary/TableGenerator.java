package me.olios.hardcoremode.Librrary;

import me.olios.hardcoremode.Main;

import java.util.List;

public class TableGenerator {

    private int PADDING_SIZE = 1;
    private String NEW_LINE = "\n";
    private String TABLE_JOINT_SYMBOL = "+";
    private String TABLE_V_SPLIT_SYMBOL = "|";
    private String TABLE_H_SPLIT_SYMBOL = "-";

    private int TABLE_WIDTH = 0;

    public String generateTable(String header,
                                List<String> rows)
    {
        StringBuilder stringBuilder = new StringBuilder();
        tableWidth(header, rows);

        stringBuilder.append(NEW_LINE);
        stringBuilder.append(separator());
        stringBuilder.append(createLine(header, true));
        stringBuilder.append(separator());
        for (String row : rows) stringBuilder.append(createLine(row, false));
        stringBuilder.append(separator());
        stringBuilder.append(NEW_LINE);

        return stringBuilder.toString();
    }

    private String separator()
    {
        String separator = TABLE_JOINT_SYMBOL;
        for (int i = 1; i <= TABLE_WIDTH; i++) separator += TABLE_H_SPLIT_SYMBOL;
        separator += TABLE_JOINT_SYMBOL;

        separator += NEW_LINE;

        return separator;
    }

    private String createLine(String str, boolean center)
    {
        String line = TABLE_V_SPLIT_SYMBOL + " ";
        line += str;

        int spaces = TABLE_WIDTH - line.length();
        Main.l(spaces);
        for (int i = 0; i < spaces; i++) line += " ";
        line += Main.ANSI_RESET + " ";

        line += TABLE_V_SPLIT_SYMBOL;

        line += NEW_LINE;

        return line;
    }

    private void tableWidth(String header, List<String> rows)
    {
        int maxWidth = header.length() + 2;

        for (String row : rows)
        {
            if (row.length() + 2 > maxWidth) maxWidth = row.length() + 2;
        }

        TABLE_WIDTH = maxWidth;
    }

//    public String generateTable(List<String> headersList, List<List<String>> rowsList, int... overRiddenHeaderHeight)
//    {
//        StringBuilder stringBuilder = new StringBuilder();
//
//        int rowHeight = overRiddenHeaderHeight.length > 0 ? overRiddenHeaderHeight[0] : 1;
//
//        Map<Integer,Integer> columnMaxWidthMapping = getMaximumWidthOfTable(headersList, rowsList);
//
//        stringBuilder.append(NEW_LINE);
//        stringBuilder.append(NEW_LINE);
//        createRowLine(stringBuilder, headersList.size(), columnMaxWidthMapping);
//        stringBuilder.append(NEW_LINE);
//
//
//        for (int headerIndex = 0; headerIndex < headersList.size(); headerIndex++) {
//            fillCell(stringBuilder, headersList.get(headerIndex), headerIndex, columnMaxWidthMapping);
//        }
//
//        stringBuilder.append(NEW_LINE);
//
//        createRowLine(stringBuilder, headersList.size(), columnMaxWidthMapping);
//
//
//        for (List<String> row : rowsList) {
//
//            for (int i = 0; i < rowHeight; i++) {
//                stringBuilder.append(NEW_LINE);
//            }
//
//            for (int cellIndex = 0; cellIndex < row.size(); cellIndex++) {
//                fillCell(stringBuilder, row.get(cellIndex), cellIndex, columnMaxWidthMapping);
//            }
//
//        }
//
//        stringBuilder.append(NEW_LINE);
//        createRowLine(stringBuilder, headersList.size(), columnMaxWidthMapping);
//        stringBuilder.append(NEW_LINE);
//        stringBuilder.append(NEW_LINE);
//
//        return stringBuilder.toString();
//    }
//
//    private void fillSpace(StringBuilder stringBuilder, int length)
//    {
//        for (int i = 0; i < length; i++) {
//            stringBuilder.append(" ");
//        }
//    }
//
//    private void createRowLine(StringBuilder stringBuilder,int headersListSize, Map<Integer,Integer> columnMaxWidthMapping)
//    {
//        for (int i = 0; i < headersListSize; i++) {
//            if(i == 0)
//            {
//                stringBuilder.append(TABLE_JOINT_SYMBOL);
//            }
//
//            for (int j = 0; j < columnMaxWidthMapping.get(i) + PADDING_SIZE * 2 ; j++) {
//                stringBuilder.append(TABLE_H_SPLIT_SYMBOL);
//            }
//            stringBuilder.append(TABLE_JOINT_SYMBOL);
//        }
//    }
//
//
//    private Map<Integer,Integer> getMaximumWidthOfTable(List<String> headersList, List<List<String>> rowsList)
//    {
//        Map<Integer,Integer> columnMaxWidthMapping = new HashMap<>();
//
//        for (int columnIndex = 0; columnIndex < headersList.size(); columnIndex++) {
//            columnMaxWidthMapping.put(columnIndex, 0);
//        }
//
//        for (int columnIndex = 0; columnIndex < headersList.size(); columnIndex++) {
//
//            if(headersList.get(columnIndex).length() > columnMaxWidthMapping.get(columnIndex))
//            {
//                columnMaxWidthMapping.put(columnIndex, headersList.get(columnIndex).length());
//            }
//        }
//
//        for (List<String> row : rowsList) {
//
//            for (int columnIndex = 0; columnIndex < row.size(); columnIndex++) {
//                if(row.get(columnIndex).length() > columnMaxWidthMapping.get(0))
//                {
//                    columnMaxWidthMapping.put(columnIndex, row.get(columnIndex).length());
//                }
//            }
//        }
//
//        for (int columnIndex = 0; columnIndex < headersList.size(); columnIndex++) {
//
//            if(columnMaxWidthMapping.get(columnIndex) % 2 != 0)
//            {
//                columnMaxWidthMapping.put(columnIndex, columnMaxWidthMapping.get(columnIndex) + 1);
//            }
//        }
//
//
//        return columnMaxWidthMapping;
//    }
//
//    private int getOptimumCellPadding(int cellIndex,int datalength,Map<Integer,Integer> columnMaxWidthMapping,int cellPaddingSize)
//    {
//        if(datalength % 2 != 0)
//        {
//            datalength++;
//        }
//
//        if(datalength < columnMaxWidthMapping.get(cellIndex))
//        {
//            cellPaddingSize = cellPaddingSize + (columnMaxWidthMapping.get(cellIndex) - datalength) / 2;
//        }
//
//        return cellPaddingSize;
//    }
//
//    private void fillCell(StringBuilder stringBuilder, String cell, int cellIndex, Map<Integer,Integer> columnMaxWidthMapping)
//    {
//
//        int cellPaddingSize = getOptimumCellPadding(cellIndex, cell.length(), columnMaxWidthMapping, PADDING_SIZE);
//
//        if(cellIndex == 0)
//        {
//            stringBuilder.append(TABLE_V_SPLIT_SYMBOL);
//        }
//
//        fillSpace(stringBuilder, PADDING_SIZE);
//        stringBuilder.append(cell);
//
//        // Remove the conditional logic for odd-length strings
//        fillSpace(stringBuilder, columnMaxWidthMapping.get(cellIndex) - cell.length() + PADDING_SIZE);
//
//        stringBuilder.append(TABLE_V_SPLIT_SYMBOL);
//    }
}