package com.github.unknownUserless.lab7.client.connection;

import java.io.Closeable;
import java.util.Scanner;

public class Console implements Closeable {
    private Scanner scanner;

    public Console() {
        scanner = new Scanner(System.in);
    }

    public String readString() {
        if (scanner.hasNextLine()) {
            return scanner.nextLine();
        } else {
            System.err.println("Что-то пошло не так, перезапустите клиент" +
                    "Ваше соединение будет восстановлено");
            System.exit(2);
            return "";
        }
    }

    public int readMainPort(){
        System.out.println("Необходимо указать главный порт сервера");
        try{
            String str = scanner.nextLine();
            return Integer.parseInt(str);
        } catch (Exception e){
            System.out.println("Неверное значение");
            System.exit(2);
            return readMainPort();
        }
    }

    @Override
    public void close() {
        this.scanner.close();
    }
}
