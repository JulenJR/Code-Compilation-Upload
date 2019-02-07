package com.mygdx.safe.InputProcessors;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.safe.CollisionManager;
import com.mygdx.safe.Entities.GameEntity;
import com.mygdx.safe.Entities.HUD_Entity;
import com.mygdx.safe.SpritemationEntityConfig;
import com.mygdx.safe.sPritemation;
import com.mygdx.safe.screens.MainGameScreen;

/**
 * Created by sensenom on 25/05/17.
 */

public class CameraGestureDetectorProcessor implements GestureDetector.GestureListener {

    //TAG
    private static final String TAG = CameraGestureDetectorProcessor.class.getSimpleName();

    //ASPECTS
    private GenericMethodsInputProcessor g;

    //HUD ENTITY
    private HUD_Entity h;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public CameraGestureDetectorProcessor(HUD_Entity h){
        this.g=h.getGenericMethodsInputProcessor(); this.h=h;
    }

    /*_______________________________________________________________________________________________________________*/

    //TAP
    @Override
    public boolean tap(float x, float y, int count, int button) {

        float mapX, mapY;
        GameEntity selectableGE = null;

        //Get the equivalent map coordinates to x & y (screen coordinates)
        mapX = g.m.gsys.getCamera().position.x - ((MainGameScreen.VIEWPORT.viewportWidth/2) * g.m.gsys.getCamera().zoom) +
                (x/((MainGameScreen.VIEWPORT.physicalWidth/ MainGameScreen.VIEWPORT.viewportWidth)/g.m.gsys.getCamera().zoom));
        mapY = g.m.gsys.getCamera().position.y + ((MainGameScreen.VIEWPORT.viewportHeight/2) * g.m.gsys.getCamera().zoom) -
                (y/((MainGameScreen.VIEWPORT.physicalHeight/ MainGameScreen.VIEWPORT.viewportHeight)/g.m.gsys.getCamera().zoom));

        Rectangle tapRectangle = new Rectangle(mapX - 0.1f, mapY - 0.1f, 0.2f, 0.2f);

        if(g.isTapMovement()){

            g.m.lvlMgr.getPlayer().getPhysicsComponent().setForcingMove(false);
            g.m.lvlMgr.getPlayer().getPhysicsComponent().setForcedMoveDestiny(0,0);
            g.m.lvlMgr.getPlayer().getInputComponent().ProcessInput(g.m.lvlMgr.getPlayer());

        }


        //CHEK IF TAP IN A SELECTABLE ENTITY
        if(!g.m.gsys.isDialogMode()) tapWhenNotDialogMode(x, y, count, button, tapRectangle, mapX, mapY);
        else tapWhenDialogMode(tapRectangle);




        //TAP SPRITEMATION LISTENER %% MORE COMPLEX %%
        /*

        g.println(TAG +" TAP : X="+x + ", TAP: Y="+ y + "CAMERA CENTER POSITION=" + g.getCamera().position + " MAPX=" + mapX + "MAPY="+ mapY);

        Rectangle spritemationBounds=new Rectangle();

        Rectangle tapRectangle2 = new Rectangle(mapX - 0.01f, mapY - 0.01f, 0.02f, 0.02f);
        for(int i=0;i<g.m.gsys.spritemationsHost.getHostClientExtendedPos().getSize();i++) {
            SpritemationEntityConfig e = g.m.gsys.spritemationsHost.getHostClientEntitySpritAnim().get(
                    g.m.gsys.spritemationsHost.getHostClientExtendedPos().getPositionArray().get(i) - 1); // SELECT CONFIG OF SP_ARRAY WITH EXTENDEDPOS VALUES

            sPritemation s = g.m.gsys.spritemationsHost.getSpritemationsId().get(e._id_Spritemation); //SELECT SPRITEMATION USING ID FROM CONFIG
            Sprite sprite = s.getSprite();

            float scx;
            float scy;

            // CASE HUD DIMENSION
            scx = s.getTxtRegion().getRegionWidth() / 45.4f;
            scy = s.getTxtRegion().getRegionHeight() / 46.47f;

            //g.println(TAG + " WITHNAME="+ e._withname + " POSITION ="+e._position + " MASTERDISTANCE" + e._mDistance+ " BOUNDINGBOXSPRITE-RECTANGLE="+ sprite.getBoundingRectangle() + " SIZE: SCX="+scx + "  SCY="+scy + " SCALEX: " +s.scalex + " SCALEY: "+s.scaley + " SPRITE: GETORIGINX "+ sprite.getOriginX() +  " SPRITE: GETORIGINY "+ sprite.getOriginY() + " SPRITE: GETwidth= "+ sprite.getWidth() + " SPRITE: GETHeight= " +sprite.getHeight());
            spritemationBounds.setSize(scx * sprite.getScaleX(), scy * sprite.getScaleY());
            spritemationBounds.setPosition(e._position.x + e._mDistance.x, e._position.y + e._mDistance.y);
            g.println(TAG + " WITHNAME=" + e._withname + " SPRITEMATION_RECTANGLE: " + spritemationBounds + " TAP RECTANGLE=" + tapRectangle);
            if (Intersector.intersectRectangles(spritemationBounds, tapRectangle2, new Rectangle())) {
                g.println(TAG + "*********************TAP***************** WITHNAME=" + e._withname);
            }
        }
        */

        return false;

    }

