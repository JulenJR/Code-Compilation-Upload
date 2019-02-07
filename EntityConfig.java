
package com.mygdx.safe;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.esotericsoftware.spine.SkeletonBinary;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;

import java.util.HashMap;

/**
 * Created by BORIS.INSPIRATGAMES on 08/06/17.
 */

public class EntityConfig {

    private String _entityID;
    private Rectangle _circunscription; // RECTANGLE AREA WITH TILED COORDINATES
    private String _state;
    private String _direction;
    private Array<String> _directions;
    private Array<String> _states;
    private Array<Transform> _transforms;
    private boolean _enabled;
    private boolean _specialEntity;

    //Circunscription
    private String _currentCircuns;

    //Proximity Area
    private float _proximityRadius = 1.0f;

    //Spine entity
    private boolean _isSpine;
    private Array <sAnimationRel> _sAnimationRel;
    private sAnimationConfig _sanimationConfig;

    //Nanimation
    private boolean _hasNAnimation;
    private HashMap<String, nAnimationTriConfig> _nAnimationTriConfigs;
    public com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g;

    //EntityItem
    private String _itemType;
    private String _itemShortDescription;
    private int _value;
    private int _quantity;

    //inverse flip
    private boolean inverseFlip = false;


    EntityConfig(){

        _sAnimationRel=new Array <sAnimationRel> ();
        _sanimationConfig=new sAnimationConfig();

        _nAnimationTriConfigs = new HashMap<String, nAnimationTriConfig>();

    }

    //Getters

    public Rectangle get_circunscription() {    return _circunscription;  }

    public com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor getG() {   return g; }

    public String get_state() {   return _state;  }

    public String get_direction() {  return _direction;  }

    public String get_entityID() {   return _entityID;   }

    public Array<String> get_directions() {    return _directions;   }

    public Array<String> get_states() {    return _states;   }

    public Array<Transform> get_transforms() {      return _transforms;  }

    public String get_currentCircuns() {
        return _currentCircuns;
    }

    public Array<sAnimationRel> get_sAnimationRel() {    return _sAnimationRel;  }

    public sAnimationConfig get_sanimationConfig() {   return _sanimationConfig;  }

    public boolean is_isSpine() {   return _isSpine;    }

    public boolean is_hasNAnimation() { return _hasNAnimation;  }

    public HashMap<String, nAnimationTriConfig> get_nAnimationTriConfigs() {return _nAnimationTriConfigs;}

    public String getItemType() {
        return _itemType;
    }

    public String getItemShortDescription() {
        return _itemShortDescription;
    }

    public int getValue() {
        return _value;
    }

    public int getQuantity() {
        return _quantity;
    }

    public boolean is_enabled() {
        return _enabled;
    }

    public String get_itemType() {
        return _itemType;
    }

    public String get_itemShortDescription() {
        return _itemShortDescription;
    }

    public int get_value() {
        return _value;
    }

    public int get_quantity() {
        return _quantity;
    }

    public boolean is_specialEntity() {
        return _specialEntity;
    }

    public float get_proximityRadius() {
        return _proximityRadius;
    }

    public boolean isInverseFlip() {
        return inverseFlip;
    }

    //Setters


    public void set_circunscription(Rectangle _circunscription) {   this._circunscription = _circunscription; }

    public void setG(com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g) {    this.g = g;  }

    public void set_state(String _state) {   this._state = _state;  }

    public void set_direction(String _direction) {   this._direction = _direction;  }

    public void set_entityID(String _entityID) {   this._entityID = _entityID;  }

    public void set_states(Array<String> _states) {   this._states = _states; }

    public void set_directions(Array<String> _directions) {  this._directions = _directions;   }

    public void set_transforms(Array<Transform> _transforms) {        this._transforms = _transforms;    }

    public void set_currentCircuns(String _currentCircuns) {
        this._currentCircuns = _currentCircuns;
    }

    public void set_sAnimationRel(Array<sAnimationRel> _sAnimationRel) {  this._sAnimationRel = _sAnimationRel;  }

    public void set_sanimationConfig(sAnimationConfig _sanimationConfig) {   this._sanimationConfig = _sanimationConfig;  }

    public void set_isSpine(boolean _isSpine) { this._isSpine = _isSpine;   }

    public void set_hasNAnimation(boolean _hasNAnimation) { this._hasNAnimation = _hasNAnimation;   }

    public void set_nAnimationTriConfigs(HashMap<String, nAnimationTriConfig> _nAnimationTriConfigs) {this._nAnimationTriConfigs = _nAnimationTriConfigs;}

    public void setItemType(String itemType) {
        this._itemType = itemType;
    }

    public void setItemShortDescription(String itemShortDescription) {
        this._itemShortDescription = itemShortDescription;
    }

    public void setValue(int value) {
        this._value = value;
    }

    public void setQuantity(int quantity) {
        this._quantity = quantity;
    }

    public void set_enabled(boolean _enabled) {
        this._enabled = _enabled;
    }

    public void set_itemType(String _itemType) {
        this._itemType = _itemType;
    }

