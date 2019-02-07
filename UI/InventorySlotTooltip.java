package com.mygdx.safe.UI;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;

public class InventorySlotTooltip extends Window {

    //TAG
    private static final String TAG = InventorySlotTooltip.class.getSimpleName();

    //ASPECTS
    private GenericMethodsInputProcessor g;

    //SKIN
    private Skin _skin;

    //DESCRIPTION
    private Label _description;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public InventorySlotTooltip(final Skin skin){
        super("", skin);
        this._skin = skin;

        _description = new Label("", skin, "inventory-item-count");

        this.add(_description);
        this.padLeft(5).padRight(5);
        this.pack();
        this.setVisible(false);
    }

    //UPDATE DESCRIPTION
    public void updateDescription(InventorySlot inventorySlot){
        if( inventorySlot.hasItem() ){
            _description.setText(inventorySlot.get_slotItem().get_itemShortDescription());
            this.pack();
        }else{
            _description.setText("");
            this.pack();
        }
    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS

    public Skin get_skin() {
        return _skin;
    }

    public Label get_description() {
        return _description;
    }

    //SETTERS
    public void setVisible(InventorySlot inventorySlot, boolean visible) {
        super.setVisible(visible);

        if( inventorySlot == null ){
            return;
        }

        if (!inventorySlot.hasItem()) {
            super.setVisible(false);
        }
    }

    public void set_skin(Skin _skin) {
        this._skin = _skin;
    }

    public void set_description(Label _description) {
        this._description = _description;
    }
}