package com.github.unknownUserless.lab7.client.history;

public class Mars implements Location {
    private static Mars mars;

    public static Mars instance(){
        if (mars == null){
            mars = new Mars();
        }
        return mars;
    }

    private Mars(){}

    @Override
    public String toString(){
        return "Mars";
    }
}
