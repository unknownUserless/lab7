package com.github.unknownUserless.lab7.client.connection;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;

public class Connector implements Closeable {

    private DatagramSocket socket;

    public final Sender sender = new Sender();
    public final Receiver receiver = new Receiver();

    public SocketAddress getLocalAddress() {
        return socket.getLocalSocketAddress();
    }

    public Connector()  throws SocketException {
        this.socket = new DatagramSocket();
        this.socket.setSoTimeout(4000);
    }

    public void setRemoteAddress(SocketAddress address) {
        this.sender.address = address;
    }

    public class Sender {

        private SocketAddress address;

        public void send(Serializable o) {
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ObjectOutputStream outObject = new ObjectOutputStream(out);
                outObject.writeObject(o);
                outObject.flush();
                byte[] data = out.toByteArray();
                send(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendTo(SocketAddress serverAddress, Serializable object) {
            SocketAddress old = address;
            address = serverAddress;
            send(object);
            address = old;
        }

        private void send(byte[] data) {
            try {
                DatagramPacket pack = new DatagramPacket(data, data.length, address);
                socket.send(pack);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class Receiver {

        public Object receiveObj(int size) throws SocketTimeoutException, ClassNotFoundException {
            DatagramPacket packet = receivePack(size);
            if (packet != null) {
                try (ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData());
                     ObjectInputStream ois = new ObjectInputStream(bais)) {
                    return ois.readObject();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                return null;
            }
        }

        public String receiveString(int length) throws SocketTimeoutException{
            DatagramPacket packet = receivePack(length*16);
            if (packet != null) {
                return new String(packet.getData(), 0, packet.getLength());
            } else {
                return null;
            }
        }

        private DatagramPacket receivePack(int capacity) throws SocketTimeoutException {
            try {
                capacity = 10000;
                DatagramPacket packet = new DatagramPacket(new byte[capacity], capacity);
                socket.receive(packet);
                return packet;
            }  catch (IOException e){
                if (e instanceof SocketTimeoutException) throw (SocketTimeoutException)e;
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public void close() {
        this.socket.close();
    }
}
