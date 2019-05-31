package com.github.unknownUserless.lab7.server.sql;

public class User extends UserCard{

    private String password;

    public User(String email, String login,  String password) {

        super(email, login);
        this.password = password;
    }

    public String password() {
        return password;
    }
}
