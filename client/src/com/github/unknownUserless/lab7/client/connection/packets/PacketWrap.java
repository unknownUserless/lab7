package com.github.unknownUserless.lab7.client.connection.packets;

import com.github.unknownUserless.lab7.client.connection.packets.Packet;

public class PacketWrap<T extends Packet> {
    private T packet;

    public PacketWrap(T packet) {
        this.packet = packet;
    }


    public T getPacket() {
        return packet;
    }
}
