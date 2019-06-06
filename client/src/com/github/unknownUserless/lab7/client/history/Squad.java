package com.github.unknownUserless.lab7.client.history;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.IntStream;

public class Squad implements Comparable<Squad> {

    private Location location;
    private String research;
    private List<Human> members;
    private String name;
    private OffsetDateTime birthday;

    public Location getLocation() {
        return location;
    }

    public void moveTo(Location location) {
        this.location = location;
    }

    public Squad(String name, List<Human> humans) {
        this.members = humans;
        this.name = name;
        this.birthday = OffsetDateTime.now(ZoneId.of("Europe/Moscow"));
    }

    public String getName() {
        return name;
    }

    public OffsetDateTime getBirthday() {
        return birthday;
    }

    public List<Human> getMembers() {
        return members;
    }

    public void report() {
        System.out.println("Отчет: " + research);
    }

    public int size() {
        return members.size();
    }

    @Override
    public int compareTo(Squad other) {
        return this.size() - other.size();
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(members.toArray()) + name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass()) return false;
        Squad other = (Squad) o;
        if (this.getMembers().size() != other.getMembers().size()) return false;
        return (IntStream.range(0, getMembers().size()).
                allMatch((i) -> this.getMembers().get(i).equals(other.getMembers().get(i))));
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(", ");
        members.forEach((h) -> joiner.add(h.getName()));
        return name + ": " + joiner.toString();
    }
}

