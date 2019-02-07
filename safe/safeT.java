package com.mygdx.safe.safe;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

/**
 * Created by practica1 on 21/06/17.
 */

public class safeT extends Group{

    //TAG
    private static final String TAG = safeT.class.getSimpleName();

    //ASPECTS
    private com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g;

    //IMAGES
    private HashMap<String, Image> images = new HashMap<String, Image>();

    //STAGE
    private Stage s;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public safeT(com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g ){
        this.g = g;
        images=new HashMap<String, Image> ();
    }

    /*_______________________________________________________________________________________________________________*/

    //CHECK STATE
    /*public void checkState(){

        String visible = "";
        Image image1;

        if(g.m.he.getEMO() >= 101) visible = "TRICEPTION_GAS";
        else if (g.m.he.getEMO()< 101 && g.m.he.getEMO() >= 0) visible = "TRICEPTION_LIQUID";
        else visible = "TRICEPTION_SOLID";

        for(String s: images.keySet()){
            if(s.equalsIgnoreCase("TRICEPTION_COLORS") || s.equalsIgnoreCase("LIVEMOTION_01") || s.equalsIgnoreCase(visible)){
                images.get(s).setVisible(true);
                image1=images.get(s);
                g.m.he.triImagetRotationRapìdAndNormal(g.m.he.getHudActorDataComponent().IMG_TRICEPTION_ENGRANAJE_PEQUEÑO,
                        g.m.he.getHudActorDataComponent().IMG_TRICEPTION_ENGRANAJE, image1,6,1);
            }
            else{
                images.get(s).setVisible(false);
            }
        }
    }
    */

    //ADD TRICEPTION LISTENERS
    /*public void addTriceptionListeners(final com.mygdx.safe.Entities.HUD_Entity he){

        for(final Image i: images.values()){
            i.addListener(new InputListener(){

                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    //g.println("***********************COLORSCOLORS*******************************");
                    he.HandleeEventToHudBooleanData(i, event, x, y, pointer, button);
                    he.getHudActorDataComponent().triception_select = true;
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    he.HandleeEventToHudBooleanData(i, event, x, y, pointer, button);
                    he.getHudActorDataComponent().triception_select = false;
                }

                @Override
                public void touchDragged(InputEvent event, float x, float y, int pointer) {
                    he.HandleeEventToHudBooleanData(i, event, x, y, pointer, 1);
                    he.getHudActorDataComponent().triception_select = true;
                }
            });
        }
    }

    //ADD TRICEPTION ACTORS
    public void addTriceptionActors(){
        for (Image i: images.values())this.addActor(i);
    }

    //ADD TRICEPTION IMAGE
    public void addTriceptionImage(String key, Image i){images.put(key,i);}

    //ADD TRICEPTION STATE IMAGE
    public void addTriceptionStateImage(String key, Image i){
        images.put(key,i);
        i.setVisible(false);
    }

    //ADD TRICEPTION ACTION
    public void addTriceptionAction(Array<ParallelAction> listActions){

        int j=0;

        for(Image i: images.values()){
            i.addAction(listActions.get(j));
            j++;
        }
    }
    */

    /*_______________________________________________________________________________________________________________*/

    //GETTERS
    public float getTriceptionOriginX(){
        return images.get("TRICEPTION_COLORS").getOriginX();
    }

    public float getTriceptionOriginY(){
        return images.get("TRICEPTION_COLORS").getOriginY();
    }

    public float getTriceptionWidth(){
        return images.get("TRICEPTION_COLORS").getWidth();
    }

    public float getTriceptionHeight(){
        return images.get("TRICEPTION_COLORS").getHeight();
    }

    public float getTriceptionScaleX(){
        return images.get("TRICEPTION_COLORS").getScaleX();
    }

    public float getTriceptionScaleY(){
        return images.get("TRICEPTION_COLORS").getScaleY();
    }

    public int getImageArraySize(){
        return images.size();
    }

    public HashMap<String, Image> getImages() {
        return images;
    }

    public Stage getS() {
        return s;
    }

    //SETTERS
    public void setTriceptionPosition(float x, float y){
        for(Image i: images.values()) i.setPosition(x,y);
    }

    public void setTriceptionOrigin(float originX, float originY){
        for(Image i: images.values()) i.setOrigin(originX, originY);
    }

    public void setStage(Stage newStage){
        this.s = newStage;
    }

    public void setImages(HashMap<String, Image> images) {
        this.images = images;
    }

    public void setS(Stage s) {
        this.s = s;
    }
}

