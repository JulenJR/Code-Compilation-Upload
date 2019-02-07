package com.mygdx.safe.Entities;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleByAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.mygdx.safe.Components.HudDataComponent;
import com.mygdx.safe.InputProcessors.GenericGestureListener;
import com.mygdx.safe.InputProcessors.GenericInputListener;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;
import com.mygdx.safe.InputProcessors.SpritemationListener;
import com.mygdx.safe.MainGameGraph.Choicer;
import com.mygdx.safe.MainGameGraph.SpecialSuperTree;
import com.mygdx.safe.MainGameGraph.ToActionNode;
//import com.mygdx.safe.UI.inventoryItemSlot;
//import com.mygdx.safe.UI.inventoryPowerSlot;
import com.mygdx.safe.Conversation.TextActor;
import com.mygdx.safe.ParticleMation;
import com.mygdx.safe.ParticleMationCreator;
import com.mygdx.safe.Safe;
import com.mygdx.safe.SoundMusicMation;
import com.mygdx.safe.SpritemationEntityConfig;
import com.mygdx.safe.sPritemation;
import com.mygdx.safe.screens.ProfileScren;
import com.mygdx.safe.spritemationsClientForHud;
import com.mygdx.safe.screens.MainGameScreen;

import java.awt.Menu;
import java.util.HashMap;
import java.util.regex.Pattern;

import static com.mygdx.safe.screens.ProfileScren.ac;
import static java.lang.System.exit;

/**
 * Created by BORIS.INSPIRATGAMES on 29/05/17.
 */

public class HUD_Entity extends Entity {

    //TAG
    private static final String TAG = HUD_Entity.class.getSimpleName();

    //ASPECTS
    private GenericMethodsInputProcessor g;

    //STAGE
    private Stage s,stageforHud;

    //HUD DATA COMPONENT
    private HudDataComponent h;

    //LISTENERS
    private HashMap<String, GenericInputListener> _hudListeners;
    private HashMap<String, GenericGestureListener> _hudGestureListeners;

    private int POS_EMOX=970;
    private int POS_EMOY=60;

    private int EMO=0;
    public int EMOzindex=0;

    public spritemationsClientForHud sclientHud;
    public ParticleMationCreator pmc;

    // STAGE FOR HUD

    public ImageButton menuButtonTurtle,menuButtonMap,menuButtonSound,menuButtonCloscaTurtle;
    public Image menuButtonTurtleA,menuButtonTurtleB,menuButtonTurtleC;
    public Image menuButtonMapA,menuButtonMapB,menuButtonMapC;
    public Image menuButtonSoundA,menuButtonSoundB,menuButtonSoundC;
    public Image menuButtonCloscaTurtleA,menuButtonCloscaTurtleB,menuButtonCloscaTurtleC;


    public Image door_left,door_right,turtlebutton,safykids,t_Map1,t_Map2,t_Map3;
    public Image door_leftFront,door_rightFront;

    public boolean controlButton =false;
    private boolean checkcontrolButton =false;
    private int controlButtonInt=0;

    private boolean booleanTurtle1 =false;
    private boolean booleanTurtle2 =false;
    private boolean booleanTurtle3 =false;
    private boolean booleanTurtle4 =false;
    private boolean booleanTurtle5 =false;
    private boolean booleanTurtle6 =false;
    private boolean booleanTurtle7 =false;



    private float controlButtonCounter =0;

    // SOUNDMUSICMATIONS SOUNDMUSIC BANK

    public SoundMusicMation smm;

    Music m1,m2,m3,m4,m5,m6,m7,m8;


    SequenceAction sa,sa1;



    /*_______________________________________________________________________________________________________________*/

    public HUD_Entity(final GenericMethodsInputProcessor g,OrthographicCamera camera){
        this.g=g;
        this.pmc = new ParticleMationCreator(g);
        _hudListeners = new HashMap<String, GenericInputListener>();
        _hudGestureListeners = new HashMap<String, GenericGestureListener>();

        h=new HudDataComponent(this,camera, g);
        s=new Stage(new ScalingViewport(Scaling.stretch, h.IMG_HUD.getWidth(),h.IMG_HUD.getHeight()),g.getSpriteBatch());



        h.IMG_TALKING_ENTITY.setVisible(false);
        h.IMG_DIALOG_1.setVisible(false);
        h.IMG_DIALOG_2.setVisible(false);
        h.IMG_DIALOG_3.setVisible(false);

        //SET VISIBILITY ACTONICONIMAGES
        for(String s:h.imageActonHash.keySet()){
            h.imageActonHash.get(s).getSecond().setVisible(false);
        }


        // ADD LISTENERS

        createDialogActonListener("TALKING_ENTITY", h.IMG_TALKING_ENTITY);
        createDialogActonListener("DIALOG_1", h.IMG_DIALOG_1);
        createDialogActonListener("DIALOG_2", h.IMG_DIALOG_2);
        createDialogActonListener("DIALOG_3", h.IMG_DIALOG_3);


        for(String s:h.imageActonHash.keySet()){
            createActonListener(s,h.imageActonHash.get(s).getSecond());
        }

        createStageforHud();


    }

