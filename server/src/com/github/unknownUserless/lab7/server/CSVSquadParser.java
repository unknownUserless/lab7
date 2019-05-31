package com.github.unknownUserless.lab7.server;

import com.github.unknownUserless.lab7.client.history.*;

import java.lang.reflect.Field;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class CSVSquadParser {

    private static final String separator = ";";

    private CSVSquadParser(){}

    public static List<Squad> parse(String[] strings) throws NoSuchFieldException,
            IllegalAccessException, DateTimeParseException {
        if (strings.length == 0) throw new IllegalArgumentException("array is empty");
        if (strings.length == 1) return new ArrayList<>();
        String[] headers = strings[0].split(separator);

        Field location = Squad.class.getDeclaredField("location");
        location.setAccessible(true);
        Field birthday = Squad.class.getDeclaredField("birthday");
        birthday.setAccessible(true);

        List<Squad> result = new ArrayList<>();

        for (int i = 1; i < strings.length; i++){
            String[] values = strings[i].replace("\"", "").
                    replace(" ", "").split(separator);
            if (values.length != 4) continue;
            Squad squad = new Squad(values[0], getPeople(values[1]));
            try {
                OffsetDateTime time = OffsetDateTime.parse(values[2]);
                birthday.set(squad, time);
            } catch (DateTimeParseException e){

            }

            Location loc;
            switch (values[3]){
                case "MOON":
                    loc = Moon.instance();
                    break;
                case "EARTH":
                    loc = Earth.instance();
                    break;
                case "MARS":
                    loc = Mars.instance();
                    break;
                default:
                    loc = null;
            }
            location.set(squad, loc);
            result.add(squad);
        }
        return result;
    }

    private static List<Human> getPeople(String str){
        String[] sHumans = str.replace("\"", "").
                replace(" ", "").split(",");
        List<Human> result = new ArrayList<>();

        for (String s: sHumans){
            String[] arr = s.split(":");
             if (arr.length == 1){
                 result.add(new Human(arr[0]));
             } else {
                 result.add(new Human(arr[0], Human.Profession.valueOf(arr[1])));
             }
        }
        return result;
    }



}
