package com.mygdx.safe.safe;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Boris.InspiratGames on 1/08/17.
 */


public class PowBarsConfig{

    //TAG
    private static final String TAG = PowBarsConfig.class.getSimpleName();

    //POW CORNERS
    private Array<PowCorners> pc;

    //POWBAR SAVE VALUES
    private Array<PowBarSaveValues> pbsv;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public PowBarsConfig(){}

    /*_______________________________________________________________________________________________________________*/

    //GETTERS
    public Array<PowCorners> getPc() {
        return pc;
    }

    public Array<PowBarSaveValues> getPbsv() {
        return pbsv;
    }

    //SETTERS
    public void setPc(Array<PowCorners> pc) {
        this.pc = pc;
    }

    public void setPbsv(Array<PowBarSaveValues> pbsv) {
        this.pbsv = pbsv;
    }
}
