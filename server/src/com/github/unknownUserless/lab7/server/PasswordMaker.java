package com.github.unknownUserless.lab7.server;

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class PasswordMaker {
    private PasswordMaker() {
    }

    public static String getHexDigest(String string) {
        try {
            MessageDigest msg = MessageDigest.getInstance("MD2");
            byte[] bytes = msg.digest(string.getBytes());
            StringBuilder builder = new StringBuilder();
            for (byte b: bytes){
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

   public static String getRandomString() {
        Random random = new Random();
        byte[] array = new byte[256];
        random.nextBytes(array);
        String randomString = new String(array);
        String alphaString = randomString.replaceAll("[^A-Za-z]", "");
        int stringSize = 5 + random.nextInt(5);
        if (alphaString.length() < stringSize){
            alphaString = getRandomString();
        }

        return alphaString.substring(0, stringSize);
    }

}
