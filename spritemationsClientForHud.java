package com.mygdx.safe;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.ImageResolver;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.mygdx.safe.Entities.HUD_Entity;
import com.mygdx.safe.IA.ExtendedPos;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;
import com.mygdx.safe.InputProcessors.SpritemationListener;


import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Boris.InsoiratGames on 28/02/18.
 */

public class spritemationsClientForHud {

    public static final String TAG=spritemationsClientForHud.class.getSimpleName();
    GenericMethodsInputProcessor g;
    ExtendedPos ep;
    Array<SpritemationEntityConfig> secarray;
    HashMap<String, SpritemationEntityConfig> secarrayHash;
    Array<Image> spritemationHudImages;
    HashMap<String,Image> spritemationHudImagesHash;
    Array<SpritemationListener> spritemationListeners;
    HashMap<String, SpritemationListener> spritemationListenersHash;

    Array<SpritemationListener> selectedSpritemationListeners;

    HUD_Entity h;
    sPritemationsHost sh;


    boolean enableListeners = true;

    public spritemationsClientForHud(HUD_Entity h, GenericMethodsInputProcessor g, Array<SpritemationEntityConfig> seca, sPritemationsHost shost){
        this.h=h;
        this.g=g;
        ep=new ExtendedPos();
        if(seca==null) {
            this.secarray = new Array<SpritemationEntityConfig>();
            this.secarrayHash = new HashMap<String, SpritemationEntityConfig>();
        }
        sh=shost;
        spritemationHudImages=new Array<Image>();
        spritemationHudImagesHash=new HashMap<String, Image>();
        spritemationListeners=new Array<SpritemationListener>();
        spritemationListenersHash=new HashMap<String, SpritemationListener>();
        selectedSpritemationListeners = new Array<SpritemationListener>();

    }

    public void setSpritemation(String name,String withname){
        setSpritemation(name,withname,0,0);
    }

    public void setSpritemation(String name,String withname,Vector2 masterdistance){
        setSpritemation(name,withname,masterdistance.x,masterdistance.y,0);
    }

    public void setSpritemation(String name,String withname,float mx,float my){
        setSpritemation(name,withname,mx,my,0);
    }

    public void setSpritemation(String name,String withname,Vector2 masterdistance,float rotation){
        setSpritemation(name,withname,masterdistance.x,masterdistance.y,rotation);
    }

    public void setSpritemation(String name,String withname,float mx,float my,float rotation){
        setSpritemation(name,withname,mx,my,rotation,-1);
    }

    public void setSpritemation(String name,String withname,float mx,float my,float rotation,float fakeFrame,float sizex,float sizey){

        int sType=0;
        int i=sh.basicSetSpritemations(name,withname,secarray, secarrayHash,ep);

        if(i<secarray.size){

            int id = sh.spritemations.get(name).Id;
            Image im=new Image((TextureRegion) sh.getSpritemationsId().get(id).spritemation.getKeyFrame(0.01f));
            im.setVisible(false);
            im.setPosition(mx,my);
            im.setName(withname);
            im.setVisible(true);
            im.setRotation(rotation);


            // ADDING LISTENER
            SpritemationListener li=new SpritemationListener(withname,im,sh.getSpritemations().get(name),g);
            im.addListener(li);
            spritemationListeners.add(li);
            spritemationListenersHash.put(withname,li);


            spritemationHudImages.add(im);
            spritemationHudImagesHash.put(withname,im);
            g.m.he.getS().addActor(im);

            secarray.get(i)._rotation=rotation;
            secarray.get(i)._systemType=sType;


            if(sizex!=-1 && sizey!=-1) {
                im.setSize(sizex,sizey);
                scaleSpritemation(withname, "STANDARD_VALUE");
                //g.println(TAG + " ******************** SIZEESSSZZZZZ: " + sizex+" , "+ sizey);
            }
            if(fakeFrame==-1 || (fakeFrame>=0 && fakeFrame<=100)) secarray.get(i)._fakeFrame=fakeFrame;
        }

    }

    public void setSpritemationPosition(int id,float mdx,float mdy){

        Image image = spritemationHudImages.get(id);
        image.setPosition(mdx,mdy);


    }

