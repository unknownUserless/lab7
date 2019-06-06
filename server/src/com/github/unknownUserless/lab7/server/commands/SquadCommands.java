package com.github.unknownUserless.lab7.server.commands;

import com.github.unknownUserless.lab7.server.CSVSquadParser;
import com.github.unknownUserless.lab7.client.connection.packets.request.CommandPack;
import com.github.unknownUserless.lab7.client.connection.packets.respond.CommandRespondPack;
import com.github.unknownUserless.lab7.client.history.*;
import com.github.unknownUserless.lab7.server.collection.Collection;
import com.github.unknownUserless.lab7.server.connection.Main;
import com.github.unknownUserless.lab7.server.sql.JDBCWorker;
import com.github.unknownUserless.lab7.server.sql.User;
import com.github.unknownUserless.tables.ListTable;
import com.github.unknownUserless.wrappers.Pair;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.channels.SelectionKey;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Stream;

public class SquadCommands {

    private static CommandRespondPack help;
    private static final CommandRespondPack JSONHELP =
            new CommandRespondPack("fulljson - объект в формате json,\n" +
                    "который содержит все необходимые ключи и значения.\n" +
                    "shortjson - может содержать только имя\n"+
                    "Пример fulljson = {\"name\":\"example\", " +
                    "\"members\":[{\"name\":\"Name\", \"prof\":\"MECHANIC\"}], " +
                    "\"location\":\"MOON\"} (Локацию можно не указывать)\n" +
                    "Пример shortjson = {\"name\":\"example\"}");

    private SquadCommands() {
    }

    @Command(names = {"add"}, description = "Добавляет элемент в коллекцию",
            arguments = "формат fulljson")
    private static CommandRespondPack add(Pair<CommandPack, User> pair) {
        Squad squad = squad(pair.element().arguments);
        if (Collection.collection.stream().map(p -> p.element().getName()).
                anyMatch((s) -> s.equals(squad.getName()))) {

            return new CommandRespondPack("Отряд с данным именем уже существует");

        } else {
            Collection.collection.add(new Pair<>(squad, pair.attachment().login()));
            JDBCWorker.instance().squads().insert(squad, pair.attachment().login());
            return new CommandRespondPack("Добавлен отраяд: " + squad.toString());
        }
    }

    @Command(names = {"help", "man"}, description = "Выводит справку по командам," +
            "\"json\" - подробнее о форматах json",
            arguments = "\"json\"")
    private static CommandRespondPack help(Pair<CommandPack, User> pair) {
        if ("json".equals(pair.element().arguments)) {
            return JSONHELP;

        } else {

            if (help == null) {
                ListTable table = new ListTable(Arrays.asList(20, 20, 40),
                        Arrays.asList("Команды", "Аргументы", "Описание"));
                for (Method m : SquadCommands.class.getDeclaredMethods()) {
                    if (m.isAnnotationPresent(Command.class)) {
                        Command command = m.getAnnotation(Command.class);
                        table.addRow(Arrays.asList(Arrays.toString(command.names()),
                                command.arguments(), command.description()));
                    }
                }
                help = new CommandRespondPack(table.toString());
            }
            return help;
        }
    }

    @Command(names = {"remove", "delete"}, description = "Удаляет элемент из коллекции",
            arguments = "fulljson/shortjson")
    private static CommandRespondPack remove(Pair<CommandPack, User> pair) {

        JsonObject object = new JsonParser().parse(pair.element().arguments).getAsJsonObject();
        CommandRespondPack respond;
        Squad squad;
        Stream<Squad> userSquads = Collection.collection.stream().
                filter((w) -> w.attachment().equals(pair.attachment().login())).map(Pair::element);
        if (object.get("members") != null) {
            Squad pattern = squad(pair.element().arguments);
            squad = userSquads.filter((s) -> s.equals(pattern)).findFirst().orElse(null);
        } else {
            squad = userSquads.filter((s) -> s.getName().equals(object.get("name").getAsString())).
                    findFirst().orElse(null);
        }

        if (squad == null) {
            respond = new CommandRespondPack("Отряд не найден или вам не принадлежит");
        } else {
            Collection.collection.removeIf(w -> w.element().equals(squad));
            JDBCWorker.instance().squads().remove(squad.getName());
            respond = new CommandRespondPack("Отряд " + squad.toString() + " удален");
        }
        return respond;
    }

    @Command(names = {"remove_first"}, description = "Удаляет первый элемент")
    private static CommandRespondPack remove_first(Pair<CommandPack, User> pair) {
        return removesome(pair.attachment().login(), 0);
    }

