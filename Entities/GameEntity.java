package com.mygdx.safe.Entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.esotericsoftware.spine.Skin;
import com.mygdx.safe.AllConfigs;
import com.mygdx.safe.Components.AIComponent;
import com.mygdx.safe.Components.GraphicsComponent;
import com.mygdx.safe.Components.InputComponent;
import com.mygdx.safe.Components.PhysicsComponent;
import com.mygdx.safe.EntityConfig;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;
import com.mygdx.safe.MainGameGraph.ProgrammedMovement;
import com.mygdx.safe.Pair;
import com.mygdx.safe.sPritemationsClient;
import com.mygdx.safe.screens.ProfileScren;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Boris.InspiratGames on 8/06/17.
 */

public class GameEntity extends Entity {

    //TAG
    private static final String TAG = GameEntity.class.getSimpleName();
    private static int CountId=0;

    //UNIQUE NUM_ID;
    public int NumId;

    //TYPE PLAYER OT NOT
    public boolean isPlayer=false;

    //ASPECTS
     GenericMethodsInputProcessor g;

    //CONFIG
    private EntityConfig e;
    private boolean setEntityConfig=false;

    //COMPONENTS
    private InputComponent ic;
    private PhysicsComponent pc;
    private GraphicsComponent gc;
    private AIComponent ai;

    //Spine
    private boolean isSpine = false;

    //Nanimations
    private boolean hasNanimation = false;

    //GAME ENTITY INFORMATION
    private String ID;
    public GameEntity collisionGE = null;
    private boolean isSelected = false;
    private boolean isSelectable = true;

    //OTHER
    private int CONTROLAUTOMATIC=3; // SETS SECONDS FOR AUTOMATIC CHANGES DIRECTION
    int secondsForAutomaticControl=0;
    private boolean ticksForInputControlEntity=false;
    private boolean select;
    private boolean enabled;

    private Array<Pair<Integer, Boolean>> proximityRelations = new Array<com.mygdx.safe.Pair<Integer, Boolean>>();



    //INSTRUCTIONS PENDING FOR OK
    private ArrayList<String> pendingOKinstructions = new ArrayList<String>();

    //ENTITY_DUST REFERENCE
    public ParticleEffect entitydust;

    //SPRITEMATION CLIENT
    public sPritemationsClient sclient;


    /*_______________________________________________________________________________________________________________*/

    //STATIC METHODS
    static public com.mygdx.safe.EntityConfig GetEntityConfig(String path){
        Json json = new Json();
        return json.fromJson(com.mygdx.safe.EntityConfig.class, Gdx.files.internal(path));
    }

    static public com.mygdx.safe.npcConfig GetArrayEntityConfig(String path){
        Json json = new Json();
        return json.fromJson(com.mygdx.safe.npcConfig.class, Gdx.files.internal(path));
    }

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public GameEntity(GenericMethodsInputProcessor g, OrthographicCamera camera){
        this.g=g;
        ic=new InputComponent(g);
        ai=new AIComponent(g);
        pc=new PhysicsComponent(g,camera, this);
        gc=new GraphicsComponent(g, this);
        NumId=CountId;
        CountId++;

    }

    //CONFIG GAME ENTITY --- RETURNS FALSE IF NOT ENTITYCONFIG LOADED, ELSE, CONFIGS ALL COMPONENTS
    public boolean ConfigGameEntity(com.mygdx.safe.Components.InputComponent.InputMethod i, Vector2 _playerposition, com.mygdx.safe.sCache sCache, HashMap<String, com.mygdx.safe.nCache> nCacheList, String ID){
        if(setEntityConfig){

            if(ID.contains("PLAYER"))
                isPlayer=true;
            g.println("\n"+ TAG + " ID: ["+ ID+"]" +" NumId : ["+NumId+ "]  + <boolean isPlayer: ["+isPlayer+"]> [" + ((e == null)?"NULL CONFIG":"NOT NULL CONFIG") + "] GAME ENTITY");

            //Actualize boolean values
            isSpine = e.is_isSpine();
            hasNanimation = e.is_hasNAnimation();
            enabled = e.is_enabled();

            setID(ID); //Sets the entityID for each entity created

            g.m.gsys.AddEntity(this);

            ic.config(e,i);                             //CONFIGS INPUTCOMPONENT
            pc.config(e,_playerposition, ID);               //CONFIGS PHYSICSCOMPONENT, PART 1 // NO SANIMATION
            gc.config(e,pc, sCache, nCacheList);        //CONFIGS GRAPHICSCOMPONENT

            pc.config_Animation(e, gc.get_Sanimation(), this); //CONFIG  PHYSICS COMPONENT , PART2

            ai.config(e);                               //CONFIGS AICOMPONENT
            if(e.is_specialEntity()) ic.setI(InputComponent.InputMethod.NONE);

            sclient = new sPritemationsClient(g, this, null, g.m.gsys.spritemationsHost);

            return true;
        }else{
            return false;
        }
    }

