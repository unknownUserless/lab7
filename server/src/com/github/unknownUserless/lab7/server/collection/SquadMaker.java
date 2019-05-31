package com.github.unknownUserless.lab7.server.collection;

import com.github.unknownUserless.lab7.client.history.Human;
import com.github.unknownUserless.lab7.client.history.Location;
import com.github.unknownUserless.lab7.client.history.Squad;

import java.lang.reflect.Field;
import java.sql.Array;
import java.text.NumberFormat;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class SquadMaker {

    private SquadMaker() {
    }

    public static Squad makeSquad(String name, List<Human> members, Location loc, OffsetDateTime birthday) {
        try {
            Squad squad = new Squad(name, members);
            squad.moveTo(loc);
            Field date = Squad.class.getDeclaredField("birthday");
            date.setAccessible(true);
            date.set(squad, birthday);
            return squad;

        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Array sqlArray(List<Human> people) {
        StringJoiner joiner = new StringJoiner(", ", "{", "}");

        for (int i = 0; i < people.size(); i++) {
            String prof = people.get(i).getProf() == null ? "" : people.get(i).getProf().name().toUpperCase();

            joiner.add(people.get(i).getName() + ":" + prof);
        }


        return null;
    }

    public static List<Human> fromSqlArray(String[] strings) {
        List<Human> people = new ArrayList<>();
        for (String s : strings) {
            String[] strs = s.replace(" ", "").split(":");
            String name = strs[0];
            if (strs.length == 1) {
                people.add(new Human(name));
            } else {
                Human.Profession profession = Human.Profession.valueOf(strs[1]);
                people.add(new Human(name, profession));
            }
        }

        return people;
    }
}
