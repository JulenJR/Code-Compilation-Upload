package com.mygdx.safe.safe;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by sensenom on 1/08/17.
 */

public class PowBar {

    //TAG
    private static final String TAG = PowBar.class.getSimpleName();

    //ID
    private String id;

    //POSITION
    private Vector2 position;

    //VALUE
    private float value;

    //EXTREME
    private String Extreme1;
    private String Extreme2;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public PowBar(){
        position=new Vector2();
    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS
    public String getId() {
        return id;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getValue() {
        return value;
    }

    public String getExtreme1() {
        return Extreme1;
    }

    public String getExtreme2() {
        return Extreme2;
    }

    //SETTERS
    public void setId(String id) {
        this.id = id;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void setExtreme1(String extreme1) {
        Extreme1 = extreme1;
    }

    public void setExtreme2(String extreme2) {
        Extreme2 = extreme2;
    }
}
