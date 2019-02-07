package com.mygdx.safe.IA;

/**
 * Created by sensenom on 27/07/17.
 */

public class IAinterpreter {

    //TAG
    private static final String TAG = IAinterpreter.class.getSimpleName();

    //ASPECTS
    private com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g;

    //INSTRUCTION
    private Instruction i;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public IAinterpreter(com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g) {
        this.g=g;
    }

    /*_______________________________________________________________________________________________________________*/

    public void processInstruction(IAProgram program) {

        if(program.get_actual_instruction()<program.get_program().size){
            i=program.getInstruction(program.get_actual_instruction());
            g.print("EXECUTING: INSTRUCTION: ");
            i.printInstruction();
        }
        else {
            g.println("ENDING PROGRAM.");
            program.set_finishing();
        }
    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS

    public Instruction getI() {
        return i;
    }

    //SETTERS

    public void setI(Instruction i) {
        this.i = i;
    }
}
