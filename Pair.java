package com.mygdx.safe;

/**
 * Created by Boris.Inspirat on 17/12/17.
 */

public class Pair<FIRST, SECOND> {

    private FIRST first;
    private SECOND second;

    public Pair (){

    }

    public Pair (FIRST first, SECOND second){
        this.first= first;
        this.second = second;
    }

    public FIRST getFirst() {
        return first;
    }

    public void setFirst(FIRST t1) {
        this.first = t1;
    }

    public SECOND getSecond() {
        return second;
    }

    public void setSecond(SECOND v1) {
        this.second = v1;
    }

    public String toString(){
        return "[" +first.toString() + " : " + second.toString() + "]";
    }
}
