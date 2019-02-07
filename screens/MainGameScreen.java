package com.mygdx.safe.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;

import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.mygdx.safe.AllConfigs;
import com.mygdx.safe.AllConfigsCharger;
import com.mygdx.safe.Conversation.TextActor;
import com.mygdx.safe.IA.IAAccessClass;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;
import com.mygdx.safe.LevelManager;
import com.mygdx.safe.Safe;

/**
 * Created by Boris.InspiratGames on 20/05/17.
 */

public class MainGameScreen implements Screen {

    //TAG
    private static final String TAG = MainGameScreen.class.getSimpleName();

    //ASPECTS
    private com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g;

    //STATIC
    public static class VIEWPORT {
        public static float viewportWidth;
        public static float viewportHeight;
        public static float virtualWidth;
        public static float virtualHeight;
        public static float physicalWidth;
        public static float physicalHeight;
        public static float aspectRatio;
    }

    public static int VIEWPORT_WIDTH_SETUP=30;
    public static int VIEWPORT_HEIGHT_SETUP=17;

    public static Vector2 resizeFactor= new Vector2();
    public static float oldWidth = 1920*3/5;
    public static float oldHeight = 1088*3/5;
    public static float blackSpaceX = 0;
    public static float blackSpaceY = 0;

    public static GameState _gameState;
    public enum GameState { RUNNING, PAUSED}
    public boolean initGame=false;


    // BACKGROUND IMAGE
    private Texture BACKGROUND;
    private Image imgBackground;

    //CAMERA
    private OrthographicCamera _camera;

    //TRICEPTION
    private Safe _game;

    //LEVEL MANAGER
    private LevelManager _lvlManager;

    // IAMASTERCONTROL
    private com.mygdx.safe.IA.IAMasterControl iamaster;

    //IA ACCESS CLASS
    private IAAccessClass iaAccess;

    // RENDER CONTROL
    private float controlframes=0;
    private float controldelta=0;
    private int framesmap=0;
    private int updatesprocessor=0;

    //AllConfigs
    public static AllConfigs ac;
    public static AllConfigsCharger acCharger;

    // command Extern
    public boolean command=false;
    public int commandInt=0;

    





    /*_______________________________________________________________________________________________________________*/

    //STATIC METHODS
    public static void setGameState(GameState gameState){
        switch(gameState){
            case RUNNING:
                _gameState = GameState.RUNNING;
                break;
            case PAUSED:
                if( _gameState == GameState.PAUSED ){
                    _gameState = GameState.RUNNING;
                    //com.mygdx.safe.profile.ProfileManager.getInstance().loadProfile();
                }else if( _gameState == GameState.RUNNING ){
                    _gameState = GameState.PAUSED;
                    //com.mygdx.safe.profile.ProfileManager.getInstance().saveProfile();
                }
                break;
            default:
                _gameState = GameState.RUNNING;
                break;
        }
    }

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public MainGameScreen(Safe game) {
        _game = game;
        g=new GenericMethodsInputProcessor(game);
        BACKGROUND = new Texture("hud/skins/safescreen.jpg");
        imgBackground =new Image(BACKGROUND);
        imgBackground.setPosition(0,0);
        imgBackground.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        // INITIAL CONFIGS
        iaAccess=new IAAccessClass();
        g.getMessageAccessClass().setmGS(this);
        setupViewport(VIEWPORT_WIDTH_SETUP, VIEWPORT_HEIGHT_SETUP);
        _camera = g.getCamera();
        _camera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);
        _lvlManager = LevelManager.getLevelManager("scripts/MAP_1.json");

