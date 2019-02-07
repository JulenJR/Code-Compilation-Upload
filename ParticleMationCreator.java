package com.mygdx.safe;

import com.badlogic.gdx.Gdx;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;

import java.util.HashMap;

/**
 * Created by Julen.InspiratGames on 01/05/2018.
 */

public class ParticleMationCreator {

    public static String TAG = ParticleMationCreator.class.getSimpleName();

    private GenericMethodsInputProcessor g;
    private HashMap<String, ParticleMation> pmHash;



    public ParticleMationCreator(GenericMethodsInputProcessor g){

        this.g = g;
        pmHash = new HashMap<String, ParticleMation>();

    }

    public void createParticle(String name, String path,float scale, float x, float y){

        ParticleMation pm = new ParticleMation(g);

        pm.setName(name);
        //SET PARTICLE EFFECT
        pm.getPe().load(Gdx.files.internal(path), Gdx.files.internal(""));
        //SET SCALE FOR PARTICLE SIZE && PARTICLE MOTION
        for (int i = 0; i < pm.getPe().getEmitters().size; i++) {
            pm.getPe().getEmitters().get(i).scaleMotion(scale);
            pm.getPe().getEmitters().get(i).scaleSize(scale);

        }

        pmHash.put(name,pm);

        //SET PARTICLE POSITION
        pm.getPe().setPosition(x, y);
        pm.getPe().positionx=x;
        pm.getPe().positiony=y;

        g.m.he.getStage().addActor(pm);
    }

    public void moveToReceive(String name, float x, float y,float duration){
        pmHash.get(name).getPe().toPositionx=x;
        pmHash.get(name).getPe().toPositiony=y;
        pmHash.get(name).getPe().movetoUpdater=true;
        pmHash.get(name).getPe().movetoTime=duration;
        pmHash.get(name).getPe().moveToPositionUnitX=( x -  pmHash.get(name).getPe().positionx ) / ( duration*60 );
        pmHash.get(name).getPe().moveToPositionUnitY=( y -  pmHash.get(name).getPe().positiony ) / ( duration*60 );

        g.printlns(TAG + " MOVETORECEIVE: duration" + duration + " MOVETOPOSITIONUNITX= "+ pmHash.get(name).getPe().moveToPositionUnitX+ " MOVETOPOSITIONUNITY= "+
                   pmHash.get(name).getPe().moveToPositionUnitY + " X= " + x + " Y= " + y + "POSITION="+  pmHash.get(name).getPe().getPosition());

    }


    public HashMap<String, ParticleMation> getPmHash() {
        return pmHash;
    }

    public ParticleMation getParticle(String withname){
        return pmHash.get(withname);
    }

    public void setVisible(String withname,boolean visible){
        ParticleMation pm=pmHash.get(withname);
        g.println( TAG + "   PARTICLEMATION: " + withname + "  VISIBLE CHANGE:" + visible + "   ZINDEX=" + pm.getZIndex());
        g.println( TAG + "   ALL PM  : " + pmHash.keySet().toString());
        g.m.he.getS().getActors().get(pm.getZIndex()).setVisible(visible);
        pm.visibility =visible;
    }
}
