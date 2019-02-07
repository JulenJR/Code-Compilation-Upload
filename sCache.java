package com.mygdx.safe;

import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;

/**
 * Created by alumne_practiques on 30/06/17.
 */

public class sCache {

    private EntityConfig entityConfig;
    private Skeleton skeleton;
    private AnimationStateData animationStateData;
    private SAnimationMap animationMap;
    private com.mygdx.safe.SAnimation sAnimation;


    public sCache(EntityConfig entityConfig, com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g){
        this.entityConfig = entityConfig;
        entityConfig.g=g;

        //Create a Skeleton and AnimationStateData objects when is a Spine entity
        this.skeleton = new Skeleton(entityConfig.get_sanimationConfig().createSkeletonData(entityConfig));
        this.animationStateData = new AnimationStateData(entityConfig.get_sanimationConfig().createSkeletonData(entityConfig));
        this.animationMap = new SAnimationMap(entityConfig, g);
        this.sAnimation = new com.mygdx.safe.SAnimation(g);
    }

    //Getters

    public EntityConfig getEntityConfig(){
        return entityConfig;
    }

    public Skeleton getSkeleton(){
        return  skeleton;
    }

    public AnimationStateData getAnimationStateData() { return animationStateData;  }

    public SAnimationMap getAnimationMap() {
        return animationMap;
    }

    public com.mygdx.safe.SAnimation getsAnimation() {
        return sAnimation;
    }

    //Setters

    public void setEntityConfig(EntityConfig newEntityConfig){
        entityConfig = newEntityConfig;
    }


    public void setSkeleton(Skeleton newSkeleton){  skeleton = newSkeleton; }

    public void setAnimationStateData(AnimationStateData newAnimationStateData) {   this.animationStateData = newAnimationStateData;    }

    public void setAnimationMap(SAnimationMap animationMap) {
        this.animationMap = animationMap;
    }

    public void setsAnimation(com.mygdx.safe.SAnimation sAnimation) {
        this.sAnimation = sAnimation;
    }
}
