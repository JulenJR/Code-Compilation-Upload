package com.mygdx.safe.IA;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Boris.InspitatGames on 25/07/17.
 */

public class IAProgram {

    //TAG
    private static final String TAG = IAMasterControl.class.getSimpleName();

    //ASPECTS
    private com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g;

    //PROGRAM
    private String program_name;
    private Array<Instruction> _program;
    private Array<String> _NoParsingProgram;

    //INSTRUCTION
    private int _actual_instruction;
    private int _next_instruction;

    //BOOLEANS
    private boolean _run=false;
    private boolean _finishing=false;
    private boolean _loading=false;
    private boolean _loaded=false;
    private boolean parsing;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public IAProgram(com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g){
        this.g = g;
         _program=new Array<Instruction>();
         _actual_instruction=_next_instruction=0;
    }

    /*_______________________________________________________________________________________________________________*/

    //LIST
    public void list(){
        for(Instruction i: _program){
            g.println(i.toString());
        }
    }

    //REMOVE INSTRUCTION
    public void removeInstruction(int num){
        _program.removeIndex(num);
    }

    //ADD INSTRUCTION
    public void addInstruction(Instruction i){
        _program.add(i);
        _loading=true;
    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS
    public boolean is_loading() {  return _loading;  }

    public boolean is_loaded() {   return _loaded;  }

    public boolean is_run() {   return _run; }

    public boolean is_finishing() {  return _finishing;  }

    public Instruction getInstruction(int num){
        return _program.get(num);
    }

    public Instruction getInstruction(String id){

        for(Instruction i: _program){
            if(i._id.contains(id))
                return i;
        }
        return null;
    }

    public int getNumInstructions(){
        return _program.size;
    }

    public IAProgram getProgram(){
        return this;
    }

    public String getProgram_name() {
        return program_name;
    }

    public Array<Instruction> get_program() {
        return _program;
    }

    public Array<String> get_NoParsingProgram() {
        return _NoParsingProgram;
    }

    public int get_actual_instruction() {
        return _actual_instruction;
    }

    public int get_next_instruction() {
        return _next_instruction;
    }

    public boolean isParsing() {
        return parsing;
    }

    //SETTERS
    public int set_loaded() {
        if(_loading && !_run) {
            _loaded = true;
            _loading = false;
            return 0;
        }else{
            return -1;
        }
    }

    public void set_loaded(boolean _loaded) {
        this._loaded = _loaded;
    }

    public int set_finishing(){
        if(_run){
            _finishing=true;
            _run=false;
            return 0;
        }else
            return -1;
    }

    public void set_finishing(boolean _finishing) {
        this._finishing = _finishing;
    }

    public int set_running(){
        if(_loaded && !_loading) {
            _run = true;
            return 0;
        }
        else
            return -1;
    }

    public void set_run(boolean _run) {
        this._run = _run;
    }

    public int set_loading(){
        if(!_loading && !_run && !_loaded)
        {
            _loading=true;
            return 0;
        }
        else
            return -1;
    }

    public void set_loading(boolean _loading) {
        this._loading = _loading;
    }

    public void setProgram_name(String program_name) {
        this.program_name = program_name;
    }

    public void set_program(Array<Instruction> _program) {
        this._program = _program;
    }

    public void set_NoParsingProgram(Array<String> _NoParsingProgram) {
        this._NoParsingProgram = _NoParsingProgram;
    }

    public void set_actual_instruction(int _actual_instruction) {
        this._actual_instruction = _actual_instruction;
    }

    public void set_next_instruction(int _next_instruction) {
        this._next_instruction = _next_instruction;
    }

    public void setParsing(boolean parsing) {
        this.parsing = parsing;
    }
}
