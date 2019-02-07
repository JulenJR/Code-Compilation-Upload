package com.mygdx.safe;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.mygdx.safe.Components.PhysicsComponent;
import com.mygdx.safe.Entities.GameEntity;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;
import com.badlogic.gdx.math.Intersector;
import com.mygdx.safe.MainGameGraph.ActionChoice;
import com.mygdx.safe.MainGameGraph.GameGraph;

import java.util.HashMap;

import static com.badlogic.gdx.Gdx.app;

/**
 * Created by alumne_practiques on 05/10/17.
 */

public class CollisionManager {

    //TAG
    private static final String TAG = CollisionManager.class.getSimpleName();

    //ASPECTS
    private GenericMethodsInputProcessor g;

    /*_______________________________________________________________________________________________________________*/

    // COLLISION TABLE FORMAT:
    // String entityOriginCollision, Pair Class< EntityDestinyCollision, Pair class <Boolean StateOfCollision, Pair Class <Rectangle EntityOriginRecangle,Rectangle EntityDestinyRectangle>

    public static final HashMap<String,Pair<String,Pair<Boolean,Pair<Rectangle,Rectangle>>>> CollisionTable=new HashMap<String,Pair<String,Pair<Boolean,Pair<Rectangle,Rectangle>>>>();

    /*_______________________________________________________________________________________________________________*/

    //COLLISION ENTITY - OTHER ENTITY
    static public boolean isCollisionWithEntity(Array<GameEntity> entities, GameEntity ge) {

        Rectangle boundingBox = ge.getPhysicsComponent().get_collisionBoundingBox();
        Rectangle secundaryBbox;


        for (int i = 0; i < entities.size; i++) {

            GameEntity geToCheckCollision = entities.get(i);

            if (ge.NumId != geToCheckCollision.NumId && geToCheckCollision.isEnabled()) {

                 secundaryBbox = geToCheckCollision.getPhysicsComponent().get_collisionBoundingBox();

                if (Intersector.intersectRectangles(boundingBox, secundaryBbox, new Rectangle())) return true;
            }
        }

        return false;
    }

    static public boolean isCollisionWithEntity(Array<GameEntity> entities, GameEntity ge, float x, float y) {

        ge.getPhysicsComponent().get_collisionBoundingBox().setPosition(ge.getPhysicsComponent().get_collisionBoundingBox().x + x, ge.getPhysicsComponent().get_collisionBoundingBox().y + y);

        isCollisionWithEntity(entities, ge);

        ge.getPhysicsComponent().get_collisionBoundingBox().setPosition(ge.getPhysicsComponent().get_collisionBoundingBox().x - x, ge.getPhysicsComponent().get_collisionBoundingBox().y - y);

        return false;
    }

    /*_______________________________________________________________________________________________________________*/

    //COLLISION FOR AN ENTITY WITH MAPLAYER
    static public boolean isCollisionWhitMaplayer( Array<Rectangle> mapRectangles, GameEntity ge,GenericMethodsInputProcessor g){

        for(Rectangle r: mapRectangles) {

            if (Intersector.intersectRectangles(ge.getPhysicsComponent().get_collisionBoundingBox(), r, new Rectangle())) {
                g.println(TAG + " COLLISION " + r);
                return true;
            }
        }

        return false;
    }

    static public boolean isCollisionWhitMaplayer(Array<Rectangle> mapRectangles, GameEntity ge, float x, float y,GenericMethodsInputProcessor g){

        ge.getPhysicsComponent().get_collisionBoundingBox().setPosition(ge.getPhysicsComponent().get_collisionBoundingBox().x + x, ge.getPhysicsComponent().get_collisionBoundingBox().y + y);

        isCollisionWhitMaplayer(mapRectangles, ge,g);

        ge.getPhysicsComponent().get_collisionBoundingBox().setPosition(ge.getPhysicsComponent().get_collisionBoundingBox().x - x, ge.getPhysicsComponent().get_collisionBoundingBox().y - y);

        return false;
    }

    /*_______________________________________________________________________________________________________________*/

