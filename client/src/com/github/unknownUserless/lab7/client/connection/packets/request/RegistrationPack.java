package com.github.unknownUserless.lab7.client.connection.packets.request;

import com.github.unknownUserless.lab7.client.connection.packets.Packet;

public class RegistrationPack implements Packet {
    public final String login;
    public final String email;

    public RegistrationPack(String login, String email) {
        this.login = login;
        this.email = email;
    }
}
