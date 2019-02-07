package com.mygdx.safe.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.CpuSpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleByAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.mygdx.safe.Safe;

import static com.mygdx.safe.screens.ProfileScren.ac;

/**
 * Created by Boris.InspiratGames on 1/06/18.
 */

public class CentralGameScreen implements Screen {

    static final String TAG=CentralGameScreen.class.getSimpleName();
    private Safe _game;
    private Stage _stage;
    private Image door_left, door_right, turtlebutton, safykids;
    private Image aki_menu;
    private Image amber_menu;
    private Image ava_menu;
    private Image badragon_menu;
    private Image bart_menu;
    private Image buda_menu;
    private Image dylan_menu;
    private Image father_menu;
    private Image grace_menu;
    private Image lisa_menu;
    private Image mia_menu;
    private Image robored_menu;
    private Image zoe_menu;
    private Image menu_botton_closcaturtle;
    private Image menu_botton_face;
    private Image menu_botton_option;
    private Image menu_botton_turtle;
    private Image menu_botton_map;
    private Image t_Map1,t_Map2,t_Map3;

    float angle, customVelocityDegrees, scale,firsrtRendererCountBool1,countButtonPress1,countButtonPress2,countButtonPress3,countButtonPress4,countButtonPress5;
    int buttonPress;

    boolean touchDown,firstRender,firstRendererBool1;


    private Music m1, m2, m3, m4, m5, m6, m7, m8, m9;


    public CentralGameScreen(Safe game) {

        _game = game;

        createStage(_game,_game.spriteBatch);

        Safe.set_loadingCentralGameScreen(true);
        Safe.createProcess=false;
    }

    void createStage(Game game, CpuSpriteBatch gamespritebatch){

        angle = 0; customVelocityDegrees = 0; scale=0.2f; firsrtRendererCountBool1=0; countButtonPress1=0; countButtonPress2=0; countButtonPress3=0;
                                                                                      countButtonPress4=0; countButtonPress5=0;
        buttonPress=0;

        touchDown = false; firstRender=false; firstRendererBool1=false;


        _stage = new Stage(new ScalingViewport(Scaling.stretch, 1362, 790), gamespritebatch);

        door_left = new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_doors/menu_door_left.png")));
        door_right = new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_doors/menu_door_right.png")));

        turtlebutton = new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_bottons/menu_botton_closcaturtle.png")));
        safykids = new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_bottons/menu_botton_closcaturtle_avatar.png")));

        aki_menu = new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_chars/aki_menu.png")));
        amber_menu = new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_chars/amber_menu.png")));
        ava_menu = new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_chars/ava_menu.png")));
        badragon_menu = new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_chars/badragon_menu.png")));
        bart_menu = new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_chars/bart_menu.png")));
        buda_menu = new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_chars/buda_menu.png")));
        dylan_menu = new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_chars/dylan_menu.png")));
        father_menu = new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_chars/father_menu.png")));
        grace_menu = new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_chars/grace_menu.png")));
        lisa_menu = new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_chars/lisa_menu.png")));
        mia_menu = new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_chars/mia_menu.png")));
        robored_menu = new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_chars/robored_menu.png")));
        zoe_menu = new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_chars/zoe_menu.png")));
        menu_botton_closcaturtle = new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_bottons/menu_botton_closcaturtle.png")));
        menu_botton_face = new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_bottons/menu_botton_face.png")));
        menu_botton_option = new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_bottons/menu_botton_option.png")));
        menu_botton_turtle = new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_bottons/menu_botton_turtle.png")));
        menu_botton_map = new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_bottons/menu_botton_map.png")));

        t_Map1     = new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01.png"  )));
        t_Map2     = new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02.png"  )));
        t_Map3     = new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03.png"  )));


        turtlebutton.setPosition(395, 64);
        safykids.setPosition(560, 309);


        menu_botton_option  . setPosition(26, 18);
        menu_botton_face    . setPosition(58, 596);
        menu_botton_turtle  . setPosition(1160, 605);
        menu_botton_map     . setPosition(1147, 0);

        turtlebutton.setScale(0.95f);

        t_Map1.      setPosition(0,800);
        t_Map2.      setPosition(0,800);
        t_Map3.      setPosition(0,800);

        turtlebutton .setOrigin(Align.center);
        safykids     .setOrigin(Align.center);

        door_left.setPosition(0, 0);
        door_right.setPosition(0, 0);

        _stage.addActor(door_left);
        _stage.addActor(door_right);


        _stage.addActor(buda_menu);
        _stage.addActor(dylan_menu);
        _stage.addActor(father_menu);
        _stage.addActor(mia_menu);
        _stage.addActor(robored_menu);
        _stage.addActor(grace_menu);
        _stage.addActor(amber_menu);
        _stage.addActor(bart_menu);
        _stage.addActor(ava_menu);
        _stage.addActor(zoe_menu);
        _stage.addActor(aki_menu);
        _stage.addActor(badragon_menu);


        _stage.addActor(turtlebutton  );
        _stage.addActor(safykids      );

        _stage.addActor(menu_botton_turtle  );
        _stage.addActor(menu_botton_face    );
        _stage.addActor(menu_botton_option  );
        _stage.addActor(menu_botton_map     );

        _stage.addActor(t_Map1);
        _stage.addActor(t_Map2);
        _stage.addActor(t_Map3);



        setSizeAndPositions();

        m1 = _game.smm.loadMusic("sound/buttonclickLVL1.ogg", false);
        m2 = _game.smm.loadMusic("sound/buttonclickUPLVL1.ogg", false);
        m3 = _game.smm.loadMusic("sound/crongLVL1.ogg", false);
        m4 = _game.smm.loadMusic("sound/fffoUuUUuUH.ogg", false);
        m5 = _game.smm.loadMusic("sound/reversefffoUuUUuUH.ogg",false);
        m8 = _game.smm.loadMusic("sound/frfrfrfrfrLVL1.mp3",false);

    }



