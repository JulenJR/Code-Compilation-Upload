package com.mygdx.safe.EntitySystems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.safe.Entities.GameEntity;
import com.mygdx.safe.Entities.ParticleEntity;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;
import com.mygdx.safe.ParticlePositions;

import java.util.HashMap;

/**
 * Created by practicas on 4/01/18.
 */

public class ParticleSystem {

    private static String TAG=ParticleSystem.class.getSimpleName();
    private GenericMethodsInputProcessor g;
    private HashMap<String, ParticlePositions.Data> positions;

    private Array<ParticleEntity> particles;
    private Array<ParticleEffect> entity_dust;
    private HashMap<String, ParticleEntity> particle_condif= new HashMap<String, ParticleEntity>();



    public ParticleSystem(GenericMethodsInputProcessor g){

        this.g = g;

        positions = g.m.lvlMgr.get_particlepositions();
        g.println( TAG + "   PARTICLESYSTEM :" + positions.keySet().toString());

        particles = new Array<ParticleEntity>();

    }

    public void createParticles () {
        String path = "";
        int d = 0;
        for (String s : positions.keySet()){
            if(s.contains(g.m.lvlMgr.get_currentLvl().get_lvlID()))  {
                for (int i = 0; i < positions.get(s).getPosition().size; i++) {

                    particles.add(new ParticleEntity(g));

                    path = positions.get(s).getPath();
                    particles.get(d).load(Gdx.files.internal(path), Gdx.files.internal(""));

                    for (int e = 0; e < particles.get(d).getEmitters().size; e++) {
                        particles.get(d).getEmitters().get(e).scaleMotion(positions.get(s).getScale());
                        particles.get(d).getEmitters().get(e).scaleSize(positions.get(s).getScale());
                    }
                    //g.printlns(d+ "    particle:   " + positions.get(s).getArrayID() + "       " + path + "         "+ positions.get(s).getScale());
                    d++;
                }
            }
        }
    }

    public void createParticlesWithEntities(GameSystem gsys){

        entity_dust = new Array<ParticleEffect>();
        int a = 0;

        for (GameEntity ge : gsys.getEntities()){
            createParticlesToEntity(a, ge);
            a++;
        }
    }

    public void createParticlesToEntity(int a, GameEntity ge){
        entity_dust.add(new ParticleEntity(g));

        entity_dust.get(a).load(Gdx.files.internal("particle/dust"), Gdx.files.internal(""));
        for (int c = 0; c < entity_dust.get(a).getEmitters().size; c++) {

            entity_dust.get(a).getEmitters().get(c).scaleMotion(0.1f);
            entity_dust.get(a).getEmitters().first().scaleSize(0.1f);

        }
        ge.entitydust=entity_dust.get(a);

    }


    public void particleEntityPosition (GameSystem gsys, PolygonSpriteBatch polspritbatch){

    for (GameEntity currentEntity : gsys.getEntities()) {
            float positionx=currentEntity.getpositionx();
            float positiony=currentEntity.getpositiony();

            ParticleEmitter e=currentEntity.entitydust.getEmitters().get(0);
            e.getEmission().setHighMax(20);

        if (!currentEntity.getID().contains("PROTESTERSMASS_00"))
            e.setPosition(positionx - 0.5f, positiony);
        else
            e.setPosition(positionx- 2.5f, positiony);

        if (!currentEntity.getPhysicsComponent().get_currentState().contains("WALK") && !currentEntity.getPhysicsComponent().get_currentState().contains("RUN")) {
                    e.getEmission().setHighMin(0);
                    e.getEmission().setHighMax(0);

            } else {
                    e.getEmission().setHighMin(0);
                    e.getEmission().setHighMax(20);

            }
            currentEntity.entitydust.draw(polspritbatch);
        }
    }


    public void setParticlePositions () {
        int d = 0;
        for (String s : positions.keySet()){
            if(s.contains(g.m.lvlMgr.get_currentLvl().get_lvlID())) {
                for (int i = 0; i < positions.get(s).getPosition().size; i++) {
                    particles.get(d).setPosition(positions.get(s).getPosition().get(i).x, positions.get(s).getPosition().get(i).y);
                    d++;
                }
            }
        }
    }

