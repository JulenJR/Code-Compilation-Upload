package com.mygdx.safe.IA;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Boris.InspiratGames on 26/07/17.
 */

public class IAPrograms {

    //TAG
    private static final String TAG = IAPrograms.class.getSimpleName();

    //PROGRAMS
    private Array<IAProgram> arrayprogs;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public IAPrograms(){arrayprogs=new Array<IAProgram>();}

    /*_______________________________________________________________________________________________________________*/

    //GETTERS
    public Array<IAProgram> getArrayprogs() {
        return arrayprogs;
    }

    //SETTERS
    public void setArrayprogs(Array<IAProgram> arrayprogs) {
        this.arrayprogs = arrayprogs;
    }
}
