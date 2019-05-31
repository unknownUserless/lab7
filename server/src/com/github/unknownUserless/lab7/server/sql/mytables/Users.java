package com.github.unknownUserless.lab7.server.sql.mytables;

import com.github.unknownUserless.lab7.server.sql.User;
import com.github.unknownUserless.lab7.server.sql.UserCard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Users {

    private Connection connection;

    public Users(Connection connection) {
        this.connection = connection;
    }

    public void insert(User user) {
        try (PreparedStatement statement = connection.
                prepareStatement("INSERT INTO users (login, email, password) VALUES (?, ?, ?)")) {
            statement.setString(1, user.login());
            statement.setString(2, user.email());
            statement.setString(3, user.password());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User get(String login) {

        try (PreparedStatement statement = connection.
                prepareStatement("SELECT * FROM users WHERE login = ?")) {

            statement.setString(1, login);

            ResultSet set = statement.executeQuery();
            if (set.next()) {
                return new User(set.getString("email"), set.getString("login"),  set.getString("password"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean containing(Object o) {
        UserCard user = (UserCard) o;
        try {
            PreparedStatement statement = connection.
                    prepareStatement("SELECT * FROM users WHERE login = ?");
            statement.setString(1, user.login());

            ResultSet set1 = statement.executeQuery();
            if (set1.next()) {
                return true;
            }

            statement.close();


            statement = connection.
                    prepareStatement("SELECT * FROM users WHERE email = ?");
            statement.setString(1, user.email());
            ResultSet set2 = statement.executeQuery();
            if (set2.next()) {
                return true;
            }
            return false;


        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

}











