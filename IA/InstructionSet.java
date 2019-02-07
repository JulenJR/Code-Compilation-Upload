package com.mygdx.safe.IA;

/**
 * Created by sensenom on 26/07/17.
 */

public class InstructionSet {

    //TAG
    private static final String TAG = InstructionSet.class.getSimpleName();

    //ASPECTS
    private com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g;

    //PATH
    static String path="/ia/instructionset.json";

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public InstructionSet(com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g){
        this.g = g;
    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS
    public static String getPath() {
        return path;
    }

    //SETTERS
    public static void setPath(String path) {
        InstructionSet.path = path;
    }

}
