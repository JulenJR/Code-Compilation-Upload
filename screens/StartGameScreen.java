package com.mygdx.safe.screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.mygdx.safe.AllConfigs;
import com.mygdx.safe.AllConfigsCharger;
import com.mygdx.safe.Safe;
import com.mygdx.safe.SoundMusicMation;
import com.mygdx.safe.SoundMusicMationsCharger;
import com.mygdx.safe.Utility;

public class StartGameScreen implements Screen {

    //TAG
    private static final String TAG = StartGameScreen.class.getSimpleName();

    //STAGE
    private Stage _stage;

    //TRICEPTION
    private Safe _game;

    private float ftimer=0;
    private boolean counftimer=false;
    private Image turtlebutton;
    private Image safykids;
    private Label loading;
    private boolean createMainScreenGame=false;

    // SOUNDMUSICMATIONS SOUNDMUSIC BANK

    private SoundMusicMationsCharger smC;//=new SoundMusicMationsCharger();
    public SoundMusicMation smm; //= smC.getSoundMusicMations();

    //AllConfigs
    public static AllConfigs ac;
    public static AllConfigsCharger acCharger= new AllConfigsCharger();

    Image door_left;
    Image door_right;


    SequenceAction ac2;
    SequenceAction ac3;

    boolean sound1Bool=false;
    boolean sound2Bool=false;
    boolean sound3Bool=false;
    boolean count4Bool=false;

    float countInitRender=0;
    boolean countInitRenderbool=false;

    Image initbackgroundScreen;

    Music s1;
    Music s2;
    Music s3;

