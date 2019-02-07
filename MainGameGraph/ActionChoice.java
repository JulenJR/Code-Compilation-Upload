package com.mygdx.safe.MainGameGraph;

/**
 * Created by Boris.InspiratGames on 30/10/17.
 */

public class ActionChoice {

    boolean blocker=false; // BLOCK OR UNBLOCK THE NEXT GRAPH NODE, default is false;
    private String ID;
    private String outerLookScreen; // TELLS THE LOCKSCREEN INTERSTATE
    private String OriginID;
    private String DestinyID;
    private int choiceCounter; // COUNTING  USES OF THIS CHOICE

    // LEVELi1@LEVELi2$CHOOSEi1@CHOOSEi2$WORLDi1@WORLDi2
    // ex: CHANGELVL#2#1$none~none
    // ex: none$KILL#GUARD_00$none
    // ex: none$CHARGE#ITEM#KEY$none
    // ex: none$none$CHANGELOOKSCREEN#DARKER
    // ex: none$CHANGE_OCTALYSIS#0,0,0,0,0,0,0,1$none
    // ex: none$BLOCKER#TRUE$none
    // ex: ROOT$none$none <---NOT USED
    // ex: SELECT$none@none
    // ex: PROXIMITY$none$none
    // ex: PORTALTAKE$none$none


    // # <- Instruction Commands separator
    // $ <- Diferent type Instruction Separator
    // @ <- Same type instruction Separator

    private String _instruction="none%none%none"; //LEVEL_Instruction$CHOOSE_Instruction$WORLD_Instruction

    private String _text;

    public ActionChoice(){

    }

    public String getContent(){
        return outerLookScreen + " [ OriginID: "+OriginID +"] [ DestinyID: "+DestinyID +
                "] [BLOCKER:"+ (blocker ?" ACTIVATE ":" DEACTIVATE ")+ "] [" +
                 "[ INSTRUCTION: " + _instruction +
                " ] [ TEXT: " + _text+" ]\n";

    }

    public String getOuterLookScreen() { return outerLookScreen; }

    public void setOuterLookScreen(String outerLookScreen) {  this.outerLookScreen = outerLookScreen; }


    public int getChoiceCounter() {
        return choiceCounter;
    }

    public void setChoiceCounter(int choiceCounter) {
        this.choiceCounter = choiceCounter;
    }

    public boolean isBlocker() {
        return blocker;
    }

    public void setBlocker(boolean blocker) {
        this.blocker = blocker;
    }

    public String getOriginID() {
        return OriginID;
    }

    public void setOriginID(String originID) {
        OriginID = originID;
    }

    public String getDestinyID() {
        return DestinyID;
    }

    public void setDestinyID(String destinyID) {
        DestinyID = destinyID;
    }



    public String get_instruction() {
        return _instruction;
    }

    public void set_instruction(String _instruction) {
        this._instruction = _instruction;
    }

    public String get_text() {
        return _text;
    }

    public void set_text(String _text) {
        this._text = _text;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}

