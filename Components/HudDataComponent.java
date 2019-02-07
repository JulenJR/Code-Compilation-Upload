package com.mygdx.safe.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.mygdx.safe.safe.safeT;

import java.util.HashMap;

/**
 *  REDUCTION COMMAND
 * for file in *.PNG; do convert $file -resize 20% $file; done
 * Created by BORIS.INSPIRATGAMES
 *
 */

public class HudDataComponent implements Component {

    //TAG
    private static final String TAG = HudDataComponent.class.getSimpleName();

    //ASPECTS
    com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g;

    //TRICEPTION
    public safeT safeT;

    //INVENTORY
    //public com.mygdx.safe.UI.InventoryUI inventory;

    //CAMERA
    Camera camera;

    //BOOLEANS
    public boolean triception_select = false;
    public boolean triception_change_state = false;
    public boolean triceptionAction = false;

    public boolean iton_select = false;// true if player select the "iton"
    public boolean itonActionMove = false;// true meanwhile iton is on movement
    public boolean itonActionDeploy = false;// true meanwhile iton habilities is on deployment
    public boolean itonActionShake = false;
    public boolean itonSlotOnFling = false;
    public boolean iton_change_state = false;
    public boolean powerSlots_change_state = false;
    public boolean iton_just_opened = false;
    public boolean GoBackToSourceAction = false;
    public boolean powerSwapAction = false;

    public boolean _comesFromDrag = false;
    public boolean _firstDrag = true;

    //ARRAYS
    public Array<Image> images;
    public Array<Vector2> positions;
    public Array<Vector2> placeHolderPositions;

    //HASHMAPS
    public HashMap<String, com.mygdx.safe.Pair<Integer,Image>> imageActonHash = new HashMap<String, com.mygdx.safe.Pair<Integer, Image>>();

    //CONFIG
    public HudConfig hc;


    //PATHS
    String ACTONCONFIGPATH;
    String PATH;
    //String PATH_ATON_BOTON_01;
    //String PATH_ATON_BOTON_02;
    //String PATH_ATON_BOTON_03;
    //String PATH_ATON_BOTON_04;
    //String PATH_ATON_BOTON_05;
    //String PATH_ATON_BOTON_06;
    //String PATH_ATON_BOTON_SALIDO;
    //String PATH_ATON_CONTRACHAPADO;
    //String PATH_ATON_ENGRANAJE;
    //String PATH_ATON_ENGRANAJE_PEQUEÑO;
    String PATH_HUD;
    //String PATH_ITON_POWERS_00;
    //String PATH_ITON_CERRADO_CAJON;
    //String PATH_ITON_CONTRACHAPADO;
    //String PATH_ITON_ITEMS;
    //String PATH_ITON_POWERS_01;
    //String PATH_OPTON_ENGRANAJE;
    //String PATH_TRICEPTION_CONTRACHAPADO;
    //String PATH_TRICEPTION_ENGRANAJE;
    //String PATH_TRICEPTION_ENGRANAJE_PEQUEÑO;
    //String PATH_TRICEPTION_SOLID;
    //String PATH_TRICEPTION_LIQUID;
    //String PATH_TRICEPTION_GAS;
    //String PATH_LIVEMOTION_01;
    //String PATH_LIVEMOTION_02;
    //String PATH_LIVEMOTION_03;
    //String PATH_TRICEPTION_COLORS;
    //String PATH_POWER_PLACEHOLDER;
    //String PATH_POWER_PLACEHOLDER_WHITE;
    ////String PATH_ITEM_PLACEHOLDER;
    //String PATH_ITON_SHADOW;
    //String PATH_ITEM_01;
    //String PATH_ITEM_02;
    //String PATH_ITEM_03;
    //String PATH_ITEM_04;
    //String PATH_ITEM_05;
    //String PATH_ITEM_06;
    //String PATH_TEXT_ACTOR_FONT; // TEXT ACTOR PATH (FNT)
    //String PATH_TEXT_ACTOR_GLYPH; // TEXT ACTOR GLYPH PATH (PNG)
    //String PATH_HOTBAR;
    String PATH_DIALOG_BACKGROUND;


    //PIXMAPS
    //public final Pixmap PIX_ATON_BOTON_01;
    //public final Pixmap PIX_ATON_BOTON_02;
    //public final Pixmap PIX_ATON_BOTON_03;
    //public final Pixmap PIX_ATON_BOTON_04;
    //public final Pixmap PIX_ATON_BOTON_05;
    //public final Pixmap PIX_ATON_BOTON_06;
    //public final Pixmap PIX_ATON_BOTON_SALIDO;
    //public final Pixmap PIX_ATON_CONTRACHAPADO;
    //public final Pixmap PIX_ATON_ENGRANAJE;
    //public final Pixmap PIX_ATON_ENGRANAJE_PEQUEÑO_1;
    //public final Pixmap PIX_ATON_ENGRANAJE_PEQUEÑO_2;
    public final Pixmap PIX_HUD;
    //public final Pixmap PIX_ITON_CERRADO_CAJON;
    //public final Pixmap PIX_ITON_POWERS_00;
    //public final Pixmap PIX_ITON_CONTRACHAPADO;
    //public final Pixmap PIX_ITON_ITEMS;
    //public final Pixmap PIX_ITON_POWERS_01;
    //public final Pixmap PIX_OPTON_ENGRANAJE;
    //public final Pixmap PIX_TRICEPTION_CONTRACHAPADO;
    //public final Pixmap PIX_TRICEPTION_ENGRANAJE;
    //public final Pixmap PIX_TRICEPTION_ENGRANAJE_PEQUEÑO;
    //public final Pixmap PIX_TRICEPTION_SOLID;
    //public final Pixmap PIX_TRICEPTION_LIQUID;
    //public final Pixmap PIX_TRICEPTION_GAS;
    //public final Pixmap PIX_LIVEMOTION_01;
    //public final Pixmap PIX_LIVEMOTION_02;
    //public final Pixmap PIX_LIVEMOTION_03;
    //public final Pixmap PIX_TRICEPTION_COLORS;
    //public final Pixmap PIX_POWER_PLACEHOLDER_01;
    //public final Pixmap PIX__POWER_PLACEHOLDER_WHITE;
    //public final Pixmap PIX_ITEM_PLACEHOLDER;
    //public final Pixmap PIX_ITON_SHADOW;
    //public final Pixmap PIX_ITEM_01;
    //public final Pixmap PIX_ITEM_02;
    //public final Pixmap PIX_ITEM_03;
    //public final Pixmap PIX_ITEM_04;
    //public final Pixmap PIX_ITEM_05;
    //public final Pixmap PIX_ITEM_06;
    //public final Pixmap PIX_HOTBAR;
    public final Pixmap PIX_DIALOG_BACKGROUND;


