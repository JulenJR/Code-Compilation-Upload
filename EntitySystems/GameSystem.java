package com.mygdx.safe.EntitySystems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.mygdx.safe.CollisionManager;
import com.mygdx.safe.Components.InputComponent;

import com.mygdx.safe.Components.PhysicsComponent;
import com.mygdx.safe.Conversation.TextActor;
import com.mygdx.safe.Entities.GEActon;
import com.mygdx.safe.Entities.GameEntity;
import com.mygdx.safe.EntityConfig;
import com.mygdx.safe.GCnAnimation;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;
import com.mygdx.safe.InputProcessors.SpritemationListener;
import com.mygdx.safe.MainGameGraph.ProgrammedMovement;
import com.mygdx.safe.MainGameGraph.Timer;
import com.mygdx.safe.Map;
import com.mygdx.safe.MapManager;
import com.mygdx.safe.SpritemationEntityConfig;
import com.mygdx.safe.nCache;
import com.mygdx.safe.sPritemation;
import com.mygdx.safe.sPritemationsHost;
import com.mygdx.safe.screens.MainGameScreen;
import com.mygdx.safe.screens.ProfileScren;

import java.awt.Menu;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by Boris Escajadillo Díaz on 8/06/17.
 */

public class GameSystem extends EntitySystem {

    //TAG
    private static final String TAG = GameSystem.class.getSimpleName();

    //ASPECTS
    GenericMethodsInputProcessor g;

    //CAMERA
    private OrthographicCamera camera;
    private Vector2 cameraCenter = new Vector2();
    private float previouscamerazoom;


    private boolean isCameraMoving;
    private float cameraMovementTime;
    private float cameraMovementX;
    private float cameraMovementY;

    private float velocityCamMovX;

    //MAP MANAGER
    private MapManager mpmgr;

    //RENDERERS
    private ShapeRenderer srenderer;
    private PolygonSpriteBatch polspritbatch;
    private SkeletonRenderer skelrender;
    private OrthoCachedTiledMapRenderer _maprenderer;

    //DELTA VALUES

    private float controldelta=0;
    private float physicControldelta=0;


    //LAYERS
    private int[] _layersFirstRenderer;
    private int[] _layersGhostRenderer;
    private int[] _layersLastRenderer;

    //ENTITIES
    private Array<GameEntity> entities;
    private GameEntity portal = null;
    private GameEntity halo = null;
    private GEActon acton = null;

    //BOOLEAN
    private boolean allRectangleRendererActivator=false;
    private boolean tickForInputControl=false;
    private boolean tickForDeltaControl=false;
    private boolean dialogMode = false;
    private boolean dialogModeChanging = false;

    //NCACHES
    private HashMap<String, nCache> _nanimationArray;
    ParticleSystem p_system;

    //CHANGE LEVEL
    private String _changeLevel= "";

    //INSTRUCTIONS PENDING FOR OK
    private ArrayList<String> pendingOKinstructions = new ArrayList<String>();

    //SPRITEMATIONS
    public sPritemationsHost spritemationsHost;



    /*_______________________________________________________________________________________________________________*/

    //STATIC METHODS
    static public String[] getOrderMessage(String[] fullMessage, int receiverInfoSize){

        String orderMessage[] = new String[fullMessage.length - receiverInfoSize];
        for(int i=0; i<orderMessage.length; i++) orderMessage[i] = fullMessage[i+receiverInfoSize]; //fullMessage[receiverInfoSize]; //COMMENT BY BORIS: FALTAVA i+, para que coja desde el indice
        //receiverInfoSize hasta que su final
        return orderMessage;
    }

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public GameSystem(GenericMethodsInputProcessor g){
        this.g = g;
        spritemationsHost = new sPritemationsHost(g);

        srenderer=g.getShapeRenderer(); // RENDERER FOR MAP RECTANGLECOLISSION AND BOUNDINGBOXES
        srenderer.setAutoShapeType(true);

        entities=new Array<GameEntity>();
        _nanimationArray=new HashMap<String, nCache>();
        polspritbatch=g.getPolspritbatch();
        skelrender=g.getSkelrender();
        spritemationsHost.config();

    }
    // PARTICLE CONFIG
    public void ParticleConfig(){
        p_system = g.m.lvlMgr.getP_system();
        p_system.createParticles();
        p_system.createParticlesWithEntities(this);
        p_system.setParticlePositions();
    }

    //UPDATE
    public void update(float delta) {


        controldelta += delta;

        if (controldelta > 1) {
            tickForInputControl = true;
            tickForDeltaControl = true;
            controldelta = 0;

        }



            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            _maprenderer.setView(camera);
            _maprenderer.getSpriteCache().setColor(0.5f, 0.5f, 0.5f, 1.0f);
            mapRenderer(_maprenderer, _layersFirstRenderer);
            mapRenderer(_maprenderer, _layersGhostRenderer);

            //TIMERS
            if(g.m.ggMgr.getCurrentgg().getTimers().size() > 0) {
                for (Timer t : g.m.ggMgr.getCurrentgg().getTimers().values()) {
                    t.update(delta);
                }
            }

            //ACTUALIZE THE ENTITIES COMPONENTS
            //if (entities!=null) {
                polspritbatch.begin();

                //SPRITEMATION SYSTEM RENDERING

                spritemationsHost.update(polspritbatch, delta);

                //UPDATE ALL THE ENTITIES EXCEPT THE LASTS...THE HALO ENTITY & ACTON ENTITY
                for (int i=0; i<entities.size; i++) {

                    GameEntity currentEntity = entities.get(i);

                    //UPDATE COMPONENTS
                    if(currentEntity.isEnabled()) updateGameEntity(camera, currentEntity, delta, mpmgr);
                    //Renderize the halo
                    /*
                    if(currentEntity.isSelectable() && (g.m.lvlMgr.get_currentLvl().getSelectedGE() == null || currentEntity.isSelected())){
                        if(g.m.lvlMgr.get_currentLvl().getSelectedGE() !=null){
                            currentEntity.setSelected(g.m.lvlMgr.get_currentLvl().getSelectedGE().NumId==currentEntity.NumId);
                        }

                        boolean b=false;
                        float skeletonScale = currentEntity.getEntityConfig().get_sanimationConfig().skeletonScale;
                        for(GCnAnimation gcNanIm:halo.getGraphicComponent().get_GCnAnim().values()){
                            b=!b;
                            renderHaloNanimation("", gcNanIm.get_sprite(), skeletonScale, halo, currentEntity, b);
                        }
                    }*/

                    //RENDERIZE ENTITIES (SANIMATIONS // NANIMATIONS)
                    if (currentEntity.isSpine() && currentEntity.isEnabled()) {
                        //currentEntity.getGraphicComponent().get_Sanimation().get_skeleton().setPosition(currentEntity.getpositionx(), currentEntity.getpositiony());
                        skelrender.draw(polspritbatch, currentEntity.getGraphicComponent().get_Sanimation().get_skeleton());

                        // ACTUALIZING TEXT ACTOR POSITION
                        g.getTextactors().get(currentEntity.getID()).setTextActorPosition(currentEntity.getposition());
                    }

                    if (currentEntity.isHasNanimation() && currentEntity.isEnabled()) {

                        float nAnimPositionx,nAnimPositiony;
                        Sprite sprite;
                        for(EntityConfig.nAnimationTriConfig nAnimTri:currentEntity.getEntityConfig().get_nAnimationTriConfigs().values()){

                            nAnimPositionx= currentEntity.getpositionx() + nAnimTri._masterDistance.x;
                            nAnimPositiony= currentEntity.getpositiony() + nAnimTri._masterDistance.y;

                            sprite = currentEntity.getGraphicComponent().get_GCnAnim().get(nAnimTri._nAnimationConfig.nAnimationName).get_sprite();

                            if(currentEntity.getEntityConfig().get_nAnimationTriConfigs().get(nAnimTri._nAnimationConfig.nAnimationName).get_nAnimationConfig().nScale == -1){

                                polspritbatch.draw(sprite, nAnimPositionx, nAnimPositiony, sprite.getOriginX(), sprite.getOriginY(),
                                        sprite.getWidth(), sprite.getHeight(),
                                        nAnimTri.get_nAnimationConfig().xScale,
                                        nAnimTri.get_nAnimationConfig().yScale, 0f);
                            }
                            else{
                                polspritbatch.draw(sprite, nAnimPositionx, nAnimPositiony, sprite.getOriginX(), sprite.getOriginY(),
                                        sprite.getWidth(), sprite.getHeight(),
                                        currentEntity.getEntityConfig().get_nAnimationTriConfigs().get(nAnimTri._nAnimationConfig.nAnimationName).get_nAnimationConfig().nScale,
                                        currentEntity.getEntityConfig().get_nAnimationTriConfigs().get(nAnimTri._nAnimationConfig.nAnimationName).get_nAnimationConfig().nScale,  0f);
                            }
                        }
                    }
                    //RENDERING SPRITEMATIONS CLIENT
                    currentEntity.sclient.updateClientSpritemations(polspritbatch, delta);

                }

                // CAMERA UPDATE
                updateCameraPosition(delta);

                //PARTICLE SYSTEM RENDEREING
                p_system.particleEntityPosition(this, polspritbatch);
                p_system.particleUpdate(delta);
                p_system.particleRender(polspritbatch, delta);


        if(p_system.getParticle("star-beauty").getPosition().x == 81.0f)p_system.quitParticle(p_system.getParticle("star-beauty"));


                polspritbatch.getProjectionMatrix().set(camera.combined);
                polspritbatch.end();
            //}



            mapRenderer(_maprenderer, _layersLastRenderer);

            g.updateprocessor();


            // COLLISION RECTANGLE RENDERER

            srenderer.begin();
            srenderer.setProjectionMatrix(camera.combined);
            srenderer.setColor(0, 0, 1, 0);

            if (allRectangleRendererActivator) {
                Rectangle bbox;

                // ENTITIES BOUNDINGBOXES & CIRCUNSCRIPTION
                for (GameEntity ge : entities) {

                    if(ge.getPhysicsComponent().get_currentCircuns() !=null){
                        Rectangle circuns = g.m.lvlMgr.get_mapManager().get_currentMap().getCircunscriptionRectangles().get(ge.getPhysicsComponent().get_currentCircuns());
                        srenderer.rect(circuns.x, circuns.y, circuns.width, circuns.height);
                    }

                    srenderer.rect(ge.getPhysicsComponent().get_collisionBoundingBox().x, ge.getPhysicsComponent().get_collisionBoundingBox().y,
                            ge.getPhysicsComponent().get_collisionBoundingBox().width, ge.getPhysicsComponent().get_collisionBoundingBox().height);

                    srenderer.rect(ge.getPhysicsComponent().get_fullBodyBoundingBox().x, ge.getPhysicsComponent().get_fullBodyBoundingBox().y,
                            ge.getPhysicsComponent().get_fullBodyBoundingBox().width, ge.getPhysicsComponent().get_fullBodyBoundingBox().height);

                }

                // MAP RECTANGLES COLLISION
                for (Rectangle bobox : mpmgr.getCollisionRectangles()) {
                    srenderer.setColor(1, 0, 0, 0);
                    srenderer.rect(bobox.x, bobox.y, bobox.width, bobox.height);
                }

                //RENDER CIRCLES
                Circle c = g.m.lvlMgr.getPlayer().getPhysicsComponent().getProximityArea();

                srenderer.setColor(0, 1, 0, 0);
                srenderer.circle(c.x, c.y, c.radius, 100);

                for(GameEntity ge: entities){
                    Circle circle = ge.getPhysicsComponent().getProximityArea();

                    srenderer.setColor(0, 1, 0, 0);
                    srenderer.circle(circle.x, circle.y, circle.radius, 100);
                }

            }
            // RECTANGLES FOR TEXTACTORS
            for (TextActor t: g.getTextactors().values()) {


                    srenderer.setColor(t.getBackground());
                    if (t.isFilledShape())
                        srenderer.set(ShapeRenderer.ShapeType.Filled);
                    else srenderer.set(ShapeRenderer.ShapeType.Line);
                    float x, y, width, height, scale;

                    if (t.isHUD() && t.getName().equalsIgnoreCase("HUD")) {
                        t.setText(" POINTS: [" + g.m.he.getEMO() + "] ");
                    }else if(t.isHUD()){
                        t.setText(t.getText());

                    }
                    else if(!(t.getName().equalsIgnoreCase("TALKING_ENTITY") || t.getName().contains("DIALOG_"))) {
                        x = t.getPosition().x;
                        y = t.getPosition().y;

                            if (allRectangleRendererActivator) {
                                t.setText(t.getName() + "\nX= " + String.format("%.2f", x) + ",Y= " + String.format("%.2f", y));
                                scale = Map.UNIT_SCALE;
                                width = t.getGlplayout().width;
                                height = t.getGlplayout().height;
                                //srenderer.set(ShapeRenderer.ShapeType.Filled);
                                srenderer.rect(x - width * scale / 6,
                                        y - height * scale * 5 / 6,
                                        width * scale,
                                        height * scale);
                            }
                            else t.setText("");



                    }

            }

            srenderer.end();



            //IF EXIST A ENTITY SELECTED AND THE FORCED MOVE OF THE PLAYER HAS STOPPED...DIALOG MODE ON
            if (g.m.lvlMgr.get_currentLvl().getSelectedGE() != null && (!g.m.lvlMgr.get_currentLvl().getSelectedGE().getID().contains("ITEM_")) &&
                    !g.m.lvlMgr.getPlayer().getPhysicsComponent().isForcingMove() && !dialogMode) {

                g.gm.sendMessage("GAMEGRAPH#SELECT#START#ENTITY#" + g.m.lvlMgr.get_currentLvl().getSelectedGE().getID(), null, -1);
            }

            if (tickForInputControl) {
                tickForInputControl = false;
            }


    }