    public void createStageforHud(){

        stageforHud=new Stage(new ScalingViewport(Scaling.stretch, h.IMG_HUD.getWidth(),h.IMG_HUD.getHeight()),g.getSpriteBatch());

        smm =g.smm;

        m1= smm.loadMusic("sound/buttonclickLVL1.ogg",false);
        m4= smm.loadMusic("sound/buttonclickLVL1.ogg",false);
        m5= smm.loadMusic("sound/buttonclickLVL1.ogg",false);
        m6= smm.loadMusic("sound/buttonclickLVL1.ogg",false);



        m2 = smm.loadMusic("sound/rasssLVL1.ogg",false);
        m3 = smm.loadMusic("sound/crongLVL1.ogg",false);

        m7 = smm.loadMusic("sound/rotatingintroProfileLVL1.ogg",false);
        m8 = smm.loadMusic("sound/frfrfrfrfrLVL1.mp3",false);


        turtlebutton = new Image(new Texture( Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_bottons/menu_botton_closcaturtle.png")));
        safykids=new Image(new Texture( Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_bottons/menu_botton_closcaturtle_avatar.png")));

        t_Map1     = new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01.png"  )));
        t_Map2     = new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02.png"  )));
        t_Map3     = new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03.png"  )));


        menuButtonTurtleA            = new Image(new Texture( Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_bottons/botons_ingame/ingame_botton_turtle.png"          )));
        menuButtonTurtleB            = new Image(new Texture( Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_bottons/botons_ingame/ingame_botton_turtle_click.png"    )));
        menuButtonTurtleC            = new Image(new Texture( Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_bottons/botons_ingame/ingame_botton_turtle_click.png"    )));

        menuButtonMapA               = new Image(new Texture( Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_bottons/botons_ingame/ingame_botton_map.png"             )));
        menuButtonMapB               = new Image(new Texture( Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_bottons/botons_ingame/ingame_botton_map_click.png"       )));
        menuButtonMapC               = new Image(new Texture( Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_bottons/botons_ingame/ingame_botton_map_click.png"       )));

        menuButtonSoundA             = new Image(new Texture( Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_bottons/botons_ingame/ingame_botton_sound.png"           )));
        menuButtonSoundB             = new Image(new Texture( Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_bottons/botons_ingame/ingame_botton_sound_click.png"      )));
        menuButtonSoundC             = new Image(new Texture( Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_bottons/botons_ingame/ingame_botton_sound_click.png"      )));


        menuButtonCloscaTurtleA      = new Image(new Texture( Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_bottons/botons_ingame/ingame_botton_closcaturtle.png"    )));
        menuButtonCloscaTurtleB      = new Image(new Texture( Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_bottons/botons_ingame/ingame_botton_closcaturtle_click.png"    )));
        menuButtonCloscaTurtleC      = new Image(new Texture( Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_bottons/botons_ingame/ingame_botton_closcaturtle_click.png"    )));


        menuButtonTurtle             = new ImageButton( menuButtonTurtleA.getDrawable(), menuButtonTurtleB.getDrawable(),menuButtonTurtleC.getDrawable());
        menuButtonCloscaTurtle       = new ImageButton( menuButtonCloscaTurtleA.getDrawable(), menuButtonCloscaTurtleB.getDrawable(),menuButtonCloscaTurtleC.getDrawable());
        menuButtonSound              = new ImageButton( menuButtonSoundA.getDrawable(), menuButtonSoundB.getDrawable(),menuButtonSoundC.getDrawable());
        menuButtonMap                = new ImageButton( menuButtonMapA.getDrawable(), menuButtonMapB.getDrawable(),menuButtonMapC.getDrawable());

        door_left= new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_bottons/botons_ingame/ingame_minidoor_left.png")));
        door_right= new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_bottons/botons_ingame/ingame_minidoor_right.png")));

        door_leftFront= new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_doors/menu_door_left.png")));
        door_rightFront= new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_doors/menu_door_right.png")));


        door_left.setPosition(0-1362/2,0);
        door_right.setPosition(1362,0);

        door_leftFront.setPosition(0-1362/2,0);
        door_rightFront.setPosition(1362,0);

        turtlebutton.setPosition(395,64);
        safykids.setPosition(560,289);

        turtlebutton.setOrigin(Align.center);
        safykids.setOrigin(Align.center);


        menuButtonTurtle.setPosition(1161,606);

        stageforHud.addActor(door_left);
        stageforHud.addActor(door_right);
        stageforHud.addActor(menuButtonTurtle);
        stageforHud.addActor(menuButtonMap);
        stageforHud.addActor(menuButtonSound);
        stageforHud.addActor(door_leftFront);
        stageforHud.addActor(door_rightFront);
        stageforHud.addActor(menuButtonCloscaTurtle);
        stageforHud.addActor(turtlebutton);
        stageforHud.addActor(safykids);


        menuButtonSound.setVisible(false);
        menuButtonMap.setVisible(false);
        menuButtonCloscaTurtle.setVisible(false);
        turtlebutton.setVisible(false);
        safykids.setVisible(false);

        menuButtonSound.setPosition(131,243);
        menuButtonCloscaTurtle.setPosition(560,243);
        menuButtonMap.setPosition(954,243);

        door_leftFront.setVisible(false);
        door_rightFront.setVisible(false);

        t_Map1.      setPosition(0,800);
        t_Map2.      setPosition(0,800);
        t_Map3.      setPosition(0,800);


        stageforHud.addActor(t_Map1);
        stageforHud.addActor(t_Map2);
        stageforHud.addActor(t_Map3);

        t_Map1.setVisible(false);
        t_Map2.setVisible(false);
        t_Map3.setVisible(false);


        sa=new SequenceAction();

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

        //Listeners
        menuButtonTurtle.addListener(new InputListener() {

                                        @Override
                                        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                                            menuButtonTurtle.setChecked(false);
                                        }

                                        @Override
                                         public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {


                                             if(!controlButton) {
                                                 if (!checkcontrolButton) {
                                                     checkcontrolButton = true;
                                                     controlButton = true;
                                                     controlButtonInt=1;


                                                     m1.setVolume(0.3f);
                                                     m1.play();
                                                 } else {
                                                     checkcontrolButton = false;

                                                     controlButton = true;
                                                     controlButtonInt=1;
                                                     m1.setVolume(0.3f);
                                                     m1.play();
                                                 }

                                             }
                                             //menuButtonTurtle.setChecked(false);
                                             g.println("menuButtonTurtle:controlButton: "+controlButton+" checkcontrolButton:"+checkcontrolButton + " controlButtonInt: "+ controlButtonInt);

                                             return true;
                                         }
                                     }
        );



        menuButtonMap.addListener(new InputListener() {

                                    @Override
                                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                                        menuButtonMap.setChecked(false);
                                    }


                                      @Override
                                      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {


                                          if(!controlButton) {
                                              if (!checkcontrolButton) {
                                                  checkcontrolButton = true;
                                                  controlButton = true;
                                                  controlButtonInt=3;


                                                  m5.setVolume(0.3f);
                                                  m5.play();
                                              } else {
                                                  checkcontrolButton = false;

                                                  controlButton = true;
                                                  controlButtonInt=3;
                                                  m5.setVolume(0.3f);
                                                  m5.play();
                                              }

                                          }
                                          g.println("menuButtonMap: controlButton: "+controlButton+" checkcontrolButton:"+checkcontrolButton + " controlButtonInt: "+ controlButtonInt);
                                          //menuButtonMap.setChecked(false);
                                          return true;
                                      }
                                  }
        );

        menuButtonCloscaTurtle.addListener(new InputListener() {
                                                @Override
                                                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                                                    menuButtonCloscaTurtle.setChecked(false);
                                                }



                                                @Override
                                               public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {


                                                   if(!controlButton) {
                                                       if (!checkcontrolButton) {
                                                           checkcontrolButton = true;
                                                           controlButton = true;
                                                           controlButtonInt=4;


                                                           m6.setVolume(0.3f);
                                                           m6.play();
                                                       } else {
                                                           checkcontrolButton = false;

                                                           controlButton = true;
                                                           controlButtonInt=4;
                                                           m6.setVolume(0.3f);
                                                           m6.play();
                                                       }

                                                   }
                                                   g.println("menuButtonCloscaTurtle:controlButton: "+controlButton+" checkcontrolButton:"+checkcontrolButton + " controlButtonInt: "+ controlButtonInt);
                                                   //menuButtonCloscaTurtle.setChecked(false);
                                                   return true;
                                               }
                                           }
        );

        addMenuButtonSoundListener();


    }

    public void addMenuButtonSoundListener(){
        menuButtonSound.addListener(new InputListener() {

                                        @Override
                                        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                                            //menuButtonSound.setChecked(false);
                                        }
                                        @Override
                                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {


                                            if(!controlButton) {
                                                if (!checkcontrolButton) {
                                                    checkcontrolButton = true;
                                                    controlButton = true;
                                                    controlButtonInt=2;


                                                    m4.setVolume(0.3f);
                                                    m4.play();
                                                } else {
                                                    checkcontrolButton = false;
                                                    controlButton = true;
                                                    controlButtonInt=2;
                                                    m4.setVolume(0.3f);
                                                    m4.play();
                                                }

                                            }
                                            g.println("menuButtonSound:controlButton: "+controlButton+" checkcontrolButton:"+checkcontrolButton + " controlButtonInt: "+ controlButtonInt);
                                            //menuButtonSound.setChecked(true);
                                            return true;
                                        }
                                    }
        );
    }



    public void setlastHHU_Entity_Config(){



        //ADDING ACTORS ACTONICONIMAGES TO STAGE
        for(String sss:h.imageActonHash.keySet()) {
            s.addActor(h.imageActonHash.get(sss).getSecond());
        }

        // SETTING HUD TEXTACTOR //:
        g.getTextactors().put("HUD",new TextActor(g.m.gsys.getCamera(),g));
        g.getTextActorConfig().Config(g.getTextactors().get("HUD"), g);
        g.getTextactors().get("HUD").setVisible(true);
        g.getTextactors().get("HUD").setHUD(true);
        g.getTextactors().get("HUD").setColor(Color.GOLDENROD);
        g.getTextactors().get("HUD").setTint(null);
        g.getTextactors().get("HUD").setName("HUD");
        g.getTextactors().get("HUD").setWithShape(true);
        g.getTextactors().get("HUD").setScale(0.8f,0.8f);
        g.getTextactors().get("HUD").setPositionDistChanger(0,0);
        g.getTextactors().get("HUD").setTextActorPosition(POS_EMOX,POS_EMOY);
        g.getTextactors().get("HUD").setText("TEXTO DE HUD");
        setEMO(ProfileScren.ac.points);
        EMOzindex=g.getTextactors().get("HUD").getZIndex();

        // SETTING GRAPH TEXTAXTORS (text0 to text99)
        for(int k=0;k<=100;k++){
            g.getTextactors().put(("text"+k), new TextActor(g.m.gsys.getCamera(), g));
            g.getTextactors().get(("text"+k)).setName(("text"+k));
            g.getTextActorConfig().setSelectedFont("dialog");
            g.getTextActorConfig().Config(g.getTextactors().get(("text"+k)), g);
            g.getTextactors().get(("text"+k)).setHUD(true);

        }

        //SEETTING ACTON DIALOG TEXTACTIORS
        g.getTextactors().put("TALKING_ENTITY", new TextActor(g.m.gsys.getCamera(), g));
        g.getTextactors().put("DIALOG_1", new TextActor(g.m.gsys.getCamera(), g));
        g.getTextactors().put("DIALOG_2", new TextActor(g.m.gsys.getCamera(), g));
        g.getTextactors().put("DIALOG_3", new TextActor(g.m.gsys.getCamera(), g));

        g.getTextActorConfig().setSelectedFont("normal");

        g.getTextActorConfig().Config(g.getTextactors().get("TALKING_ENTITY"), g);
        g.getTextActorConfig().Config(g.getTextactors().get("DIALOG_1"), g);
        g.getTextActorConfig().Config(g.getTextactors().get("DIALOG_2"), g);
        g.getTextActorConfig().Config(g.getTextactors().get("DIALOG_3"), g);

        g.getTextActorConfig().setSelectedFont("");


        g.getTextactors().get("TALKING_ENTITY").setName("TALKING_ENTITY");
        g.getTextactors().get("DIALOG_1").setName("DIALOG_1");
        g.getTextactors().get("DIALOG_2").setName("DIALOG_2");
        g.getTextactors().get("DIALOG_3").setName("DIALOG_3");

        g.getTextactors().get("TALKING_ENTITY").setVisible(false);
        g.getTextactors().get("DIALOG_1").setVisible(false);
        g.getTextactors().get("DIALOG_2").setVisible(false);
        g.getTextactors().get("DIALOG_3").setVisible(false);

        g.getTextactors().get("TALKING_ENTITY").setHUD(true);
        g.getTextactors().get("DIALOG_1").setHUD(true);
        g.getTextactors().get("DIALOG_2").setHUD(true);
        g.getTextactors().get("DIALOG_3").setHUD(true);

        g.getTextactors().get("TALKING_ENTITY").setPositionDistChanger(0,0);
        g.getTextactors().get("DIALOG_1").setPositionDistChanger(0,0);
        g.getTextactors().get("DIALOG_2").setPositionDistChanger(0,0);
        g.getTextactors().get("DIALOG_3").setPositionDistChanger(0,0);

        g.getTextactors().get("TALKING_ENTITY").setPositionDistChanger(0,0);
        g.getTextactors().get("DIALOG_1").setPositionDistChanger(0,0);
        g.getTextactors().get("DIALOG_2").setPositionDistChanger(0,0);
        g.getTextactors().get("DIALOG_3").setPositionDistChanger(0,0);

        g.getTextactors().get("TALKING_ENTITY").setHUD(true);
        g.getTextactors().get("DIALOG_1").setHUD(true);
        g.getTextactors().get("DIALOG_2").setHUD(true);
        g.getTextactors().get("DIALOG_3").setHUD(true);


        addTextActorsToStage();

        //s.addActor(h.IMG_HOTBAR);

        //s.addActor(h.inventory.get_hotBar()                  );

        sclientHud=new spritemationsClientForHud(this,g,null,g.m.gsys.spritemationsHost);

        //sclientHud.setSpritemation("tape","tape1",200,200);
        //sclientHud.setSpritemation("stars","stars1",500,200,45);
        //sclientHud.setSpritemation("stars","stars2",900,400,70);
        //sclientHud.unsetSpritemation("tape1");
        //sclientHud.unsetSpritemation("stars1");

        add(h);

        //getHudActorDataComponent().safeT.checkState();




    }

    //UPDATE
    public void execUpdateControlButtonTurtle(float delta){

        controlButtonCounter += delta;

        if (checkcontrolButton) {
            g.println("***********************checkcontrolButton");
            if(controlButtonInt==1) {
                g.println("***********************controlButtonInt==1");
                if (controlButtonCounter > 0.1 && !booleanTurtle1) {
                    g.println("***********************controlButtonCounter > 0.1 && !booleanTurtle1");
                    booleanTurtle1 = true;
                    door_left.clearActions();
                    door_right.clearActions();
                    door_left.addAction(Actions.moveTo(0, 0, 0.5f));
                    door_right.addAction(Actions.moveTo(1362 / 2, 0, 0.5f));
                    m2.setVolume(0.3f);
                    m2.play();

                }

                if (controlButtonCounter > 0.8f && !booleanTurtle2) {
                    g.println("***********************controlButtonCounter > 0.8f && !booleanTurtle2");
                    booleanTurtle2 = true;
                    menuButtonMap.setVisible(true);
                    menuButtonSound.setVisible(true);
                    menuButtonCloscaTurtle.setVisible(true);
                    m3.setVolume(0.3f);
                    m3.play();
                }


                if (controlButtonCounter > 0.8 && !booleanTurtle3) {
                    g.println("***********************controlButtonCounter > 0.8 && !booleanTurtle3");
                    booleanTurtle3 = true;
                    booleanTurtle1 = false;
                    booleanTurtle2 = false;
                    booleanTurtle3 = false;
                    controlButton = false;
                    controlButtonInt = 0;
                    controlButtonCounter = 0;
                    //menuButtonTurtle.setChecked(false);

                }
            }


        } else {
            g.println("***********************NOT: checkcontrolButton (else case)");
            if(controlButtonInt==1 || controlButtonInt==2) {
                g.println("***********************controlButtonInt==1 || controlButtonInt==2");
                if (controlButtonCounter > 0.2 && !booleanTurtle1) {
                    g.println("***********************controlButtonCounter > 0.2 && !booleanTurtle1");

                    booleanTurtle1=true;
                    door_left.clearActions();
                    door_right.clearActions();
                    door_left.addAction(Actions.moveTo(0 - 1362 / 2, 0, 0.5f));
                    door_right.addAction(Actions.moveTo(1362, 0, 0.5f));
                    m2.setVolume(0.3f);
                    m2.play();
                    if(controlButtonInt==2) {
                        g.musicMute = !g.musicMute;
                        g.smm.allMusicVolume();
                    }
                }
                if (controlButtonCounter > 0.1f && !booleanTurtle2) {
                    g.println("***********************controlButtonCounter > 0.1f && !booleanTurtle2");
                    booleanTurtle2 = true;
                    m3.setVolume(0.3f);
                    m3.play();
                    menuButtonMap.setVisible(false);
                    menuButtonSound.setVisible(false);
                    menuButtonCloscaTurtle.setVisible(false);
                }

                if (controlButtonCounter > 0.9 && !booleanTurtle3) {
                    g.println("***********************controlButtonCounter > 0.9 && !booleanTurtle3");
                    booleanTurtle3 = true;
                    booleanTurtle1 = false;
                    booleanTurtle2 = false;
                    booleanTurtle3 = false;
                    controlButton = false;
                    controlButtonCounter = 0;
                    controlButtonInt=0;

                }
            }else if(controlButtonInt==3) {
                g.println("***********************controlButtonInt==3");
                if (controlButtonCounter > 0.05 && !booleanTurtle1) {
                    g.println("***********************controlButtonCounter > 0.05 && !booleanTurtle1");
                    booleanTurtle1=true;

                    door_leftFront.setVisible(true);
                    door_rightFront.setVisible(true);
                    door_leftFront.clearActions();
                    door_rightFront.clearActions();
                    door_leftFront.addAction(Actions.moveTo(0, 0, 0.3f));
                    door_rightFront.addAction(Actions.moveTo(0, 0, 0.3f));
                    m2.setVolume(0.3f);
                    m2.play();
                }

                if( controlButtonCounter>0.4 && !booleanTurtle2) {
                    g.println("***********************controlButtonCounter>0.4 && !booleanTurtle2");
                    booleanTurtle2 = true;

                    menuButtonCloscaTurtle.setVisible(false);
                    turtlebutton.setVisible(true);
                    sa.restart();
                    sa1.restart();
                    turtlebutton.clearActions();
                    safykids.clearActions();
                    turtlebutton.addAction(sa);
                    safykids.addAction(sa1);
                    m7.play();
                    m7.setVolume(0.3f);
                }

                if(controlButtonCounter>1.0 && !booleanTurtle3) {
                    g.println("***********************controlButtonCounter>0.9 && !booleanTurtle3");
                    booleanTurtle3 = true;
                    if(ac.Level<10) {
                        t_Map1.setVisible(true);
                        t_Map1.addAction(Actions.moveTo(0, 0, 0.7f));
                    }
                    if(ac.Level>9 && ac.Level<19) {
                        t_Map2.setVisible(true);
                        t_Map2.addAction(Actions.moveTo(0, 0, 0.7f));
                    }
                    if(ac.Level>18 && ac.Level<28) {
                        t_Map3.setVisible(true);
                        t_Map3.addAction(Actions.moveTo(0, 0, 0.7f));
                    }


                    m8.play();
                    m8.setVolume(0.3f);
                }



                if (controlButtonCounter > 1.8 && !booleanTurtle4) {
                    g.println("***********************(controlButtonCounter > 1.8 && !booleanTurtle4)");

                    turtlebutton.setVisible(false);
                    safykids.setVisible(false);
                    menuButtonSound.setVisible(false);
                    menuButtonMap.setVisible(false);

                    booleanTurtle4 = true;
                    booleanTurtle1 = false;
                    booleanTurtle2 = false;
                    booleanTurtle3 = false;

                    controlButtonCounter = 0;
                    controlButtonInt=0;
                    booleanTurtle4=false;



                    door_leftFront.setPosition(0-1362/2,0);
                    door_rightFront.setPosition(1362,0);
                    menuButtonCloscaTurtle  .setChecked(false);
                    menuButtonSound         .setChecked(false);
                    menuButtonMap           .setChecked(false);
                    menuButtonTurtle        .setChecked(false);
                    t_Map1.      setPosition(0,800);
                    t_Map2.      setPosition(0,800);
                    t_Map3.      setPosition(0,800);


                    door_left.setPosition(0-1362/2,0);
                    door_right.setPosition(1362,0);


                    menuButtonCloscaTurtle  .setChecked(false);
                    menuButtonSound         .setChecked(false);
                    menuButtonMap           .setChecked(false);
                    menuButtonTurtle        .setChecked(false);

                    Safe.getInstance().setScreen(Safe.getInstance().getScreenType(Safe.ScreenType.GeneralMenuScreen));

                    controlButton=false;


                }
            }else  if(controlButtonInt==4) {

                if (controlButtonCounter > 0.05 && !booleanTurtle1) {
                    g.println("***********************controlButtonCounter > 0.05 && !booleanTurtle1");
                    booleanTurtle1=true;

                    door_leftFront.setVisible(true);
                    door_rightFront.setVisible(true);
                    door_leftFront.clearActions();
                    door_rightFront.clearActions();
                    door_leftFront.addAction(Actions.moveTo(0, 0, 0.3f));
                    door_rightFront.addAction(Actions.moveTo(0, 0, 0.3f));
                    m2.setVolume(0.3f);
                    m2.play();
                }

                if( controlButtonCounter>0.4 && !booleanTurtle2) {
                    g.println("***********************controlButtonCounter>0.4 && !booleanTurtle2");
                    booleanTurtle2 = true;

                    menuButtonCloscaTurtle.setVisible(false);
                    turtlebutton.setVisible(true);
                    sa.restart();
                    sa1.restart();
                    turtlebutton.clearActions();
                    safykids.clearActions();
                    turtlebutton.addAction(sa);
                    safykids.addAction(sa1);
                    m7.play();
                    m7.setVolume(0.3f);
                }

                if(controlButtonCounter>1.0 && !booleanTurtle3) {
                    g.println("controlButtonCounter>1.0 && !booleanTurtle3");

                    turtlebutton.setVisible(false);
                    safykids.setVisible(false);
                    menuButtonSound.setVisible(false);
                    menuButtonMap.setVisible(false);

                    booleanTurtle4 = true;
                    booleanTurtle1 = false;
                    booleanTurtle2 = false;

                    controlButtonCounter = 0;
                    controlButtonInt=0;
                    booleanTurtle4=false;

                    door_leftFront.setPosition(0-1362/2,0);
                    door_rightFront.setPosition(1362,0);

                    t_Map1.      setPosition(0,800);
                    t_Map2.      setPosition(0,800);
                    t_Map3.      setPosition(0,800);


                    door_left.setPosition(0-1362/2,0);
                    door_right.setPosition(1362,0);

                    smm.pauseAllMusic();
                    g.println(" smm.pauseAllMusic(); ");
                    booleanTurtle3=true;

                    controlButton=false;
                    booleanTurtle3 = false;
                    Safe.get_mainGameScreen().get_game().setScreen(Safe.get_mainGameScreen().get_game().getScreenType(Safe.ScreenType.CentralGameScreen));
                }


            }
        }

    }

    public Stage getStageforHud() {
        return stageforHud;
    }

    public void addTextActorsToStage(){

        for (String key : g.getTextactors().keySet()) {
            if (!key.contains("HUD") && !key.contains("DIALOG_") && !key.contains("TALKING_ENTITY")) {
                g.println(TAG + " ADDING HUD [" + key + "] TEXTACTOR TO HUD");
                g.getTextactors().get(key).addTextActorToHUD(s);
            }
        }

        s.addActor(h.IMG_TALKING_ENTITY);
        s.addActor(h.IMG_DIALOG_1);
        s.addActor(h.IMG_DIALOG_2);
        s.addActor(h.IMG_DIALOG_3);

        g.getTextactors().get("TALKING_ENTITY").addTextActorToHUD(s);
        g.getTextactors().get("DIALOG_1").addTextActorToHUD(s);
        g.getTextactors().get("DIALOG_2").addTextActorToHUD(s);
        g.getTextactors().get("DIALOG_3").addTextActorToHUD(s);

        //s.addActor(h.IMG_HUD                          );

        g.getTextactors().get("HUD").addTextActorToHUD(s);
    }



    /*_______________________________________________________________________________________________________________*/

    private boolean AlphaHitDetector(int pixel){
        return (pixel & 0x000000ff) == 0;
    }

    public void addToEMO (int quantity){
        EMO += quantity;
    }

    /*_______________________________________________________________________________________________________________*/



    public void createDialogActonListener(String name, Image image) {

        _hudListeners.put(name, new GenericInputListener(name, g) {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                if(!this.getListenerOwner().equalsIgnoreCase("TALKING_ENTITY")){

                    //GET ACTON && TREEID && TREENUMNODE OF THE CURRENT PENDING INSTRUCTION
                    GEActon acton = g.m.lvlMgr.get_acton();
                    String treeID = acton.getPendingOKinstructions().get(0).split("@")[1];
                    int treeNumNode = Integer.valueOf(acton.getPendingOKinstructions().get(0).split("@")[2]);

                    //GET CURRENTNODEGRAPH && CHOICENODE && CHOICER

                    //g.printlns(TAG + "   " + treeID + "    " + treeNumNode);

                    ToActionNode currentNodeGraph = g.m.ggMgr.getCurrentgg().getNodeById(treeID.split("%")[1]);
                    com.mygdx.safe.MainGameGraph.SpecialSuperTree.TreeNode choiceNode = g.m.ggMgr.getCurrentgg().getAllShooterTrees().get(treeID).getNode(treeNumNode);

                    //IF THE PENDING INSTRUCTION HAVE THE WORD "CHOICE", IT MEANS THAT THIS INSTRUCTION IS OWNED BY A CHOICER INSTRUCTION
                    if(g.findInstruction("CHOICE", acton.getPendingOKinstructions())){

                        //g.printlns(TAG + "   " + choiceNode.getName() + "    " + choiceNode.getParentNode().getName() + (currentNodeGraph == null));
                        com.mygdx.safe.MainGameGraph.Choicer choicer = currentNodeGraph.getConditions()._choicers.get(choiceNode.getParentNode().getName());

                        choicer.markChoiceToExecute(Integer.valueOf(this.getListenerOwner().split("_")[1]) - 1);
                    }

                    //SET ALL TO NOT VISIBLE
                    g.getTextactors().get("TALKING_ENTITY").setVisible(false);
                    g.getTextactors().get("DIALOG_1").setVisible(false);
                    g.getTextactors().get("DIALOG_2").setVisible(false);
                    g.getTextactors().get("DIALOG_3").setVisible(false);

                    h.IMG_TALKING_ENTITY.setVisible(false);
                    h.IMG_DIALOG_1.setVisible(false);
                    h.IMG_DIALOG_2.setVisible(false);
                    h.IMG_DIALOG_3.setVisible(false);

                    g.sendIntructionOK("", acton.getPendingOKinstructions(), null);
                }


            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
            }
        });

        image.addListener(_hudListeners.get(name));
    }

    public void createActonListener(String name, Image image) {

        _hudListeners.put(name, new GenericInputListener(name, g) {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

                //GET ACTON && TREEID && TREENUMNODE OF THE CURRENT PENDING INSTRUCTION
                GEActon acton = g.m.lvlMgr.get_acton();
                String treeID = acton.getPendingOKinstructions().get(0).split("@")[1];
                int treeNumNode = Integer.valueOf(acton.getPendingOKinstructions().get(0).split("@")[2]);

                //GET CURRENTNODEGRAPH && CHOICENODE && CHOICER
                g.println(TAG + "   " + treeID + "    " + treeNumNode);
                ToActionNode currentNodeGraph = g.m.ggMgr.getCurrentgg().getNodeById(treeID.split("%")[1]);
                SpecialSuperTree.TreeNode choiceNode = g.m.ggMgr.getCurrentgg().getAllShooterTrees().get(treeID).getNode(treeNumNode);

                //IF THE PENDING INSTRUCTION HAVE THE WORD "CHOICE", IT MEANS THAT THIS INSTRUCTION IS OWNED BY A CHOICER INSTRUCTION
                g.println(TAG + "   " + choiceNode.getName() + "    " + choiceNode.getParentNode().getName() + (currentNodeGraph == null) +"this.getListenerOwner()="+this.getListenerOwner());
                Choicer choicer = currentNodeGraph.getConditions()._choicers.get(choiceNode.getParentNode().getName());

                choicer.markChoiceToExecute(h.imageActonHash.get(this.getListenerOwner()).getFirst()); // <--NEW imageACTONHASH


                //SET ALL TO NOT VISIBLE
                for(String sss:h.imageActonHash.keySet()){
                    h.imageActonHash.get(sss).getSecond().setVisible(false);
                }

                g.sendIntructionOK("", acton.getPendingOKinstructions(), null);

            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
            }
        });

        image.addListener(_hudListeners.get(name));
    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS

    public Stage getStage() {
        return s;
    }

    public HudDataComponent getHudActorDataComponent() {return h; }

    public GenericMethodsInputProcessor getGenericMethodsInputProcessor() {  return g;  }

    public HashMap<String, GenericInputListener> get_hudListeners(){
        return _hudListeners;
    }

    public HashMap<String, GenericGestureListener> get_hudGestureListeners() {
        return _hudGestureListeners;
    }

    public Stage getS() {
        return s;
    }

    public HudDataComponent getH() {
        return h;
    }

    public int getEMO() {
        return EMO;
    }

    public spritemationsClientForHud getSclientHud() {
        return sclientHud;
    }

    //SETTERS

    public void set_hudGestureListeners(HashMap<String, GenericGestureListener> _hudGestureListeners) {
        this._hudGestureListeners = _hudGestureListeners;
    }

    public void set_hudListeners(HashMap<String, GenericInputListener> _hudListeners) {
        this._hudListeners = _hudListeners;
    }

    public void setEMO(int EMO) {

        this.EMO = EMO;
        int emoaux=EMO;
        int i=0;
        while(emoaux>9){
            emoaux=(int) ((float)emoaux/10.0);
            i++;
        }
        g.getTextactors().get("HUD").setTextActorPosition(POS_EMOX-i*45,POS_EMOY);
    }

    public void receive(String[] message, String treeID, int treeNumNode){

        String str = "";

        for(int i=0; i<message.length; i++){
            str += message[i] +"#";
        }
        str = str.substring(0, str.length()-1);

        g.println(TAG + "RECEIVING:   " + str.substring(0, str.length()-1));


        // SET_TEXT_ACTOR#_name#FONT#_font%
        // SET_TEXT_ACTOR#_name_#SET_TEXT#_text_%
        // SET_TEXT_ACTOR#_name_# SET_POSITION #_positionX#positionY%
        // SET_TEXT_ACTOR#_name# SET_SCALE #scaleX#scaleY%
        // SET_TEXT_ACTOR#_name_ SET_VISIBLE#_visible_%
        // SET_TEXT_ACTOR#_name_ SET_COLOR#_color_%
        // SET_TEXT_ACTOR#_name_#ADD_TO_STAGE%
        if(message[0].equalsIgnoreCase("SET_TEXTACTOR")){
            if(message.length==4 && message[2].equalsIgnoreCase("FONT")) {
                if(g.getTextactors().get(message[1])!=null) {
                    g.getTextActorConfig().setSelectedFont(message[3]);
                    g.getTextActorConfig().Config(g.getTextactors().get(message[1]), g);
                    g.getTextactors().get(message[1]).setName(message[1]);
                    g.getTextactors().get(message[1]).setHUD(true);
                    Image im=new Image();
                    g.m.he.getS().addActor(im);
                    g.getTextactors().get(message[1]).setZIndex(im.getZIndex());

                }

                g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

            }
            else if(message.length>2 && g.getTextactors().get(message[1])!=null){
                if(message.length==4 && message[2].equalsIgnoreCase("SET_TEXT")){
                    g.getTextactors().get(message[1]).setText(message[3]);
                    g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
                }
                else if(message.length==6 && message[2].equalsIgnoreCase("SET_TEXT")){
                    g.getTextactors().get(message[1]).setAdjustingText(message[3],Float.valueOf(message[4]),Float.valueOf(message[5]));
                    g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
                }
                else if(message.length==5 && message[2].equalsIgnoreCase("SET_POSITION")){
                    g.getTextactors().get(message[1]).setTextActorPosition(Float.valueOf(message[3]),Float.valueOf(message[4]));
                    g.getTextactors().get(message[1]).setPositionDistChanger(0,0);
                    g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
                }

                else if(message.length==5 && message[2].equalsIgnoreCase("SET_SCALE")){
                    g.getTextactors().get(message[1]).setScale(Float.valueOf(message[3]),Float.valueOf(message[4]));
                    g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
                }
                else if(message.length==4 && message[2].equalsIgnoreCase("SET_VISIBLE")){
                    g.getTextactors().get(message[1]).setVisible(Boolean.valueOf(message[3]));
                    g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
                }
                else if(message.length==4 && message[2].equalsIgnoreCase("SET_COLOR")){
                    String colorS=message[3];
                    Color color=new Color();
                    if(colorS.equalsIgnoreCase("BLACK")) color=Color.BLACK;
                    if(colorS.equalsIgnoreCase("GREEN")) color=Color.GREEN;
                    if(colorS.equalsIgnoreCase("BLUE")) color=Color.BLUE;
                    if(colorS.equalsIgnoreCase("BROWN")) color=Color.BROWN;
                    if(colorS.equalsIgnoreCase("CHARTREUSE")) color=Color.CHARTREUSE;
                    if(colorS.equalsIgnoreCase("CLEAR")) color=Color.CLEAR;
                    if(colorS.equalsIgnoreCase("CORAL")) color=Color.CORAL;
                    if(colorS.equalsIgnoreCase("CYAN")) color=Color.CYAN;
                    if(colorS.equalsIgnoreCase("DARK_GRAY")) color=Color.DARK_GRAY;
                    if(colorS.equalsIgnoreCase("FIREBRICK")) color=Color.FIREBRICK;
                    if(colorS.equalsIgnoreCase("FOREST")) color=Color.FOREST;
                    if(colorS.equalsIgnoreCase("GOLD")) color=Color.GOLD;
                    if(colorS.equalsIgnoreCase("GOLDENROD")) color=Color.GOLDENROD;
                    if(colorS.equalsIgnoreCase("GRAY")) color=Color.GRAY;
                    if(colorS.equalsIgnoreCase("LIGHT_GRAY")) color=Color.LIGHT_GRAY;
                    if(colorS.equalsIgnoreCase("LIME")) color=Color.LIME;
                    if(colorS.equalsIgnoreCase("MAGENTA")) color=Color.MAGENTA;
                    if(colorS.equalsIgnoreCase("MAROON")) color=Color.MAROON;
                    if(colorS.equalsIgnoreCase("NAVY")) color=Color.NAVY;
                    if(colorS.equalsIgnoreCase("OLIVE")) color=Color.OLIVE;
                    if(colorS.equalsIgnoreCase("ORANGE")) color=Color.ORANGE;
                    if(colorS.equalsIgnoreCase("PINK")) color=Color.PINK;
                    if(colorS.equalsIgnoreCase("PURPLE")) color=Color.PURPLE;
                    if(colorS.equalsIgnoreCase("RED")) color=Color.RED;
                    if(colorS.equalsIgnoreCase("ROYAL")) color=Color.ROYAL;
                    if(colorS.equalsIgnoreCase("SALMON")) color=Color.SALMON;
                    if(colorS.equalsIgnoreCase("SCARLET")) color=Color.SCARLET;
                    if(colorS.equalsIgnoreCase("SKY")) color=Color.SKY;
                    if(colorS.equalsIgnoreCase("SLATE")) color=Color.SLATE;
                    if(colorS.equalsIgnoreCase("TAN")) color=Color.TAN;
                    if(colorS.equalsIgnoreCase("TEAL")) color=Color.TEAL;
                    if(colorS.equalsIgnoreCase("VIOLET")) color=Color.VIOLET;
                    if(colorS.equalsIgnoreCase("WHITE")) color=Color.WHITE;
                    if(colorS.equalsIgnoreCase("YELLOW")) color=Color.YELLOW;
                    if(colorS.contains("HEX")){ // HEX_hexValue_
                        String[] hex=colorS.split("_");
                        if(hex.length==2 && hex[0].equalsIgnoreCase("HEX"))
                               color=Color.valueOf(hex[1]);
                    }
                    if(colorS.contains("RGBA")){ // RGBA_red_green_blue_alpha , ex: RGBA_0.9_0.5_0.4_1
                        String[] rgba=colorS.split("_");
                        if(rgba.length==5 && rgba[0].equalsIgnoreCase("RGBA"))
                            color.r=Float.valueOf(rgba[1]);
                            color.g=Float.valueOf(rgba[2]);
                            color.b=Float.valueOf(rgba[3]);
                            color.a=Float.valueOf(rgba[4]);
                    }

                    g.getTextactors().get(message[1]).setTint(color);
                    g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
                }

            }else{
                System.out.println(TAG + " ERROR:" + message[1] + " " + g.getTextactors().get(message[1]) + " TEXTACTOR ");
                exit(1);
            }
        }

        else if(message[0].equalsIgnoreCase("ADD_PARTICLEMATION")){

        pmc.createParticle(message[1], "particle/"+ message[2], Float.valueOf(message[3]), Float.valueOf(message[4]), Float.valueOf(message[5]));
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

        }

        else if(message[0].equalsIgnoreCase("SET_PARTICLEMATION_VISIBILITY")){

            pmc.setVisible(message[1],Boolean.valueOf(message[2]));

            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

        }

        else if(message[0].equalsIgnoreCase("DELETE_PARTICLEMATION")){

            ParticleMation pe=pmc.getParticle(message[1]);
            pe.addAction(Actions.removeActor());
            pmc.getPmHash().remove(message[1]);

            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

        }

        else if(message[0].equalsIgnoreCase("ADD_PARTICLEMATION_ACTION")){

            if(message[2].equalsIgnoreCase("MOVE_TO")) {
                pmc.moveToReceive(message[1], Float.valueOf(message[3]), Float.valueOf(message[4]), Float.valueOf(message[5]));
                g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
            }

        }

        else if(message[0].equalsIgnoreCase("ADD_TEXT_ACTOR_ACTION") && message.length>3 && g.getTextactors().get(message[1])!=null){
            TextActor t=g.getTextactors().get(message[1]);



            if(message[2].equalsIgnoreCase("MOVE_TO_POSITION") && message.length==6){
                    t.moveTo(Float.valueOf(message[3]),Float.valueOf(message[4]),Float.valueOf(message[5]));
                    g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
            }




        }

         /*

        g.getTextactors().put("HUD",new TextActor(g.m.gsys.getCamera(),g));
        g.getTextActorConfig().Config(g.getTextactors().get("HUD"), g);
        g.getTextactors().get("HUD").setVisible(true);
        g.getTextactors().get("HUD").setHUD(true);
        g.getTextactors().get("HUD").setName("HUD");
        g.getTextactors().get("HUD").setWithShape(true);
        g.getTextactors().get("HUD").setScale(0.8f,0.8f);
        g.getTextactors().get("HUD").setPositionDistChanger(0,0);
        g.getTextactors().get("HUD").setTextActorPosition(930,60);
        g.getTextactors().get("HUD").setText("TEXTO DE HUD");
          */

         //SET /UNSET SPRITEMATION_HUD

        else if(message[0].equalsIgnoreCase("SET_SPRITEMATION")){

            if(message.length==7 ) {
                //SINTAX: SET_SPRITEMATION # name # withname # masterDistanceX # masterDistanceY # rotation # (fakeFrame %) (FROM 0% a 100%) (-1 IF ANIMATIONLOOPS NORMALLY)
                sclientHud.setSpritemation(message[1], message[2], Float.valueOf(message[3]), Float.valueOf(message[4]), Float.valueOf(message[5]), Integer.valueOf(message[6]));
                g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
            }else if(message.length==9){
                //SINTAX: SET_SPRITEMATION_HUD # name # withname # masterDistanceX # masterDistanceY # rotation # (fakeFrame %) (FROM 0% a 100%) (-1 IF ANIMATIONLOOPS NORMALLY)
                sclientHud.setSpritemation(message[1], message[2], Float.valueOf(message[3]), Float.valueOf(message[4]), Float.valueOf(message[5]), Integer.valueOf(message[6]),
                        Float.valueOf(message[7]), Float.valueOf(message[8]));
                g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
            }


        }
        else if(message[0].equalsIgnoreCase("CLEAR_ALL_SPRITEMATIONS")){

            sclientHud.reset();
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

        }
        else if(message[0].equalsIgnoreCase("SWAP_RENDERIZE_POSITION") && message.length == 3){

            Actor firstActor = null;
            Actor secondActor = null;

            for (int i=0; i<s.getActors().size && (firstActor == null || secondActor == null) ; i++){

                if(s.getActors().get(i).getName() != null){
                    g.println(TAG +"     " + s.getActors().get(i).getName() +" " + i + "   " + s.getActors().size);

                    if(s.getActors().get(i).getName().equalsIgnoreCase(message[1])) firstActor = s.getActors().get(i);

                    if(s.getActors().get(i).getName().equalsIgnoreCase(message[2])) secondActor = s.getActors().get(i);
                }
            }

            g.println(TAG + "     " + firstActor.getName() + "   " + secondActor.getName());

            if(!(firstActor == null || secondActor == null)) {

                s.getRoot().swapActor(firstActor, secondActor);
            }
            else g.println(TAG + "   SWAP_RENDERIZE_POSITION INSTRUCTION ERROR!!");

            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

        }

        else if(message[0].equalsIgnoreCase("SET_SPRITEMATION_ANIMATION_PLAYMODE")){
            sPritemation spritemation = g.m.gsys.spritemationsHost.getSpritemations().get(sclientHud.getSecarrayHash().get(message[1])._name);

            if(message[2].equalsIgnoreCase("LOOP"))
                spritemation.spritemation.setPlayMode(Animation.PlayMode.LOOP);
            else if(message[2].equalsIgnoreCase("NORMAL"))
                spritemation.spritemation.setPlayMode(Animation.PlayMode.NORMAL);
            else if (message[2].equalsIgnoreCase("PINGPONG"))
                spritemation.spritemation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
            else if(message[2].equalsIgnoreCase("RANDOM"))
                spritemation.spritemation.setPlayMode(Animation.PlayMode.LOOP_RANDOM);
            else if(message[2].equalsIgnoreCase("REVERSED"))
                spritemation.spritemation.setPlayMode(Animation.PlayMode.REVERSED);
            else if(message[2].equalsIgnoreCase("LOOP_REVERSED"))
                spritemation.spritemation.setPlayMode(Animation.PlayMode.LOOP_REVERSED);


        }
        else if(message[0].equalsIgnoreCase("SET_SPRITEMATION_COUNTSTATETIME") && message.length>2){
            sclientHud.getSecarrayHash().get(message[1])._countstatetime=Float.valueOf(message[2]);
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

        }
        else if(message[0].equalsIgnoreCase("SET_SPRITEMATION_VISIBILITY") && message.length==3 ){
            //SINTAX: UNSET_SPRITEMATION #  withname
            if(message[2].equalsIgnoreCase("TRUE") || message[2].equalsIgnoreCase("FALSE")){
                sclientHud.setSpritemationVisibility(message[1], Boolean.valueOf(message[2]));
                g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
            }
        }

        else if(message[0].equalsIgnoreCase("SET_SPRITEMATION_POSITION") && message.length==4 ){
            //SINTAX: SET_SPRITEMATION_POSITION #  withname # positionx # positiony

            Image image = sclientHud.getSpritemationListenersHash().get(message[1]).getImage();
            float x;
            float y;

            //Get x value
            if(message[2].equalsIgnoreCase("CURRENT")) x = image.getX();
            else x = Float.valueOf(message[2]);

            //Get y value
            if(message[3].equalsIgnoreCase("CURRENT")) y = image.getY();
            else y = Float.valueOf(message[3]);

            sclientHud.setSpritemationPosition(message[1],x,y);
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
        }
        else if(message[0].equalsIgnoreCase("SET_SPRITEMATION_CENTER_POSITION") && message.length==4 ){
            //SINTAX: SET_SPRITEMATION_POSITION #  withname # positionx # positiony

            Image image = sclientHud.getSpritemationListenersHash().get(message[1]).getImage();
            float x;
            float y;

            //Get x value
            if(message[2].equalsIgnoreCase("CURRENT")) x = image.getX() + (image.getWidth()/2);
            else x = Float.valueOf(message[2]);

            //Get y value
            if(message[3].equalsIgnoreCase("CURRENT")) y = image.getY() + (image.getHeight()/2);
            else y = Float.valueOf(message[3]);

            sclientHud.setSpritemationCenterPosition(message[1],x,y);
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
        }

        else if(message[0].equalsIgnoreCase("SET_SPRITEMATION_ORIGIN")){
            SpritemationListener l = sclientHud.getSpritemationListenersHash().get(message[1]);
            l.setOrigin(message[2]);

            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
        }
        else if(message[0].equalsIgnoreCase("ADD_SPRITEMATION_ACTION")){

            boolean isParallel = (message[message.length-1].equalsIgnoreCase("PARALLEL"));


            Image image = sclientHud.getSpritemationListenersHash().get(message[1]).getImage();
            SpritemationEntityConfig sc = sclientHud.getSecarrayHash().get(message[1]);
            ParallelAction p = new ParallelAction();

            if(message[2].equalsIgnoreCase("FADE_IN")){
                if(isParallel)sc.getParallelAction().addAction(Actions.fadeIn(Float.valueOf(message[3])));
                else image.addAction(Actions.fadeIn(Float.valueOf(message[3])));
            }
            else if(message[2].equalsIgnoreCase("FADE_OUT")){
                if(isParallel) sc.getParallelAction().addAction(Actions.fadeOut(Float.valueOf(message[3])));
                else image.addAction(Actions.fadeOut(Float.valueOf(message[3])));
            }
            else if(message[2].equalsIgnoreCase("SET_ALPHA")){
                if(isParallel) sc.getParallelAction().addAction(Actions.alpha(Float.valueOf(message[3])));
                else image.addAction(Actions.alpha(Float.valueOf(message[3])));
            }
            else if(message[2].equalsIgnoreCase("ADD_ROTATION")){
                if(isParallel) sc.getParallelAction().addAction(Actions.rotateBy(Float.valueOf(message[3]),Float.valueOf(message[4])));
                else image.addAction(Actions.rotateBy(Float.valueOf(message[3]),Float.valueOf(message[4])));
            }
            else if(message[2].equalsIgnoreCase("SCALE")){

                if(message.length == 5){
                    if(isParallel) sc.getParallelAction().addAction(Actions.scaleTo(Float.valueOf(message[3]),Float.valueOf(message[4])));
                    else image.addAction(Actions.scaleTo(Float.valueOf(message[3]),Float.valueOf(message[4])));
                }
                else{
                    if(isParallel) sc.getParallelAction().addAction(Actions.scaleTo(Float.valueOf(message[3]),Float.valueOf(message[4]),Float.valueOf(message[5])));
                    else image.addAction(Actions.scaleTo(Float.valueOf(message[3]),Float.valueOf(message[4]),Float.valueOf(message[5])));
                }

            }
            else if(message[2].equalsIgnoreCase("MOVE_TO_POSITION")){

                MoveToAction m = new MoveToAction();
                float x=0;
                float y=0;

                if(message[3].equalsIgnoreCase("CURRENT")) {
                    x = image.getX();
                }else if(message[3].contains("CURRENT") && (message[3].contains("+") || message[3].contains("-"))){
                    String[] splitvalue;
                    if(message[3].contains("+")) {
                        splitvalue=message[3].split(Pattern.quote("+"));
                        x = image.getX();
                        x+=Float.valueOf(splitvalue[1]);
                    }
                    if(message[3].contains("-")) {
                        splitvalue=message[3].split(Pattern.quote("-"));
                        x = image.getX();
                        x-=Float.valueOf(splitvalue[1]);
                    }

                }
                else x = Float.valueOf(message[3]);

                if(message[4].equalsIgnoreCase("CURRENT")) {
                    y = image.getY();
                }else if(message[4].contains("CURRENT") && (message[4].contains("+") || message[4].contains("-"))){
                    String[] splitvalue;
                    if(message[4].contains("+")) {
                        splitvalue=message[4].split(Pattern.quote("+"));
                        y = image.getY();
                        y+=Float.valueOf(splitvalue[1]);
                    }
                    if(message[4].contains("-")) {
                        splitvalue=message[4].split(Pattern.quote("-"));
                        y = image.getY();
                        y-=Float.valueOf(splitvalue[1]);
                    }

                }
                else y = Float.valueOf(message[4]);

                m.setPosition(x - (image.getWidth()/2), y - (image.getHeight()/2));
                m.setDuration(Float.valueOf(message[5]));
                m.setTarget(image);

                if(isParallel)sc.getParallelAction().addAction(m);
                else image.addAction(m);

            }
            else if(message[2].equalsIgnoreCase("MOVE_TO_ORIGIN_XY_POSITION")) {

                MoveToAction m = new MoveToAction();
                float x=0;
                float y=0;

                if(message[3].equalsIgnoreCase("CURRENT")) {
                    x = image.getX();
                }else if(message[3].contains("CURRENT") && (message[3].contains("+") || message[3].contains("-"))){
                    String[] splitvalue;
                    if(message[3].contains("+")) {
                        splitvalue=message[3].split(Pattern.quote("+"));
                        x = image.getX();
                        x+=Float.valueOf(splitvalue[1]);
                    }
                    if(message[3].contains("-")) {
                        splitvalue=message[3].split(Pattern.quote("-"));
                        x = image.getX();
                        x-=Float.valueOf(splitvalue[1]);
                    }

                }
                else x = Float.valueOf(message[3]);

                if(message[4].equalsIgnoreCase("CURRENT")) {
                    y = image.getY();
                }else if(message[4].contains("CURRENT") && (message[4].contains("+") || message[4].contains("-"))){
                    String[] splitvalue;
                    if(message[4].contains("+")) {
                        splitvalue=message[4].split(Pattern.quote("+"));
                        y = image.getY();
                        y+=Float.valueOf(splitvalue[1]);
                    }
                    if(message[4].contains("-")) {
                        splitvalue=message[4].split(Pattern.quote("-"));
                        y = image.getY();
                        y-=Float.valueOf(splitvalue[1]);
                    }

                }
                else y = Float.valueOf(message[4]);

                m.setPosition(x,y);
                m.setDuration(Float.valueOf(message[5]));
                m.setTarget(image);

                if (isParallel) sc.getParallelAction().addAction(m);
                else image.addAction(m);
            }

            else if(message[2].equalsIgnoreCase("PARABOLIC_MOVEMENT")) {
                SpritemationEntityConfig spEntityConfig = getSclientHud().getSecarrayHash().get(message[1]);

                spEntityConfig.parabolicMovement(Float.valueOf(message[7]), Float.valueOf(message[3]), Float.valueOf(message[4]),
                        Float.valueOf(message[5]), Float.valueOf(message[6]));
            }


            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

        }
        else if(message[0].equalsIgnoreCase("SET_SPRITEMATION_ANIMATION_SPEED")){

            sPritemation spritemation = g.m.gsys.spritemationsHost.getSpritemations().get(sclientHud.getSecarrayHash().get(message[1])._name);

            //spritemation.getSpritemation().setFrameDuration(spritemation.originalFrameDuration / Float.valueOf(message[2]));
            sclientHud.getSecarrayHash().get(message[1])._duration=(spritemation.originalFrameDuration / Float.valueOf(message[2]));

            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

        }
        else if(message[0].equalsIgnoreCase("SET_SPRITEMATION_DURATION")){

            //sPritemation spritemation = g.m.gsys.spritemationsHost.getSpritemations().get(sclientHud.getSecarrayHash().get(message[1])._name);
            sclientHud.getSecarrayHash().get(message[1])._duration=(Float.valueOf(message[2]));


            //spritemation.getSpritemation().setFrameDuration(Float.valueOf(message[2]));
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

        }

        else if(message[0].equalsIgnoreCase("ADD_SPRITEMATION_PARALLEL_ACTION")){

            SpritemationListener l = sclientHud.getSpritemationListenersHash().get(message[1]);
            SpritemationEntityConfig sc = sclientHud.getSecarrayHash().get(message[1]);


            l.getImage().addAction(sc.getParallelAction());
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

        }
        else if(message[0].equalsIgnoreCase("CLEAR_SPRITEMATION_PARALLEL_ACTION")){

            SpritemationEntityConfig sc = sclientHud.getSecarrayHash().get(message[1]);

            sc.getParallelAction().reset();
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

        }

        else if(message[0].equalsIgnoreCase("ACTIVATE_SPRITEMATION_DRAG_POSITION") && message.length==3 ){
            //SINTAX: ACTIVATE_SPRITEMATION_DRAG_POSITION #  withname # true o false
            sclientHud.activateDragImagePosition(message[1],Boolean.valueOf(message[2]));
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
        }

        /*else if(message[0].equalsIgnoreCase("ACTIVATE_SPRITEMATION_BIGSCALER_TOUCH") && message.length==3 ){
            //SINTAX: ACTIVATE_SPRITEMATION_DRAG_POSITION #  withname # true o false
            sclientHud.activatebigScalerTouch(message[1],Boolean.valueOf(message[2]));
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
        }*/

        else if((message[0].equalsIgnoreCase("SCALE_SPRITEMATION")) && message.length>1 ){
            //SINTAX: SCALE SPRITEMATION WITHNAME  # scalex # scaley
            if(message.length == 4){
                sclientHud.scaleSpritemation(message[1],Float.valueOf(message[2]),Float.valueOf(message[3]));
                g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
            }
            else if(message.length == 3){
                sclientHud.scaleSpritemation(message[1],message[2]);
                g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
            }
        }
        else if(message[0].equalsIgnoreCase("ORIGINAL_SCALE_SPRITEMATION")){
            if(message.length == 4){

                sclientHud.getSpritemationListenersHash().get(message[1]).setOriginalScale(-1, -1);

                sclientHud.scaleSpritemation(message[1],Float.valueOf(message[2]),Float.valueOf(message[3]));
                sclientHud.getSpritemationListenersHash().get(message[1]).setOriginalSize(sclientHud.getSpritemationHudImagesHash().get(message[1]));

                g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
            }
        }

        else if(message[0].equalsIgnoreCase("SET_SPRITEMATION_SELECTED")){
            if(message[2].equalsIgnoreCase("TRUE"))sclientHud.getSpritemationListenersHash().get(message[1]).setSelected(true);
            else sclientHud.getSpritemationListenersHash().get(message[1]).setSelected(false);

            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
        }
        else if(message[0].equalsIgnoreCase("SPRITEMATION_LISTENERS")){
            if(message[1].equalsIgnoreCase("ENABLE"))sclientHud.setEnableListeners(true);
            else if(message[1].equalsIgnoreCase("DISABLE"))sclientHud.setEnableListeners(false);

            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
        }
        else if(message[0].equalsIgnoreCase("DETECT_ALPHA_AS_IMAGE")){

            g.println(sclientHud.getSpritemationListenersHash().keySet().toString());

            sclientHud.getSpritemationListenersHash().get(message[1]).setListenAlpha(Boolean.valueOf(message[2]));
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
        }
         // SET_SPRITEMATION_VALUE # spritemation_withname # value
        else if(message[0].equalsIgnoreCase("SET_SPRITEMATION_VALUE") && message.length==3){
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
            sclientHud.getSecarrayHash().get(message[1])._value=Integer.valueOf(message[2]);
            g.println(TAG +" SETTING VALUE AT ["+sclientHud.getSecarrayHash().get(message[1])._withname +"] SPRITEMATION : "+ sclientHud.getSecarrayHash().get(message[1])._value);
            g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

        }
        else if(message[0].equalsIgnoreCase("SELECTED_SPRITEMATIONS")){

            g.printlns(TAG + " ++++++++++++++++++++++++++++++++++++++++++++++++++  " + message[1]);


            if(message[1].equalsIgnoreCase("UNSELECT")){

                float arrayInitialSize = 0 + sclientHud.getSelectedSpritemationListeners().size;

                for(int i=0; i<arrayInitialSize; i++){
                    g.printlns(TAG + "   " + sclientHud.getSelectedSpritemationListeners().size);
                    sclientHud.getSelectedSpritemationListeners().get(0).setSelected(false);
                }
                g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
            }
            else if(message[1].equalsIgnoreCase("REMOVE_PROBES")){

                for(SpritemationListener l: sclientHud.getSelectedSpritemationListeners()){
                    g.m.he.setEMO(g.m.he.getEMO()+sclientHud.getSecarrayHash().get(l.getListenerOwner())._value);
                    g.println(TAG +" ADDING POINTS FROM ["+ l.getListenerOwner() +"] "+ g.m.he.getEMO()+sclientHud.getSecarrayHash().get(l.getListenerOwner())._value);
                    if(ProfileScren.ac.probes.get(l.getListenerOwner()) != null) ProfileScren.ac.probes.remove(l.getListenerOwner());
                }

                g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

            }
            else if(message[1].equalsIgnoreCase("SET_SCALE") && message.length >2){
                for(SpritemationListener l: sclientHud.getSelectedSpritemationListeners()){
                    if(message.length == 3) sclientHud.scaleSpritemation(l.getListenerOwner(), message[2]);
                    else sclientHud.scaleSpritemation(l.getListenerOwner(), Float.valueOf(message[2]), Float.valueOf(message[3]));
                }
                g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
            } // SET_POSITION # xx ## yy
            // SET_POSITION # LISTENER # withname spritemation ::: (listener position)
            else if(message[1].equalsIgnoreCase("SET_POSITION")){
                SpritemationListener li=null;
                if(message.length==4){
                    if(message[2].equalsIgnoreCase("LISTENER")) {
                        for(SpritemationListener l:sclientHud.getSpritemationListeners()){
                            if(l.getListenerOwner().equalsIgnoreCase(message[3])){ li=l; }
                        }
                        if(li!=null) {
                            for (SpritemationListener l : sclientHud.getSelectedSpritemationListeners()) {
                                sclientHud.setSpritemationFixedPosition(l.getListenerOwner(), li.getImage().getX()+li.getActualx(),li.getImage().getY()+li.getActualy());
                            }
                        }
                    }
                    g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
                }
                else if(message.length >3){
                    for(SpritemationListener l: sclientHud.getSelectedSpritemationListeners()) {
                        sclientHud.setSpritemationFixedPosition(l.getListenerOwner(), Float.valueOf(message[2]), Float.valueOf(message[3]));
                    }
                    g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
                }
            } // MOVE_TO_INITIAL_POSITION
            else if(message[1].equalsIgnoreCase("MOVE_TO_INITIAL_POSITION") && message.length==2){
                for(SpritemationListener l: sclientHud.getSelectedSpritemationListeners()) {
                    l.moveToInitPosition();
                }
                g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
            }
            else if(message[1].equalsIgnoreCase("SET_VISIBILITY") && message.length==3){
                Array<String> withname=new Array<String>();

                for(SpritemationListener l: sclientHud.getSelectedSpritemationListeners()){
                    withname.add(l.getListenerOwner());
                }

                float arrayInitialSize = 0 + sclientHud.getSelectedSpritemationListeners().size;

                for(int i=0; i<arrayInitialSize; i++){
                    g.printlns(TAG + "   " + sclientHud.getSelectedSpritemationListeners().size);
                    sclientHud.getSelectedSpritemationListeners().get(0).setSelected(false);
                }

                for(String w:withname){
                    sclientHud.setSpritemationVisibility(w, Boolean.valueOf(message[2]));
                }


                g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);

            }else if(message[1].equalsIgnoreCase("MOVE_TO_POSITION")){
                if(message.length==3){
                    for (SpritemationListener l : sclientHud.getSelectedSpritemationListeners()) {
                        l.moveToPosition(Float.valueOf(message[2]), Float.valueOf(message[3]), Float.valueOf(message[4]));
                    }
                }
                else if(message.length>4){
                    if(message[2].equalsIgnoreCase("LISTENER")){
                        SpritemationListener li=null;
                        for(SpritemationListener l:sclientHud.getSpritemationListeners()){
                            if(l.getListenerOwner().equalsIgnoreCase(message[3])){ li=l; }
                        }
                        if(li!=null) {
                            for (SpritemationListener l : sclientHud.getSelectedSpritemationListeners()) {
                                if(message.length == 5)l.moveToPosition(li.getImage().getX()+li.getActualx(),li.getImage().getY()+li.getActualy(), Float.valueOf(message[4]));
                                else if(message.length == 7){
                                    l.moveToPosition(li.getImage().getX() + Float.valueOf(message[4]), li.getImage().getY() + Float.valueOf(message[5]), Float.valueOf(message[6]));
                                }
                                else if(message[4].equalsIgnoreCase("PERCENTAGE") && message.length == 8) {
                                    l.moveToPosition(li.getImage().getX() + (li.getImage().getWidth() * Float.valueOf(message[5])),
                                            li.getImage().getY() + (li.getImage().getHeight() * Float.valueOf(message[6])), Float.valueOf(message[7]));
                                }

                            }
                        }
                    }
                }
                g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
            }
        }
        else if(message[0].equalsIgnoreCase("ACTON_IMAGE")){

            //GET IMAGE FROM THE HASHMAP
            Image image = h.imageActonHash.get("ICON_" + message[1]).getSecond();

            if(message[2].equalsIgnoreCase("SET_VISIBILITY")){

                if(message[3].equalsIgnoreCase("true")) image.setVisible(true);
                else image.setVisible(false);

                g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
            }else if(message[2].equalsIgnoreCase("FADE_OUT")){
                image.addAction(Actions.fadeOut(Float.valueOf(message[3])));
                g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
            }else if(message[2].equalsIgnoreCase("SET_ALPHA")){
                image.addAction(Actions.alpha(Float.valueOf(message[3])));
                g.gm.sendMessage(g.messageOK(treeID, treeNumNode), treeID, treeNumNode);
            }
        }
    }

    public void setS(Stage s) {
        this.s = s;
    }


}
