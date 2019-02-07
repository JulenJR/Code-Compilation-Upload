package com.mygdx.safe;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

/**
 * Created by practicas on 19/12/17.
 */

public class ParticlePositions {

    private HashMap<String, Data> particlePositions;
    private Data d;

    public ParticlePositions() {
        particlePositions = new HashMap<String, Data>();
        this.d = new Data();
    }

    public Data getD() {
        return d;
    }

    public HashMap<String, Data> getPositions() {
        return particlePositions;
    }


    static public class Data {

        private String ArrayID;
        String path;
        float scale;
        private Array<Vector2> position;

        public String getArrayID() {
            return ArrayID;
        }

        public String getPath() {
            return path;
        }

        public float getScale() {
            return scale;
        }

        public Array<Vector2> getPosition() {
            return position;
        }
    }
}
