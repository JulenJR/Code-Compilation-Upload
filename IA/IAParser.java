package com.mygdx.safe.IA;

/**
 * Created by Boris.InspiratGames on 25/07/17.
 */

public class IAParser {

    //TAG
    private static final String TAG = IAParser.class.getSimpleName();

    //ASPECTS
    private com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g;

    //INSTRUCTION SET ---- CONVERTS STRING PROGRAM TO INSTRUCTION PROGRAM USING THE INSTRUCTIONSET
    private InstructionSet i;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public IAParser(com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g){
        this.g = g;
        i = new InstructionSet(g);
    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS
    public InstructionSet getI() {
        return i;
    }

    //SETTERS
    public void setI(InstructionSet i) {
        this.i = i;
    }
}