    public void particleUpdate (float delta) {
        for (ParticleEntity pe : particles)
                pe.update(delta);

        for (ParticleEffect pe : entity_dust)
                pe.update(delta);
    }

    public void particleRender (PolygonSpriteBatch polspritbatch,float delta) {

        for (ParticleEntity pe : particles) {
                    pe.draw(polspritbatch, delta);

                    if (pe.movetoUpdater){
                        g.println("current pos: " + pe.getPosition());
                        moveTo(pe, delta);
                    }
        }
    }



    public void quitParticle(ParticleEntity pe){
        g.printlns("QUITING PARTICLE: ");
        particle_condif.get(pe).dispose();
        //particle_condif.remove(pe);
    }

    public void clearPartiles(){
        particles.clear();
        entity_dust.clear();
    }

    public void createParticle(String name, String path,float scale, float x, float y,boolean isHud){
        ParticleEntity pe = new ParticleEntity(g);
        //SET PARTICLE EFFECT
        particle_condif.put(name,pe);
        pe.load(Gdx.files.internal(path), Gdx.files.internal(""));
        //SET SCALE FOR PARTICLE SIZE && PARTICLE MOTION
        pe.isHud=isHud;
        for (int i = 0; i < pe.getEmitters().size; i++) {
            pe.getEmitters().get(i).scaleMotion(scale);
            pe.getEmitters().get(i).scaleSize(scale);

        }
        //SET PARTICLE POSITION
        pe.setPosition(x, y);
        pe.positionx=x;
        pe.positiony=y;
        //ADD PARTICLE TO ARRAY TO UPDATE&DRAW
        if(isHud) particles.add(pe);
    }

    public void setParticlePosition (String name, float x, float y){
        particle_condif.get(name).setPosition(x,y);
        particle_condif.get(name).positionx=x;
        particle_condif.get(name).positiony=y;
    }

    public void set_duration(String name, int duration){
        particle_condif.get(name).setDuration(duration);
    }

    public void moveToReceive(String name, float x, float y,float duration){
        ParticleEntity pe=particle_condif.get(name);
        pe.toPositionx=x;
        pe.toPositiony=y;
        pe.movetoUpdater=true;
        pe.movetoTime=duration;
        pe.moveToPositionUnitX=( x - pe.positionx ) / ( duration*60 );
        pe.moveToPositionUnitY=( y - pe.positiony ) / ( duration*60 );

        g.println(TAG + " MOVETORECEIVE: duration" + duration + " MOVETOPOSITIONUNITX= "+pe.moveToPositionUnitX+ " MOVETOPOSITIONUNITY= "+pe.moveToPositionUnitY
                   + " X= " + x + " Y= " + y + "POSITION="+ pe.getPosition());

    }

    public ParticleEntity getParticle(String pe){

        return particle_condif.get(pe);
    }
    private void moveTo(ParticleEntity pe,float delta){

        if(Math.abs(pe.toPositionx-pe.positionx)<0.015 && Math.abs(pe.toPositiony-pe.positiony)<0.015 ){
            pe.movetoUpdater=false;
            pe.toPositionx=0;
            pe.toPositiony=0;
            pe.movetoTime=0;
            pe.moveToPositionUnitX=0;
            pe.moveToPositionUnitY=0;
            pe.acumulatorMoveTo=0;

        }else {
            pe.acumulatorMoveTo+=1/60f;
            pe.setPosition(pe.positionx + pe.moveToPositionUnitX,
                           pe.positiony + pe.moveToPositionUnitY);
            pe.positionx+=pe.moveToPositionUnitX;
            pe.positiony+=pe.moveToPositionUnitY;
        }

        g.printlns(TAG + "  positionParticle: " + pe.getPosition() + "       ACUMULATOR " + pe.acumulatorMoveTo);




    }
}
