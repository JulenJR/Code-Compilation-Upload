package com.mygdx.safe.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.safe.Entities.GameEntity;
import com.mygdx.safe.EntityConfig;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;

import java.util.Random;

import static com.mygdx.safe.Components.InputComponent.InputMethod.AUTOMATIC;

/**
 * Created by Created by Boris.InspiratGames on 8/06/17.on 8/06/17.
 */

public class InputComponent implements Component  {

    //TAG
    private static final String TAG = InputComponent.class.getSimpleName();

    //ASPECTS
    private GenericMethodsInputProcessor g;

    //TYPES OF INPUT: AT FUTURE IS A HASHMAP
    public enum InputMethod{AUTOMATIC,ALL_INPUTS,NONE}
    private InputMethod i=AUTOMATIC;

    //DIRECTION CONTROL
    private boolean UP=false;
    private boolean RIGHT=false;
    private boolean DOWN=false;
    private boolean LEFT=false;
    private boolean LAST_UP=false;
    private boolean LAST_DOWN=false;
    private boolean LAST_LEFT=false;
    private boolean LAST_RIGHT=false;

    //DIRECTION AND STATE CHANGING
    private boolean CHANGE_DIRECTION=false;
    private boolean CHANGE_DIRECTION_FOR_PHYSICSDETECTION=false;
    private boolean CHANGEAUTOMATIC=false;
    private boolean FORCED_IDLE=false;

    //OTHER
    private boolean CONTROL_KEY=false;
    private Random random;
    private boolean _searchingDirection = false;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public InputComponent(GenericMethodsInputProcessor g){
        random=new Random();
        this.g=g;
    }

    //CONFIG
    public void config(EntityConfig e,InputMethod i){
        this.i=i;
    }

    //UPDATE && RENDER
    public void ProcessInput(GameEntity ge){
        PhysicsComponent pc=ge.getPhysicsComponent();
        switch(i) {
            case AUTOMATIC:
                if(pc.isForcingMove())setDirectionToForcedDestiny(ge, ge.getPhysicsComponent().getForcedMoveDestiny());
                else if(pc.getTarget() != null){
                    setDirectionToForcedDestiny(ge, ge.getPhysicsComponent().getTarget().getposition());
                }
                break;
            case ALL_INPUTS:
                if(pc.isForcingMove()) setDirectionToForcedDestiny(ge, ge.getPhysicsComponent().getForcedMoveDestiny());
                else AllInputsMovement();
                g.updateHasInputs();
                break;
            case NONE:
                break;
            default:
        }
    }

    /*_______________________________________________________________________________________________________________*/

    //SET DIRECTION TO FORCED DESTINY
    public void setDirectionToForcedDestiny(GameEntity ge, Vector2 destiny){
        PhysicsComponent pc=ge.getPhysicsComponent();
        float distanceX, distanceY;

        RIGHT =false; LEFT = false; UP = false; DOWN = false;

        distanceX = destiny.x - pc.getGeCurrentPos().x;
        distanceY = destiny.y - pc.getGeCurrentPos().y;

        if(distanceX < 0){LEFT = true ;RIGHT = false;}
        else if(distanceX > 0){ LEFT = false; RIGHT = true;}

        if(distanceY < 0){UP = false; DOWN = true;}
        else if(distanceY > 0){UP = true; DOWN = false;}


        if(!UP && !DOWN && !RIGHT && !LEFT){
            pc.setForcingMove(false);
        }
    }

    public void AutomaticMovement(GameEntity ge){
        PhysicsComponent pc=ge.getPhysicsComponent();
        int randomNum;

        //CHANGE ONE DIRECTION
        if(CHANGEAUTOMATIC) {



            if(FORCED_IDLE) randomNum = 18;
            else randomNum = random.nextInt(31);

            /*if(!FORCED_IDLE && ge.getID().contains("LESBOBIKE")){

                if(randomNum<27 && RIGHT) {randomNum=4; }
                else if(randomNum<27 && LEFT ) {randomNum=13;}
                else randomNum = random.nextInt(31);

            }

            if(!FORCED_IDLE && ge.getID().contains("LESBOBIKEFRONT")){

                if(randomNum<28 && UP) {
                    randomNum=3;
                    //g.println("LESBOBIKEFRONT *******************  UP");
                }
                else if(randomNum<27 && DOWN ) {
                    randomNum=11;
                    //g.println("LESBOBIKEFRONT *******************  DOWN");
                } else {
                    //g.println("LESBOBIKEFRONT *******************  RANDOM");
                    randomNum = random.nextInt(31);
                    while (randomNum == 4 || randomNum == 9 || randomNum == 12 || randomNum == 13 || randomNum == 20 || randomNum == 17) {
                        randomNum = random.nextInt(31);
                    }
                    //g.println("LESBOBIKEFRONT *******************  RANDOM="+randomNum);
                }

            }*/

            UP=DOWN=LEFT=RIGHT=false;

                     switch(randomNum){
                     case 0:case 3:case 5:
                         UP=true;
                         break;
                     case 2:case 6:case 10:
                         UP=true;RIGHT=true;
                         break;
                     case 4: case 9: case 12:
                         RIGHT=true;
                         break;
                     case 1:case 7:case 8:
                         DOWN=true;RIGHT=true;
                         break;
                     case 11:case 14:case 23:
                         DOWN=true;
                         break;
                     case 15:case 19:case 22:
                         DOWN=true;LEFT=true;
                         break;
                     case 13:case 20:case 17:
                         LEFT=true;
                         break;
                     case 16:case 21:case 25:
                         UP=true;LEFT=true;
                         break;
                     case 18:case 27:case 26:case 24:case 28:case 29:case 30:
                         UP=DOWN=LEFT=RIGHT=false;
                     default:
                         UP=DOWN=LEFT=RIGHT=false;
                         break;
                 }

                 if(UP || DOWN || LEFT || RIGHT) pc.setForcedMoveState("WALK");
             }

            changeAsignment();
            CHANGEAUTOMATIC=false;
            FORCED_IDLE=false;
    }

