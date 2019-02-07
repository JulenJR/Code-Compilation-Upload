package com.mygdx.safe;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.mygdx.safe.Conversation.DialogsGraphManager;
import com.mygdx.safe.Conversation.TextActor;
import com.mygdx.safe.Entities.GEActon;
import com.mygdx.safe.Entities.GameEntity;
import com.mygdx.safe.Entities.HUD_Entity;
import com.mygdx.safe.EntitySystems.GameSystem;
import com.mygdx.safe.EntitySystems.HUD_System;
import com.mygdx.safe.EntitySystems.ParticleSystem;
import com.mygdx.safe.InputProcessors.CameraGestureDetectorProcessor;
import com.mygdx.safe.InputProcessors.CameraInputProcessor;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;
import com.mygdx.safe.MainGameGraph.GameGraphsManager;
import com.mygdx.safe.screens.MainGameScreen;


import java.util.HashMap;

/**
 * Created by alumne_practiques on 09/10/17.
 */

public class LevelManager {

    private static final String TAG = LevelManager.class.getSimpleName();
    private com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g;
    private OrthoCachedTiledMapRenderer _mapRenderer;

    private CameraInputProcessor camerainputprocessor;
    private com.mygdx.safe.InputProcessors.CameraGestureDetectorProcessor cameragesturedetectorprocessor;
    private InputMultiplexer _multiplexer;

    // GRAPHSES DIALOG AND GAMEGRAPHSES
    private com.mygdx.safe.Conversation.DialogsGraphManager dgMgr;
    private com.mygdx.safe.MainGameGraph.GameGraphsManager ggMgr;

    private HashMap<String, String> _lvlJsonConfigPaths;
    private boolean _lvlChanged = false;
    private Level _currentLvl;

    //ENTITIES DATA
    private HashMap<String, sCache> _sCacheMap = new HashMap<String, sCache>();
    private HashMap<String, nCache> _nCacheMap = new HashMap<String, nCache>();
    private HashMap<String, com.mygdx.safe.EntityConfig> _entityConfigMap = new HashMap<String, com.mygdx.safe.EntityConfig>();

    //PARTICLES
    private HashMap<String, ParticlePositions.Data> _particlepositions = new HashMap<String, ParticlePositions.Data>();

    //MAP MANAGER
    private MapManager _mapManager;

    //ENTITIES
    private com.mygdx.safe.Entities.HUD_Entity h;

    private com.mygdx.safe.Entities.GameEntity player;
    private com.mygdx.safe.Entities.GameEntity _halo;
    private com.mygdx.safe.Entities.GEActon _acton;

    //SYSTEMS
    private com.mygdx.safe.EntitySystems.HUD_System h_system;
    private com.mygdx.safe.EntitySystems.GameSystem g_system;
    private com.mygdx.safe.EntitySystems.ParticleSystem p_system;


    private String lastPortalUsed = "";

    // ASHLEY ENGINE ENTITY & SYSTEM
    private Engine engine;

    //CURRENT TREE & NODE
    private String TreeID;
    private int TreeNumNode;

    public LevelManager (){

        this._lvlJsonConfigPaths = new HashMap<String, String>();
        engine =new Engine();

    }


    public synchronized void preconfig(OrthographicCamera _camera, GenericMethodsInputProcessor g, MainGameScreen mGS) {

        this.g=g;

        this._currentLvl = new Level(g);
        this._mapManager = new MapManager(g);

        g.getMessageAccessClass().setLvlMgr(this);

        _mapRenderer = new OrthoCachedTiledMapRenderer(_mapManager.getCurrentTiledMap(), Map.UNIT_SCALE,8191);
        _mapRenderer.setBlending(true);
        //_mapRenderer.setOverCache(1f);

        //SETTING CAMERA ON MAP
        _camera.zoom=1f; // SETTING INIT CAMERA.ZOOM
        _mapRenderer.setView(_camera);
        _mapManager.setCamera(_camera);

        // CREATING HUD ENTITY & HUD SYSTEM
        h=new HUD_Entity(g,_camera);
        h_system=new HUD_System(g,_camera,h);
        g.getMessageAccessClass().setHe(h);

        // INPUT PROCESSORS (GESTURE AND TOUCH AND KEYBOARD INPUTS)

        cameragesturedetectorprocessor=new CameraGestureDetectorProcessor(h);
        camerainputprocessor=new CameraInputProcessor(h);
        g.getMessageAccessClass().setcInProcess(camerainputprocessor);

        // SETUP MULTIPLEXER WITH INPUTPROCESSORS
        createMultiplexer();


        // CREATING GAMESYSTEM
        g_system=new GameSystem(g);
        g.getMessageAccessClass().setGsys(g_system);



        setCurrentLevel("01", "01");

    }

