package com.github.unknownUserless.lab7.client.connection.packets.respond;

import com.github.unknownUserless.lab7.client.connection.packets.Packet;

import java.net.SocketAddress;

public class ConnectRespondPack implements Packet {
    public final SocketAddress address;
    public final String respond;

    public ConnectRespondPack(SocketAddress address, String respond) {
        this.address = address;
        this.respond = respond;
    }
}
