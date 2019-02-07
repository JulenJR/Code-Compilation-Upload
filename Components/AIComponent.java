package com.mygdx.safe.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mygdx.safe.CollisionManager;
import com.mygdx.safe.Entities.GameEntity;
import com.mygdx.safe.EntityConfig;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;

import static com.badlogic.gdx.math.MathUtils.random;

/**
 * Created by Created by Boris.InspiratGames on 8/06/17 on 8/06/17.
 */

public class AIComponent implements Component {


    //TAG
    private static final String TAG = AIComponent.class.getSimpleName();

    //ASPECTS
    private GenericMethodsInputProcessor g;

    //SPECIAL ENTITYS (INPUT COMPONENT MUST BE "NONE")
    private boolean specialEntity=false;

    //OTHER
    private boolean controlSecondIA=false;
    private boolean controlProgram=false;
    private boolean programActivated=false;
    private boolean programFinished=false;
    private boolean InstructionActivated=false;
    private boolean InstructionFinished=false;
    private Array<String> posibleDirections = new Array<String>();
    private boolean _changingDirection = false;
    private int _oppDirectionCounter = 0;
    private int secondIA=0;
    private float totalsecondIA=0;
    private float programtime=0;
    private float delta=0f;

    // GRAPH CONTROLLER
    private boolean graphControllerAnim=false;
    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public  AIComponent(GenericMethodsInputProcessor g){
      this.g=g;
        posibleDirections.addAll("UP", "UPRIGHT", "RIGHT", "DOWNRIGHT", "DOWN", "DOWNLEFT", "LEFT", "UPLEFT");
    }


    // CONFIG

    public void config(EntityConfig e){
        if(e.is_specialEntity()) {
            specialEntity = e.is_specialEntity();
            posibleDirections.clear();
            posibleDirections.addAll(e.get_directions());

        }
    }

    // UPDATE,RENDER
    public void update(float delta,boolean tick) {

        this.delta = delta;
        if(controlSecondIA)
            totalsecondIA += delta;
        if (tick)
            if (controlSecondIA){
                secondIA++;
            } else {
                secondIA = 0;
                totalsecondIA = 0;
            }
    }

     /*_______________________________________________________________________________________________________________*/

    public void oppositeDirection(InputComponent i, com.mygdx.safe.Components.PhysicsComponent pc){

        if(!specialEntity) {
            if ((i.isUP() && i.isDOWN() == i.isRIGHT() == i.isLEFT() == false) || (i.isDOWN() && i.isUP() == i.isRIGHT() == i.isLEFT() == false)) {
                pc.set_velocity((pc.get_velocity().x), -(pc.get_velocity().y));
            } else if ((i.isRIGHT() && i.isDOWN() == i.isUP() == i.isLEFT() == false) || (i.isLEFT() && i.isUP() == i.isRIGHT() == i.isDOWN() == false)) {
                pc.set_velocity(-(pc.get_velocity().x), (pc.get_velocity().y));
            } else if ((i.isRIGHT() && i.isUP() && !(i.isLEFT()) && !(i.isDOWN())) || (i.isLEFT() && i.isUP() && !(i.isRIGHT()) && !(i.isDOWN()))
                    || (i.isRIGHT() && i.isDOWN() && !(i.isLEFT()) && !(i.isUP())) || (i.isLEFT() && i.isDOWN() && !(i.isRIGHT()) && !(i.isUP()))) {
                pc.set_velocity(-(pc.get_velocity().x), -(pc.get_velocity().y));
            }

            _oppDirectionCounter = 1;
        }

    }