    //UPDATE GAME ENTITY
    void updateGameEntity (Camera camera, GameEntity gameEntity,float delta, MapManager mpmgr){

        if (tickForInputControl) { gameEntity.setTicksForInputControlEntity(true);}

        updateInput(gameEntity, delta);
        updatePhysics(gameEntity, delta, mpmgr);
        updateAI(gameEntity, delta);
        updateEntityGraphics(gameEntity, delta);



    }

    //UPDATE INPUT
    void updateInput (GameEntity ge,float delta){

        ge.getInputComponent().ProcessInput(ge);


            if (ge.getSecondsForAutomaticControl() == ge.getCONTROLAUTOMATIC() && ge.getInputComponent().getI() == InputComponent.InputMethod.AUTOMATIC) {
                ge.setSecondsForAutomaticControl(0);
                ge.getInputComponent().setCHANGEAUTOMATIC(true);
            }


        // COORDINATED TICK FOR CHANGES PHYSICS COMPONENT DIRECTION STATE TO RUN
        if (ge.isTicksForInputControlEntity()) {

            if (!(ge.getInputComponent().isCHANGE_DIRECTION_FOR_PHYSICSDETECTION())) {
                ge.getPhysicsComponent().increase__secondsFORDIRECTION();

            } else {
                ge.getPhysicsComponent().set_secondsFORDIRECTION(0);
                ge.getInputComponent().setCHANGE_DIRECTION_FOR_PHYSICSDETECTION(false);
            }

            ge.setSecondsForAutomaticControl(ge.getSecondsForAutomaticControl() + 1);
            ge.setTicksForInputControlEntity(false);
        }


    }

    //UPDATE PHYSICS
    void updatePhysics (GameEntity ge,float delta, MapManager mpmgr){ //CHECK

        //UPDATE PHYSICS WHEN THE ENTITY IS WORKING NORMALLY

        if(ge.getPhysicsComponent().get_moveProg()!= null) moveProgUpdatePhysics(ge,delta);
        else if (!ge.getPhysicsComponent().isStopPhysicsUpdate() && !ge.getAiComponent().is_changingDirection() && !dialogMode) normallyUpdatePhysics(ge,delta);
        else if (dialogMode){
            if(g.m.lvlMgr.get_currentLvl().getSelectedGE() != null){
                if(ge.NumId==g.m.lvlMgr.get_currentLvl().getSelectedGE().NumId || ge.getInputComponent().getI()== InputComponent.InputMethod.ALL_INPUTS)
                    dialogMOdeUpdatePhysics(ge);
            } else if(ge.getInputComponent().getI()== InputComponent.InputMethod.ALL_INPUTS){
                dialogMOdeUpdatePhysics(ge);
            }

        }

        if (ge.isSpine())
                ge.getGraphicComponent().get_Sanimation().get_skeleton().setPosition(ge.getpositionx(), ge.getpositiony());

        //if(ge.getInputComponent().getI() == InputComponent.InputMethod.ALL_INPUTS) updateCameraPosition(ge);

    }

    //MOVE PROG UPDATE PHYSICS
    public void moveProgUpdatePhysics(GameEntity ge,float delta){


        //g.println(TAG + "**************ENTRAAA");

        PhysicsComponent pc = ge.getPhysicsComponent();
        ProgrammedMovement moveProg = pc.get_moveProg();

        Vector2 movement = new Vector2(0, 0);
        Vector2 nextPosition = new Vector2(0, 0);
        int movNumber;
        boolean noMove = true;


        //GET NEXT STEP AT THE PROGRAMMED MOVE
        if (!pc.isForcingMove()){
            pc.nextMoveProg();


        }
        //CALCULATE THE NEW POSITION WHEN THE STEP HAS NOT FINISHED YET
        else{
            movNumber = ge.getInputComponent().getMovementNumber();

            //CHECK IF IS POSSIBLE THE MOVEMENT
            if (movNumber >= 0) {
                noMove = checkPossibleMovement(ge, nextPosition, movement,delta);
            }

            if (!noMove) {
                //g.println(TAG + "CASE NO MOVE");

                pc.set_currentEntityposition(nextPosition);
                ge.getAiComponent().programStateDirection(ge.getEntityConfig(), ge.getInputComponent().isUP(), ge.getInputComponent().isDOWN(),
                        ge.getInputComponent().isLEFT(), ge.getInputComponent().isRIGHT(), ge.getGraphicComponent(), pc);

                if(Math.abs(pc.getGeCurrentPos().x - pc.getForcedMoveDestiny().x)<0.001 && Math.abs(pc.getGeCurrentPos().y - pc.getForcedMoveDestiny().y)<0.001){

                    g.println(TAG + "    " + "no move by space" + pc.getForcedMoveDestiny());
                    moveProg.stepOK();
                    pc.setForcingMove(false);
                }

            } else {
                g.println(TAG + "  ******************PUREMNAGIC");
                //MOVEPROG AUTO-PuRE MaGIC TELEPORTATION IF noMOVE:

                //pure magic (bug is niccceeee)

                pc.set_currentEntityposition(0 + pc.getForcedMoveDestiny().x, 0 + pc.getForcedMoveDestiny().y);
                pc.setForcingMove(false);
                pc.set_moveProg(null);
                g.findInstruction("MOVEPROG", ge.getPendingOKinstructions());

                //pc.setForcingMove(false);
                //pc.set_moveProg(null);

                if (ge.getInputComponent().getI()== InputComponent.InputMethod.ALL_INPUTS) g.setUp(false); g.setDown(false); g.setRight(false); g.setLeft(false); /////'????????
            }
        }

        //SETTING ENTITY ON COORDINATES AT STAGE IN SKELETON IF ENTITY IS SPINE
        //if (ge.isSpine())
        //    ge.getGraphicComponent().get_Sanimation().get_skeleton().setPosition(ge.getpositionx(), ge.getpositiony());

        if(ge.isSpine()){
            //ACTUALIZE PROXIMITY AREA
            ge.getPhysicsComponent().getProximityArea().setPosition(ge.getpositionx(),
                    ge.getpositiony() + ((ge.getGraphicComponent().get_Sanimation().get_skeleton().getData().getHeight() / 2) * ge.getEntityConfig().get_sanimationConfig().skeletonScale / 64));
        }

        //CHECK PROXIMITY WITH OTHER ENTITIES
        CollisionManager.checkCollisionProximityArea(ge, g);

        if(ge.getInputComponent().getI()== InputComponent.InputMethod.ALL_INPUTS){
            CollisionManager.isCollisionWithPortal(mpmgr.getPortalRectangles(), g);
            CollisionManager.isCollisionWithEventRectangle(mpmgr.getPortalRectangles(), g);
        }

        /*if(ge.getPhysicsComponent().get_boundingBoxes().size() > 0)
            ge.getPhysicsComponent().set_eventArea(
                    ge.getPhysicsComponent().get_boundingBoxes().get(ge.getPhysicsComponent().get_boundingBoxes().keySet().toArray()[0]));*/

    }


