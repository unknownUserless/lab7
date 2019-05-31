package com.github.unknownUserless.lab7.client.connection;

import com.github.unknownUserless.lab7.client.connection.packets.request.LoginPack;
import com.github.unknownUserless.lab7.client.connection.packets.Packet;
import com.github.unknownUserless.lab7.client.connection.packets.request.RegistrationPack;

public class SomeAnalyzer {
    private SomeAnalyzer(){}

    public static Packet connect(){
        System.out.println("Войдите под уже существующим аккаунтом с помощью команды login\n" +
                "или зарегестрируйтесь с помощью команды registration");
        String command = Main.console.readString();
        Packet pack;
        switch (command){
            case "registration":
                pack = registration();
                break;
            case "login":
                pack = login();
                break;
            case "exit":
                System.exit(0);
                return null;
            default:
                System.out.println("Команда " + command + " не найдена");
                pack = null;
        }
        if (pack == null){
            return connect();
        } else {
            return pack;
        }
    }

    private static LoginPack login(){
        System.out.println("Введите свой логин");
        String login = Main.console.readString();
        System.out.println("Введите пароль");
        String pass = Main.console.readString();
        return new LoginPack(login, pass.toCharArray());
    }

    private static RegistrationPack registration(){
        System.out.println("Введите желаемый login");
        String login = Main.console.readString();
        System.out.println("Введите свою электронную почту");
        String email = "";
        do {
            System.out.println("Адрес должен оканчиваться на @mail.ru / @bk.ru / @inbox.ru");
            email = Main.console.readString();
            if (email.equals("exit")){
                return null;
            }
        } while (!isMail(email));

        return new RegistrationPack(login, email);
    }

    private static boolean isMail(String str){
        if (str.contains("@mail.ru") || str.contains("@bk.ru") ||
                str.contains("@inbox.ru")){

            return true;
        } else {
            System.out.println("Адрес не распознан");
            return false;
        }
    }
}
