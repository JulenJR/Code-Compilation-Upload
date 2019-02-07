package com.mygdx.safe.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.mygdx.safe.Components.ParticleComponent;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;
import com.mygdx.safe.ParticlePositions;
import com.mygdx.safe.sPritemation;

/**
 * Created by practicas on 12/12/17.
 */

public class ParticleEntity extends ParticleEffect {

    private GenericMethodsInputProcessor g;

    private ParticleComponent pComp;
    private ParticlePositions pPos;

    public float positionx= 0;
    public float positiony= 0;

    public float toPositionx=0;
    public float toPositiony=0;
    public float movetoTime=0;
    public float moveToPositionUnitX=0;
    public float moveToPositionUnitY=0;
    public float acumulatorMoveTo=0;

    public boolean movetoUpdater;
    public boolean isHud=false;

    public ParticleEntity(GenericMethodsInputProcessor g){

        this.g = g;

        pComp = new ParticleComponent();
        pPos = new ParticlePositions();

    }

    static public ParticlePositions getParticles(String path){
        Json json = new Json();
        return json.fromJson(ParticlePositions.class, Gdx.files.internal(path));
    }

    public ParticleComponent getpComp() {
        return pComp;
    }

    public ParticlePositions getpPos() {
        return pPos;
    }


    public Vector2 getPosition(){
        return new Vector2(positionx,positiony);
    }
}