    //NORMALLY UPDATE PHYSICS
    public void normallyUpdatePhysics (GameEntity ge,float delta){

        boolean noMove;
        int movNumber;
        Vector2 movement = new Vector2(0, 0);
        Vector2 nextPosition = new Vector2(0, 0);

        PhysicsComponent pc = ge.getPhysicsComponent();

        //GET ASSOCIATED NUMBER TO A DETERMINATE MOVEMENT
        movNumber = ge.getInputComponent().getMovementNumber();if (ge.isSpine())
            ge.getGraphicComponent().get_Sanimation().get_skeleton().setPosition(ge.getpositionx(), ge.getpositiony());


        //CHECK IF IS POSSIBLE THE MOVEMENT AND IF NOT...CHECK IF ARE POSSIBLE THE MOST TWO SIMILAR MOVEMENTS
        if (movNumber >= 0) {

            noMove = checkPossibleMovement(ge, nextPosition, movement,delta);

            if (noMove) {
                ge.getInputComponent().getMovement((movNumber + 1) % 8);
                noMove = checkPossibleMovement(ge, nextPosition, movement,delta);

            }
            if (noMove) {
                ge.getInputComponent().getMovement((movNumber + 8 - 1) % 8);
                noMove = checkPossibleMovement(ge, nextPosition, movement,delta);

            }

            //ACTUALIZE POSITION AND STATE WHEN THE ENTITY HAVE A POSSIBLE MOVEMENT
            if (!noMove) {

                pc.set_currentEntityposition(nextPosition);
                ge.getAiComponent().programStateDirection(ge.getEntityConfig(), ge.getInputComponent().isUP(), ge.getInputComponent().isDOWN(),
                        ge.getInputComponent().isLEFT(), ge.getInputComponent().isRIGHT(), ge.getGraphicComponent(), pc);

                if(pc.isForcingMove() && pc.getGeCurrentPos().x==pc.getForcedMoveDestiny().x && pc.getGeCurrentPos().y==pc.getForcedMoveDestiny().y){

                    pc.setForcingMove(false);
                    pc.setForcedMoveState(null);

                    if(g.findInstruction("MOVE_TO", ge.getPendingOKinstructions()))g.sendIntructionOK("MOVE_TO", ge.getPendingOKinstructions(),ge);
                    else g.sendIntructionOK("CHANGE_CIRCUNSCRIPTION", ge.getPendingOKinstructions(),ge);

                }



            } else {

                if (ge.getInputComponent().getI()== InputComponent.InputMethod.ALL_INPUTS) {

                    if(pc.isForcingMove() && g.findInstruction("MOVE_TO", ge.getPendingOKinstructions())){

                        ge.getPhysicsComponent().setBoundsAfterMovement(0 + pc.getForcedMoveDestiny().x, 0 + pc.getForcedMoveDestiny().y);
                        pc.set_currentEntityposition(0 + pc.getForcedMoveDestiny().x, 0 + pc.getForcedMoveDestiny().y);
                        pc.setForcingMove(false);
                        pc.setForcedMoveState(null);

                        if(!ge.getPhysicsComponent().getPreferedIdleState().equalsIgnoreCase("")){
                            ge.getPhysicsComponent().set_currentState(ge.getPhysicsComponent().getPreferedIdleState());
                        }
                        else ge.getPhysicsComponent().set_currentState("IDLE");

                        g.sendIntructionOK("MOVE_TO", ge.getPendingOKinstructions(), ge);

                    }
                    else{
                        pc.setForcingMove(false);
                        pc.setForcedMoveState(null);

                        g.setUp(false); g.setDown(false); g.setRight(false); g.setLeft(false);

                        if(!ge.getPhysicsComponent().getPreferedIdleState().equalsIgnoreCase("")){
                            ge.getPhysicsComponent().set_currentState(ge.getPhysicsComponent().getPreferedIdleState());
                        }
                        else ge.getPhysicsComponent().set_currentState("IDLE");

                        g.sendIntructionOK("MOVE_TO", ge.getPendingOKinstructions(),ge);


                        if (pc.isForcingMove() && g.m.lvlMgr.get_currentLvl().getSelectedGE() != null)
                            ge.getAiComponent().programStateDirection(ge.getEntityConfig(), ge.getInputComponent().isUP(), ge.getInputComponent().isDOWN(),
                                    ge.getInputComponent().isLEFT(), ge.getInputComponent().isRIGHT(), ge.getGraphicComponent(), pc);
                    }
                }else if(pc.isForcingMove()){
                    //pure magic (bug is niccceeee)
                    ge.getPhysicsComponent().setBoundsAfterMovement(0 + pc.getForcedMoveDestiny().x, 0 + pc.getForcedMoveDestiny().y);
                    pc.set_currentEntityposition(0 + pc.getForcedMoveDestiny().x, 0 + pc.getForcedMoveDestiny().y);
                    pc.setForcingMove(false);
                    pc.setForcedMoveState(null);


                    if(g.findInstruction("MOVE_TO", ge.getPendingOKinstructions()))g.sendIntructionOK("MOVE_TO", ge.getPendingOKinstructions(),ge);
                    else g.sendIntructionOK("CHANGE_CIRCUNSCRIPTION", ge.getPendingOKinstructions(), ge);
                }
            }
        } else {

            ge.getAiComponent().programStateDirection(ge.getEntityConfig(),
                    ge.getInputComponent().isUP(), ge.getInputComponent().isDOWN(),
                    ge.getInputComponent().isLEFT(), ge.getInputComponent().isRIGHT(),
                    ge.getGraphicComponent(), pc
            );

            if(g.findInstruction("MOVE_TO", ge.getPendingOKinstructions()))g.sendIntructionOK("MOVE_TO", ge.getPendingOKinstructions(), ge);
            else g.sendIntructionOK("CHANGE_CIRCUNSCRIPTION", ge.getPendingOKinstructions(), ge);
        }

        //SETTING ENTITY ON COORDINATES AT STAGE IN SKELETON IF ENTITY IS SPINE
        //if (ge.isSpine())
        //    ge.getGraphicComponent().get_Sanimation().get_skeleton().setPosition(ge.getpositionx(), ge.getpositiony());

        if(ge.isSpine()){
            //ACTUALIZE PROXIMITY AREA
            ge.getPhysicsComponent().getProximityArea().setPosition(ge.getpositionx(),
                    ge.getpositiony() + ((ge.getGraphicComponent().get_Sanimation().get_skeleton().getData().getHeight() / 2) * ge.getEntityConfig().get_sanimationConfig().skeletonScale / 64));
        }

        //CHECK PROXIMITY WITH OTHER ENTITIES
        CollisionManager.checkCollisionProximityArea(ge, g);

        if(ge.getInputComponent().getI()== InputComponent.InputMethod.ALL_INPUTS){
            CollisionManager.isCollisionWithPortal(mpmgr.getPortalRectangles(), g);
            CollisionManager.isCollisionWithEventRectangle(mpmgr.getPortalRectangles(), g);
        }

        //if(pc.get_boundingBoxes().size() > 0) pc.set_eventArea(pc.get_boundingBoxes().get(pc.get_boundingBoxes().keySet().toArray()[0]));
    }

    //DIALOG MODE UPDATE PHYSICS
    public void dialogMOdeUpdatePhysics (GameEntity ge){

        ge.getInputComponent().setUP(false);
        ge.getInputComponent().setDOWN(false);
        ge.getInputComponent().setRIGHT(false);
        ge.getInputComponent().setLEFT(false);

        ge.getAiComponent().programStateDirection(ge.getEntityConfig(), ge.getInputComponent().isUP(), ge.getInputComponent().isDOWN(),
                ge.getInputComponent().isLEFT(), ge.getInputComponent().isRIGHT(), ge.getGraphicComponent(), ge.getPhysicsComponent());

        /*if(g.m.lvlMgr.get_currentLvl().getSelectedGE() !=null)
            if(ge.getID().contains(g.m.lvlMgr.get_currentLvl().getSelectedGE().getID())) updateCameraPosition(ge);
        else updateCameraPosition(ge);*/

    }

    //UPDATE CAMERA POSITION
    /*
    public void updateCameraPosition (GameEntity ge){

            // CENTERING CAMERA ON PLAYER ENTITY

            if (g.m.lvlMgr.getPlayer().getpositionx() + MainGameScreen.VIEWPORT.viewportWidth / 2 < mpmgr.getMAP_WIDHT() &&
                    g.m.lvlMgr.getPlayer().getpositionx() - (MainGameScreen.VIEWPORT.viewportWidth / 2) > 0)

                cameraCenter.x = ge.getpositionx();

            if (g.m.lvlMgr.getPlayer().getpositiony() + MainGameScreen.VIEWPORT.viewportHeight / 2 < mpmgr.getMAP_HEIGHT() &&
                    g.m.lvlMgr.getPlayer().getpositiony() - (MainGameScreen.VIEWPORT.viewportHeight / 2) > 0)

                cameraCenter.y = ge.getpositiony() + ge.getPhysicsComponent().get_collisionBoundingBox().getHeight() / 2;

            camera.position.set(cameraCenter, 0f);



        camera.update();
    }
    */

