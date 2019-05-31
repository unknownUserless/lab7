package com.github.unknownUserless.lab7.server.connection;

import com.github.unknownUserless.lab7.client.connection.packets.request.CommandPack;
import com.github.unknownUserless.lab7.server.commands.SquadsExecutor;
import com.github.unknownUserless.lab7.client.connection.packets.respond.CommandRespondPack;
import com.github.unknownUserless.lab7.server.sql.User;
import com.github.unknownUserless.wrappers.Pair;

import java.io.IOException;

public class Respond implements Runnable{

    private static SquadsExecutor executor;

    private Pair<CommandPack, User> params;
    private Connector.Sender sender;

    public Respond(Pair<CommandPack, User> params, Connector.Sender sender) {
        if (executor == null){
            executor = new SquadsExecutor();
        }
        this.params = params;
        this.sender = sender;
    }

    @Override
    public void run(){
        try {
            CommandRespondPack packToSend = executor.execute(params);
            sender.send(packToSend);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
