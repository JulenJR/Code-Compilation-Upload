package com.mygdx.safe.UI;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.safe.Conversation.TextActor;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;

/**
 * Created by alumne_practiques on 21/08/17.
 */
 /*
public class inventoryItemSlot extends InventorySlot {

    //TAG
    private static final String TAG = inventoryItemSlot.class.getSimpleName();

    //ASPECTS
    private GenericMethodsInputProcessor g;

    //LISTENER
    private String _itemListenerOwner;

    //BOOLEAN
    private boolean _selected = false;

    /*_______________________________________________________________________________________________________________

    //CONSTRUCTOR
    public inventoryItemSlot(com.mygdx.safe.Entities.HUD_Entity he, int inventoryPosition, float x, float y, Pixmap background, String item, GenericMethodsInputProcessor g){

        super(he, inventoryPosition, "ITEM", x, y);

        this.g = g;

        this.set_customBackground(new Image(new Texture(background)));
        this.get_customBackground().setSize(95.0f,95.0f);

        this.set_BackgroundListenerOwner("ITEM_SLOT#"+ get_inventoryPosition() + "#BACKGROUND");
        this._itemListenerOwner = "ITEM_SLOT#"+ get_inventoryPosition() + "#ITEM";

        he.createItemSlotActorGestureListener(get_customBackground(), get_BackgroundListenerOwner());
        this.addActor(get_customBackground());
        this.get_customBackground().setVisible(false);

        if (item != null) addSlotItem(he.getHudActorDataComponent().inventory.get_completeItemList().get(item));

        this.set_slotPosition(x,y);
    }

    //ACT
    public void act(float delta){
        get_customBackground().act(delta);
        if(hasItem()){

            if(_selected) get_slotItem().setSize(95.0f, 95.0f);
            else get_slotItem().setSize(90.0f, 90.0f);
            get_slotItem().act(delta);
        }

        set_slotPosition(get_customBackground().getX(), get_customBackground().getY());

        if(hasItem() && get_slotItem().getActions().size==0) set_slotPosition(get_slotPosition().x, get_slotPosition().y);
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
    public void addSlotItem(com.mygdx.safe.UI.InventoryItem slotItem) {

        if(doesAcceptItemUseType(slotItem.get_itemType())) {

            if(this.get_inventoryPosition() == 0) g.m.invUI.get_hotBar().addSlotItem(new com.mygdx.safe.UI.InventoryItem(slotItem, g));

            if(hasItem()) removeSlotItem();
            else getHe().get_hudGestureListeners().get(get_BackgroundListenerOwner()).set_enabled(false);

            set_slotItem(slotItem);
            get_slotItem().setSize(90.0f,90.0f);
            get_slotItem().setPosition(get_slotPosition().x+2.5f, get_slotPosition().y+2.5f);

            this.addActor(get_slotItem());
            getHe().createItemSlotActorGestureListener(slotItem, _itemListenerOwner);
            get_slotItem().set_listenerKey(_itemListenerOwner);

            //ANIMATION FOR NEW ITEMS FROM THE MAP
            if(!(g.m.he.getHudActorDataComponent().iton_change_state)) {
                if (g.m.lvlMgr.get_currentLvl().getSelectedGE() != null && g.m.lvlMgr.get_currentLvl().getSelectedGE().getID().contains(get_slotItem().get_itemID())) {
                    slotItem.setVisible(true);
                    Vector2 pos = TextActor.convertToRealCoordinates(g.m.lvlMgr.get_currentLvl().getSelectedGE().getposition(), g);
                    g.m.he.moveTranslate(get_slotItem(), pos, null, 0.8f);

                } else if (g.m.lvlMgr.get_currentLvl().getPreviousSelectedGE() != null) {
                    slotItem.setVisible(true);
                    Vector2 pos = TextActor.convertToRealCoordinates(g.m.lvlMgr.get_currentLvl().getPreviousSelectedGE().getposition(), g);
                    g.m.he.moveTranslate(get_slotItem(), pos, null, 0.8f);
                }
            }
        }
    }

    //REMOVE SLOT ITEM
    public void removeSlotItem() {
        if(hasItem()){

            if(this.get_inventoryPosition() == 0) g.m.invUI.get_hotBar().removeSlotItem();

            get_slotItem().removeListener(getHe().get_hudGestureListeners().get(_itemListenerOwner));
            getHe().get_hudGestureListeners().put(_itemListenerOwner, null);

            this.removeActor(get_slotItem(), false);

            if (getHe().getHudActorDataComponent().inventory.get_dragSlotSource() == null && !_selected){
                //_customBackground.setVisible(true);
                getHe().get_hudGestureListeners().get(get_BackgroundListenerOwner()).set_enabled(true);
                get_slotItem().set_listenerKey(null);
            }
            set_slotItem(null);
        }
    }

    //ADD SLOT MOVE LIKE ITON
    public void addSlotMoveLikeIton(float x, float y, float duration){

        MoveByAction moveBackground = new MoveByAction();
        moveBackground.setAmount(x,y);
        moveBackground.setDuration(duration);
        get_customBackground().addAction(moveBackground);

        if(hasItem()){
            MoveByAction moveItem = new MoveByAction();
            moveItem.setAmount(x,y);
            moveItem.setDuration(duration);
            get_slotItem().addAction(moveItem);
        }
    }

    //PROGRAM ITON SLOT SHAKE
    public void programItonSlotShake (int pixelsMove, float duration, int numActions){

        boolean inverseMove =false;

        float initX ;
        float initY ;

        SequenceAction slotShake1 = new SequenceAction();
        SequenceAction slotShake3 = new SequenceAction();

        for (int i=0; i<numActions; i += 1){
            MoveByAction move1 = new MoveByAction();
            MoveByAction move2 = new MoveByAction();

            initX = get_customBackground().getOriginX();
            initY = get_customBackground().getOriginY();
            if(inverseMove)move1.setAmount(initX, initY - pixelsMove);
            else move1.setAmount(initX, -initY + pixelsMove);
            move1.setDuration(duration/numActions);

            if(hasItem()){
                initX = get_slotItem().getOriginX();
                initY = get_slotItem().getOriginY();
                if(inverseMove) move2.setAmount(initX, initY - pixelsMove);
                else move2.setAmount(initX, -initY + pixelsMove);
                move2.setDuration(duration/numActions);

                slotShake3.addAction(move2);
            }

            slotShake1.addAction(move1);

            if(inverseMove)pixelsMove-= 2;
            inverseMove = !inverseMove;
        }

        get_customBackground().addAction(slotShake1);
        if(hasItem())get_slotItem().addAction(slotShake3);
    }

    /*_______________________________________________________________________________________________________________

    //GETTERS

    public static String getTAG() {
        return TAG;
    }

    public GenericMethodsInputProcessor getG() {return g;}

    public String get_itemListenerOwner() {
        return _itemListenerOwner;
    }

    public boolean is_selected() {
        return _selected;
    }

    //SETTERS

    public void set_slotPosition(float x, float y) {

        this.get_slotPosition().x = x;
        this.get_slotPosition().y = y;

        get_customBackground().setPosition(x,y);

        if(hasItem()){
            if(get_slotItem().getWidth() < 95.0f)get_slotItem().setPosition(x+2.5f,y+2.5f);
            else get_slotItem().setPosition(x,y);
        }
        get_slotArea().setPosition(x + 22.5f, y + 22.5f);
    }

    public void set_itemListenerOwner(String _itemListenerOwner) {
        this._itemListenerOwner = _itemListenerOwner;
    }

    public void set_selected(boolean _selected) {
        this._selected = _selected;
    }

    public void setG(GenericMethodsInputProcessor g) {this.g = g;}
}

*/