    void setSizeAndPositions(){

         float scale =0.12f;

         buda_menu      .setScale(scale);
         dylan_menu     .setScale(scale);
         father_menu    .setScale(scale);
         mia_menu       .setScale(scale);
         robored_menu   .setScale(scale);
         grace_menu     .setScale(scale);
         amber_menu     .setScale(scale);
         bart_menu      .setScale(scale);
         ava_menu       .setScale(scale);
         zoe_menu       .setScale(scale);
         aki_menu       .setScale(scale);
         badragon_menu  .setScale(scale);



         buda_menu     .setOrigin(buda_menu    .getWidth()/2, 0);
         dylan_menu    .setOrigin(dylan_menu   .getWidth()/2, 0);
         father_menu   .setOrigin(father_menu  .getWidth()/2, 0);
         mia_menu      .setOrigin(mia_menu     .getWidth()/2, 0);
         robored_menu  .setOrigin(robored_menu .getWidth()/2, 0);
         grace_menu    .setOrigin(grace_menu   .getWidth()/2, 0);
         amber_menu    .setOrigin(amber_menu   .getWidth()/2, 0);
         bart_menu     .setOrigin(bart_menu    .getWidth()/2, 0);
         ava_menu      .setOrigin(ava_menu     .getWidth()/2, 0);
         zoe_menu      .setOrigin(zoe_menu     .getWidth()/2, 0);
         aki_menu      .setOrigin(aki_menu     .getWidth()/2, 0);
         badragon_menu .setOrigin(badragon_menu.getWidth()/2, 0);


          buda_menu       .setPosition(1362/2-465/2,790/2 );
          dylan_menu      .setPosition(1362/2-465/2,790/2 );
          father_menu     .setPosition(1362/2-465/2,790/2 );
          mia_menu        .setPosition(1362/2-465/2,790/2 );
          robored_menu    .setPosition(1362/2-465/2,790/2 );
          grace_menu      .setPosition(1362/2-465/2,790/2 );
          amber_menu      .setPosition(1362/2-465/2,790/2 );
          bart_menu       .setPosition(1362/2-465/2,790/2 );

          ava_menu        .setPosition(1362/2-465/2,790/2 );
          zoe_menu        .setPosition(1362/2-465/2,790/2 );
          aki_menu        .setPosition(1362/2-465/2,790/2 );
          badragon_menu   .setPosition(1362/2-465/2,790/2 );


          buda_menu     .setRotation(0-360/12*0   + angle);
          dylan_menu    .setRotation(0-360/12*1   + angle);
          father_menu   .setRotation(0-360/12*2   + angle);
          mia_menu      .setRotation(0-360/12*3   + angle);
          robored_menu  .setRotation(0-360/12*4   + angle);
          grace_menu    .setRotation(0-360/12*5   + angle);
          amber_menu    .setRotation(0-360/12*6   + angle);
          bart_menu     .setRotation(0-360/12*7   + angle);
          ava_menu      .setRotation(0-360/12*8   + angle);
          zoe_menu      .setRotation(0-360/12*9   + angle);
          aki_menu      .setRotation(0-360/12*10  + angle);
          badragon_menu .setRotation(0-360/12*11  + angle);

          // buda_menu      .setVisible(false);
          // dylan_menu     .setVisible(false);
          // father_menu    .setVisible(false);
          // mia_menu       .setVisible(false);
          // robored_menu   .setVisible(false);
          // grace_menu     .setVisible(false);
          // amber_menu     .setVisible(false);
          // bart_menu      .setVisible(false);
          // ava_menu       .setVisible(false);
          // zoe_menu       .setVisible(false);
          // aki_menu       .setVisible(false);
          // badragon_menu  .setVisible(false);

        System.out.println(TAG+ " Adding listeners");

        menu_botton_turtle .addListener(listenerButtonEffect("menu_botton_turtle" , menu_botton_turtle ));
        menu_botton_face   .addListener(listenerButtonEffect("menu_botton_face"   , menu_botton_face   ));
        menu_botton_option .addListener(listenerButtonEffect("menu_botton_option" , menu_botton_option ));
        menu_botton_map    .addListener(listenerButtonEffect("menu_botton_map"    , menu_botton_map    ));
        turtlebutton       .addListener(listenerButtonEffect("turtlebutton"       , turtlebutton       ));
        safykids           .addListener(listenerButtonEffect("safykids"           , turtlebutton       ));

        buda_menu      .addAction(charactersAppear());
        dylan_menu     .addAction(charactersAppear());
        father_menu    .addAction(charactersAppear());
        mia_menu       .addAction(charactersAppear());
        robored_menu   .addAction(charactersAppear());
        grace_menu     .addAction(charactersAppear());
        amber_menu     .addAction(charactersAppear());
        bart_menu      .addAction(charactersAppear());
        ava_menu       .addAction(charactersAppear());
        zoe_menu       .addAction(charactersAppear());
        aki_menu       .addAction(charactersAppear());
        badragon_menu  .addAction(charactersAppear());

        System.out.println(TAG+ " Adding listeners END");



       //lisa_menu                .setPosition(1362/2,790/2);   lisa_menu                .setOrigin(1362/2,790/2);
    }

