package com.github.unknownUserless.lab7.client.connection.packets.respond;

import com.github.unknownUserless.lab7.client.connection.packets.Packet;

public class AuthorizationRespondPack implements Packet {
    public final boolean success;

    public AuthorizationRespondPack(boolean success) {
        this.success = success;
    }
}