    public void AllInputsMovement(){

        CONTROL_KEY=g.isControl();
        UP=g.isUp();
        DOWN=g.isDown();
        LEFT=g.isLeft();
        RIGHT=g.isRight();

        changeAsignment();
    }


    public boolean notchangeComprobation(){
       return  (UP==LAST_UP) && (DOWN==LAST_DOWN) && (LEFT==LAST_LEFT) && (RIGHT==LAST_RIGHT);
    }

    public void changeAsignment(){
        if (notchangeComprobation()){
            CHANGE_DIRECTION=false;
        }else{
            CHANGE_DIRECTION=true;
            CHANGE_DIRECTION_FOR_PHYSICSDETECTION=true;
            LAST_DOWN=DOWN; LAST_UP=UP; LAST_LEFT=LEFT; LAST_RIGHT=RIGHT;
        }
    }

    public void getOppositeMovement(){

        if(UP && !DOWN && !RIGHT && !LEFT)
        {
            DOWN=true; UP=false; RIGHT=false; LEFT=false;
        }
        else if(DOWN && !UP && !RIGHT && !LEFT)
        {
            UP=true; DOWN=false; RIGHT=false; LEFT=false;
        }
        else if(RIGHT && !UP && !DOWN && LEFT)
        {
            LEFT=true; RIGHT=false; UP=false; DOWN=false;
        }
        else if(LEFT && !RIGHT && !UP && !DOWN)
        {
            RIGHT=true; LEFT=false; UP=false; DOWN=false;
        }
        else if(RIGHT && UP && !DOWN && !LEFT)
        {
            DOWN=true; LEFT=true; RIGHT=false; UP=false;
        }
        else if(DOWN && LEFT && !RIGHT && !UP)
        {
            RIGHT=true; UP=true; DOWN=false; LEFT=false;
        }
        else if(RIGHT && DOWN && !UP && !LEFT)
        {
            UP=true; LEFT=true; RIGHT=false; DOWN=false;
        }
        else if(DOWN && RIGHT && !UP && !LEFT)
        {
            UP=true; LEFT=true; DOWN=false; RIGHT=false;
        }else{
            UP=false; DOWN=false; LEFT=false; RIGHT=false;
        }

        changeAsignment();
    }

    public int getMovementNumber (){

        if(!DOWN && UP && !RIGHT && !LEFT) return 0;
        else if(!DOWN && UP && RIGHT && !LEFT) return 1;
        else if(!DOWN && !UP && RIGHT && !LEFT) return 2;
        else if(DOWN && !UP && RIGHT && !LEFT) return 3;
        else if(DOWN && !UP && !RIGHT && !LEFT) return 4;
        else if(DOWN && !UP && !RIGHT && LEFT) return 5;
        else if(!DOWN && !UP && !RIGHT && LEFT) return 6;
        else if(!DOWN && UP && !RIGHT && LEFT) return 7;
        else return -1;
    }

    public String getMovement(int movement){

        String movName= "";

        switch (movement){
            case 0:
                DOWN=false; UP=true; RIGHT=false; LEFT=false;
                movName = "UP";
                break;
            case 1:
                DOWN=false; UP=true; RIGHT=true; LEFT=false;
                movName = "UPRIGHT";
                break;
            case 2:
                DOWN=false; UP=false; RIGHT=true; LEFT=false;
                movName = "RIGHT";
                break;
            case 3:
                DOWN=true; UP=false; RIGHT=true; LEFT=false;
                movName = "DOWNRIGHT";
                break;
            case 4:
                DOWN=true; UP=false; RIGHT=false; LEFT=false;
                movName = "DOWN";
                break;
            case 5:
                DOWN=true; UP=false; RIGHT=false; LEFT=true;
                movName = "DOWNLEFT";
                break;
            case 6:
                DOWN=false; UP=false; RIGHT=false; LEFT=true;
                movName = "LEFT";
                break;
            case 7:
                DOWN=false; UP=true; RIGHT=false; LEFT=true;
                movName = "UPLEFT";
                break;
            default:
                DOWN=false; UP=false; RIGHT=false; LEFT=false;
                movName = "IDLE";
                break;
        }

        return movName;
    }

