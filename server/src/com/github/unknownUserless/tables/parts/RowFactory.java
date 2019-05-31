package com.github.unknownUserless.tables.parts;

import java.util.*;
import java.util.stream.Collectors;

public class RowFactory {

    public static Builder builder(List<Integer> columnLengths) {
        return new RowFactory(columnLengths).new Builder();
    }

     private RowFactory(List<Integer> columnLengths) {
        this.columnLengths = columnLengths;
    }

    public class Builder {
        private Builder() {
        }

        public Builder addLine(Line line) {
            RowFactory.this.line = line;
            return Builder.this;
        }

        public Builder addLeftChar(Character ch) {
            RowFactory.this.leftChar = ch.toString();
            return Builder.this;
        }

        public Builder addRightChar(Character ch) {
            RowFactory.this.rightChar = ch.toString();
            return Builder.this;
        }

        public Builder addMiddleChar(Character ch) {
            RowFactory.this.middleChar = ch.toString();
            return Builder.this;
        }

        public RowFactory build() {
            return RowFactory.this;
        }
    }

    private Line line = Line.defaultLine();
    private List<Integer> columnLengths;
    private String leftChar = "|";
    private String rightChar = "|";
    private String middleChar = "|";

    public Row row(List<String> objects) {
        return new Row(objects);
    }

    public class Row {
        private List<String> strings;
        private String format;

        private Row(List<String> objects) {
            //

            strings = new ArrayList<>();
            for (String str: objects){
                strings.add(str);
            }



            //
            //this.strings = objects.stream().map(Object::toString).collect(Collectors.toList());
            StringBuilder format = new StringBuilder();
            format.append(leftChar);
            for (Integer i : columnLengths) {
                format.append(" %").append(i).append("s").append(middleChar);
            }
            format.delete(format.length() - middleChar.length(), format.length());
            format.append(rightChar);
            this.format = format.toString();
        }

        private void setFormat() {

        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            for (Integer i : columnLengths) {
                builder.append(RowFactory.this.line.getLine(i));
            }
            builder.append(RowFactory.this.line.endChar()).append("\n");

            List<List<String>> minitable = new ArrayList<>();

            int rowsCount = 0;
            for (int i = 0; i < strings.size(); i++) {
                List<String> rows = formRows(strings.get(i), columnLengths.get(i));
                if (rows.size() > rowsCount) {
                    rowsCount = rows.size();
                }
                minitable.add(rows);
            }

            for (int j = 0; j < rowsCount; j++) {
                List<String> values = new ArrayList<>();
                if (j != 0){
                    builder.append("\n");
                }
                for (int i = 0; i < strings.size(); i++) {
                    try {
                        values.add(minitable.get(i).get(j));
                    } catch (IndexOutOfBoundsException e) {
                        values.add("");
                    }
                }
                builder.append(String.format(format, values.toArray()));
            }

            return builder.toString();
        }
    }

    private String[] getWords(String str) {
        try {
            return str.split(" ");
        } catch (NullPointerException e){
            return new String[]{""};
        }
    }

    private List<String> formRows(String string, int maxLength) {
        Queue<String> words = new LinkedList<>(Arrays.asList(getWords(string)));
        List<String> result = new ArrayList<>();

        Iterator<String> wordIter = words.iterator();

        StringBuilder builder = new StringBuilder();
        int length = 0;
        while (wordIter.hasNext()) {
            String word = wordIter.next();
            if (length + word.length() < maxLength) {
                length += word.length() + 1;
                builder.append(word).append(" ");

            } else {
                result.add(builder.toString());
                builder = new StringBuilder();
                length = word.length() + 1;
                builder.append(word).append(" ");
            }
        }
        result.add(builder.toString());
        return result;
    }
}