    public void updateCameraPosition (float delta){

        float xMov;
        float yMov;

        if(isCameraMoving){

            if(delta > cameraMovementTime){
                xMov = cameraMovementX;
                yMov = cameraMovementY;

                cameraMovementX = 0;
                cameraMovementY = 0;
                cameraMovementTime = 0;
                isCameraMoving = false;


            }else{
                xMov = cameraMovementX * (delta/cameraMovementTime);
                yMov = cameraMovementY * (delta/cameraMovementTime);

                cameraMovementTime = cameraMovementTime - delta;
                cameraMovementX = cameraMovementX - xMov;
                cameraMovementY = cameraMovementY - yMov;
            }

            camera.position.set(camera.position.x + xMov ,camera.position.y + yMov, camera.position.z);
        } else if (g.is_cameraWithPlayer()){

            // CENTERING CAMERA ON PLAYER ENTITY

            if (g.m.lvlMgr.getPlayer().getpositionx() + MainGameScreen.VIEWPORT.viewportWidth / 2 < mpmgr.getMAP_WIDHT() &&
                    g.m.lvlMgr.getPlayer().getpositionx() - (MainGameScreen.VIEWPORT.viewportWidth / 2) > 0)

                cameraCenter.x = g.m.lvlMgr.getPlayer().getpositionx();

            if (g.m.lvlMgr.getPlayer().getpositiony() + MainGameScreen.VIEWPORT.viewportHeight / 2 < mpmgr.getMAP_HEIGHT() &&
                    g.m.lvlMgr.getPlayer().getpositiony() - (MainGameScreen.VIEWPORT.viewportHeight / 2) > 0)

                cameraCenter.y = g.m.lvlMgr.getPlayer().getpositiony() + g.m.lvlMgr.getPlayer().getPhysicsComponent().get_collisionBoundingBox().getHeight() / 2;

            camera.position.set(cameraCenter, 0f);
        }



        camera.update();
    }


    //UPDATE CAMERA POSITION AFTER PORTAL
    public void updateCameraPositionAfterPortal (Vector2 pos){

        float xCompensation = 0, yCompensation = 0;

        if(g.m.lvlMgr.getPlayer().getpositionx() + MainGameScreen.VIEWPORT.viewportWidth / 2 > mpmgr.getMAP_WIDHT()){
            xCompensation = mpmgr.getMAP_WIDHT() - (g.m.lvlMgr.getPlayer().getpositionx() + (MainGameScreen.VIEWPORT.viewportWidth / 2));
        }
        else if(g.m.lvlMgr.getPlayer().getpositionx() - (MainGameScreen.VIEWPORT.viewportWidth / 2) < 0){
            xCompensation = 0 - (g.m.lvlMgr.getPlayer().getpositionx() - (MainGameScreen.VIEWPORT.viewportWidth / 2));
        }

        if(g.m.lvlMgr.getPlayer().getpositiony() + MainGameScreen.VIEWPORT.viewportHeight / 2 > mpmgr.getMAP_HEIGHT()){
            yCompensation = mpmgr.getMAP_HEIGHT() - (g.m.lvlMgr.getPlayer().getpositiony() + (MainGameScreen.VIEWPORT.viewportHeight / 2));
        }
        else if(g.m.lvlMgr.getPlayer().getpositiony() - (MainGameScreen.VIEWPORT.viewportHeight / 2) < 0){
            yCompensation = 0 - (g.m.lvlMgr.getPlayer().getpositiony() - (MainGameScreen.VIEWPORT.viewportHeight / 2));
        }

        cameraCenter.x = g.m.lvlMgr.getPlayer().getpositionx() + xCompensation;
        cameraCenter.y = g.m.lvlMgr.getPlayer().getpositiony() + yCompensation;

        //camera.position.set(cameraCenter, 0f);
        camera.update();
    }

    //UPDATE GRAPHICS
    void updateEntityGraphics (GameEntity ge,float delta){
        ge.getGraphicComponent().updatingAnimation(delta, ge.isSpine(), ge.isHasNanimation());
    }

    //UPDATE AI
    void updateAI (GameEntity ge,float delta){
        ge.getAiComponent().update(delta, tickForDeltaControl);
        if (tickForDeltaControl)
            tickForDeltaControl = false;
    }

    //RENDER HALO NANIMATION
    void renderHaloNanimation (String key, Sprite sprite, float SkeletonScale, GameEntity halo, GameEntity ge, boolean left){

        int i;
        float rightCompensation = 0;

        if(left) i = -1;
        else{
            i = 1; rightCompensation = sprite.getWidth()*3.0f;
        }

        if(ge.isSpine())
            polspritbatch.draw(sprite,
                    ge.getpositionx() + (((ge.getGraphicComponent().get_Sanimation().get_skeleton().getData().getWidth()/2)) * (SkeletonScale/64) * i)  + (sprite.getWidth()*2.0f * i) - rightCompensation,
                    ge.getpositiony() - (((ge.getGraphicComponent().get_Sanimation().get_skeleton().getData().getHeight()/2)) * (SkeletonScale/64) * 0.9f),
                    sprite.getWidth()*3.0f,
                    ((ge.getGraphicComponent().get_Sanimation().get_skeleton().getData().getHeight()) * ge.getEntityConfig().get_sanimationConfig().skeletonScale)/64);
    }

    //MAP RENDERER
    void mapRenderer (MapRenderer _mapRenderer,int[] _layersForRender){
        _mapRenderer.render(_layersForRender); // CONTROLLING THE RENDERMAPP (FOR LAYER)
    }

    /*_______________________________________________________________________________________________________________*/