    //IS COLLISION OUT OF CURRENT CIRCUNSCRIPTION
    static public boolean isCollisionOutOfCurrentCircunscription (GameEntity ge, GenericMethodsInputProcessor g){

        if (ge.getPhysicsComponent().get_currentCircuns() == null || ge.isPlayer || ge.getPhysicsComponent().isChangingCircuns()){

            return false;
        }

        Rectangle currentCircunscription = g.m.lvlMgr.get_mapManager().get_currentMap().circunscriptionRectangles.get(ge.getPhysicsComponent().get_currentCircuns());
        Rectangle intersection = new Rectangle();
        Rectangle boundingBox = ge.getPhysicsComponent().get_collisionBoundingBox();

        if (Intersector.intersectRectangles(boundingBox, currentCircunscription, intersection)) {

            if (Math.abs(boundingBox.x - intersection.x) > 0.01f || Math.abs(boundingBox.y - intersection.y) > 0.01f ||
                    Math.abs(boundingBox.width - intersection.width) > 0.01f || Math.abs(boundingBox.height - intersection.height) > 0.01f) return true;
        }


        return false;
    }

    /*_______________________________________________________________________________________________________________*/

    static public boolean isCollisionWithCurrentCircunscription(GameEntity ge, GenericMethodsInputProcessor g){

        if (ge.getPhysicsComponent().get_currentCircuns() == null || ge.isPlayer) return false;

        Rectangle currentCircunscription = g.m.lvlMgr.get_mapManager().get_currentMap().circunscriptionRectangles.get(ge.getPhysicsComponent().get_currentCircuns());
        Rectangle boundingBox = ge.getPhysicsComponent().get_collisionBoundingBox();

        return Intersector.intersectRectangles(boundingBox, currentCircunscription, new Rectangle());

    }

    /*_______________________________________________________________________________________________________________*/

    //CHECK PUNTUAL PROXIMITY COLLISION
    public static boolean checkPuntualProximityCollision(GameEntity ge1, GameEntity ge2){
        return  Intersector.overlaps(ge1.getPhysicsComponent().getProximityArea(), ge2.getPhysicsComponent().get_fullBodyBoundingBox());
    }

    /*_______________________________________________________________________________________________________________*/

    //IS COLLISION WITH PORTAL
    static public void isCollisionWithPortal(HashMap<String, Array<Rectangle>> portalRectangles, GenericMethodsInputProcessor g){

        Rectangle playerCollisionBbox = g.m.lvlMgr.getPlayer().getPhysicsComponent().get_collisionBoundingBox();

        for (String s : portalRectangles.keySet()) {
            if (s.contains("PORTAL_")) {

                for(Rectangle portal: portalRectangles.get(s)){
                    if(Intersector.intersectRectangles(playerCollisionBbox, portal, new Rectangle())){
                        g.gm.sendMessage("GAMEGRAPH#PORTAL_TAKE#PORTAL#" + s, null, -1);
                        return;
                    }
                }
            }
        }
        return;
    }

    /*_______________________________________________________________________________________________________________*/

    //IS COLLISION WITH EVENT RECTANGLE
    static public void isCollisionWithEventRectangle(HashMap<String, Array<Rectangle>> eventRectangles, GenericMethodsInputProcessor g){

        PhysicsComponent pc = g.m.lvlMgr.getPlayer().getPhysicsComponent();
        HashMap<String, Boolean> eventStates = g.m.lvlMgr.get_mapManager().get_currentMap().eventStates;

        for (String s : eventRectangles.keySet()) {

            if (s.contains("EVENT_") && eventStates.get(s) != null) {
                boolean collision = false;

                for(Rectangle eventZone : eventRectangles.get(s)) {
                    if(Intersector.intersectRectangles(pc.get_fullBodyBoundingBox(), eventZone, new Rectangle())){
                        collision = true;
                        break;
                    }
                }

                if (collision && !eventStates.get(s)) {
                    g.gm.sendMessage("GAMEGRAPH#EVENT_TAKE#START#EVENT#" + s, null, -1);
                    eventStates.put(s, true);
                } else if (!collision && eventStates.get(s)) {
                    g.gm.sendMessage("GAMEGRAPH#EVENT_TAKE#STOP#EVENT#" + s, null, -1);
                    eventStates.put(s, false);
                }
            }
        }
    }

    /*_______________________________________________________________________________________________________________*/

