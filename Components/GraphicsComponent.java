package com.mygdx.safe.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.safe.Entities.GameEntity;
import com.mygdx.safe.EntityConfig;
import com.mygdx.safe.GCnAnimation;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;
import com.mygdx.safe.NanimationMap;
import com.mygdx.safe.SAnimation;
import com.mygdx.safe.SoundMusicMation;
import com.mygdx.safe.nCache;
import com.mygdx.safe.sCache;
import com.mygdx.safe.screens.MainGameScreen;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Created by Boris.InspiratGames on 8/06/17. on 8/06/17.
 */

public  class GraphicsComponent implements Component {

    //TAG
    private static final String TAG = GraphicsComponent.class.getSimpleName();



    //ASPECTS
    private GenericMethodsInputProcessor g;

    //GAMEENTITY
    private GameEntity ge;

    //STATE & DIRECTION & FLIP_X
    private boolean _state;
    private boolean _direction;
    private boolean _flipx=false;

    //SANIMATION
    private sCache _sCache;
    private SAnimation _sanimation; //SANIMATION PARAMETERS
    private com.mygdx.safe.SAnimationMap.SAnimationProgram SProgram=null; //SPINE CONFIG FOR SPINE PROGRAMS

    //NANIMATIONS
    private HashMap<String, nCache> _nCaches;
    private HashMap<String, GCnAnimation> _GCnAnim;

    // LOOP DURATION CONTROLLERS
    float loopDeltaAcumulator;
    float loopDuration;
    int loopCounter;
    int loopLimit;

    //ANIMATION PARAMETERS
    private final float SINCRONIFYFACTOR=1.1f; // THIS FACTOR RELATES TIMESCALE MOVEMENT WITH VELOCITY OF FLOOR MOVEMENT
    private float countStateTime;

    //OTHER
    private String _lastSkin;
    private String _lastAnimationName;

    /*_______________________________________________________________________________________________________________*/

    //STATIC METHODS
    static public Sprite selectFrame(Animation animation, float countStateTime) { // SELECT KEYFRAME BY TIME

        Sprite sprite = new Sprite();
        sprite.setRegion((TextureRegion) animation.getKeyFrame(countStateTime));
        sprite.setSize(1.0f, 1.0f);
        return sprite;

    }

    static public Sprite selectFrame(Animation animation, int numFrame) { // SELECT SPECIFIC KEYFRAME

        Sprite sprite = new Sprite();
        float frameduration=animation.getFrameDuration();
        sprite.setRegion((TextureRegion) animation.getKeyFrame(numFrame*frameduration));
        sprite.setSize(1.0f, 1.0f);
        return sprite;
    }

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public GraphicsComponent(GenericMethodsInputProcessor g, GameEntity ge) {
        this.g=g;
        this.ge = ge;

        _nCaches = new HashMap<String, nCache>();
        _GCnAnim = new HashMap<String, GCnAnimation>();
    }