    public void changeToPossibleDirection(GameEntity ge, Array<GameEntity> entities, Array<Rectangle> mapRectangles, String currentDirection){

        if(!specialEntity) {
            if (currentDirection != null) {
                for (int i = 0; i < posibleDirections.size; i++) {
                    if (posibleDirections.get(i).contains(currentDirection)) {
                        programStateDirection(ge, "IDLE");
                        posibleDirections.removeIndex(i);
                    }
                }
            } else {
                boolean hasCollision = false;
                String direction = null;

                int index = random.nextInt(posibleDirections.size + 20); //2 more numbers that will correspond to IDLE
                if (index < posibleDirections.size) direction = posibleDirections.get(index);

                if (direction != null) {
                    if (!_changingDirection) _changingDirection = true;

                    float x = ge.getPhysicsComponent().get_velocity().x;
                    float y = ge.getPhysicsComponent().get_velocity().y;

                    if (direction.contains("UP"))
                        hasCollision = CollisionManager.isCollisionWithEntity(entities, ge, 0, y) || CollisionManager.isCollisionWhitMaplayer(mapRectangles, ge, 0, y,g);
                    else if (direction.contains("UPRIGHT"))
                        hasCollision = CollisionManager.isCollisionWithEntity(entities, ge, x, y) || CollisionManager.isCollisionWhitMaplayer(mapRectangles, ge, x, y,g);
                    else if (direction.contains("RIGHT"))
                        hasCollision = CollisionManager.isCollisionWithEntity(entities, ge, x, 0) || CollisionManager.isCollisionWhitMaplayer(mapRectangles, ge, x, 0,g);
                    else if (direction.contains("DOWNRIGHT"))
                        hasCollision = CollisionManager.isCollisionWithEntity(entities, ge, x, -y) || CollisionManager.isCollisionWhitMaplayer(mapRectangles, ge, x, -y,g);
                    else if (direction.contains("DOWN"))
                        hasCollision = CollisionManager.isCollisionWithEntity(entities, ge, 0, -y) || CollisionManager.isCollisionWhitMaplayer(mapRectangles, ge, 0, -y,g);
                    else if (direction.contains("DOWNLEFT"))
                        hasCollision = CollisionManager.isCollisionWithEntity(entities, ge, -x, -y) || CollisionManager.isCollisionWhitMaplayer(mapRectangles, ge, -x, -y,g);
                    else if (direction.contains("LEFT"))
                        hasCollision = CollisionManager.isCollisionWithEntity(entities, ge, -x, 0) || CollisionManager.isCollisionWhitMaplayer(mapRectangles, ge, -x, 0,g);
                    else if (direction.contains("UPLEFT"))
                        hasCollision = CollisionManager.isCollisionWithEntity(entities, ge, -x, y) || CollisionManager.isCollisionWhitMaplayer(mapRectangles, ge, -x, y,g);

                    if (hasCollision) {
                        programStateDirection(ge, "IDLE");
                        posibleDirections.removeIndex(index);
                    } else {
                        programStateDirection(ge, direction);
                        posibleDirections.clear();
                        posibleDirections.addAll("UP", "UPRIGHT", "RIGHT", "DOWNRIGHT", "DOWN", "DOWNLEFT", "LEFT", "UPLEFT");
                        _changingDirection = false;
                    }
                } else {
                    programStateDirection(ge, "IDLE");
                    posibleDirections.clear();
                    posibleDirections.addAll("UP", "UPRIGHT", "RIGHT", "DOWNRIGHT", "DOWN", "DOWNLEFT", "LEFT", "UPLEFT");
                    _changingDirection = false;
                }
            }
        }

    }

    public void program(GameEntity ge, GameEntity CollisionGE, String program, float time){
        if(!specialEntity) {
            controlProgram = true;

            /*for (String S : ge.getPhysicsComponent().get_boundingBoxes().keySet()) {
                g.println(ge.getPhysicsComponent().get_boundingBoxes().get(S) + "  " + S);
            }*/

            if (program.contains("OPPOSITEDIRECTION")) {
                ge.getAiComponent().oppositeDirection(ge.getInputComponent(), ge.getPhysicsComponent());
                controlSecondIA = true;
                programtime = time;
            }
        }
    }

    public void programStateDirection(GameEntity ge, String movement){

        if(!specialEntity && !graphControllerAnim) {

            InputComponent i = ge.getInputComponent();
            i.setUP(false);
            i.setDOWN(false);
            i.setLEFT(false);
            i.setRIGHT(false);

            if (movement.contains("UP")) i.setUP(true);
            if (movement.contains("DOWN")) i.setDOWN(true);
            if (movement.contains("RIGHT")) i.setRIGHT(true);
            if (movement.contains("LEFT")) i.setLEFT(true);

            programStateDirection(ge.getEntityConfig(), i.isUP(), i.isDOWN(), i.isLEFT(), i.isRIGHT(), ge.getGraphicComponent(), ge.getPhysicsComponent());
        }

    }

