package com.github.unknownUserless.lab7.client.connection;

import com.github.unknownUserless.lab7.client.CommandManager;
import com.github.unknownUserless.lab7.client.connection.packets.request.CommandPack;
import com.github.unknownUserless.lab7.client.connection.packets.request.ConnectPack;
import com.github.unknownUserless.lab7.client.connection.packets.Packet;
import com.github.unknownUserless.lab7.client.connection.packets.request.RegistrationPack;
import com.github.unknownUserless.lab7.client.connection.packets.respond.AuthorizationRespondPack;
import com.github.unknownUserless.lab7.client.connection.packets.respond.CommandRespondPack;
import com.github.unknownUserless.lab7.client.connection.packets.respond.ConnectRespondPack;
import com.github.unknownUserless.lab7.client.history.*;

import java.io.File;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private static PeopleMap allPeople = new PeopleMap();
    private static Spaceship spaceship;
    private static List<Rocket> rockets = new ArrayList<>();
    private static Connector connector;
    public static Console console;
    private static SocketAddress serverAddress;
    private static CommandManager manager;

    public static void main(String[] args) throws UnknownHostException, SocketTimeoutException {
        try {
            addAll();
            System.out.println(spaceship.toString());
            spaceship.landing();
            // Подключение к серверу
            connector = new Connector();
            console = new Console();

            File file = new File(System.getenv("file"));

            manager = new CommandManager(console, file);

            connection();

            boolean cont = false;

            System.out.println("Для получения справки по командам воспользуйтесь командой help");
            do {
                CommandPack packToSend = manager.getCommandPack();
                if (packToSend != null) {
                    try {
                        if (packToSend.command.equals("cont")) {
                            cont = true;
                            continue;
                        }

                        if (packToSend.command.equals("connect")) Main.connect();
                        connector.sender.send(packToSend);

                        CommandRespondPack receivedPack = (CommandRespondPack) connector.receiver.receiveObj(10000);
                        System.out.println(receivedPack.getRespond());

                        if (packToSend.command.equals("exit")) {
                            Main.connect();
                        }
                    } catch (SocketTimeoutException e) {
                        System.out.println("Ответ не получен, повторите попытку");
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Попробуйте ввести команду по-другому");
                }

            } while (!cont);

            System.out.println("THE END");

        } catch (Throwable e){
            e.printStackTrace();
        }
    }

    private static void addAll() {
        allPeople.add(new Human("Ponchick", Human.Profession.PROFESSOR));
        allPeople.add(new Human("Pilulkin", Human.Profession.ARCHITECT));
        allPeople.add(new Human("Vintik"));
        allPeople.add(new Human("Neznaika"));
        allPeople.add(new Human("Seledochka", Human.Profession.DOCTOR));
        allPeople.add(new Human("Shpuntik"));
        allPeople.add(new Human("Fuksia", Human.Profession.MUSICIAN));
        spaceship = Earth.instance().factory.getSpaceship(allPeople.get("Vintik"),
                new ArrayList<>(allPeople.getSome("Vintik", "Shpuntik", "Seledochka").values()));
        rockets.add(Earth.instance().factory.getRocket("НИП", "в честь Незнайки и Пончика"));
        rockets.add(Earth.instance().factory.getRocket("ФИС", "в честь Фуксии и Селедочки"));
    }

    public static void connection() {
        try {
            connect(); // Подключение (обмен адресами сокетов)
            logreg();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(-2);
        } catch (SocketTimeoutException e) {
            System.out.println("Проблемы с соединением, попробуйте переподключиться");
            connection();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void connect() throws UnknownHostException, SocketTimeoutException, ClassNotFoundException {
        if (serverAddress == null) {
            serverAddress = new InetSocketAddress(InetAddress.getLocalHost(), console.readMainPort());
        }
        connector.sender.sendTo(serverAddress, new ConnectPack(connector.getLocalAddress()));
        ConnectRespondPack pack = (ConnectRespondPack) connector.receiver.receiveObj(2000);
        connector.setRemoteAddress(pack.address);
        System.out.println(pack.respond);
    }

    private static void logreg() throws ClassNotFoundException {

        Packet pack = SomeAnalyzer.connect();
        boolean registration = pack instanceof RegistrationPack;

        connector.sender.send(pack); //Отправили пакет регистрации/логина
        try {
            AuthorizationRespondPack respond =
                    (AuthorizationRespondPack) connector.receiver.receiveObj(8000); //Приняли ответ с сервера

            if (registration) {

                if (respond.success) {
                    System.out.println("Успешная регистрация, пароль отправлен на почту, указанную при регистрации");
                } else {
                    System.out.println("Логин или почта уже используется, попробуйте вспомнить логин и пароль\n" +
                            "или используйте другие данные для регистрации");
                    logreg();
                }

            } else {

                if (respond.success) {
                    System.out.println("Вы успешно залогинились");
                } else {
                    System.out.println("Введен неверный логин/пароль, попробуйте еще раз");
                    logreg();
                }

            }

        } catch (SocketTimeoutException e){
            System.out.println("Проблемы с регистрацией/входом");
        }



    }
}