    //CONFIG
    public void config(EntityConfig e, PhysicsComponent pc, sCache sCache, HashMap<String,nCache> nCache) {

      if(e.is_isSpine()){

           _sCache=new sCache(e,g);
           _sanimation=_sCache.getsAnimation();
           //_sCache = sCache;
           //_sanimation = _sCache.getsAnimation();
            _sanimation.set_sanimationName(e.get_entityID());

            _sanimation.set_skeletonScale(e.get_sanimationConfig().skeletonScale);
            _sanimation.set_skeleton(sCache.getSkeleton());

            _sanimation.set_animationStateData(sCache.getAnimationStateData());
            //ADD MIXES
            _sanimation.createAnimationState();


            // TIMESCALE AND POSITION AND SKIN AND ANIMATIONNAME SELECTS ON RENDER

            _sanimation.setSanimationTimescale(_sCache.getAnimationMap().getEntityConfigTimescale(e.get_sanimationConfig().statetimescale,e.get_state()));
            _sanimation.setTimeScale();

            if (pc.get_states().contains(pc.get_lastState(),false)) {
                if (pc.get_directions().contains(pc.get_lastDirection(),false)) {
                    SProgram = _sCache.getAnimationMap().getSProgram(pc.get_lastState(), pc.get_lastDirection(), false);
                /*
                jsonexport<SAnimationProgram> j1;
                j1 =new jsonexport <SAnimationProgram>();
                j1.set(SProgram);
                j1.export();
                */
                }
                // PROGRAM SPINE WITH SPROGRAM PARAMETERS
                // SETTING ANIMATION BEFORE THE FIRST RENDER

                _sanimation.setAnimation(0, SProgram._sanimationName,true);
                _lastAnimationName=SProgram._sanimationName; //PROGRAM LASTANIMATIONAME
                //SET SKIN BEFORE THE FIRST RENDER
                _sanimation.get_skeleton().setSkin(SProgram._skin);


            }
            _sanimation.set_load(true); // SET SANIMATION LOAD TRUE (FINISHING LOAD)
        }


        //Initializate the configuration for the entities that have Nanimation
        if(e.is_hasNAnimation()) {

            int i = 0;
            Iterator<String> itr = e.get_nAnimationTriConfigs().keySet().iterator();

            while (itr.hasNext()){

                String key = itr.next();

                _nCaches.put(key, nCache.get(key));
                _GCnAnim.put(key, new GCnAnimation());

                if (pc.get_states().contains(pc.get_lastState(), false)) {
                    if (pc.get_directions().contains(pc.get_lastDirection(), false)) {

                        NanimationMap.NanimationProgram NProgram = _nCaches.get(key).get_nAnimation().getNProgram(pc.get_lastState(), pc.get_lastDirection(), false);


                        // SETTING NANIMATION BEFORE THE FIRST RENDER
                        set_currentnanimation(NProgram.get_nanimation(), _nCaches.get(key), key);
                        _GCnAnim.get(key).set_sprite(selectFrame(NProgram.get_nanimation(), countStateTime));

                    }
                }
                i++;
            }
        }
    }

