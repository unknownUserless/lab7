package com.github.unknownUserless.lab7.client;

import com.github.unknownUserless.lab7.client.connection.Console;
import com.github.unknownUserless.lab7.client.connection.FileWorker;
import com.github.unknownUserless.lab7.client.connection.packets.Packet;
import com.github.unknownUserless.lab7.client.connection.packets.request.CommandPack;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;

public class CommandManager {

    private Console console;
    private FileWorker worker;

    public CommandManager(Console console, File file){
        this.console = console;
        this.worker = new FileWorker(file);
    }

    public CommandPack getCommandPack() throws IllegalArgumentException{
        try {
            String string = console.readString();
            String[] arr = string.split(" ", 2);
            if (arr.length == 0) throw new IllegalArgumentException("empty");
            String command = arr[0];
            String arguments = arr.length > 1 ? arr[1] : null;
            String attachment = null;
            switch (command){
                case "add":
                    if (!isFullJsonCorrect(arguments)) {
                        System.out.println("Json должен содержать как минимум имя и членов отряда");
                        return null;
                    }
                    break;
                case "remove":
                case "get":
                    if (!isNameInJson(arguments)) {
                        System.out.println("Json должен сожержать имя отряда");
                        return null;
                    }
                    break;
                case "import":
                    attachment = worker.getFileContent();
                    break;
            }
            return new CommandPack(command, arguments, attachment);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private boolean isFullJsonCorrect(String string){
        if (string == null) return false;
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(string).getAsJsonObject();
        return object.has("name") && object.has("members");
    }

    private boolean isNameInJson(String string){
        if (string == null) return false;
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(string).getAsJsonObject();
        return  object.has("name");
    }

}
