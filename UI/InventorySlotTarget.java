package com.mygdx.safe.UI;

import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;

/**
 * Created by alumne_practiques on 07/07/17.
 */

public class InventorySlotTarget extends Target{

    //TAG
    private static final String TAG = InventorySlotTarget.class.getSimpleName();

    //ASPECTS
    private GenericMethodsInputProcessor g;

    //TARGET SLOT
    private com.mygdx.safe.UI.InventorySlot _targetSlot;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public com.mygdx.safe.UI.InventorySlot get_targetSlot() {
        return _targetSlot;
    }

    public InventorySlotTarget(com.mygdx.safe.UI.InventorySlot actor){
        super(actor);
        _targetSlot = actor;
    }

    //DRAG
    @Override
    public boolean drag(Source source, Payload payload, float x, float y, int pointer) {
        //resize slot background if the slot don't have any item.
        return true;
    }

    //RESET
    @Override
    public void reset(Source source, Payload payload) {
        //resize slot background if the drow has not ocurred
    }

    //DROP
    @Override
    public void drop(Source source, Payload payload, float x, float y, int pointer) {
        InventoryItem sourceActor = (InventoryItem) payload.getDragActor();
        InventoryItem targetActor = _targetSlot.get_slotItem();
        com.mygdx.safe.UI.InventorySlot sourceSlot = ((InventorySlotSource)source).getSourceSlot();

        if( sourceActor == null ) {
            return;
        }

        //First, does the slot accept the source item type?
        if( !_targetSlot.doesAcceptItemUseType(sourceActor.get_itemType()))  {
            //Put item back where it came from, slot doesn't accept item
            sourceSlot.addActor(sourceActor);
            return;
        }

        if( !_targetSlot.hasItem() ){
            _targetSlot.addActor(sourceActor);
            _targetSlot.addSlotItem(sourceActor);
        }else{
        }
    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS

    //SETTERS

    public void set_targetSlot(com.mygdx.safe.UI.InventorySlot _targetSlot) {
        this._targetSlot = _targetSlot;
    }



}