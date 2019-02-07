package com.mygdx.safe.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.Bone;
import com.esotericsoftware.spine.Skeleton;
import com.mygdx.safe.Conversation.TextActor;
import com.mygdx.safe.Entities.GameEntity;
import com.mygdx.safe.EntityConfig;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;
import com.mygdx.safe.MainGameGraph.ProgrammedMovement;
import com.mygdx.safe.SAnimation;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Boris.InspiratGames on 8/06/17.
 */

public  class PhysicsComponent implements Component {

    //TAG
    private static final String TAG = PhysicsComponent.class.getSimpleName();

    //ASPECTS
    private GenericMethodsInputProcessor g;
    private OrthographicCamera camera;


    //POSITION
    private Vector2 _nextEntityPosition=new Vector2(0,0);
    private Vector2 geCurrentPos=new Vector2(0,0);
    private Vector2 _oldEntityPosition = new Vector2(0,0);
    private HashMap<String, Vector2> _nMasterDistance = new HashMap<String, Vector2>();

    //SIZE
    private Vector2 _sSize= new Vector2(0,0);
    private Vector2 _sOffset=new Vector2(0,0);

    //VELOCITY & ACELERATION
    private Vector2 _velocity;
    private Vector2 _fixedVelocity;
    private Vector2 _aceleration=new Vector2(0,0);

    //BOUNDING BOXES

    private Rectangle _collisionBoundingBox = new Rectangle();
    private Rectangle _fullBodyBoundingBox = new Rectangle();


    // BOUNDINGBOXESBACK FOR REENABLING (IF THE ORIGINAL POINTERS SETTING TO NULL)

    private Rectangle _collisionBoundingBoxBack = new Rectangle();

    //FAKE NOT NULL DECLARED BOUNDINGBOXES FOR DISABLING BOUNDINGBOXES;

    private Rectangle _collisionBoundingBoxFake=new Rectangle();

    //PROXIMITY AREA
    private Circle proximityArea = new Circle(0, 0, 0);
    //private Rectangle _eventArea = new Rectangle();

    //CIRCUNSCRIPTIONS
    private String _currentCircuns;
    private boolean isChangingCircuns;

    //STATES & DIRECTIONS
    private Array<String> _states;
    private Array<String> _directions;
    private HashMap<String,Integer> _statesHash;
    private HashMap<String,Integer> _directionsHash;

    //PROGRAMMED MOVEMENT
    private ProgrammedMovement _moveProg;

    private String _lastDirection;
    private String _lastState;
    private String _currentState;
    private String _currentDirection;
    private int _secondsFORDIRECTION;

    //ROOTBONE
    private Bone _rootbone;

    //COLLISIONS
    private int _collisionMapCounter;
    private int _collisionEntityCounter;

    private boolean sCollisionMapLayer = false;
    private boolean nCollisionMapLayer = false;
    private boolean sCollisionEntity = false;
    private boolean nCollisionEntity = false;

    private boolean stopPhysicsUpdate = false;
    private boolean notMoveOptions = false;

    //FORCED MOVE
    private boolean forcingMove = false;
    private Vector2 forcedMoveDestiny = new Vector2(0,0);
    private String  forcedMoveState = "";

    private String  preferedTapForcedMoveState = "";
    private String  preferedIdleState = "";

    private GameEntity target = null;


    //GAMEENTITY
    private GameEntity ge;

    /*_______________________________________________________________________________________________________________*/

    public  PhysicsComponent(GenericMethodsInputProcessor g, OrthographicCamera camera, GameEntity gameEntity){
        this.camera=camera;
        this.g=g;
        _statesHash=new HashMap<String,Integer>();
        _directionsHash=new HashMap<String,Integer> ();
        _lastState="nothing";
        _lastDirection="nothing";
        _velocity=new Vector2(0f,0f);
        _secondsFORDIRECTION=0;
        _states=new Array<String>();
        _directions=new Array<String>();
        this.ge = gameEntity;
    }

