package com.github.unknownUserless.lab7.server.sql;

public class UserCard {

    private String email;
    private String login;

    public UserCard(String email, String login) {
        this.email = email;
        this.login = login;
    }

    public String email(){
        return email;
    }

    public String login(){
        return login;
    }
}
