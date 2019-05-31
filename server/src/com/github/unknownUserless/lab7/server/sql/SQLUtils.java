package com.github.unknownUserless.lab7.server.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class SQLUtils {
    private SQLUtils(){}

    public static String getTableAsString(ResultSet set){
        try {
            int count = set.getMetaData().getColumnCount();
            String[] headers = new String[count];
            int[] sizes = new int[count];
            StringBuilder result = new StringBuilder();
            List<String[]> table = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                headers[i] = " " + set.getMetaData().getColumnName(i + 1) + " ";
                sizes[i] = headers[i].length();
            }
            int rows = 0;
            while (set.next()) {
                String[] values = new String[count];
                for (int i = 0; i < count; i++) {
                    values[i] = " " + set.getString(i + 1) + " ";
                    if (sizes[i] < values[i].length()) {
                        sizes[i] = values[i].length();
                    }
                }
                rows++;
                table.add(values);
            }
            StringJoiner formatJoiner = new StringJoiner("|");
            StringJoiner lineJoiner = new StringJoiner("+");
            for (int size: sizes){
                formatJoiner.add("%" + size + "s");
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < size; i++){
                    builder.append("-");
                }
                lineJoiner.add(builder);
            }

            String line = lineJoiner.toString();
            String format = formatJoiner.toString();

            result.append(String.format(format, headers)).append("\n");
            for (int i = 0; i < rows; i++){
                result.append(line).append("\n");
                result.append(String.format(format, table.get(i))).append("\n");
            }
            return result.toString();
        } catch (SQLException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }



}
