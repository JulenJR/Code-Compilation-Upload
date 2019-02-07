package com.mygdx.safe.IA;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.mygdx.safe.Entities.GameEntity;
import com.mygdx.safe.InputProcessors.CameraGestureDetectorProcessor;
import com.mygdx.safe.InputProcessors.CameraInputProcessor;
import com.mygdx.safe.MapManager;
import com.mygdx.safe.Safe;

import java.util.HashMap;

/**
 * Created by Boris.InspiratGames on 28/07/17.
 */

public class IAAccessClass {

    //TAG
    private static final String TAG = IAAccessClass.class.getSimpleName();

    // PLAYER
    private static GameEntity player;

    //ASPECTS
    private com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g;

    //TRICEPTION
    private Safe _game;

    //CAMERA
    private OrthographicCamera _camera;
    private CameraInputProcessor camerainputprocessor;
    private CameraGestureDetectorProcessor cameragesturedetectorprocessor;

    //MULTIPLEXER
    private InputMultiplexer _multiplexer;

    //MAP MANAGER & RENDERER
    private MapManager _mapMgr;
    private OrthoCachedTiledMapRenderer _mapRenderer;

    //LAYERS
    private int [] _layersForRender;
    private int [] _layersForRenderfinal;

    //ASHLEY ENGINE ENTITY & SYSTEM
    private Engine engine;
    private com.mygdx.safe.Entities.HUD_Entity h;
    private com.mygdx.safe.EntitySystems.HUD_System h_system;
    private com.mygdx.safe.EntitySystems.GameSystem g_system;
    private HashMap<String, GameEntity> npcArray;

    //SCACHE
    private HashMap<String, com.mygdx.safe.sCache> _cacheMap;

    //IA MASTER CONTROL
    private IAMasterControl iamaster;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public IAAccessClass(){}

    /*_______________________________________________________________________________________________________________*/

    //GETTERS

    public static GameEntity getPlayer() {
        return player;
    }

    public Safe get_game() {
        return _game;
    }

    public InputMultiplexer get_multiplexer() {
        return _multiplexer;
    }

    public OrthographicCamera get_camera() {
        return _camera;
    }

    public MapManager get_mapMgr() {
        return _mapMgr;
    }

    public OrthoCachedTiledMapRenderer get_mapRenderer() {
        return _mapRenderer;
    }

    public int[] get_layersForRender() {
        return _layersForRender;
    }

    public int[] get_layersForRenderfinal() {
        return _layersForRenderfinal;
    }

    public CameraInputProcessor getCamerainputprocessor() {
        return camerainputprocessor;
    }

    public CameraGestureDetectorProcessor getCameragesturedetectorprocessor() {
        return cameragesturedetectorprocessor;
    }

    public Engine getEngine() {
        return engine;
    }

    public com.mygdx.safe.Entities.HUD_Entity getH() {
        return h;
    }

    public com.mygdx.safe.EntitySystems.HUD_System getH_system() {
        return h_system;
    }

    public HashMap<String, GameEntity> getNpcArray() {
        return npcArray;
    }

    public com.mygdx.safe.EntitySystems.GameSystem getG_system() {
        return g_system;
    }

    public HashMap<String, com.mygdx.safe.sCache> get_cacheMap() {
        return _cacheMap;
    }

    public IAMasterControl getIamaster() {
        return iamaster;
    }

    //SETTERS
    public static void setPlayer(GameEntity player) {
        IAAccessClass.player = player;
    }

    public void setMainGameScreenData(
            Safe _game,
            InputMultiplexer _multiplexer,
            OrthographicCamera _camera,
            MapManager _mapMgr,
            OrthoCachedTiledMapRenderer _mapRenderer,
            int [] _layersForRender,
            int [] _layersForRenderfinal,
            CameraInputProcessor camerainputprocessor,
            CameraGestureDetectorProcessor cameragesturedetectorprocessor,
            Engine engine,
            com.mygdx.safe.Entities.HUD_Entity h,
            com.mygdx.safe.EntitySystems.HUD_System h_system,
            com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g,
            GameEntity player,
            HashMap <String, GameEntity> npcArray,
            com.mygdx.safe.EntitySystems.GameSystem g_system,
            HashMap<String, com.mygdx.safe.sCache> _cacheMap,
            IAMasterControl iamaster
    ){
        this._game                             =  _game                          ;
        this._multiplexer                      =  _multiplexer                   ;
        this._camera                           =  _camera                        ;
        this._mapMgr                           =  _mapMgr                        ;
        this._mapRenderer                      =  _mapRenderer                   ;
        this._layersForRender                  =  _layersForRender               ;
        this._layersForRenderfinal             =  _layersForRenderfinal          ;
        this.camerainputprocessor              =  camerainputprocessor           ;
        this.cameragesturedetectorprocessor    =  cameragesturedetectorprocessor ;
        this.engine                            =  engine                         ;
        this.h                                 =  h                              ;
        this.h_system                          =  h_system                       ;
        this.g                                 =  g                              ;
        IAAccessClass.player =  player                         ;

        this.npcArray                          =  npcArray                       ;
        this.g_system                          =  g_system                       ;
        this._cacheMap                         =  _cacheMap                      ;
        this.iamaster                          =  iamaster                       ;
    }

    public void set_game(Safe _game) {
        this._game = _game;
    }

    public void set_multiplexer(InputMultiplexer _multiplexer) {
        this._multiplexer = _multiplexer;
    }

    public void set_camera(OrthographicCamera _camera) {
        this._camera = _camera;
    }

    public void set_mapMgr(MapManager _mapMgr) {
        this._mapMgr = _mapMgr;
    }

    public void set_mapRenderer(OrthoCachedTiledMapRenderer _mapRenderer) {
        this._mapRenderer = _mapRenderer;
    }

    public void set_layersForRender(int[] _layersForRender) {
        this._layersForRender = _layersForRender;
    }

    public void set_layersForRenderfinal(int[] _layersForRenderfinal) {
        this._layersForRenderfinal = _layersForRenderfinal;
    }

    public void setCamerainputprocessor(CameraInputProcessor camerainputprocessor) {
        this.camerainputprocessor = camerainputprocessor;
    }

    public void setCameragesturedetectorprocessor(CameraGestureDetectorProcessor cameragesturedetectorprocessor) {
        this.cameragesturedetectorprocessor = cameragesturedetectorprocessor;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void setH(com.mygdx.safe.Entities.HUD_Entity h) {
        this.h = h;
    }

    public void setH_system(com.mygdx.safe.EntitySystems.HUD_System h_system) {
        this.h_system = h_system;
    }

    public void setNpcArray(HashMap<String, GameEntity> npcArray) {
        this.npcArray = npcArray;
    }

    public void setG_system(com.mygdx.safe.EntitySystems.GameSystem g_system) {
        this.g_system = g_system;
    }

    public void set_cacheMap(HashMap<String, com.mygdx.safe.sCache> _cacheMap) {
        this._cacheMap = _cacheMap;
    }

    public void setIamaster(IAMasterControl iamaster) {
        this.iamaster = iamaster;
    }

}