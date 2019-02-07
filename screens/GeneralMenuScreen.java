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
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.mygdx.safe.Safe;

import static com.mygdx.safe.screens.ProfileScren.ac;

public class GeneralMenuScreen implements Screen {

    //TAG
    private static final String TAG = GeneralMenuScreen.class.getSimpleName();

    //STAGE
    private Stage _stage;

    //TRICEPTION
    private Safe _game;

    private  Image turtlebutton,safykids,door_left,door_right;

    private boolean initialRender=false;
    private boolean bnpress=false, buttonpress=false;
    private float bnpressfloat=0, buttonpressfloat=0, buttonpressAc=0;

    private Image  n1a ,  n1b ,  n1bn ,  n1c;
    private Image  n2a ,  n2b ,  n2bn ,  n2c;
    private Image  n3a ,  n3b ,  n3bn ,  n3c;
    private Image  n4a ,  n4b ,  n4bn ,  n4c;
    private Image  n5a ,  n5b ,  n5bn ,  n5c;
    private Image  n6a ,  n6b ,  n6bn ,  n6c;
    private Image  n7a ,  n7b ,  n7bn ,  n7c;
    private Image  n8a ,  n8b ,  n8bn ,  n8c;
    private Image  n9a ,  n9b ,  n9bn ,  n9c;
    private Image n10a , n10b , n10bn , n10c;
    private Image n11a , n11b , n11bn , n11c;
    private Image n12a , n12b , n12bn , n12c;
    private Image n13a , n13b , n13bn , n13c;
    private Image n14a , n14b , n14bn , n14c;
    private Image n15a , n15b , n15bn , n15c;
    private Image n16a , n16b , n16bn , n16c;
    private Image n17a , n17b , n17bn , n17c;
    private Image n18a , n18b , n18bn , n18c;
    private Image n19a , n19b , n19bn , n19c;
    private Image n20a , n20b , n20bn , n20c;
    private Image n21a , n21b , n21bn , n21c;
    private Image n22a , n22b , n22bn , n22c;
    private Image n23a , n23b , n23bn , n23c;
    private Image n24a , n24b , n24bn , n24c;
    private Image n25a , n25b , n25bn , n25c;
    private Image n26a , n26b , n26bn , n26c;
    private Image n27a , n27b , n27bn , n27c;

    private Image w1_01   ,w2_01  , w3_01 ;
    private Image w1_02   ,w2_02  , w3_02 ;
    private Image w1_03   ,w2_03  , w3_03 ;
    private Image w1_04   ,w2_04  , w3_04 ;
    private Image w1_05   ,w2_05  , w3_05 ;
    private Image w1_06   ,w2_06  , w3_06 ;
    private Image w1_07   ,w2_07  , w3_07 ;
    private Image w1_08   ,w2_08  , w3_08 ;
    private Image w1_09   ,w2_09  , w3_09 ;

    private Image arrowDown ;
    private Image arrowUp   ;
    private Image tExit     ;
    private Image t_Map1    ;
    private Image t_Map2    ;
    private Image t_Map3    ;

    private int frontMap=-1; // -1=None, 1=t_Map1, 2=t_Map2, 3=t_Map3;

    private ImageButton     bn1    ;
    private ImageButton     bn2    ;
    private ImageButton     bn3    ;
    private ImageButton     bn4    ;
    private ImageButton     bn5    ;
    private ImageButton     bn6    ;
    private ImageButton     bn7    ;
    private ImageButton     bn8    ;
    private ImageButton     bn9    ;
    private ImageButton    bn10    ;
    private ImageButton    bn11    ;
    private ImageButton    bn12    ;
    private ImageButton    bn13    ;
    private ImageButton    bn14    ;
    private ImageButton    bn15    ;
    private ImageButton    bn16    ;
    private ImageButton    bn17    ;
    private ImageButton    bn18    ;
    private ImageButton    bn19    ;
    private ImageButton    bn20    ;
    private ImageButton    bn21    ;
    private ImageButton    bn22    ;
    private ImageButton    bn23    ;
    private ImageButton    bn24    ;
    private ImageButton    bn25    ;
    private ImageButton    bn26    ;
    private ImageButton    bn27    ;

    private float position1_21_13x,position1_21_13y;
    private float position2_20_11x,position2_20_11y;
    private float position3_19_10x,position3_19_10y;
    private float position4_22_14x,position4_22_14y;
    private float position5_23_15x,position5_23_15y;
    private float position6_24_16x,position6_24_16y;
    private float position7_25_17x,position7_25_17y;
    private float position8_27_12x,position8_27_12y;
    private float position9_26_18x,position9_26_18y;

    // Down, UP and EXIT
    private SequenceAction ac2,ac3,ac4;
    private boolean arrowDownBool;
    private boolean   arrowUpBool;
    private boolean     tExitBool;
    private float arrowDownCount=0;
    private float arrowDownCountLimit=0;
    private float   arrowUpCount=0;
    private float arrowUpCountLimit=0;
    private float     tExitCount=0;

