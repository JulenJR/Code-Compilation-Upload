package com.mygdx.safe.InputProcessors;

import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

/**
 * Created by alumne_practiques on 25/07/17.
 */

public class GenericGestureListener extends ActorGestureListener{

    //TAG
    private static final String TAG = GenericGestureListener.class.getSimpleName();

    //ASPECTS
    private GenericMethodsInputProcessor g;

    //BOOLEANS
    private boolean _enabled = true;
    private boolean _draggItemStarted = false;
    private boolean _longPressed = false;

    //LISTENER
    private String listenerOwner;

    //OTHER
    private int _parentIndex;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public GenericGestureListener(String listenerOwner, GenericMethodsInputProcessor g){
        this.g = g;
        this.listenerOwner = listenerOwner;
    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS

    public boolean get_enabled(){
        return _enabled;
    }

    public String getListenerOwner(){
        return listenerOwner;
    }

    public boolean is_draggItemStarted() {
        return _draggItemStarted;
    }

    public boolean is_longPressed() {
        return _longPressed;
    }

    public int get_parentIndex() {
        return _parentIndex;
    }

    //SETTERS

    public void set_enabled(boolean enabled){
        this._enabled = enabled;
    }

    public void setListenerOwner(String listenerOwner){
        this.listenerOwner = listenerOwner;
    }

    public void set_draggItemStarted(boolean _draggItemStarted) {
        this._draggItemStarted = _draggItemStarted;
    }

    public void set_longPressed(boolean _longPressed) {
        this._longPressed = _longPressed;
    }

    public void set_parentIndex(int _parentIndex) {
        this._parentIndex = _parentIndex;
    }

}
