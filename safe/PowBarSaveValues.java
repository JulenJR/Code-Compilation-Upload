package com.mygdx.safe.safe;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Boris.InspiratGames on 1/08/17.
 */

public class PowBarSaveValues {

    //TAG
    private static final String TAG = PowBarSaveValues.class.getSimpleName();

    //ID
    private String id;

    //VALUE
    private float value;

    //POSITION
    private Vector2 position;

    //BOOLEAN
    private boolean visible;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public PowBarSaveValues(){}

    /*_______________________________________________________________________________________________________________*/

    //GETTERS
    public String getId() {
        return id;
    }

    public float getValue() {
        return value;
    }

    public Vector2 getPosition() {
        return position;
    }

    public boolean isVisible() {
        return visible;
    }

    //SETTERS
    public void setId(String id) {
        this.id = id;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
