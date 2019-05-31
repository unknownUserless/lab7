package com.github.unknownUserless.lab7.server.collection;

import com.github.unknownUserless.lab7.client.history.Squad;
import com.github.unknownUserless.lab7.server.sql.JDBCWorker;
import com.github.unknownUserless.wrappers.Pair;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;

public class Collection {
    public static CopyOnWriteArrayList<Pair<Squad, String>> collection ;

    public static void loadSqads(){
        collection = new CopyOnWriteArrayList<>(JDBCWorker.instance().squads().list());
    }

    public static boolean contains(Predicate<Squad> p){
        return collection.stream().map(Pair::element).filter(p).anyMatch(p);
    }
}