    //CONFIGS
    public void config(EntityConfig e, Vector2 _entityposition, String ID){

        //CREATE ARRAYS FOR STATES AND DIRECTIONS
        for(String s:e.get_states()){_states.add(s);}
        for(String s:e.get_directions()){_directions.add(s);}

        //CREATE HASHMAPS
        Integer i=0;
        for(String _s:_states){
            _statesHash.put(_s,i);
            i++;
        }

        i=0;
        for(String _s:_directions){
            _directionsHash.put(_s,i);
            i++;
        }

        //SET CURRENT AND LAST STATE/DIRECTION
        _lastDirection=e.get_direction(); //PROGRAM LASTDIRECTION
        _currentDirection=_lastDirection;
        _lastState=e.get_state(); //PROGRAM LASTSTATE
        _currentState=_lastState;

        //SET POSITIONS
        set_currentEntityposition(_entityposition.x, _entityposition.y);
        set_nextEntityPosition(0 + _entityposition.x, 0 + _entityposition.y);

        //SET CURRENT CIRCUNSCRIPTION
        set_currentCircuns(ID + "#1" );

        //SET PLAYER PROXIMITY AREA

        proximityArea.setPosition(geCurrentPos);
        proximityArea.setRadius(e.get_proximityRadius());
    }

    public void config_Animation(EntityConfig e, SAnimation _sanimation, GameEntity ge) {


        //SET BOUNDING BOXES, MASTER DISTANCES AND TEXTACTORS FOR ALL THE NANIMATIONS
        if(!e.is_isSpine()){

            Iterator<String> itr = e.get_nAnimationTriConfigs().keySet().iterator();
            String key = itr.next();

            _collisionBoundingBox = createCollisionBoundingBox(false, e.get_nAnimationTriConfigs().get(key).get_nAnimationConfig().boundingBoxSetter,
                    ge.getposition(), null, e.get_nAnimationTriConfigs().get(key).get_nAnimationConfig(),
                    g.m.lvlMgr.get_nCacheMap().get(key).get_nAnimation().getNProgram(_lastState,_lastDirection, false).get_nanimation());

            _fullBodyBoundingBox = createCollisionBoundingBox(false, new Rectangle(0, 0, 1, 1), ge.getposition(), null, e.get_nAnimationTriConfigs().get(key).get_nAnimationConfig(),
                    g.m.lvlMgr.get_nCacheMap().get(key).get_nAnimation().getNProgram(_lastState,_lastDirection, false).get_nanimation());
        }else {

            // BACK ASIGNEMENT BOUNDINBOXES;

            _collisionBoundingBoxBack = _collisionBoundingBox; // BACK REFERENCE: OTHER REFERENCE ACCESS POINT;
            float widht, height, x, y;

            _rootbone = _sanimation.get_skeleton().getRootBone();

            widht = (_sanimation.get_skeleton().getData().getWidth() * e.get_sanimationConfig().skeletonScale) / 64;
            height = (_sanimation.get_skeleton().getData().getHeight() * e.get_sanimationConfig().skeletonScale) / 64;
            x = geCurrentPos.x - (widht / 2);
            y = geCurrentPos.y;

            _collisionBoundingBox = createCollisionBoundingBox(true, e.get_sanimationConfig().boundingBoxSetter, new Vector2(x, y), new Rectangle(x, y, widht, height), null, null);
            _fullBodyBoundingBox = createCollisionBoundingBox(true, new Rectangle(0, 0, 1, 1), new Vector2(x, y), new Rectangle(x, y, widht, height), null, null);

            // BACK ASIGNEMENT BOUNDINBOXES
            _collisionBoundingBoxBack = _collisionBoundingBox;     // BACK REFERENCE: OTHER REFERENCE ACCESS POINT;

            // SETTING TEXTACTOR FOR THIS SANIMATION:
            g.println("\n" + TAG + " CREATING " + e.get_entityID() + " SANIMATION TEXTACTOR");
            g.getTextactors().put(ge.getID(), new TextActor(camera, g));
            g.getTextActorConfig().Config(g.getTextactors().get(ge.getID()), g);
            // TEXT IS KEY OF HASH GEcount + e.get_entityID()
            g.getTextactors().get(ge.getID()).setText(e.get_entityID());
            g.getTextactors().get(ge.getID()).setName(e.get_entityID());
            // SETTING POSITION OF TEXTACTOR
            g.getTextactors().get(ge.getID()).setTextActorPosition(geCurrentPos.x + g.getTextactors().get(ge.getID()).getPositionDistChanger().x,
                    geCurrentPos.y + g.getTextactors().get(ge.getID()).getPositionDistChanger().y);
            g.println(TAG + " PHYSICS COMPONENT CONFIG TEXT FOR  [" + e.get_entityID() + "] TEXTACTOR POSITION : " + g.getTextactors().get(ge.getID()).getPosition());

            g.getTextactors().get(ge.getID()).addTextActorToHUD(g.m.lvlMgr.getH().getStage());

            //set_eventArea(_boundingBoxes.get(_boundingBoxes.keySet().toArray()[0]));
        }
    }

