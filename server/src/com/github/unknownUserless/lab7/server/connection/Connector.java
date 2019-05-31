package com.github.unknownUserless.lab7.server.connection;

import com.github.unknownUserless.lab7.server.sql.User;

import java.io.*;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class Connector {

    public enum Status{
        COMMANDS,
        LOGREG
    }

    private Status status = Status.LOGREG;
    private User user;
    public final DatagramChannel channel;
    public final Receiver receiver;
    public final Sender sender;
    private SocketAddress sendAddress;

    public Connector() throws IOException{
        this.channel = DatagramChannel.open();
        this.channel.configureBlocking(false);
        this.receiver = new Receiver();
        this.sender = new Sender();
    }

    public void bind(SocketAddress address) throws IOException{
        this.channel.bind(address);
    }

    public SocketAddress getLocalAddress() throws IOException{
        return this.channel.getLocalAddress();
    }

    public SocketAddress getRemoteAddress() {
        return sendAddress;
    }

    public void setRemoteAddress(SocketAddress sendAddress) {
        this.sendAddress = sendAddress;
    }

    public class Receiver{
        public Object receiveObj() throws IOException, ClassNotFoundException{
            ByteBuffer buffer = ByteBuffer.allocate(2000);
            buffer.clear();
            channel.receive(buffer);
            buffer.flip();
            byte[] data = getBytes(buffer);
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object o = ois.readObject();
            bais.close();
            ois.close();
            return o;
        }

        private byte[] getBytes(ByteBuffer buffer) {
            byte[] result = new byte[buffer.limit()];
            for (int i = 0; i < buffer.limit(); i++) {
                result[i] = buffer.get();
            }
            buffer.clear();
            return result;
        }
    }

    public class Sender{
        public void send(Serializable o) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(o);
            byte[] data = baos.toByteArray();
            ByteBuffer buffer = ByteBuffer.allocate(10000);
            buffer.clear();
            buffer.put(data);
            buffer.flip();
            channel.send(buffer, sendAddress);
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}