    public void tapWhenNotDialogMode(float x, float y, int count, int button, Rectangle tapRectangle, float mapX, float mapY){

        GameEntity player = g.m.lvlMgr.getPlayer();
        boolean selectEntity = CollisionManager.checkCollisionWithSelectableEntity(g.m.gsys.getEntities(), tapRectangle, g);


        if(!selectEntity && g.isTapMovement()){

            if(g.m.lvlMgr.get_currentLvl().getSelectedGE() != null){
                g.m.lvlMgr.get_currentLvl().getSelectedGE().setSelected(false);
                g.m.lvlMgr.get_currentLvl().getSelectedGE().getPhysicsComponent().setStopPhysicsUpdate(false);
                g.m.lvlMgr.get_currentLvl().setSelectedGE(null);
            }

            player.getPhysicsComponent().setForcedMoveDestiny(mapX, mapY);
            player.getPhysicsComponent().setForcingMove(true);

            if(!player.getPhysicsComponent().getPreferedTapForcedMoveState().equalsIgnoreCase("")){
                player.getPhysicsComponent().setForcedMoveState(player.getPhysicsComponent().getPreferedTapForcedMoveState());
            }
            else player.getPhysicsComponent().setForcedMoveState("RUN");

            /*if(count == 1)player.getPhysicsComponent().setForcedMoveState("RUN");
            else if(count > 1)player.getPhysicsComponent().setForcedMoveState("WALK");*/

            player.getGraphicComponent().setVelocity(
                    player.getGraphicComponent().get_sCache().getAnimationMap().getSProgram
                            (player.getPhysicsComponent().getForcedMoveState(), player.getPhysicsComponent().get_currentDirection(), false),
                    player.getPhysicsComponent());

            g.penfroidRose(x, y, 0, button);
        }
    }

    public void tapWhenDialogMode(Rectangle tapRectangle){

    }

    //TOUCH DOWN
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    //LONG PRESS
    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    //FLING
    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    //PAN
    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    //PAN STOP
    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    //ZOOM
    @Override
    public boolean zoom(float initialDistance, float distance) {
        /*
        if(!g.isHasStageInput() && !g.m.gsys.isDialogMode()) {
            g.setZoomFactor(distance / initialDistance);
            g.setZooming(true);
            return true;
        }*/

        return false;
    }

    //PINCH
    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    //PINCH STOP
    @Override
    public void pinchStop() {
        /*
        if(!g.isHasStageInput()) {
            g.setZoomFactor(1);
            g.setZooming(false);
        }
        */

    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS
    public com.mygdx.safe.Entities.HUD_Entity getH() {
        return h;
    }

    //SETTERS
    public void setH(com.mygdx.safe.Entities.HUD_Entity h) {
        this.h = h;
    }

}