    /*_______________________________________________________________________________________________________________*/

    //RECEIVE
    public void receive(String[] message, String treeID, int treeNumNode){

        String str = "";

        for(int i=0; i<message.length; i++){
            str += message[i] +"#";
        }
        str = str.substring(0, str.length()-1);

        g.println(TAG + "RECEIVING:   " + str.substring(0, str.length()));

        if (message[0].equalsIgnoreCase("MOVE_TO")) {
            int indexForState;

            if(message[1].equalsIgnoreCase("POSITION")){

                if(message.length == 4) {

                    Rectangle destiny = g.m.lvlMgr.get_mapManager().get_currentMap().getPortalRectangles().get(message[2]).get(0);
                    pc.setForcedMoveDestiny(destiny.x, destiny.y);
                    pc.setForcedMoveState(message[3]);
                    pc.setForcingMove(true);

                }else if(message.length == 5) {

                    pc.setForcedMoveDestiny(Float.valueOf(message[2]), Float.valueOf(message[3]));
                    pc.setForcedMoveState(message[4]);
                    pc.setForcingMove(true);

                }else if(message.length == 7){

                    float xVariance = Float.valueOf(message[3]);
                    float yVariance = Float.valueOf(message[5]);

                    if(message[2].equalsIgnoreCase("-")) xVariance = -xVariance;
                    if(message[4].equalsIgnoreCase("-")) yVariance = -yVariance;


                    pc.setForcedMoveDestiny(getpositionx() + xVariance, getpositiony() + yVariance);
                    pc.setForcedMoveState(message[6]);
                    pc.setForcingMove(true);
                }

                removeMovePendingInstructions();
                pendingOKinstructions.add(str + "@" + treeID + "@" + treeNumNode);

            }else if(message[1].equalsIgnoreCase("INTERACT_WITH")){

                GameEntity GEtoInteraction;

                if(message[2].contains("PLAYER")) GEtoInteraction = g.m.lvlMgr.getPlayer();
                else GEtoInteraction = g.m.lvlMgr.get_currentLvl().get_lvlEntities().get(message[2]);

                Vector2 destiny = pc.getDestinyWhenSelectGE(GEtoInteraction);

                if(destiny != null){
                    pc.setForcedMoveDestiny(pc.getDestinyWhenSelectGE(GEtoInteraction));
                    pc.setForcedMoveState(message[3]);
                    pc.setForcingMove(true);

                    removeMovePendingInstructions();
                    pendingOKinstructions.add(str + "@" + treeID + "@" + treeNumNode);
                }
                else{
                    g.m.lvlMgr.get_currentLvl().setSelectedGE(null);
                    GEtoInteraction.getPhysicsComponent().setStopPhysicsUpdate(false);
                }

            }

        }else if(message[0].equalsIgnoreCase("SET_POSITION")){
            if(message.length==3){

                getPhysicsComponent().setBoundsAfterMovement(Float.valueOf(message[1]),Float.valueOf(message[2]));
                getPhysicsComponent().setGeCurrentPos(new Vector2(Float.valueOf(message[1]),Float.valueOf(message[2])));
                g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

            }else if(message.length==4){
                if(message[1].equalsIgnoreCase("FROM_ALLCONFIG") && message[2].equalsIgnoreCase("ENTITY")){
                    if( message[3].contains("PLAYER")) {
                        getPhysicsComponent().setGeCurrentPos(ProfileScren.ac.playerPosition);
                    }else {
                        for(int i = 0; i< ProfileScren.ac.lvlEntitiesPosition.size; i++){
                            if(ProfileScren.ac.lvlEntitiesPosition.get(i).getFirst().equalsIgnoreCase(message[3])){
                                getPhysicsComponent().setBoundsAfterMovement(ProfileScren.ac.lvlEntitiesPosition.get(i).getSecond().x,
                                                                             ProfileScren.ac.lvlEntitiesPosition.get(i).getSecond().y);
                                getPhysicsComponent().setGeCurrentPos(ProfileScren.ac.lvlEntitiesPosition.get(i).getSecond());

                                break;
                            }
                        }
                    }
                }

                g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

            }
        }else if(message[0].equalsIgnoreCase("ADD_TARGET")){

            pc.setForcingMove(false);
            pc.setForcedMoveDestiny(null);

            if(message[1].equalsIgnoreCase("PLAYER")) getPhysicsComponent().setTarget(g.m.lvlMgr.getPlayer());
            else getPhysicsComponent().setTarget(g.m.lvlMgr.get_currentLvl().get_lvlEntities().get(message[1]));

            removeMovePendingInstructions();
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

        }else if(message[0].equalsIgnoreCase("FLIPX")){

            getGraphicComponent().set_flipx(!getGraphicComponent().is_flipx());
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

        }else if(message[0].equalsIgnoreCase("LOOK_RIGHT")){

            if(e.isInverseFlip()) getGraphicComponent().set_flipx(true);
            else getGraphicComponent().set_flipx(false);

            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

        }else if(message[0].equalsIgnoreCase("LOOK_LEFT")){

            if(e.isInverseFlip()) getGraphicComponent().set_flipx(false);
            else getGraphicComponent().set_flipx(true);

            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

        }else if(message[0].equalsIgnoreCase("REMOVE_TARGET")){

            getPhysicsComponent().setTarget(null);
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

        }else if(message[0].equalsIgnoreCase("ENABLE_SELECTABLE")){

            setSelectable(true);
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

        }else if(message[0].equalsIgnoreCase("DISABLE_SELECTABLE")){

            setSelectable(false);
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

        }else if (message[0].equalsIgnoreCase("NOTIFY")){
            if(message[1].equalsIgnoreCase("WHEN_JUST_IDLE")){
                pendingOKinstructions.add(str + "@" + treeID + "@" + treeNumNode);
            }
        }
        else if (message[0].equalsIgnoreCase("STOP_PHYSICS")){

            removeMovePendingInstructions();

            getPhysicsComponent().setStopPhysicsUpdate(true);
            getPhysicsComponent().setForcingMove(false);
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
        }else if(message[0].equalsIgnoreCase("ENABLE_PHYSICS")){
            getPhysicsComponent().setStopPhysicsUpdate(false);
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
        }else if(message[0].equalsIgnoreCase("SET_TAP_FORCED_MOVE_STATE")){
            getPhysicsComponent().setPreferedTapForcedMoveState(message[1]);
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
        }else if(message[0].equalsIgnoreCase("UNSET_TAP_FORCED_MOVE_STATE")){
            getPhysicsComponent().setPreferedTapForcedMoveState("");
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
        }else if(message[0].equalsIgnoreCase("SET_PREFERED_IDLE_STATE")){
            getPhysicsComponent().setPreferedIdleState(message[1]);
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
        }else if(message[0].equalsIgnoreCase("UNSET_PREFERED_IDLE_STATE")){
            getPhysicsComponent().setPreferedIdleState("");
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
        }else if(message[0].equalsIgnoreCase("DISABLE_CIRCUNSCRIPTION")){
            pc.set_currentCircuns(null);
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
        }else if (message[0].equalsIgnoreCase("CHANGE_CIRCUNSCRIPTION")) {

            removeMovePendingInstructions();

            if(message[1].equalsIgnoreCase("-1")) pc.set_currentCircuns(getID().split("_")[0]+ "_" + getID().split("_")[1] + "#" + message[1]) ;
            else pc.set_currentCircuns(getID()+ "#" + message[1]);

            Rectangle circuns = g.m.lvlMgr.get_mapManager().get_currentMap().getCircunscriptionRectangles().get(pc.get_currentCircuns());

            //REVISAR

            if(!com.mygdx.safe.CollisionManager.isCollisionWithCurrentCircunscription(this, g) || com.mygdx.safe.CollisionManager.isCollisionOutOfCurrentCircunscription(this, g)){
                if(circuns.y > getpositiony()) pc.setForcedMoveDestiny(circuns.x + (circuns.getWidth()/2), circuns.y + 0.5f);
                else pc.setForcedMoveDestiny(circuns.x + (circuns.getWidth()/2), circuns.y + circuns.getHeight() - pc.get_collisionBoundingBox().getHeight() - 0.5f);

                pc.setForcedMoveState(message[2]);
                pc.setForcingMove(true);
                pc.setChangingCircuns(true);

                pendingOKinstructions.add(str + "@" + treeID + "@" + treeNumNode);
            }else{
                pc.setForcedMoveState(message[2]);
                pc.setForcingMove(false);
                g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
            }

        }else if(message[0].equalsIgnoreCase("ENABLE")) {
            enabled = true;
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
        }
        else if(message[0].equalsIgnoreCase("DISABLE")){
            enabled = false;
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
        }else if(message[0].equalsIgnoreCase("MOVEPROG") && message.length>2){

            g.println(TAG + "   " + g.m.ggMgr.getCurrentgg().getMoveprogs().keySet().toString());

            getPhysicsComponent().set_moveProg(new ProgrammedMovement(g.m.ggMgr.getCurrentgg().getMoveprogs().get(message[1]+"#"+message[2])));
            pc.nextMoveProg();

            removeMovePendingInstructions();
            pendingOKinstructions.add(str + "@" + treeID + "@" + treeNumNode);

        }else if(message[0].equalsIgnoreCase("INTERRUPT_MOVEPROG")){

            getPhysicsComponent().set_moveProg(null);
            getPhysicsComponent().setForcingMove(false);
            getPhysicsComponent().set_fixedVelocity(null);
            g.sendIntructionOK("MOVEPROG", pendingOKinstructions, this);

            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

        }else if(message[0].equalsIgnoreCase("LOAD_CUSTOM_SKIN") && message.length>1){  //LOADS CUSTOM SKIN
            if(isSpine()) {
                for(Skin s:getGraphicComponent().get_Sanimation().get_skeleton().getData().getSkins()){
                    if(s.getName().equalsIgnoreCase(message[1])){

                        g.println("LOAD CUSTOM SKIIIIIIIIIIIN NN");

                        getGraphicComponent().get_Sanimation().set_hasCustomSkin(true);
                        getGraphicComponent().get_Sanimation().set_preCustomSkin(getGraphicComponent().get_Sanimation().get_skeleton().getSkin().getName()); // SAVE PRECUSTOM_SKIN
                        getGraphicComponent().get_Sanimation().set_customSkin(message[1]);
                        //getGraphicComponent().set_state(true);
                        getGraphicComponent().get_Sanimation().get_skeleton().setSkin(getGraphicComponent().get_Sanimation().get_customSkin());
                        break;

                    }
                }
                if(!getGraphicComponent().get_Sanimation().hasCustomSkin()){
                    g.println(TAG + " ERROR: SKELETON OF : " + getID() + " DON'T HAVE THE SKIN: " + message[1]);
                }
            }
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

        }else if(message[0].equalsIgnoreCase("UNLOAD_CUSTOM_SKIN") && message.length==1) { //UNLOADS CUSTOM SKIN; CHARGES THE LATEST SKIN
            if(isSpine()){
                if(getGraphicComponent().get_Sanimation().hasCustomSkin()){
                    getGraphicComponent().get_Sanimation().set_hasCustomSkin(false);
                    getGraphicComponent().get_Sanimation().set_customSkin(getGraphicComponent().get_Sanimation().get_preCustomSkin()); // LOADS THE PREVIOUS SAVING PRE_CUSTOM_SKIN
                    getGraphicComponent().set_state(true);

                }else{
                    g.println(TAG + " ERROR: SKELETON OF :" + getID() + "DON'T HAVE PREVIOUS CUSTOM SKIN VALUE: ");
                }
            }
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

        }

        //unchecked
        /*else if(message[0].equalsIgnoreCase("ESCAPE_FROM")){

            GameEntity escapeFrom = new GameEntity(g, g.m.gsys.getCamera());

            for(GameEntity ge : g.m.gsys.getEntities().values()){
                if(ge.getID().contains(message[1])) escapeFrom = ge;
            }

            Vector2 forcedDestiny = pc.getPossibleDestiny(this, Integer.valueOf(message[2]));

            if(getID().contains("PLAYER"))g.println("*****" +  g.m.lvlMgr.getPlayer().getposition() + "     " + forcedDestiny);

            pc.setForcedMoveDestiny(forcedDestiny.x, forcedDestiny.y);
            pc.setForcedMoveState("RUN");
            pc.setForcingMove(true);

            pendingOKinstructions.add(str + "%" + treeID + "%" + treeNumNode);
        } */
        else if(message[0].equalsIgnoreCase("CHANGE_STATE_DIRECTION") && message.length>2){

            if(pc.get_states().contains(message[1],false)){

                boolean changestate=getPhysicsComponent().setState(message[1]);
                boolean changedirection=getPhysicsComponent().setDirection(message[2]);

                pc.setForcedMoveState(null);
                getGraphicComponent().set_state(changestate);
                getGraphicComponent().set_direction(changedirection);

                // CASE SPINE
                if(isSpine && message.length>3) {
                    getAiComponent().setGraphControllerAnim(true);
                    if (message[3].equalsIgnoreCase("LOOP") && message.length > 4) {
                        getGraphicComponent().setLoopLimit(Integer.valueOf(message[4]));
                    } else {
                        getGraphicComponent().setLoopLimit(-1);
                    }

                    pendingOKinstructions.add(str + "@" + treeID + "@" + treeNumNode);
                } // CASE NANIMATION
                else{
                    g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
                }
            }
        }

        else if(message[0].equalsIgnoreCase("CONTROL_ANIM") && message.length>1){
            if(Boolean.valueOf(message[1].toLowerCase())) getAiComponent().setGraphControllerAnim(true);
            else getAiComponent().setGraphControllerAnim(false);
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
        }
        else if(message[0].equalsIgnoreCase("ENABLE_COLLISIONS")) {

            getPhysicsComponent().enableBoundingBoxes();
            g.println(TAG+ " : ["+ getID()+ "] : COLLISIONS ON");
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
        }
        else if(message[0].equalsIgnoreCase("DISABLE_COLLISIONS")) {

            getPhysicsComponent().disableBoundingBoxes();
            g.println(TAG+ " : ["+ getID()+"] : COLLISIONS OFF");
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
        }
        /*else if(message[0].equalsIgnoreCase("PENFROID_MOVE")){

            String direction = g.OppossitePenfroidRose(this, g.m.lvlMgr.getPlayer());
            pc.setForcedMoveDestiny(pc.getDestinyForMovement(direction, Integer.valueOf(message[1])));
            pc.setForcedMoveState(message[2]);
            pc.setForcingMove(true);


            g.println(TAG + "ID:" + getID()+ " TO HERE : DIRECTION = [" + direction +"] M1: " + message[1]
                       + " M2: " +message[2] + " VECTOR TO MOVE= " + pc.getDestinyForMovement(direction, Integer.valueOf(message[1]))
                       + "ORIGIN OF MOVE: " + pc.getGeCurrentPos() + pc.get_currentDirection() + pc.get_currentState()
                      );


            pendingOKinstructions.add(str + "%" + treeID + "%" + treeNumNode);
        }*/
        else if(message[0].equalsIgnoreCase("LOAD_ATTACH") && message.length>2){

            g.println(getGraphicComponent().get_Sanimation().get_skeleton().getSlots().get(49).getAttachment().toString());


                getGraphicComponent().get_Sanimation().get_skeleton().setAttachment(message[1],message[2]);
                g.println(TAG + " LOADING ON SLOT: "+ message[1] +" THE ATTACH: " + message[2]);

            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

        }
        else if(message[0].equalsIgnoreCase("UNLOAD_ATTACH") && message.length>1){

            if(isSpine() && getGraphicComponent().get_Sanimation().get_skeleton().findSlot(message[1])!=null){
                getGraphicComponent().get_Sanimation().get_skeleton().findSlot(message[1]);
                getGraphicComponent().get_Sanimation().get_skeleton().setAttachment(message[1],null);
                g.println(TAG + " UNLOADING THE CURRENT ATTACH ON SLOT: "+ message[1]);
            }
            else{
                g.println(TAG + " ERROR: NOT SPINE OR NOT SLOT FOUND :" + message[1]);
            }
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

        }
        else if(message[0].equalsIgnoreCase("SPECIAL_ENTITY")){

            getAiComponent().setSpecialEntity(Boolean.valueOf(message[1]));
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

        }

    }

