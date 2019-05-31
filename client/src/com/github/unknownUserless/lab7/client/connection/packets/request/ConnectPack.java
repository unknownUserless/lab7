package com.github.unknownUserless.lab7.client.connection.packets.request;

import com.github.unknownUserless.lab7.client.connection.packets.Packet;

import java.io.Serializable;
import java.net.SocketAddress;

public class ConnectPack implements Packet {

    public final SocketAddress address;

    public ConnectPack(SocketAddress address){
        this.address = address;
    }
}
