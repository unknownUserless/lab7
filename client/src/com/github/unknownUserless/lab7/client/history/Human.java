package com.github.unknownUserless.lab7.client.history;


public class Human {
    public enum Profession {
        MECHANIC,
        PROFESSOR,
        ASTRONOMER,
        ENGINEER,
        ARCHITECT,
        ARTIST,
        MUSICIAN,
        DOCTOR
    }

    private String name;
    private Profession prof;

    public Profession getProf() {
        return prof;
    }

    public String getName() {
        return name;
    }

    public Human(String name) {
        this.name = name;
    }

    public Human(String name, Profession prof) {
        this.name = name;
        this.prof = prof;
    }

    @Override
    public int hashCode() {
        if (prof != null) {
            return name.hashCode() + prof.hashCode();
        } else return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass() || o.hashCode() != this.hashCode()) return false;
        Human h = (Human) o;
        return h.getName().equals(this.getName()) && h.getProf() == this.getProf();
    }

    public void say(String str) {
        System.out.println(name + " say: " + str);
    }

    public void respond() {
        if (prof != null) {
            System.out.println(prof + " " + name + " здесь");
        } else {
            System.out.println(name + " здесь");
        }
    }

    @Override
    public String toString(){
        return this.name + ":" + (prof == null ? "" : prof.name());
    }
}