    //RECEIVE
    public void receive (String message, String treeID, int treeNumNode){

        g.println(TAG   +
                "***************************************** " + message);

        if(message == null || message.length() == 0) return;

        if(message != null){

            g.println(TAG + "    GAMESYSTEM RECEIVE: " + message);

            String fullMessage[];
            fullMessage = message.split("#");

            if(fullMessage[0].equalsIgnoreCase("ENTITY")){

                g.println(TAG + "   DISPATCH ENTITY /ACTON MESSAGE  :  " + message);

                if(fullMessage[1].equalsIgnoreCase("ACTON")) g.m.lvlMgr.get_acton().receive(getOrderMessage(fullMessage, 2), treeID, treeNumNode);
                else if(fullMessage[1].equalsIgnoreCase("PLAYER")) g.m.lvlMgr.getPlayer().receive(getOrderMessage(fullMessage, 2), treeID, treeNumNode);
                else{

                    GameEntity ge= g.m.lvlMgr.get_currentLvl().get_lvlEntities().get(fullMessage[1]);
                    ge.receive(getOrderMessage(fullMessage, 2), treeID, treeNumNode);
                }
            }

            else if(fullMessage[0].equalsIgnoreCase("ENTITIES")){

                g.println(TAG + "   DISPATCH ENTITIES MESSAGE  :  " + message);

                for(GameEntity ge: entities){
                    if(ge.getID().contains(fullMessage[1])){
                        ge.receive(getOrderMessage(fullMessage, 2), treeID, treeNumNode);
                    }
                }
            }
            else if(fullMessage[0].equalsIgnoreCase("LEVEL_MANAGER")){
                g.println(TAG + "   DISPATCH LEVEL MANAGER MESSAGE  :  " + message);
                g.m.lvlMgr.receive(getOrderMessage(fullMessage, 1), treeID, treeNumNode);
            }
            /*else if(fullMessage[0].equalsIgnoreCase("INVENTORY")) {
                g.println(TAG + "   DISPATCH INVENTORY MESSAGE  :  " +message);
                g.m.invUI.receive(getOrderMessage(fullMessage, 1), treeID, treeNumNode);
            }*/
            else if(fullMessage[0].equalsIgnoreCase("GAMESYSTEM")){
                g.println(TAG + "   DISPATCH INTO GAMESYSTEM MESSAGE  :  " + message + "    " + fullMessage[1]);
                if(fullMessage[1].equalsIgnoreCase("DIALOG_MODE_ON")) {
                    set_dialogMode(true);
                    g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
                }else if(fullMessage[1].equalsIgnoreCase("DIALOG_MODE_OFF")) {
                    set_dialogMode(false);
                    g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
                }else if(fullMessage[1].equalsIgnoreCase("DISABLE_TAP_MOVEMENT")){
                    g.setTapMovement(false);
                    g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
                }else if(fullMessage[1].equalsIgnoreCase("ENABLE_TAP_MOVEMENT")){
                    g.setTapMovement(true);
                    g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
                }else if(fullMessage[1].equalsIgnoreCase("ADD_POINTS") && fullMessage.length==3){
                    g.m.he.setEMO(g.m.he.getEMO()+Integer.valueOf(fullMessage[2]));
                    g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
                }
                else if(fullMessage[1].equalsIgnoreCase("SUBS_POINTS") && fullMessage.length==3){
                    g.m.he.setEMO(g.m.he.getEMO()-Integer.valueOf(fullMessage[2]));
                    g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
                }
                // SOUNDMUSICMATION#SOUND#PLAY#name-sound#lvl-number#with_name-sound%
                //           1             2    3      4           5           6
                else if(fullMessage[1].equalsIgnoreCase("SOUNDMUSICMATION") && fullMessage.length>2){

                    /*  --------------- SOUND ---------------  */
                    if(fullMessage[2].equalsIgnoreCase("SOUND") && fullMessage.length>3) {

                        if (fullMessage[3].equalsIgnoreCase("PLAY") && fullMessage.length == 7) {
                            g.smm.playSound(fullMessage[6], g.smm.loadSound(fullMessage[4], fullMessage[5]));
                            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
                        }
                    }

                    /*  --------------- MUSIC ---------------  */
                    //SOUNDMUSICMATION#MUSIC#name_of_music#intsruction#{...}
                    //      1            2         3            4        5
                    if(fullMessage[2].equalsIgnoreCase("MUSIC") && fullMessage.length>3){

                        if (fullMessage[4].equalsIgnoreCase("PLAY") && fullMessage.length == 6){
                            g.smm.playMusic(fullMessage[3], Boolean.valueOf(fullMessage[5])); //bool looping
                            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

                        }else if (fullMessage[4].equalsIgnoreCase("IS_PLAYING") && fullMessage.length == 5){
                            g.smm.pauseMusic(fullMessage[3]);
                            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
                        }else if (fullMessage[4].equalsIgnoreCase("PAUSE") && fullMessage.length == 5){
                            g.smm.pauseMusic(fullMessage[3]);
                            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
                        }else if (fullMessage[3].equalsIgnoreCase("STOP_ALL")){
                            g.smm.pauseAllMusic();
                            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
                        }else if (fullMessage[4].equalsIgnoreCase("LOADM")&& fullMessage.length==6){//LOAD SOUND BY PATH
                            g.smm.loadMusic(fullMessage[3], Boolean.valueOf(fullMessage[5]));
                            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

                        }else if (fullMessage[4].equalsIgnoreCase("LOAD")&& fullMessage.length==6){//LOAD SOUND BY NAME AND LEVEL
                            g.smm.loadMusic(fullMessage[3], fullMessage[5]);
                            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

                        }else if (fullMessage[4].equalsIgnoreCase("SET_VOLUME")){ //SET VOLUME OF MUSIC
                            g.smm.setMusicVolume(fullMessage[3],Float.valueOf(fullMessage[5]));
                            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

                        }else if(fullMessage[4].equalsIgnoreCase("SET_LOOPING")){   //SET LOOPING MUSIC
                            g.smm.getMusic(fullMessage[3]).setLooping(Boolean.valueOf(fullMessage[5]));
                            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

                        }else if(fullMessage[4].equalsIgnoreCase("DISPOSE")){   //DISPOSE MUSIC
                            g.smm.disposeMusic(fullMessage[3]);
                            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
                        }
                    }
                    else if(fullMessage[2].equalsIgnoreCase("CHARGE_ALL") && fullMessage.length>3){
                        g.smm.loadAllLevelMusicAndSounds(Integer.valueOf(fullMessage[4]));
                    }
                    else if(fullMessage[2].equalsIgnoreCase("DISPOSE_ALL") && fullMessage.length>3){
                        g.smm.disposeAllLevelMusicAndSounds(Integer.valueOf(fullMessage[4]));
                    }



                    g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
                }
                else if(fullMessage[1].equalsIgnoreCase("SET_POINTS") && fullMessage.length==3){
                    if(fullMessage[2].equalsIgnoreCase("TO_FRONT")){
                        Image im=new Image();
                        g.m.he.getS().addActor(im);
                        g.getTextactors().get("HUD").setZIndex(im.getZIndex());
                    }else if(fullMessage[2].equalsIgnoreCase("TO_ORIGIN")){
                        g.getTextactors().get("HUD").setZIndex(g.m.he.EMOzindex);
                    }
                    else{
                        g.m.he.setEMO(Integer.valueOf(fullMessage[2]));
                    }

                    g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
                }else if(fullMessage[1].equalsIgnoreCase("CAMERA")){

                    if(fullMessage[2].equalsIgnoreCase("ZOOM") && fullMessage.length > 3){

                        //Calculate  new zoom value
                        float newZoom;

                        if(fullMessage[4].contains("-")) newZoom = camera.zoom - Float.valueOf(fullMessage[4]);
                        else if(fullMessage[4].contains("+")) newZoom = camera.zoom + Float.valueOf(fullMessage[4]);
                        else newZoom = Float.valueOf(fullMessage[4]);

                        if(fullMessage[3].equalsIgnoreCase("INSTANT") && fullMessage.length == 5){
                            camera.zoom = 0 + newZoom;
                            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
                        }
                        else if(fullMessage[3].equalsIgnoreCase("PROGRESSIVE") && fullMessage.length == 6){

                            float zoomDiference = Math.abs(newZoom - camera.zoom);

                            g.println("****************************++zooooooommm");

                            if(zoomDiference != 0){

                                g.setZooming(true);
                                g.setDesiredZoom(newZoom);
                                g.setZoomingTime(Float.valueOf(fullMessage[5]));
                                g.setZoomVariance(zoomDiference/((Float.valueOf(fullMessage[5]))*60.0f));

                                if(newZoom > camera.zoom) g.setZoomFactor(0);
                                else g.setZoomFactor(2);

                                pendingOKinstructions.add(message + "@" + treeID + "@" + treeNumNode);
                            }
                            else{
                                g.gm.sendMessage(g.messageOK(treeID,treeNumNode), treeID, treeNumNode);
                            }
                        }
                    }else if(fullMessage[2].equalsIgnoreCase("SET_POSITION") && fullMessage.length>4){
                        camera.position.set(Float.valueOf(fullMessage[3]),Float.valueOf(fullMessage[4]), 0f);
                        g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

                    }else if(fullMessage[2].equalsIgnoreCase("MOVE")){

                        isCameraMoving = true;

                        cameraMovementX = Float.valueOf(fullMessage[3]);
                        cameraMovementY = Float.valueOf(fullMessage[4]);
                        cameraMovementTime = Float.valueOf(fullMessage[5]);

                        g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

                    }else if(fullMessage[2].equalsIgnoreCase("CAMERA_WITH_PLAYER")){

                        g.set_cameraWithPlayer(Boolean.valueOf(fullMessage[3]));
                        g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

                    }
                }
                else if(fullMessage[1].equalsIgnoreCase("ADD_PROBES")){
                    for(int i=2; i<fullMessage.length; i++) ProfileScren.ac.probes.put(fullMessage[i], fullMessage[i]);

                    g.printlns(TAG + " add Proves" + ProfileScren.ac.probes.toString());

                    g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
                    //g.m.lvlMgr.receive(getOrderMessage(fullMessage, 1), treeID, treeNumNode);
                }
                else if(fullMessage[1].equalsIgnoreCase("SWAP_RENDERIZE_POSITION") && fullMessage.length == 4){

                    String firstEntity = fullMessage[2];
                    String secondEntity = fullMessage[3];

                    int firstEntityPos = -1;
                    int secondEntityPos = -1;

                    for (int i=0; i<entities.size && (firstEntityPos == -1 || secondEntityPos ==-1) ; i++){
                        if(entities.get(i).getID().equalsIgnoreCase(firstEntity)) firstEntityPos = i;
                        if(entities.get(i).getID().equalsIgnoreCase(secondEntity)) secondEntityPos = i;
                    }

                    if(!(firstEntityPos == -1 || secondEntityPos == -1)){

                        GameEntity geAux = entities.get(firstEntityPos);

                        entities.set(firstEntityPos, entities.get(secondEntityPos));
                        entities.set(secondEntityPos, geAux);


                    }
                    else g.println(TAG + "   SWAP_RENDERIZE_POSITION INSTRUCTION ERROR!!");

                    g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);



                }

            }

            if(fullMessage[0].equalsIgnoreCase("HOST_CLIENT")){
                //SET /UNSET SPRITEMATION_HUD
                if(fullMessage[1].equalsIgnoreCase("SET_SPRITEMATION")){


                    //SINTAX: SET_SPRITEMATION # name # withname # masterDistanceX # masterDistanceY # rotation # (fakeFrame %) (FROM 0% a 100%) (-1 IF ANIMATIONLOOPS NORMALLY)
                    if(fullMessage.length==10){

                        spritemationsHost.setHostClientSpritemation(fullMessage[2], fullMessage[3], Float.valueOf(fullMessage[4]),
                                Float.valueOf(fullMessage[5]), Float.valueOf(fullMessage[6]), Float.valueOf(fullMessage[7]),
                                Integer.valueOf(fullMessage[8]), Boolean.valueOf(fullMessage[9]));
                        g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
                    }
                }
                else if(fullMessage[1].equalsIgnoreCase("SET_SPRITEMATION_DURATION")){
                    spritemationsHost.setSpritemationAnimationDuration(fullMessage[2], Float.valueOf(fullMessage[3]));
                    g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

                }
                else if(fullMessage[1].equalsIgnoreCase("SET_SPRITEMATION_VISIBILITY") && fullMessage.length==4 ){

                    //SINTAX: UNSET_SPRITEMATION #  withname
                    if(fullMessage[3].equalsIgnoreCase("TRUE") || fullMessage[3].equalsIgnoreCase("FALSE")){
                        spritemationsHost.setSpritemationVisibility(fullMessage[2], Boolean.valueOf(fullMessage[3]));

                        g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
                    }
                }
                else if(fullMessage[1].equalsIgnoreCase("SET_SPRITEMATION_FAKEFRAME") && fullMessage.length==4 ){
                    //SINTAX: UNSET_SPRITEMATION #  withname
                        spritemationsHost.setSpritemationFakeFrame(fullMessage[2], Float.valueOf(fullMessage[3]));
                        g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

                }
                else if(fullMessage[1].equalsIgnoreCase("SET_SPRITEMATION_POSITION") && fullMessage.length==6 ){
                    //SINTAX: SET_SPRITEMATION_POSITION #  withname # positionx # positiony
                    spritemationsHost.setHostClientSpritemationPosition(fullMessage[2],Float.valueOf(fullMessage[3]),
                            Float.valueOf(fullMessage[4]), Boolean.valueOf(fullMessage[5]));
                    g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
                }
                else if(fullMessage[1].equalsIgnoreCase("MOVE_SPRITEMATION_TO_POSITION")){
                    spritemationsHost.moveClientHostSpritMationToPosition(fullMessage[2], Float.valueOf(fullMessage[3]),
                            Float.valueOf(fullMessage[4]), Float.valueOf(fullMessage[5]), Boolean.valueOf(fullMessage[6]));

                    g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
                }
                else if((fullMessage[1].equalsIgnoreCase("SCALE_SPRITEMATION")) && fullMessage.length>2 ){
                    //SINTAX: SCALE SPRITEMATION WITHNAME  # scalex # scaley
                    if(fullMessage.length == 5){
                        spritemationsHost.scaleSpritemation(fullMessage[2],Float.valueOf(fullMessage[3]),
                                Float.valueOf(fullMessage[4]));
                        g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
                    }
                }
                else if(fullMessage[1].equalsIgnoreCase("ORIGINAL_SCALE_SPRITEMATION")){
                    if(fullMessage.length == 5){

                        boolean found=false;
                        for(int i=0;i<spritemationsHost.getHostClientEntitySpritAnim().size && !found;i++){
                            SpritemationEntityConfig e = spritemationsHost.getHostClientEntitySpritAnim().get(i);
                            if(e._withname.equalsIgnoreCase(fullMessage[2])) {

                                e.originalWidth = -1;
                                e.originalHeight = -1;
                                found = true;
                            }
                        }

                        spritemationsHost.scaleSpritemation(fullMessage[2],Float.valueOf(fullMessage[3]),
                                Float.valueOf(fullMessage[4]));
                        g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

                        g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
                    }
                }
                else if(fullMessage[1].equalsIgnoreCase("SET_SPRITEMATION_SELECTED")){

                    boolean found=false;
                    for(int i=0;i<spritemationsHost.getHostClientEntitySpritAnim().size && !found;i++){
                        SpritemationEntityConfig e = spritemationsHost.getHostClientEntitySpritAnim().get(i);
                        if(e._withname.equalsIgnoreCase(fullMessage[2])) {

                            e.selected = Boolean.valueOf(fullMessage[3]);
                            found = true;
                        }
                    }

                    g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
                }
                else if(fullMessage[1].equalsIgnoreCase("ADD_SPRITEMATION_ACTION")){

                    boolean isParallel = (fullMessage[fullMessage.length-1].equalsIgnoreCase("PARALLEL"));

                    Image image = spritemationsHost.getSpritemationImages().get(fullMessage[2]);
                    SpritemationEntityConfig sc= spritemationsHost.getHostClientSpriteMationEntityConfigHash().get(fullMessage[2]);

                    if(fullMessage[3].equalsIgnoreCase("FADE_IN")){
                        if(isParallel)sc.getParallelAction().addAction(Actions.fadeIn(Float.valueOf(fullMessage[4])));
                        else image.addAction(Actions.fadeIn(Float.valueOf(fullMessage[4])));
                    }
                    else if(fullMessage[3].equalsIgnoreCase("FADE_OUT")){
                        if(isParallel) sc.getParallelAction().addAction(Actions.fadeOut(Float.valueOf(fullMessage[4])));
                        else image.addAction(Actions.fadeOut(Float.valueOf(fullMessage[4])));
                    }
                    else if(fullMessage[3].equalsIgnoreCase("SET_ALPHA")){
                        if(isParallel) sc.getParallelAction().addAction(Actions.alpha(Float.valueOf(fullMessage[4])));
                        else image.addAction(Actions.alpha(Float.valueOf(fullMessage[4])));
                    }
                    else if(fullMessage[3].equalsIgnoreCase("ADD_ROTATION")){
                        if(isParallel) sc.getParallelAction().addAction(Actions.rotateBy(Float.valueOf(fullMessage[4]),Float.valueOf(fullMessage[5])));
                        else image.addAction(Actions.rotateBy(Float.valueOf(fullMessage[4]),Float.valueOf(fullMessage[5])));
                    }
                    else if(fullMessage[3].equalsIgnoreCase("SCALE")){

                        if(fullMessage.length == 6){
                            if(isParallel) sc.getParallelAction().addAction(Actions.scaleTo(Float.valueOf(fullMessage[4]),Float.valueOf(fullMessage[5])));
                            else image.addAction(Actions.scaleTo(Float.valueOf(fullMessage[4]),Float.valueOf(fullMessage[5])));
                        }
                        else{
                            if(isParallel) sc.getParallelAction().addAction(Actions.scaleTo(Float.valueOf(fullMessage[4]),Float.valueOf(fullMessage[5]),Float.valueOf(fullMessage[6])));
                            else image.addAction(Actions.scaleTo(Float.valueOf(fullMessage[4]),Float.valueOf(fullMessage[5]),Float.valueOf(fullMessage[6])));
                        }

                    }
                    else if(fullMessage[3].equalsIgnoreCase("MOVE_TO_POSITION")){

                        MoveToAction m = new MoveToAction();
                        float x=0;
                        float y=0;

                        if(fullMessage[4].equalsIgnoreCase("CURRENT")) {
                            x = image.getX();
                        }else if(fullMessage[4].contains("CURRENT") && (fullMessage[4].contains("+") || fullMessage[4].contains("-"))){
                            String[] splitvalue;
                            if(fullMessage[4].contains("+")) {
                                splitvalue=fullMessage[4].split(Pattern.quote("+"));
                                x = image.getX();
                                x+=Float.valueOf(splitvalue[1]);
                            }
                            if(fullMessage[4].contains("-")) {
                                splitvalue=fullMessage[4].split(Pattern.quote("-"));
                                x = image.getX();
                                x-=Float.valueOf(splitvalue[1]);
                            }

                        }
                        else x = Float.valueOf(fullMessage[4]);

                        if(fullMessage[5].equalsIgnoreCase("CURRENT")) {
                            y = image.getY();
                        }else if(fullMessage[5].contains("CURRENT") && (fullMessage[5].contains("+") || fullMessage[5].contains("-"))){
                            String[] splitvalue;
                            if(fullMessage[5].contains("+")) {
                                splitvalue=fullMessage[5].split(Pattern.quote("+"));
                                y = image.getY();
                                y+=Float.valueOf(splitvalue[1]);
                            }
                            if(fullMessage[5].contains("-")) {
                                splitvalue=fullMessage[5].split(Pattern.quote("-"));
                                y = image.getY();
                                y-=Float.valueOf(splitvalue[1]);
                            }

                        }
                        else y = Float.valueOf(fullMessage[5]);

                        m.setPosition(x - (image.getWidth()/2), y - (image.getHeight()/2));
                        m.setDuration(Float.valueOf(fullMessage[6]));
                        m.setTarget(image);

                        if(isParallel)sc.getParallelAction().addAction(m);
                        else image.addAction(m);

                    }
                    else if(fullMessage[3].equalsIgnoreCase("MOVE_TO_ORIGIN_XY_POSITION")) {

                        MoveToAction m = new MoveToAction();
                        float x=0;
                        float y=0;

                        if(fullMessage[4].equalsIgnoreCase("CURRENT")) {
                            x = image.getX();
                        }else if(fullMessage[4].contains("CURRENT") && (fullMessage[4].contains("+") || fullMessage[4].contains("-"))){
                            String[] splitvalue;
                            if(fullMessage[4].contains("+")) {
                                splitvalue=fullMessage[4].split(Pattern.quote("+"));
                                x = image.getX();
                                x+=Float.valueOf(splitvalue[1]);
                            }
                            if(fullMessage[4].contains("-")) {
                                splitvalue=fullMessage[4].split(Pattern.quote("-"));
                                x = image.getX();
                                x-=Float.valueOf(splitvalue[1]);
                            }

                        }
                        else x = Float.valueOf(fullMessage[4]);

                        if(fullMessage[5].equalsIgnoreCase("CURRENT")) {
                            y = image.getY();
                        }else if(fullMessage[5].contains("CURRENT") && (fullMessage[5].contains("+") || fullMessage[5].contains("-"))){
                            String[] splitvalue;
                            if(fullMessage[5].contains("+")) {
                                splitvalue=fullMessage[5].split(Pattern.quote("+"));
                                y = image.getY();
                                y+=Float.valueOf(splitvalue[1]);
                            }
                            if(fullMessage[5].contains("-")) {
                                splitvalue=fullMessage[5].split(Pattern.quote("-"));
                                y = image.getY();
                                y-=Float.valueOf(splitvalue[1]);
                            }

                        }
                        else y = Float.valueOf(fullMessage[5]);

                        m.setPosition(x,y);
                        m.setDuration(Float.valueOf(fullMessage[5]));
                        m.setTarget(image);

                        if (isParallel) sc.getParallelAction().addAction(m);
                        else image.addAction(m);
                    }

                    else if(fullMessage[3].equalsIgnoreCase("PARABOLIC_MOVEMENT")) {

                        g.println(TAG + "    " + spritemationsHost.getSpritemationImages().size() + "    " +
                            spritemationsHost.arrayImages.size);

                        sc.parabolicMovement(Float.valueOf(fullMessage[8]), Float.valueOf(fullMessage[4]), Float.valueOf(fullMessage[5]),
                                Float.valueOf(fullMessage[6]), Float.valueOf(fullMessage[7]));
                    }

                    g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

                }

                else if(fullMessage[1].equalsIgnoreCase("ADD_SPRITEMATION_PARALLEL_ACTION")){

                    Image image = spritemationsHost.getSpritemationImages().get(fullMessage[2]);
                    SpritemationEntityConfig sc= spritemationsHost.getHostClientSpriteMationEntityConfigHash().get(fullMessage[2]);

                    image.addAction(sc.getParallelAction());

                    g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

                }
                else if(fullMessage[1].equalsIgnoreCase("CLEAR_SPRITEMATION_PARALLEL_ACTION")){

                    SpritemationEntityConfig sc= spritemationsHost.getHostClientSpriteMationEntityConfigHash().get(fullMessage[2]);

                    sc.getParallelAction().reset();

                    g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

                }

                else if(fullMessage[1].equalsIgnoreCase("SELECTED_SPRITEMATIONS")) {

                    if (fullMessage[2].equalsIgnoreCase("UNSELECT")) {

                        float arrayInitialSize = 0 + spritemationsHost.getSelectedSpritemations().size;

                        for (int i = 0; i < arrayInitialSize; i++) {
                            g.printlns(TAG + "   " + spritemationsHost.getSelectedSpritemations().size);
                            spritemationsHost.getSelectedSpritemations().get(0).selected = false;
                            spritemationsHost.getSelectedSpritemations().removeIndex(0);
                        }
                        g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

                    } else if (fullMessage[2].equalsIgnoreCase("REMOVE_PROBES")) {

                       /* for () {
                            if (ProfileScren.ac.probes.get(l.getListenerOwner()) != null)
                                ProfileScren.ac.probes.remove(l.getListenerOwner());
                        }

                        g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
*/
                    } else if (fullMessage[2].equalsIgnoreCase("SET_SCALE") && fullMessage.length > 3) {
                        for (SpritemationEntityConfig e: spritemationsHost.getSelectedSpritemations()) {
                            if (fullMessage.length == 5)
                                spritemationsHost.scaleSpritemation(e._withname, Float.valueOf(fullMessage[3]),
                                        Float.valueOf(fullMessage[4]));
                        }
                        g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
                    } // SET_POSITION # xx ## yy
                    // SET_POSITION # LISTENER # withname spritemation ::: (listener position)
                    else if (fullMessage[2].equalsIgnoreCase("SET_POSITION")) {

                        if (fullMessage.length > 3) {
                            for (SpritemationEntityConfig e: spritemationsHost.getSelectedSpritemations()) {
                                spritemationsHost.setHostClientSpritemationPosition(e._withname, Float.valueOf(fullMessage[3]), Float.valueOf(fullMessage[4]), Boolean.valueOf(fullMessage[5]));
                            }
                            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
                        }
                    }
                    else if (fullMessage[2].equalsIgnoreCase("SET_VISIBILITY") && fullMessage.length == 3) {

                        for(SpritemationEntityConfig e: spritemationsHost.getSelectedSpritemations()){
                            spritemationsHost.setSpritemationVisibility(e._withname, Boolean.valueOf(fullMessage[3]));
                        }
                        g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

                    } else if (fullMessage[1].equalsIgnoreCase("MOVE_TO_POSITION")) {
                        if (fullMessage.length == 3) {
                            for (SpritemationEntityConfig e: spritemationsHost.getSelectedSpritemations()) {
                                spritemationsHost.moveClientHostSpritMationToPosition(fullMessage[2],
                                        Float.valueOf(fullMessage[3]), Float.valueOf(fullMessage[4]), Float.valueOf(fullMessage[5]), Boolean.valueOf(fullMessage[6]));
                            }
                        }
                        g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
                    }
                }
                else if (fullMessage.length>2 && fullMessage[1].equalsIgnoreCase("CONVERT_TO_HUD")){
                    SpritemationEntityConfig e=null;
                    Vector2 HudPosition=null;
                    for(int i=2;i<fullMessage.length;i++){
                        for (int j=0;j< spritemationsHost.getHostClientExtendedPos().getSize(); j++){
                            e= spritemationsHost.getHostClientEntitySpritAnim().get(
                                    spritemationsHost.getHostClientExtendedPos().getPositionArray().get(j)-1); // SELECT CONFIG OF SP_ARRAY WITH EXTENDEDPOS VALUES
                            if (e._withname.equalsIgnoreCase(fullMessage[i])) break;
                        }
                        if(e!=null) {
                            Vector2 actualposition = new Vector2(e._mDistance.x + e._position.x, e._mDistance.y + e._position.y);
                            HudPosition=TextActor.convertToRealCoordinates(actualposition,g);
                            g.println(TAG +" CONVERT_TO_HUD SPRITEMATION: WITHNAME=" + e._withname + " POSITION: " + actualposition + " HUDPOSITION:" +HudPosition);

                        }

                        e.visible=false;

                        float hviewrely=g.m.he.getH().IMG_HUD.getHeight()/g.getCamera().viewportHeight;
                        float hviewrelx=g.m.he.getH().IMG_HUD.getWidth()/g.getCamera().viewportWidth;

                        g.println( TAG + " IMAGE WIDTH"+ hviewrelx + " IMG HEIGHT" + hviewrely);
                        g.println( TAG + " e. CURRENT SCALEX="+ e.currentScaleX + " e. CURRENT SCALEY=" + e.currentScaleY);


                        sPritemation s=spritemationsHost.getSpritemationsId().get(e._id_Spritemation);


                        g.println( TAG + " s.getSprite().getWidth()="+ s.getSprite().getWidth()+ " .getSprite().getHeight()=" + s.getSprite().getHeight());
                        g.println( TAG + " g.getCamera().zoom="+g.getCamera().zoom);



                        g.m.he.sclientHud.setSpritemation(e._name,e._withname,HudPosition.x,HudPosition.y,e._rotation,e._fakeFrame,
                                (((s.getSprite().getWidth()*e.currentScaleX) * hviewrelx)/g.getCamera().zoom),((s.getSprite().getHeight()*e.currentScaleY) * hviewrely)/g.getCamera().zoom);

                        float imgWidth=g.m.he.sclientHud.getSpritemationHudImagesHash().get(e._withname).getWidth();
                        float imgHeight=g.m.he.sclientHud.getSpritemationHudImagesHash().get(e._withname).getHeight();

                        g.println( TAG + " IMAGE WIDTH"+ imgWidth + " IMG HEIGHT" + imgHeight+ " SPRITE WIDTH=" + s.getSprite().getWidth() + " SPRITE HEIGHT= " + s.getSprite().getHeight());
                        g.println(TAG + " CAMERA VWIDTH" + g.getCamera().viewportWidth + " CAMERA VHEIGH" +g.getCamera().viewportHeight);


                    }
                    g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
                }


            }







            /*else if(fullMessage[0].equalsIgnoreCase("ACTIVATE_SPRITEMATION_DRAG_POSITION") && fullMessage.length==3 ){
                //SINTAX: ACTIVATE_SPRITEMATION_DRAG_POSITION #  withname # true o false
                sclientHud.activateDragImagePosition(fullMessage[1],Boolean.valueOf(fullMessage[2]));
                g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
            }*/

        /*else if(message[0].equalsIgnoreCase("ACTIVATE_SPRITEMATION_BIGSCALER_TOUCH") && message.length==3 ){
            //SINTAX: ACTIVATE_SPRITEMATION_DRAG_POSITION #  withname # true o false
            sclientHud.activatebigScalerTouch(message[1],Boolean.valueOf(message[2]));
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
        }*/





           /* else if(fullMessage[0].equalsIgnoreCase("SPRITEMATION_LISTENERS")){
                if(fullMessage[1].equalsIgnoreCase("ENABLE"))sclientHud.setEnableListeners(true);
                else if(fullMessage[1].equalsIgnoreCase("DISABLE"))sclientHud.setEnableListeners(false);

                g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
            }*/


        }
        else g.println(TAG + " THE GAMESYSTEM MESSAGE CAN'T BE PROCESSED ");
    }

    /*_______________________________________________________________________________________________________________*/

    //ADD ENTITY
    public void AddEntity(GameEntity gamentity){

        if(gamentity.getID().contains("HALO")) halo = gamentity;
        else if(gamentity.getID().contains("ACTON")) acton = (GEActon) gamentity;
        else {
            entities.add(gamentity);
        }
    }

    //CALCULATE POSITIONS
    public Vector2 calculateTalkingEntityTextPosition(float height){

        Vector2 position = new Vector2();

        float screenWidth= (MainGameScreen.VIEWPORT.viewportWidth*g.m.gsys.getCamera().zoom);
        float screenHeight = (MainGameScreen.VIEWPORT.viewportHeight*g.m.gsys.getCamera().zoom);



        float sizex = screenWidth * 0.133f;

        float initialY = camera.position.y - (screenHeight/2);
        float initialX=  camera.position.x - (screenWidth/2 );

        position.x = initialX+sizex;//acton.getPhysicsComponent().get_boundingBoxes().get("TextActor" + 0) .x/g.m.gsys.getCamera().zoom ;
        position.y = initialY + (screenHeight * 0.3f) - (height/2);

        return position;
    }

    public Vector2 calculateTalkingEntityTextSize(){

        Vector2 size = new Vector2();

        float screenWidth= (MainGameScreen.VIEWPORT.viewportWidth*g.m.gsys.getCamera().zoom);
        float screenHeight = (MainGameScreen.VIEWPORT.viewportHeight*g.m.gsys.getCamera().zoom );

        size.x = screenWidth * 0.10f;
        size.y = screenHeight * 0.05f;

        return size;
    }

    public Vector2 calculateActonIconPosition(int numIcon, int totalIcons, float scale, Sprite sprite){

        Vector2 position = new Vector2();
        float initialX, initialY;

        initialX = camera.position.x - ((MainGameScreen.VIEWPORT.viewportWidth/2)*0.7f);
        initialY = camera.position.y - ((MainGameScreen.VIEWPORT.viewportHeight/2)*0.7f);

        position.x += initialX + (((MainGameScreen.VIEWPORT.viewportWidth *0.75f * 0.7f)/(totalIcons+2)) * numIcon) - (scale * sprite.getWidth()/2);
        position.y += initialY + ((MainGameScreen.VIEWPORT.viewportHeight/6) *0.7) - (scale * sprite.getHeight()/2);


        return position;

    }

    public Vector2 calculateTalkTextSize(int totalTexts){

        Vector2 size = new Vector2();

        float screenWidth= (MainGameScreen.VIEWPORT.viewportWidth* g.m.gsys.getCamera().zoom);
        float screenHeight = (MainGameScreen.VIEWPORT.viewportHeight* g.m.gsys.getCamera().zoom);

        size.x = ((screenWidth * 0.65f)/totalTexts) - ((screenWidth * (0.03f * (totalTexts-1)))/totalTexts);
        size.y = screenHeight * 0.20f;

        return size;
    }

    public Vector2 calculateTalkTextPosition(int numText, int totalTexts, float width, float height){

        Vector2 position = new Vector2();
        float initialX, initialY;


        float screenWidth= (MainGameScreen.VIEWPORT.viewportWidth* 0.9f*g.m.gsys.getCamera().zoom);
        float screenHeight = (MainGameScreen.VIEWPORT.viewportHeight* g.m.gsys.getCamera().zoom);

        initialX = camera.position.x - (screenWidth/2) + (screenWidth * 0.13f);
        initialY = camera.position.y - (screenHeight/2);

        position.x = initialX  + (((screenWidth* 0.65f)/totalTexts)* numText) ;
        position.y = initialY + (screenHeight/6) - (height/2);

        return position;
    }



    //CHECK POSSIBLE MOVEMENT
    public boolean checkPossibleMovement (GameEntity ge, Vector2 nextPosition, Vector2 movement,float delta){

        boolean noMove;
        PhysicsComponent pc = ge.getPhysicsComponent();
        Vector2 forcedDest = pc.getForcedMoveDestiny();

        // CONTROLLING POSITION OF ENTITY READING ENTITY INPUTS
        if (ge.getInputComponent().getI() != InputComponent.InputMethod.NONE) {

            float x = 0 + ge.getpositionx();
            float y = 0 + ge.getpositiony();

            if (ge.getInputComponent().isLEFT()) x += -(ge.getPhysicsComponent().get_velocity().x*delta/ GenericMethodsInputProcessor.DELTAUNITY);
            if (ge.getInputComponent().isRIGHT()) x += (ge.getPhysicsComponent().get_velocity().x*delta/ GenericMethodsInputProcessor.DELTAUNITY);
            if (ge.getInputComponent().isUP()) y += (ge.getPhysicsComponent().get_velocity().y*delta/ GenericMethodsInputProcessor.DELTAUNITY);
            if (ge.getInputComponent().isDOWN()) y += -(ge.getPhysicsComponent().get_velocity().y*delta/ GenericMethodsInputProcessor.DELTAUNITY);

            if(ge.getPhysicsComponent().isForcingMove()){
                if((x > forcedDest.x && ge.getpositionx() < forcedDest.x)  || (x < forcedDest.x && ge.getpositionx() > forcedDest.x)) x = 0 + forcedDest.x;
                if((y > forcedDest.y && ge.getpositiony() < forcedDest.y) || (y < forcedDest.y && ge.getpositiony() > forcedDest.y)) y = 0 + forcedDest.y;
            }

            if(ge.getGraphicComponent().get_Sanimation().get_animationState().getCurrent(0).isComplete()){

            }

            nextPosition.set(0 + x, 0 + y);
        }

        // PROGRAMING ENTITY BOUNDS FOR COLLISION PURPOSES
        movement.set(nextPosition.x - ge.getpositionx(), nextPosition.y - ge.getpositiony());

        //SET BOUNDS TO THE NEXT POSSIBLE POSITION
        ge.getPhysicsComponent().setBoundsAfterMovement(movement, false);


        noMove = CollisionManager.isCollisionWhitMaplayer(mpmgr.getCollisionRectangles(), ge,g) ||
                CollisionManager.isCollisionWithEntity(entities, ge) || CollisionManager.isCollisionOutOfCurrentCircunscription(ge, g);

        //IF NEXT POSITION IS NOT POSSIBLE...SET BOUNDS TO THE PREAVIOUS POSITION
        if (noMove) {
            ge.getPhysicsComponent().setBoundsAfterMovement(movement, true);
        }

        return noMove;
    }


    /*_______________________________________________________________________________________________________________*/

    //GETTERS

    public OrthographicCamera getCamera() {  return camera;   }

    public String get_changeLevel() {
        return _changeLevel;
    }

    public Vector2 getCameraCenter () {
        return cameraCenter;
    }

    public OrthoCachedTiledMapRenderer get_maprenderer() {
        return _maprenderer;
    }

    public Array <GameEntity> getEntities () {
        return entities;
    }



    public float getPreviouscamerazoom() {
        return previouscamerazoom;
    }

    public MapManager getMpmgr() {
        return mpmgr;
    }

    public ShapeRenderer getSrenderer() {
        return srenderer;
    }

    public PolygonSpriteBatch getPolspritbatch() {
        return polspritbatch;
    }

    public SkeletonRenderer getSkelrender() {
        return skelrender;
    }

      public float getControldelta() {
        return controldelta;
    }

    public float getPhysicControldelta() {
        return physicControldelta;
    }

    public float getCameraMovementX() {
        return cameraMovementX;
    }

    public float getCameraMovementY() {
        return cameraMovementY;
    }

    public int[] get_layersFirstRenderer() {
        return _layersFirstRenderer;
    }

    public int[] get_layersGhostRenderer() {
        return _layersGhostRenderer;
    }

    public int[] get_layersLastRenderer() {
        return _layersLastRenderer;
    }

    public GameEntity getPortal() {
        return portal;
    }

    public GameEntity getHalo() {
        return halo;
    }

    public GEActon getActon() {
        return acton;
    }

    public boolean isAllRectangleRendererActivator() {
        return allRectangleRendererActivator;
    }

    public boolean isTickForInputControl() {
        return tickForInputControl;
    }

    public boolean isTickForDeltaControl() {
        return tickForDeltaControl;
    }

    public boolean isDialogMode() {
        return dialogMode;
    }

    public boolean isDialogModeChanging() {
        return dialogModeChanging;
    }

    public HashMap<String, nCache> get_nanimationArray() {
        return _nanimationArray;
    }

    public ArrayList<String> getPendingOKinstructions() {
        return pendingOKinstructions;
    }

    //SETTERS

    public void set_dialogMode(boolean setDMode){

        Array<GameEntity> ent = g.getMessageAccessClass().gsys.getEntities();
        OrthographicCamera camera = g.getMessageAccessClass().gsys.getCamera();

        if (setDMode) {

            //STOPING PHYSICS
            for (GameEntity ge : ent) {

                ge.getInputComponent().setUP(false);
                ge.getInputComponent().setDOWN(false);
                ge.getInputComponent().setLEFT(false);
                ge.getInputComponent().setRIGHT(false);

                ge.getPhysicsComponent().setStopPhysicsUpdate(true);
            }
        } else {
            //RESUME PHYSICS
            for (GameEntity ge : ent) {

                ge.getPhysicsComponent().setStopPhysicsUpdate(false);
            }

            g.m.lvlMgr.get_currentLvl().setSelectedGE(null);
        }

        g.getMessageAccessClass().gsys.dialogMode = setDMode;
    }

    public void setMapCameraLayers (OrthoCachedTiledMapRenderer m,OrthographicCamera c,int[] lf, int[] lg,int[] ll){
        this._maprenderer=m;
        this.camera = c;

        this._layersFirstRenderer = lf;
        this._layersGhostRenderer = lg;
        this._layersLastRenderer = ll;
    }

    public void setMapMgr (MapManager _m) {this.mpmgr=_m;}

    public void set_changeLevel(String _changeLevel) {
        this._changeLevel = _changeLevel;
    }

    public void set_maprenderer(OrthoCachedTiledMapRenderer _maprenderer) {
        this._maprenderer = _maprenderer;
    }

    public void setCameraCenter(Vector2 cameraCenter) {
        this.cameraCenter = cameraCenter;
    }

    public void setPreviouscamerazoom(float previouscamerazoom) {
        this.previouscamerazoom = previouscamerazoom;
    }

    public void setMpmgr(MapManager mpmgr) {
        this.mpmgr = mpmgr;
    }

    public void setSrenderer(ShapeRenderer srenderer) {
        this.srenderer = srenderer;
    }

    public void setPolspritbatch(PolygonSpriteBatch polspritbatch) {
        this.polspritbatch = polspritbatch;
    }

    public void setSkelrender(SkeletonRenderer skelrender) {
        this.skelrender = skelrender;
    }


    public void setControldelta(float controldelta) {
        this.controldelta = controldelta;
    }

    public void setPhysicControldelta(float physicControldelta) {
        this.physicControldelta = physicControldelta;
    }



    public void set_layersFirstRenderer(int[] _layersFirstRenderer) {
        this._layersFirstRenderer = _layersFirstRenderer;
    }

    public void set_layersGhostRenderer(int[] _layersGhostRenderer) {
        this._layersGhostRenderer = _layersGhostRenderer;
    }

    public void set_layersLastRenderer(int[] _layersLastRenderer) {
        this._layersLastRenderer = _layersLastRenderer;
    }

    public void setEntities(Array<GameEntity> entities) {
        this.entities = entities;
    }

    public void setPortal(GameEntity portal) {
        this.portal = portal;
    }

    public void setHalo(GameEntity halo) {
        this.halo = halo;
    }

    public void setActon(GEActon acton) {
        this.acton = acton;
    }

    public void setAllRectangleRendererActivator(boolean allRectangleRendererActivator) {
        this.allRectangleRendererActivator = allRectangleRendererActivator;
    }

    public void setTickForInputControl(boolean tickForInputControl) {
        this.tickForInputControl = tickForInputControl;
    }

    public void setTickForDeltaControl(boolean tickForDeltaControl) {
        this.tickForDeltaControl = tickForDeltaControl;
    }

    public void setDialogMode(boolean dialogMode) {
        this.dialogMode = dialogMode;
    }

    public void setDialogModeChanging(boolean dialogModeChanging) {
        this.dialogModeChanging = dialogModeChanging;
    }

    public void set_nanimationArray(HashMap<String, nCache> _nanimationArray) {
        this._nanimationArray = _nanimationArray;
    }

    public void setPendingOKinstructions(ArrayList<String> pendingOKinstructions) {
        this.pendingOKinstructions = pendingOKinstructions;
    }

    public void setCameraMovementX(float cameraMovementX) {
        this.cameraMovementX = cameraMovementX;
    }

    public void setCameraMovementY(float cameraMovementY) {
        this.cameraMovementY = cameraMovementY;
    }
}



