package com.mygdx.safe.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

import com.mygdx.safe.Safe;
import com.mygdx.safe.SoundMusicMation;
import com.mygdx.safe.SoundMusicMationsCharger;


import java.util.Random;

import static com.mygdx.safe.screens.ProfileScren.ac;


public class AgeScreen implements Screen {

    //TAG
    private static final String TAG = AgeScreen.class.getSimpleName();

    //STAGE
    private Stage _stage;

    //SAFE
    private Safe _game;

    //AGE DATA SCREEN
    static public String age="0";
    private Image turtlebutton,turtlebuttonnormal;
    private Image goButtonA;
    private Image yourAge;
    private Image shadowTurtlebutton;

    private Image door_left;
    private Image door_right;

    boolean time1control=false;
    float time1counter=0;
    boolean time2control=false;
    boolean time3control=false;
    boolean time4control=false;
    boolean time5control=false;
    boolean time6control=false;


    // SOUNDMUSICMATIONS SOUNDMUSIC BANK

    private SoundMusicMationsCharger smC;
    public SoundMusicMation smm;

    Music m1,m2,m3,m4,m5,m6;

    Music nm4m3 ;
    Music n4m3  ;
    Music n5m3  ;
    Music n6m3  ;
    Music n7m3  ;
    Music n8m3  ;
    Music n9m3  ;
    Music n10m3 ;
    Music n11m3 ;
    Music n12m3 ;
    Music nm12m3;

    SequenceAction ac1,ac2,ac3,ac4,ac5,ac6,ac7,ac8,ac9,
                   sanm4,san4,san5,san6,san7,san8,san9,san10,san11,san12,sanm12,
                   ranm4,ran4,ran5,ran6,ran7,ran8,ran9,ran10,ran11,ran12,ranm12;

    Image nm4,n4,n5,n6,n7,n8,n9,n10,n11,n12,nm12,
          pnm4,pn4,pn5,pn6,pn7,pn8,pn9,pn10,pn11,pn12,pnm12,menuAgeBart,menuAgeLisa,
          snm4,sn4,sn5,sn6,sn7,sn8,sn9,sn10,sn11,sn12,snm12;

    ImageButton bnm4,bn4,bn5,bn6,bn7,bn8,bn9,bn10,bn11,bn12,bnm12;

    float currentbutton=0;
    boolean currentListener=false;

    boolean  clickcontrolnm4  = false;
    boolean  clickcontroln4   = false;
    boolean  clickcontroln5   = false;
    boolean  clickcontroln6   = false;
    boolean  clickcontroln7   = false;
    boolean  clickcontroln8   = false;
    boolean  clickcontroln9   = false;
    boolean  clickcontroln10  = false;
    boolean  clickcontroln11  = false;
    boolean  clickcontroln12  = false;
    boolean  clickcontrolnm12 = false;

    boolean currentListenerTurtleAge=false;
    float currentListenerTurtleAgeCounter=0;

    boolean finalAnimationsControl=false;
    float finalAnimationsControlCounter=0;


    float counterCurrentListener=0;


    //_______________________________________________________________________________________________________________