    public void setSpritemationPosition(String withname,float mdx,float mdy){

        Image image = spritemationHudImagesHash.get(withname);
        image.setPosition (mdx, mdy);


    }

    public void setSpritemationCenterPosition(String withname,float mdx,float mdy){

        Image image = spritemationHudImagesHash.get(withname);
        image.setPosition( mdx - (image.getWidth()/2), mdy - (image.getHeight()/2));

    }

    public void setSpritemationFixedPosition(String withname,float mdx,float mdy){

        Image image = spritemationHudImagesHash.get(withname);
        image.setPosition( mdx, mdy);


    }








    public void activateDragImagePosition(String withname,boolean b){
        SpritemationListener li;
        for(int i=0;i<spritemationListeners.size;i++){
            if(spritemationListeners.get(i).getListenerOwner().equalsIgnoreCase(withname)){
                li=spritemationListeners.get(i);
                li.setActivateImagePositionDrag(b);
            }
        }
    }

    public void scaleSpritemation(String withname,float scx,float scy){

        SpritemationListener li = null;
        Image image = null;

        for(int i=0;i<spritemationListeners.size;i++){
            if(spritemationListeners.get(i).getListenerOwner().equalsIgnoreCase(withname)) {
                image = spritemationHudImages.get(i);
                li = spritemationListeners.get(i);
                break;
            }
        }

        if(image != null && li != null) {

            //At the first scale configuration the values of original sizes are allways -1
            if(li.getOriginalWidth()==-1 && li.getOriginalHeight() == -1) {

                //save the initial scale values from the original image
                li.setOriginalScale(scx, scy);

                //set the new image size with the scale values
                image.setSize(image.getWidth()*scx, image.getHeight()*scy);

                //save the initual size values
                li.setOriginalSize(image);

            }
            else {
                //resize the image
                image.setSize(li.getOriginalWidth() * scx, li.getOriginalHeight() * scy);

                //save the scale values applied to the initial size
                li.setCurrentOriginalSizeScale(scx, scy);
            }

        }
        else {
            g.println(TAG + " SCALE SPRITEMATION ERROR: SPRITEMATION " + withname + " DOESN'T EXIST.." );
        }
    }

    public void scaleSpritemation(String withname, String scaleType){

        if(scaleType.equalsIgnoreCase("SELECTED")){
            scaleSpritemation(withname, 1.15f, 1.15f);
        }
        else if(scaleType.equalsIgnoreCase("STANDARD_VALUE")){
            scaleSpritemation(withname, 1.0f, 1.0f);
        }
    }



    public void setSpritemation(String name,String withname,float mx,float my,float rotation,float fakeFrame){
        setSpritemation(name,withname,mx,my,rotation,fakeFrame,-1,-1);
    }





    public void setSpritemations(Array<SpritemationEntityConfig> secArray){
        sh.setSpritemations(secArray);
    }
    public void setSpritemationVisibility(String withname, boolean isVisible){

        boolean found=false;
        for (int i = 0; i < ep.getSize(); i++) {
            SpritemationEntityConfig e = secarray.get(i);
            if (e._withname.equalsIgnoreCase(withname)) {

                g.printlns(TAG + " ******* " + withname + "    " + isVisible);

                found=true;
                spritemationHudImages.get(ep.getPositionArray().get(i)-1).setVisible(isVisible);

                g.println(TAG + " SET VISIBILITY name:" + e._name + " AS " + e._withname + " ARRAY ID:" + i + " Spritemation Id: "
                        + e._id_Spritemation + " " + ep.toString());

            }
        }
        if(!found) System.out.println(TAG + " SET VISIBILITY ERROR!! " + withname + " NOT EXIST");

    }

    public void setSpritemationFakeFrame(String withname, float fakeframe){

        boolean found=false;
        for (int i = 0; i < ep.getSize(); i++) {
            SpritemationEntityConfig e = secarray.get(i);
            if (e._withname.equalsIgnoreCase(withname)) {
                found=true;
               e._fakeFrame=fakeframe;

                g.println(TAG + " SET FAKEFRAME name:" + e._name + " AS " + e._withname + " ARRAY ID:" + i + " Spritemation Id: "
                        + e._id_Spritemation + " " + ep.toString());

            }
        }
        if(!found) System.out.println(TAG + " SET FAKEFRAME ERROR!! " + withname + " NOT EXIST");

    }