    /*_______________________________________________________________________________________________________________*/

    //CREATE BOUNDING BOX
    public Rectangle createCollisionBoundingBox(boolean isSBoundingBox, Rectangle setterBox, Vector2 position,
                                       Rectangle _sBoundingBox, EntityConfig.nAnimationConfig nAnimationConfig , Animation animation){

        float x, y, widht, height;

        if(isSBoundingBox){
            x = _sBoundingBox.x + (setterBox.x * _sBoundingBox.getWidth());
            y = _sBoundingBox.y ;
            widht = _sBoundingBox.getWidth() * setterBox.getWidth();
            height = _sBoundingBox.getHeight() * setterBox.getHeight();
        }
        else{

            float scale = nAnimationConfig.nScale;
            Sprite sprite = GraphicsComponent.selectFrame(animation, 0);

            if(scale == -1){

                float xScale = nAnimationConfig.xScale;
                float yScale = nAnimationConfig.yScale;

                x = position.x + (sprite.getWidth()* xScale * setterBox.getX());
                y = position.y + (sprite.getHeight()* yScale * setterBox.getY());
                widht = sprite.getWidth() * xScale * setterBox.getWidth();
                height = sprite.getHeight() * yScale * setterBox.getHeight();

            }
            else{
                x = position.x + (sprite.getWidth()* scale * setterBox.getX());
                y = position.y + (sprite.getHeight()* scale * setterBox.getY());
                widht = sprite.getWidth() * scale * setterBox.getWidth();
                height = sprite.getHeight() * scale * setterBox.getHeight();
            }
        }

        return (new Rectangle(x, y, widht, height));
    }

    public Vector2 getDestinyWhenSelectGE (GameEntity toInteractGE){

        HashMap<Integer, Vector2> dstHash = new HashMap<Integer, Vector2>();
        float skeletonScale = toInteractGE.getEntityConfig().get_sanimationConfig().skeletonScale;
        Skeleton skeleton= toInteractGE.getGraphicComponent().get_Sanimation().get_skeleton();

        Vector2 nearestPosition = null;

        dstHash.put(0, new Vector2(toInteractGE.getposition().x - (skeleton.getData().getWidth() * skeletonScale/64), toInteractGE.getposition().y + 0.5f));
        dstHash.put(1, new Vector2(toInteractGE.getposition().x + (skeleton.getData().getWidth() * skeletonScale/64), toInteractGE.getposition().y + 0.5f));

        boolean collisionable=false;

        for(Vector2 v:dstHash.values()) {

            //Calculate the distance between where the entity is and the possible destiny
            Vector2 distance = new Vector2(v.x - ge.getpositionx(), v.y - ge.getpositiony());

            //Modify the positions of the boundingboxes for collisions checking

            _collisionBoundingBox.setPosition(_collisionBoundingBox.x + distance.x, _collisionBoundingBox.y + distance.y);

            //Check if the possible destiny produces a collision
            collisionable = com.mygdx.safe.CollisionManager.isCollisionWhitMaplayer(g.m.lvlMgr.get_mapManager().getCollisionRectangles(), ge,g) ||
                    com.mygdx.safe.CollisionManager.isCollisionWithEntity(g.m.gsys.getEntities(), ge);

            _collisionBoundingBox.setPosition(_collisionBoundingBox.x - distance.x, _collisionBoundingBox.y - distance.y);

            if(collisionable) v.set(-1, -1);
        }

        for(int i=0; i<dstHash.size(); i++) {

            if (nearestPosition == null){
                if(dstHash.get(i).x != -1 && dstHash.get(i).y != -1) nearestPosition = dstHash.get(i);
            }
            else if (dstHash.get(i).x != -1 && dstHash.get(i).y != -1){
                if(nearestPosition.dst2(ge.getposition()) > dstHash.get(i).dst2(ge.getposition()))nearestPosition = dstHash.get(i);
            }
        }

        return nearestPosition;
    }