    /*_______________________________________________________________________________________________________________*/

    public void fillProximityRelations(com.mygdx.safe.MainGameGraph.Conditions c){

        for(String shooter: c._shooters.keySet()){

            if(shooter.contains("SHOOTER#PROXIMITY") && shooter.contains("START")){
                String entityName = shooter.split("#")[2];

                if(entityName.equalsIgnoreCase("PLAYER"))proximityRelations.add(new com.mygdx.safe.Pair<Integer, Boolean>(g.m.lvlMgr.getPlayer().NumId, false));
                else proximityRelations.add(new com.mygdx.safe.Pair<Integer, Boolean>(g.m.lvlMgr.get_currentLvl().get_lvlEntities().get(entityName).NumId, false));
            }
        }
    }

    public void removeMovePendingInstructions(){

        //pc.setForcedMoveState(null);

        if(g.findInstruction("MOVE_TO", pendingOKinstructions)) g.removeInstructionType("MOVE_TO", pendingOKinstructions, this);
        else if(g.findInstruction("MOVEPROG", pendingOKinstructions)) g.removeInstructionType("MOVEPROG", pendingOKinstructions, this);
        else if(g.findInstruction("CHANGE_CIRCUNSCRIPTION", pendingOKinstructions)) g.removeInstructionType("CHANGE_CIRCUNSCRIPTION", pendingOKinstructions, this);
    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS

    public com.mygdx.safe.EntityConfig getEntityConfig() {
        return e;
    }

    public boolean isSetEntityConfig() {
        return setEntityConfig;
    }

    public com.mygdx.safe.Components.InputComponent getInputComponent() {  return ic;  }

    public com.mygdx.safe.Components.PhysicsComponent getPhysicsComponent() { return pc;   }

    public com.mygdx.safe.Components.GraphicsComponent getGraphicComponent() {  return gc;  }

    public com.mygdx.safe.Components.AIComponent getAiComponent() { return ai;  }

    public boolean isSpine() {  return isSpine; }

    public boolean isHasNanimation() {  return hasNanimation;   }

    public String getID() {
        return ID;
    }

    public GameEntity getCollisionGE() {
        return collisionGE;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public int getCONTROLAUTOMATIC() {     return CONTROLAUTOMATIC;  }

    public int getSecondsForAutomaticControl() {    return secondsForAutomaticControl;  }

    public boolean isTicksForInputControlEntity() {   return ticksForInputControlEntity;  }

    public float getpositionx(){ return pc.getGeCurrentPos().x; }

    public float getpositiony(){ return pc.getGeCurrentPos().y; }

    public Vector2 getposition(){ return pc.getGeCurrentPos(); }

    public Vector2 getVelocity(){ return pc.get_velocity(); }

    public boolean isSelect() {   return select;  }

    public boolean isEnabled() {
        return enabled;
    }

    public ArrayList<String> getPendingOKinstructions() {
        return pendingOKinstructions;
    }

    public Array<com.mygdx.safe.Pair<Integer, Boolean>> getProximityRelations() {
        return proximityRelations;
    }

    public boolean isSelectable() {
        return isSelectable;
    }

    public static int getCountId() {
        return CountId;
    }

    //SETTERS

    public void setEntityConfig(com.mygdx.safe.EntityConfig e){   this.e=e; setEntityConfig=true; }

    public void setSpine(boolean spine) {   isSpine = spine;    }

    public void setHasNanimation(boolean hasNanimation) {   this.hasNanimation = hasNanimation; }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setCollisionGE(GameEntity collisionGE) {
        this.collisionGE = collisionGE;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;}

    public void setSecondsForAutomaticControl(int secondsForAutomaticControl) { this.secondsForAutomaticControl = secondsForAutomaticControl;   }

    public void setCONTROLAUTOMATIC(int CONTROLAUTOMATIC) {  this.CONTROLAUTOMATIC = CONTROLAUTOMATIC;  }

    public void setTicksForInputControlEntity(boolean ticksForInputControlEntity) {  this.ticksForInputControlEntity = ticksForInputControlEntity; }

    public void setVelocity(float x,float y){ pc.set_velocity(x,y);}

    public void setVelocity(Vector2 velocity){pc.set_velocity(velocity);}

    public void setSelect(boolean select) {    this.select = select;  }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setProximityRelations(Array<com.mygdx.safe.Pair<Integer, Boolean>> proximityRelations) {
        this.proximityRelations = proximityRelations;
    }

    public void setSelectable(boolean selectable) {
        isSelectable = selectable;
    }

    public static void setCountId(int countId) {
        CountId = countId;
    }

    //TO STRING
    public String toString(){
        return this.getID();
    }

}