    //IMAGES
    //public Image IMG_ATON_BOTON_01;
    //public Image IMG_ATON_BOTON_02;
    //public Image IMG_ATON_BOTON_03;
    //public Image IMG_ATON_BOTON_04;
    //public Image IMG_ATON_BOTON_05;
    //public Image IMG_ATON_BOTON_06;
    //public Image IMG_ATON_BOTON_SALIDO;
    //public Image IMG_ATON_CONTRACHAPADO;
    //public Image IMG_ATON_ENGRANAJE;
    //public Image IMG_ATON_ENGRANAJE_PEQUEÑO_1;
    //public Image IMG_ATON_ENGRANAJE_PEQUEÑO_2;
    public Image IMG_HUD;
    //public Image IMG_ITON_CERRADO_CAJON;
    //public Image IMG_ITON_POWERS_00;
    //public Image IMG_ITON_CONTRACHAPADO;
    //public Image IMG_ITON_ITEMS;
    //public Image IMG_ITON_POWERS_01;
    //public Image IMG_OPTON_ENGRANAJE;
    //public Image IMG_TRICEPTION_CONTRACHAPADO;
    //public Image IMG_TRICEPTION_ENGRANAJE;
    //public Image IMG_TRICEPTION_ENGRANAJE_PEQUEÑO;
    //public Image IMG_TRICEPTION_SOLID;
    //public Image IMG_TRICEPTION_LIQUID;
    //public Image IMG_TRICEPTION_GAS;
    //public Image IMG_LIVEMOTION_01;
    //public Image IMG_LIVEMOTION_02;
    //public Image IMG_LIVEMOTION_03;
    //public Image IMG_TRICEPTION_COLORS;
    //public Image IMG_POWER_PLACEHOLDER;
    //public Image IMG_ITEM_PLACEHOLDER;
    //public Image IMG_ITON_SHADOW;
    //public Image IMG_ITEM;
    //public Image IMG_HOTBAR;
    public Image IMG_TALKING_ENTITY;
    public Image IMG_DIALOG_1;
    public Image IMG_DIALOG_2;
    public Image IMG_DIALOG_3;

    // SPECIAL SCALING COORDINATES
    //public Vector2 POS_ATON_BOTON_01;
    //public Vector2 POS_ATON_BOTON_02;
    //public Vector2 POS_ATON_BOTON_03;
    //public Vector2 POS_ATON_BOTON_04;
    //public Vector2 POS_ATON_BOTON_05;
    //public Vector2 POS_ATON_BOTON_06;
    //public Vector2 POS_ATON_BOTON_SALIDO;
    //public Vector2 POS_ATON_CONTRACHAPADO;
    //public Vector2 POS_ATON_ENGRANAJE;
    //public Vector2 POS_ATON_ENGRANAJE_PEQUEÑO_1;
    //public Vector2 POS_ATON_ENGRANAJE_PEQUEÑO_2;
    public Vector2 POS_HUD;
    //public Vector2 POS_ITON_CERRADO_CAJON;
    //public Vector2 POS_ITON_POWERS_00;
    //public Vector2 POS_ITON_CONTRACHAPADO;
    //public Vector2 POS_ITON_ITEMS;
    //public Vector2 POS_ITON_POWERS_01;
    //public Vector2 POS_OPTON_ENGRANAJE;
    //public Vector2 POS_TRICEPTION_CONTRACHAPADO;
    //public Vector2 POS_TRICEPTION_ENGRANAJE;
    //public Vector2 POS_TRICEPTION_ENGRANAJE_PEQUEÑO;
    //public Vector2 POS_TRICEPTION_IMAGE;
    //public Vector2 POS_LIVEMOTION_01;
    //public Vector2 POS_LIVEMOTION_02;
    //public Vector2 POS_LIVEMOTION_03;
    //public Vector2 POS_TRICEPTION_COLORS;
    //public Vector2 POS_TRICEPTION;
    //public Vector2 POS_POWER_PLACEHOLDER;
    //public Vector2 POS_ITEM_PLACEHOLDER;
    //public Vector2 POS_ITON_SHADOW;
    //public Vector2 POS_ITEM;
    //public Vector2 POS_HOTBAR;


    //OTHER POSITIONS
    //public float Y_ITON_POWERS_00_BOTTOM;
    //public float Y_ITON_POWERS_00_TOP;
    //public float X_ITON_POWERS_00_END;

