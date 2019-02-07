package com.mygdx.safe.UI;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created by alumne_practiques on 20/11/17.
 */

/*

public class hotBar extends InventorySlot{

    //TAG
    private static final String TAG = hotBar.class.getSimpleName();

    //ASPECTS
    private com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g;

    //LISTENER
    private String _itemListenerOwner;

    //SIZE
    private Vector2 hotbarSize;

    /*_______________________________________________________________________________________________________________

    //CONSTRUCTOR
    public hotBar(com.mygdx.safe.Entities.HUD_Entity he, Pixmap background, String item, com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g){
        super(he, -1, "ITEM", he.getHudActorDataComponent().POS_HOTBAR.x , he.getHudActorDataComponent().POS_HOTBAR.y);

        this.g = g;

        hotbarSize = new Vector2(he.getHudActorDataComponent().IMG_HOTBAR.getWidth() * 0.65f, he.getHudActorDataComponent().IMG_HOTBAR.getHeight() * 0.65f);

        this.set_customBackground(new Image(new Texture(background)));
        this.get_customBackground().setSize( hotbarSize.x, hotbarSize.y);

        this.set_BackgroundListenerOwner("HOTBAR_BACKGROUND");
        this._itemListenerOwner = "HOTBAR_ITEM";

        he.createHotBarListener(get_customBackground(), get_BackgroundListenerOwner());
        this.addActor(get_customBackground());
        this.get_customBackground().setVisible(false);

        if(item!=null)addSlotItem(he.getHudActorDataComponent().inventory.get_completeItemList().get(item));

        this.set_slotPosition(he.getHudActorDataComponent().POS_HOTBAR.x , he.getHudActorDataComponent().POS_HOTBAR.y );
    }

    //ACT
    public void act(float delta){
        get_customBackground().act(delta);
        if(hasItem()){
            get_slotItem().act(delta);
        }

        if(get_slotItem()!=null && get_slotItem().getActions().size==0) set_slotPosition(get_slotPosition().x, get_slotPosition().y);
    }

    //DRAW
    public void draw (Batch batch, float parentAlpha){

        if(get_customBackground().isVisible())get_customBackground().draw(batch,parentAlpha);
        if(hasItem()){
            get_slotItem().draw(batch,parentAlpha);
        }
    }

    /*_______________________________________________________________________________________________________________

    //ADD SLOT ITEM
    public void addSlotItem(InventoryItem slotItem) {

        if(doesAcceptItemUseType("ITEM")) {

            if(hasItem()) removeSlotItem();
            else getHe().get_hudGestureListeners().get(get_BackgroundListenerOwner()).set_enabled(false);

            set_slotItem(slotItem);
            get_slotItem().setSize(hotbarSize.x, hotbarSize.y);
            get_slotItem().setPosition(get_slotPosition().x, get_slotPosition().y);

            this.addActor(get_slotItem());
            getHe().createHotBarListener(slotItem, _itemListenerOwner);
            get_slotItem().set_listenerKey(_itemListenerOwner);
        }
    }

    //REMOVE SLOT ITEM
    public void removeSlotItem() {
        if(hasItem()){

            get_slotItem().removeListener(getHe().get_hudGestureListeners().get(_itemListenerOwner));
            getHe().get_hudGestureListeners().put(_itemListenerOwner, null);

            this.removeActor(get_slotItem(), false);

            if (getHe().getHudActorDataComponent().inventory.get_dragSlotSource() == null && !g.m.invUI.get_itemSlots().get(0).is_selected()){
                //_customBackground.setVisible(true);
                getHe().get_hudGestureListeners().get(get_BackgroundListenerOwner()).set_enabled(true);
                get_slotItem().set_listenerKey(null);
            }
            set_slotItem(null);
        }
    }

    //SET SLOT POSITION
    public void set_slotPosition(float x, float y) {
        this.get_slotPosition().x = x;
        this.get_slotPosition().y = y;

        get_customBackground().setPosition(x - get_customBackground().getWidth()/2, y - get_customBackground().getHeight()/2);

        if(hasItem()){
            get_slotItem().setPosition(x - get_slotItem().getWidth()/2 + 5.0f, y - get_slotItem().getHeight()/2 + 5.0f);
        }

        get_slotArea().setPosition(x , y );
    }

    /*_______________________________________________________________________________________________________________

    //GETTERS
    public String get_itemListenerOwner() {
        return _itemListenerOwner;
    }

    public Vector2 getHotbarSize() {
        return hotbarSize;
    }

    //SETTERS
    public void set_itemListenerOwner(String _itemListenerOwner) {
        this._itemListenerOwner = _itemListenerOwner;
    }

    public void setHotbarSize(Vector2 hotbarSize) {
        this.hotbarSize = hotbarSize;
    }
}*/
