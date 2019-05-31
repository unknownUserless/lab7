package com.github.unknownUserless.tables.parts;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Line {

    private static Line defaultLine;
    public static Line defaultLine(){
        if (defaultLine == null){
            defaultLine  = new Line('+', '-');
        }
        return defaultLine;
    }

    private Character begchar;
    private Character ch;

    public Line(char beginChar, char ch){
        this.begchar = beginChar;
        this.ch = ch;
    }

    public String endChar(){
        return begchar.toString();
    }

    public String getLine(int length){
        return begchar.toString() + IntStream.range(0, length+1).
                <String>mapToObj((i)->ch.toString()).
                collect(Collectors.joining());
    }

}