    //SLOTS VALUES
    //public float _powerSlotsDistance;
    //public float _minXtoFirstSlot;
    //public float _maxXtoFirstSlot;

    //public Vector2 _itemSlotsDistance = new Vector2();

    //ACTION MOVE AMOUNTS
    //public float X_AMOUNT_PROGRAM_ITON_MOVE = 670.f;
    //public float Y_AMOUNT_PROGRAM_ITEMS_MOVE = 410.0f;

    //ACTION DURATIONS
    //public float ITON_MOVE_DURATION = 0.7f;
    //public float ITON_DEPLOY_DURATION = 0.6f;
    //public float ITON_SHAKE_DURATION = 0.5f;
    //public float ITON_SLOT_SCROLLMOVE_TOTALDURATION;
    //public float ITEM_GOBACKTOSLOT_DURATION = 0.2f;
    //public float SWAP_SLOTS_DURATION = 0.2f;
    //public float ITON_X_SLOT_VELOCITY;
    //public float ITON_Y_SLOT_VELOCITY;

    //RECTANGLE
    //public Rectangle RECTANGLE_ITON_POWERS_00;
    //public Rectangle RECTANGLE_ITON_ITEMS;

    /*_______________________________________________________________________________________________________________*/


    //CONSTRUCTOR
    public HudDataComponent(com.mygdx.safe.Entities.HUD_Entity he, Camera cam, com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g ) {

        this.g = g;
        camera = cam;

        //VECTOR2 DECLARATIONS
        //POS_ATON_BOTON_01 = new Vector2();
        //POS_ATON_BOTON_02 = new Vector2();
        //POS_ATON_BOTON_03 = new Vector2();
        //POS_ATON_BOTON_04 = new Vector2();
        //POS_ATON_BOTON_05 = new Vector2();
        //POS_ATON_BOTON_06 = new Vector2();
        //POS_ATON_BOTON_SALIDO = new Vector2();
        //POS_ATON_CONTRACHAPADO = new Vector2();
        //POS_ATON_ENGRANAJE = new Vector2();
        //POS_ATON_ENGRANAJE_PEQUEÑO_1 = new Vector2();
        //POS_ATON_ENGRANAJE_PEQUEÑO_2 = new Vector2();
        POS_HUD = new Vector2();
        //POS_ITON_CERRADO_CAJON = new Vector2();
        //POS_ITON_POWERS_00 = new Vector2();
        //POS_ITON_CONTRACHAPADO = new Vector2();
        //POS_ITON_ITEMS = new Vector2();
        //POS_ITON_POWERS_01 = new Vector2();
        //POS_OPTON_ENGRANAJE = new Vector2();
        //POS_TRICEPTION_CONTRACHAPADO = new Vector2();
        //POS_TRICEPTION_ENGRANAJE = new Vector2();
        //POS_TRICEPTION_ENGRANAJE_PEQUEÑO = new Vector2();
        //POS_TRICEPTION_IMAGE =new Vector2();
        //POS_LIVEMOTION_01 = new Vector2();
        //POS_LIVEMOTION_02 = new Vector2();
        //POS_LIVEMOTION_03 = new Vector2();
        //POS_TRICEPTION_COLORS = new Vector2();
        //POS_TRICEPTION = new Vector2();
        //POS_POWER_PLACEHOLDER = new Vector2();
        //POS_ITEM_PLACEHOLDER = new Vector2();
        //POS_ITON_SHADOW = new Vector2();
        //POS_ITEM = new Vector2();
        //POS_HOTBAR = new Vector2();


        // ORIGINAL CLASS PATHS
        ACTONCONFIGPATH="scripts/HUDACTONCONFIG.json";
        PATH = "hud/HUD_00/";
        //PATH_ATON_BOTON_01 = PATH + "ATON_BOTON_01.PNG";
        //PATH_ATON_BOTON_02 = PATH + "ATON_BOTON_02.PNG";
        //PATH_ATON_BOTON_03 = PATH + "ATON_BOTON_03.PNG";
        //PATH_ATON_BOTON_04 = PATH + "ATON_BOTON_04.PNG";
        //PATH_ATON_BOTON_05 = PATH + "ATON_BOTON_05.PNG";
        //PATH_ATON_BOTON_06 = PATH + "ATON_BOTON_06.PNG";
        //PATH_ATON_BOTON_SALIDO = PATH + "ATON_BOTON_SALIDO.PNG";
        //PATH_ATON_CONTRACHAPADO = PATH + "ATON_CONTRACHAPADO.PNG";
        //PATH_ATON_ENGRANAJE = PATH + "ATON_ENGRANAJE.PNG";
        //PATH_ATON_ENGRANAJE_PEQUEÑO = PATH + "ATON_ENGRANAJE_PEQUEÑO.PNG";
        PATH_HUD = PATH + "HUD.PNG";
        //PATH_ITON_POWERS_00 = PATH + "ITON_POWERS_00.PNG";
        //PATH_ITON_CERRADO_CAJON = PATH + "ITON_CERRADO_CAJON.PNG";
        //PATH_ITON_CONTRACHAPADO = PATH + "ITON_CONTRACHAPADO.PNG";
        //PATH_ITON_ITEMS = PATH + "ITON_ITEMS.PNG";
        //PATH_ITON_POWERS_01 = PATH + "ITON_POWERS_01.PNG";
        //PATH_OPTON_ENGRANAJE = PATH + "OPTON_ENGRANAJE.PNG";
        //PATH_TRICEPTION_CONTRACHAPADO = PATH + "TRICEPTION_CONTRACHAPADO.PNG";
        //PATH_TRICEPTION_ENGRANAJE = PATH + "TRICEPTION_ENGRANAJE.PNG";
        //PATH_TRICEPTION_ENGRANAJE_PEQUEÑO = PATH + "TRICEPTION_ENGRANAJE_PEQUEÑO.PNG";
        //PATH_TRICEPTION_SOLID = PATH + "TRICEPTION_SOLID.PNG";
        //PATH_TRICEPTION_LIQUID = PATH + "TRICEPTION_LIQUID.PNG";
        //PATH_TRICEPTION_GAS = PATH + "TRICEPTION_GAS.PNG";
        //PATH_LIVEMOTION_01 = PATH + "AURA.PNG";
        //PATH_LIVEMOTION_02 = PATH + "LIVEMOTION_02.PNG";
        //PATH_LIVEMOTION_03 = PATH + "LIVEMOTION_03.PNG";
        //PATH_TRICEPTION_COLORS = PATH + "BACK.PNG";
        //PATH_POWER_PLACEHOLDER = PATH + "POWER_PLACEHOLDER.PNG";
        //PATH_POWER_PLACEHOLDER_WHITE = PATH + "POWER_PLACEHOLDER_WHITE.PNG";
        //PATH_ITEM_PLACEHOLDER = PATH + "POWER_PLACEHOLDER.PNG";
        //PATH_ITON_SHADOW = PATH + "ITON_SHADOW.PNG";
        //PATH_ITEM_01 = PATH + "ITEM01.PNG";
        //PATH_ITEM_02 = PATH + "ITEM02.PNG";
        //PATH_ITEM_03 = PATH + "ITEM03.PNG";
        //PATH_ITEM_04 = PATH + "ITEM04.PNG";
        //PATH_ITEM_05 = PATH + "ITEM05.PNG";
        //PATH_ITEM_06 = PATH + "ITEM06.PNG";
        //PATH_HOTBAR = PATH + "HOTBAR.PNG";
        PATH_DIALOG_BACKGROUND = PATH + "DIALOG_BACKGROUND.PNG";


        // PIXMAPS
        //PIX_ATON_BOTON_01 = new Pixmap(Gdx.files.internal(PATH_ATON_BOTON_01));
        //PIX_ATON_BOTON_02 = new Pixmap(Gdx.files.internal(PATH_ATON_BOTON_02));
        //PIX_ATON_BOTON_03 = new Pixmap(Gdx.files.internal(PATH_ATON_BOTON_03));
        //PIX_ATON_BOTON_04 = new Pixmap(Gdx.files.internal(PATH_ATON_BOTON_04));
        //PIX_ATON_BOTON_05 = new Pixmap(Gdx.files.internal(PATH_ATON_BOTON_05));
        //PIX_ATON_BOTON_06 = new Pixmap(Gdx.files.internal(PATH_ATON_BOTON_06));
        //PIX_ATON_BOTON_SALIDO = new Pixmap(Gdx.files.internal(PATH_ATON_BOTON_SALIDO));
        //PIX_ATON_CONTRACHAPADO = new Pixmap(Gdx.files.internal(PATH_ATON_CONTRACHAPADO));
        //PIX_ATON_ENGRANAJE = new Pixmap(Gdx.files.internal(PATH_ATON_ENGRANAJE));
        //PIX_ATON_ENGRANAJE_PEQUEÑO_1 = new Pixmap(Gdx.files.internal(PATH_ATON_ENGRANAJE_PEQUEÑO));
        //PIX_ATON_ENGRANAJE_PEQUEÑO_2 = new Pixmap(Gdx.files.internal(PATH_ATON_ENGRANAJE_PEQUEÑO));
        PIX_HUD = new Pixmap(Gdx.files.internal(PATH_HUD));
        //PIX_ITON_CERRADO_CAJON = new Pixmap(Gdx.files.internal(PATH_ITON_CERRADO_CAJON));
        //PIX_ITON_POWERS_00 = new Pixmap(Gdx.files.internal(PATH_ITON_POWERS_00)); //By.JulenDani
        //PIX_ITON_CONTRACHAPADO = new Pixmap(Gdx.files.internal(PATH_ITON_CONTRACHAPADO));
        //PIX_ITON_ITEMS = new Pixmap(Gdx.files.internal(PATH_ITON_ITEMS));
        //PIX_ITON_POWERS_01 = new Pixmap(Gdx.files.internal(PATH_ITON_POWERS_01));
        //PIX_OPTON_ENGRANAJE = new Pixmap(Gdx.files.internal(PATH_OPTON_ENGRANAJE));
        //PIX_TRICEPTION_CONTRACHAPADO = new Pixmap(Gdx.files.internal(PATH_TRICEPTION_CONTRACHAPADO));
        //PIX_TRICEPTION_ENGRANAJE = new Pixmap(Gdx.files.internal(PATH_TRICEPTION_ENGRANAJE));
        //PIX_TRICEPTION_ENGRANAJE_PEQUEÑO = new Pixmap(Gdx.files.internal(PATH_TRICEPTION_ENGRANAJE_PEQUEÑO));
        //PIX_TRICEPTION_SOLID = new Pixmap(Gdx.files.internal(PATH_TRICEPTION_SOLID));
        //PIX_TRICEPTION_LIQUID = new Pixmap(Gdx.files.internal(PATH_TRICEPTION_LIQUID));
        //PIX_TRICEPTION_GAS = new Pixmap(Gdx.files.internal(PATH_TRICEPTION_GAS));
        //PIX_LIVEMOTION_01 = new Pixmap(Gdx.files.internal(PATH_LIVEMOTION_01));
        //PIX_LIVEMOTION_02 = new Pixmap(Gdx.files.internal(PATH_LIVEMOTION_02));
        //PIX_LIVEMOTION_03 = new Pixmap(Gdx.files.internal(PATH_LIVEMOTION_03));
        //PIX_TRICEPTION_COLORS = new Pixmap(Gdx.files.internal(PATH_TRICEPTION_COLORS));
        //PIX_POWER_PLACEHOLDER_01 = new Pixmap(Gdx.files.internal(PATH_POWER_PLACEHOLDER));
        //PIX__POWER_PLACEHOLDER_WHITE = new Pixmap(Gdx.files.internal(PATH_POWER_PLACEHOLDER_WHITE));
        //PIX_ITEM_PLACEHOLDER = new Pixmap(Gdx.files.internal(PATH_ITEM_PLACEHOLDER));
        //PIX_ITON_SHADOW = new Pixmap(Gdx.files.internal(PATH_ITON_SHADOW));
        //PIX_ITEM_01 = new Pixmap(Gdx.files.internal(PATH_ITEM_01));
        //PIX_ITEM_02 = new Pixmap(Gdx.files.internal(PATH_ITEM_02));
        //PIX_ITEM_03 = new Pixmap(Gdx.files.internal(PATH_ITEM_03));
        //PIX_ITEM_04 = new Pixmap(Gdx.files.internal(PATH_ITEM_04));
        //PIX_ITEM_05 = new Pixmap(Gdx.files.internal(PATH_ITEM_05));
        //PIX_ITEM_06 = new Pixmap(Gdx.files.internal(PATH_ITEM_06));
        //PIX_HOTBAR = new Pixmap(Gdx.files.internal(PATH_HOTBAR));
        PIX_DIALOG_BACKGROUND = new Pixmap(Gdx.files.internal(PATH_DIALOG_BACKGROUND));


        // IMAGES
        //IMG_ATON_BOTON_01 = new Image(new Texture(PIX_ATON_BOTON_01));
        //IMG_ATON_BOTON_02 = new Image(new Texture(PIX_ATON_BOTON_02));
        //IMG_ATON_BOTON_03 = new Image(new Texture(PIX_ATON_BOTON_03));
        //IMG_ATON_BOTON_04 = new Image(new Texture(PIX_ATON_BOTON_04));
        //IMG_ATON_BOTON_05 = new Image(new Texture(PIX_ATON_BOTON_05));
        //IMG_ATON_BOTON_06 = new Image(new Texture(PIX_ATON_BOTON_06));
        //IMG_ATON_BOTON_SALIDO = new Image(new Texture(PIX_ATON_BOTON_SALIDO));
        //IMG_ATON_CONTRACHAPADO = new Image(new Texture(PIX_ATON_CONTRACHAPADO));
        //IMG_ATON_ENGRANAJE = new Image(new Texture(PIX_ATON_ENGRANAJE));
        //IMG_ATON_ENGRANAJE_PEQUEÑO_1 = new Image(new Texture(PIX_ATON_ENGRANAJE_PEQUEÑO_1));
        //IMG_ATON_ENGRANAJE_PEQUEÑO_2 = new Image(new Texture(PIX_ATON_ENGRANAJE_PEQUEÑO_2));
        IMG_HUD = new Image(new Texture(PIX_HUD));
        //IMG_ITON_CERRADO_CAJON = new Image(new Texture(PIX_ITON_CERRADO_CAJON));
        //IMG_ITON_POWERS_00 = new Image(new Texture(PIX_ITON_POWERS_00));
        //IMG_ITON_CONTRACHAPADO = new Image(new Texture(PIX_ITON_CONTRACHAPADO));
        //IMG_ITON_ITEMS = new Image(new Texture(PIX_ITON_ITEMS));
        //IMG_ITON_POWERS_01 = new Image(new Texture(PIX_ITON_POWERS_01));
        //IMG_OPTON_ENGRANAJE = new Image(new Texture(PIX_OPTON_ENGRANAJE));
        //IMG_TRICEPTION_CONTRACHAPADO = new Image(new Texture(PIX_TRICEPTION_CONTRACHAPADO));
        //IMG_TRICEPTION_ENGRANAJE = new Image(new Texture(PIX_TRICEPTION_ENGRANAJE));
        //IMG_TRICEPTION_ENGRANAJE_PEQUEÑO = new Image(new Texture(PIX_TRICEPTION_ENGRANAJE_PEQUEÑO));
        //IMG_TRICEPTION_SOLID = new Image(new Texture(PIX_TRICEPTION_SOLID));
        //IMG_TRICEPTION_LIQUID =new Image(new Texture(PIX_TRICEPTION_LIQUID));
        //IMG_TRICEPTION_GAS = new Image(new Texture(PIX_TRICEPTION_GAS));
        //IMG_LIVEMOTION_01 = new Image(new Texture(PIX_LIVEMOTION_01));
        //IMG_LIVEMOTION_02 = new Image(new Texture(PIX_LIVEMOTION_02));
        //IMG_LIVEMOTION_03 = new Image(new Texture(PIX_LIVEMOTION_03));
        //IMG_TRICEPTION_COLORS = new Image(new Texture(PIX_TRICEPTION_COLORS));
        //IMG_POWER_PLACEHOLDER = new Image(new Texture(PIX_POWER_PLACEHOLDER_01));
        //IMG_ITEM_PLACEHOLDER = new Image(new Texture(PIX_ITEM_PLACEHOLDER));
        //IMG_ITON_SHADOW = new Image(new Texture(PIX_ITON_SHADOW));
        //IMG_ITEM = new Image(new Texture(PIX_ITEM_01));
        //IMG_HOTBAR = new Image(new Texture(PIX_HOTBAR));
        IMG_TALKING_ENTITY = new Image(new Texture(PIX_DIALOG_BACKGROUND));
        IMG_DIALOG_1 = new Image(new Texture(PIX_DIALOG_BACKGROUND));
        IMG_DIALOG_2 = new Image(new Texture(PIX_DIALOG_BACKGROUND));
        IMG_DIALOG_3 = new Image(new Texture(PIX_DIALOG_BACKGROUND));

        // IMAGES FOR ACTON:
        // HUDCONFIG (for acton)
        hc=GetHudConfig(ACTONCONFIGPATH);
        Array<com.mygdx.safe.Pair<String,Image>> actonImages=hc.generateDataImages();
        for(com.mygdx.safe.Pair<String,Image> p:actonImages){
            imageActonHash.put(p.getFirst(),new com.mygdx.safe.Pair<Integer, Image>(0,p.getSecond()));
        }

        //IMAGES SIZE
        //IMG_ITON_POWERS_00.setSize(885.0f, 130.0f);
        //IMG_ITON_POWERS_01.setSize(885.0f, 128.0f);
        //IMG_POWER_PLACEHOLDER.setSize(95.0f, 95.0f);
        //IMG_ITON_ITEMS.setSize(916.5f, 550.0f);
        //IMG_ITON_SHADOW.setSize(995.0f, 25.0f);
        //IMG_ITEM.setSize(95.0f, 95.0f);
        //IMG_HOTBAR.setSize(185.0f, 185.0f);
        //IMG_TRICEPTION_SOLID.setSize(IMG_LIVEMOTION_01.getWidth(), IMG_LIVEMOTION_01.getHeight());
        //IMG_TRICEPTION_LIQUID.setSize(IMG_LIVEMOTION_01.getWidth(), IMG_LIVEMOTION_01.getHeight());
        //IMG_TRICEPTION_GAS.setSize(IMG_LIVEMOTION_01.getWidth(), IMG_LIVEMOTION_01.getHeight());
        IMG_DIALOG_1.setSize(500.0f, 500.0f);

        //ADD IMAGES TO images ARRAY
        images = new Array<Image>();
        //images.add(IMG_ATON_BOTON_01);
        //images.add(IMG_ATON_BOTON_02);
        //images.add(IMG_ATON_BOTON_03);
        //images.add(IMG_ATON_BOTON_04);
        //images.add(IMG_ATON_BOTON_05);
        //images.add(IMG_ATON_BOTON_06);
        //images.add(IMG_ATON_BOTON_SALIDO);
        //images.add(IMG_ATON_CONTRACHAPADO);
        //images.add(IMG_ATON_ENGRANAJE);
        //images.add(IMG_ATON_ENGRANAJE_PEQUEÑO_1);
        //images.add(IMG_ATON_ENGRANAJE_PEQUEÑO_2);
        //images.add(IMG_HUD);
        //images.add(IMG_ITON_CERRADO_CAJON);
        //images.add(IMG_ITON_POWERS_00);
        //images.add(IMG_ITON_CONTRACHAPADO);
        //images.add(IMG_ITON_ITEMS);
        //images.add(IMG_ITON_POWERS_01);
        //images.add(IMG_OPTON_ENGRANAJE);
        //images.add(IMG_TRICEPTION_CONTRACHAPADO);
        //images.add(IMG_TRICEPTION_ENGRANAJE);
        //images.add(IMG_TRICEPTION_ENGRANAJE_PEQUEÑO);
        //images.add(IMG_TRICEPTION_SOLID);
        //images.add(IMG_TRICEPTION_LIQUID);
        //images.add(IMG_TRICEPTION_GAS);
        //images.add(IMG_ITON_SHADOW);
        //images.add(IMG_ITEM);
        //images.add(IMG_HOTBAR);
        images.add(IMG_TALKING_ENTITY);
        images.add(IMG_DIALOG_1);
        images.add(IMG_DIALOG_2);
        images.add(IMG_DIALOG_3);

        // ADD IMAGES FROM ACTOHASHMAP
        for(String s:imageActonHash.keySet()){
            images.add(imageActonHash.get(s).getSecond());
        }

        //CREATE AND ADD IMAGES TO TRICEPTION
        //safeT = new safeT(g);
        //safeT.addTriceptionImage("TRICEPTION_COLORS",IMG_TRICEPTION_COLORS);
        //safeT.addTriceptionImage("LIVEMOTION_01",IMG_LIVEMOTION_01);
        //safeT.addTriceptionStateImage("TRICEPTION_SOLID", IMG_TRICEPTION_SOLID);
        //safeT.addTriceptionStateImage("TRICEPTION_LIQUID", IMG_TRICEPTION_LIQUID);
        //safeT.addTriceptionStateImage("TRICEPTION_GAS", IMG_TRICEPTION_GAS);

        //SET POSITIONS
        Vector2 HUDSIZE = new Vector2(IMG_HUD.getWidth(), IMG_HUD.getHeight());

        //HUD
        POS_HUD.x = 0.0f;
        POS_HUD.y = 0.0f;

        //OPTON
        //POS_OPTON_ENGRANAJE.x = 34.0f;
        //POS_OPTON_ENGRANAJE.y = 30.0f;

        //ATON
        //POS_ATON_ENGRANAJE.x = 60.0f;
        //POS_ATON_ENGRANAJE.y = 331f;

        //POS_ATON_CONTRACHAPADO.x = POS_ATON_ENGRANAJE.x;
        //POS_ATON_CONTRACHAPADO.y = POS_ATON_ENGRANAJE.y;

        //POS_ATON_BOTON_SALIDO.x = POS_ATON_ENGRANAJE.x;
        //POS_ATON_BOTON_SALIDO.y = POS_ATON_ENGRANAJE.y;

        //POS_ATON_ENGRANAJE_PEQUEÑO_1.x = 20f;
        //POS_ATON_ENGRANAJE_PEQUEÑO_1.y = 286f;

        //POS_ATON_ENGRANAJE_PEQUEÑO_2.x = 107f;
        //POS_ATON_ENGRANAJE_PEQUEÑO_2.y = 374f;

        //ATON LEFT BUTTONS

        //POS_ATON_BOTON_06.x = 4f - IMG_ATON_BOTON_06.getWidth() / 2;
        //POS_ATON_BOTON_06.y = 134.0f;

        //POS_ATON_BOTON_02.x = 4f - IMG_ATON_BOTON_06.getWidth() / 2;
        //POS_ATON_BOTON_02.y = 212.0f;

        //ATON DOWN BUTTONS
        //POS_ATON_BOTON_04.x = 192.0f;
        //POS_ATON_BOTON_04.y = 390.0f + IMG_ATON_BOTON_04.getHeight() / 2;

        //POS_ATON_BOTON_03.x = 272.0f;
        //POS_ATON_BOTON_03.y = 390.0f + IMG_ATON_BOTON_03.getHeight() / 2;

        //POS_ATON_BOTON_01.x = 350.0f;
        //POS_ATON_BOTON_01.y = 390.0f + IMG_ATON_BOTON_01.getHeight() / 2;

        //POS_ATON_BOTON_05.x = 432.0f;
        //POS_ATON_BOTON_05.y = 390.0f + IMG_ATON_BOTON_05.getHeight() / 2;

        // TRICEPTION POSITIONS % (681x395 HUDSIZE BASED)
        //POS_TRICEPTION_ENGRANAJE_PEQUEÑO.x = 547.0f / 681.0f * HUDSIZE.x - IMG_TRICEPTION_ENGRANAJE_PEQUEÑO.getWidth() / 2;
        //POS_TRICEPTION_ENGRANAJE_PEQUEÑO.y = HUDSIZE.y - 382.0f / 395.0f * HUDSIZE.y - IMG_TRICEPTION_ENGRANAJE_PEQUEÑO.getHeight() / 2;

        //POS_TRICEPTION_CONTRACHAPADO.x = 610.0f / 681.0f * HUDSIZE.x - IMG_TRICEPTION_CONTRACHAPADO.getWidth() / 2;
        //POS_TRICEPTION_CONTRACHAPADO.y = HUDSIZE.y - 325.0f / 395.0f * HUDSIZE.y - IMG_TRICEPTION_CONTRACHAPADO.getHeight() / 2;


        //POS_TRICEPTION_ENGRANAJE.x = 612.0f / 681.0f * HUDSIZE.x - IMG_TRICEPTION_ENGRANAJE.getWidth() / 2;
        //POS_TRICEPTION_ENGRANAJE.y = HUDSIZE.y - 325.0f / 395.0f * HUDSIZE.y - IMG_TRICEPTION_ENGRANAJE.getHeight() / 2;

        //POS_TRICEPTION.x = 609.0f / 681.0f * HUDSIZE.x - IMG_TRICEPTION_COLORS.getWidth() / 2;
        //POS_TRICEPTION.y = HUDSIZE.y - 324.0f / 395.0f * HUDSIZE.y - IMG_TRICEPTION_COLORS.getHeight() / 2;

        //ITON POSITIONS
        //POS_ITON_CONTRACHAPADO.x = 648.0f;
        //POS_ITON_CONTRACHAPADO.y = IMG_HUD.getImageHeight() - 31.0f;

        //POS_ITON_POWERS_00.x = 2000.0f / 1362.0f * HUDSIZE.x - IMG_ITON_POWERS_00.getWidth();
        //POS_ITON_POWERS_00.y = HUDSIZE.y - 20.0f / 790.0f * HUDSIZE.y - IMG_ITON_POWERS_00.getHeight();

        //Y_ITON_POWERS_00_BOTTOM = POS_ITON_POWERS_00.y + ((38.0f / 790.0f) * HUDSIZE.y);
        //Y_ITON_POWERS_00_TOP = POS_ITON_POWERS_00.y + ((135.0f / 790.0f) * HUDSIZE.y);
        //X_ITON_POWERS_00_END = (1170.0f / 1362.0f) * IMG_HUD.getWidth();

        //POS_ITON_POWERS_01.x = 2000.0f / 1362.0f * HUDSIZE.x - IMG_ITON_POWERS_01.getWidth();
        //POS_ITON_POWERS_01.y = HUDSIZE.y - 20.0f / 790.0f * HUDSIZE.y - IMG_ITON_POWERS_01.getHeight();

        //POS_ITON_ITEMS.x = 1346.0f / 1362.0f * HUDSIZE.x - IMG_ITON_ITEMS.getWidth();
        //POS_ITON_ITEMS.y = HUDSIZE.y + 405.0f / 790.0f * HUDSIZE.y - IMG_ITON_ITEMS.getHeight();

        //POS_POWER_PLACEHOLDER.x = 1343.0f / 1362.0f * HUDSIZE.x - IMG_POWER_PLACEHOLDER.getWidth() / 2;
        //POS_POWER_PLACEHOLDER.y = HUDSIZE.y - 88.0f / 790.0f * HUDSIZE.y - IMG_POWER_PLACEHOLDER.getHeight() / 2;

        //POS_ITEM_PLACEHOLDER.x = 603.0f / 1362.0f * HUDSIZE.x - IMG_ITEM_PLACEHOLDER.getWidth() / 2;
        //POS_ITEM_PLACEHOLDER.y = HUDSIZE.y + 184.0f / 790.0f * HUDSIZE.y - IMG_ITEM_PLACEHOLDER.getHeight() / 2;

        //_powerSlotsDistance = (105.0f / 1362.0f) * IMG_HUD.getWidth();
        //_minXtoFirstSlot = (515.0f / 1362.0f) * IMG_HUD.getWidth();
        //_maxXtoFirstSlot = (625.0f / 1362.0f) * IMG_HUD.getWidth();

        //_itemSlotsDistance.x = (109.6f / 1362.0f) * IMG_HUD.getWidth();
        //_itemSlotsDistance.y = (189.7f / 1362.0f) * IMG_HUD.getHeight();

        //POS_ITEM.x = POS_POWER_PLACEHOLDER.x;
        //POS_ITEM.y = POS_POWER_PLACEHOLDER.y;

        //POS_ITON_SHADOW.x = 2060.0f / 1362.0f * HUDSIZE.x - IMG_ITON_SHADOW.getWidth();
        //POS_ITON_SHADOW.y = HUDSIZE.y - 148.0f / 790.0f * HUDSIZE.y - IMG_ITON_SHADOW.getHeight();

        //HOTBAR
        //POS_HOTBAR.x = (102.0f / 1362.0f) * HUDSIZE.x ;
        //POS_HOTBAR.y = (682.0f / 790.0f) * HUDSIZE.y ;

        //RECTANGLE ITON POWERS & ITEMS
        //RECTANGLE_ITON_POWERS_00 = new Rectangle(POS_ITON_POWERS_00.x - X_AMOUNT_PROGRAM_ITON_MOVE, POS_ITON_POWERS_00.y, IMG_ITON_POWERS_00.getWidth(), IMG_ITON_POWERS_00.getHeight());
        //RECTANGLE_ITON_ITEMS = new Rectangle(POS_ITON_ITEMS.x + (50.0f / 1362.0f * HUDSIZE.x), POS_ITON_POWERS_01.y - Y_AMOUNT_PROGRAM_ITEMS_MOVE,
                //IMG_ITON_ITEMS.getWidth() - (120.0f / 1362.0f * HUDSIZE.x), IMG_ITON_ITEMS.getHeight());

        //ADD POSITIONS TO positions ARRAY
        positions = new Array<Vector2>();
        //positions.add(POS_ATON_BOTON_01);
        //positions.add(POS_ATON_BOTON_02);
        //positions.add(POS_ATON_BOTON_03);
        //positions.add(POS_ATON_BOTON_04);
        //positions.add(POS_ATON_BOTON_05);
        //positions.add(POS_ATON_BOTON_06);
        //positions.add(POS_ATON_BOTON_SALIDO);
        //positions.add(POS_ATON_CONTRACHAPADO);
        //positions.add(POS_ATON_ENGRANAJE);
        //positions.add(POS_ATON_ENGRANAJE_PEQUEÑO_1);
        //positions.add(POS_ATON_ENGRANAJE_PEQUEÑO_2);
        //positions.add(POS_HUD);
        //positions.add(POS_ITON_CERRADO_CAJON);
        //positions.add(POS_ITON_POWERS_00);
        //positions.add(POS_ITON_CONTRACHAPADO);
        //positions.add(POS_LIVEMOTION_01);
        //positions.add(POS_LIVEMOTION_02);
        //positions.add(POS_LIVEMOTION_03);
        //positions.add(POS_ITON_POWERS_01);
        //positions.add(POS_OPTON_ENGRANAJE);
        //positions.add(POS_TRICEPTION_CONTRACHAPADO);
        //positions.add(POS_TRICEPTION_ENGRANAJE);
        //positions.add(POS_TRICEPTION_ENGRANAJE_PEQUEÑO);
        //positions.add(POS_ITON_ITEMS);
        //positions.add(POS_TRICEPTION_COLORS);
        //positions.add(POS_TRICEPTION);
        //positions.add(POS_POWER_PLACEHOLDER);
        // positions.add(POS_HOTBAR);

        //CREATE INVENTORY
        //inventory = new com.mygdx.safe.UI.InventoryUI(this, he, g);
        //g.m.setInvUI(inventory);
        }


