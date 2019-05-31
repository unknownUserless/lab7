package com.github.unknownUserless.lab7.client.connection.packets.request;

import com.github.unknownUserless.lab7.client.connection.packets.Packet;

public class LoginPack implements Packet {
    private final char[] password;
    public final String login;

    public LoginPack(String login, char[] password) {
        this.password = password;
        this.login = login;
    }

    public char[] password(){
        return password;
    }
}
