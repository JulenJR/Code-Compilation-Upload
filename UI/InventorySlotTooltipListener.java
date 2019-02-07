package com.mygdx.safe.UI;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;

public class InventorySlotTooltipListener extends InputListener {

    //TAG
    private static final String TAG = InventorySlotTooltipListener.class.getSimpleName();

    //ASPECTS
    private GenericMethodsInputProcessor g;

    //VECTOR2
    private Vector2 _currentCoords;
    private Vector2 _offset;

    //BOOLEAN
    private boolean _isInside = false;

    //INVENTORY SLOT TOOLTIP
    private com.mygdx.safe.UI.InventorySlotTooltip _toolTip;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public InventorySlotTooltipListener(com.mygdx.safe.UI.InventorySlotTooltip toolTip){
        this._toolTip = toolTip;
        this._currentCoords = new Vector2(0,0);
        this._offset = new Vector2(20, 10);
    }

    //MOUSE MOVED
    @Override
    public boolean mouseMoved(InputEvent event, float x, float y){
        InventorySlot inventorySlot = (InventorySlot)event.getListenerActor();
        if( _isInside ){
            _currentCoords.set(x, y);
            inventorySlot.localToStageCoordinates(_currentCoords);

            _toolTip.setPosition(_currentCoords.x+_offset.x, _currentCoords.y+_offset.y);
        }
        return false;
    }

    //TOUCH DRAGGED
    @Override
    public void touchDragged (InputEvent event, float x, float y, int pointer) {
        InventorySlot inventorySlot = (InventorySlot)event.getListenerActor();
        _toolTip.setVisible(inventorySlot, false);
    }

    //TOUCH DOWN
    @Override
    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
        return true;
    }

    //ENTER
    @Override
    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
        InventorySlot inventorySlot = (InventorySlot)event.getListenerActor();

        _isInside = true;

        _currentCoords.set(x, y);
        inventorySlot.localToStageCoordinates(_currentCoords);

        _toolTip.updateDescription(inventorySlot);
        _toolTip.setPosition(_currentCoords.x + _offset.x, _currentCoords.y + _offset.y);
        _toolTip.toFront();
        _toolTip.setVisible(inventorySlot, true);
    }

    //EXIT
    @Override
    public void exit(InputEvent event, float x, float y, int pointer, Actor toActor){
        InventorySlot inventorySlot = (InventorySlot)event.getListenerActor();
        _toolTip.setVisible(inventorySlot, false);
        _isInside = false;

        _currentCoords.set(x, y);
        inventorySlot.localToStageCoordinates(_currentCoords);
    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS

    public Vector2 get_currentCoords() {
        return _currentCoords;
    }

    public Vector2 get_offset() {
        return _offset;
    }

    public boolean is_isInside() {
        return _isInside;
    }

    public com.mygdx.safe.UI.InventorySlotTooltip get_toolTip() {
        return _toolTip;
    }

    //SETTERS

    public void set_currentCoords(Vector2 _currentCoords) {
        this._currentCoords = _currentCoords;
    }

    public void set_offset(Vector2 _offset) {
        this._offset = _offset;
    }

    public void set_isInside(boolean _isInside) {
        this._isInside = _isInside;
    }

    public void set_toolTip(com.mygdx.safe.UI.InventorySlotTooltip _toolTip) {
        this._toolTip = _toolTip;
    }
}
