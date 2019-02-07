package com.mygdx.safe;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

    /**
     * Created by alumne_practiques on 27/09/17.
     */

public class NanimationMap {

    com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g;

    private HashMap<String, NanimationSel> _NMap;
    private NanimationProgram NProgram=null;
    private HashMap<String, Animation> _animationsList;

    public NanimationMap(com.mygdx.safe.EntityConfig.nAnimationTriConfig nAnimConfig, com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g){
        this.g = g;

        this._NMap = new HashMap<String, NanimationSel>();

        createNAnimationMap(nAnimConfig, new TextureAtlas(nAnimConfig.get_nAnimationConfig().getAtlasPath()));
    }

    void createNAnimationMap(com.mygdx.safe.EntityConfig.nAnimationTriConfig config, TextureAtlas textureAtlas){

        String lastState = "nothing";

        float _timescale=0;
        float _velocity=0;

        Array<com.mygdx.safe.EntityConfig.StateTimeScale> ntarray = config.get_nAnimationConfig().statetimescale;

        for(com.mygdx.safe.EntityConfig.nAnimationRel aRel: config.get_nAnimationRel()){

            Array<TextureAtlas.AtlasRegion> listFrames = new Array<TextureAtlas.AtlasRegion>();
            int index = 1;

            _timescale = getEntityConfigTimescale(ntarray, aRel._state);
            _velocity = getEntityConfigVelocity(ntarray, aRel._state);

            while (textureAtlas.findRegion(aRel._nAnimationType, index) != null){

                listFrames.add(textureAtlas.findRegion(aRel._nAnimationType, index));
                index++;
            }

            if(!aRel._state.equals(lastState)){
                _NMap.put(aRel._state, new NanimationSel());
                //_nAnimationMap.get(aRel._state)._stateHashvalue
                _NMap.get(aRel._state)._nanimationsel.put(aRel._direction, new NanimationProgram(aRel._nAnimationType,
                        new Animation(1.0f/listFrames.size, listFrames), aRel._flipX, _timescale, _velocity));
                lastState = aRel._state;
            }
            else{
                //_nAnimationMap.get(aRel._state)._stateHashvalue = pc._statesHash.get(aRel._state);
                _NMap.get(aRel._state)._nanimationsel.put(aRel._direction, new NanimationProgram(aRel._nAnimationType,
                        new Animation(1.0f/listFrames.size, listFrames), aRel._flipX, _timescale, _velocity));//STORES HASH OF DIRECTION
            }
        }
    }


    public float getEntityConfigTimescale(Array<com.mygdx.safe.EntityConfig.StateTimeScale> starray, String state){

        // return the timescale for each state
        float t=0;
        for(com.mygdx.safe.EntityConfig.StateTimeScale st: starray){
            if(st._state.contains(state))
                t=st._timescale;
        }
        return t;
    }

    public float getEntityConfigVelocity(Array<com.mygdx.safe.EntityConfig.StateTimeScale> starray, String state){

        //return the velociti for each state
        float v=0;
        for(com.mygdx.safe.EntityConfig.StateTimeScale st: starray){
            if(st._state.contains(state))
                v=st._velocity;
        }
        return v;
    }

/**/

    public NanimationProgram getNProgram(String _state, String _direction, boolean random){
        NanimationProgram n;

        if(_state.contains("IDLE") && !(_direction.equals("DOWN"))) {
            _direction = "DOWN";
            g.println(" CORRECTION!!! STATE=" + _state + " DIRECTION=" + _direction);

        }
        n = _NMap.get(_state).get_nanimationsel().get(_direction);

        return n; // Returns the direction depending his state
    }

    static public class NanimationSel{
        private HashMap<String,NanimationProgram> _nanimationsel;
        private Integer _stateHashvalue;

        public NanimationSel(){
            _nanimationsel=new HashMap<String, NanimationProgram> ();
        }

        public HashMap<String,NanimationProgram> get_nanimationsel() {    return _nanimationsel; }
    }

    static public class NanimationProgram{

        public String _nanimationName;
        Animation _nanimation;
        public boolean _flipx;
        public float _timescale;
        public float _velocity;

        public NanimationProgram(String _n,Animation _na,boolean _fx,float _te,float _ve){
            this._nanimationName=_n;
            this._nanimation=_na;
            this._flipx=_fx;
            this._timescale=_te;
            this._velocity=_ve;
        }

        public String get_nanimationName() {
            return _nanimationName;
        }

        public Animation get_nanimation() {
            return _nanimation;
        }

        public boolean is_flipx() {
            return _flipx;
        }

        public float get_timescale() {
            return _timescale;
        }

        public float get_velocity() {
            return _velocity;
        }
    }


}