    public void createMultiplexer(){

        _multiplexer = new InputMultiplexer();

        // PUT PLAYER INPUT PROCESSOR
        // AND HUD INPUT PROCESSOR
        // AND CAMERAINPUTPROCESSOR
        // AND CAMERAGESTUREINPUTPROCESSOR IN MULTIPLEXER

        _multiplexer.addProcessor(h.getStageforHud());


        _multiplexer.addProcessor(h.getStage()); // HUD STAGE PROCESSOR
        _multiplexer.addProcessor(new GestureDetector(cameragesturedetectorprocessor));
        _multiplexer.addProcessor(camerainputprocessor);


        // SETTING GDX INPUT
        Gdx.input.setInputProcessor(_multiplexer);
    }


    public synchronized void config(OrthographicCamera _camera, GenericMethodsInputProcessor g, MainGameScreen mGS){

        EntityFactory.load(g,_sCacheMap, _nCacheMap, _entityConfigMap, _currentLvl.get_lvlConfig().get_entityJsonPaths());
        ParticleFactory.load( _particlepositions, g);
        p_system = new ParticleSystem(g);
        _currentLvl.config(g,_camera, this);
        //g.m.he.getHudActorDataComponent().inventory.config();

        _mapManager.get_currentMap().setClosestStartPositionFromScaledUnits(new Vector2(_mapManager.getMAP_WIDHT()/2, _mapManager.getMAP_HEIGHT()/2));
        _mapManager.get_currentMap().setPlayerStart(_mapManager.get_currentMap()._closestPlayerStartPosition);

        player = EntityFactory.getEntity(_entityConfigMap.get("PLAYER"), _sCacheMap.get("PLAYER"), _nCacheMap,g,_mapManager,_camera, "PLAYER");

        //player.sclient.setSpritemation("stars", "stars1" , 0f, 2f, 45f);
        //player.sclient.setSpritemation("stone", "stone1" , 1f, 2f, 45f);
        //player.sclient.setSpritemation("stars", "stars2" , 2f, 2f, 45f,50);
        //player.sclient.setSpritemation("liquid", "liquid1" , -1f, 2f, 45f);
        //player.sclient.setSpritemation("liquid", "liquid2" , -2f, 2f, 45f,30);
        //player.sclient.setSpritemation("stars", "stars3" , -3f, 2f, 45f);
        //player.sclient.setSpritemation("stars", "stars4" , 3f, 2f, 45f);
        //player.sclient.setSpritemation("stars", "stars5" , -4f, 2f, 45f);
        //player.sclient.setSpritemation("stars", "stars6" , -5f, 2f, 45f);

        g_system.ParticleConfig();

        _halo = EntityFactory.getEntity(_entityConfigMap.get("HALO"), null, _nCacheMap, g, _mapManager, _camera, "HALO");
        _acton = EntityFactory.getActonEntity(_entityConfigMap.get("ACTON"), null, _nCacheMap, g, _mapManager, _camera, "ACTON");


        /*for(GameEntity e: _currentLvl.get_lvlEntities().values()){
            g_system.AddEntity(e);
        }

        // ADDING PLAYER
        g_system.AddEntity(player);

        g_system.AddEntity(_halo);
        g_system.AddEntity(_acton);*/

        //CONFIG MAPMANAGER && CAMERA IN GAMESYSTEM
        g_system.setMapMgr(_mapManager);


        // SETTINGS FOR RENDER LAST_LAYER IN GAMESYSTEM
        g_system.setMapCameraLayers(_mapRenderer,_camera, _mapManager.get_currentMap().get_layersFirst(), _mapManager.get_currentMap().get_layersGhost(),
                _mapManager.get_currentMap().get_layersLast());

        // LAST HUD CONFIG , AFTER GSYSTEEM AND ALL ENTITIES CHARGED
        h.setlastHHU_Entity_Config();

        // CREATES AND MANAGE ALL DIALOG GRAHPSES



        dgMgr=new DialogsGraphManager(g);
        dgMgr.load();
        dgMgr.selectDialogGraph("LVL_1#CONV_1#GUARD_00_1#STANDARD"); // LEVEL, CONVERSATION NUMBER, GAME ENTITY, VISUALGAMESTATE
        g.getMessageAccessClass().setDgMgr(dgMgr);


       // CREATES AND MANAGE GAMEGRAPHSES WITH GAMEGRAPHSMANAGER
        ggMgr = new GameGraphsManager(g);
        ggMgr.load();

        g.smm.loadMusic("sound/lvl_clear.ogg", true);

        // PARTICLE PROBE
        p_system.createParticle("star-beauty", "particle/star",0.025f, 78f, 40f,true);
        p_system.moveToReceive("star-beauty",81f,40f,8);

        //p_system.createParticle("a", "particle/fire",0.1f , 200f, 100f, true);

    }