    public void nextMoveProg(){

        g.println(TAG + "  ----------------------------------------------  " + ge.getID());
        for(int i=0;i<_moveProg.getMoveProg_Steps().length;i++) {
            g.println(TAG + " STEPS " + _moveProg.getMoveProg_Steps()[i].toString());
        }

        String instruction[];
        Vector2 destiny = new Vector2();

        if(_moveProg.getCurrentStep() < 0 || _moveProg.getCurrentStep() > _moveProg.getMoveProg_Steps().length-1){

            g.println(TAG + " **************SETFORCINGMOVE:FALSE");
            setForcingMove(false);

            if(_moveProg.getCurrentStep() > _moveProg.getMoveProg_Steps().length-1) {
                g.println(TAG + " **************" + ge.getPendingOKinstructions().toString());
                _moveProg = null;
                _fixedVelocity =null;
                g.sendIntructionOK("MOVEPROG", ge.getPendingOKinstructions(), ge);
            }

        }
        else{
            instruction = _moveProg.getMoveProg_Steps()[_moveProg.getCurrentStep()].getFirst().split("_");
            g.println(TAG + " **************INSTRUCTION:"+_moveProg.getMoveProg_Steps()[_moveProg.getCurrentStep()].getFirst());


            if(instruction[0].equalsIgnoreCase("L")) destiny.set(getGeCurrentPos().x - Float.valueOf(instruction[1]), getGeCurrentPos().y);
            else if(instruction[0].equalsIgnoreCase("R")) destiny.set(getGeCurrentPos().x + Float.valueOf(instruction[1]), getGeCurrentPos().y);
            else if(instruction[0].equalsIgnoreCase("U")) destiny.set(getGeCurrentPos().x, getGeCurrentPos().y + Float.valueOf(instruction[1]));
            else if(instruction[0].equalsIgnoreCase("D")) destiny.set(getGeCurrentPos().x, getGeCurrentPos().y - Float.valueOf(instruction[1]));
            else if(instruction[0].equalsIgnoreCase("UL")) destiny.set(getGeCurrentPos().x - Float.valueOf(instruction[1]), getGeCurrentPos().y + Float.valueOf(instruction[1]));
            else if(instruction[0].equalsIgnoreCase("UR")) destiny.set(getGeCurrentPos().x + Float.valueOf(instruction[1]), getGeCurrentPos().y + Float.valueOf(instruction[1]));
            else if(instruction[0].equalsIgnoreCase("DL")) destiny.set(getGeCurrentPos().x - Float.valueOf(instruction[1]), getGeCurrentPos().y - Float.valueOf(instruction[1]));
            else if(instruction[0].equalsIgnoreCase("DR")) destiny.set(getGeCurrentPos().x + Float.valueOf(instruction[1]), getGeCurrentPos().y - Float.valueOf(instruction[1]));
            else{
                g.println(TAG + " ELSEINSTRUCTION**************SETFORCINGMOVE:FALSE");
                setForcingMove(false);
                //setForcedMoveDestiny(null);
                _moveProg = null;
                _fixedVelocity = null;
                return;
            }

            if(!isForcingMove() && _moveProg != null) setForcingMove(true);

            setForcedMoveDestiny(destiny);
            if(!instruction[2].equalsIgnoreCase("CURRENT"))setForcedMoveState(instruction[2]);

            if(instruction.length > 3){
                _fixedVelocity = new Vector2 ((Float.valueOf(instruction[1]) * GenericMethodsInputProcessor.DELTAUNITY) / Float.valueOf(instruction[3]),
                        (Float.valueOf(instruction[1]) * GenericMethodsInputProcessor.DELTAUNITY) / Float.valueOf(instruction[3]));
            }
        }
    }

