package com.mygdx.safe;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;

import java.util.HashMap;

/**
 * Created by alumne_practiques on 02/11/17.
 */

public class SAnimationMap {

    //TAG
    private static final String TAG = SAnimationMap.class.getSimpleName();

    private GenericMethodsInputProcessor g;
    private HashMap<String, SAnimationSel> _SMap;
    private SAnimationProgram SProgram=null;

    private HashMap<String, Animation> _animationsList;


    public SAnimationMap(com.mygdx.safe.EntityConfig e, GenericMethodsInputProcessor g){
        this.g = g;

        this._SMap = new HashMap<String, SAnimationSel>();

        createSAnimationMap(e.get_sanimationConfig().statetimescale,e.get_sAnimationRel()); // MAP OF SPINE CONFIGS FOR SPINE PROGRAMS SPROGRAM
    }

    void createSAnimationMap(Array<com.mygdx.safe.EntityConfig.StateTimeScale> starray, Array<com.mygdx.safe.EntityConfig.sAnimationRel> arraysanimations) {
        String lastState = "nothing"; // LAST STATE FOR CONFIGS;
        float _timescale=0;
        float _velocity=0;

        for (com.mygdx.safe.EntityConfig.sAnimationRel s : arraysanimations) {
            _timescale=getEntityConfigTimescale(starray,s._state);
            _velocity=getEntityConfigVelocity(starray,s._state);


            if (!s._state.equals(lastState)) {
                _SMap.put(s._state,new SAnimationSel());

                _SMap.get(s._state)._sanimationsel.put(s._direction,new SAnimationProgram(s._sAnimationType,s._skin,s._flipX,_timescale,_velocity)); //STORES HASH OF DIRECTION
                lastState = s._state;
            }else{
                _SMap.get(s._state)._sanimationsel.put(s._direction,new SAnimationProgram(s._sAnimationType,s._skin,s._flipX, _timescale,_velocity)); //STORES HASH OF DIRECTION
            }
        }
    }

    public float getEntityConfigTimescale(Array<com.mygdx.safe.EntityConfig.StateTimeScale> starray, String state){

        // return the timescale for each state
        float t=0;
        for(com.mygdx.safe.EntityConfig.StateTimeScale st: starray){
            if(st._state.equalsIgnoreCase(state))
                t=st._timescale;
        }
        return t;
    }

    public float getEntityConfigVelocity(Array<com.mygdx.safe.EntityConfig.StateTimeScale> starray, String state){

        //return the velociti for each state
        float v=0;
        for(com.mygdx.safe.EntityConfig.StateTimeScale st: starray){
            if(st._state.equalsIgnoreCase(state))
                v=st._velocity;
        }
        return v;
    }

    public SAnimationProgram getSProgram(String _state, String _direction, boolean random) {
        SAnimationProgram s;

        if(_SMap.get(_state).get_sanimationsel().get(_direction)==null) {
            _direction="DOWN";
            g.println(" CORRECTION!!! STATE="+_state+" DIRECTION="+_direction);
        }
        s=_SMap.get(_state).get_sanimationsel().get(_direction);


        return s;  // Returns the direction depending his state

    }

    static public class SAnimationSel{

        private HashMap<String,SAnimationProgram> _sanimationsel;
        private Integer _stateHashvalue;


        public SAnimationSel(){
            _sanimationsel=new HashMap<String, SAnimationProgram> ();
        }

        public HashMap<String, SAnimationProgram> get_sanimationsel() {    return _sanimationsel; }

    }

    //INNER CLASSES

    static public class SAnimationProgram {

        public String _sanimationName;
        public String _skin;
        public boolean _flipx;
        public float _timescale;
        public float _velocity;

        public SAnimationProgram(String _s,String _sk,boolean _f,float _t,float _v){
            this._sanimationName=_s;
            this._skin=_sk;
            this._flipx=_f;
            this._timescale=_t;
            this._velocity=_v;

        }

    }




}
