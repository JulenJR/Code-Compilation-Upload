package com.mygdx.safe.UI;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;

/**
 * Created by alumne_practiques on 07/07/17.
 */

public class InventorySlotSource extends Source {

    //TAG
    private static final String TAG = InventorySlotSource.class.getSimpleName();

    //ASPECTS
    private GenericMethodsInputProcessor g;

    //DRAG & DROP
    private DragAndDrop _dragAndDrop;

    //SOURCE SLOT
    private InventorySlot _sourceSlot;

    //DRAGG ITEM
    private com.mygdx.safe.UI.InventoryItem _draggItem;

    //INITIAL SOURCE SLOT POSITION
    private Vector2 _initialSourceSlotPosition;

    //ITEM AREA
    private Rectangle _itemArea;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public InventorySlotSource(InventorySlot sourceSlot, com.mygdx.safe.UI.InventoryItem item){
        super(sourceSlot.get_slotItem());
        this._sourceSlot = sourceSlot;
        this._draggItem = item;

        _dragAndDrop = new DragAndDrop();

        this._initialSourceSlotPosition = new Vector2(_sourceSlot.get_slotPosition().x, _sourceSlot.get_slotPosition().y);
        this._itemArea = new Rectangle(_initialSourceSlotPosition.x + 22.5f, _initialSourceSlotPosition.y + 22.5f, 50.0f, 50.f);
    }

    //DRAG START
    @Override
    public DragAndDrop.Payload dragStart(InputEvent event, float x, float y, int pointer) {

        DragAndDrop.Payload payload = new DragAndDrop.Payload();

        //payload.setDragActor(_draggItem);

        _dragAndDrop.setDragActorPosition(x,y);
        _sourceSlot.removeSlotItem();

        return payload;
    }

    //DRAG STOP
    @Override
    public void dragStop (InputEvent event, float x, float y, int pointer, DragAndDrop.Payload payload, DragAndDrop.Target target) {

    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS

    public InventorySlot getSourceSlot() {
        return _sourceSlot;
    }

    public Rectangle get_itemArea() {
        return _itemArea;
    }

    public float getX(){
        return _draggItem.getX();
    }

    public float getY(){
        return _draggItem.getY();
    }

    public com.mygdx.safe.UI.InventoryItem get_draggItem() {
        return _draggItem;
    }

    public DragAndDrop get_dragAndDrop() {
        return _dragAndDrop;
    }

    public InventorySlot get_sourceSlot() {
        return _sourceSlot;
    }

    public Vector2 get_initialSourceSlotPosition() {
        return _initialSourceSlotPosition;
    }

    //SETTERS

    public void setPosition(float x, float y){
        _draggItem.setPosition(x,y);
        _itemArea.setPosition(x + 22.5f,y + 22.5f);
    }

    public void set_sourceSlot(InventorySlot _sourceSlot) {
        this._sourceSlot = _sourceSlot;
    }

    public void set_itemArea(Rectangle _itemArea) {
        this._itemArea = _itemArea;
    }

    public void set_draggItem(com.mygdx.safe.UI.InventoryItem _draggItem) {
        this._draggItem = _draggItem;
    }

    public void set_dragAndDrop(DragAndDrop _dragAndDrop) {
        this._dragAndDrop = _dragAndDrop;
    }

    public void set_initialSourceSlotPosition(Vector2 _initialSourceSlotPosition) {
        this._initialSourceSlotPosition = _initialSourceSlotPosition;
    }
}