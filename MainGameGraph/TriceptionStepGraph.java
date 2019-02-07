package com.mygdx.safe.MainGameGraph;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Boris,Escajadillo on 25/07/17.
 */

public class TriceptionStepGraph {

    public Octalysis o;
    public Vector3 triceptioncube;


    public TriceptionStepGraph(){
        o=new Octalysis();


    }

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
}