    public void updateClientSpritemations(float delta){

        for(int i=0;i<ep.getSize();i++){

            Vector2 newPosition = null;

            SpritemationEntityConfig e=secarray.get(ep.getPositionArray().get(i)-1); // SELECT CONFIG OF SP_ARRAY WITH EXTENDEDPOS VALUES

            sPritemation s=sh.getSpritemationsId().get(e._id_Spritemation); //SELECT SPRITEMATION USING ID FROM CONFIG

            getSpritemationListenersHash().get(e._withname).setSprite(new Sprite(s.getSprite(delta, e)));
            e._spriteDrawable.setSprite(getSpritemationListenersHash().get(e._withname).getSprite()); // FOR HUD CASES

            spritemationHudImages.get(ep.getPositionArray().get(i)-1).setDrawable(e._spriteDrawable); // SELECT IMAGES OF IMAGES ARRAY  WITH LONGPOS VALUES



            if(e.parabolicMovementActivator){
                newPosition = e.updateParabolicMovement(delta);

                spritemationHudImages.get(ep.getPositionArray().get(i)-1).setPosition(newPosition.x, newPosition.y);
            }
            // spritemationHudImages.get(ep.getPositionArray().get(i)-1).setPosition(e._position.x+e._mDistance.x,e._position.y+e._mDistance.y);

        }
    }

    public void reset(){

        ep=new ExtendedPos();

        this.secarray = new Array<SpritemationEntityConfig>();
        this.secarrayHash = new HashMap<String, SpritemationEntityConfig>();

        spritemationHudImages=new Array<Image>();
        spritemationHudImagesHash=new HashMap<String, Image>();
        spritemationListeners=new Array<SpritemationListener>();
        spritemationListenersHash=new HashMap<String, SpritemationListener>();
        selectedSpritemationListeners = new Array<SpritemationListener>();

    }



    public void receive(String[] message, String treeID, int treeNumNode){




    }

    //GETTERS


    public Array<Image> getSpritemationHudImages() {
        return spritemationHudImages;
    }

    public Array<SpritemationListener> getSpritemationListeners() {
        return spritemationListeners;
    }

    public HashMap<String, Image> getSpritemationHudImagesHash() {
        return spritemationHudImagesHash;
    }

    public HashMap<String, SpritemationListener> getSpritemationListenersHash() {
        return spritemationListenersHash;
    }

    public Array<SpritemationListener> getSelectedSpritemationListeners() {
        return selectedSpritemationListeners;
    }

    public Array<SpritemationEntityConfig> getSecarray() {
        return secarray;
    }

    public HashMap<String, SpritemationEntityConfig> getSecarrayHash() {
        return secarrayHash;
    }

    public boolean isEnableListeners() {
        return enableListeners;
    }

    //SETTERS


    public void setSpritemationHudImages(Array<Image> spritemationHudImages) {
        this.spritemationHudImages = spritemationHudImages;
    }

    public void setSpritemationHudImagesHash(HashMap<String, Image> spritemationHudImagesHash) {
        this.spritemationHudImagesHash = spritemationHudImagesHash;
    }

    public void setSpritemationListeners(Array<SpritemationListener> spritemationListeners) {
        this.spritemationListeners = spritemationListeners;
    }

    public void setSpritemationListenersHash(HashMap<String, SpritemationListener> spritemationListenersHash) {
        this.spritemationListenersHash = spritemationListenersHash;
    }

    public void setSelectedSpritemationListeners(Array<SpritemationListener> selectedSpritemationListeners) {
        this.selectedSpritemationListeners = selectedSpritemationListeners;
    }

    public void setEnableListeners(boolean enableListeners) {
        this.enableListeners = enableListeners;
    }

    public void setSecarray(Array<SpritemationEntityConfig> secarray) {
        this.secarray = secarray;
    }

    public void setSecarrayHash(HashMap<String, SpritemationEntityConfig> secarrayHash) {
        this.secarrayHash = secarrayHash;
    }

    public ExtendedPos getEp() {
        return ep;
    }
}
