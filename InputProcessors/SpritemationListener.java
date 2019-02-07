package com.mygdx.safe.InputProcessors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.mygdx.safe.SpritemationEntityConfig;
import com.mygdx.safe.sPritemation;

import javax.swing.GroupLayout;

/**
 * Created by sensenom on 1/03/18.
 */

public class SpritemationListener extends DragListener {

    //TAG
    private static final String TAG = SpritemationListener.class.getSimpleName();

    //ASPECTS
    private GenericMethodsInputProcessor g;

    //BOOLEANS
    private boolean _enabled = true;
    private boolean listenAlpha = false;
    //private boolean _draggItemStarted = false;

    //LISTENER
    private String listenerOwner;

    //IMAGE POINTER
    private Image im;
    private sPritemation spritemation;
    private Sprite sprite;

    //OTHER
    private boolean activateImagePositionDrag=false;
    private boolean selected = false;
    private boolean init_drag=false;
    private float init_pos_dragx;
    private float init_pos_dragy;

    //SCALE
    private float originalWidth = -1;
    private float originalHeight = -1;
    private float originalScalex = 1;
    private float originalScaley = 1;
    private float currentOriginalWidhScale = 1;
    private float currentOriginalHeightScale = 1;

    //ACTUAL POSITION
    private float actualx;
    private float actualy;

    //HUD LAST TOUCH POSITION
    private float hudActualX;
    private float hudActualY;


    private ParallelAction parallelAction= new ParallelAction();

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public SpritemationListener(String listenerOwner, Image image, sPritemation spritemation , GenericMethodsInputProcessor g){
        this.g = g;
        this.im=image;
        this.listenerOwner = listenerOwner;
        this.spritemation = spritemation;

        //this.sprite=new Sprite(spritemation.getSprite());
        //this.sprite.setRegion((TextureRegion) spritemation.getSpritemation().getKeyFrame(0.01f));


    }

