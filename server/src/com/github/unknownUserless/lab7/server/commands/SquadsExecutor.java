package com.github.unknownUserless.lab7.server.commands;

import com.github.unknownUserless.lab7.client.connection.packets.request.CommandPack;
import com.github.unknownUserless.lab7.client.connection.packets.respond.CommandRespondPack;
import com.github.unknownUserless.lab7.server.sql.User;
import com.github.unknownUserless.wrappers.Pair;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SquadsExecutor {

    private List<Method> methods;


    public SquadsExecutor() {
        this.methods = Arrays.stream(SquadCommands.class.getDeclaredMethods()).
                filter( m -> m.isAnnotationPresent(Command.class)).collect(Collectors.toList());
    }

    public CommandRespondPack execute(Pair<CommandPack, User> args) {
        for (Method m : methods) {
            if (m.isAnnotationPresent(Command.class)) {
                Command command = m.getAnnotation(Command.class);
                if (Arrays.asList(command.names()).contains(args.element().command)) {
                    try {
                        m.setAccessible(true);
                        CommandRespondPack respondPack = (CommandRespondPack) m.invoke(null, args);

                        return respondPack;

                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
        }
        return new CommandRespondPack("Команда " + args.element().command + " не найдена," +
                "чтобы получить справку по командам, введите help");
    }

}

