package com.mygdx.safe.safe;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Boris.InspiratGames on 18/07/17.
 */

public class PowBars {

    //TAG
    private static final String TAG = PowBar.class.getSimpleName();

    //POWBAR
    private Array<PowBar> pb;
    private PowBarsConfig pbcfg;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public PowBars(){
        pb= new Array<PowBar>();
    }

    //ADD POWBAR
    public void addPowBar(PowBar p){   pb.add(p); }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS
    public Array<PowBar> getArrayPowBars() {  return pb; }

    public PowBarsConfig getPowBarsConfig(){  return pbcfg; }

    //SETTERS
    public void setPb(Array<PowBar> pb) {
        this.pb = pb;
    }

    public void setPbcfg(PowBarsConfig pbcfg) {
        this.pbcfg = pbcfg;
    }
}
