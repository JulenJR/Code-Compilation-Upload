package com.mygdx.safe.UI;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by alumne_practiques on 06/07/17.
 */

public class InventorySlot extends Group{

    //TAG
    private static final String TAG = InventorySlot.class.getSimpleName();

    //ASPECTS
    private com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g;

    //HUD ENTITY
    private com.mygdx.safe.Entities.HUD_Entity he;

    //IMAGES
    private Image _customBackground;
    private InventoryItem _slotItem = null;

    //POSITION
    private Vector2 _slotPosition;

    //LISTENER
    private String _BackgroundListenerOwner;

    //INVENTORY POSITION
    private int _inventoryPosition;

    //AREA
    private Rectangle _slotArea;

    //ACCEPTED TYPE
    private String _acceptedType;

    //OTHERS
    private float _dragItemOverlapTime = 0;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public InventorySlot(com.mygdx.safe.Entities.HUD_Entity he, int inventoryPosition, String acceptedType, float x, float y){

        this.he = he;
        this._inventoryPosition = inventoryPosition;
        this._acceptedType = acceptedType;

        this._slotPosition = new Vector2(x, y);
        this._slotArea = new Rectangle(x + 22.5f ,y + 22.5f, 50.0f, 50.0f);
    }

    //ADD ACTOR
    @Override
    public void addActor(Actor actor) {
        super.addActor(actor);
    }

    //ACT
    @Override
    public void act(float delta){}

    //DRAW
    @Override
    public void draw (Batch batch, float parentAlpha){}

    /*_______________________________________________________________________________________________________________*/

    //ADD SLOT ITEM
    public void addSlotItem(InventoryItem slotItem) {}

    //REMOVE SLOT ITEM
    public void removeSlotItem() {}

    //HAS ITEM
    public boolean hasItem(){
        return _slotItem != null;
    }

    //DOES ACCEPT ITEM USE TYPE
    public boolean doesAcceptItemUseType(String itemType){
        return itemType.contains(_acceptedType);
    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS
    public Image get_customBackground(){
        return _customBackground;
    }

    public InventoryItem get_slotItem() {
        return _slotItem;
    }

    public Vector2 get_slotPosition() {
        return _slotPosition;
    }

    public int get_inventoryPosition() {
        return _inventoryPosition;
    }

    public String get_acceptedType() {
        return _acceptedType;
    }

    public Rectangle get_slotArea() {
        return _slotArea;
    }

    public float get_dragItemOverlapTime() {
        return _dragItemOverlapTime;
    }

    public com.mygdx.safe.Entities.HUD_Entity getHe() {
        return he;
    }

    public String get_BackgroundListenerOwner() {
        return _BackgroundListenerOwner;
    }

    //SETTERS
    public void set_slotPosition(float x, float y) {
        _slotPosition.x = x;
        _slotPosition.y = y;

    }

    public void set_slotItem(InventoryItem _slotItem) {
        this._slotItem = _slotItem;
    }

    public void set_inventoryPosition(int _inventoryPosition) {
        this._inventoryPosition = _inventoryPosition;
    }

    public void set_slotArea(Rectangle _slotArea) {
        this._slotArea = _slotArea;
    }

    public void set_dragItemOverlapTime(float _dragItemOverlapTime) {
        this._dragItemOverlapTime = _dragItemOverlapTime;
    }

    public void set_customBackground(Image _customBackground) {
        this._customBackground = _customBackground;
    }

    public void set_slotPosition(Vector2 _slotPosition) {
        this._slotPosition = _slotPosition;
    }

    public void set_BackgroundListenerOwner(String _BackgroundListenerOwner) {
        this._BackgroundListenerOwner = _BackgroundListenerOwner;
    }

    public void set_acceptedType(String _acceptedType) {
        this._acceptedType = _acceptedType;
    }

    //TO STRING
    public String toString(){
        return String.valueOf(_inventoryPosition);
    }




}