    public void getOtherMovement(){

        UP=DOWN=LEFT=RIGHT=false;

        switch(random.nextInt(31)){
            case 0:case 3:case 5:
                UP=true;
                break;
            case 2:case 6:case 10:
                UP=true;RIGHT=true;
                break;
            case 4: case 9: case 12:
                RIGHT=true;
                break;
            case 1:case 7:case 8:
                DOWN=true;RIGHT=true;
                break;
            case 11:case 14:case 23:
                DOWN=true;
                break;
            case 15:case 19:case 22:
                DOWN=true;LEFT=true;
                break;
            case 13:case 20:case 17:
                LEFT=true;
                break;
            case 16:case 21:case 25:
                UP=true;LEFT=true;
                break;
            case 18:case 27:case 26:case 24:case 28:case 29:case 30:
                UP=DOWN=LEFT=RIGHT=false;
                break;
            default:
                UP=DOWN=LEFT=RIGHT=false;
                break;
        }
         changeAsignment();
    }

    /*_______________________________________________________________________________________________________________*/

    //Getters

    public GenericMethodsInputProcessor getG() {
        return g;
    }

    public InputMethod getI() {
        return i;
    }

    public boolean isUP() {
        return UP;
    }

    public boolean isRIGHT() {
        return RIGHT;
    }

    public boolean isDOWN() {
        return DOWN;
    }

    public boolean isLEFT() {
        return LEFT;
    }

    public boolean isLAST_UP() {
        return LAST_UP;
    }

    public boolean isLAST_DOWN() {
        return LAST_DOWN;
    }

    public boolean isLAST_LEFT() {
        return LAST_LEFT;
    }

    public boolean isLAST_RIGHT() {
        return LAST_RIGHT;
    }

    public boolean isCHANGE_DIRECTION() {
        return CHANGE_DIRECTION;
    }

    public boolean isCHANGE_DIRECTION_FOR_PHYSICSDETECTION() {
        return CHANGE_DIRECTION_FOR_PHYSICSDETECTION;
    }

    public boolean isCHANGEAUTOMATIC() {
        return CHANGEAUTOMATIC;
    }

    public boolean isFORCED_IDLE() {
        return FORCED_IDLE;
    }

    public boolean isCONTROL_KEY() {
        return CONTROL_KEY;
    }

    public Random getRandom() {
        return random;
    }

    public boolean is_searchingDirection() {
        return _searchingDirection;
    }

    //Setters


    public void setI(InputMethod i) {
        this.i = i;
    }

    public void setUP(boolean UP) {
        this.UP = UP;
    }

    public void setRIGHT(boolean RIGHT) {
        this.RIGHT = RIGHT;
    }

    public void setDOWN(boolean DOWN) {
        this.DOWN = DOWN;
    }

    public void setLEFT(boolean LEFT) {
        this.LEFT = LEFT;
    }

    public void setLAST_UP(boolean LAST_UP) {
        this.LAST_UP = LAST_UP;
    }

    public void setLAST_DOWN(boolean LAST_DOWN) {
        this.LAST_DOWN = LAST_DOWN;
    }

    public void setLAST_LEFT(boolean LAST_LEFT) {
        this.LAST_LEFT = LAST_LEFT;
    }

    public void setLAST_RIGHT(boolean LAST_RIGHT) {
        this.LAST_RIGHT = LAST_RIGHT;
    }

    public void setCHANGE_DIRECTION(boolean CHANGE_DIRECTION) {
        this.CHANGE_DIRECTION = CHANGE_DIRECTION;
    }

    public void setCHANGE_DIRECTION_FOR_PHYSICSDETECTION(boolean CHANGE_DIRECTION_FOR_PHYSICSDETECTION) {
        this.CHANGE_DIRECTION_FOR_PHYSICSDETECTION = CHANGE_DIRECTION_FOR_PHYSICSDETECTION;
    }

    public void setCHANGEAUTOMATIC(boolean CHANGEAUTOMATIC) {
        this.CHANGEAUTOMATIC = CHANGEAUTOMATIC;
    }

    public void setFORCED_IDLE(boolean FORCED_IDLE) {
        this.FORCED_IDLE = FORCED_IDLE;
    }

    public void setCONTROL_KEY(boolean CONTROL_KEY) {
        this.CONTROL_KEY = CONTROL_KEY;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public void set_searchingDirection(boolean _searchingDirection) {
        this._searchingDirection = _searchingDirection;
    }
}

