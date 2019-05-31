package com.github.unknownUserless.lab7.client.history;

import java.util.ArrayList;
import java.util.List;

public class Moon implements Location{
    private static Moon moon;

    public static Moon instance(){
        if (moon == null){
            moon = new Moon();
        }
        return moon;
    }

    private Moon(){
        this.base = new Base();
    }

    public final Base base;

    public class Base{
        public final List<Human> humans = new ArrayList<>();
    }

    @Override
    public String toString(){
        return "Moon";
    }
}