    //CONSTRUCTOR
    public AgeScreen(Safe game){

        _game = game;

        smC=_game.smC;
        smm = _game.smm;

        m1 = _game.smm.loadMusic("sound/rotatingintroProfileLVL1.ogg",false);
        m2 = _game.smm.loadMusic("sound/rasssLVL1.ogg",false);
        m3  = _game.smm.loadMusic("sound/buttonclickLVL1.ogg",false);

        m4 = _game.smm.loadMusic("sound/picLVL1.ogg",false);
        m5 = _game.smm.loadMusic("sound/shrpfffLVL1.ogg",false);
        m6 = _game.smm.loadMusic("sound/crongLVL1.ogg",false);

        nm4m3  = _game.smm.loadMusic("sound/buttonclickLVL1.ogg",false);
        n4m3   = _game.smm.loadMusic("sound/buttonclickLVL1.ogg",false);
        n5m3   = _game.smm.loadMusic("sound/buttonclickLVL1.ogg",false);
        n6m3   = _game.smm.loadMusic("sound/buttonclickLVL1.ogg",false);
        n7m3   = _game.smm.loadMusic("sound/buttonclickLVL1.ogg",false);
        n8m3   = _game.smm.loadMusic("sound/buttonclickLVL1.ogg",false);
        n9m3   = _game.smm.loadMusic("sound/buttonclickLVL1.ogg",false);
        n10m3  = _game.smm.loadMusic("sound/buttonclickLVL1.ogg",false);
        n11m3  = _game.smm.loadMusic("sound/buttonclickLVL1.ogg",false);
        n12m3  = _game.smm.loadMusic("sound/buttonclickLVL1.ogg",false);
        nm12m3 = _game.smm.loadMusic("sound/buttonclickLVL1.ogg",false);

        turtlebuttonnormal = new Image(new Texture( Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_bottons/menu_botton_closcaturtle.png")));
        turtlebutton = new Image(new Texture( Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_age/menu_age_turtle.png")));
        shadowTurtlebutton = new Image(new Texture( Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_age/menu_age_shadow.png")));

        yourAge=new Image(new Texture( Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_age/menu_age_yourage.png")));

        goButtonA=new Image(new Texture( Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_profile/menu_profile_go_A.png")));

        door_left= new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_doors/menu_door_left.png")));
        door_right= new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_doors/menu_door_right.png")));

        menuAgeBart = new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_age/menu_age_Bart.png"  )));
        menuAgeLisa = new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_age/menu_age_Lisa.png"  )));

        nm4     =    new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_age/menu_age_-04.png"   )));
        n4      =    new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_age/menu_age_04.png"    )));
        n5      =    new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_age/menu_age_05.png"    )));
        n6      =    new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_age/menu_age_06.png"    )));
        n7      =    new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_age/menu_age_07.png"    )));
        n8      =    new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_age/menu_age_08.png"    )));
        n9      =    new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_age/menu_age_09.png"    )));
        n10     =    new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_age/menu_age_10.png"    )));
        n11     =    new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_age/menu_age_11.png"    )));
        n12     =    new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_age/menu_age_12.png"    )));
        nm12    =    new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_age/menu_age_+12.png"   )));

        pnm4     =    new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_age/menu_age_-04_clik.png"   )));
        pn4      =    new Image(new Texture(Gdx.files.internal(   "SAFE/ART/GENERIC/MENU/menu_age/menu_age_04_clik.png"    )));
        pn5      =    new Image(new Texture(Gdx.files.internal(   "SAFE/ART/GENERIC/MENU/menu_age/menu_age_05_clik.png"    )));
        pn6      =    new Image(new Texture(Gdx.files.internal(   "SAFE/ART/GENERIC/MENU/menu_age/menu_age_06_clik.png"    )));
        pn7      =    new Image(new Texture(Gdx.files.internal(   "SAFE/ART/GENERIC/MENU/menu_age/menu_age_07_clik.png"    )));
        pn8      =    new Image(new Texture(Gdx.files.internal(   "SAFE/ART/GENERIC/MENU/menu_age/menu_age_08_clik.png"    )));
        pn9      =    new Image(new Texture(Gdx.files.internal(   "SAFE/ART/GENERIC/MENU/menu_age/menu_age_09_clik.png"    )));
        pn10     =    new Image(new Texture(Gdx.files.internal(   "SAFE/ART/GENERIC/MENU/menu_age/menu_age_10_clik.png"    )));
        pn11     =    new Image(new Texture(Gdx.files.internal(   "SAFE/ART/GENERIC/MENU/menu_age/menu_age_11_clik.png"    )));
        pn12     =    new Image(new Texture(Gdx.files.internal(   "SAFE/ART/GENERIC/MENU/menu_age/menu_age_12_clik.png"    )));
        pnm12    =    new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_age/menu_age_+12_clik.png"   )));

        snm4     =    new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_age/menu_age_-04_clik.png"  )));
        sn4      =    new Image(new Texture(Gdx.files.internal(   "SAFE/ART/GENERIC/MENU/menu_age/menu_age_04_clik.png"  )));
        sn5      =    new Image(new Texture(Gdx.files.internal(   "SAFE/ART/GENERIC/MENU/menu_age/menu_age_05_clik.png"  )));
        sn6      =    new Image(new Texture(Gdx.files.internal(   "SAFE/ART/GENERIC/MENU/menu_age/menu_age_06_clik.png"  )));
        sn7      =    new Image(new Texture(Gdx.files.internal(   "SAFE/ART/GENERIC/MENU/menu_age/menu_age_07_clik.png"  )));
        sn8      =    new Image(new Texture(Gdx.files.internal(   "SAFE/ART/GENERIC/MENU/menu_age/menu_age_08_clik.png"  )));
        sn9      =    new Image(new Texture(Gdx.files.internal(   "SAFE/ART/GENERIC/MENU/menu_age/menu_age_09_clik.png"  )));
        sn10     =    new Image(new Texture(Gdx.files.internal(   "SAFE/ART/GENERIC/MENU/menu_age/menu_age_10_clik.png"  )));
        sn11     =    new Image(new Texture(Gdx.files.internal(   "SAFE/ART/GENERIC/MENU/menu_age/menu_age_11_clik.png"  )));
        sn12     =    new Image(new Texture(Gdx.files.internal(   "SAFE/ART/GENERIC/MENU/menu_age/menu_age_12_clik.png"  )));
        snm12    =    new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_age/menu_age_+12_clik.png"  )));
//turtlebutton.setPosition(451,181);

        bnm4   = new ImageButton(nm4  .getDrawable(),pnm4  .getDrawable(), snm4 .getDrawable() );
        bn4    = new ImageButton(n4   .getDrawable(),pn4   .getDrawable(), sn4  .getDrawable() );
        bn5    = new ImageButton(n5   .getDrawable(),pn5   .getDrawable(), sn5  .getDrawable() );
        bn6    = new ImageButton(n6   .getDrawable(),pn6   .getDrawable(), sn6  .getDrawable() );
        bn7    = new ImageButton(n7   .getDrawable(),pn7   .getDrawable(), sn7  .getDrawable() );
        bn8    = new ImageButton(n8   .getDrawable(),pn8   .getDrawable(), sn8  .getDrawable() );
        bn9    = new ImageButton(n9   .getDrawable(),pn9   .getDrawable(), sn9  .getDrawable() );
        bn10   = new ImageButton(n10  .getDrawable(),pn10  .getDrawable(), sn10 .getDrawable() );
        bn11   = new ImageButton(n11  .getDrawable(),pn11  .getDrawable(), sn11 .getDrawable() );
        bn12   = new ImageButton(n12  .getDrawable(),pn12  .getDrawable(), sn12 .getDrawable() );
        bnm12  = new ImageButton(nm12 .getDrawable(),pnm12 .getDrawable(), snm12.getDrawable() );


        bnm4  .setOrigin(Align.center)    ;bnm4   .setPosition(1362/2,790/2);
        bn4   .setOrigin(Align.center)    ;bn4    .setPosition(1362/2,790/2);
        bn5   .setOrigin(Align.center)    ;bn5    .setPosition(1362/2,790/2);
        bn6   .setOrigin(Align.center)    ;bn6    .setPosition(1362/2,790/2);
        bn7   .setOrigin(Align.center)    ;bn7    .setPosition(1362/2,790/2);
        bn8   .setOrigin(Align.center)    ;bn8    .setPosition(1362/2,790/2);
        bn9   .setOrigin(Align.center)    ;bn9    .setPosition(1362/2,790/2);
        bn10  .setOrigin(Align.center)    ;bn10   .setPosition(1362/2,790/2);
        bn11  .setOrigin(Align.center)    ;bn11   .setPosition(1362/2,790/2);
        bn12  .setOrigin(Align.center)    ;bn12   .setPosition(1362/2,790/2);
        bnm12 .setOrigin(Align.center)    ;bnm12  .setPosition(1362/2,790/2);

        float timemov=0.4f;

        Random randnm4  =new Random();
        Random randn4   =new Random();
        Random randn5   =new Random();
        Random randn6   =new Random();
        Random randn7   =new Random();
        Random randn8   =new Random();
        Random randn9   =new Random();
        Random randn10  =new Random();
        Random randn11  =new Random();
        Random randn12  =new Random();
        Random randnm12 =new Random();

        randnm4  .setSeed(System.nanoTime());
        randn4   .setSeed(System.nanoTime());
        randn5   .setSeed(System.nanoTime());
        randn6   .setSeed(System.nanoTime());
        randn7   .setSeed(System.nanoTime());
        randn8   .setSeed(System.nanoTime());
        randn9   .setSeed(System.nanoTime());
        randn10  .setSeed(System.nanoTime());
        randn11  .setSeed(System.nanoTime());
        randn12  .setSeed(System.nanoTime());
        randnm12 .setSeed(System.nanoTime());


        sanm4  =new SequenceAction(Actions.moveTo( 827.0f, 519.0f,timemov + (float) (randnm4 .nextInt(10000)/9999)) );
        san4   =new SequenceAction(Actions.moveTo( 903.0f, 367.0f,timemov + (float) (randn4  .nextInt(10000)/9999)) );
        san5   =new SequenceAction(Actions.moveTo( 881.0f, 192.0f,timemov + (float) (randn5  .nextInt(10000)/9999)) );
        san6   =new SequenceAction(Actions.moveTo( 763.0f, 56.0f ,timemov + (float) (randn6  .nextInt(10000)/9999)) );
        san7   =new SequenceAction(Actions.moveTo( 594.0f, 10.0f ,timemov + (float) (randn7  .nextInt(10000)/9999)) );
        san8   =new SequenceAction(Actions.moveTo( 422.0f, 61.0f ,timemov + (float) (randn8  .nextInt(10000)/9999)) );
        san9   =new SequenceAction(Actions.moveTo( 311.0f, 193.0f,timemov + (float) (randn9  .nextInt(10000)/9999)) );
        san10  =new SequenceAction(Actions.moveTo( 290.0f, 364.0f,timemov + (float) (randn10 .nextInt(10000)/9999)) );
        san11  =new SequenceAction(Actions.moveTo( 365.0f, 517.0f,timemov + (float) (randn11 .nextInt(10000)/9999)) );
        san12  =new SequenceAction(Actions.moveTo( 510.0f, 610.0f,timemov + (float) (randn12 .nextInt(10000)/9999)) );
        sanm12 =new SequenceAction(Actions.moveTo( 681.0f, 611.0f,timemov + (float) (randnm12.nextInt(10000)/9999)) );


        ranm4  =new SequenceAction(Actions.moveTo(1362/2,790/2,timemov));
        ran4   =new SequenceAction(Actions.moveTo(1362/2,790/2,timemov));
        ran5   =new SequenceAction(Actions.moveTo(1362/2,790/2,timemov));
        ran6   =new SequenceAction(Actions.moveTo(1362/2,790/2,timemov));
        ran7   =new SequenceAction(Actions.moveTo(1362/2,790/2,timemov));
        ran8   =new SequenceAction(Actions.moveTo(1362/2,790/2,timemov));
        ran9   =new SequenceAction(Actions.moveTo(1362/2,790/2,timemov));
        ran10  =new SequenceAction(Actions.moveTo(1362/2,790/2,timemov));
        ran11  =new SequenceAction(Actions.moveTo(1362/2,790/2,timemov));
        ran12  =new SequenceAction(Actions.moveTo(1362/2,790/2,timemov));
        ranm12 =new SequenceAction(Actions.moveTo(1362/2,790/2,timemov));
        turtlebutton.setPosition(394,59);
        turtlebuttonnormal.setPosition(395,42);

        shadowTurtlebutton.setPosition(232,0);
        yourAge.setPosition(507,246);

        menuAgeBart.setPosition(516,201);
        menuAgeLisa.setPosition(516,201);


        shadowTurtlebutton.setOrigin(Align.center);
        turtlebuttonnormal.setOrigin(Align.center);
        turtlebutton.setOrigin(Align.center);
        yourAge.setOrigin(Align.center);
        goButtonA.setOrigin(Align.center);
        menuAgeBart.setOrigin(Align.center);
        menuAgeLisa.setOrigin(Align.center);


        yourAge.setVisible(false);
        door_left.setPosition(-0,0);
        door_right.setPosition(0,0);

        goButtonA.setPosition(545,292);
        // ACTION FOR GOBUTTONA
        ac1=new SequenceAction();
        ac1.addAction(Actions.scaleTo(1f,0.1f,0.3f));
        ac1.addAction(Actions.visible(false));
        // ACTION FOR YOURAGE
        ac2=new SequenceAction();
        ac2.addAction(Actions.scaleTo(1f,0.1f,0.3f));
        ac2.addAction(Actions.visible(true));
        ac2.addAction(Actions.scaleTo(1f,1f,0.3f));

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
        ac7.addAction(Actions.scaleTo(0.8f,0.8f,0.2f));
        ac7.addAction(Actions.visible(true));
        ac7.addAction(Actions.scaleTo(1f,1f,0.2f));

        ac8=new SequenceAction();
        ac8.addAction(Actions.scaleTo(0.2f,0.2f,0.3f));
        ac8.addAction(Actions.visible(false));





        _stage = new Stage(new ScalingViewport(Scaling.stretch,1362,790),game.spriteBatch);
        _stage.addActor(door_left);
        _stage.addActor(door_right);


        //_stage.addActor(turtlebutton);
        _stage.addActor(shadowTurtlebutton);

        _stage.addActor(bnm4);
        _stage.addActor(bn4);
        _stage.addActor(bn5);
        _stage.addActor(bn6);
        _stage.addActor(bn7);
        _stage.addActor(bn8);
        _stage.addActor(bn9);
        _stage.addActor(bn10);
        _stage.addActor(bn11);
        _stage.addActor(bn12);
        _stage.addActor(bnm12);



        //_stage.addActor(shadowTurtlebutton);
        _stage.addActor(turtlebuttonnormal);
        _stage.addActor(turtlebutton);

        _stage.addActor(yourAge);
        _stage.addActor(goButtonA);
        _stage.addActor(menuAgeLisa);
        _stage.addActor(menuAgeBart);

        menuAgeBart.setVisible(false);
        menuAgeLisa.setVisible(false);
        shadowTurtlebutton.setScale(0.3f,0.3f);
        shadowTurtlebutton.setVisible(false);
        turtlebutton.setVisible(false);





        Safe.set_loadingAge(true);


    }

    //RENDER
    @Override
    public void render(float delta) {

        if(Safe.is_loadingAge() && !time1control)
            time1control=true;
        if(time1control && !time4control)
            time1counter+=delta;

        if(time1counter>0.4 && !time2control) {
            time2control=true;
            m5.play();
            m5.setVolume(0.4f);

            goButtonA.addAction(ac1);
            yourAge.addAction(ac2);

        }
        if(currentListenerTurtleAge){
            currentListenerTurtleAgeCounter+=delta;
        }

        if(finalAnimationsControl){
            finalAnimationsControlCounter+=delta;
        }

        if(finalAnimationsControlCounter>0.6 && finalAnimationsControl){
            finalAnimationsControl=false;
            finalAnimationsControlCounter=0;
            _game.setScreen(_game.getScreenType(Safe.ScreenType.MainGame));
        }

        if(currentListenerTurtleAgeCounter>0.21){
            currentListenerTurtleAgeCounter=0;
            currentListenerTurtleAge=false;
            finalAnimationsControl=true;
            finalAnimations();
        }

        if(time1counter>0.4 && !time3control) {
            time3control=true;
            turtlebuttonnormal.setVisible(false);
            turtlebutton.setVisible(true);
            turtlebutton.addAction(Actions.moveBy(0,+8));
            shadowTurtlebutton.addAction(ac7);
             bnm4  .addAction( sanm4  ) ;
             bn4   .addAction( san4   ) ;
             bn5   .addAction( san5   ) ;
             bn6   .addAction( san6   ) ;
             bn7   .addAction( san7   ) ;
             bn8   .addAction( san8   ) ;
             bn9   .addAction( san9   ) ;
             bn10  .addAction( san10  ) ;
             bn11  .addAction( san11  ) ;
             bn12  .addAction( san12  ) ;
             bnm12 .addAction( sanm12 ) ;
        }

        if(time1counter>1.5 && !time4control){
            time4control=true;
            addlisteners();
            int aux=turtlebutton.getZIndex();
            turtlebutton.setZIndex(bnm4.getZIndex());
            bnm4.setZIndex(aux);
            time1counter=0;
            time1control=false;
        }

        if(currentListener){
            counterCurrentListener+=delta;
        }


        if(counterCurrentListener>0.2 && currentListener) {

            time5control = false;

            if (currentbutton == -4 && !clickcontrolnm4) {
                clickcontrolnm4 = true;
                age = "4-";
            } else {
                clickcontrolnm4 = false;
            }
            if (currentbutton == 4 && !clickcontroln4) {
                clickcontroln4 = true;
                age = "4";
            } else {
                clickcontroln4 = false;
            }
            if (currentbutton == 5 && !clickcontroln5) {
                clickcontroln5 = true;
                age = "5";
            } else {
                clickcontroln5 = false;
            }
            if (currentbutton == 6 && !clickcontroln6) {
                clickcontroln6 = true;
                age = "6";
            } else {
                clickcontroln6 = false;
            }
            if (currentbutton == 7 && !clickcontroln7) {
                clickcontroln7 = true;
                age = "7";
            } else {
                clickcontroln7 = false;
            }
            if (currentbutton == 8 && !clickcontroln8) {
                clickcontroln8 = true;
                age = "8";
            } else {
                clickcontroln8 = false;
            }
            if (currentbutton == 9 && !clickcontroln9) {
                clickcontroln9 = true;
                age = "9";
            } else {
                clickcontroln9 = false;
            }
            if (currentbutton == 10 && !clickcontroln10) {
                clickcontroln10 = true;
                age = "10";
            } else {
                clickcontroln10 = false;
            }
            if (currentbutton == 11 && !clickcontroln11) {
                clickcontroln11 = true;
                age = "11";
            } else {
                clickcontroln11 = false;
            }
            if (currentbutton == 12 && !clickcontroln12) {
                clickcontroln12 = true;
                age = "12";
            } else {
                clickcontroln12 = false;
            }
            if (currentbutton == -12 && !clickcontrolnm12) {
                clickcontrolnm12 = true;
                age = "12+";
            } else {
                clickcontrolnm12 = false;
            }
            ac.Age=age;

            bnm4.setChecked(clickcontrolnm4);
            bn4.setChecked(clickcontroln4);
            bn5.setChecked(clickcontroln5);
            bn6.setChecked(clickcontroln6);
            bn7.setChecked(clickcontroln7);
            bn8.setChecked(clickcontroln8);
            bn9.setChecked(clickcontroln9);
            bn10.setChecked(clickcontroln10);
            bn11.setChecked(clickcontroln11);
            bn12.setChecked(clickcontroln12);
            bnm12.setChecked(clickcontrolnm12);

            if (currentbutton != 0) {
                System.out.println("PROFILESCREEN.AC.PLAYERTYPE=" + ac.PlayerType);
                if (ac.PlayerType.equalsIgnoreCase("girl")) {
                    if(!menuAgeLisa.isVisible()) {
                        menuAgeLisa.setVisible(true);
                        menuAgeBart.setVisible(false);
                        yourAge.setVisible(false);
                        m6.play();
                        m6.setVolume(1.0f);
                    }
                }
                if (ac.PlayerType.equalsIgnoreCase("boy")) {
                    if(!menuAgeBart.isVisible()) {
                        menuAgeBart.setVisible(true);
                        menuAgeLisa.setVisible(false);
                        yourAge.setVisible(false);
                        m6.play();
                        m6.setVolume(1.0f);
                    }
                }
            } else {
                menuAgeLisa.setVisible(false);
                menuAgeBart.setVisible(false);
                yourAge.setVisible(true);
                m6.play();
                m6.setVolume(1.0f);
            }
            currentListener=false;
            counterCurrentListener=0;

        }


        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        _stage.act(delta);
        _stage.draw();

    }

    private void finalAnimations(){

        int aux=turtlebutton.getZIndex();
        turtlebutton.setZIndex(bnm4.getZIndex());
        bnm4.setZIndex(aux);
        bnm4  .addAction( ranm4  ) ;
        bn4   .addAction( ran4   ) ;
        bn5   .addAction( ran5   ) ;
        bn6   .addAction( ran6   ) ;
        bn7   .addAction( ran7   ) ;
        bn8   .addAction( ran8   ) ;
        bn9   .addAction( ran9   ) ;
        bn10  .addAction( ran10  ) ;
        bn11  .addAction( ran11  ) ;
        bn12  .addAction( ran12  ) ;
        bnm12 .addAction( ranm12 ) ;
        yourAge.addAction(new SequenceAction(Actions.scaleTo(0.01f,0.01f,0.4f),Actions.visible(false)));
        menuAgeLisa.addAction(new SequenceAction(Actions.scaleTo(0.01f,0.01f,0.4f),Actions.visible(false)));
        menuAgeBart.addAction(new SequenceAction(Actions.scaleTo(0.01f,0.01f,0.4f),Actions.visible(false)));
        shadowTurtlebutton.addAction(ac8);


    }

    private void addlisteners(){

        //Listeners
        turtlebutton.addListener(new InputListener() {
                                     @Override
                                     public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                                         if (currentbutton!=0) {
                                             if (!currentListenerTurtleAge) {
                                                 currentListenerTurtleAge = true;
                                                 turtlebutton.clearActions();
                                                 yourAge.clearActions();
                                                 menuAgeBart.clearActions();
                                                 menuAgeLisa.clearActions();

                                                 ac3.restart();
                                                 ac4.restart();
                                                 ac5.restart();
                                                 ac6.restart();
                                                 turtlebutton.addAction(ac3);
                                                 yourAge.addAction(ac4);
                                                 menuAgeBart.addAction(ac5);
                                                 menuAgeLisa.addAction(ac6);
                                                 m3.play();
                                                 m3.setVolume(0.3f);
                                             }
                                         } else {

                                             m4.play();
                                             m4.setVolume(0.3f);
                                             turtlebutton.clearActions();
                                             turtlebutton.addAction(new SequenceAction(Actions.alpha(0.5f, 0.05f), Actions.alpha(1f, 0.05f)));
                                         }

                                         return true;
                                     }
                                 }
        );

        //Listeners
        menuAgeLisa.addListener(new InputListener() {
                                     @Override
                                     public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                                         if (currentbutton!=0) {
                                             if (!currentListenerTurtleAge) {
                                                 currentListenerTurtleAge = true;
                                                 turtlebutton.clearActions();
                                                 yourAge.clearActions();
                                                 menuAgeBart.clearActions();
                                                 menuAgeLisa.clearActions();

                                                 ac3.restart();
                                                 ac4.restart();
                                                 ac5.restart();
                                                 ac6.restart();
                                                 turtlebutton.addAction(ac3);
                                                 yourAge.addAction(ac4);
                                                 menuAgeBart.addAction(ac5);
                                                 menuAgeLisa.addAction(ac6);
                                                 m3.play();
                                                 m3.setVolume(0.3f);
                                             }
                                         } else {

                                             m4.play();
                                             m4.setVolume(0.3f);
                                             turtlebutton.clearActions();
                                             turtlebutton.addAction(new SequenceAction(Actions.alpha(0.5f, 0.05f), Actions.alpha(1f, 0.05f)));
                                         }

                                         return true;
                                     }
                                 }
        );

        //Listeners
        menuAgeBart.addListener(new InputListener() {
                                     @Override
                                     public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                                         if (currentbutton!=0) {
                                             if (!currentListenerTurtleAge) {
                                                 currentListenerTurtleAge = true;
                                                 turtlebutton.clearActions();
                                                 yourAge.clearActions();
                                                 menuAgeBart.clearActions();
                                                 menuAgeLisa.clearActions();

                                                 ac3.restart();
                                                 ac4.restart();
                                                 ac5.restart();
                                                 ac6.restart();
                                                 turtlebutton.addAction(ac3);
                                                 yourAge.addAction(ac4);
                                                 menuAgeBart.addAction(ac5);
                                                 menuAgeLisa.addAction(ac6);
                                                 m3.play();
                                                 m3.setVolume(0.3f);
                                             }
                                         } else {

                                             m4.play();
                                             m4.setVolume(0.3f);
                                             turtlebutton.clearActions();
                                             turtlebutton.addAction(new SequenceAction(Actions.alpha(0.5f, 0.05f), Actions.alpha(1f, 0.05f)));
                                         }

                                         return true;
                                     }
                                 }
        );
        //Listeners
        yourAge.addListener(new InputListener() {
                                     @Override
                                     public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                                         if (currentbutton!=0) {
                                             if (!currentListenerTurtleAge) {
                                                 currentListenerTurtleAge = true;
                                                 turtlebutton.clearActions();
                                                 yourAge.clearActions();
                                                 menuAgeBart.clearActions();
                                                 menuAgeLisa.clearActions();

                                                 ac3.restart();
                                                 ac4.restart();
                                                 ac5.restart();
                                                 ac6.restart();
                                                 turtlebutton.addAction(ac3);
                                                 yourAge.addAction(ac4);
                                                 menuAgeBart.addAction(ac5);
                                                 menuAgeLisa.addAction(ac6);
                                                 m3.play();
                                                 m3.setVolume(0.3f);
                                             }
                                         } else {

                                             m4.play();
                                             m4.setVolume(0.3f);
                                             turtlebutton.clearActions();
                                             turtlebutton.addAction(new SequenceAction(Actions.alpha(0.5f, 0.05f), Actions.alpha(1f, 0.05f)));
                                         }

                                         return true;
                                     }
                                 }
        );

        bnm4  .addListener(new InputListener() {
                                             @Override
                                             public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                                 if(!currentListener) {
                                                     currentListener = true;
                                                     if(currentbutton==-4) currentbutton=0; else currentbutton=-4;
                                                     System.out.println("BUTTON -4");
                                                     System.out.println("CURRENTBUTTON="+currentbutton);
                                                     nm4m3.play();
                                                     nm4m3.setVolume(0.3f);
                                                 }
                                             return true;
                                             }

                                             });
        bn4   .addListener(new InputListener() {
                                             @Override
                                             public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                                 if(!currentListener) {
                                                     currentListener = true;
                                                     if(currentbutton==4) currentbutton=0; else currentbutton=4;
                                                     System.out.println("BUTTON 4");
                                                     System.out.println("CURRENTBUTTON="+currentbutton);
                                                     n4m3.play();
                                                     n4m3.setVolume(0.3f);
                                                 }

                                             return true;
                                             }

                })                             ;
        bn5   .addListener(new InputListener() {
                                             @Override
                                             public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                                 if(!currentListener) {
                                                     currentListener = true;
                                                     if(currentbutton==5) currentbutton=0; else currentbutton=5;
                                                     System.out.println("BUTTON 5");
                                                     System.out.println("CURRENTBUTTON="+currentbutton);
                                                     n5m3.play();
                                                     n5m3.setVolume(0.3f);
                                                 }
                                             return true;
                                             }

                                             });
        bn6   .addListener(new InputListener() {
                                             @Override
                                             public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                                 if(!currentListener) {
                                                     currentListener = true;
                                                     if(currentbutton==6) currentbutton=0; else currentbutton=6;
                                                     System.out.println("BUTTON 6");
                                                     System.out.println("CURRENTBUTTON="+currentbutton);
                                                     n6m3.play();
                                                     n6m3.setVolume(0.3f);
                                                 }
                                             return true;
                                             }

                })                             ;
        bn7   .addListener(new InputListener() {
                                             @Override
                                             public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                                 if(!currentListener) {
                                                     currentListener = true;
                                                     if(currentbutton==7) currentbutton=0; else currentbutton=7;
                                                     System.out.println("BUTTON 7");
                                                     System.out.println("CURRENTBUTTON="+currentbutton);
                                                     n7m3.play();
                                                     n7m3.setVolume(0.3f);
                                                 }
                                             return true;
                                             }

                                             });
        bn8   .addListener(new InputListener() {
                                             @Override
                                             public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                                 if(!currentListener) {
                                                     currentListener = true;
                                                     if(currentbutton==8) currentbutton=0; else currentbutton=8;
                                                     System.out.println("BUTTON 8");
                                                     System.out.println("CURRENTBUTTON="+currentbutton);
                                                     n8m3.play();
                                                     n8m3.setVolume(0.3f);
                                                 }
                                             return true;
                                             }

                })                             ;
        bn9   .addListener(new InputListener() {
                                             @Override
                                             public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                                 if(!currentListener) {
                                                     currentListener = true;
                                                     if(currentbutton==9) currentbutton=0; else currentbutton=9;
                                                     System.out.println("BUTTON 9");
                                                     System.out.println("CURRENTBUTTON="+currentbutton);
                                                     n9m3.play();
                                                     n9m3.setVolume(0.3f);
                                                 }
                                             return true;
                                             }

                                             });
        bn10  .addListener(new InputListener() {
                                             @Override
                                             public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                                 if(!currentListener) {
                                                     currentListener = true;
                                                     if(currentbutton==10) currentbutton=0; else currentbutton=10;
                                                     System.out.println("BUTTON 10");
                                                     System.out.println("CURRENTBUTTON="+currentbutton);
                                                     n10m3.play();
                                                     n10m3.setVolume(0.3f);
                                                 }
                                             return true;
                                             }

                })                             ;
        bn11  .addListener(new InputListener() {
                                             @Override
                                             public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                                 if(!currentListener) {
                                                     currentListener = true;
                                                     if(currentbutton==11) currentbutton=0; else currentbutton=11;
                                                     System.out.println("BUTTON 11");
                                                     System.out.println("CURRENTBUTTON="+currentbutton);
                                                     n11m3.play();
                                                     n11m3.setVolume(0.3f);
                                                 }
                                             return true;
                                             }

                                             });
        bn12  .addListener(new InputListener() {
                                             @Override
                                             public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                                 if(!currentListener) {
                                                     currentListener = true;
                                                     if(currentbutton==12) currentbutton=0; else currentbutton=12;
                                                     System.out.println("BUTTON 12");
                                                     System.out.println("CURRENTBUTTON="+currentbutton);
                                                     n12m3.play();
                                                     n12m3.setVolume(0.3f);
                                                 }
                                             return true;
                                             }

                })                             ;
        bnm12 .addListener(new InputListener() {
                                             @Override
                                             public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                                 if(!currentListener) {
                                                     currentListener = true;
                                                     if(currentbutton==-12) currentbutton=0; else currentbutton=-12;
                                                     System.out.println("BUTTON +12");
                                                     System.out.println("CURRENTBUTTON="+currentbutton);
                                                     nm12m3.play();
                                                     nm12m3.setVolume(0.3f);
                                                 }
                                             return true;
                                             }

                                             });
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

    //RESUME
    @Override
    public void resume() {
    }

    //DISPOSE
    @Override
    public void dispose() {
        _stage.clear();
        _stage.dispose();
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

    //_______________________________________________________________________________________________________________

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


