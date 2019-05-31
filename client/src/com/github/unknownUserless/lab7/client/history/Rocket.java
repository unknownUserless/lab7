package com.github.unknownUserless.lab7.client.history;

public class Rocket implements Location, Researchable {
    private String name;
    private String nameFrom;

    public Rocket(String name, String nameFrom) {
        this.name = name;
        this.nameFrom = nameFrom;
    }
/*

    public String getName() {
        return name;
    }

    public String getNameFrom() {
        return nameFrom;
    }
*/

    @Override
    public String research(){
        return "Рокета " + name + ", " + nameFrom;
    }


    @Override
    public int hashCode() {
        return name.hashCode() + nameFrom.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass()) return false;
        if (o.hashCode() != this.hashCode()) return false;
        Rocket rocket = (Rocket) o;
        return this.name.equals(rocket.name) && this.nameFrom.equals(rocket.nameFrom);
    }
}
