package com.github.unknownUserless.lab7.server;

import com.github.unknownUserless.lab7.client.connection.packets.request.CommandPack;
import com.github.unknownUserless.lab7.client.connection.packets.respond.CommandRespondPack;
import com.github.unknownUserless.lab7.server.commands.SquadsExecutor;
import com.github.unknownUserless.lab7.server.sql.JDBCWorker;
import com.github.unknownUserless.lab7.server.sql.User;
import com.github.unknownUserless.wrappers.Pair;

public class Test {
    public static void main(String[] args) throws Exception {


    }

    private static void executor() {

        String arguments = "{\"name\":\"Beauty\", \"members\":[{\"name\":\"Sam\", \"prof\":\"MECHANIC\"}, {\"name\":\"John\"}], \"location\":\"MOON\"}";
        CommandPack pack = new CommandPack("add", arguments, null);


        CommandPack comPack = new CommandPack("ls",
                null, null);
        User user = new User("mail", "login", "pass");

        JDBCWorker.instance().users().insert(user);

        SquadsExecutor executor = new SquadsExecutor();


        Pair<CommandPack, User> pair1 = new Pair<>(pack, user);
        CommandRespondPack commandRespondPack = executor.execute(pair1);

        Pair<CommandPack, User> pair = new Pair<>(comPack, user);
        CommandRespondPack pcak = executor.execute(pair);
        System.out.println(commandRespondPack.getRespond());

        System.out.println(pcak.getRespond());

    }
}

//{"name":"Beauty", "members":[{"name":"Sam", "prof":"MECHANIC"}, {"name":"John"}], "location":"MOON"}