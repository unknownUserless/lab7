package com.github.unknownUserless.wrappers;

public class Pair<E, S> {

    private E e;
    private S s;

    public Pair(E e, S s){
        this.e = e;
        this.s = s;
    }

    public E element(){
        return e;
    }

    public S attachment(){
        return s;
    }

}