    /*public void finishMoveProg(boolean state){
        // STATE=FALSE: abort moveprog execution.
        if(!state) {
            int moveProgInst = currentMoveProg.getFirst();
            currentMoveProg.getSecond().getSecond().getSecond().getFirst()[moveProgInst] = state;
            g.println(TAG + " FORCING (FAILING) END MOVEPROG");
        }

        currentMoveProg.setFirst(-1);
        g.println(TAG + " MOVEPROG : HAS ENDING.");

    }*/

    public Vector2 getDestinyForMovement(String direction, float distance){

        if(direction.equalsIgnoreCase("UP")){
            return new Vector2(geCurrentPos.x, geCurrentPos.y + distance);
        } else if(direction.equalsIgnoreCase("UPRIGHT")){
            return new Vector2(geCurrentPos.x + distance, geCurrentPos.y + distance);
        } else if(direction.equalsIgnoreCase("RIGHT")){
            return new Vector2(geCurrentPos.x + distance, geCurrentPos.y);
        } else if(direction.equalsIgnoreCase("DOWNRIGHT")){
            return new Vector2(geCurrentPos.x + distance, geCurrentPos.y -distance);
        } else if(direction.equalsIgnoreCase("DOWN")){
            return new Vector2(geCurrentPos.x, geCurrentPos.y -distance);
        } else if(direction.equalsIgnoreCase("DOWNLEFT")){
            return new Vector2(geCurrentPos.x -distance, geCurrentPos.y -distance);
        } else if(direction.equalsIgnoreCase("LEFT")){
            return new Vector2(geCurrentPos.x -distance, geCurrentPos.y);
        } else if(direction.equalsIgnoreCase("UPLEFT")){
            return new Vector2(geCurrentPos.x -distance, geCurrentPos.y +distance);
        }
        else return new Vector2(geCurrentPos.x, geCurrentPos.y);
    }

    public void enableBoundingBoxes(){

        _collisionBoundingBox=_collisionBoundingBoxBack;
    }

    public void disableBoundingBoxes(){

        _collisionBoundingBox =_collisionBoundingBoxFake;
    }

    public void increase__secondsFORDIRECTION(){ _secondsFORDIRECTION += 1;}

    /*_______________________________________________________________________________________________________________*/

    //Getters