    public void set_itemShortDescription(String _itemShortDescription) {
        this._itemShortDescription = _itemShortDescription;
    }

    public void set_value(int _value) {
        this._value = _value;
    }

    public void set_quantity(int _quantity) {
        this._quantity = _quantity;
    }

    public void set_specialEntity(boolean _specialEntity) {
        this._specialEntity = _specialEntity;
    }

    public void set_proximityRadius(float _proximityRadius) {
        this._proximityRadius = _proximityRadius;
    }

    public void setInverseFlip(boolean inverseFlip) {
        this.inverseFlip = inverseFlip;
    }

    static public class StateTimeScale{

        public String _state;
        public float _timescale;
        public float _velocity;

        public StateTimeScale(){

        }
    }

   
    
    //Spine

    static public class sAnimationRel{

        public String _state;
        public String _direction;
        public String _sAnimationType; //NAME OF ANIMATION
        public String _skin;
        public boolean _flipX;

        public sAnimationRel(){

        }

    }

    static public class sAnimationConfig { // BY BORIS.INSPIRATGAMES

        public com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g;
        public Rectangle boundingBoxSetter; // CONFIGURE BOUNDINGBOXES: IF VALUES <1 THEN ARE %, IF VALUES >1 VALUES ARE PIXEL
        public String skelPath;
        public String atlasPath;
        public String jsonPath;
        public float skeletonScale;
        public Array<StateTimeScale> statetimescale;

        public SkeletonJson skelJson;
        public SkeletonBinary skelBin;
        public TextureAtlas skelAtlas;
        public SkeletonData skeletondata;

        public sAnimationConfig(){

        }

        public synchronized SkeletonData createSkeletonData(EntityConfig e) {
            //LOAD ATLAS OF SKELETON. ON ERROR, EXIT PROGRAM.
            try {
                if (atlasPath == null) {
                    //e.g.println("Atlas Path not charged");
                    System.exit(0);
                } else {

                    Utility.loadTextureAtlasAsset(atlasPath);
                    skelAtlas = Utility.getTextureAtlasAsset(atlasPath);
                    //e.g.println("Atlas charged");
                }
                // LOAD SKELETON. ON ERROR, TRY THE JSON OPTION.
                if (skelBin != null) {  // BINARY OPTION LOAD
                    skelBin = new SkeletonBinary(skelAtlas);
                    skelBin.setScale(skeletonScale);
                    skeletondata = skelBin.readSkeletonData(Gdx.files.internal(skelPath));
                    //e.g.println("skel SkeletonData charged");
                } else {
                    //e.g.println("Skeleton Path not charged");
                    if (jsonPath == null) {   //JSON OPTION LOAD. ON ERROR, EXIT PROGRAM
                        //e.g.println("SkeletonJson Path not charged");
                        System.exit(0);
                    } else {
                        skelJson = new SkeletonJson(skelAtlas);
                        skelJson.setScale(skeletonScale * Map.UNIT_SCALE);

                        skeletondata = skelJson.readSkeletonData(Gdx.files.internal(jsonPath));
                        //e.g.println("Json SkeletonData charged");
                    }
                }

            } catch (Exception er) {
                e.g.println("Exception = " + er);
            }
            return skeletondata;
        }
    }

    //Nanimation

    static public class nAnimationConfig{

        public String nAnimationName;
        public Rectangle boundingBoxSetter; // CONFIGURE BOUNDINGBOXES: IF VALUES <1 THEN ARE %, IF VALUES >1 VALUES ARE PIXEL

        public String atlasPath;
        public String jsonPath;

        public float nScale;
        public float xScale;
        public float yScale;

        public Array<StateTimeScale> statetimescale;

        public nAnimationConfig(){

        }

        public String getAtlasPath(){
            return atlasPath;
        }
    }

    static public class nAnimationRel{

        public String _state;
        public String _direction;
        public String _nAnimationType; //ANIMATION NAME
        public boolean _flipX;

        public nAnimationRel(){

        }
    }

    //Transform

    static public class Transform{
        // LIVEMOTIONCONFIG
        public String _transform;
        //public int _actualFrameState; // ACTUAL FRAME
        public int _initialFrame;
        public int _finalFrame;
        public int _variation;
        public boolean _loop;

    }

    static public class nAnimationTriConfig {

        public Array<nAnimationRel> _nAnimationRel;
        public nAnimationConfig _nAnimationConfig;
        public Vector2 _masterDistance;

        public nAnimationTriConfig(){

        }

        //Getters
        public Array<nAnimationRel> get_nAnimationRel() {return _nAnimationRel;}

        public nAnimationConfig get_nAnimationConfig() {return _nAnimationConfig;}

        public Vector2 get_masterDistance() {return _masterDistance;}


        //Setters
        public void set_nAnimationConfig(nAnimationConfig _nAnimationConfig) {this._nAnimationConfig = _nAnimationConfig;}

        public void set_masterDistance(Vector2 _masterDistance) {this._masterDistance = _masterDistance;}

        public void set_nAnimationRel(Array<nAnimationRel> _nAnimationRel) {this._nAnimationRel = _nAnimationRel;}

    }

}