        _lvlManager.preconfig(_camera, g, this);

       

    }

    //RENDER
    @Override
    public void render(float delta) {
        if (_gameState == GameState.PAUSED) {
            return;
        }else if(!Safe.createProcess){
            // RENDER RENGINE (ENGINE INTERNAL CONTROL)
            g.m.lvlMgr.getEngine().update(delta);
            // RENDER GRAPH (GRAPH INTERNAL CONTROL)
            g.m.lvlMgr.getGgMgr().update(delta);
        }


    }

    public void command(){
        if(commandInt==0) return;
        // commandInt=1; // CHARGE LEVEL
        if(commandInt==1) {
            g.println(" COMMANDINT 1");

            g.m.lvlMgr.getH().menuButtonCloscaTurtle  .setChecked(false);
            g.m.lvlMgr.getH().menuButtonSound         .setChecked(false);
            g.m.lvlMgr.getH().menuButtonMap           .setChecked(false);
            g.m.lvlMgr.getH().menuButtonTurtle        .setChecked(false);

            for (TextActor t:g.getTextactors().values()){
                t.setVisible(false);
            }

            g.m.he.setS(new Stage(new ScalingViewport(Scaling.stretch, g.m.he.getHudActorDataComponent().IMG_HUD.getWidth(),
                    g.m.he.getHudActorDataComponent().IMG_HUD.getHeight()),g.getSpriteBatch()));

            g.m.he.addTextActorsToStage();

            g.m.getLvlMgr().createMultiplexer();

            g.m.he.sclientHud.reset();
            g.m.gsys.spritemationsHost.reset();
            if(ac.Level<10) _lvlManager.changeMap("01");
            if(ac.Level>9 && ac.Level<19) _lvlManager.changeMap("02");
            if(ac.Level>18 && ac.Level<28) _lvlManager.changeMap("03");

            initGame=false;
            for(int i=0;i<g.m.he.getStageforHud().getActors().size;i++){
                g.m.he.getStageforHud().getActors().get(i).clearActions();
            }
            initGameGraphMainGame();

            _game.disposeAndCreateGeneralMenuScreen();

            commandInt=0;
            // DISPOSE CENTRALGAMESCREEN AND RECREATE
        }else if(commandInt==2){
            _game.disposeAndCreateCentralGameScreen();
            for(int i=0;i<g.m.he.getStageforHud().getActors().size;i++){
                g.m.he.getStageforHud().getActors().get(i).clearActions();
            }

            for(int i=0;i<g.m.he.getStageforHud().getActors().size;i++){
                g.m.he.getStageforHud().getActors().get(i).addAction(Actions.alpha(1));
            }
            for(int i=0;i<g.m.he.getStageforHud().getActors().size;i++){
                g.m.he.getStageforHud().getActors().get(i).setVisible(false);
            }

            g.m.lvlMgr.getH().menuButtonTurtle.setVisible(true);
            g.m.lvlMgr.getH().door_left.setVisible(true);
            g.m.lvlMgr.getH().door_right.setVisible(true);

            g.m.lvlMgr.getH().door_left.setPosition(0-1362/2,0);
            g.m.lvlMgr.getH().door_right.setPosition(1362,0);


            g.m.lvlMgr.getH().door_leftFront.setPosition(0-1362/2,0);
            g.m.lvlMgr.getH().door_rightFront.setPosition(1362,0);

            g.m.lvlMgr.getH().turtlebutton.setPosition(395,64);
            g.m.lvlMgr.getH().safykids.setPosition(560,289);

            g.m.lvlMgr.getH().menuButtonCloscaTurtle  .setChecked(false);

            g.m.lvlMgr.getH().menuButtonMap           .setChecked(false);
            g.m.lvlMgr.getH().menuButtonTurtle        .setChecked(false);


            g.m.lvlMgr.getH().controlButton=false;

            commandInt=0;






        }

    }




    public synchronized boolean config() {



        _lvlManager.config(_camera, g, this);

        g.m.lvlMgr.addEngineContent();

        iaAccess.setMainGameScreenData(
                _game,
                _lvlManager.get_multiplexer(),
                _camera,
                _lvlManager.get_mapManager(),
                _lvlManager.get_mapRenderer(),
                _lvlManager.get_mapManager().get_currentMap().get_layersFirst(),
                _lvlManager.get_mapManager().get_currentMap().get_layersLast(),
                _lvlManager.getCamerainputprocessor(),
                _lvlManager.getCameragesturedetectorprocessor(),
                g.m.lvlMgr.getEngine(),
                _lvlManager.getH(),
                _lvlManager.getH_system(),
                _lvlManager.getG(),
                _lvlManager.getPlayer(),
                _lvlManager.get_currentLvl().get_lvlEntities(),
                _lvlManager.getG_system(),
                _lvlManager.get_sCacheMap(),
                iamaster
        );

        g.setResizeFactorX(Gdx.graphics.getWidth() / _lvlManager.getH().getStage().getWidth());
        g.setResizeFactorY(Gdx.graphics.getHeight() / _lvlManager.getH().getStage().getHeight());

        Safe.set_loadedMainGame(true);

        _game.get_ProfileScreen().resume();

        return true;
    }

    public void initGameGraphMainGame(){

        ac= ProfileScren.ac;
        acCharger= ProfileScren.acCharger;
        AllConfigsCharger.g =g;

        if(!initGame) {
            //INTRODUCE CONFIGS:

            g.println(TAG + " ACLOAD= "+ ProfileScren.acLoad + " ac.PlayerType=" + ac.PlayerType);

            if(ProfileScren.acLoad==false) {
                ac.Age = AgeScreen.age;
                if(_game.get_ProfileScreen().chooseBart) {
                    ac.PlayerType = "boy";
                    ac.actualprofile="boy";
                }
                if(_game.get_ProfileScreen().chooseLisa) {
                    ac.PlayerType = "girl";
                    ac.actualprofile="girl";
                }

                ac.Lvl_id = _lvlManager.get_currentLvl().get_lvlID();
            }

            g.gm.sendMessage("GAMEGRAPH#GO#0#SHOOTER#LEVEL#START", null, -1);
            g.println(" g.gm.sendMessage : GAMEGRAPH#GO#0#SHOOTER#LEVEL#START");
           
            initGame = true;
            _gameState = GameState.RUNNING;
        }
    }



    //RESIZE
    @Override
    public void resize(int width, int height) {
        if(Safe.is_loadedMainGame()) {

            Vector2 size = Scaling.stretch.apply(1362, 790, width, height);
            int viewportX = (int) (width - size.x) / 2;
            int viewportY = (int) (height - size.y) / 2;
            int viewportWidth = (int) size.x;
            int viewportHeight = (int) size.y;
            Gdx.gl.glViewport(viewportX, viewportY, viewportWidth, viewportHeight);


            setupViewport(VIEWPORT_WIDTH_SETUP, VIEWPORT_HEIGHT_SETUP);

            //_camera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);

            if (!_game.isAndroid()) {
                _lvlManager.getH().getStageforHud().getViewport().setScreenSize(viewportWidth, viewportHeight);
                _lvlManager.getH().getStageforHud().getViewport().setScreenPosition((int) blackSpaceX, (int) blackSpaceY);
                _lvlManager.getH().getStage().getViewport().setScreenSize(viewportWidth, viewportHeight);
                _lvlManager.getH().getStage().getViewport().setScreenPosition((int) blackSpaceX, (int) blackSpaceY);

                resizeFactor.x = viewportWidth / oldWidth;
                resizeFactor.y = viewportHeight / oldHeight;
                oldWidth = viewportWidth;
                oldHeight = viewportHeight;
                if (viewportX != blackSpaceX) blackSpaceX = viewportX;
                if (viewportY != blackSpaceY) blackSpaceY = viewportY;

                g.setResizeFactorX(Gdx.graphics.getWidth() / _lvlManager.getH().getStage().getWidth());
                g.setResizeFactorY(Gdx.graphics.getHeight() / _lvlManager.getH().getStage().getHeight());
            }
            g.m.lvlMgr.get_acton().setFirstAdjustedTextBox(true);
        }
    }

    //SHOW
    @Override
    public void show() {

        if(Safe.is_loadedMainGame()) {
            _gameState = GameState.RUNNING;
            Gdx.input.setInputProcessor(_lvlManager.get_multiplexer());
        }


    }

    //PAUSE
    @Override
    public void pause() {

        _gameState = GameState.PAUSED;
        //com.mygdx.safe.profile.ProfileManager.getInstance().saveProfile();
    }

    //RESUME
    @Override
    public void resume() {
        _gameState = GameState.RUNNING;
        //com.mygdx.safe.profile.ProfileManager.getInstance().loadProfile();
    }

    //HIDE
    @Override
    public void hide() {

        _gameState = GameState.PAUSED;
        Gdx.input.setInputProcessor(null);
    }

    //DISPOSE
    @Override
    public void dispose() {

        _lvlManager.getGgMgr().closeFiles();
        _lvlManager.get_mapRenderer().dispose();
        g.m.lvlMgr.setEngine(null);
    }

    /*_______________________________________________________________________________________________________________*/

    //MAP RENDERER
    public void MapRenderer(MapRenderer _mapRenderer, OrthographicCamera _camera, int [] _layersForRender){
        _mapRenderer.setView(_camera);
        _camera.setToOrtho(false, VIEWPORT.viewportWidth, VIEWPORT.viewportHeight);
        _mapRenderer.render(_layersForRender); // CONTROLLING THE RENDERMAPP (FOR LAYER)
    }

    //SETUP VIEWPORT
    public void setupViewport(int width, int height){

        //Make the viewport a percentage of the total display area
        VIEWPORT.virtualWidth = width;
        VIEWPORT.virtualHeight = height;
        VIEWPORT.viewportWidth = VIEWPORT.virtualWidth;
        VIEWPORT.viewportHeight = VIEWPORT.virtualHeight;
        VIEWPORT.physicalWidth = Gdx.graphics.getWidth();
        VIEWPORT.physicalHeight = Gdx.graphics.getHeight();

        VIEWPORT.aspectRatio = (VIEWPORT.virtualWidth / VIEWPORT.virtualHeight);

        if( VIEWPORT.physicalWidth / VIEWPORT.physicalHeight >= VIEWPORT.aspectRatio){
            VIEWPORT.viewportWidth = VIEWPORT.viewportHeight * (VIEWPORT.physicalWidth/VIEWPORT.physicalHeight);
            VIEWPORT.viewportHeight = VIEWPORT.virtualHeight;
        }else{
            VIEWPORT.viewportWidth = VIEWPORT.virtualWidth;
            VIEWPORT.viewportHeight = VIEWPORT.viewportWidth * (VIEWPORT.physicalHeight/VIEWPORT.physicalWidth);
        }

        g.println(TAG+ "WorldRenderer: virtual: (" + VIEWPORT.virtualWidth + "," + VIEWPORT.virtualHeight + ")" );
        g.println(TAG+ "WorldRenderer: viewport: (" + VIEWPORT.viewportWidth + "," + VIEWPORT.viewportHeight + ")" );
        g.println(TAG+ "WorldRenderer: physical: (" + VIEWPORT.physicalWidth + "," + VIEWPORT.physicalHeight + ")" );
    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS
    public InputMultiplexer get_multiplexer() {  return _lvlManager.get_multiplexer(); }

    public OrthographicCamera get_camera() {
        return _camera;
    }

    public IAAccessClass getIaAccess() {
        return iaAccess;
    }

    public static GameState get_gameState() {
        return _gameState;
    }

    public Safe get_game() {
        return _game;
    }

    public LevelManager get_lvlManager() {
        return _lvlManager;
    }

    public com.mygdx.safe.IA.IAMasterControl getIamaster() {
        return iamaster;
    }

    public float getControlframes() {
        return controlframes;
    }

    public float getControldelta() {
        return controldelta;
    }

    public int getFramesmap() {
        return framesmap;
    }

    public int getUpdatesprocessor() {
        return updatesprocessor;
    }

    //SETTERS
    public void setIaAccess(IAAccessClass iaAccess) {
        this.iaAccess = iaAccess;
    }

    public static void set_gameState(GameState _gameState) {
        MainGameScreen._gameState = _gameState;
    }

    public void set_camera(OrthographicCamera _camera) {
        this._camera = _camera;
    }

    public void set_game(Safe _game) {
        this._game = _game;
    }

    public void set_lvlManager(LevelManager _lvlManager) {
        this._lvlManager = _lvlManager;
    }

    public void setIamaster(com.mygdx.safe.IA.IAMasterControl iamaster) {
        this.iamaster = iamaster;
    }

    public void setControlframes(float controlframes) {
        this.controlframes = controlframes;
    }

    public void setControldelta(float controldelta) {
        this.controldelta = controldelta;
    }

    public void setFramesmap(int framesmap) {
        this.framesmap = framesmap;
    }

    public void setUpdatesprocessor(int updatesprocessor) {
        this.updatesprocessor = updatesprocessor;
    }

    public GenericMethodsInputProcessor getG() {
        return g;
    }

}