    public GenericMethodsInputProcessor getG() {
        return g;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Vector2 get_nextEntityPosition() {
        return _nextEntityPosition;
    }

    public Vector2 getGeCurrentPos() {
        return geCurrentPos;
    }

    public Vector2 get_oldEntityPosition() {
        return _oldEntityPosition;
    }

    public HashMap<String, Vector2> get_nMasterDistance() {
        return _nMasterDistance;
    }

    public Vector2 get_sSize() {
        return _sSize;
    }

    public Vector2 get_sOffset() {
        return _sOffset;
    }

    public Vector2 get_velocity() {
        if(_fixedVelocity == null) return _velocity;
        else return _fixedVelocity;
    }

    public Vector2 get_fixedVelocity() {
        return _fixedVelocity;
    }

    public Vector2 get_aceleration() {
        return _aceleration;
    }

    public Rectangle get_collisionBoundingBox() {
        return _collisionBoundingBox;
    }

    public Rectangle get_collisionBoundingBoxFake() {
        return _collisionBoundingBoxFake;
    }

    public Rectangle get_collisionBoundingBoxBack() {
        return _collisionBoundingBoxBack;
    }

    public Circle getProximityArea() {
        return proximityArea;
    }

    public String get_currentCircuns() {
        return _currentCircuns;
    }

    public Array<String> get_states() {
        return _states;
    }

    public Array<String> get_directions() {
        return _directions;
    }

    public HashMap<String, Integer> get_statesHash() {
        return _statesHash;
    }

    public HashMap<String, Integer> get_directionsHash() {
        return _directionsHash;
    }

    public String get_lastDirection() {
        return _lastDirection;
    }

    public String get_lastState() {
        return _lastState;
    }

    public String get_currentState() {
        return _currentState;
    }

    public String get_currentDirection() {
        return _currentDirection;
    }

    public int get_secondsFORDIRECTION() {
        return _secondsFORDIRECTION;
    }

    public Bone get_rootbone() {
        return _rootbone;
    }

    public int get_collisionMapCounter() {
        return _collisionMapCounter;
    }

    public int get_collisionEntityCounter() {
        return _collisionEntityCounter;
    }

    public boolean issCollisionMapLayer() {
        return sCollisionMapLayer;
    }

    public boolean isnCollisionMapLayer() {
        return nCollisionMapLayer;
    }

    public boolean issCollisionEntity() {
        return sCollisionEntity;
    }

    public boolean isnCollisionEntity() {
        return nCollisionEntity;
    }

    public boolean isStopPhysicsUpdate() {
        return stopPhysicsUpdate;
    }

    public boolean isNotMoveOptions() {
        return notMoveOptions;
    }

    public boolean isForcingMove() {
        return forcingMove;
    }

    public Vector2 getForcedMoveDestiny() {
        return forcedMoveDestiny;
    }

    public String getForcedMoveState() {
        return forcedMoveState;
    }

    public com.mygdx.safe.MainGameGraph.ProgrammedMovement get_moveProg() {
        return _moveProg;
    }

    public GameEntity getTarget() {
        return target;
    }

    public boolean isChangingCircuns() {
        return isChangingCircuns;
    }

    public Rectangle get_fullBodyBoundingBox() {
        return _fullBodyBoundingBox;
    }

    public GameEntity getGe() {
        return ge;
    }

    public String getPreferedTapForcedMoveState() {
        return preferedTapForcedMoveState;
    }

    public String getPreferedIdleState() {
        return preferedIdleState;
    }

    //Setters

    public void setBoundsAfterMovement(Vector2 movement, boolean backToOldPosition){

        if (backToOldPosition) movement.set(movement.x * (-1), movement.y *(-1));

        _collisionBoundingBox.setPosition(_collisionBoundingBox.x + movement.x, _collisionBoundingBox.y + movement.y);
        _fullBodyBoundingBox.setPosition(_fullBodyBoundingBox.x + movement.x, _fullBodyBoundingBox.y + movement.y);
    }

    public void setBoundsAfterMovement(float x, float y){

        Vector2 distanceToCurrentPos = new Vector2(x - ge.getpositionx(), y - ge.getpositiony());

        _collisionBoundingBox.setPosition(_collisionBoundingBox.x + distanceToCurrentPos.x, _collisionBoundingBox.y + distanceToCurrentPos.y);
        _fullBodyBoundingBox.setPosition(_fullBodyBoundingBox.x + distanceToCurrentPos.x, _fullBodyBoundingBox.y + distanceToCurrentPos.y);

    }

    public void firstSetBounds(){

        //g.printlns("***************** " + ge.getID() + "      " + _collisionBoundingBox.getX() + "    " + _collisionBoundingBox.getY() +"     " + geCurrentPos);

        _collisionBoundingBox.setPosition(_collisionBoundingBox.x + geCurrentPos.x, _collisionBoundingBox.y + geCurrentPos.y);
        _fullBodyBoundingBox.setPosition(_fullBodyBoundingBox.x + geCurrentPos.x, _fullBodyBoundingBox.y + geCurrentPos.y);

        //g.printlns("***************** " + ge.getID() + "      " + _collisionBoundingBox.getX() + "    " + _collisionBoundingBox.getY() +"     " + geCurrentPos);

    }

    public boolean setDirection(String d){
        if(d.equals(_currentDirection)){
            return false;
        }else{
            _lastDirection=_currentDirection;
            _currentDirection=d;
            return true;
        }
    }

    public boolean setState(String s){
        if(s.equals(_currentState)){
            return false;
        }else{
            _lastState=_currentState;
            _currentState=s;
            return true;
        }
    }

    /*public void set_eventArea(Rectangle bbox) {
        this._eventArea.set(bbox.getX()- 0.25f, bbox.getY() - 0.25f, bbox.getWidth() + 0.5f, bbox.getHeight() + 0.5f);
    }*/

    public void set_nextEntityPosition(float x, float y) {
        this._nextEntityPosition.x = x;
        this._nextEntityPosition.y = y;
    }

    public void set_oldEntityPosition(float x, float y){
        this._oldEntityPosition.x = x;
        this._oldEntityPosition.y = y;
    }

    public void set_currentEntityposition(float positionx, float positiony) {
        geCurrentPos.x= positionx;
        geCurrentPos.y= positiony;
    }

    public void set_currentEntityposition(Vector2 _nextEntityPosition) {
        this.geCurrentPos = _nextEntityPosition;
    }

    public void set_velocity(float velocityx,float velocityy) {
        this._velocity.x = velocityx;
        this._velocity.y = velocityy;
    }

    public void set_nextEntityPosition(Vector2 _nextEntityPosition) {
        this._nextEntityPosition = _nextEntityPosition;
    }

    public void setGeCurrentPos(Vector2 geCurrentPos) {
        this.geCurrentPos = geCurrentPos;
    }

    public void set_oldEntityPosition(Vector2 _oldEntityPosition) {
        this._oldEntityPosition = _oldEntityPosition;
    }

    public void set_nMasterDistance(HashMap<String, Vector2> _nMasterDistance) {
        this._nMasterDistance = _nMasterDistance;
    }

    public void set_sSize(Vector2 _sSize) {
        this._sSize = _sSize;
    }

    public void set_sOffset(Vector2 _sOffset) {
        this._sOffset = _sOffset;
    }

    public void set_velocity(Vector2 _velocity) {
        this._velocity = _velocity;
    }

    public void set_fixedVelocity(Vector2 _fixedVelocity) {
        this._fixedVelocity = _fixedVelocity;
    }

    public void set_aceleration(Vector2 _aceleration) {
        this._aceleration = _aceleration;
    }

    public void set_collisionBoundingBox(Rectangle _collisionBoundingBox) {
        this._collisionBoundingBox = _collisionBoundingBox;
    }

    public void set_collisionBoundingBoxBack(Rectangle _collisionBoundingBoxBack) {
        this._collisionBoundingBoxBack = _collisionBoundingBoxBack;
    }

    public void set_collisionBoundingBoxFake(Rectangle _collisionBoundingBoxFake) {
        this._collisionBoundingBoxFake = _collisionBoundingBoxFake;
    }

    public void setProximityArea(Circle proximityArea) {
        this.proximityArea = proximityArea;
    }

    public void set_currentCircuns(String _newCurrentCircuns) {

        if(_newCurrentCircuns == null){
            this._currentCircuns = null;
            return;
        }

        HashMap<String, Rectangle> circuns = g.m.lvlMgr.get_mapManager().get_currentMap().getCircunscriptionRectangles();

        if(circuns.get(_newCurrentCircuns) == null){
            g.println("\n"+TAG + " SET CURRENT CIRCUNS: CIRCUNSCRIPTION: " +  _newCurrentCircuns + " NOT EXIST!!");
            return;
        }
        else{
            this._currentCircuns = _newCurrentCircuns;
        }
    }

    public void set_states(Array<String> _states) {
        this._states = _states;
    }

    public void set_directions(Array<String> _directions) {
        this._directions = _directions;
    }

    public void set_statesHash(HashMap<String, Integer> _statesHash) {
        this._statesHash = _statesHash;
    }

    public void set_directionsHash(HashMap<String, Integer> _directionsHash) {
        this._directionsHash = _directionsHash;
    }

    public void set_lastDirection(String _lastDirection) {
        this._lastDirection = _lastDirection;
    }

    public void set_lastState(String _lastState) {
        this._lastState = _lastState;
    }

    public void set_currentState(String _currentState) {
        this._currentState = _currentState;
    }

    public void set_currentDirection(String _currentDirection) {
        this._currentDirection = _currentDirection;
    }

    public void set_secondsFORDIRECTION(int _secondsFORDIRECTION) {
        this._secondsFORDIRECTION = _secondsFORDIRECTION;
    }

    public void set_rootbone(Bone _rootbone) {
        this._rootbone = _rootbone;
    }

    public void set_collisionMapCounter(int _collisionMapCounter) {
        this._collisionMapCounter = _collisionMapCounter;
    }

    public void set_collisionEntityCounter(int _collisionEntityCounter) {
        this._collisionEntityCounter = _collisionEntityCounter;
    }

    public void setsCollisionMapLayer(boolean sCollisionMapLayer) {
        this.sCollisionMapLayer = sCollisionMapLayer;
    }

    public void setnCollisionMapLayer(boolean nCollisionMapLayer) {
        this.nCollisionMapLayer = nCollisionMapLayer;
    }

    public void setsCollisionEntity(boolean sCollisionEntity) {
        this.sCollisionEntity = sCollisionEntity;
    }

    public void setnCollisionEntity(boolean nCollisionEntity) {
        this.nCollisionEntity = nCollisionEntity;
    }

    public void setStopPhysicsUpdate(boolean stopPhysicsUpdate) {
        this.stopPhysicsUpdate = stopPhysicsUpdate;
    }

    public void setNotMoveOptions(boolean notMoveOptions) {
        this.notMoveOptions = notMoveOptions;
    }

    public void setForcingMove(boolean forcingMove) {
        this.forcingMove = forcingMove;
    }

    public void setForcedMoveDestiny(Vector2 forcedMoveDestiny) {
        this.forcedMoveDestiny = forcedMoveDestiny;
    }

    public void setForcedMoveDestiny(float x, float y) {
        this.forcedMoveDestiny.x = x;
        this.forcedMoveDestiny.y = y;
    }

    public void setForcedMoveState(String forcedMoveState) {
        this.forcedMoveState = forcedMoveState;
    }

    public void set_moveProg(com.mygdx.safe.MainGameGraph.ProgrammedMovement _moveProg) {
        this._moveProg = _moveProg;
    }

    public void setTarget(GameEntity target) {
        this.target = target;
    }

    public void set_fullBodyBoundingBox(Rectangle _fullBodyBoundingBox) {
        this._fullBodyBoundingBox = _fullBodyBoundingBox;
    }

    public void setPreferedTapForcedMoveState(String preferedTapForcedMoveState) {
        this.preferedTapForcedMoveState = preferedTapForcedMoveState;
    }

    public void setPreferedIdleState(String preferedIdleState) {
        this.preferedIdleState = preferedIdleState;
    }

    public void setChangingCircuns(boolean changingCircuns) {
        isChangingCircuns = changingCircuns;
    }
}


    /*//SETS THE POSITION OF THE PLAYER WHEN NEW MAP IS RENDERED
    public void setPositioninNewMap (Vector2 _entityposition){

        set_currentEntityposition(_entityposition);
        _lastDirection= "DOWN"; //PROGRAM LASTDIRECTION
        _currentDirection=_lastDirection;
        _lastState= "IDLE"; //PROGRAM LASTSTATE
        _currentState=_lastState;

    }*/