    /*_______________________________________________________________________________________________________________*/

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){

        if(g.m.he.sclientHud.isEnableListeners()){


            if(!AlphaHitDetector(x, y) || listenAlpha) {

                g.println(TAG + "   " + AlphaHitDetector(x, y) + "   TOUCH DOWN!! " + listenerOwner);
                actualx = x;
                actualy = y;
                hudActualX = this.im.getX() + x;
                hudActualY = this.im.getY() + y;

                g.printlns(TAG + " IMG_POS: " + getImage().getX() + " , " + getImage().getY() + "  "+ getImage().getOriginX());
                g.printlns(TAG + " X: " + x + "    Y: " + y);
                g.printlns(TAG + " hudx: " + hudActualX + "    hudy: " + hudActualY);
                g.printlns(TAG + " widht: " + originalWidth + " , " + originalHeight);

                g.gm.sendMessage("GAMEGRAPH#LISTENER_EVENT#SPRITEMATION#" + listenerOwner + "#TOUCH_DOWN", null, -1);
                return true;
            }
            else{

            }
        }

        return false;
    }

    @Override
    public void touchDragged(InputEvent event, float x, float y, int pointer){

        if(g.m.he.sclientHud.isEnableListeners()){
            if(!AlphaHitDetector(x, y) || listenAlpha) {
                actualx = x;
                actualy = y;

                hudActualX = this.im.getX() + x;
                hudActualY = this.im.getY() + y;

                if (!init_drag) {
                    init_drag = true;
                    init_pos_dragx = x;
                    init_pos_dragy = y;
                    g.println(TAG + " SAVING POS: " + init_pos_dragx + " , " + init_pos_dragy);
                    g.println(TAG + " IMG_POS: " + getImage().getX() + " , " + getImage().getY());
                }

                if (activateImagePositionDrag && selected) {
                    getImage().setPosition(x, y);
                }
            }
        }
    }

    @Override
    public void touchUp(InputEvent event, float x, float y, int pointer, int button){

        if(g.m.he.sclientHud.isEnableListeners()){
            //g.printlns(TAG + "   " + AlphaHitDetector(x, y) + "   TOUCH UP!! " + listenerOwner);

            if(!AlphaHitDetector(x, y) || listenAlpha) {

                if (init_drag) {
                    init_drag = false;
                }
                g.gm.sendMessage("GAMEGRAPH#LISTENER_EVENT#SPRITEMATION#" + listenerOwner + "#TOUCH_UP", null, -1);
            }
            else{
                //g.printlns("OUTSIDE UP");
                g.gm.sendMessage("GAMEGRAPH#LISTENER_EVENT#SPRITEMATION#" + listenerOwner + "#TOUCH_UP_OUTSIDE", null, -1);
            }
        }
    }

    public void moveToInitPosition(){
        if(activateImagePositionDrag) {
            MoveToAction m = new MoveToAction();
            m.setPosition(init_pos_dragx, init_pos_dragy);
            getImage().addAction(m);
        }
        activateImagePositionDrag=false;
    }

    public void setOrigin(String origin){
        if(origin.equalsIgnoreCase("CENTER")){
            getImage().setOrigin((getImage().getWidth()/2), (getImage().getHeight()/2));
        }
    }


    public void moveToPosition(float x,float y, float duration){
        MoveToAction m = new MoveToAction();
        m.setPosition(x - (getImage().getWidth()/2), y - (getImage().getHeight()/2));
        m.setDuration(duration);
        m.setTarget(getImage());
        getImage().addAction(m);
        getImage().act(Gdx.graphics.getDeltaTime());
    }


    /*_______________________________________________________________________________________________________________*/

    //GETTERS

    public boolean get_enabled(){
        return _enabled;
    }

    public String getListenerOwner(){
        return listenerOwner;
    }

    public Image getImage() {
        return im;
    }

    public boolean isActivateImagePositionDrag() {
        return activateImagePositionDrag;
    }

    public boolean isSelected() {
        return selected;
    }

    public float getOriginalWidth() {
        return originalWidth;
    }

    public float getOriginalHeight() {
        return originalHeight;
    }

    public float getActualx() {
        return actualx;
    }

    public float getActualy() {
        return actualy;
    }

    public float getHudActualX() {
        return hudActualX;
    }

    public float getHudActualY() {
        return hudActualY;
    }

    //SETTERS

    public void set_enabled(boolean enabled){
        this._enabled = enabled;
    }

    public void setListenerOwner(String listenerOwner){
        this.listenerOwner = listenerOwner;
    }

    public void setImage(Image im) {
        this.im = im;
    }

    public void setActivateImagePositionDrag(boolean activateImagePositionDrag) {
        this.activateImagePositionDrag = activateImagePositionDrag;
    }


    public void setSelected(boolean selected) {

        Array<SpritemationListener> selectedListeners = g.m.he.sclientHud.getSelectedSpritemationListeners();

        if(selected){
            selectedListeners.add(this);
        }else{
            for(int i=0; i<selectedListeners.size; i++){

                if(selectedListeners.get(i).getListenerOwner().equalsIgnoreCase(this.listenerOwner)){
                    selectedListeners.removeIndex(i);
                    break;
                }
            }
        }

        this.selected = selected;
    }

    public void setOriginalWidth(float originalScaleX) {
        this.originalWidth = originalScaleX;
    }

    public void setOriginalHeight(float originalHeight) {
        this.originalHeight = originalHeight;
    }

    public void setOriginalSize(Image image){
        this.originalWidth = 0 + image.getWidth();
        this.originalHeight = 0 + image.getHeight();

    }

    public void setHudActualX(float hudActualX) {
        this.hudActualX = hudActualX;
    }

    public void setHudActualY(float hudActualY) {
        this.hudActualY = hudActualY;
    }

    public void setOriginalScale(float scalex, float scaley){
        this.originalScalex = 0 + scalex;
        this.originalScaley = 0 + scaley;
    }

    //ALPHA HIT DETECTOR
    public boolean AlphaHitDetector(int pixel){

        g.printlns(TAG + "     " + ((pixel & 0x000000ff) == 0));

        return (pixel & 0x000000ff) == 0;
    }

    public boolean AlphaHitDetector(float x,float y){


        TextureRegion t;
        Pixmap pixmap;

        float pixmapX;
        float pixmapY;

        //sprite = spritemation.getSprite();

        TextureRegion tregion = spritemation.getTxtRegion();

        Rectangle spriteBounds = sprite.getBoundingRectangle();

        /*g.printlns(TAG + "    SPRITEBOUNDS: " + spriteBounds + "     ------ " + Gdx.input.getX() + "    " + Gdx.input.getY()+ "    "
                + x + "  " + y +  "   region position:" +  tregion.getRegionX() +" " + tregion.getRegionY());*/


        Texture texture = tregion.getTexture();

        int spriteLocalX = (int)(x);
        int spriteLocalY = (int)(spriteBounds.height - (y));

        int textureLocalX = (int)(tregion.getRegionX() + (spriteLocalX / originalScalex));
        int textureLocalY = (int)(tregion.getRegionY() + (spriteLocalY / originalScaley));

        if (!texture.getTextureData().isPrepared()) {
            texture.getTextureData().prepare();
        }
        pixmap = texture.getTextureData().consumePixmap();

            /*g.printlns(TAG + "  TEXTURELOCAL X: " + textureLocalX + "   TEXTURELOCAL Y: " + textureLocalY + "    x: " + spriteLocalX +
                    "     y: " + spriteLocalY + "   resize factor x:  " + g.getResizeFactorX() + "  resize factor y:   " + g.getResizeFactorY());
*/
        return (AlphaHitDetector(pixmap.getPixel(textureLocalX, textureLocalY)));





        /*t=spritemation.getTextureRegion();
        t.getTexture().getTextureData().prepare();
        pixmap = t.getTexture().getTextureData().consumePixmap();


        pixmapX = 0 + x*pixmap.getWidth()/spritemation.getImage().getWidth();
        pixmapY = pixmap.getHeight() - y*pixmap.getHeight()/spritemation.getImage().getHeight();
        //float pixmapY = 0 + y*pixmap.getHeight()/i.getHeight();

        g.printlns(TAG + "     " + listenerOwner + "   x: " + x + "    y : " + y + "   **** " + (pixmap.getPixel((int)pixmapX,(int)pixmapY)  & 0x000000ff));

        return (AlphaHitDetector(pixmap.getPixel((int)pixmapX,(int)pixmapY)));*/
    }

    /*public boolean AlphaHitDetector(Image i,float x,float y){
        Image image = i;

        image.get
        return (AlphaHitDetector(((SpriteDrawable) i.getDrawable()).getSprite().getTexture().getTextureData().consumePixmap().getPixel((int)x,(int)y)));
    }*/

    public boolean isListenAlpha() {
        return listenAlpha;
    }

    public void setCurrentOriginalSizeScale(float x, float y){
        this.currentOriginalWidhScale = x;
        this.currentOriginalHeightScale = y;
    }

    public float getCurrentOriginalHeightScale() {
        return currentOriginalHeightScale;
    }

    public float getCurrentOriginalWidhScale() {
        return currentOriginalWidhScale;
    }

    public void setCurrentOriginalHeightScale(float currentOriginalHeightScale) {
        this.currentOriginalHeightScale = currentOriginalHeightScale;
    }

    public void setCurrentOriginalWidhScale(float currentOriginalWidhScale) {
        this.currentOriginalWidhScale = currentOriginalWidhScale;
    }

    public ParallelAction getParallelAction() {
        return parallelAction;
    }

    public void setParallelAction(ParallelAction parallelAction) {
        this.parallelAction = parallelAction;
    }

    public void setListenAlpha(boolean listenAlpha) {
        this.listenAlpha = listenAlpha;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
}
