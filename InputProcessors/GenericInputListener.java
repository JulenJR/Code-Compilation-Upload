package com.mygdx.safe.InputProcessors;

import com.badlogic.gdx.scenes.scene2d.InputListener;

/**
 * Created by alumne_practiques on 18/07/17.
 */

/**********************NEW NEW NEW*******************/

public class GenericInputListener extends InputListener{

    //TAG
    private static final String TAG = GenericInputListener.class.getSimpleName();

    //ASPECTS
    private GenericMethodsInputProcessor g;

    //ENABLED
    private boolean _enabled = true;

    //LISTENER
    private String listenerOwner;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public GenericInputListener(String listenerOwner, GenericMethodsInputProcessor g){
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

    //SETTERS
    public void set_enabled(boolean enabled){
        this._enabled = enabled;
    }

    public void setListenerOwner(String listenerOwner){
        this.listenerOwner = listenerOwner;
    }


}