    @Command(names = {"remove_last"}, description = "Удаляет последний элемент")
    private static CommandRespondPack remove_last(Pair<CommandPack, User> pair) {
        int index = Collection.collection.size() - 1;
        return removesome(pair.attachment().login(), index);
    }

    @Command(names = {"show", "ls"}, description = "Выводит содержимое коллекции")
    private static CommandRespondPack show(Pair<CommandPack, User> pair) {


        if (Collection.collection.size() == 0) {
            return new CommandRespondPack("Коллекция пустая");
        }

        List<Integer> sizes = Arrays.asList(6, 20, 25, 30, 15, 30);
        List<String> headers = Arrays.asList("Index", "Name", "Crew", "Birthday", "Location", "Owner");
        ListTable table = new ListTable(sizes, headers);
        for (int i = 0; i < Collection.collection.size(); i++) {
            Pair<Squad, String> squad = Collection.collection.get(i);

            String location = squad.element().getLocation() == null ? "null" :
                    squad.element().getLocation().toString();

            table.addRow(String.valueOf(i), squad.element().getName(),
                    Arrays.toString(squad.element().getMembers().toArray()),
                    squad.element().getBirthday().toString(),
                    location,
                    squad.attachment());
        }
        return new CommandRespondPack(table.toString());
    }

    @Command(names = {"import"}, description = "Добавляет на сервер отряды из файла пользователя")
    private static CommandRespondPack importing(Pair<CommandPack, User> pair) {
        CommandRespondPack res;
        try {
            List<Squad> squads = CSVSquadParser.parse(((String) pair.element().attachment).split("\n"));
            StringJoiner joiner = new StringJoiner(";",
                    "Добавленные отряды: ", "");
            for (Squad squad : squads) {
                if (!Collection.contains( s -> s.getName().equals(squad.getName()))) {
                    Collection.collection.add(new Pair<>(squad, pair.attachment().login()));
                    JDBCWorker.instance().squads().insert(squad, pair.attachment().login());
                    joiner.add(squad.toString());
                }
            }
            res = new CommandRespondPack(joiner.toString());
        } catch (IllegalArgumentException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            res = new CommandRespondPack("Проверьте файл на корректность");
        } catch (DateTimeParseException e){
            res = new CommandRespondPack("Время введено не верно");
        }
        return res;
    }

    @Command(names = {"exit"}, description = "Выход клинта из системы")
    private static CommandRespondPack exit(Pair<CommandRespondPack, User> pair) throws IOException {
        SelectionKey key = Main.getCurrentConnections().get(pair.attachment().login());
        if (key != null){
            key.channel().close();
            key.cancel();
        }
        return new CommandRespondPack("До свидания!");
    }

    private static CommandRespondPack removesome(String username, int index) {
        if (Collection.collection.size() != 0) {
            Pair<Squad, String> pair = Collection.collection.get(index);
            if (pair.attachment().equals(username)) {
                Squad squad = pair.element();
                Collection.collection.removeIf(p -> p.element().equals(squad));
                JDBCWorker.instance().squads().remove(squad.getName());
                return new CommandRespondPack("Отряд " + squad.toString() + " удален");
            } else {
                return new CommandRespondPack("Отряд вам не принадлежит");
            }
        } else {
            return new CommandRespondPack("Коллекция пустая");
        }
    }

    public static Squad squad(String string) throws IllegalArgumentException {
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(string);
        JsonObject object = element.getAsJsonObject();

        JsonElement oname = object.get("name");
        if (oname == null) throw new IllegalArgumentException("name");


        JsonElement ohumans = object.get("members");
        if (ohumans == null) throw new IllegalArgumentException("members");


        JsonArray jhumans = ohumans.getAsJsonArray();
        List<Human> humans = new ArrayList<>();
        try {
            for (int i = 0; i < jhumans.size(); i++) {
                JsonObject jhuman = jhumans.get(i).getAsJsonObject();
                String hname = jhuman.get("name").getAsString();

                Human.Profession profession;
                try {
                    profession = Human.Profession.valueOf(jhuman.get("prof").getAsString());
                } catch (IllegalArgumentException | NullPointerException e) {
                    profession = null;
                }
                humans.add(new Human(hname, profession));
            }
        } catch (NullPointerException e) {

            throw new IllegalArgumentException("human");
        }

        Squad squad = new Squad(oname.getAsString(), humans);

        if (object.get("location") != null) {
            Location location;
            switch (object.get("location").getAsString().toUpperCase()) {
                case "MOON":
                    location = Moon.instance();
                    break;
                case "EARTH":
                    location = Earth.instance();
                    break;
                case "MARS":
                    location = Mars.instance();
                    break;
                default:
                    location = null;
                    break;
            }
            if (location != null) {
                squad.moveTo(location);
            }
        }

        return squad;

    }

}