    // STATIC HUDCONFIG DATA CONFIG LOAD:

    static public HudConfig GetHudConfig(String path){
        Json json = new Json();
        return json.fromJson(HudConfig.class, Gdx.files.internal(path));
    }

    public HudConfig getHudCondif(){
        return hc;
    }

}


/*

    public boolean hud_alpha_select =  false ;
    public boolean opton_select =  false ;
    public boolean aton_select =  false ;
    public boolean hud_select =  false ;

    public Vector3       triception_filled            =  new          Vector3(0f, 0f, 0f)               ;
    public Vector3       triception_opacity           =  new          Vector3(0f, 0f, 0f)               ;
    public Vector2       triception_scale             =  new          Vector2(1f,1f)                    ;
    public Vector2       engranaje_scale              =  new          Vector2(1f,1f)                    ;
    public Vector2       engranaje_pequeño_scale      =  new          Vector2(1f,1f)                    ;

    public boolean       inventory_display            =  false                                          ;
    public boolean       menu_display                 =  false                                          ;
    public boolean       dialog_display               =  false                                          ;
    public Vector2       last_Hud_inputXY             =  new          Vector2(0f, 0f)                   ;

    public String        dialog                       =                                       ""        ;

    public boolean       powerSlotOutOfZone           =  false                                          ;


        //public InventoryUI   inventory                    =  null                                           ;

*/