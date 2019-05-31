package com.github.unknownUserless.lab7.client.connection.packets.respond;

import com.github.unknownUserless.lab7.client.connection.packets.Packet;
import com.github.unknownUserless.lab7.client.history.Squad;

public class CommandRespondPack implements Packet {
    private String respond;
    private Squad squad;

    public CommandRespondPack(String respond) {
        this.respond = respond;
    }

    public String getRespond() {
        return respond;
    }

    public Squad getSquad() {
        return squad;
    }

    public void setSquad(Squad squad) {
        this.squad = squad;
    }
}