    public void addNPCEntities(){
        // ENTITY REAL MAP CHARGER
        String s="";
        String splitNpc[];
        String entityConfigName;
        int i=0;
        Array<String> npcNames=_mapManager.getNpcNameEntities();
        for(Vector2 v:_mapManager.getNpcStartPositions()){
            g.println("\n"+ TAG + " READING NPC START POSITION VECTOR FROM NPCNAME AT MAP: ["  +npcNames.get(i)+"]");

            splitNpc=npcNames.get(i).split("#");
            s=splitNpc[0];

            entityConfigName = s.split("_")[0];

            for(int j=1; j<s.split("_").length - 1; j++){
                entityConfigName += "_" + s.split("_")[j];
            }
            g.println(TAG+ " CHARGING FROM MAP: PRINTING ENTITY CONFIG NAME :[" + entityConfigName+"]");

            _currentLvl.get_lvlEntities().put(s, EntityFactory.getNPCEntity(_entityConfigMap.get(entityConfigName), _sCacheMap.get(entityConfigName),
                    _nCacheMap, g,v, g.getCamera(), s)); //GENERATES ALL NPCS
            g.println(TAG+ " CHARGING FROM MAP: CREATING ENTITY FROM MAP READING CACHES: "+s+" NPC ("+i+"), PUTTING AT GAME READING MAP POSITION = "+v);
            i++;
        }
    }

    public void removeEngineContent (){

        engine = null;
        engine = new Engine();
        g.m.mGS.getIaAccess().setEngine(engine);

    }

    public void addEngineContent(){

        //ADDING ARRAY NPC TO ENGINE
        for(GameEntity e:_currentLvl.get_lvlEntities().values()){
            engine.addEntity(e);
        }

        engine.addEntity(player);

        //ADDING G_SYSTEM TO ENGINE
        engine.addSystem(g_system);

        // ADDING HUD ENTITY && ENGINE
        engine.addEntity(h);
        engine.addSystem(h_system);


    }

