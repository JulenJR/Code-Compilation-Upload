package com.mygdx.safe.screens;

/**
 * Created by Boris.InspiratGames on 9/03/18.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.mygdx.safe.AllConfigs;
import com.mygdx.safe.AllConfigsCharger;
import com.mygdx.safe.Safe;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.mygdx.safe.SoundMusicMation;
import com.mygdx.safe.SoundMusicMationsCharger;
import com.mygdx.safe.Utility;

public class ProfileScren implements Screen {


    //TAG
    private static final String TAG = GeneralMenuScreen.class.getSimpleName();

    //STAGE
    private Stage _stage;

    //TRICEPTION
    private Safe _game;

    private int selection=0; // 0=none // 1=NEW // 2=LOAD // 3=EXIT
    private float counterdelta=0;
    public static boolean acLoad=false;

    private Image turtlebutton;
    private Image safykids;
    private Image goButtonA;
    private Image goButtonB;
    private Image chooseProfile;

    private Image door_left;
    private Image door_right;
    private Image back_door_left;
    private Image back_door_right;
    private Image super_back_door_left;
    private Image super_back_door_right;



    private Image bartprofileA;
    private Image lisaprofileA;

    private Image bartprofileB;
    private Image lisaprofileB;

    private Image bartprofileC;
    private Image lisaprofileC;

    private Image bartprofileD;
    private Image lisaprofileD;

    private Label loading;

    boolean disableDoors=false;
    boolean sound1Bool=false;


    // SOUNDMUSICMATIONS SOUNDMUSIC BANK

    private SoundMusicMationsCharger smC;
    public SoundMusicMation smm;
    //AllConfigs
    public static AllConfigs ac;
    public static AllConfigsCharger acCharger;

    SequenceAction ac2;
    SequenceAction ac3;
    SequenceAction ac4;
    SequenceAction ac5;
    SequenceAction ac6;
    SequenceAction ac7;
    SequenceAction ac8;
    SequenceAction ac9;
    SequenceAction ac10;
    SequenceAction ac11;

    public boolean chooseLisa=false;
    public boolean chooseBart=false;
    public boolean prechooseLisa=false;
    public boolean prechooseBart=false;
    public boolean existprofileLisa=false;
    public boolean existprofileBart=false;

    public static boolean firstresume=false;
    float firstresumefloat=0;

    float countersoundtimefloat=0;
    boolean countersoundtime=false;

    Label currentBart;
    Label currentLisa;

    boolean currentListenerBart=false;
    boolean currentListenerLisa=false;
    boolean currentListenerTurtleGo=false;

    float currentCounterListenerLisa=0;
    float currentCounterListenerBart=0;
    float currentCounterListenerTurtleGo=0;

    SequenceAction sa;
    SequenceAction sa1;
    SequenceAction sa2;
    SequenceAction sa3;

    Music m1;
    Music m2;
    Music s3;
    Music s4;


    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public ProfileScren(Safe game){

        _game = game;


        smC=_game.smC;
        smm = _game.smm;

        m1 = _game.smm.loadMusic("sound/rotatingintroProfileLVL1.ogg",false);
        m2 = _game.smm.loadMusic("sound/rasssLVL1.ogg",false);
        s3 = _game.smm.loadMusic("sound/buttonclickLVL1.ogg",false);
        s4 = _game.smm.loadMusic("sound/picLVL1.ogg",false);


        turtlebutton = new Image(new Texture( Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_bottons/menu_botton_closcaturtle.png")));
        safykids=new Image(new Texture( Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_bottons/menu_botton_closcaturtle_avatar.png")));

        goButtonA=new Image(new Texture( Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_profile/menu_profile_go_A.png")));
        goButtonB=new Image(new Texture( Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_profile/menu_profile_go_B.png")));

        chooseProfile=new Image(new Texture( Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_profile/menu_profile_choose.png")));

        bartprofileA= new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_profile/menu_profile_choose_Bart_A.png")));
        lisaprofileA= new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_profile/menu_profile_choose_lisa_A.png")));

        bartprofileB= new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_profile/menu_profile_choose_Bart_B.png")));
        lisaprofileB= new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_profile/menu_profile_choose_lisa_B.png")));

        bartprofileC= new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_profile/menu_profile_choose_Bart_C.png")));
        lisaprofileC= new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_profile/menu_profile_choose_lisa_C.png")));

        bartprofileD= new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_profile/menu_profile_choose_Bart_D.png")));
        lisaprofileD= new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_profile/menu_profile_choose_lisa_D.png")));

        door_left= new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_doors/menu_door_left.png")));
        door_right= new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_doors/menu_door_right.png")));

        back_door_left= new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_profile/DOOR_CHOSE-BART.png")));
        back_door_right= new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_profile/DOOR_CHOSE-LISA.png")));

        super_back_door_left=new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_doors/menu_door_left.png")));
        super_back_door_right=new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_doors/menu_door_right.png")));

        loading=new Label(" LOADING...", Utility.STATUSUI_SKIN);
        loading.setPosition(1920*9/26*1362/1920+10,1080*1/8*790/1080);
        loading.setFontScale(5*1362/1920+0.1f,3*790/1080+0.1f);


        turtlebutton.setPosition(395,64);
        safykids.setPosition(560,289);


        turtlebutton.setOrigin(Align.center);
        safykids.setOrigin(Align.center);

        turtlebutton.setScale(0.75f,0.75f);
        safykids.setScale(0.75f,0.75f);

        back_door_left.setPosition(-5,0);
        back_door_right.setPosition(1362/2,0);

        door_left.setPosition(0,0);
        door_right.setPosition(0,0);

        super_back_door_left.setPosition(0,0);
        super_back_door_right.setPosition(0,0);

        goButtonA.setPosition(545,292);
        goButtonB.setPosition(545,292);

        bartprofileA.setPosition(91,25);
        bartprofileB.setPosition(91,83);
        bartprofileC.setPosition(91,83);
        bartprofileD.setPosition(91,83);

        lisaprofileA.setPosition(917,25);
        lisaprofileB.setPosition(917,83);
        lisaprofileC.setPosition(917,83);
        lisaprofileD.setPosition(917,83);

        chooseProfile.setPosition(543,611);

        goButtonB.setVisible(false);
        goButtonA.setVisible(false);

        bartprofileD.setVisible(false);
        bartprofileC.setVisible(false);
        bartprofileB.setVisible(false);

        lisaprofileD.setVisible(false);
        lisaprofileC.setVisible(false);
        lisaprofileB.setVisible(false);

        sa=new SequenceAction();

        sa.addAction(Actions.delay(0.25f));
        sa.addAction(Actions.scaleTo(1,1,0.1f));
        sa.addAction(Actions.scaleTo(0.1f,1f,0.05f));
        sa.addAction(Actions.scaleTo(1f,1f,0.05f));
        sa.addAction(Actions.scaleTo(0.1f,1f,0.05f));
        sa.addAction(Actions.scaleTo(1f,1f,0.05f));
        sa.addAction(Actions.scaleTo(0.1f,1f,0.05f));
        sa.addAction(Actions.scaleTo(1f,1f,0.05f));

        sa1=new SequenceAction();

        sa1.addAction(Actions.delay(0.25f));
        sa1.addAction(Actions.scaleTo(1,1,0.1f));
        sa1.addAction(Actions.scaleTo(0.1f,1f,0.05f));
        sa1.addAction(Actions.scaleTo(1f,1f,0.05f));
        sa1.addAction(Actions.scaleTo(0.1f,1f,0.05f));
        sa1.addAction(Actions.scaleTo(1f,1f,0.05f));

        sa1.addAction(Actions.scaleTo(0.1f,1f,0.05f));
        sa1.addAction(Actions.visible(false));
        sa1.addAction(Actions.scaleTo(1f,1f,0.05f));

        sa2=new SequenceAction();

        sa2.addAction(Actions.delay(0.3f));
        sa2.addAction(Actions.scaleTo(0.1f,1f,0.05f));
        sa2.addAction(Actions.scaleTo(1f,1f,0.05f));
        sa2.addAction(Actions.scaleTo(0.1f,1f,0.05f));
        sa2.addAction(Actions.scaleTo(1f,1f,0.05f));

        sa2.addAction(Actions.scaleTo(0.1f,1f,0.05f));
        sa2.addAction(Actions.visible(true));
        sa2.addAction(Actions.scaleTo(1f,1f,0.05f));

        sa3=new SequenceAction();
        sa3.addAction(Actions.scaleTo(1,0.02f,0.15f));
        sa3.addAction(Actions.visible(false));

        ac2=new SequenceAction();
        ac2.addAction(new ParallelAction(Actions.moveBy(5,8,0.15f),Actions.scaleTo(0.9f,0.9f,0.15f)));
        ac2.addAction(new ParallelAction(Actions.moveBy(-5,-8,0.15f),Actions.scaleTo(1f,1f,0.15f)));

        ac3=new SequenceAction();
        ac3.addAction(new ParallelAction(Actions.moveBy(5,8,0.15f),Actions.scaleTo(0.9f,0.9f,0.15f)));
        ac3.addAction(new ParallelAction(Actions.moveBy(-5,-8,0.15f),Actions.scaleTo(1f,1f,0.15f)));

        ac4=new SequenceAction();
        ac4.addAction(new ParallelAction(Actions.moveBy(5,8,0.15f),Actions.scaleTo(0.9f,0.9f,0.15f)));
        ac4.addAction(new ParallelAction(Actions.moveBy(-5,-8,0.15f),Actions.scaleTo(1f,1f,0.15f)));

        ac5=new SequenceAction();
        ac5.addAction(new ParallelAction(Actions.moveBy(5,8,0.15f),Actions.scaleTo(0.9f,0.9f,0.15f)));
        ac5.addAction(new ParallelAction(Actions.moveBy(-5,-8,0.15f),Actions.scaleTo(1f,1f,0.15f)));

        ac6=new SequenceAction();
        ac6.addAction(new ParallelAction(Actions.moveBy(5,8,0.15f),Actions.scaleTo(0.9f,0.9f,0.15f)));
        ac6.addAction(new ParallelAction(Actions.moveBy(-5,-8,0.15f),Actions.scaleTo(1f,1f,0.15f)));

        ac7=new SequenceAction();
        ac7.addAction(new ParallelAction(Actions.moveBy(5,8,0.15f),Actions.scaleTo(0.9f,0.9f,0.15f)));
        ac7.addAction(new ParallelAction(Actions.moveBy(-5,-8,0.15f),Actions.scaleTo(1f,1f,0.15f)));

        ac8=new SequenceAction();
        ac8.addAction(new ParallelAction(Actions.moveBy(5,8,0.15f),Actions.scaleTo(0.9f,0.9f,0.15f)));
        ac8.addAction(new ParallelAction(Actions.moveBy(-5,-8,0.15f),Actions.scaleTo(1f,1f,0.15f)));

        ac9=new SequenceAction();
        ac9.addAction(new ParallelAction(Actions.moveBy(5,8,0.15f),Actions.scaleTo(0.9f,0.9f,0.15f)));
        ac9.addAction(new ParallelAction(Actions.moveBy(-5,-8,0.15f),Actions.scaleTo(1f,1f,0.15f)));

        ac10=new SequenceAction();
        ac10.addAction(new ParallelAction(Actions.moveBy(5,8,0.15f),Actions.scaleTo(0.9f,0.9f,0.15f)));
        ac10.addAction(new ParallelAction(Actions.moveBy(-5,-8,0.15f),Actions.scaleTo(1f,1f,0.15f)));

        ac11=new SequenceAction();
        ac11.addAction(new ParallelAction(Actions.moveBy(5,8,0.15f),Actions.scaleTo(0.9f,0.9f,0.15f)));
        ac11.addAction(new ParallelAction(Actions.moveBy(-5,-8,0.15f),Actions.scaleTo(1f,1f,0.15f)));


        acCharger=StartGameScreen.acCharger;
        ac=StartGameScreen.ac;

        if(ac.profileboy.setprofile) {
            currentBart = new Label("ACTUAL LEVEL:  " + ac.profileboy.Level + "\n" + "POINTS:  " + ac.profileboy.points, Utility.STATUSUI_SKIN);
            currentBart.setFontScale(1.1f, 0.7f);
            currentBart.setPosition(91+65,8);
            currentBart.setVisible(false);
        }
        if(ac.profilegirl.setprofile) {
            currentLisa = new Label("ACTUAL LEVEL:  " + ac.profilegirl.Level + "\n" + "POINTS:  " + ac.profilegirl.points, Utility.STATUSUI_SKIN);
            currentLisa.setPosition(917+65,8);
            currentLisa.setFontScale(1.1f, 0.7f);
            currentLisa.setVisible(false);
        }

        _stage = new Stage(new ScalingViewport(Scaling.stretch,1362,790),game.spriteBatch);
        _stage.addActor(super_back_door_left);
        _stage.addActor(super_back_door_right);
        _stage.addActor(back_door_left);
        _stage.addActor(back_door_right);
        _stage.addActor(chooseProfile);
        if(currentBart!=null)_stage.addActor(currentBart);
        if(currentLisa!=null)_stage.addActor(currentLisa);
        _stage.addActor(door_left);
        _stage.addActor(door_right);
        _stage.addActor(lisaprofileA);
        _stage.addActor(lisaprofileB);
        _stage.addActor(lisaprofileC);
        _stage.addActor(lisaprofileD);
        _stage.addActor(bartprofileA);
        _stage.addActor(bartprofileB);
        _stage.addActor(bartprofileC);
        _stage.addActor(bartprofileD);
        door_right.toFront();
        door_left.toFront();
        _stage.addActor(turtlebutton);
        _stage.addActor(goButtonA);
        _stage.addActor(goButtonB);
        _stage.addActor(safykids);
        _stage.addActor(loading);


        Safe.set_loadingProfile(true);

    }

    //RENDER
    @Override
    public void render(float delta) {


            if (firstresume && firstresumefloat < 10)
                firstresumefloat += Gdx.graphics.getDeltaTime();

            if (countersoundtime) {
                countersoundtimefloat += delta;
                if (countersoundtimefloat > 0.1 && !sound1Bool) {

                    m1.play();
                    m1.setVolume(0.3f);
                    sound1Bool = true;

                }

                if (countersoundtimefloat > 0.7 && !disableDoors) {
                    //DISABLEDOORS

                    door_left.toBack();
                    door_right.toBack();
                    super_back_door_right.toBack();
                    super_back_door_left.toBack();
                    door_left.setVisible(false);
                    door_right.setVisible(false);
                    disableDoors = true;


                }
                if (countersoundtimefloat > 0.8) {
                    countersoundtime = false;
                }


            }

            if (firstresume && firstresumefloat > 3.5) {
                firstresume = false;
                countersoundtime = true;


                m2.play();
                m2.setVolume(0.3f);

                door_left.addAction(Actions.moveBy(-800, 0, 0.65f));
                door_right.addAction(Actions.moveBy(+800 , 0, 0.65f));

                turtlebutton.addAction(sa);
                safykids.addAction(sa1);
                goButtonA.addAction(sa2);

                loading.addAction(Actions.moveBy(1362, 0, 0.6f));

                //Listeners
                turtlebutton.addListener(new InputListener() {
                                             @Override
                                             public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                                                 if (chooseBart || chooseLisa) {
                                                     if (!currentListenerTurtleGo) {
                                                         currentListenerTurtleGo = true;

                                                         turtlebutton.clearActions();
                                                         goButtonA.clearActions();

                                                         ac2.restart();
                                                         ac3.restart();
                                                         turtlebutton.addAction(ac2);
                                                         goButtonA.addAction(ac3);
                                                         goButtonB.addAction(new SequenceAction(Actions.delay(0.15f), Actions.visible(true), Actions.delay(0.05f), Actions.visible(false)));
                                                         s3.play();
                                                         s3.setVolume(0.3f);

                                                         opendoors();



                                                     }
                                                 } else {

                                                     s4.play();
                                                     s4.setVolume(0.3f);
                                                     turtlebutton.clearActions();
                                                     turtlebutton.addAction(new SequenceAction(Actions.alpha(0.5f, 0.05f), Actions.alpha(1f, 0.05f)));
                                                 }

                                                 return true;
                                             }
                                         }
                );

                goButtonA.addListener(new InputListener() {
                                          @Override
                                          public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                                              if (chooseBart || chooseLisa) {
                                                  if (!currentListenerTurtleGo) {
                                                      currentListenerTurtleGo = true;
                                                      turtlebutton.clearActions();
                                                      goButtonA.clearActions();

                                                      ac2.restart();
                                                      ac3.restart();
                                                      turtlebutton.addAction(ac2);
                                                      goButtonA.addAction(ac3);
                                                      goButtonB.addAction(new SequenceAction(Actions.delay(0.15f), Actions.visible(true), Actions.delay(0.05f), Actions.visible(false)));

                                                      s3.play();
                                                      s3.setVolume(0.3f);

                                                      opendoors();

                                                  }
                                              } else {

                                                  turtlebutton.clearActions();
                                                  turtlebutton.addAction(new SequenceAction(Actions.alpha(0.5f, 0.05f), Actions.alpha(1f, 0.05f)));
                                                  s4.play();
                                                  s4.setVolume(0.3f);
                                              }
                                              return true;
                                          }
                                      }
                );

                bartprofileA.addListener(new InputListener() {
                                             @Override
                                             public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                                 if (!currentListenerBart) {
                                                     currentListenerBart = true;
                                                     System.out.println("BARPROFILEA");
                                                     bartprofileA.clearActions();


                                                     ac4.restart();
                                                     bartprofileA.addAction(ac4);

                                                     s3.play();
                                                     s3.setVolume(0.3f);

                                                 }

                                                 return true;
                                             }
                                         }
                );

                lisaprofileA.addListener(new InputListener() {
                                             @Override
                                             public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                                                 if (!currentListenerLisa) {
                                                     currentListenerLisa = true;
                                                     System.out.println("LISAPROFILEA");

                                                     lisaprofileA.clearActions();
                                                     ac9.restart();
                                                     lisaprofileA.addAction(ac9);

                                                     s3.play();
                                                     s3.setVolume(0.3f);

                                                 }
                                                 return true;
                                             }
                                         }
                );

                bartprofileC.addListener(new InputListener() {
                                             @Override
                                             public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                                                 if (!currentListenerBart) {
                                                     currentListenerBart = true;
                                                     System.out.println("BARPROFILEC");
                                                     bartprofileC.clearActions();

                                                     ac5.restart();
                                                     bartprofileC.addAction(ac5);

                                                     s3.play();
                                                     s3.setVolume(0.3f);

                                                 }
                                                 return true;
                                             }
                                         }
                );

                lisaprofileC.addListener(new InputListener() {
                                             @Override
                                             public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                                                 if (!currentListenerLisa) {
                                                     currentListenerLisa = true;
                                                     System.out.println("LISAPROFILEC");

                                                     lisaprofileC.clearActions();
                                                     ac6.restart();
                                                     lisaprofileC.addAction(ac6);

                                                     s3.play();
                                                     s3.setVolume(0.3f);

                                                 }
                                                 return true;
                                             }
                                         }
                );

                bartprofileD.addListener(new InputListener() {
                                             @Override
                                             public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                                                 if (!currentListenerBart) {
                                                     currentListenerBart = true;
                                                     System.out.println("BARPROFILED");
                                                     bartprofileD.clearActions();

                                                     ac7.restart();
                                                     bartprofileD.addAction(ac7);

                                                     s3.play();
                                                     s3.setVolume(0.3f);

                                                 }
                                                 return true;
                                             }
                                         }
                );

                lisaprofileD.addListener(new InputListener() {
                                             @Override
                                             public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                                 if (!currentListenerLisa) {
                                                     currentListenerLisa = true;
                                                     System.out.println("LISAPROFILED");

                                                     lisaprofileD.clearActions();
                                                     ac8.restart();
                                                     lisaprofileD.addAction(ac8);

                                                     s3.play();
                                                     s3.setVolume(0.3f);
                                                 }
                                                 return true;
                                             }
                                         }
                );

                System.out.println("AC ACTUALPROFILE=" + ac.actualprofile);
                if (ac.actualprofile.equalsIgnoreCase("girl")) {
                    prechooseLisa = true;
                    if (currentLisa != null) currentLisa.setVisible(true);
                    System.out.println("PRECHOOSING LISA");
                    acLoad=true;

                }
                if (ac.actualprofile.equalsIgnoreCase("boy")) {
                    prechooseBart = true;
                    if (currentBart != null) currentBart.setVisible(true);
                    System.out.println("PRECHOOSING BART");
                    acLoad=true;

                }

                if (ac.profileboy.setprofile) {
                    existprofileBart = true;
                }

                if (ac.profilegirl.setprofile) {
                    existprofileLisa = true;
                }


                if (existprofileLisa) {
                    System.out.println("EXIST PROFILE LISA");
                    lisaprofileD.setVisible(true);
                    lisaprofileA.setVisible(false);
                }

                if (existprofileBart) {
                    System.out.println("EXIST PROFILE BART");

                    bartprofileD.setVisible(true);
                    bartprofileA.setVisible(false);
                }

                if (!(existprofileBart && existprofileLisa)) {
                    System.out.println("PROFILES NOT EXIST");

                }
        }

        if(currentListenerBart){
            currentCounterListenerBart+=delta;
        }
        if(currentListenerLisa){
            currentCounterListenerLisa+=delta;

        }
        if(currentListenerTurtleGo){
            currentCounterListenerTurtleGo+=delta;
        }

        if(Math.abs(currentCounterListenerBart-0.55f)<0.01){
            currentCounterListenerBart=0;
            if(bartprofileA.isVisible() || bartprofileD.isVisible()){
                bartprofileA.setVisible(false);
                bartprofileD.setVisible(false);
                bartprofileC.setVisible(true);
                lisaprofileC.setVisible(false);
                lisaprofileA.setVisible(true);
                if(existprofileLisa) {
                    System.out.println("EXIST PROFILE BART");
                    lisaprofileD.setVisible(true);
                    lisaprofileA.setVisible(false);
                }

                chooseBart=true;
                ac.actualprofile="boy";
                ac.PlayerType="boy";
                chooseLisa=false;
            }else if(bartprofileC.isVisible() && chooseBart){
                bartprofileC.setVisible(false);
                bartprofileA.setVisible(true);
                if(existprofileBart) {
                    System.out.println("EXIST PROFILE BART");
                    bartprofileD.setVisible(true);
                    bartprofileA.setVisible(false);
                }
                chooseBart=false;
            }

            currentListenerBart=false;
        }
        if(Math.abs(currentCounterListenerLisa-0.55f)<0.01){
            currentCounterListenerLisa=0;
            if(lisaprofileA.isVisible() || lisaprofileD.isVisible()){
                lisaprofileA.setVisible(false);
                lisaprofileD.setVisible(false);
                lisaprofileC.setVisible(true);
                bartprofileC.setVisible(false);
                bartprofileA.setVisible(true);
                if(existprofileBart) {
                    System.out.println("EXIST PROFILE BART");
                    bartprofileD.setVisible(true);
                    bartprofileA.setVisible(false);
                }

                chooseLisa=true;
                ac.actualprofile="girl";
                ac.PlayerType="girl";
                chooseBart=false;
            }else if(lisaprofileC.isVisible() && chooseLisa){
                lisaprofileC.setVisible(false);
                lisaprofileA.setVisible(true);
                if(existprofileLisa) {
                    System.out.println("EXIST PROFILE BART");
                    lisaprofileD.setVisible(true);
                    lisaprofileA.setVisible(false);
                }
                chooseLisa=false;
            }

            currentListenerLisa=false;
        }

        if(Math.abs(currentCounterListenerTurtleGo-1.1f)<0.01){
            currentCounterListenerTurtleGo=0;
            currentListenerTurtleGo=false;
            if (chooseBart) {
                if (ac.profileboy.setprofile) {
                        _game.setScreen(_game.getScreenType(Safe.ScreenType.MainGame));
                } else {

                    _game.setScreen(_game.getScreenType(Safe.ScreenType.AgeScreen));
                }
            }
            if (chooseLisa) {
                if (ac.profilegirl.setprofile) {
                    _game.setScreen(_game.getScreenType(Safe.ScreenType.MainGame));
                } else {
                    _game.setScreen(_game.getScreenType(Safe.ScreenType.AgeScreen));
                }
            }
        }


        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        _stage.act(delta);
        _stage.draw();


    }

    void opendoors(){
        float movrightX=+800.0f;
        float movleftX=-800.0f;
        float durmov=0.5f;

        back_door_right.addAction(Actions.moveBy(movrightX,0,durmov));
        back_door_left.addAction(Actions.moveBy(movleftX,0,durmov));
        if(bartprofileA.isVisible())
            bartprofileA.addAction(Actions.moveBy(movleftX,0,durmov));
        if(bartprofileB.isVisible())
            bartprofileB.addAction(Actions.moveBy(movleftX,0,durmov));
        if(bartprofileC.isVisible())
            bartprofileC.addAction(Actions.moveBy(movleftX,0,durmov));
        if(bartprofileD.isVisible())
            bartprofileD.addAction(Actions.moveBy(movleftX,0,durmov));

        if(lisaprofileA.isVisible())
            lisaprofileA.addAction(Actions.moveBy(movrightX,0,durmov));
        if(lisaprofileB.isVisible())
            lisaprofileB.addAction(Actions.moveBy(movrightX,0,durmov));
        if(lisaprofileC.isVisible())
            lisaprofileC.addAction(Actions.moveBy(movrightX,0,durmov));
        if(lisaprofileD.isVisible())
            lisaprofileD.addAction(Actions.moveBy(movrightX,0,durmov));

        chooseProfile.addAction(Actions.moveBy(0,400,durmov));

        m2.play();
        m2.setVolume(0.3f);
    }

    //RESIZE
    @Override
    public void resize(int width, int height) {
        _stage.getViewport().setScreenSize(width, height);
        Gdx.input.setInputProcessor(_stage);
        System.out.println("RESIZE");
    }

    //SHOW
    @Override
    public void show() {
        Gdx.input.setInputProcessor(_stage);
        System.out.println("SHOW");
    }

    //HIDE
    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        System.out.println("HIDE");
    }

    //PAUSE
    @Override
    public void pause() {
        Gdx.input.setInputProcessor(_stage);
        System.out.println("PAUSE");
    }

    //RESUME
    @Override
    public void resume() {

        show();
        render(Gdx.graphics.getDeltaTime());
        System.out.println("RESUME");
        if(!firstresume && firstresumefloat==0) {

            System.out.println("FIRST RESUME");
            firstresume = true;
        }
    }

    //DISPOSE
    @Override
    public void dispose() {
        _stage.clear();
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
