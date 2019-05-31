package com.github.unknownUserless.tables;

import com.github.unknownUserless.tables.parts.Line;
import com.github.unknownUserless.tables.parts.RowFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class ListTable {

    private RowFactory.Row headers;
    private List<RowFactory.Row> rows;
    private RowFactory factory;
    private String endLine;

    public ListTable(List<Integer> sizes, List<String> headers){
        if (sizes.size() != headers.size()){
            System.err.println("Error");
            throw new RuntimeException("sizes.size != headers.size");
        }
        this.rows = new ArrayList<>();
        this.factory = RowFactory.builder(sizes).build();
        this.headers = factory.row(headers);

        StringBuilder builder = new StringBuilder();
        for (Integer i: sizes){
            builder.append(Line.defaultLine().getLine(i));
        }
        builder.append(Line.defaultLine().endChar());
        this.endLine = builder.toString();
    }

    public void addRow(String... objects){
        List<String> list = Arrays.asList(objects);
        rows.add(factory.row(list));
    }

    public void addRow(List<String> strings){
        rows.add(factory.row(strings));
    }

    @Override
    public String toString(){
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add(headers.toString());
        for (RowFactory.Row row: rows) {
            joiner.add(row.toString());
        }
        joiner.add(endLine);
        return joiner.toString();
    }

}