    private Music m1,m2,m3,m4,m5,m6,m7,m8,m9;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public GeneralMenuScreen(Safe game){

        _game = game;
        _stage = new Stage(new ScalingViewport(Scaling.stretch,1362,790),game.spriteBatch);

        door_left= new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_doors/menu_door_left.png")));
        door_right= new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_doors/menu_door_right.png")));

         n1a=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01_number_01_A.png" )));
         n2a=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01_number_02_A.png" )));
         n3a=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01_number_03_A.png" )));
         n4a=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01_number_04_A.png" )));
         n5a=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01_number_05_A.png" )));
         n6a=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01_number_06_A.png" )));
         n7a=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01_number_07_A.png" )));
         n8a=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01_number_08_A.png" )));
         n9a=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01_number_09_A.png" )));
        n10a=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_10_A.png" )));
        n11a=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_11_A.png" )));
        n12a=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_12_A.png" )));
        n13a=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_13_A.png" )));
        n14a=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_14_A.png" )));
        n15a=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_15_A.png" )));
        n16a=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_16_A.png" )));
        n17a=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_17_A.png" )));
        n18a=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_18_A.png" )));
        n19a=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_19_A.png" )));
        n20a=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03_number_20_A.png" )));
        n21a=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03_number_21_A.png" )));
        n22a=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03_number_22_A.png" )));
        n23a=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03_number_23_A.png" )));
        n24a=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03_number_24_A.png" )));
        n25a=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03_number_25_A.png" )));
        n26a=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03_number_26_A.png" )));
        n27a=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03_number_27_A.png" )));

         n1b=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01_number_01_B.png" )));
         n2b=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01_number_02_B.png" )));
         n3b=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01_number_03_B.png" )));
         n4b=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01_number_04_B.png" )));
         n5b=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01_number_05_B.png" )));
         n6b=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01_number_06_B.png" )));
         n7b=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01_number_07_B.png" )));
         n8b=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01_number_08_B.png" )));
         n9b=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01_number_09_B.png" )));
        n10b=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_10_B.png" )));
        n11b=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_11_B.png" )));
        n12b=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_12_B.png" )));
        n13b=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_13_B.png" )));
        n14b=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_14_B.png" )));
        n15b=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_15_B.png" )));
        n16b=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_16_B.png" )));
        n17b=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_17_B.png" )));
        n18b=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_18_B.png" )));
        n19b=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03_number_19_B.png" )));
        n20b=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03_number_20_B.png" )));
        n21b=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03_number_21_B.png" )));
        n22b=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03_number_22_B.png" )));
        n23b=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03_number_23_B.png" )));
        n24b=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03_number_24_B.png" )));
        n25b=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03_number_25_B.png" )));
        n26b=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03_number_26_B.png" )));
        n27b=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03_number_27_B.png" )));

          n1bn=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01_number_01BN.png" )));
          n2bn=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01_number_02BN.png" )));
          n3bn=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01_number_03BN.png" )));
          n4bn=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01_number_04BN.png" )));
          n5bn=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01_number_05BN.png" )));
          n6bn=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01_number_06BN.png" )));
          n7bn=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01_number_07BN.png" )));
          n8bn=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01_number_08BN.png" )));
          n9bn=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01_number_09BN.png" )));
         n10bn=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_10BN.png" )));
         n11bn=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_11BN.png" )));
         n12bn=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_12BN.png" )));
         n13bn=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_13BN.png" )));
         n14bn=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_14BN.png" )));
         n15bn=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_15BN.png" )));
         n16bn=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_16BN.png" )));
         n17bn=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_17BN.png" )));
         n18bn=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_18BN.png" )));
         n19bn=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03_number_19BN.png" )));
         n20bn=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03_number_20BN.png" )));
         n21bn=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03_number_21BN.png" )));
         n22bn=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03_number_22BN.png" )));
         n23bn=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03_number_23BN.png" )));
         n24bn=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03_number_24BN.png" )));
         n25bn=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03_number_25BN.png" )));
         n26bn=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03_number_26BN.png" )));
         n27bn=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03_number_27BN.png" )));

         n1c=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01_number_01_C.png" )));
         n2c=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01_number_02_C.png" )));
         n3c=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01_number_03_C.png" )));
         n4c=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01_number_04_C.png" )));
         n5c=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01_number_05_C.png" )));
         n6c=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01_number_06_C.png" )));
         n7c=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01_number_07_C.png" )));
         n8c=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01_number_08_C.png" )));
         n9c=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01_number_09_C.png" )));
        n10c=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_10_C.png" )));
        n11c=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_11_C.png" )));
        n12c=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_12_C.png" )));
        n13c=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_13_C.png" )));
        n14c=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_14_C.png" )));
        n15c=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_15_C.png" )));
        n16c=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_16_C.png" )));
        n17c=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_17_C.png" )));
        n18c=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02_number_18_C.png" )));
        n19c=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03_number_19_C.png" )));
        n20c=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03_number_20_C.png" )));
        n21c=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03_number_21_C.png" )));
        n22c=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03_number_22_C.png" )));
        n23c=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03_number_23_C.png" )));
        n24c=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03_number_24_C.png" )));
        n25c=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03_number_25_C.png" )));
        n26c=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03_number_26_C.png" )));
        n27c=new Image(new Texture(Gdx.files.internal(  "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03_number_27_C.png" )));

        w1_01 = new Image(new Texture(Gdx.files.internal( "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_way_1_01.png")));
        w1_02 = new Image(new Texture(Gdx.files.internal( "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_way_1_02.png")));
        w1_03 = new Image(new Texture(Gdx.files.internal( "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_way_1_03.png")));
        w1_04 = new Image(new Texture(Gdx.files.internal( "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_way_1_04.png")));
        w1_05 = new Image(new Texture(Gdx.files.internal( "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_way_1_05.png")));
        w1_06 = new Image(new Texture(Gdx.files.internal( "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_way_1_06.png")));
        w1_07 = new Image(new Texture(Gdx.files.internal( "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_way_1_07.png")));
        w1_08 = new Image(new Texture(Gdx.files.internal( "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_way_1_08.png")));
        w1_09 = new Image(new Texture(Gdx.files.internal( "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_way_1_09.png")));

        w2_01 = new Image(new Texture(Gdx.files.internal( "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_way_2_01.png")));
        w2_02 = new Image(new Texture(Gdx.files.internal( "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_way_2_02.png")));
        w2_03 = new Image(new Texture(Gdx.files.internal( "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_way_2_03.png")));
        w2_04 = new Image(new Texture(Gdx.files.internal( "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_way_2_04.png")));
        w2_05 = new Image(new Texture(Gdx.files.internal( "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_way_2_05.png")));
        w2_06 = new Image(new Texture(Gdx.files.internal( "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_way_2_06.png")));
        w2_07 = new Image(new Texture(Gdx.files.internal( "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_way_2_07.png")));
        w2_08 = new Image(new Texture(Gdx.files.internal( "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_way_2_08.png")));
        w2_09 = new Image(new Texture(Gdx.files.internal( "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_way_2_09.png")));

        w3_01 = new Image(new Texture(Gdx.files.internal( "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_way_3_01.png")));
        w3_02 = new Image(new Texture(Gdx.files.internal( "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_way_3_02.png")));
        w3_03 = new Image(new Texture(Gdx.files.internal( "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_way_3_03.png")));
        w3_04 = new Image(new Texture(Gdx.files.internal( "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_way_3_04.png")));
        w3_05 = new Image(new Texture(Gdx.files.internal( "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_way_3_05.png")));
        w3_06 = new Image(new Texture(Gdx.files.internal( "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_way_3_06.png")));
        w3_07 = new Image(new Texture(Gdx.files.internal( "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_way_3_07.png")));
        w3_08 = new Image(new Texture(Gdx.files.internal( "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_way_3_08.png")));
        w3_09 = new Image(new Texture(Gdx.files.internal( "SAFE/ART/GENERIC/MENU/menu_tablero/tablero_way_3_09.png")));

        arrowDown  = new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_tablero/arrow_down.png"     )));
        arrowUp    = new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_tablero/arrow_up.png"       )));
        tExit      = new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_tablero/tablero_exit.png"   )));
        t_Map1     = new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map01.png"  )));
        t_Map2     = new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map02.png"  )));
        t_Map3     = new Image(new Texture(Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_tablero/tablero_Map03.png"  )));

        turtlebutton = new Image(new Texture( Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_bottons/menu_botton_closcaturtle.png")));
        safykids=new Image(new Texture( Gdx.files.internal("SAFE/ART/GENERIC/MENU/menu_bottons/menu_botton_closcaturtle_avatar.png")));

           bn1    =new ImageButton(  n1a.getDrawable(),  n1b.getDrawable() ,   n1c.getDrawable() );
           bn2    =new ImageButton(  n2a.getDrawable(),  n2b.getDrawable() ,   n2c.getDrawable() );
           bn3    =new ImageButton(  n3a.getDrawable(),  n3b.getDrawable() ,   n3c.getDrawable() );
           bn4    =new ImageButton(  n4a.getDrawable(),  n4b.getDrawable() ,   n4c.getDrawable() );
           bn5    =new ImageButton(  n5a.getDrawable(),  n5b.getDrawable() ,   n5c.getDrawable() );
           bn6    =new ImageButton(  n6a.getDrawable(),  n6b.getDrawable() ,   n6c.getDrawable() );
           bn7    =new ImageButton(  n7a.getDrawable(),  n7b.getDrawable() ,   n7c.getDrawable() );
           bn8    =new ImageButton(  n8a.getDrawable(),  n8b.getDrawable() ,   n8c.getDrawable() );
           bn9    =new ImageButton(  n9a.getDrawable(),  n9b.getDrawable() ,   n9c.getDrawable() );
          bn10    =new ImageButton( n10a.getDrawable(), n10b.getDrawable() ,  n10c.getDrawable() );
          bn11    =new ImageButton( n11a.getDrawable(), n11b.getDrawable() ,  n11c.getDrawable() );
          bn12    =new ImageButton( n12a.getDrawable(), n12b.getDrawable() ,  n12c.getDrawable() );
          bn13    =new ImageButton( n13a.getDrawable(), n13b.getDrawable() ,  n13c.getDrawable() );
          bn14    =new ImageButton( n14a.getDrawable(), n14b.getDrawable() ,  n14c.getDrawable() );
          bn15    =new ImageButton( n15a.getDrawable(), n15b.getDrawable() ,  n15c.getDrawable() );
          bn16    =new ImageButton( n16a.getDrawable(), n16b.getDrawable() ,  n16c.getDrawable() );
          bn17    =new ImageButton( n17a.getDrawable(), n17b.getDrawable() ,  n17c.getDrawable() );
          bn18    =new ImageButton( n18a.getDrawable(), n18b.getDrawable() ,  n18c.getDrawable() );
          bn19    =new ImageButton( n19a.getDrawable(), n19b.getDrawable() ,  n19c.getDrawable() );
          bn20    =new ImageButton( n20a.getDrawable(), n20b.getDrawable() ,  n20c.getDrawable() );
          bn21    =new ImageButton( n21a.getDrawable(), n21b.getDrawable() ,  n21c.getDrawable() );
          bn22    =new ImageButton( n22a.getDrawable(), n22b.getDrawable() ,  n22c.getDrawable() );
          bn23    =new ImageButton( n23a.getDrawable(), n23b.getDrawable() ,  n23c.getDrawable() );
          bn24    =new ImageButton( n24a.getDrawable(), n24b.getDrawable() ,  n24c.getDrawable() );
          bn25    =new ImageButton( n25a.getDrawable(), n25b.getDrawable() ,  n25c.getDrawable() );
          bn26    =new ImageButton( n26a.getDrawable(), n26b.getDrawable() ,  n26c.getDrawable() );
          bn27    =new ImageButton( n27a.getDrawable(), n27b.getDrawable() ,  n27c.getDrawable() );


        /*

        5-23-15:155/572
        4-22-14:548/572
        3-19-10:931/572
        6-24-16:155/343
        1-21-13:548/343
        2-20-11:931/543
        7-25-17:155/126
        9-26-18:548/126
        8-27-12:931/126


         */


         position1_21_13x =  548 ; position1_21_13y= 343;
         position2_20_11x =  931 ; position2_20_11y= 343;
         position3_19_10x =  931 ; position3_19_10y= 572;
         position4_22_14x =  548 ; position4_22_14y= 572;
         position5_23_15x =  155 ; position5_23_15y= 572;
         position6_24_16x =  155 ; position6_24_16y= 343;
         position7_25_17x =  155 ; position7_25_17y= 126;
         position8_27_12x =  931 ; position8_27_12y= 126;
         position9_26_18x =  548 ; position9_26_18y= 126;


           bn1 .setOrigin(Align.center);
           bn2 .setOrigin(Align.center);
           bn3 .setOrigin(Align.center);
           bn4 .setOrigin(Align.center);
           bn5 .setOrigin(Align.center);
           bn6 .setOrigin(Align.center);
           bn7 .setOrigin(Align.center);
           bn8 .setOrigin(Align.center);
           bn9 .setOrigin(Align.center);
          bn10 .setOrigin(Align.center);
          bn11 .setOrigin(Align.center);
          bn12 .setOrigin(Align.center);
          bn13 .setOrigin(Align.center);
          bn14 .setOrigin(Align.center);
          bn15 .setOrigin(Align.center);
          bn16 .setOrigin(Align.center);
          bn17 .setOrigin(Align.center);
          bn18 .setOrigin(Align.center);
          bn19 .setOrigin(Align.center);
          bn20 .setOrigin(Align.center);
          bn21 .setOrigin(Align.center);
          bn22 .setOrigin(Align.center);
          bn23 .setOrigin(Align.center);
          bn24 .setOrigin(Align.center);
          bn25 .setOrigin(Align.center);
          bn26 .setOrigin(Align.center);
          bn27 .setOrigin(Align.center);

        /*

        5-23-15:155/572
        4-22-14:548/572
        3-19-10:931/572
        6-24-16:155/343
        1-21-13:548/343
        2-20-11:931/543
        7-25-17:155/126
        9-26-18:548/126
        8-27-12:931/126

        */

         bn1 .setPosition(position1_21_13x,  position1_21_13y   );
         bn2 .setPosition(position2_20_11x,  position2_20_11y   );
         bn3 .setPosition(position3_19_10x,  position3_19_10y   );
         bn4 .setPosition(position4_22_14x,  position4_22_14y   );
         bn5 .setPosition(position5_23_15x,  position5_23_15y   );
         bn6 .setPosition(position6_24_16x,  position6_24_16y   );
         bn7 .setPosition(position7_25_17x,  position7_25_17y   );
         bn8 .setPosition(position8_27_12x,  position8_27_12y   );
         bn9 .setPosition(position9_26_18x,  position9_26_18y   );
        bn10 .setPosition(position3_19_10x,  position3_19_10y   );
        bn11 .setPosition(position2_20_11x,  position2_20_11y   );
        bn12 .setPosition(position8_27_12x,  position8_27_12y   );
        bn13 .setPosition(position1_21_13x,  position1_21_13y   );
        bn14 .setPosition(position4_22_14x,  position4_22_14y   );
        bn15 .setPosition(position5_23_15x,  position5_23_15y   );
        bn16 .setPosition(position6_24_16x,  position6_24_16y   );
        bn17 .setPosition(position7_25_17x,  position7_25_17y   );
        bn18 .setPosition(position9_26_18x,  position9_26_18y   );
        bn19 .setPosition(position3_19_10x,  position3_19_10y   );
        bn20 .setPosition(position2_20_11x,  position2_20_11y   );
        bn21 .setPosition(position1_21_13x,  position1_21_13y   );
        bn22 .setPosition(position4_22_14x,  position4_22_14y   );
        bn23 .setPosition(position5_23_15x,  position5_23_15y   );
        bn24 .setPosition(position6_24_16x,  position6_24_16y   );
        bn25 .setPosition(position7_25_17x,  position7_25_17y   );
        bn26 .setPosition(position9_26_18x,  position9_26_18y   );
        bn27 .setPosition(position8_27_12x,  position8_27_12y   );

          n1bn .setPosition(position1_21_13x,  position1_21_13y );
          n2bn .setPosition(position2_20_11x,  position2_20_11y );
          n3bn .setPosition(position3_19_10x,  position3_19_10y );
          n4bn .setPosition(position4_22_14x,  position4_22_14y );
          n5bn .setPosition(position5_23_15x,  position5_23_15y );
          n6bn .setPosition(position6_24_16x,  position6_24_16y );
          n7bn .setPosition(position7_25_17x,  position7_25_17y );
          n8bn .setPosition(position8_27_12x,  position8_27_12y );
          n9bn .setPosition(position9_26_18x,  position9_26_18y );
         n10bn .setPosition(position3_19_10x,  position3_19_10y );
         n11bn .setPosition(position2_20_11x,  position2_20_11y );
         n12bn .setPosition(position8_27_12x,  position8_27_12y );
         n13bn .setPosition(position1_21_13x,  position1_21_13y );
         n14bn .setPosition(position4_22_14x,  position4_22_14y );
         n15bn .setPosition(position5_23_15x,  position5_23_15y );
         n16bn .setPosition(position6_24_16x,  position6_24_16y );
         n17bn .setPosition(position7_25_17x,  position7_25_17y );
         n18bn .setPosition(position9_26_18x,  position9_26_18y );
         n19bn .setPosition(position3_19_10x,  position3_19_10y );
         n20bn .setPosition(position2_20_11x,  position2_20_11y );
         n21bn .setPosition(position1_21_13x,  position1_21_13y );
         n22bn .setPosition(position4_22_14x,  position4_22_14y );
         n23bn .setPosition(position5_23_15x,  position5_23_15y );
         n24bn .setPosition(position6_24_16x,  position6_24_16y );
         n25bn .setPosition(position7_25_17x,  position7_25_17y );
         n26bn .setPosition(position9_26_18x,  position9_26_18y );
         n27bn .setPosition(position8_27_12x,  position8_27_12y );
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        w1_01.setPosition(0,0);
        w1_01.setPosition(0,0);
        w1_02.setPosition(0,0);
        w1_03.setPosition(0,0);
        w1_04.setPosition(0,0);
        w1_05.setPosition(0,0);
        w1_06.setPosition(0,0);
        w1_07.setPosition(0,0);
        w1_08.setPosition(0,0);
        w1_09.setPosition(0,0);
        w2_01.setPosition(0,0);
        w2_02.setPosition(0,0);
        w2_03.setPosition(0,0);
        w2_04.setPosition(0,0);
        w2_05.setPosition(0,0);
        w2_06.setPosition(0,0);
        w2_07.setPosition(0,0);
        w2_08.setPosition(0,0);
        w2_09.setPosition(0,0);
        w3_01.setPosition(0,0);
        w3_02.setPosition(0,0);
        w3_03.setPosition(0,0);
        w3_04.setPosition(0,0);
        w3_05.setPosition(0,0);
        w3_06.setPosition(0,0);
        w3_07.setPosition(0,0);
        w3_08.setPosition(0,0);
        w3_09.setPosition(0,0);

        t_Map1.      setPosition(0,0);
        t_Map2.      setPosition(0,0);
        t_Map3.      setPosition(0,0);
        tExit.       setPosition(1199,29);
        arrowUp.     setPosition(1213,790-368);
        arrowDown.   setPosition(1213,790-482);



        turtlebutton.setPosition(395,64);
        safykids.    setPosition(560,289);

        turtlebutton.setOrigin(Align.center);
        safykids.setOrigin(Align.center);

        door_left.setPosition(0,0);
        door_right.setPosition(0,0);

        _stage.addActor(door_left);
        _stage.addActor(door_right);
        _stage.addActor(turtlebutton);
        _stage.addActor(safykids);


        _stage.addActor(t_Map1);
        _stage.addActor(t_Map2);
        _stage.addActor(t_Map3);


        _stage.addActor(w1_01);
        _stage.addActor(w1_02);
        _stage.addActor(w1_03);
        _stage.addActor(w1_04);
        _stage.addActor(w1_05);
        _stage.addActor(w1_06);
        _stage.addActor(w1_07);
        _stage.addActor(w1_08);
        _stage.addActor(w1_09);
        _stage.addActor(w2_01);
        _stage.addActor(w2_02);
        _stage.addActor(w2_03);
        _stage.addActor(w2_04);
        _stage.addActor(w2_05);
        _stage.addActor(w2_06);
        _stage.addActor(w2_07);
        _stage.addActor(w2_08);
        _stage.addActor(w2_09);
        _stage.addActor(w3_01);
        _stage.addActor(w3_02);
        _stage.addActor(w3_03);
        _stage.addActor(w3_04);
        _stage.addActor(w3_05);
        _stage.addActor(w3_06);
        _stage.addActor(w3_07);
        _stage.addActor(w3_08);
        _stage.addActor(w3_09);


         _stage.addActor(   n1bn   );
         _stage.addActor(   n2bn   );
         _stage.addActor(   n3bn   );
         _stage.addActor(   n4bn   );
         _stage.addActor(   n5bn   );
         _stage.addActor(   n6bn   );
         _stage.addActor(   n7bn   );
         _stage.addActor(   n8bn   );
         _stage.addActor(   n9bn   );
         _stage.addActor(  n10bn   );
         _stage.addActor(  n11bn   );
         _stage.addActor(  n12bn   );
         _stage.addActor(  n13bn   );
         _stage.addActor(  n14bn   );
         _stage.addActor(  n15bn   );
         _stage.addActor(  n16bn   );
         _stage.addActor(  n17bn   );
         _stage.addActor(  n18bn   );
         _stage.addActor(  n19bn   );
         _stage.addActor(  n20bn   );
         _stage.addActor(  n21bn   );
         _stage.addActor(  n22bn   );
         _stage.addActor(  n23bn   );
         _stage.addActor(  n24bn   );
         _stage.addActor(  n25bn   );
         _stage.addActor(  n26bn   );
         _stage.addActor(  n27bn   );


         _stage.addActor(    bn1    );
         _stage.addActor(    bn2    );
         _stage.addActor(    bn3    );
         _stage.addActor(    bn4    );
         _stage.addActor(    bn5    );
         _stage.addActor(    bn6    );
         _stage.addActor(    bn7    );
         _stage.addActor(    bn8    );
         _stage.addActor(    bn9    );
         _stage.addActor(   bn10    );
         _stage.addActor(   bn11    );
         _stage.addActor(   bn12    );
         _stage.addActor(   bn13    );
         _stage.addActor(   bn14    );
         _stage.addActor(   bn15    );
         _stage.addActor(   bn16    );
         _stage.addActor(   bn17    );
         _stage.addActor(   bn18    );
         _stage.addActor(   bn19    );
         _stage.addActor(   bn20    );
         _stage.addActor(   bn21    );
         _stage.addActor(   bn22    );
         _stage.addActor(   bn23    );
         _stage.addActor(   bn24    );
         _stage.addActor(   bn25    );
         _stage.addActor(   bn26    );
         _stage.addActor(   bn27    );

        _stage.addActor(arrowDown);
        _stage.addActor(arrowUp);
        _stage.addActor(tExit);

         w1_01.    setVisible(false);
         w1_02.    setVisible(false);
         w1_03.    setVisible(false);
         w1_04.    setVisible(false);
         w1_05.    setVisible(false);
         w1_06.    setVisible(false);
         w1_07.    setVisible(false);
         w1_08.    setVisible(false);
         w1_09.    setVisible(false);
         w2_01.    setVisible(false);
         w2_02.    setVisible(false);
         w2_03.    setVisible(false);
         w2_04.    setVisible(false);
         w2_05.    setVisible(false);
         w2_06.    setVisible(false);
         w2_07.    setVisible(false);
         w2_08.    setVisible(false);
         w2_09.    setVisible(false);
         w3_01.    setVisible(false);
         w3_02.    setVisible(false);
         w3_03.    setVisible(false);
         w3_04.    setVisible(false);
         w3_05.    setVisible(false);
         w3_06.    setVisible(false);
         w3_07.    setVisible(false);
         w3_08.    setVisible(false);
         w3_09.    setVisible(false);
          n1bn.    setVisible(false);
          n2bn.    setVisible(false);
          n3bn.    setVisible(false);
          n4bn.    setVisible(false);
          n5bn.    setVisible(false);
          n6bn.    setVisible(false);
          n7bn.    setVisible(false);
          n8bn.    setVisible(false);
          n9bn.    setVisible(false);
         n10bn.    setVisible(false);
         n11bn.    setVisible(false);
         n12bn.    setVisible(false);
         n13bn.    setVisible(false);
         n14bn.    setVisible(false);
         n15bn.    setVisible(false);
         n16bn.    setVisible(false);
         n17bn.    setVisible(false);
         n18bn.    setVisible(false);
         n19bn.    setVisible(false);
         n20bn.    setVisible(false);
         n21bn.    setVisible(false);
         n22bn.    setVisible(false);
         n23bn.    setVisible(false);
         n24bn.    setVisible(false);
         n25bn.    setVisible(false);
         n26bn.    setVisible(false);
         n27bn.    setVisible(false);
          bn1.     setVisible(false);
          bn2.     setVisible(false);
          bn3.     setVisible(false);
          bn4.     setVisible(false);
          bn5.     setVisible(false);
          bn6.     setVisible(false);
          bn7.     setVisible(false);
          bn8.     setVisible(false);
          bn9.     setVisible(false);
         bn10.     setVisible(false);
         bn11.     setVisible(false);
         bn12.     setVisible(false);
         bn13.     setVisible(false);
         bn14.     setVisible(false);
         bn15.     setVisible(false);
         bn16.     setVisible(false);
         bn17.     setVisible(false);
         bn18.     setVisible(false);
         bn19.     setVisible(false);
         bn20.     setVisible(false);
         bn21.     setVisible(false);
         bn22.     setVisible(false);
         bn23.     setVisible(false);
         bn24.     setVisible(false);
         bn25.     setVisible(false);
         bn26.     setVisible(false);
         bn27.     setVisible(false);
         t_Map1.   setVisible(false);
         t_Map2.   setVisible(false);
         t_Map3.   setVisible(false);
         tExit.    setVisible(false);
         arrowUp.  setVisible(false);
         arrowDown.setVisible(false);



        ac2=new SequenceAction();
        ac2.addAction(new ParallelAction(Actions.moveBy(5,8,0.15f),Actions.scaleTo(0.9f,0.9f,0.15f)));
        ac2.addAction(new ParallelAction(Actions.moveBy(-5,-8,0.15f),Actions.scaleTo(1f,1f,0.15f)));

        ac3=new SequenceAction();
        ac3.addAction(new ParallelAction(Actions.moveBy(5,8,0.15f),Actions.scaleTo(0.9f,0.9f,0.15f)));
        ac3.addAction(new ParallelAction(Actions.moveBy(-5,-8,0.15f),Actions.scaleTo(1f,1f,0.15f)));


        ac4=new SequenceAction();
        ac4.addAction(new ParallelAction(Actions.moveBy(5,8,0.15f),Actions.scaleTo(0.9f,0.9f,0.15f)));
        ac4.addAction(new ParallelAction(Actions.moveBy(-5,-8,0.15f),Actions.scaleTo(1f,1f,0.15f)));


        m1=game.smm.loadMusic("sound/buttonclickLVL1.ogg",false);
        m2=game.smm.loadMusic("sound/buttonclickLVL1.ogg",false);
        m3=game.smm.loadMusic("sound/buttonclickLVL1.ogg",false);

        m4=game.smm.loadMusic("sound/frfrfrfrfrLVL1.mp3",false);
        m5=game.smm.loadMusic("sound/frfrfrfrfrLVL1.mp3",false);
        m6=game.smm.loadMusic("sound/frfrfrfrfrLVL1.mp3",false);

        m7=_game.smm.loadMusic("sound/buttonclickLVL1.ogg",false);
        m8=_game.smm.loadMusic("sound/buttonclickUPLVL1.ogg",false);
        m9=_game.smm.loadMusic("sound/crongLVL1.ogg",false);


        clearAndCreateImagebuttonListeners();



         //Listeners
        tExit.addListener(new InputListener() {
              @Override
              public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                  if(!tExit.hasActions()) {
                      tExit.clearActions();
                      ac2.restart();
                      tExit.addAction(ac2);
                      m1.setVolume(0.3f);
                      m1.play();

                      setButtonsToInvisible();
                       if(  arrowDown .   isVisible()  )     arrowDown  .    addAction(addCustomSequenceFadeOutAndVisibleFalse());
                       if(  arrowUp   .   isVisible()  )     arrowUp    .    addAction(addCustomSequenceFadeOutAndVisibleFalse());

                      if(t_Map1.isVisible()){
                          t_Map1.clearActions();
                          t_Map1.addAction((new SequenceAction(Actions.moveTo(0, 800, 1.15f),Actions.visible(false))));
                          m4.play();
                          m4.setVolume(0.3f);


                      }else if(t_Map2.isVisible()){
                          t_Map2.clearActions();
                          t_Map2.addAction((new SequenceAction(Actions.moveTo(0, 800, 1.15f),Actions.visible(false))));
                          m4.play();
                          m4.setVolume(0.3f);


                      }else if(t_Map3.isVisible()){

                          t_Map3.clearActions();
                          t_Map3.addAction((new SequenceAction(Actions.moveTo(0, 800,1.15f),Actions.visible(false))));
                          m4.play();
                          m4.setVolume(0.3f);

                      }
                      tExitBool=true;


                  }

              return true;
              }
        });

        arrowUp.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if(!arrowUp.hasActions() && !arrowUpBool) {
                        arrowUp.clearActions();
                        ac3.restart();
                        arrowUp.addAction(ac3);
                        m2.setVolume(0.3f);
                        m2.play();
                        arrowUpBool=true;
                        arrowUpCountLimit=1.2f;

                        setButtonsToInvisible();


                        if(t_Map2.isVisible()){
                            t_Map1.clearActions();
                            t_Map2.clearActions();
                            t_Map1.setPosition(0,800);
                            t_Map1.setVisible(true);

                            t_Map2.addAction((new SequenceAction(Actions.moveTo(0,-800, 1.15f),Actions.visible(false))));
                            t_Map1.addAction(Actions.moveTo(0,0,1.15f));
                            m4.play();
                            m4.setVolume(0.3f);


                        }else if(t_Map3.isVisible()){
                            t_Map2.clearActions();
                            t_Map3.clearActions();
                            t_Map2.setPosition(0,800);
                            t_Map2.setVisible(true);
                            t_Map3.addAction((new SequenceAction(Actions.moveTo(0,-800,1.15f),Actions.visible(false))));
                            t_Map2.addAction(Actions.moveTo(0, 0, 1.15f));
                            m4.play();
                            m4.setVolume(0.3f);

                        }
                    }



                return true;
            }
        });

        arrowDown.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(!arrowDown.hasActions() && !arrowDownBool) {
                    arrowDown.clearActions();
                    ac4.restart();
                    arrowDown.addAction(ac4);
                    m3.setVolume(0.3f);
                    m3.play();
                    arrowDownBool=true;
                    arrowDownCountLimit=1.2f;

                   setButtonsToInvisible();

                    if(t_Map1.isVisible()){
                        t_Map1.clearActions();
                        t_Map2.clearActions();
                        t_Map2.setPosition(0,-800);
                        t_Map2.setVisible(true);
                        t_Map1.addAction(new SequenceAction(Actions.moveTo(0, 800, 1.15f),Actions.addAction(Actions.visible(false))));

                        t_Map2.addAction(Actions.moveTo(0,0,1.15f));
                        m4.play();
                        m4.setVolume(0.3f);

                    }else if(t_Map2.isVisible()){
                        t_Map2.clearActions();
                        t_Map3.setPosition(0,-800);
                        t_Map3.clearActions();
                        t_Map3.setVisible(true);

                        t_Map2.addAction(new SequenceAction(Actions.moveTo(0, 800, 1.15f),Actions.addAction(Actions.visible(false))));
                        t_Map3.addAction(Actions.moveTo(0,0,1.15f));
                        m4.play();
                        m4.setVolume(0.3f);

                    }

                }



                return true;
            }
        });

        Safe.set_loadingGeneralMenu(true);
        Safe.createProcess=false;

    }

    public void setFrontMap(){
        //private int frontMap=-1; // -1=None, 1=t_Map1, 2=t_Map2, 3=t_Map3;

        if(t_Map1.isVisible()) frontMap=1;
        if(t_Map2.isVisible()) frontMap=2;
        if(t_Map3.isVisible()) frontMap=3;

        //////

        if(frontMap==1){
            arrowUp.setVisible(false);
            arrowDown.setVisible(true);
        }


        if(frontMap==2){
            arrowUp.setVisible(true);
            arrowDown.setVisible(true);
        }

        if(frontMap==3){
            arrowUp.setVisible(true);
            arrowDown.setVisible(false);
        }



        setButtons(0.05f);

        clearAndCreateImagebnListeners();
        clearAndCreateImagebuttonListeners();
    }

    public void setButtons(float time) {

        // INITIAL RENDER

        System.out.println("****************************************CONDIFLEVEL:"+ ac.Level);
        if (time < 0.002) {



            if (ac.Level < 10) {
                t_Map1.setVisible(true);
                t_Map2.setVisible(false);
                t_Map3.setVisible(false);


            }

            if (ac.Level > 9 && ac.Level < 19) {
                t_Map1.setVisible(false);
                t_Map2.setVisible(true);
                t_Map3.setVisible(false);

            }

            if (ac.Level > 18 && ac.Level < 28) {
                t_Map1.setVisible(false);
                t_Map2.setVisible(false);
                t_Map3.setVisible(true);


            }
            setFrontMap();
        }



        if(frontMap==1) {

            if (ac.Level == 1) { w1_01  .addAction(addCustomSequence1(time)); } else w1_01.setVisible(false);
            if (ac.Level == 2) { w1_02  .addAction(addCustomSequence1(time)); } else w1_02.setVisible(false);
            if (ac.Level == 3) { w1_03  .addAction(addCustomSequence1(time)); } else w1_03.setVisible(false);
            if (ac.Level == 4) { w1_04  .addAction(addCustomSequence1(time)); } else w1_04.setVisible(false);
            if (ac.Level == 5) { w1_05  .addAction(addCustomSequence1(time)); } else w1_05.setVisible(false);
            if (ac.Level == 6) { w1_06  .addAction(addCustomSequence1(time)); } else w1_06.setVisible(false);
            if (ac.Level == 7) { w1_07  .addAction(addCustomSequence1(time)); } else w1_07.setVisible(false);
            if (ac.Level == 8) { w1_08  .addAction(addCustomSequence1(time)); } else w1_08.setVisible(false);
            if (ac.Level >= 9) { w1_09  .addAction(addCustomSequence1(time)); } else w1_09.setVisible(false);

            if (ac.Level < 1)  { n1bn   .addAction(addCustomSequence1(time));  } else n1bn.setVisible(false);
            if (ac.Level < 2)  { n2bn   .addAction(addCustomSequence1(time));  } else n2bn.setVisible(false);
            if (ac.Level < 3)  { n3bn   .addAction(addCustomSequence1(time));  } else n3bn.setVisible(false);
            if (ac.Level < 4)  { n4bn   .addAction(addCustomSequence1(time));  } else n4bn.setVisible(false);
            if (ac.Level < 5)  { n5bn   .addAction(addCustomSequence1(time));  } else n5bn.setVisible(false);
            if (ac.Level < 6)  { n6bn   .addAction(addCustomSequence1(time));  } else n6bn.setVisible(false);
            if (ac.Level < 7)  { n7bn   .addAction(addCustomSequence1(time));  } else n7bn.setVisible(false);
            if (ac.Level < 8)  { n8bn   .addAction(addCustomSequence1(time));  } else n8bn.setVisible(false);
            if (ac.Level < 9)  { n9bn   .addAction(addCustomSequence1(time));  } else n9bn.setVisible(false);


            if (ac.Level >= 1 ) { bn1   .addAction(addCustomSequence1(time)); } else bn1.setVisible(false);
            if (ac.Level >= 2 ) { bn2   .addAction(addCustomSequence1(time)); } else bn2.setVisible(false);
            if (ac.Level >= 3 ) { bn3   .addAction(addCustomSequence1(time)); } else bn3.setVisible(false);
            if (ac.Level >= 4 ) { bn4   .addAction(addCustomSequence1(time)); } else bn4.setVisible(false);
            if (ac.Level >= 5 ) { bn5   .addAction(addCustomSequence1(time)); } else bn5.setVisible(false);
            if (ac.Level >= 6 ) { bn6   .addAction(addCustomSequence1(time)); } else bn6.setVisible(false);
            if (ac.Level >= 7 ) { bn7   .addAction(addCustomSequence1(time)); } else bn7.setVisible(false);
            if (ac.Level >= 8 ) { bn8   .addAction(addCustomSequence1(time)); } else bn8.setVisible(false);
            if (ac.Level >= 9 ) { bn9   .addAction(addCustomSequence1(time)); } else bn9.setVisible(false);

        }



        if(frontMap==2) {


            if (ac.Level == 10) {w2_01.     addAction(addCustomSequence1(time));   } else w2_01.setVisible(false);
            if (ac.Level == 11) {w2_02.     addAction(addCustomSequence1(time));   } else w2_02.setVisible(false);
            if (ac.Level == 12) {w2_03.     addAction(addCustomSequence1(time));   } else w2_03.setVisible(false);
            if (ac.Level == 13) {w2_04.     addAction(addCustomSequence1(time));   } else w2_04.setVisible(false);
            if (ac.Level == 14) {w2_05.     addAction(addCustomSequence1(time));   } else w2_05.setVisible(false);
            if (ac.Level == 15) {w2_06.     addAction(addCustomSequence1(time));   } else w2_06.setVisible(false);
            if (ac.Level == 16) {w2_07.     addAction(addCustomSequence1(time));   } else w2_07.setVisible(false);
            if (ac.Level == 17) {w2_08.     addAction(addCustomSequence1(time));   } else w2_08.setVisible(false);
            if (ac.Level >= 18) {w2_09.     addAction(addCustomSequence1(time));   } else w2_09.setVisible(false);

            if ( ac.Level < 10) {n10bn.     addAction(addCustomSequence1(time));   } else n10bn.setVisible(false);
            if ( ac.Level < 11) {n11bn.     addAction(addCustomSequence1(time));   } else n11bn.setVisible(false);
            if ( ac.Level < 12) {n12bn.     addAction(addCustomSequence1(time));   } else n12bn.setVisible(false);
            if ( ac.Level < 13) {n13bn.     addAction(addCustomSequence1(time));   } else n13bn.setVisible(false);
            if ( ac.Level < 14) {n14bn.     addAction(addCustomSequence1(time));   } else n14bn.setVisible(false);
            if ( ac.Level < 15) {n15bn.     addAction(addCustomSequence1(time));   } else n15bn.setVisible(false);
            if ( ac.Level < 16) {n16bn.     addAction(addCustomSequence1(time));   } else n16bn.setVisible(false);
            if ( ac.Level < 17) {n17bn.     addAction(addCustomSequence1(time));   } else n17bn.setVisible(false);
            if ( ac.Level < 18) {n18bn.     addAction(addCustomSequence1(time));   } else n18bn.setVisible(false);

            if (ac.Level >= 10 ) { bn10.    addAction(addCustomSequence1(time));   } else bn10.setVisible(false);
            if (ac.Level >= 11 ) { bn11.    addAction(addCustomSequence1(time));   } else bn11.setVisible(false);
            if (ac.Level >= 12 ) { bn12.    addAction(addCustomSequence1(time));   } else bn12.setVisible(false);
            if (ac.Level >= 13 ) { bn13.    addAction(addCustomSequence1(time));   } else bn13.setVisible(false);
            if (ac.Level >= 14 ) { bn14.    addAction(addCustomSequence1(time));   } else bn14.setVisible(false);
            if (ac.Level >= 15 ) { bn15.    addAction(addCustomSequence1(time));   } else bn15.setVisible(false);
            if (ac.Level >= 16 ) { bn16.    addAction(addCustomSequence1(time));   } else bn16.setVisible(false);
            if (ac.Level >= 17 ) { bn17.    addAction(addCustomSequence1(time));   } else bn17.setVisible(false);
            if (ac.Level >= 18 ) { bn18.    addAction(addCustomSequence1(time));   } else bn18.setVisible(false);

        }

        if(frontMap==3) {

            if (ac.Level == 19) { w3_01.    addAction(addCustomSequence1(time));    } else w3_01.setVisible(false);
            if (ac.Level == 20) { w3_02.    addAction(addCustomSequence1(time));    } else w3_02.setVisible(false);
            if (ac.Level == 21) { w3_03.    addAction(addCustomSequence1(time));    } else w3_03.setVisible(false);
            if (ac.Level == 22) { w3_04.    addAction(addCustomSequence1(time));    } else w3_04.setVisible(false);
            if (ac.Level == 23) { w3_05.    addAction(addCustomSequence1(time));    } else w3_05.setVisible(false);
            if (ac.Level == 24) { w3_06.    addAction(addCustomSequence1(time));    } else w3_06.setVisible(false);
            if (ac.Level == 25) { w3_07.    addAction(addCustomSequence1(time));    } else w3_07.setVisible(false);
            if (ac.Level == 26) { w3_08.    addAction(addCustomSequence1(time));    } else w3_08.setVisible(false);
            if (ac.Level == 27) { w3_09.    addAction(addCustomSequence1(time));    } else w3_09.setVisible(false);

            if ( ac.Level < 19) { n19bn.    addAction(addCustomSequence1(time));        } else n19bn.setVisible(false);
            if ( ac.Level < 20) { n20bn.    addAction(addCustomSequence1(time));        } else n20bn.setVisible(false);
            if ( ac.Level < 21) { n21bn.    addAction(addCustomSequence1(time));        } else n21bn.setVisible(false);
            if ( ac.Level < 22) { n22bn.    addAction(addCustomSequence1(time));        } else n22bn.setVisible(false);
            if ( ac.Level < 23) { n23bn.    addAction(addCustomSequence1(time));        } else n23bn.setVisible(false);
            if ( ac.Level < 24) { n24bn.    addAction(addCustomSequence1(time));        } else n24bn.setVisible(false);
            if ( ac.Level < 25) { n25bn.    addAction(addCustomSequence1(time));        } else n25bn.setVisible(false);
            if ( ac.Level < 26) { n26bn.    addAction(addCustomSequence1(time));        } else n26bn.setVisible(false);
            if ( ac.Level < 27) { n27bn.    addAction(addCustomSequence1(time));        } else n27bn.setVisible(false);

            if (ac.Level >= 19) { bn19.     addAction(addCustomSequence1(time));       } else bn19.setVisible(false);
            if (ac.Level >= 20) { bn20.     addAction(addCustomSequence1(time));       } else bn20.setVisible(false);
            if (ac.Level >= 21) { bn21.     addAction(addCustomSequence1(time));       } else bn21.setVisible(false);
            if (ac.Level >= 22) { bn22.     addAction(addCustomSequence1(time));       } else bn22.setVisible(false);
            if (ac.Level >= 23) { bn23.     addAction(addCustomSequence1(time));       } else bn23.setVisible(false);
            if (ac.Level >= 24) { bn24.     addAction(addCustomSequence1(time));       } else bn24.setVisible(false);
            if (ac.Level >= 25) { bn25.     addAction(addCustomSequence1(time));       } else bn25.setVisible(false);
            if (ac.Level >= 26) { bn26.     addAction(addCustomSequence1(time));       } else bn26.setVisible(false);
            if (ac.Level >= 27) { bn27.     addAction(addCustomSequence1(time));       } else bn27.setVisible(false);
        }

    }

    public SequenceAction addCustomSequence1 (float time ){
        //return new SequenceAction(Actions.delay(time), Actions.visible(true), Actions.alpha(0), Actions.fadeIn(0.4f));
        return new SequenceAction( Actions.visible(true), Actions.alpha(0), Actions.fadeIn(0.4f));
    }

    public SequenceAction addCustomSequenceFadeIn(){
        return  new SequenceAction(Actions.alpha(0),Actions.fadeIn(0.4f));
    }

    public SequenceAction addCustomSequenceFadeOutAndVisibleFalse(){
        return new SequenceAction(Actions.alpha(1),Actions.fadeOut(0.4f),Actions.visible(false));
    }



    public void setButtonsToInvisible(){
        if(  w1_01.  isVisible()  )     w1_01.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  w1_02.  isVisible()  )     w1_02.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  w1_03.  isVisible()  )     w1_03.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  w1_04.  isVisible()  )     w1_04.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  w1_05.  isVisible()  )     w1_05.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  w1_06.  isVisible()  )     w1_06.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  w1_07.  isVisible()  )     w1_07.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  w1_08.  isVisible()  )     w1_08.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  w1_09.  isVisible()  )     w1_09.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  w2_01.  isVisible()  )     w2_01.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  w2_02.  isVisible()  )     w2_02.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  w2_03.  isVisible()  )     w2_03.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  w2_04.  isVisible()  )     w2_04.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  w2_05.  isVisible()  )     w2_05.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  w2_06.  isVisible()  )     w2_06.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  w2_07.  isVisible()  )     w2_07.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  w2_08.  isVisible()  )     w2_08.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  w2_09.  isVisible()  )     w2_09.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  w3_01.  isVisible()  )     w3_01.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  w3_02.  isVisible()  )     w3_02.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  w3_03.  isVisible()  )     w3_03.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  w3_04.  isVisible()  )     w3_04.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  w3_05.  isVisible()  )     w3_05.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  w3_06.  isVisible()  )     w3_06.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  w3_07.  isVisible()  )     w3_07.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  w3_08.  isVisible()  )     w3_08.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  w3_09.  isVisible()  )     w3_09.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  n1bn.   isVisible()  )     n1bn.        addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  n2bn.   isVisible()  )     n2bn.        addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  n3bn.   isVisible()  )     n3bn.        addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  n4bn.   isVisible()  )     n4bn.        addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  n5bn.   isVisible()  )     n5bn.        addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  n6bn.   isVisible()  )     n6bn.        addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  n7bn.   isVisible()  )     n7bn.        addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  n8bn.   isVisible()  )     n8bn.        addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  n9bn.   isVisible()  )     n9bn.        addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  n10bn.  isVisible()  )     n10bn.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  n11bn.  isVisible()  )     n11bn.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  n12bn.  isVisible()  )     n12bn.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  n13bn.  isVisible()  )     n13bn.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  n14bn.  isVisible()  )     n14bn.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  n15bn.  isVisible()  )     n15bn.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  n16bn.  isVisible()  )     n16bn.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  n17bn.  isVisible()  )     n17bn.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  n18bn.  isVisible()  )     n18bn.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  n19bn.  isVisible()  )     n19bn.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  n20bn.  isVisible()  )     n20bn.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  n21bn.  isVisible()  )     n21bn.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  n22bn.  isVisible()  )     n22bn.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  n23bn.  isVisible()  )     n23bn.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  n24bn.  isVisible()  )     n24bn.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  n25bn.  isVisible()  )     n25bn.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  n26bn.  isVisible()  )     n26bn.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  n27bn.  isVisible()  )     n27bn.       addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  bn1.    isVisible()  )     bn1.         addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  bn2.    isVisible()  )     bn2.         addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  bn3.    isVisible()  )     bn3.         addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  bn4.    isVisible()  )     bn4.         addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  bn5.    isVisible()  )     bn5.         addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  bn6.    isVisible()  )     bn6.         addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  bn7.    isVisible()  )     bn7.         addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  bn8.    isVisible()  )     bn8.         addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  bn9.    isVisible()  )     bn9.         addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  bn10.   isVisible()  )     bn10.        addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  bn11.   isVisible()  )     bn11.        addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  bn12.   isVisible()  )     bn12.        addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  bn13.   isVisible()  )     bn13.        addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  bn14.   isVisible()  )     bn14.        addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  bn15.   isVisible()  )     bn15.        addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  bn16.   isVisible()  )     bn16.        addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  bn17.   isVisible()  )     bn17.        addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  bn18.   isVisible()  )     bn18.        addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  bn19.   isVisible()  )     bn19.        addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  bn20.   isVisible()  )     bn20.        addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  bn21.   isVisible()  )     bn21.        addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  bn22.   isVisible()  )     bn22.        addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  bn23.   isVisible()  )     bn23.        addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  bn24.   isVisible()  )     bn24.        addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  bn25.   isVisible()  )     bn25.        addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  bn26.   isVisible()  )     bn26.        addAction(addCustomSequenceFadeOutAndVisibleFalse());
        if(  bn27.   isVisible()  )     bn27.        addAction(addCustomSequenceFadeOutAndVisibleFalse());


    }

    public InputListener addListener1(final String imgStrin){
        return new InputListener() {

            boolean press=false;

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                if(imgStrin.equalsIgnoreCase("n1bn")  ) {System.out.print(" BN PRESS: " + imgStrin);   n1bn.setVisible(false);   bn1.  clearActions();  bn1.  addAction(Actions.alpha(1));  bn1.setChecked(false);   if(! bn1. isVisible()) { System.out.println("  BUTTON APPEAR:   bn1 " );  bn1.addAction(Actions.alpha(1)); bn1.  setVisible(true); press=true; bnpress=true; }}
                if(imgStrin.equalsIgnoreCase("n2bn")  ) {System.out.print(" BN PRESS: " + imgStrin);   n2bn.setVisible(false);   bn2.  clearActions();  bn2.  addAction(Actions.alpha(1));  bn2.setChecked(false);   if(! bn2. isVisible()) { System.out.println("  BUTTON APPEAR:   bn2 " );  bn2.addAction(Actions.alpha(1)); bn2.  setVisible(true); press=true; bnpress=true; }}
                if(imgStrin.equalsIgnoreCase("n3bn")  ) {System.out.print(" BN PRESS: " + imgStrin);   n3bn.setVisible(false);   bn3.  clearActions();  bn3.  addAction(Actions.alpha(1));  bn3.setChecked(false);   if(! bn3. isVisible()) { System.out.println("  BUTTON APPEAR:   bn3 " );  bn3.addAction(Actions.alpha(1)); bn3.  setVisible(true); press=true; bnpress=true; }}
                if(imgStrin.equalsIgnoreCase("n4bn")  ) {System.out.print(" BN PRESS: " + imgStrin);   n4bn.setVisible(false);   bn4.  clearActions();  bn4.  addAction(Actions.alpha(1));  bn4.setChecked(false);   if(! bn4. isVisible()) { System.out.println("  BUTTON APPEAR:   bn4 " );  bn4.addAction(Actions.alpha(1)); bn4.  setVisible(true); press=true; bnpress=true; }}
                if(imgStrin.equalsIgnoreCase("n5bn")  ) {System.out.print(" BN PRESS: " + imgStrin);   n5bn.setVisible(false);   bn5.  clearActions();  bn5.  addAction(Actions.alpha(1));  bn5.setChecked(false);   if(! bn5. isVisible()) { System.out.println("  BUTTON APPEAR:   bn5 " );  bn5.addAction(Actions.alpha(1)); bn5.  setVisible(true); press=true; bnpress=true; }}
                if(imgStrin.equalsIgnoreCase("n6bn")  ) {System.out.print(" BN PRESS: " + imgStrin);   n6bn.setVisible(false);   bn6.  clearActions();  bn6.  addAction(Actions.alpha(1));  bn6.setChecked(false);   if(! bn6. isVisible()) { System.out.println("  BUTTON APPEAR:   bn6 " );  bn6.addAction(Actions.alpha(1)); bn6.  setVisible(true); press=true; bnpress=true; }}
                if(imgStrin.equalsIgnoreCase("n7bn")  ) {System.out.print(" BN PRESS: " + imgStrin);   n7bn.setVisible(false);   bn7.  clearActions();  bn7.  addAction(Actions.alpha(1));  bn7.setChecked(false);   if(! bn7. isVisible()) { System.out.println("  BUTTON APPEAR:   bn7 " );  bn7.addAction(Actions.alpha(1)); bn7.  setVisible(true); press=true; bnpress=true; }}
                if(imgStrin.equalsIgnoreCase("n8bn")  ) {System.out.print(" BN PRESS: " + imgStrin);   n8bn.setVisible(false);   bn8.  clearActions();  bn8.  addAction(Actions.alpha(1));  bn8.setChecked(false);   if(! bn8. isVisible()) { System.out.println("  BUTTON APPEAR:   bn8 " );  bn8.addAction(Actions.alpha(1)); bn8.  setVisible(true); press=true; bnpress=true; }}
                if(imgStrin.equalsIgnoreCase("n9bn")  ) {System.out.print(" BN PRESS: " + imgStrin);   n9bn.setVisible(false);   bn9.  clearActions();  bn9.  addAction(Actions.alpha(1));  bn9.setChecked(false);   if(! bn9. isVisible()) { System.out.println("  BUTTON APPEAR:   bn9 " );  bn9.addAction(Actions.alpha(1)); bn9.  setVisible(true); press=true; bnpress=true; }}
                if(imgStrin.equalsIgnoreCase("n10bn") ) {System.out.print(" BN PRESS: " + imgStrin);  n10bn.setVisible(false);  bn10.  clearActions(); bn10.  addAction(Actions.alpha(1)); bn10.setChecked(false);   if(!bn10. isVisible()) { System.out.println("  BUTTON APPEAR:  bn10 " ); bn10.addAction(Actions.alpha(1));bn10.  setVisible(true); press=true; bnpress=true; }}
                if(imgStrin.equalsIgnoreCase("n11bn") ) {System.out.print(" BN PRESS: " + imgStrin);  n11bn.setVisible(false);  bn11.  clearActions(); bn11.  addAction(Actions.alpha(1)); bn11.setChecked(false);   if(!bn11. isVisible()) { System.out.println("  BUTTON APPEAR:  bn11 " ); bn11.addAction(Actions.alpha(1));bn11.  setVisible(true); press=true; bnpress=true; }}
                if(imgStrin.equalsIgnoreCase("n12bn") ) {System.out.print(" BN PRESS: " + imgStrin);  n12bn.setVisible(false);  bn12.  clearActions(); bn12.  addAction(Actions.alpha(1)); bn12.setChecked(false);   if(!bn12. isVisible()) { System.out.println("  BUTTON APPEAR:  bn12 " ); bn12.addAction(Actions.alpha(1));bn12.  setVisible(true); press=true; bnpress=true; }}
                if(imgStrin.equalsIgnoreCase("n13bn") ) {System.out.print(" BN PRESS: " + imgStrin);  n13bn.setVisible(false);  bn13.  clearActions(); bn13.  addAction(Actions.alpha(1)); bn13.setChecked(false);   if(!bn13. isVisible()) { System.out.println("  BUTTON APPEAR:  bn13 " ); bn13.addAction(Actions.alpha(1));bn13.  setVisible(true); press=true; bnpress=true; }}
                if(imgStrin.equalsIgnoreCase("n14bn") ) {System.out.print(" BN PRESS: " + imgStrin);  n14bn.setVisible(false);  bn14.  clearActions(); bn14.  addAction(Actions.alpha(1)); bn14.setChecked(false);   if(!bn14. isVisible()) { System.out.println("  BUTTON APPEAR:  bn14 " ); bn14.addAction(Actions.alpha(1));bn14.  setVisible(true); press=true; bnpress=true; }}
                if(imgStrin.equalsIgnoreCase("n15bn") ) {System.out.print(" BN PRESS: " + imgStrin);  n15bn.setVisible(false);  bn15.  clearActions(); bn15.  addAction(Actions.alpha(1)); bn15.setChecked(false);   if(!bn15. isVisible()) { System.out.println("  BUTTON APPEAR:  bn15 " ); bn15.addAction(Actions.alpha(1));bn15.  setVisible(true); press=true; bnpress=true; }}
                if(imgStrin.equalsIgnoreCase("n16bn") ) {System.out.print(" BN PRESS: " + imgStrin);  n16bn.setVisible(false);  bn16.  clearActions(); bn16.  addAction(Actions.alpha(1)); bn16.setChecked(false);   if(!bn16. isVisible()) { System.out.println("  BUTTON APPEAR:  bn16 " ); bn16.addAction(Actions.alpha(1));bn16.  setVisible(true); press=true; bnpress=true; }}
                if(imgStrin.equalsIgnoreCase("n17bn") ) {System.out.print(" BN PRESS: " + imgStrin);  n17bn.setVisible(false);  bn17.  clearActions(); bn17.  addAction(Actions.alpha(1)); bn17.setChecked(false);   if(!bn17. isVisible()) { System.out.println("  BUTTON APPEAR:  bn17 " ); bn17.addAction(Actions.alpha(1));bn17.  setVisible(true); press=true; bnpress=true; }}
                if(imgStrin.equalsIgnoreCase("n18bn") ) {System.out.print(" BN PRESS: " + imgStrin);  n18bn.setVisible(false);  bn18.  clearActions(); bn18.  addAction(Actions.alpha(1)); bn18.setChecked(false);   if(!bn18. isVisible()) { System.out.println("  BUTTON APPEAR:  bn18 " ); bn18.addAction(Actions.alpha(1));bn18.  setVisible(true); press=true; bnpress=true; }}
                if(imgStrin.equalsIgnoreCase("n19bn") ) {System.out.print(" BN PRESS: " + imgStrin);  n19bn.setVisible(false);  bn19.  clearActions(); bn19.  addAction(Actions.alpha(1)); bn19.setChecked(false);   if(!bn19. isVisible()) { System.out.println("  BUTTON APPEAR:  bn19 " ); bn19.addAction(Actions.alpha(1));bn19.  setVisible(true); press=true; bnpress=true; }}
                if(imgStrin.equalsIgnoreCase("n20bn") ) {System.out.print(" BN PRESS: " + imgStrin);  n20bn.setVisible(false);  bn20.  clearActions(); bn20.  addAction(Actions.alpha(1)); bn20.setChecked(false);   if(!bn20. isVisible()) { System.out.println("  BUTTON APPEAR:  bn20 " ); bn20.addAction(Actions.alpha(1));bn20.  setVisible(true); press=true; bnpress=true; }}
                if(imgStrin.equalsIgnoreCase("n21bn") ) {System.out.print(" BN PRESS: " + imgStrin);  n21bn.setVisible(false);  bn21.  clearActions(); bn21.  addAction(Actions.alpha(1)); bn21.setChecked(false);   if(!bn21. isVisible()) { System.out.println("  BUTTON APPEAR:  bn21 " ); bn21.addAction(Actions.alpha(1));bn21.  setVisible(true); press=true; bnpress=true; }}
                if(imgStrin.equalsIgnoreCase("n22bn") ) {System.out.print(" BN PRESS: " + imgStrin);  n22bn.setVisible(false);  bn22.  clearActions(); bn22.  addAction(Actions.alpha(1)); bn22.setChecked(false);   if(!bn22. isVisible()) { System.out.println("  BUTTON APPEAR:  bn22 " ); bn22.addAction(Actions.alpha(1));bn22.  setVisible(true); press=true; bnpress=true; }}
                if(imgStrin.equalsIgnoreCase("n23bn") ) {System.out.print(" BN PRESS: " + imgStrin);  n23bn.setVisible(false);  bn23.  clearActions(); bn23.  addAction(Actions.alpha(1)); bn23.setChecked(false);   if(!bn23. isVisible()) { System.out.println("  BUTTON APPEAR:  bn23 " ); bn23.addAction(Actions.alpha(1));bn23.  setVisible(true); press=true; bnpress=true; }}
                if(imgStrin.equalsIgnoreCase("n24bn") ) {System.out.print(" BN PRESS: " + imgStrin);  n24bn.setVisible(false);  bn24.  clearActions(); bn24.  addAction(Actions.alpha(1)); bn24.setChecked(false);   if(!bn24. isVisible()) { System.out.println("  BUTTON APPEAR:  bn24 " ); bn24.addAction(Actions.alpha(1));bn24.  setVisible(true); press=true; bnpress=true; }}
                if(imgStrin.equalsIgnoreCase("n25bn") ) {System.out.print(" BN PRESS: " + imgStrin);  n25bn.setVisible(false);  bn25.  clearActions(); bn25.  addAction(Actions.alpha(1)); bn25.setChecked(false);   if(!bn25. isVisible()) { System.out.println("  BUTTON APPEAR:  bn25 " ); bn25.addAction(Actions.alpha(1));bn25.  setVisible(true); press=true; bnpress=true; }}
                if(imgStrin.equalsIgnoreCase("n26bn") ) {System.out.print(" BN PRESS: " + imgStrin);  n26bn.setVisible(false);  bn26.  clearActions(); bn26.  addAction(Actions.alpha(1)); bn26.setChecked(false);   if(!bn26. isVisible()) { System.out.println("  BUTTON APPEAR:  bn26 " ); bn26.addAction(Actions.alpha(1));bn26.  setVisible(true); press=true; bnpress=true; }}
                if(imgStrin.equalsIgnoreCase("n27bn") ) {System.out.print(" BN PRESS: " + imgStrin);  n27bn.setVisible(false);  bn27.  clearActions(); bn27.  addAction(Actions.alpha(1)); bn27.setChecked(false);   if(!bn27. isVisible()) { System.out.println("  BUTTON APPEAR:  bn27 " ); bn27.addAction(Actions.alpha(1));bn27.  setVisible(true); press=true; bnpress=true; }}

                System.out.print("BN PRESS: " + imgStrin);
                m9.setVolume(0.3f);
                m9.play();



                return true;
            }
        };

    }

    public InputListener addListener2(final String imgStrin){
        return new InputListener() {

            boolean press=false;

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                if(imgStrin.equalsIgnoreCase("bn1")  ) {System.out.print("BUTTON PRESS: " + imgStrin);  bn1. setChecked(true); press=true; buttonpress=true; buttonpressAc=1   ; }
                if(imgStrin.equalsIgnoreCase("bn2")  ) {System.out.print("BUTTON PRESS: " + imgStrin);  bn2. setChecked(true); press=true; buttonpress=true; buttonpressAc=2   ; }
                if(imgStrin.equalsIgnoreCase("bn3")  ) {System.out.print("BUTTON PRESS: " + imgStrin);  bn3. setChecked(true); press=true; buttonpress=true; buttonpressAc=3   ; }
                if(imgStrin.equalsIgnoreCase("bn4")  ) {System.out.print("BUTTON PRESS: " + imgStrin);  bn4. setChecked(true); press=true; buttonpress=true; buttonpressAc=4   ; }
                if(imgStrin.equalsIgnoreCase("bn5")  ) {System.out.print("BUTTON PRESS: " + imgStrin);  bn5. setChecked(true); press=true; buttonpress=true; buttonpressAc=5   ; }
                if(imgStrin.equalsIgnoreCase("bn6")  ) {System.out.print("BUTTON PRESS: " + imgStrin);  bn6. setChecked(true); press=true; buttonpress=true; buttonpressAc=6   ; }
                if(imgStrin.equalsIgnoreCase("bn7")  ) {System.out.print("BUTTON PRESS: " + imgStrin);  bn7. setChecked(true); press=true; buttonpress=true; buttonpressAc=7   ; }
                if(imgStrin.equalsIgnoreCase("bn8")  ) {System.out.print("BUTTON PRESS: " + imgStrin);  bn8. setChecked(true); press=true; buttonpress=true; buttonpressAc=8   ; }
                if(imgStrin.equalsIgnoreCase("bn9")  ) {System.out.print("BUTTON PRESS: " + imgStrin);  bn9. setChecked(true); press=true; buttonpress=true; buttonpressAc=9   ; }
                if(imgStrin.equalsIgnoreCase("bn10") ) {System.out.print("BUTTON PRESS: " + imgStrin); bn10. setChecked(true); press=true; buttonpress=true; buttonpressAc=10  ; }
                if(imgStrin.equalsIgnoreCase("bn11") ) {System.out.print("BUTTON PRESS: " + imgStrin); bn11. setChecked(true); press=true; buttonpress=true; buttonpressAc=11  ; }
                if(imgStrin.equalsIgnoreCase("bn12") ) {System.out.print("BUTTON PRESS: " + imgStrin); bn12. setChecked(true); press=true; buttonpress=true; buttonpressAc=12  ; }
                if(imgStrin.equalsIgnoreCase("bn13") ) {System.out.print("BUTTON PRESS: " + imgStrin); bn13. setChecked(true); press=true; buttonpress=true; buttonpressAc=13  ; }
                if(imgStrin.equalsIgnoreCase("bn14") ) {System.out.print("BUTTON PRESS: " + imgStrin); bn14. setChecked(true); press=true; buttonpress=true; buttonpressAc=14  ; }
                if(imgStrin.equalsIgnoreCase("bn15") ) {System.out.print("BUTTON PRESS: " + imgStrin); bn15. setChecked(true); press=true; buttonpress=true; buttonpressAc=15  ; }
                if(imgStrin.equalsIgnoreCase("bn16") ) {System.out.print("BUTTON PRESS: " + imgStrin); bn16. setChecked(true); press=true; buttonpress=true; buttonpressAc=16  ; }
                if(imgStrin.equalsIgnoreCase("bn17") ) {System.out.print("BUTTON PRESS: " + imgStrin); bn17. setChecked(true); press=true; buttonpress=true; buttonpressAc=17  ; }
                if(imgStrin.equalsIgnoreCase("bn18") ) {System.out.print("BUTTON PRESS: " + imgStrin); bn18. setChecked(true); press=true; buttonpress=true; buttonpressAc=18  ; }
                if(imgStrin.equalsIgnoreCase("bn19") ) {System.out.print("BUTTON PRESS: " + imgStrin); bn19. setChecked(true); press=true; buttonpress=true; buttonpressAc=19  ; }
                if(imgStrin.equalsIgnoreCase("bn20") ) {System.out.print("BUTTON PRESS: " + imgStrin); bn20. setChecked(true); press=true; buttonpress=true; buttonpressAc=20  ; }
                if(imgStrin.equalsIgnoreCase("bn21") ) {System.out.print("BUTTON PRESS: " + imgStrin); bn21. setChecked(true); press=true; buttonpress=true; buttonpressAc=21  ; }
                if(imgStrin.equalsIgnoreCase("bn22") ) {System.out.print("BUTTON PRESS: " + imgStrin); bn22. setChecked(true); press=true; buttonpress=true; buttonpressAc=22  ; }
                if(imgStrin.equalsIgnoreCase("bn23") ) {System.out.print("BUTTON PRESS: " + imgStrin); bn23. setChecked(true); press=true; buttonpress=true; buttonpressAc=23  ; }
                if(imgStrin.equalsIgnoreCase("bn24") ) {System.out.print("BUTTON PRESS: " + imgStrin); bn24. setChecked(true); press=true; buttonpress=true; buttonpressAc=24  ; }
                if(imgStrin.equalsIgnoreCase("bn25") ) {System.out.print("BUTTON PRESS: " + imgStrin); bn25. setChecked(true); press=true; buttonpress=true; buttonpressAc=25  ; }
                if(imgStrin.equalsIgnoreCase("bn26") ) {System.out.print("BUTTON PRESS: " + imgStrin); bn26. setChecked(true); press=true; buttonpress=true; buttonpressAc=26  ; }
                if(imgStrin.equalsIgnoreCase("bn27") ) {System.out.print("BUTTON PRESS: " + imgStrin); bn27. setChecked(true); press=true; buttonpress=true; buttonpressAc=27  ; }

                System.out.print("BUTTON PRESS: " + imgStrin);

                m8.setVolume(0.3f);
                m8.play();



                return true;
            }
        };

    }

    public void clearAndCreateImagebnListeners(){

        n1bn. clearListeners();  n1bn. addListener(addListener1("n1bn"));
        n2bn. clearListeners();  n2bn. addListener(addListener1("n2bn"));
        n3bn. clearListeners();  n3bn. addListener(addListener1("n3bn"));
        n4bn. clearListeners();  n4bn. addListener(addListener1("n4bn"));
        n5bn. clearListeners();  n5bn. addListener(addListener1("n5bn"));
        n6bn. clearListeners();  n6bn. addListener(addListener1("n6bn"));
        n7bn. clearListeners();  n7bn. addListener(addListener1("n7bn"));
        n8bn. clearListeners();  n8bn. addListener(addListener1("n8bn"));
        n9bn. clearListeners();  n9bn. addListener(addListener1("n9bn"));
        n10bn.clearListeners();  n10bn.addListener(addListener1("n10bn"));
        n11bn.clearListeners();  n11bn.addListener(addListener1("n11bn"));
        n12bn.clearListeners();  n12bn.addListener(addListener1("n12bn"));
        n13bn.clearListeners();  n13bn.addListener(addListener1("n13bn"));
        n14bn.clearListeners();  n14bn.addListener(addListener1("n14bn"));
        n15bn.clearListeners();  n15bn.addListener(addListener1("n15bn"));
        n16bn.clearListeners();  n16bn.addListener(addListener1("n16bn"));
        n17bn.clearListeners();  n17bn.addListener(addListener1("n17bn"));
        n18bn.clearListeners();  n18bn.addListener(addListener1("n18bn"));
        n19bn.clearListeners();  n19bn.addListener(addListener1("n19bn"));
        n20bn.clearListeners();  n20bn.addListener(addListener1("n20bn"));
        n21bn.clearListeners();  n21bn.addListener(addListener1("n21bn"));
        n22bn.clearListeners();  n22bn.addListener(addListener1("n22bn"));
        n23bn.clearListeners();  n23bn.addListener(addListener1("n23bn"));
        n24bn.clearListeners();  n24bn.addListener(addListener1("n24bn"));
        n25bn.clearListeners();  n25bn.addListener(addListener1("n25bn"));
        n26bn.clearListeners();  n26bn.addListener(addListener1("n26bn"));
        n27bn.clearListeners();  n27bn.addListener(addListener1("n27bn"));


    }


    public void clearAndCreateImagebuttonListeners(){

        bn1.   clearListeners();    bn1. setChecked(false);     bn1.      addListener(addListener2("bn1"));
        bn2.   clearListeners();    bn2. setChecked(false);     bn2.      addListener(addListener2("bn2"));
        bn3.   clearListeners();    bn3. setChecked(false);     bn3.      addListener(addListener2("bn3"));
        bn4.   clearListeners();    bn4. setChecked(false);     bn4.      addListener(addListener2("bn4"));
        bn5.   clearListeners();    bn5. setChecked(false);     bn5.      addListener(addListener2("bn5"));
        bn6.   clearListeners();    bn6. setChecked(false);     bn6.      addListener(addListener2("bn6"));
        bn7.   clearListeners();    bn7. setChecked(false);     bn7.      addListener(addListener2("bn7"));
        bn8.   clearListeners();    bn8. setChecked(false);     bn8.      addListener(addListener2("bn8"));
        bn9.   clearListeners();    bn9. setChecked(false);     bn9.      addListener(addListener2("bn9"));
        bn10.   clearListeners();   bn10. setChecked(false);    bn10.      addListener(addListener2("bn10"));
        bn11.   clearListeners();   bn11. setChecked(false);    bn11.      addListener(addListener2("bn11"));
        bn12.   clearListeners();   bn12. setChecked(false);    bn12.      addListener(addListener2("bn12"));
        bn13.   clearListeners();   bn13. setChecked(false);    bn13.      addListener(addListener2("bn13"));
        bn14.   clearListeners();   bn14. setChecked(false);    bn14.      addListener(addListener2("bn14"));
        bn15.   clearListeners();   bn15. setChecked(false);    bn15.      addListener(addListener2("bn15"));
        bn16.   clearListeners();   bn16. setChecked(false);    bn16.      addListener(addListener2("bn16"));
        bn17.   clearListeners();   bn17. setChecked(false);    bn17.      addListener(addListener2("bn17"));
        bn18.   clearListeners();   bn18. setChecked(false);    bn18.      addListener(addListener2("bn18"));
        bn19.   clearListeners();   bn19. setChecked(false);    bn19.      addListener(addListener2("bn19"));
        bn20.   clearListeners();   bn20. setChecked(false);    bn20.      addListener(addListener2("bn20"));
        bn21.   clearListeners();   bn21. setChecked(false);    bn21.      addListener(addListener2("bn21"));
        bn22.   clearListeners();   bn22. setChecked(false);    bn22.      addListener(addListener2("bn22"));
        bn23.   clearListeners();   bn23. setChecked(false);    bn23.      addListener(addListener2("bn23"));
        bn24.   clearListeners();   bn24. setChecked(false);    bn24.      addListener(addListener2("bn24"));
        bn25.   clearListeners();   bn25. setChecked(false);    bn25.      addListener(addListener2("bn25"));
        bn26.   clearListeners();   bn26. setChecked(false);    bn26.      addListener(addListener2("bn26"));
        bn27.   clearListeners();   bn27. setChecked(false);    bn27.      addListener(addListener2("bn27"));


    }


    //RENDER
    @Override
    public void render(float delta) {
        if( delta == 0){
            return;
        }
        if(!initialRender){
            initialRender=true;
            tExit.     setVisible(true); tExit.    addAction(addCustomSequenceFadeIn());
            arrowUp.   setVisible(true); arrowUp.  addAction(addCustomSequenceFadeIn());
            arrowDown. setVisible(true); arrowDown.addAction(addCustomSequenceFadeIn());

            setButtons(0.0001f);
            if(Safe.createProcess){
                if(Safe.typScreen== Safe.ScreenType.CentralGameScreen){ _game.disposeAndCreateCentralGameScreen();
                }

            }
        }
        if(arrowDownBool){
            arrowDownCount+=delta;
            if(arrowDownCount>arrowDownCountLimit){


                arrowDownCount=0;
                arrowDownCountLimit=0;
                arrowDownBool=false;
                setFrontMap();

                System.out.println(" *********************  t_Map1.isVisible():" + t_Map1.isVisible()+ "   t_Map2.isVisible():"+t_Map2.isVisible()+"   t_Map3.isVisible():"+t_Map3.isVisible());

            }

        }else if(arrowUpBool){
            arrowUpCount+=delta;
            if(arrowUpCount>arrowUpCountLimit){


                arrowUpCount=0;
                arrowUpCountLimit=0;
                arrowUpBool=false;
                setFrontMap();

                System.out.println(" *********************  t_Map1.isVisible():" + t_Map1.isVisible()+ "   t_Map2.isVisible():"+t_Map2.isVisible()+"   t_Map3.isVisible():"+t_Map3.isVisible());

            }

        }else if(tExitBool){
            tExitCount+=delta;
            if(tExitCount>1.2){
                tExitBool=false;
                tExitCount=0;
                initialRender=false;

                t_Map1.      setPosition(0,0);
                t_Map2.      setPosition(0,0);
                t_Map3.      setPosition(0,0);



                tExit.       setPosition(1199,29);
                arrowUp.     setPosition(1213,790-368);
                arrowDown.   setPosition(1213,790-482);

                turtlebutton.setPosition(395,64);
                safykids.    setPosition(560,289);

                safykids.setVisible(false);

                turtlebutton.setOrigin(Align.center);
                safykids.setOrigin(Align.center);

                door_left.setPosition(0,0);
                door_right.setPosition(0,0);
                buttonpress=false;
                buttonpressAc=0;
                bnpress=false;
                safykids.setVisible(false);
                Safe.get_mainGameScreen().commandInt=2;
                Safe.getInstance().setScreen(Safe.getInstance().getScreenType(Safe.ScreenType.MainGame));
                Safe.get_mainGameScreen().command();

            }
        }else if (buttonpress){
            buttonpressfloat+=delta;
            if(buttonpressfloat>1.2){
               ac.Level= (int) buttonpressAc;
                tExitBool=false;
                tExitCount=0;
                initialRender=false;

                t_Map1.      setPosition(0,0);
                t_Map2.      setPosition(0,0);
                t_Map3.      setPosition(0,0);

                tExit.       setPosition(1199,29);
                arrowUp.     setPosition(1213,790-368);
                arrowDown.   setPosition(1213,790-482);

                turtlebutton.setPosition(395,64);
                safykids.    setPosition(560,289);


                turtlebutton.setOrigin(Align.center);
                safykids.setOrigin(Align.center);

                door_left.setPosition(0,0);
                door_right.setPosition(0,0);
                buttonpress=false;
                buttonpressAc=0;
                bnpress=false;
                safykids.setVisible(false);

                Safe.get_mainGameScreen().commandInt=2;
                Safe.getInstance().setScreen(Safe.getInstance().getScreenType(Safe.ScreenType.MainGame));
                Safe.get_mainGameScreen().command();

            }
        }


        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        _stage.act(delta);
        _stage.draw();


    }



    //RESIZE
    @Override
    public void resize(int width, int height) {
        _stage.getViewport().setScreenSize(width, height);
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
    public void pause() {}

    //RESUME
    @Override
    public void resume() {
        show();
        render(Gdx.graphics.getDeltaTime());
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