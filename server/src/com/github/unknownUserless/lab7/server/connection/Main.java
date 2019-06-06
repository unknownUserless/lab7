package com.github.unknownUserless.lab7.server.connection;

import com.github.unknownUserless.lab7.client.connection.packets.request.CommandPack;
import com.github.unknownUserless.lab7.client.connection.packets.request.ConnectPack;
import com.github.unknownUserless.lab7.client.connection.packets.request.LoginPack;
import com.github.unknownUserless.lab7.client.connection.packets.request.RegistrationPack;
import com.github.unknownUserless.lab7.client.connection.packets.respond.AuthorizationRespondPack;
import com.github.unknownUserless.lab7.client.connection.packets.respond.ConnectRespondPack;
import com.github.unknownUserless.lab7.server.PasswordMaker;
import com.github.unknownUserless.lab7.server.collection.Collection;
import com.github.unknownUserless.lab7.server.sql.JDBCWorker;
import com.github.unknownUserless.lab7.server.sql.User;
import com.github.unknownUserless.lab7.server.sql.UserCard;
import com.github.unknownUserless.mail.MailWorker;
import com.github.unknownUserless.wrappers.Pair;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.*;

public class Main {

    private static Selector selector;
    private static Connector mainConnector;
    private static int mainPort;
    private static JDBCWorker database;
    private static MailWorker email;
    private static Map<String, SelectionKey> currentConnections;

    public static Map<String, SelectionKey> getCurrentConnections(){
        return currentConnections;
    }



    private static final String regFormat = "Вы успешно зарегестрировались\n" +
            "Ваш логин: %s\n" +
            "Ваш пароль: %s\n" +
            "Удачи!";

    public static void main(String[] args) throws IOException {
        Collection.loadSqads();
        currentConnections = new HashMap<>();
        email = new MailWorker();
        database = JDBCWorker.instance();
        selector = Selector.open();
        mainPort = Integer.parseInt(args[0]);
        mainConnector = new Connector();
        mainConnector.bind(new InetSocketAddress(InetAddress.getLocalHost(), mainPort));
        mainConnector.channel.register(selector, SelectionKey.OP_READ);

        while (true) {
            if (selector.select() == 0) continue;
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iter = keys.iterator();

            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();
                try {
                    if (key.attachment() == null){
                        handleConnection();
                    } else  {
                        switch (( (Connector)key.attachment()).getStatus()){
                            case COMMANDS:
                                handleCommand(key);
                                break;
                            case LOGREG:
                                handleLogReg(key);
                                break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private static void handleConnection() throws IOException, ClassNotFoundException{
        ConnectPack pack = (ConnectPack) mainConnector.receiver.receiveObj();
        Connector connector = new Connector();
        connector.bind(null);
        connector.channel.configureBlocking(false);
        connector.setRemoteAddress(pack.address);
        connector.setStatus(Connector.Status.LOGREG);
        connector.sender.send(new ConnectRespondPack(connector.getLocalAddress(), "Вы успешно подключились"));
        connector.channel.register(selector, SelectionKey.OP_READ, connector);
    }

    private static boolean handleLogin(SelectionKey key, LoginPack pack) {
        Connector connector = (Connector)key.attachment();

        User user = database.users().get(pack.login);
        if (user == null){
            return false;
        }

        String gotPassword = PasswordMaker.getHexDigest(new String(pack.password()));

        if (gotPassword.equals(user.password())){
            connector.setUser(user);
            return true;
        } else {
            return false;
        }
    }

    private static void handleLogReg(SelectionKey key) throws IOException, ClassNotFoundException{
        boolean success;
        Object o = ((Connector)key.attachment()).receiver.receiveObj();
        if (o instanceof LoginPack){
            success = handleLogin(key, (LoginPack) o);
        } else {
            success = handleRegistration(key, (RegistrationPack)o);
        }
        AuthorizationRespondPack respondPack = new AuthorizationRespondPack(success);
        ((Connector)key.attachment()).sender.send(respondPack);
        if (success){

            SelectionKey oldKey = currentConnections.get(((Connector) key.attachment()).getUser().login());
            if (oldKey != null){
                oldKey.channel().close();
                oldKey.cancel();
            }

            ((Connector)key.attachment()).setStatus(Connector.Status.COMMANDS);
            currentConnections.put(((Connector) key.attachment()).getUser().login(), key);
        }
    }

    private static void handleCommand(SelectionKey key) throws IOException, ClassNotFoundException{
        Connector connector = (Connector)key.attachment();
        CommandPack receivedPack = (CommandPack)connector.receiver.receiveObj();

        Pair<CommandPack, User> pair = new Pair<>(receivedPack, connector.getUser());
        new Thread(new Respond(pair, connector.sender)).start();
    }

    private static boolean handleRegistration(SelectionKey key, RegistrationPack pack) {
        Connector connector = (Connector)key.attachment();
        if (database.users().containing(new UserCard(pack.email, pack.login))){
            return false;
        }
        String pass = PasswordMaker.getRandomString();

        String encodePass = PasswordMaker.getHexDigest(pass);

        if (email.send("Регистрация",
                String.format(regFormat, pack.login, pass),
                pack.email)) {

            database.users().insert(new User(pack.email, pack.login, encodePass));

            connector.setUser(database.users().get(pack.login));
            return true;
        } else {
            return false;
        }
    }

}