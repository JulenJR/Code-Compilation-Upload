package com.mygdx.safe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.mygdx.safe.Entities.ParticleEntity;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;

import java.util.HashMap;

/**
 * Created by practicas on 15/12/17.
 */

public class ParticleFactory {

    private static String PARTICLE_POSITIONS = "scripts/particles.json";

    ParticleFactory (){

    }

    static public void load (HashMap<String, ParticlePositions.Data> positions, GenericMethodsInputProcessor g){
        ParticlePositions particlepositions = ParticleEntity.getParticles(PARTICLE_POSITIONS);
        g.m.lvlMgr.set_particlepositions(particlepositions.getPositions());
    }

    static public ParticlePositions getParticlePositions(){
        Json json = new Json();
        return json.fromJson(ParticlePositions.class, Gdx.files.internal(PARTICLE_POSITIONS));
    }
}