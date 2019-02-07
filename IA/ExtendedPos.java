package com.mygdx.safe.IA;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Boris.InspiratGames on 1/03/18.
 */

public class ExtendedPos {

    public static final String TAG=ExtendedPos.class.getSimpleName();
    private Array<Integer> arraypos;

    public ExtendedPos(){
        clearAll();
    }

    public void clearAll(){
        if(arraypos==null)
            arraypos=new Array<Integer>();

        else
            arraypos.clear();
    }

    public void setPos(int n){
        if(n>0) {
            if (arraypos.contains(n, false)) {
                System.out.println(TAG + " " + n + " IS ALREADY AT EXTENDEDPOS");
            } else {
                arraypos.add(n);
                arraypos.sort();
                System.out.println(TAG + " "+ this);
            }
        }else{
            System.out.println(TAG + " O IS NOT VALID VALUE");
        }

    }

    public void unsetPos(int n){
        if (arraypos.contains(n,false)){
            arraypos.removeValue(n,false);
            arraypos.sort();
            System.out.println(TAG +" "+  n +" REMOVED");
            System.out.println(TAG + this);
        }else{
            System.out.println(TAG + " " + n + " IS NOT AT EXTENDEDPOS");
        }
    }

    public Array<Integer> getPositionArray(){
        return arraypos;
    }


    public int getSize(){
        return arraypos.size;
    }

    public String toString(){
        return arraypos.toString();
    }

}