    public void updatingAnimation(float delta, boolean isSpine, boolean hasNanimation){
        PhysicsComponent pc = ge.getPhysicsComponent();

        if (countStateTime > 1) countStateTime = 0; else countStateTime += delta;
        if(ge.getAiComponent().isGraphControllerAnim()){

            if (loopDeltaAcumulator > loopDuration) {
                loopDeltaAcumulator = 0;
                loopCounter++;

            } else {
                loopDeltaAcumulator += delta;
            }


            if(loopLimit == -1){

                loopCounter=0;
                loopDeltaAcumulator=0;
                ge.getAiComponent().setGraphControllerAnim(false);
                g.sendIntructionOK("CHANGE_STATE_DIRECTION", ge.getPendingOKinstructions(), ge);

            }else if(loopCounter>=loopLimit){


                loopCounter=0;
                loopLimit=0;
                loopDeltaAcumulator=0;
                ge.getAiComponent().setGraphControllerAnim(false);
                g.sendIntructionOK("CHANGE_STATE_DIRECTION", ge.getPendingOKinstructions(), ge);

                // return OK && CHANGES GRAPHCONTROLLERANIM TO FALSE
            }

        }





        if(_state || _direction){

            if(isSpine){

                SProgram = _sCache.getAnimationMap().getSProgram(pc.get_currentState(), pc.get_currentDirection(), false);

                if(_state) {
                    pc.set_velocity(SINCRONIFYFACTOR * SProgram._timescale * SProgram._velocity,
                                    SINCRONIFYFACTOR * SProgram._timescale * SProgram._velocity);
                }



                //SETTING SPROGRAM SKIN
                //CUSTOM SKIN SETTINGS ARE POSIBLE (CHANGES AT SANIMATIONS)
                if(!_sanimation.hasCustomSkin()) {
                    _sanimation.get_skeleton().setSkin(SProgram._skin);
                }else{
                    g.println(TAG+ "  SETTING CUSTOM-SKIN " + _sanimation.get_customSkin());
                    _sanimation.get_skeleton().setSkin(_sanimation.get_customSkin());
                }

                //SETTING SPROGRAM TIMESCALE
                _sanimation.setSanimationTimescale(SProgram._timescale);
                //_sanimation.setSanimationTimescale(_sanimation.get_skeleton().getData().findAnimation(SProgram._sanimationName).getDuration());
                _sanimation.setTimeScale();

                _sanimation.setAnimation(0, SProgram._sanimationName,true);
                _lastAnimationName=SProgram._sanimationName;


                loopDuration=_sanimation.get_skeleton().getData().findAnimation(SProgram._sanimationName).getDuration()/  _sanimation.getSanimationTimescale();

                //if(ge.getID().contains("ROBOTHIEF")) g.printlns(TAG + "  *************************** DURATION: " + _sanimation.get_skeleton().getData().findAnimation(SProgram._sanimationName).getDuration() +
                //        "     "+  _sanimation.getSanimationTimescale() + "    " + loopDuration);
                //if(ge.getInputComponent().getI()== InputComponent.InputMethod.ALL_INPUTS) g.println(TAG+" PLAYER SETTED SPROGRAM SANIMATION NAME: "+ SProgram._sanimationName + " LOOP DURATION: "+loopDuration);


            }

            if(hasNanimation){

                NanimationMap.NanimationProgram NProgram = null;
                Iterator<String> itr = _nCaches.keySet().iterator();
                String key = "";

                while (itr.hasNext()){
                    key = itr.next();

                    NProgram = _nCaches.get(key).get_nAnimation().getNProgram(pc.get_currentState(), pc.get_currentDirection(), false);

                    set_currentnanimation(NProgram.get_nanimation(), _nCaches.get(key), key);
                    _GCnAnim.get(key).set_sprite(selectFrame(NProgram.get_nanimation(), countStateTime));

                }

                if(_state) {
                    if(!isSpine) pc.set_velocity(SINCRONIFYFACTOR * NProgram._timescale * NProgram._velocity,
                            SINCRONIFYFACTOR * NProgram._timescale * NProgram._velocity);

                    //countStateTime = 0;
                }

                _lastAnimationName=NProgram._nanimationName;
            }

            if (pc.get_currentDirection().contains("LEFT"))_flipx = true;
            if (pc.get_currentDirection().contains("RIGHT"))      _flipx = false;

            if(ge.getEntityConfig().isInverseFlip() &&
                    (pc.get_currentDirection().contains("LEFT") || pc.get_currentDirection().contains("RIGHT")))
                _flipx = !_flipx;


            if(g.m.lvlMgr.get_currentLvl().getSelectedGE() != null && !g.getMessageAccessClass().lvlMgr.getPlayer().getPhysicsComponent().isForcingMove()){

                if(ge.getInputComponent().getI()== InputComponent.InputMethod.ALL_INPUTS){

                    _flipx = ge.getpositionx() >= g.m.lvlMgr.get_currentLvl().getSelectedGE().getpositionx();
                }
                else if (ge.getID().contains(g.m.lvlMgr.get_currentLvl().getSelectedGE().getID())){

                    _flipx = ge.getpositionx() < g.m.lvlMgr.get_currentLvl().getSelectedGE().getpositionx();
                }
            }

            //if(ge.getID().equalsIgnoreCase("PLAYER")) g.println(TAG + "**************************  "+ _state + "    " + pc.get_currentState());

            if(_state && pc.get_currentState().equalsIgnoreCase("IDLE") && !_sanimation.hasCustomSkin()) {
                g.sendIntructionOK("NOTIFY#WHEN_JUST_IDLE", ge.getPendingOKinstructions(), ge);
            }

            _state=false; _direction=false;

        }


        if (ge.isSpine()){
            _sanimation.setSkeletonFlipX(_flipx);
            _sanimation.get_skeleton().setFlipX(_sanimation.getSkeletonFlipX());
            _sanimation.UpdateSAnimation(delta);
        }

        if(ge.isHasNanimation()){

            Iterator<String> itr = _nCaches.keySet().iterator();
            String key = "";

            while (itr.hasNext()){
                key = itr.next();

                _GCnAnim.get(key).set_sprite(selectFrame(_GCnAnim.get(key).get_currentAnimation(), countStateTime));
                _GCnAnim.get(key).get_sprite().flip(_flipx, false);
            }
        }


    }