    static public void checkCollisionProximityArea(GameEntity ge, GenericMethodsInputProcessor g){

        Circle proximityArea = ge.getPhysicsComponent().getProximityArea();

        for(Pair<Integer, Boolean> p: ge.getProximityRelations()){

            GameEntity entityToCheckProximity = g.m.gsys.getEntities().get( p.getFirst());
            boolean isProxim = p.getSecond();
            boolean collision = false;

            if (Intersector.overlaps(proximityArea, entityToCheckProximity.getPhysicsComponent().get_fullBodyBoundingBox())) collision = true;

            if (collision && !isProxim) {
                p.setSecond(true);
                g.gm.sendMessage("GAMEGRAPH#PROXIMITY#" + entityToCheckProximity.getID() +"#START#ENTITY#" + ge.getID() , null, -1);
            } else if (!collision && isProxim) {
                p.setSecond(false);
                g.gm.sendMessage("GAMEGRAPH#PROXIMITY#" + entityToCheckProximity.getID() +"#STOP#ENTITY#" + ge.getID(), null ,-1);
            }
        }
    }

    /*_______________________________________________________________________________________________________________*/

    //CHECK COLLISION WITH SELECTABEL ENTITY
    static public boolean checkCollisionWithSelectableEntity (Array<GameEntity> entities,  Rectangle r, GenericMethodsInputProcessor g) {

        for (GameEntity ge: entities) {

            HashMap<String, ActionChoice> graphPointers = g.m.ggMgr.getCurrentgg().getPointers();
            String pointer = "SELECT#ENTITY#" + ge.getID();
            GameGraph currentGraph = g.m.ggMgr.getCurrentgg();

            g.printlns(ge.getID() + "   " + pointer + "    " + ge.isSelectable());

            if(ge.isEnabled() && ge.isSelectable() && graphPointers.get(pointer) != null){
                if(!(currentGraph.getNodeById(graphPointers.get(pointer).getDestinyID()).is_blocked()))
                    if (Intersector.intersectRectangles(r, ge.getPhysicsComponent().get_fullBodyBoundingBox(), new Rectangle()) ){

                        g.gm.sendMessage("GAMEGRAPH#SELECT#ENTITY#" + ge.getID(), null, -1);
                        return true;
                    }
            }


        }

        return false;
    }

}

//CHECK COLLISION PLAYER PROXIMITY AREA
    /*static public boolean checkCollisionPlayerProximityArea (GameEntity ge, Circle proximityArea, GenericMethodsInputProcessor g){

        HashMap<String, ActionChoice> pointers = g.m.ggMgr.getCurrentgg().getPointers();
        GameGraph currengGraph = g.m.ggMgr.getCurrentgg();

        boolean collision = false;
        boolean haveSelectPointer = (pointers.get("SELECT#ENTITY#" + ge.getID()) != null);
        boolean haveProximityPointer = (pointers.get("PROXIMITY#ENTITY#" + ge.getID()) != null);
        boolean isblocked = false;

            if (haveSelectPointer)
                isblocked = (currengGraph.getNodeById(pointers.get("SELECT#ENTITY#" + ge.getID()).getDestinyID()).is_blocked());
            else if (haveProximityPointer)
                isblocked = (currengGraph.getNodeById(pointers.get("PROXIMITY#ENTITY#" + ge.getID()).getDestinyID()).is_blocked());

            if (haveSelectPointer || haveProximityPointer) {

                if (isblocked && ge.isProximity()) {
                    ge.setProximity(false);
                }
                else if (!isblocked){
                    for (String key : ge.getPhysicsComponent().get_boundingBoxes().keySet()) {
                        if (Intersector.overlaps(proximityArea, ge.getPhysicsComponent().get_boundingBoxes().get(key))) {
                            collision = true;
                        }
                    }

                    if (collision && !ge.isProximity()) {
                        ge.setProximity(true);
                        if (haveProximityPointer)
                            g.gm.sendMessage("GAMEGRAPH#PROXIMITY#START#ENTITY#" + ge.getID() , null, -1);
                    } else if (!collision && ge.isProximity()) {
                        ge.setProximity(false);
                        if (haveProximityPointer)
                            g.gm.sendMessage("GAMEGRAPH#PROXIMITY#STOP#ENTITY#" + ge.getID(), null ,-1);
                    }
                }
            }

        return false;
    }*/
