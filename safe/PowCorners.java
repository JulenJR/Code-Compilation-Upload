package com.mygdx.safe.safe;

/**
 * Created by Bori.InspiratGames on 1/08/17.
 */

class PowCorners {

    //TAG
    private static final String TAG = PowCorners.class.getSimpleName();

    //ID
    private String id;

    //EXTREME
    private String Extreme1;
    private String Extreme2;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public PowCorners(){}

    /*_______________________________________________________________________________________________________________*/

    //GETTERS
    public String getId() {
        return id;
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

    public void setExtreme1(String extreme1) {
        Extreme1 = extreme1;
    }

    public void setExtreme2(String extreme2) {
        Extreme2 = extreme2;
    }
}