    /*_______________________________________________________________________________________________________________*/

    //Getters


    public int getLoopLimit() {
        return loopLimit;
    }

    public float getLoopDuration() {
        return loopDuration;
    }

    public int getLoopCounter() {
        return loopCounter;
    }

    public static String getTAG() {
        return TAG;
    }

    public GenericMethodsInputProcessor getG() {
        return g;
    }

    public boolean is_state() {
        return _state;
    }

    public boolean is_direction() {
        return _direction;
    }

    public boolean is_flipx() {
        return _flipx;
    }

    public sCache get_sCache() {
        return _sCache;
    }

    public SAnimation get_Sanimation() {
        return _sanimation;
    }

    public com.mygdx.safe.SAnimationMap.SAnimationProgram getSProgram() {
        return SProgram;
    }

    public HashMap<String, nCache> get_nCaches() {
        return _nCaches;
    }

    public HashMap<String, GCnAnimation> get_GCnAnim() {
        return _GCnAnim;
    }

    public float getSINCRONIFYFACTOR() {
        return SINCRONIFYFACTOR;
    }

    public float getCountStateTime() {
        return countStateTime;
    }

    public String get_lastSkin() {
        return _lastSkin;
    }

    public String get_lastAnimationName() {
        return _lastAnimationName;
    }

    public GameEntity getGe() {
        return ge;
    }

    //Setters

    public void set_currentnanimation(Animation _currentnanimation, nCache nCache, String key) {

        _GCnAnim.get(key).set_currentAnimation(_currentnanimation);
        _GCnAnim.get(key).set_totalFrames(_currentnanimation.getKeyFrames().length);

        _GCnAnim.get(key).set_frameDuration(1/_GCnAnim.get(key).get_totalFrames());
        _GCnAnim.get(key).get_currentAnimation().setPlayMode(Animation.PlayMode.LOOP);

    }

    public void setVelocity(com.mygdx.safe.SAnimationMap.SAnimationProgram SProgram, com.mygdx.safe.Components.PhysicsComponent pc){
        pc.set_velocity(SINCRONIFYFACTOR * SProgram._timescale * SProgram._velocity, SINCRONIFYFACTOR * SProgram._timescale * SProgram._velocity);
    }

    public void setLoopCounter(int loopCounter) {
        this.loopCounter = loopCounter;
    }

    public void setLoopLimit(int loopLimit) {
        this.loopLimit = loopLimit;
    }

    public void setLoopDuration(float loopDuration) {
        this.loopDuration = loopDuration;
    }

    public void set_state(boolean _state) {
        this._state = _state;
    }

    public void set_direction(boolean _direction) {
        this._direction = _direction;
    }

    public void set_flipx(boolean _flipx) {
        this._flipx = _flipx;
    }

    public void set_sCache(sCache _sCache) {
        this._sCache = _sCache;
    }

    public void set_sanimation(SAnimation _sanimation) {
        this._sanimation = _sanimation;
    }

    public void setSProgram(com.mygdx.safe.SAnimationMap.SAnimationProgram SProgram) {
        this.SProgram = SProgram;
    }

    public void set_nCaches(HashMap<String, nCache> _nCaches) {
        this._nCaches = _nCaches;
    }

    public void set_GCnAnim(HashMap<String, GCnAnimation> _GCnAnim) {
        this._GCnAnim = _GCnAnim;
    }

    public void setCountStateTime(float countStateTime) {
        this.countStateTime = countStateTime;
    }

    public void set_lastSkin(String _lastSkin) {
        this._lastSkin = _lastSkin;
    }

    public void set_lastAnimationName(String _lastAnimationName) {
        this._lastAnimationName = _lastAnimationName;
    }

    public void setGe(GameEntity ge) {
        this.ge = ge;
    }
}

/*
            jsonexport <HashMap<String, NanimationSel>> j1;
            j1 =new jsonexport <HashMap<String, NanimationSel>>();
            j1.set(_nAnimationMap);
            j1.export();
            */

                        /*jsonexport<GCnAnimation> j1;
                        j1 =new jsonexport <GCnAnimation>();
                        j1.set(get_GCnAnim());
                        j1.export();
*/


