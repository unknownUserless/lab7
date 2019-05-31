package com.github.unknownUserless.lab7.server.sql;

import com.github.unknownUserless.lab7.client.history.Human;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class HumanArray implements Array {

    private String[] strings;

    private List<Human> people;

    public List<Human> getPeople() {
        return people;
    }

    public HumanArray(List<Human> people) {
        this.people = people;
        this.strings = new String[people.size()];
        for (int i = 0; i < people.size(); i++) {
            String prof = people.get(i).getProf() == null ? "" : people.get(i).getProf().name().toUpperCase();

            strings[i] = people.get(i).getName() + ":" + prof;
        }
    }

    @Override
    public String getBaseTypeName() throws SQLException {
        return "VARCHAR";
    }

    @Override
    public int getBaseType() throws SQLException {
        return Types.VARCHAR;
    }

    @Override
    public Object getArray() throws SQLException {
        return strings == null ? null : Arrays.copyOf(strings, strings.length);
    }

    @Override
    public Object getArray(Map<String, Class<?>> map) throws SQLException {
        return getArray();
    }

    @Override
    public Object getArray(long l, int i) throws SQLException {
        return strings == null ? null : Arrays.copyOfRange(strings, (int) l, i);
    }

    @Override
    public Object getArray(long l, int i, Map<String, Class<?>> map) throws SQLException {
        return getArray(l, i);
    }

    @Override
    public ResultSet getResultSet() throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResultSet getResultSet(Map<String, Class<?>> map) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResultSet getResultSet(long l, int i) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public ResultSet getResultSet(long l, int i, Map<String, Class<?>> map) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void free() throws SQLException {

    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ", "{", "}");
        for (String str : strings) {
            joiner.add(str);
        }
        return joiner.toString();
    }
}
