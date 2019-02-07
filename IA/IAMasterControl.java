package com.mygdx.safe.IA;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;

import java.util.HashMap;

/**
 * Created by Boris.InspiratGames on 26/07/17.
 */

public class IAMasterControl {

    //TAG
    private static final String TAG = IAMasterControl.class.getSimpleName();

    //ASPECTS
    private GenericMethodsInputProcessor g;

    //PATHS
    private static String IAMASTERCONTROLNAME="iamaster.json";
    private static String programspath="ia/programs/";

    //IA
    private Instruction i;
    private com.mygdx.safe.IA.IAParser iaparser;
    private com.mygdx.safe.IA.IAinterpreter interpreter;
    private com.mygdx.safe.IA.IAProgram program;
    private com.mygdx.safe.IA.IAPrograms programsArray;
    private IAAccessClass iaAccess;
    private HashMap <String, com.mygdx.safe.IA.IAProgram> iaprogs;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public IAMasterControl(GenericMethodsInputProcessor g){
        this.g=g;
        iaparser=new com.mygdx.safe.IA.IAParser(g);
        program=null;
        programsArray=loadPrograms();
        iaprogs=new HashMap<String, com.mygdx.safe.IA.IAProgram>();
        interpreter =new com.mygdx.safe.IA.IAinterpreter(g);

        for(com.mygdx.safe.IA.IAProgram p:programsArray.getArrayprogs()){
            p.set_loading();
            iaprogs.put(p.getProgram_name(),p);
        }
    }

    /*_______________________________________________________________________________________________________________*/

    //PARSE PROGRAM
    public void parseProgram(){}

    public void parseProgram(String name){
        if(program==null)
            setProgram(name);
        parseProgram();
    }

    //RUN PROGRAM
    public void runProgram(){
        if(program!=null) {
            g.println("RUN " + program.getProgram_name() + "PROGRAM:");
            if (!program.isParsing()) {
                g.println("DON'T EXECUTE NO PARSING PROGRAM. PLEASE PARSING FIRST");
            } else // EXECUTES INSTRUCTIONS USING IAINTERPRETER
            {
                program.set_running();
                g.println("INIT PROGRAM: " + program.getProgram_name());

                while (program.is_run()) {
                    program.set_next_instruction(program.get_actual_instruction() + 1);
                    interpreter.processInstruction(program);
                    program.set_actual_instruction(program. get_next_instruction());
                }
            }
            g.println("ENDING EXECUTION.");
        }else{
            g.println("NO PROGRAM CHARGED.");
        }
    }

    public void runProgram(String name){
        setProgram(name);
        runProgram();
    }

    //LIST PROGRAM
    public void listProgram(){ // LIST PARSING PROGRAM IF PROGRAM.PARSING TRUE, ELSE LIST UNPARSING
        if(program!=null) {
            if (program.isParsing()) {
                g.println("PARSING PROGRAM.");
                g.println("PARSING LISTING:");
                for (Instruction i : program.get_program()) {
                    i.printInstruction();
                }
            } else {
                g.println("NO PARSING PROGRAM.");
                g.println("UNPARSING LISTING:");
                for (String s : program.get_NoParsingProgram()) {
                    g.println(s);
                }
            }
            g.println("END LIST.");
        }else{
            g.println("DON'T LIST NO PROGRAM ");
        }
    }

    public void listProgram(boolean unparsing){ //LIST UNPARSING IF TRUE
        if(program!=null) {
            if (unparsing) {
                g.println("FORCE UNPARSING LISTING:");
                for (String s : program.get_NoParsingProgram()) {
                    g.println(s);
                }
                g.println("END LIST.");
            } else {
                listProgram();
            }
        }else {
            g.println("DON'T LIST NO PROGRAM ");
        }
    }

    public void listProgram(String name) {
        if(program==null)
            setProgram(name); // SETS PROGRAM
        listProgram();
    }

    public void listProgram(String name, boolean unparsing){
        if(program==null)
            setProgram(name); //SETS PROGRAM
        else
            listProgram(unparsing);
    }

    //LOAD PROGRAMS
    public com.mygdx.safe.IA.IAPrograms loadPrograms(){
        Json json = new Json();
        return json.fromJson(com.mygdx.safe.IA.IAPrograms.class, Gdx.files.internal(programspath+IAMASTERCONTROLNAME));
    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS

    public static String getIAMASTERCONTROLNAME() {
        return IAMASTERCONTROLNAME;
    }

    public static String getProgramspath() {
        return programspath;
    }

    public Instruction getI() {
        return i;
    }

    public com.mygdx.safe.IA.IAParser getIaparser() {
        return iaparser;
    }

    public com.mygdx.safe.IA.IAinterpreter getInterpreter() {
        return interpreter;
    }

    public com.mygdx.safe.IA.IAProgram getProgram() {
        return program;
    }

    public com.mygdx.safe.IA.IAPrograms getProgramsArray() {
        return programsArray;
    }

    public IAAccessClass getIaAccess() {
        return iaAccess;
    }

    public HashMap<String, com.mygdx.safe.IA.IAProgram> getIaprogs() {
        return iaprogs;
    }

    //SETTERS

    public void setProgram(String name){
        if(program==null) {
            program = iaprogs.get(name).getProgram();
            g.println("PROGRAM:" + name);
            program.set_loaded();
        }
        if(program==null){
            g.println("NO HAS PROGRAM :" + name);
        }
    }

    public void setProgram(com.mygdx.safe.IA.IAProgram program) {
        this.program = program;
    }

    public void setIaAccess(IAAccessClass iaAccess){
        this.iaAccess=iaAccess;
    }

    public static void setIAMASTERCONTROLNAME(String IAMASTERCONTROLNAME) {
        IAMasterControl.IAMASTERCONTROLNAME = IAMASTERCONTROLNAME;
    }

    public static void setProgramspath(String programspath) {
        IAMasterControl.programspath = programspath;
    }

    public void setI(Instruction i) {
        this.i = i;
    }

    public void setIaparser(com.mygdx.safe.IA.IAParser iaparser) {
        this.iaparser = iaparser;
    }

    public void setInterpreter(com.mygdx.safe.IA.IAinterpreter interpreter) {
        this.interpreter = interpreter;
    }

    public void setProgramsArray(com.mygdx.safe.IA.IAPrograms programsArray) {
        this.programsArray = programsArray;
    }

    public void setIaprogs(HashMap<String, com.mygdx.safe.IA.IAProgram> iaprogs) {
        this.iaprogs = iaprogs;
    }

}
