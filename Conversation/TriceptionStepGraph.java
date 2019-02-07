package com.mygdx.safe.Conversation;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Boris,Escajadillo on 25/07/17.
 */

public class TriceptionStepGraph {

    //TAG
    private static final String TAG = TriceptionStepGraph.class.getSimpleName();

    //ASPECTS
    private com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g;

    private Octalysis o;
    private Vector3 triceptioncube;

    /*_______________________________________________________________________________________________________________*/

    public TriceptionStepGraph(){
        o=new Octalysis();
    }

    /*_______________________________________________________________________________________________________________*/

    public class Octalysis{
        Array<String> names;
        Array<Integer> values;

        public Octalysis(){
            for(int i=0;i<8;i++){
                names.add("iddle");
                values.add(0);
            }

            triceptioncube=new Vector3();
        }
    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS

    public Octalysis getO() {
        return o;
    }

    public Vector3 getTriceptioncube() {
        return triceptioncube;
    }

    //SETTERS


    public void setO(Octalysis o) {
        this.o = o;
    }

    public void setTriceptioncube(Vector3 triceptioncube) {
        this.triceptioncube = triceptioncube;
    }
}


