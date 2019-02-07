package com.mygdx.safe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.safe.Entities.ParticleEntity;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;

/**
 * Created by Julen.InspiratGames on 01/05/2018.
 */

public class ParticleMation extends Actor {

    public static String TAG = ParticleMation.class.getSimpleName();

    private String name;
    private ParticleEntity pe;
    private GenericMethodsInputProcessor g;
    private Stage s;
    boolean visibility =true;


    public ParticleMation(GenericMethodsInputProcessor g){
        name = "";
        pe = new ParticleEntity(g);
        this.g = g;


    }

    public void createParticle(String name, String path,float scale, float x, float y){

        setName(name);
        visibility=true;
        //SET PARTICLE EFFECT
        pe.load(Gdx.files.internal(path), Gdx.files.internal(""));
        //SET SCALE FOR PARTICLE SIZE && PARTICLE MOTION
        for (int i = 0; i < pe.getEmitters().size; i++) {
            pe.getEmitters().get(i).scaleMotion(scale);
            pe.getEmitters().get(i).scaleSize(scale);

        }
        //SET PARTICLE POSITION
        pe.setPosition(x, y);
        pe.positionx=x;
        pe.positiony=y;
        g.m.he.getStage().addActor(this);



    }

    private void moveTo(){

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



    public ParticleEntity getPe() {
        return pe;
    }

    @Override
    public void setName(String name) {
        super.setName(name);
    }

    @Override
    public void act(float delta) {
        if(visibility) {
            pe.update(delta);
            if (pe.movetoUpdater) moveTo();
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(visibility) pe.draw(batch);
    }

    @Override
    protected void setStage(Stage stage) {
        stage.addActor(this);
    }

    /**
     * If false, the actor will not be drawn and will not receive touch events. Default is true.
     *
     * @param visible
     */
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        visibility=visible;
    }
}
