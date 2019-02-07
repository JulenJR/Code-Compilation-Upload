package com.mygdx.safe.MainGameGraph;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;
import com.mygdx.safe.Pair;

/**
 * Created by Boris.InspiratGames on 12/01/18.
 */

public class ProgrammedMovement {

    Integer StateActive = -1; // -1 is not Active, [0, NN] is current position instruction in ProgammedMovement
    String MoveProgName = "";
    String EntityOwner = "";
    Boolean RouteCrashState = true;
    Pair<String, Boolean> MoveProg_Steps[]; //L_2_0.5_STATE == left 2 tiles at 0.5 velocity
    Vector2 _preStablishedStartPoint;
    Boolean LOOP = false;
    Integer currentStep = 0;

    GenericMethodsInputProcessor g;


    public ProgrammedMovement(){

    }

    public ProgrammedMovement(ProgrammedMovement moveProg){
        this(moveProg.getStateActive(), moveProg.getMoveProgName(), moveProg.getEntityOwner(), moveProg.getRouteCrashState(), moveProg.getMoveProg_Steps(), moveProg.get_preStablishedStartPoint()
                , moveProg.getLOOP(), moveProg.getG());
    }

    public ProgrammedMovement(Integer StateActive, String MoveProgName, String EntityOwner, Boolean RouteCrashState, Pair<String, Boolean>[] MoveProg_Steps, Vector2 StartPoint, Boolean LOOP, GenericMethodsInputProcessor g){
        this.StateActive=StateActive;
        this.MoveProgName=MoveProgName;
        this.EntityOwner=EntityOwner;
        this.RouteCrashState = RouteCrashState;

        this.MoveProg_Steps = MoveProg_Steps;

        this._preStablishedStartPoint = StartPoint;
        this.LOOP = LOOP;
        this.g = g;

    }

    public void stepOK(){


        //THE STEP IS OKEY
        MoveProg_Steps[currentStep].setSecond(true);

        g.println(MoveProg_Steps[currentStep].getFirst() + " okkkeeyy" + MoveProg_Steps[currentStep].getSecond()        );

        //NORMALLY ONLY INCREMENT THE VALUE OF THE CURRENTSTEP FOR THE NEXT, BUT IF LOOP IS TRUE THE VALUE COME BACK TO 0 WHEN THERE ARE NO MORE STEPS
        if(currentStep + 1 >= MoveProg_Steps.length && LOOP)currentStep = 0;
        else currentStep++;
    }


    public String getEntityOwner() {
        return EntityOwner;
    }

    public void setEntityOwner(String entityOwner) {
        EntityOwner = entityOwner;
    }


    public Integer getStateActive() {
        return StateActive;
    }

    public void setStateActive(Integer stateActive) {
        StateActive = stateActive;
    }

    public String getMoveProgName() {
        return MoveProgName;
    }

    public void setMoveProgName(String moveProgName) {
        MoveProgName = moveProgName;
    }

    public Boolean getRouteCrashState() {
        return RouteCrashState;
    }

    public void setRouteCrashState(Boolean routeCrashState) {
        RouteCrashState = routeCrashState;
    }

    public Pair<String, Boolean>[] getMoveProg_Steps() {
        return MoveProg_Steps;
    }

    public void setMoveProg_Steps(Pair<String, Boolean>[] moveProg_Steps) {
        MoveProg_Steps = moveProg_Steps;
    }

    public Vector2 get_preStablishedStartPoint() {
        return _preStablishedStartPoint;
    }

    public void set_preStablishedStartPoint(Vector2 _preStablishedStartPoint) {
        this._preStablishedStartPoint = _preStablishedStartPoint;
    }

    public Boolean getLOOP() {
        return LOOP;
    }

    public void setLOOP(Boolean LOOP) {
        this.LOOP = LOOP;
    }

    public Integer getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(Integer currentStep) {
        this.currentStep = currentStep;
    }

    public GenericMethodsInputProcessor getG() {
        return g;
    }
}

