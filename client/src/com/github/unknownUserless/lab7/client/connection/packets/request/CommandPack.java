package com.github.unknownUserless.lab7.client.connection.packets.request;

import com.github.unknownUserless.lab7.client.connection.packets.Packet;

public class CommandPack implements Packet {
    public final String command;
    public final String arguments;
    public final Object attachment;

    public CommandPack(String command, String arguments, Object attachment) {
        this.command = command;
        this.arguments = arguments;
        this.attachment = attachment;
    }
}
