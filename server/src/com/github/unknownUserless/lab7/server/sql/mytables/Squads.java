package com.github.unknownUserless.lab7.server.sql.mytables;

import com.github.unknownUserless.lab7.client.history.*;
import com.github.unknownUserless.lab7.server.collection.SquadMaker;
import com.github.unknownUserless.lab7.server.sql.*;
import com.github.unknownUserless.wrappers.Pair;

import java.sql.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Squads {

    private Connection connection;

    public Squads(Connection connection){
        this.connection = connection;
    }

    public void insert(Squad squad, String login){
        try(PreparedStatement statement = connection.
                prepareStatement("INSERT INTO squads (name, members, location, birthday, owner) " +
                        "VALUES (?, ?, ?, ?, ?)")){

            String location = squad.getLocation() == null ? "null" : squad.getLocation().toString().toUpperCase();
            statement.setString(1, squad.getName());
            statement.setArray(2, new HumanArray(squad.getMembers()));
            statement.setString(3, location);
            statement.setString(4, squad.getBirthday().toString());
            statement.setString(5, login);
            statement.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public Squad get(Object o) {
        throw new UnsupportedOperationException();
    }

    public boolean remove(Squad squad){
        try (PreparedStatement statement = connection.
                prepareStatement("DELETE FROM squads WHERE name = ?")){

            statement.setString(1, squad.getName());
            statement.executeUpdate();
            return true;

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean remove(String squadName){
        try(PreparedStatement statement = connection.
                prepareStatement("DELETE FROM squads WHERE name = ?")){

            statement.setString(1, squadName);
            statement.executeUpdate();
            return true;

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean containing(Object o) {
        String name = (String)o;
        try(PreparedStatement statement = connection.
                prepareStatement("SELECT * FROM squads WHERE name = ?")){

            statement.setString(1, name);
            ResultSet set = statement.executeQuery();
            if (set.next()) return true;

        } catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public List<Pair<Squad, String>> list(){

        try(Statement statement = connection.createStatement()){

            ResultSet set = statement.executeQuery("SELECT * FROM squads");
            List<Pair<Squad, String>> result = new ArrayList<>();

            while (set.next()){
                String name = set.getString("name");
                List<Human> crew = SquadMaker.fromSqlArray((String[])set.getArray("members").getArray());

                Location location;
                switch (set.getString("location")){
                    case "MARS":
                        location = Mars.instance();
                        break;
                    case "MOON":
                        location = Moon.instance();
                        break;
                    case "EARTH":
                        location = Earth.instance();
                        break;
                     default:
                         location = null;
                         break;
                }
                OffsetDateTime birthday = OffsetDateTime.parse(set.getString("birthday"));

                String login = set.getString("owner");

                Squad squad = SquadMaker.makeSquad(name, crew, location, birthday);

                result.add(new Pair<>(squad, login));
            }

            return result;

        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }


    @Override
    public String toString(){
        try(Statement st = connection.createStatement()){
            ResultSet set = st.executeQuery("SELECT * FROM squads");
            return SQLUtils.getTableAsString(set);
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }
}