    public void calculaterotation(){

        if(angle>360) angle=0;

        angle+=1/20f+customVelocityDegrees;


        buda_menu     .setRotation(0-360/12*0   + angle);
        dylan_menu    .setRotation(0-360/12*1   + angle);
        father_menu   .setRotation(0-360/12*2   + angle);
        mia_menu      .setRotation(0-360/12*3   + angle);
        robored_menu  .setRotation(0-360/12*4   + angle);
        grace_menu    .setRotation(0-360/12*5   + angle);
        amber_menu    .setRotation(0-360/12*6   + angle);
        bart_menu     .setRotation(0-360/12*7   + angle);
        ava_menu      .setRotation(0-360/12*8   + angle);
        zoe_menu      .setRotation(0-360/12*9   + angle);
        aki_menu      .setRotation(0-360/12*10  + angle);
        badragon_menu .setRotation(0-360/12*11  + angle);

    }


    public InputListener listenerButtonEffect(final String butStr,final Image im){

        return new InputListener() {


            Image ima=im;
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(!im.hasActions()) {
                    System.out.println(TAG + " listener" + " [" + butStr + "]");
                    touchDown = true;
                    im.clearActions();
                    im.addAction(sequenceButtonizzer());
                    if (butStr.equals("menu_botton_turtle"  )){
                        System.out.println(TAG+ " BUTTON PRESS= "+buttonPress + " = "+ butStr);
                        buttonPress=1;
                    }

                    else if (butStr.equals("menu_botton_face"    )){
                        System.out.println(TAG+ " BUTTON PRESS= "+buttonPress + " = "+ butStr);
                        buttonPress=2;
                    }

                    else if (butStr.equals("menu_botton_option"  )){
                        System.out.println(TAG+ " BUTTON PRESS= "+buttonPress + " = "+ butStr);
                        buttonPress=3;
                    }

                    else if (butStr.equals("menu_botton_map"     )){
                        System.out.println(TAG+ " BUTTON PRESS= "+buttonPress + " = "+ butStr);
                        buttonPress=4;
                    }

                    else if (butStr.equals("turtlebutton"        )){
                        System.out.println(TAG+ " BUTTON PRESS= "+buttonPress + " = "+ butStr);
                        buttonPress=5;
                    }

                    else if (butStr.equals("safykids"            )){
                        System.out.println(TAG+ " BUTTON PRESS= "+buttonPress + " = "+ butStr);
                        buttonPress=5;
                    }



                    m1.setVolume(0.3f);
                    m1.play();
                }

                return true;
            }
        };

    }

    public ScaleToAction charactersAppear(){

        ScaleToAction sba=new ScaleToAction();
        sba.setScale(0.62f);
        sba.setDuration(1.1f);

        return sba;


    }

    public ScaleToAction charactersDissAppear(){

        ScaleToAction sba=new ScaleToAction();
        sba.setScale(0.12f);
        sba.setDuration(1.1f);

        return sba;


    }


    public DragListener dragCircleEffect(){
        return new DragListener() {

            @Override
            public void dragStart(InputEvent event, float x, float y, int pointer) {
                super.dragStart(event, x, y, pointer);
            }

            @Override
            public void drag(InputEvent event, float x, float y, int pointer) {
                super.drag(event, x, y, pointer);
            }

            @Override
            public void dragStop(InputEvent event, float x, float y, int pointer) {
                super.dragStop(event, x, y, pointer);
            }
        };

    }

    public SequenceAction sequenceButtonizzer(){
        return new  SequenceAction(new ParallelAction(Actions.moveBy(5,8,0.15f),Actions.scaleTo(0.9f,0.9f,0.15f)),
                                   new ParallelAction(Actions.moveBy(-5,-8,0.15f),Actions.scaleTo(1f,1f,0.15f)));
    }




    @Override
    public void show() {
        Gdx.input.setInputProcessor(_stage);
    }

    @Override
    public void render(float delta) {

        if(!firstRender){

            firstRender=true;
            // INITS
            Safe.get_mainGameScreen().set_gameState(MainGameScreen.GameState.PAUSED);



        }else {

            if(!firstRendererBool1) {
                firsrtRendererCountBool1+=delta;
                if(firsrtRendererCountBool1>0.5f){
                    firstRendererBool1=true;
                    m4.setVolume(0.7f);
                    m4.play();
                }
            }

            if(touchDown) touchDownProcess(delta);


            Gdx.gl.glClearColor(0, 0, 0, 0);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            calculaterotation();
            _stage.act(delta);
            _stage.draw();
        }

   }

   public void touchDownProcess(float delta){
       if(buttonPress==0) return;
       else
       {
           if(buttonPress==5){
               countButtonPress5+=delta;
               if(Math.abs(countButtonPress5-0.1)<=delta){
                   buda_menu      .addAction(charactersDissAppear());
                   dylan_menu     .addAction(charactersDissAppear());
                   father_menu    .addAction(charactersDissAppear());
                   mia_menu       .addAction(charactersDissAppear());
                   robored_menu   .addAction(charactersDissAppear());
                   grace_menu     .addAction(charactersDissAppear());
                   amber_menu     .addAction(charactersDissAppear());
                   bart_menu      .addAction(charactersDissAppear());
                   ava_menu       .addAction(charactersDissAppear());
                   zoe_menu       .addAction(charactersDissAppear());
                   aki_menu       .addAction(charactersDissAppear());
                   badragon_menu  .addAction(charactersDissAppear());
               }else if(Math.abs(countButtonPress5-0.2)<=delta){
                   m5.setVolume(0.7f);
                   m5.play();
               }else if(Math.abs(countButtonPress5-1.3)<=delta) {
                   menu_botton_turtle.addAction(new SequenceAction(Actions.fadeOut(0.5f), Actions.visible(false), Actions.alpha(1)));
                   menu_botton_face.addAction(new SequenceAction(Actions.fadeOut(0.5f), Actions.visible(false), Actions.alpha(1)));
                   menu_botton_option.addAction(new SequenceAction(Actions.fadeOut(0.5f), Actions.visible(false), Actions.alpha(1)));
                   menu_botton_map.addAction(new SequenceAction(Actions.fadeOut(0.5f), Actions.visible(false), Actions.alpha(1)));
                   m3.setVolume(7.0f);
                   m3.play();
               }else if(Math.abs(countButtonPress5-2.0)<=delta) {
                   buttonPress=0;
                   countButtonPress5=0;
                   touchDown=false;
                   Safe.get_mainGameScreen().commandInt=2; // DISPOSE CENTRALGAMESCREEN AND RE-CREATE THIS
                   Safe.get_mainGameScreen().command();
                   Safe.getInstance().setScreen(Safe.getInstance().getScreenType(Safe.ScreenType.MainGame));




               }


           }else if(buttonPress==4){
               countButtonPress4+=delta;
               if(Math.abs(countButtonPress4-0.1)<=delta){
                   buda_menu      .addAction(charactersDissAppear());
                   dylan_menu     .addAction(charactersDissAppear());
                   father_menu    .addAction(charactersDissAppear());
                   mia_menu       .addAction(charactersDissAppear());
                   robored_menu   .addAction(charactersDissAppear());
                   grace_menu     .addAction(charactersDissAppear());
                   amber_menu     .addAction(charactersDissAppear());
                   bart_menu      .addAction(charactersDissAppear());
                   ava_menu       .addAction(charactersDissAppear());
                   zoe_menu       .addAction(charactersDissAppear());
                   aki_menu       .addAction(charactersDissAppear());
                   badragon_menu  .addAction(charactersDissAppear());
               }else if(Math.abs(countButtonPress4-0.2)<=delta){
                   m5.setVolume(0.7f);
                   m5.play();
               }else if(Math.abs(countButtonPress4-1.3)<=delta) {
                   menu_botton_turtle.addAction(new SequenceAction(Actions.fadeOut(0.5f), Actions.visible(false), Actions.alpha(1)));
                   menu_botton_face.addAction(new SequenceAction(Actions.fadeOut(0.5f), Actions.visible(false), Actions.alpha(1)));
                   menu_botton_option.addAction(new SequenceAction(Actions.fadeOut(0.5f), Actions.visible(false), Actions.alpha(1)));
                   menu_botton_map.addAction(new SequenceAction(Actions.fadeOut(0.5f), Actions.visible(false), Actions.alpha(1)));
                   m3.setVolume(7.0f);
                   m3.play();
               }else if(Math.abs(countButtonPress4-1.5)<=delta) {
                   System.out.println(TAG+ " LEVEL="+ac.Level);
                   if (ac.Level < 10) {
                       t_Map1.setVisible(true);
                       t_Map1.addAction(Actions.alpha(1));
                       t_Map1.addAction(Actions.moveTo(0, 0, 0.7f));
                       System.out.println(TAG+ "MAP_1");
                   }
                   if (ac.Level > 9 && ac.Level < 19) {
                       t_Map2.setVisible(true);
                       t_Map1.addAction(Actions.alpha(1));
                       t_Map2.addAction(Actions.moveTo(0, 0, 0.7f));
                       System.out.println(TAG+ "MAP_2");
                   }
                   if (ac.Level > 18 && ac.Level < 28) {
                       t_Map3.setVisible(true);
                       t_Map1.addAction(Actions.alpha(1));
                       t_Map3.addAction(Actions.moveTo(0, 0, 0.7f));
                       System.out.println(TAG+ "MAP_3");
                   }


                   m8.play();
                   m8.setVolume(0.3f);
               }else if(Math.abs(countButtonPress4-2.4)<=delta){
                   buttonPress=0;
                   countButtonPress4=0;
                   touchDown=false;
                   t_Map1.      setPosition(0,800);
                   t_Map2.      setPosition(0,800);
                   t_Map3.      setPosition(0,800);

                   Safe.createProcess=false;
                   Safe.typScreen=Safe.ScreenType.CentralGameScreen;

                   Safe.getInstance().setScreen(Safe.getInstance().getScreenType(Safe.ScreenType.GeneralMenuScreen));

               }

           }

       }

   }

    @Override
    public void resize(int width, int height) {

        float percentage=790/1362;
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

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {

            _stage.dispose();

    }

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
