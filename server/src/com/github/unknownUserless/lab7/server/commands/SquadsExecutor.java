package com.github.unknownUserless.lab7.server.commands;

import com.github.unknownUserless.lab7.client.connection.packets.request.CommandPack;
import com.github.unknownUserless.lab7.client.connection.packets.respond.CommandRespondPack;
import com.github.unknownUserless.lab7.server.sql.User;
import com.github.unknownUserless.wrappers.Pair;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SquadsExecutor {

    private Map<String, Method> methods;


    public SquadsExecutor() {
        methods = new HashMap<>();
        for (Method m: SquadCommands.class.getDeclaredMethods()){
            if (m.isAnnotationPresent(Command.class)){
                m.setAccessible(true);
                Command command = m.getAnnotation(Command.class);
                if (command.names().length == 0) throw new RuntimeException("Проверьте аннотации" +
                        "в классе " + SquadCommands.class.getSimpleName());
                for (String name: command.names()){
                    this.methods.put(name, m);
                }
            }
        }
    }

    public CommandRespondPack execute(Pair<CommandPack, User> args) {

        Method m = methods.get(args.element().command);

        if (m != null){
            try {
                return (CommandRespondPack) m.invoke(null, args);
            } catch (InvocationTargetException | IllegalAccessException e){
                return new CommandRespondPack("Произошла ошибка на сервере: " + e.getMessage());
            }
        } else {
            return new CommandRespondPack("Команда " + args.element().command + " не найдена," +
                    "чтобы получить справку по командам, введите help");
        }
    }

}

