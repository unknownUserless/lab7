package com.github.unknownUserless.lab7.client.history;

import java.io.Serializable;
import java.util.HashMap;

public class PeopleMap extends HashMap<String, Human> implements Serializable {
    public void add(Human h){
        super.put(h.getName(), h);
    }

    public PeopleMap getSome(String... names){
        PeopleMap values = new PeopleMap();
        for(int i = 0; i<names.length; i++){
            values.add(this.get(names[i]));
        }
        return values;
    }

    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append("Всего людей ").append(this.size()).append("\n");
        for (Human h : this.values()){
            str.append(h.getName()).append("\n");
        }
        return str.toString();
    }
}