    RepeatAction re;
    SequenceAction sa;
    SequenceAction sa1;



    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public StartGameScreen(Safe game){

        ac=acCharger.getAllConfigs();
        _game = game;

        Gdx.graphics.setContinuousRendering(true);
        //creation
        smC=new SoundMusicMationsCharger();
        smm = smC.getSoundMusicMations();
        _game.setSmC(smC);
        _game.setSmm(smm);

        s1 = _game.smm.loadMusic("sound/clonckLVL1.ogg",false);
        s2 = _game.smm.loadMusic("sound/rotatingintroLVL1.ogg",false);
        s3 = _game.smm.loadMusic("sound/buttonclickLVL1.ogg",false);



        initbackgroundScreen= new Image(new Texture( Gdx.files.internal("SAFE/ART/GENERIC/SCREENS/backgroundinit.jpg")));
        initbackgroundScreen.setSize(1920,1080);

        turtlebutton = new Image(new Texture( Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_bottons/menu_botton_closcaturtle.png")));
        safykids=new Image(new Texture( Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_bottons/menu_botton_closcaturtle_avatar.png")));
        door_left= new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_doors/menu_door_left.png")));
        door_right= new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_doors/menu_door_right.png")));

        turtlebutton.setPosition(1920/2,1080/2);
        safykids.setPosition(1920/2-213/2,1080/2-103);

        door_left.setSize(1920,1080);
        door_right.setSize(1920,1080);

        turtlebutton.setOrigin(Align.center);
        safykids.setOrigin(Align.center);

        turtlebutton.setPosition(2800,-410);//1362/2,790/2-1080/790*turtlebutton.getHeight()/3);

        initbackgroundScreen.setPosition(0,0);

        door_left.setPosition(-1920/2,0);
        door_right.setPosition(1920,0);

        loading=new Label(" LOADING...", Utility.STATUSUI_SKIN);
        loading.setPosition(1920*9/26,1080*1/8);
        loading.setFontScale(5,3);

        safykids.setVisible(false);
        loading.setVisible(false);

        ac2=new SequenceAction();
        ac2.addAction(new ParallelAction(Actions.moveBy(5,8,0.15f),Actions.scaleTo(0.9f,0.9f,0.15f)));
        ac2.addAction(new ParallelAction(Actions.moveBy(-5,-8,0.15f),Actions.scaleTo(1f,1f,0.15f)));

        ac3=new SequenceAction();
        ac3.addAction(new ParallelAction(Actions.moveBy(5,8,0.15f),Actions.scaleTo(0.9f,0.9f,0.15f)));
        ac3.addAction(new ParallelAction(Actions.moveBy(-5,-8,0.15f),Actions.scaleTo(1f,1f,0.15f)));

        SequenceAction sq1=new SequenceAction();
        sq1.addAction(Actions.moveBy(10,0,0.2f));
        sq1.addAction(Actions.moveBy(-10,0,0.2f));
        re=new RepeatAction();
        re.setAction(sq1);
        re.setCount(RepeatAction.FOREVER);

        sa=new SequenceAction();
        sa.addAction(Actions.delay(0.9f));
        sa.addAction(Actions.scaleTo(0.1f,1f,0.05f));
        sa.addAction(Actions.scaleTo(1f,1f,0.05f));
        sa.addAction(Actions.scaleTo(0.1f,1f,0.02f));
        sa.addAction(Actions.scaleTo(1f,1f,0.02f));
        sa.addAction(Actions.scaleTo(0.1f,1f,0.02f));
        sa.addAction(Actions.scaleTo(1f,1f,0.02f));
        // 1 second
        sa.addAction(Actions.scaleTo(0.1f,1f,0.02f));
        sa.addAction(Actions.scaleTo(1f,1f,0.02f));
        sa.addAction(Actions.scaleTo(0.1f,1f,0.05f));
        sa.addAction(Actions.scaleTo(1f,1f,0.05f));
        sa.addAction(Actions.scaleTo(0.1f,1f,0.1f));
        sa.addAction(Actions.scaleTo(1f,1f,0.2f));

        sa1=new SequenceAction();

        sa1.addAction(Actions.delay(0.9f));
        sa1.addAction(Actions.scaleTo(0.01f,1f,0.05f));
        sa1.addAction(Actions.scaleTo(1f,1f,0.05f));
        sa1.addAction(Actions.scaleTo(0.01f,1f,0.02f));
        sa1.addAction(Actions.scaleTo(1f,1f,0.02f));
        sa1.addAction(Actions.scaleTo(0.01f,1f,0.02f));
        sa1.addAction(Actions.visible(true));
        sa1.addAction(Actions.scaleTo(1f,1f,0.02f));

        // 1 second
        sa1.addAction(Actions.scaleTo(0.01f,1f,0.02f));
        sa1.addAction(Actions.scaleTo(1f,1f,0.02f));
        sa1.addAction(Actions.scaleTo(0.01f,1f,0.05f));
        sa1.addAction(Actions.scaleTo(1f,1f,0.05f));
        sa1.addAction(Actions.scaleTo(0.01f,1f,0.1f));
        sa1.addAction(Actions.scaleTo(1f,1f,0.2f));

        _game.createMainGameScreen();

        _stage = new Stage(new ScalingViewport(Scaling.stretch,1920,1080),game.spriteBatch);



        _stage.addActor(initbackgroundScreen);
        _stage.addActor(door_left);
        _stage.addActor(door_right);
        _stage.addActor(turtlebutton);
        _stage.addActor(safykids);
        _stage.addActor(loading);

        Safe.set_loadingStartGame(true);

    }

    //RENDER
    @Override
    public void render(float delta) {

        if(Safe.is_loadingStartGame() && Safe.is_loadingProfile() && Safe.is_loadingAge() && Safe.is_loadingGeneralMenu() && Safe.is_loadingCentralGameScreen() && !countInitRenderbool && !counftimer ) {
            countInitRenderbool = true;


            //initbackgroundScreen.addAction(new SequenceAction(Actions.alpha(0),Actions.fadeIn(0.3f),Actions.fadeOut(0.3f),Actions.visible(false)));
            initbackgroundScreen.addAction(new SequenceAction(Actions.alpha(0),Actions.fadeIn(0.3f)));
        }


        if(countInitRenderbool && !counftimer){
            countInitRender+=delta;
            if(countInitRender>2.5){
               counftimer=true;
            }
        }

        if(counftimer) {
            ftimer += delta;
            if(createMainScreenGame==false) {

                if (ftimer > 0.3 && !sound1Bool) {


                    loading.addAction(re);
                    turtlebutton.addAction(Actions.scaleTo(6,6,0.5f));
                    turtlebutton.addAction(new SequenceAction(Actions.delay(0.5f),new ParallelAction(Actions.scaleTo(1,1,0.3f),Actions.moveTo(1362/2,790/2-1080/790*592/3,0.3f))));
                    turtlebutton.addAction(sa);
                    safykids.addAction(sa1);
                    s1.play();
                    s1.setVolume(0.3f);
                    sound1Bool=true;
                    door_left.addAction(Actions.moveTo(0,0,0.6f));
                    door_right.addAction(Actions.moveTo(0,0,0.6f));


                }

                if (ftimer > 1.1 && !sound2Bool) {
                    s2.play();
                    s2.setVolume(0.3f);
                    sound2Bool=true;

                }

                if (ftimer > 2.0 && !sound3Bool) {
                    //Listeners
                    turtlebutton.addListener(new InputListener() {
                                                 @Override
                                                 public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                                     turtlebutton.addAction(ac2);
                                                     safykids.addAction(ac3);
                                                     s3.play();
                                                     s3.setVolume(0.3f);
                                                     loading.setVisible(true);
                                                     createMainScreenGame = true;
                                                     ftimer = 0;
                                                     return true;
                                                 }
                                             }
                    );

                    safykids.addListener(new InputListener() {
                                             @Override
                                             public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                                 turtlebutton.addAction(ac2);
                                                 safykids.addAction(ac3);
                                                 s3.play();
                                                 s3.setVolume(0.3f);
                                                 loading.setVisible(true);
                                                 createMainScreenGame = true;
                                                 ftimer = 0;

                                                 //
                                                 return true;
                                             }
                                         }
                    );
                    sound3Bool=true;

                }
            }else{
                if(ftimer > 0.99 && !count4Bool){
                    counftimer=false;
                    _game.configMainGameScreen();
                    _game.setScreen(_game.getScreenType(Safe.ScreenType.ProfileScreen));
                    count4Bool=true;

                }
            }
        }




        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        _stage.act(delta);
        _stage.draw();
    }

    //RESIZE
    @Override
    public void resize(int width, int height) {

        float percentage=1088/1920;
        float newPercentage=height/width;
        int newWidth;
        int newHeight;
        if(1/percentage>1/newPercentage) {
            newWidth = width; newHeight=(int)(width*percentage);
        }else  if(1/percentage<1/newPercentage){
            newWidth = (int)(height/percentage); newHeight=height;
        }else{
            newWidth=width;
            newHeight=height;
        }
        _stage.getViewport().setScreenSize(newWidth,newHeight);
        _stage.getViewport().update(newWidth,newHeight);
    }

    //SHOW
    @Override
    public void show() {
        Gdx.input.setInputProcessor(_stage);
    }

    //HIDE
    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    //PAUSE
    @Override
    public void pause() {
    }

    //RESUME
    @Override
    public void resume() {
        show();
        render(Gdx.graphics.getDeltaTime());
    }

    //DISPOSE
    @Override
    public void dispose() {
        _stage.dispose();
    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS
    public Stage get_stage() {
        return _stage;
    }

    public Safe get_game() {
        return _game;
    }

    //SETTERS
    public void set_stage(Stage _stage) {
        this._stage = _stage;
    }

    public void set_game(Safe _game) {
        this._game = _game;
    }
}


