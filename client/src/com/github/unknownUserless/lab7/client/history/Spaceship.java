package com.github.unknownUserless.lab7.client.history;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Spaceship implements Location{
    private List<Human> crew;
    private Captain captain;
    private Squad squad;

    public Spaceship(Human captain, List<Human> crew) {
        this.captain = new Captain(captain);
        this.crew = crew;
    }

    public class Captain extends Human {

        @Override
        public void respond() {
            System.out.println("Капитан " + getName() + " на месте");
        }

        private Captain(Human human) {
            super(human.getName(), human.getProf());
        }

        @Override
        public void say(String str) {
            System.out.println("Капитан " + getName() + " сказал: " + str);
        }

        public void toOrder(Order order, Squad whom) {
            order.order(whom);
        }

    }

    @Override
    public String toString() {
        StringBuilder strCrew = new StringBuilder("Капитан: ");
        strCrew.append(captain.getName()).append("; Экипаж: ");
        int i = 0;
        for (Human h : this.crew) {
            i = i + 1;
            if (h.getProf() != null) {
                strCrew.append(h.getProf().name()).append(" ").append(h.getName()).append(", ");
            } else {
                strCrew.append(h.getName()).append(", ");
            }
            if (i == 4) {
                strCrew.append("\n");
                i = 0;
            }
        }
        strCrew.delete(strCrew.length()-2, strCrew.length());
        return strCrew.toString();
    }

    public void setSquad(Squad squad){
        this.squad = squad;
        this.squad.moveTo(this);
    }


    public void landing() {
        System.out.println("Корабль приземлился");
    }

    public void takeoff() {
        System.out.println("Корабль взлетел");
    }

    public String explore(Researchable researchable) {
        if (squad == null){
            System.err.println("Некому приказать");
            return null;
        }
        this.captain.toOrder( (l)-> l.getMembers().forEach( (h)-> h.say("Закончил исследование")), squad);
        return researchable.research();
    }

    public Captain getCaptain(){
        return captain;
    }

    public void setCaptain(Human human){
        this.captain = new Captain(human);
    }

}
