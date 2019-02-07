package com.mygdx.safe;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;

/**
 * Created by BORIS.INSPIRATGAMES on 3/05/17.
 */

public class SAnimation {

    protected com.mygdx.safe.Conversation.TextActor textactor;
    protected Vector2 rpTextActor; // RELATIVE POSITION
    protected String _sanimationName;
    protected Skeleton _skeleton;
    protected boolean _load;
    protected AnimationStateData _animationStateData;
    protected AnimationState _animationState;
    protected boolean _skeletonFlipX;


    protected float _skeletonScale;
    protected float _sanimationTimeScale;
    protected String _defaultSanimationType;

    // CUSTOM SKIN
    protected String _customSkin="";
    protected boolean _hasCustomSkin=false;
    protected String _preCustomSkin="";

    protected GenericMethodsInputProcessor g;

    public SAnimation(GenericMethodsInputProcessor g){
        this.g=g;
        set_load(false);
        setSkeletonFlipX(false);
    }

    public String get_customSkin() {
        return _customSkin;
    }

    public void set_customSkin(String _customSkin) {
        this._customSkin = _customSkin;
    }

    public boolean hasCustomSkin() {
        return _hasCustomSkin;
    }

    public void set_hasCustomSkin(boolean _hasCustomSkin) {
        this._hasCustomSkin = _hasCustomSkin;
    }

    public String get_preCustomSkin() {
        return _preCustomSkin;
    }

    public void set_preCustomSkin(String _preCustomSkin) {
        this._preCustomSkin = _preCustomSkin;
    }

    public com.mygdx.safe.Conversation.TextActor getTextactor() {  return textactor; }

    public void setTextactor(com.mygdx.safe.Conversation.TextActor textactor) { this.textactor = textactor; }

    public Vector2 getRpTextActor() {  return rpTextActor;  }

    public void setRpTextActor(Vector2 rpTextActor) {   this.rpTextActor = rpTextActor; }

    public void set_sanimationName(String _sanimationName) {    this._sanimationName = _sanimationName;   }

    public String get_sanimationName() {    return _sanimationName;   }

    public void set_defaultSanimationType(String _sAnimationType) {  this._defaultSanimationType = _sAnimationType;  }

    public String get_defaultSanimationType() {
        return _defaultSanimationType;
    }

    public void setSanimationTimescale(float sanimationtimescale) {   this._sanimationTimeScale = sanimationtimescale;  }

    public float getSanimationTimescale() {  return _sanimationTimeScale;   }

    public void set_load(boolean _load) {
        this._load = _load;
    }

    public boolean is_load() {
        return _load;
    }

    public void set_animationState(AnimationState _animationState) {    this._animationState = _animationState;  }

    public AnimationState get_animationState() {
        return _animationState;
    }

    public void set_skeleton(Skeleton _skeleton) {
        this._skeleton = _skeleton;
    }

    public Skeleton get_skeleton() {
        return _skeleton;
    }


    public void setSkeletonFlipX(boolean skeletonFlipX) {
        _skeletonFlipX = skeletonFlipX;
    }

    public boolean getSkeletonFlipX(){return _skeletonFlipX;}

    public void set_skeletonScale(float _skeletonScale) {
        this._skeletonScale = _skeletonScale;
    }

    public float get_skeletonScale() {
        return _skeletonScale;
    }



    public void createSkeleton(EntityConfig e){
       _skeleton=new Skeleton(e.get_sanimationConfig().createSkeletonData(e));
    }

    public void createAnimationStateData(EntityConfig e){
        _animationStateData=new AnimationStateData(e.get_sanimationConfig().createSkeletonData(e));

    }

    public void set_mixInStateData(String animation1,String animation2,float time){
        if(_animationStateData!=null){
            _animationStateData.setMix(animation1,animation2,time);
        }
    }

    public void set_defaultMixInStateData(float defaultMix){_animationStateData.setDefaultMix(defaultMix);}

    public void createAnimationState(){
        if(_animationStateData!=null){
            _animationState=new AnimationState(_animationStateData);
        }
    }

    public void setTimeScale(){
        if(_animationState!=null){
            _animationState.setTimeScale(_sanimationTimeScale);
        }
    }

    public void setAnimation(int trackIndex,String animationName,boolean loop){
        if(_animationState!=null){
            _animationState.setAnimation(trackIndex,animationName,loop);
        }
    }

    public void addAnimation(int trackIndex,String animationName,boolean loop,float delay){
        if(_animationState!=null){
            _animationState.addAnimation(trackIndex,animationName,loop,delay);
        }
    }



    public void UpdateSAnimation(float delta){


        _animationState.update(delta);
        _animationState.apply(_skeleton);

        _skeleton.updateWorldTransform();

    }

    public void SetAndUpdateFrameAnimation(int trackIndex,String animationName,boolean loop,int frame){
        if(_animationState!=null){
            _animationState.setAnimation(trackIndex,animationName,loop);
        }
        _animationState.update(frame*1/30.0f);
        _animationState.apply(_skeleton);
        _skeleton.updateWorldTransform();
    }


    public void UpdateVariationFrameAnimation(int variation){
        _animationState.update(1/30.0f*variation);
        _animationState.apply(_skeleton);
        _skeleton.updateWorldTransform();
    }


    public void set_animationStateData(AnimationStateData _animationStateData) {
        this._animationStateData = _animationStateData;
    }
}
