package com.github.unknownUserless.lab7.client.history;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class Earth implements Location{

    private static Earth earth;

    public static Earth instance(){
        if (earth == null){
            earth = new Earth();
        }
        return earth;
    }

    public final Factory factory;
    public final MaternityHospital maternityHospital;

    public class Factory{
        public Spaceship getSpaceship(Human captain, List<Human> crew){
            return new Spaceship(captain, crew);
        }

        public Rocket getRocket(String name, String nameFrom){
            return new Rocket(name, nameFrom);
        }
    }

    public class MaternityHospital {
        public Human toBeBorn(String name){
            return new Human(name);
        }

        public Human toBeBorn(String name, Human.Profession prof){
            return new Human(name, prof);
        }
    }

    private Earth(){
        this.maternityHospital = new MaternityHospital();
        this.factory = new Factory();
    }

    @Override
    public String toString(){
        return "Earth";
    }
}