    //RECEIVE
    public void receive(String[] message, String treeID, int treeNumNode){

        if(message[0].equalsIgnoreCase("CHANGE_MAP")){

            g.println(TAG + "RECEIVING: CHANGE MAP TO "    + message[1]);


            g.m.he.setS(new Stage(new ScalingViewport(Scaling.stretch, g.m.he.getHudActorDataComponent().IMG_HUD.getWidth(),
                    g.m.he.getHudActorDataComponent().IMG_HUD.getHeight()),g.getSpriteBatch()));

            g.m.he.addTextActorsToStage();

            createMultiplexer();

            g.m.he.sclientHud.reset();
            g.m.gsys.spritemationsHost.reset();

            changeMap(message[1]);



            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);


        }else if(message[0].equalsIgnoreCase("SELECTED_ENTITY")){

            _currentLvl.setSelectedGE(_currentLvl.get_lvlEntities().get(message[1]));
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

        }else if(message[0].equalsIgnoreCase("CREATE_ENTITY") && _currentLvl.get_lvlEntities().get(message[2]) == null){

            _currentLvl.get_lvlEntities().put(message[2], EntityFactory.getNPCEntity(_entityConfigMap.get(message[1]), _sCacheMap.get(message[1]),
                    _nCacheMap, g,new Vector2(Float.valueOf(message[3]), Float.valueOf(message[4])), g.getCamera(), message[2]));
            g.m.lvlMgr.p_system.createParticlesToEntity(g.m.gsys.getEntities().size-1, _currentLvl.get_lvlEntities().get(message[2]));
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

        }

    }

    public void changeMap(String numMap){

        //SET LVL_ID
        _currentLvl.set_lvlID("LVL_" + numMap);
        g.println(TAG+" SETTING CURRENT LEVEL AT : [LVL_"+numMap+"]");

        for (TextActor t:g.getTextactors().values()){
            t.setVisible(false);
        }

        //Empty the entity array and the data Hashmaps
        g_system.getEntities().removeRange(0, g_system.getEntities().size-1);
        g_system.getEntities().clear();

        p_system.clearPartiles();

        removeEngineContent();

        for(GameEntity ge: _currentLvl.get_lvlEntities().values()){
            _currentLvl.get_lvlEntities().remove(ge);
        }
        _currentLvl.get_lvlEntities().clear();

        GameEntity.setCountId(0);

        player = null;
        _halo = null;

        _mapRenderer.dispose();
        _mapManager.loadMap(numMap);

        _mapRenderer = new OrthoCachedTiledMapRenderer(g.m.lvlMgr.get_mapManager().getCurrentTiledMap(), Map.UNIT_SCALE,8191);
        _mapRenderer.setBlending(true);
        //_mapRenderer.setOverCache(1f);

        //SETTING CAMERA ON MAP
        _mapRenderer.setView(g.m.mGS.get_camera());
        _mapManager.setCamera(g.m.mGS.get_camera());

        g_system.set_maprenderer(_mapRenderer);
        g.m.mGS.getIaAccess().set_mapRenderer(_mapRenderer);

        addNPCEntities();

        player = EntityFactory.getEntity(_entityConfigMap.get("PLAYER"), _sCacheMap.get("PLAYER"), _nCacheMap,g,_mapManager,g.m.mGS.get_camera(), "PLAYER");
        g_system.ParticleConfig();
        _halo = EntityFactory.getEntity(_entityConfigMap.get("HALO"), null, _nCacheMap, g, _mapManager, g.m.mGS.get_camera(), "HALO");

        addEngineContent();

        g.m.gsys.setMapCameraLayers(_mapRenderer, g.getCamera(),
                _mapManager.get_currentMap()._layersFirst,
                _mapManager.get_currentMap()._layersGhost,
                _mapManager.get_currentMap()._layersLast);


        for(Entity e: engine.getEntities()){
            g.println(e.toString());
        }


    }


    /*
    public void changeLevelMap (String portalName){

        //SPLIT PORTAL NAME (PORTAL_XX_XX)
        String m[] = portalName.split("_");

        //SET LVL_ID
        _currentLvl.set_lvlID("LVL_" + m[2]);
        g.println(TAG+" SETTING CURRENT LEVEL AT : [LVL_"+m[2]+"]");

        //Empty the entity array and the data Hashmaps
        g_system.getEntities().removeRange(0, g_system.getEntities().size-1);
        g_system.getEntities().clear();

        p_system.clearPartiles();

        removeEngineContent();

        for(GameEntity ge: _currentLvl.get_lvlEntities().values()){
            _currentLvl.get_lvlEntities().remove(ge);
        }
        _currentLvl.get_lvlEntities().clear();

        GameEntity.setCountId(0);

        player = null;
        _halo = null;

        _mapRenderer.dispose();
        _mapManager.loadMap(Integer.valueOf(m[2]));

        _mapRenderer = new OrthoCachedTiledMapRenderer(g.m.lvlMgr.get_mapManager().getCurrentTiledMap(), Map.UNIT_SCALE,8191);
        _mapRenderer.setBlending(true);
        //_mapRenderer.setOverCache(1f);

        //SETTING CAMERA ON MAP
        _mapRenderer.setView(g.m.mGS.get_camera());
        _mapManager.setCamera(g.m.mGS.get_camera());

        g_system.set_maprenderer(_mapRenderer);
        g.m.mGS.getIaAccess().set_mapRenderer(_mapRenderer);

        addNPCEntities();

        for(String st : _mapManager.getPortalRectangles().keySet()){
            if(st.equals(m[0]+ "_" + m[2]+ "_" +m[1]))
                _mapManager.get_currentMap().setClosestStartPositionFromScaledUnits(_mapManager.getPortalRectangles().get(st).get(0).getPosition(new Vector2()));
        }

        _mapManager.get_currentMap().setPlayerStart(_mapManager.get_currentMap()._closestPlayerStartPosition);

        player = EntityFactory.getEntity(_entityConfigMap.get("PLAYER"), _sCacheMap.get("PLAYER"), _nCacheMap,g,_mapManager,g.m.mGS.get_camera(), "PLAYER");
        g_system.ParticleConfig();
        _halo = EntityFactory.getEntity(_entityConfigMap.get("HALO"), null, _nCacheMap, g, _mapManager, g.m.mGS.get_camera(), "HALO");



        //CREATE THE NEW ENTITIES

        for(GameEntity e: _currentLvl.get_lvlEntities().values()){
            g_system.AddEntity(e);
        }

        // ADDING PLAYER
        g_system.AddEntity(player);
        g_system.AddEntity(_halo);

        addEngineContent();

        for(Entity e: engine.getEntities()){
            g.println(e.toString());
        }

        g_system.updateCameraPositionAfterPortal(_mapManager.getPlayerStartUnitScaled());
        g.println(TAG+"////////////////////// ENDING CHARGING OF LEVEL [LVL_"+m[2]+"]");

        // CHANGING GRAPH!

        // FIRST GRAPH INSTRUCTION AT CHANGE LEVEL AND GRAPH
        g.gm.sendMessage("GAMEGRAPH#GO#0#SHOOTER#LEVEL#START", null, -1);

    }

    */

    public Engine getEngine() {
        return engine;
    }

    public MapManager get_mapManager() {
        return _mapManager;
    }

    public void set_mapManager(MapManager _mapManager) {
        this._mapManager = _mapManager;
    }

    public GameEntity getPlayer() {
        return player;
    }

    public void set_halo(com.mygdx.safe.Entities.GameEntity _halo) {
        this._halo = _halo;
    }

    public GameEntity get_halo() {
        return _halo;
    }

    public void setCurrentLevel(String numMap, String numLevel){


        _currentLvl.set_lvlConfig((Level.GetLevelConfig(_lvlJsonConfigPaths.get(numMap+"_"+numLevel))));
    }

    public String getLastPortalUsed() {
        return lastPortalUsed;
    }

    public void setLastPortalUsed(String lastPortalUsed) {
        this.lastPortalUsed = lastPortalUsed;
    }

    public HashMap<String, sCache> get_sCacheMap() {
        return _sCacheMap;
    }
    public HashMap<String, nCache> get_nCacheMap() {
        return _nCacheMap;
    }

    public HashMap<String, com.mygdx.safe.EntityConfig> get_entityConfigMap() {
        return _entityConfigMap;
    }

    public Level get_currentLvl() {
        return _currentLvl;
    }

    public static String getTAG() {
        return TAG;
    }

    public OrthoCachedTiledMapRenderer get_mapRenderer() {
        return _mapRenderer;
    }

    public HashMap<String, String> get_lvlJsonPaths() {
        return _lvlJsonConfigPaths;
    }

    public boolean is_lvlChanged() {
        return _lvlChanged;
    }

    public GenericMethodsInputProcessor getG() {
        return g;
    }

    public HUD_Entity getH() {
        return h;
    }

    public HUD_System getH_system() {
        return h_system;
    }

    public ParticleSystem getP_system() {
        return p_system;
    }

    public CameraInputProcessor getCamerainputprocessor() {
        return camerainputprocessor;
    }

    public CameraGestureDetectorProcessor getCameragesturedetectorprocessor() {
        return cameragesturedetectorprocessor;
    }

    public InputMultiplexer get_multiplexer() {
        return _multiplexer;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public GameSystem getG_system() {
        return g_system;
    }

    public void set_mapRenderer(OrthoCachedTiledMapRenderer _mapRenderer) {
        this._mapRenderer = _mapRenderer;
    }

    public GEActon get_acton() {
        return _acton;
    }

    public void set_acton(GEActon _acton) {
        this._acton = _acton;
    }
    //JSON serialization
    static public LevelManager getLevelManager(String path){
        Json json = new Json();
        return json.fromJson(LevelManager.class, Gdx.files.internal(path));
    }

    public GameGraphsManager getGgMgr() {
        return ggMgr;
    }

    public DialogsGraphManager getDgMgr() {
        return dgMgr;
    }

    public HashMap<String, ParticlePositions.Data> get_particlepositions() {
        return _particlepositions;
    }

    public void set_particlepositions(HashMap<String, ParticlePositions.Data> _particlepositions) {
        this._particlepositions = _particlepositions;
    }
}