    public void programStateDirection(EntityConfig e, boolean UP, boolean DOWN, boolean LEFT, boolean RIGHT, GraphicsComponent gc, PhysicsComponent pc){

        if(!specialEntity && !graphControllerAnim) {

            if (UP == false && DOWN == false && LEFT == false && RIGHT == false) {

                if(!gc.getGe().getPhysicsComponent().getPreferedIdleState().equalsIgnoreCase("")){
                    gc.set_state(pc.setState(gc.getGe().getPhysicsComponent().getPreferedIdleState()));
                }
                else gc.set_state(pc.setState("IDLE"));



                gc.set_direction(pc.setDirection("DOWN"));
                pc.set_secondsFORDIRECTION(0);
            } else {

                if (pc.get_states().contains("WALK", false) || pc.get_states().contains("RUN", false) || pc.get_states().contains("ATTACK", false) || pc.get_states().contains("MOVE", false)) {

                    if (pc.get_states().contains(pc.getForcedMoveState(), false))
                        gc.set_state(pc.setState(pc.getForcedMoveState()));
                    else {gc.set_state(pc.setState("IDLE"));
                          gc.set_direction(pc.setDirection("DOWN"));
                    }

                    if(!pc.get_currentState().contains("IDLE")){
                        if (UP && LEFT) gc.set_direction(pc.setDirection("UPLEFT"));
                        else if (UP && RIGHT) gc.set_direction(pc.setDirection("UPRIGHT"));
                        else if (DOWN && LEFT) gc.set_direction(pc.setDirection("DOWNLEFT"));
                        else if (DOWN && RIGHT) gc.set_direction(pc.setDirection("DOWNRIGHT"));
                        else if (UP && !LEFT && !RIGHT) gc.set_direction(pc.setDirection("UP"));
                        else if (DOWN && !LEFT && !RIGHT) gc.set_direction(pc.setDirection("DOWN"));
                        else if (RIGHT && !UP && !DOWN) gc.set_direction(pc.setDirection("RIGHT"));
                        else if (LEFT && !UP && !DOWN) gc.set_direction(pc.setDirection("LEFT"));
                    }
                } else {
                    gc.set_state(pc.setState("IDLE"));
                    gc.set_direction(pc.setDirection("DOWN"));
                }

            }
        }
    }

    /*_______________________________________________________________________________________________________________*/

    // GETTERS AND SETTERS

    //SETTERS


    public void setGraphControllerAnim(boolean graphControllerAnim) {
        this.graphControllerAnim = graphControllerAnim;
    }

    public void set_changingDirection(boolean _changingDirection) {
        this._changingDirection = _changingDirection;
    }

    public void setControlProgram(boolean controlProgram) {
        this.controlProgram = controlProgram;
    }

    public void setControlSecondIA(boolean controlSecondIA) {
        this.controlSecondIA = controlSecondIA;
    }

    public void setG(GenericMethodsInputProcessor g) {
        this.g = g;
    }

    public void setProgramActivated(boolean programActivated) {
        this.programActivated = programActivated;
    }

    public void setProgramFinished(boolean programFinished) {
        this.programFinished = programFinished;
    }

    public void setInstructionActivated(boolean instructionActivated) {
        InstructionActivated = instructionActivated;
    }

    public void setInstructionFinished(boolean instructionFinished) {
        InstructionFinished = instructionFinished;
    }

    public void set_oppDirectionCounter(int _oppDirectionCounter) {
        this._oppDirectionCounter = _oppDirectionCounter;
    }

    public void setPosibleDirections(Array<String> posibleDirections) {
        this.posibleDirections = posibleDirections;
    }

    public void setProgramtime(float programtime) {
        this.programtime = programtime;
    }

    public void setSecondIA(int secondIA) {
        this.secondIA = secondIA;
    }

    public void setSpecialEntity(boolean specialEntity) {
        this.specialEntity = specialEntity;
    }

    public void setTotalsecondIA(float totalsecondIA) {
        this.totalsecondIA = totalsecondIA;
    }

    //GETERS


    public boolean isGraphControllerAnim() {
        return graphControllerAnim;
    }

    public GenericMethodsInputProcessor getG() {
        return g;
    }

    public int get_oppDirectionCounter() {
        return _oppDirectionCounter;
    }

    public Array<String> getPosibleDirections() {
        return posibleDirections;
    }

    public float getProgramtime() {
        return programtime;
    }

    public int getSecondIA() {
        return secondIA;
    }

    public float getTotalsecondIA() {
        return totalsecondIA;
    }

    public boolean is_changingDirection() {
        return _changingDirection;
    }

    public boolean isControlProgram() {
        return controlProgram;
    }

    public boolean isControlSecondIA() {
        return controlSecondIA;
    }

    public boolean isInstructionActivated() {
        return InstructionActivated;
    }

    public boolean isInstructionFinished() {
        return InstructionFinished;
    }

    public boolean isProgramActivated() {
        return programActivated;
    }

    public boolean isProgramFinished() {
        return programFinished;
    }

    public boolean isSpecialEntity() {
        return specialEntity;
    }

}
