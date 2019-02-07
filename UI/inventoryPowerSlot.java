package com.mygdx.safe.UI;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;

/**
 * Created by alumne_practiques on 21/08/17.
 */
 /*
public class inventoryPowerSlot extends InventorySlot {

    //TAG
    private static final String TAG = inventoryPowerSlot.class.getSimpleName();

    //ASPECTS
    private GenericMethodsInputProcessor g;

    //BOOLEAN
    private boolean _isSelected;

    //IMAGE
    private Image _powerSelected;

    //LISTENER
    private String _powerSelectedListenerOwner;
    private String _powerListenerOwner;

    /*_______________________________________________________________________________________________________________

    //CONSTRUCTOR
    public inventoryPowerSlot(com.mygdx.safe.Entities.HUD_Entity he, int inventoryPosition, float x, float y, com.mygdx.safe.UI.InventoryItem item, Pixmap background, Pixmap itemSelected, boolean isSelected){

        super(he, inventoryPosition, "POWER", x, y);

        //Create the images using pixmaps, set the size and visibility
        this.set_customBackground(new Image(new Texture(background)));
        this.get_customBackground().setSize(95.0f,95.0f);
        this._powerSelected = new Image(new Texture(itemSelected));
        this._powerSelected.setSize(95.0f,95.0f);

        //Create the keys to find the listeneres in a Hashmap
        this.set_BackgroundListenerOwner("POWER_SLOT_"+ get_inventoryPosition() + "_BACKGROUND");
        this._powerSelectedListenerOwner = "POWER_SLOT_"+ get_inventoryPosition() + "_ITEMSELECTED";
        this._powerListenerOwner = "POWER_SLOT_"+ get_inventoryPosition() + "_ITEM";

        this._isSelected = isSelected;
        he.createItemActorGestureListener(get_customBackground(), get_BackgroundListenerOwner());
        he.createItemActorGestureListener(_powerSelected, _powerSelectedListenerOwner);

        if(item == null || (item !=null && !doesAcceptItemUseType(item.get_itemType()))){

            this.get_customBackground().setVisible(true);
            this._powerSelected.setVisible(false);

            he.get_hudGestureListeners().get(_powerSelectedListenerOwner).set_enabled(false);

            this.addActor(get_customBackground());

        }
        else{
            addSlotItem(item);

            if(_isSelected){
                this._powerSelected.setVisible(true);
                this.addActor(_powerSelected);
            }

            he.get_hudGestureListeners().get(get_BackgroundListenerOwner()).set_enabled(false);
        }

        set_slotPosition(get_slotPosition().x,get_slotPosition().y);
    }

    //ACT
    public void act(float delta){
        get_customBackground().act(delta);
        _powerSelected.act(delta);
        if(hasItem())get_slotItem().act(delta);

        get_slotPosition().x = get_customBackground().getX();
        get_slotPosition().y = get_customBackground().getY();

        set_slotPosition(get_slotPosition().x, get_slotPosition().y);
    }

    //DRAW
    public void draw (Batch batch, float parentAlpha){

        if(get_customBackground().isVisible())get_customBackground().draw(batch,parentAlpha);
        if(_powerSelected.isVisible())_powerSelected.draw(batch,parentAlpha);
        if(hasItem())get_slotItem().draw(batch,parentAlpha);
    }

    /*_______________________________________________________________________________________________________________

    //ADD SLOT ITEM
    public void addSlotItem(com.mygdx.safe.UI.InventoryItem slotItem) {

        if(doesAcceptItemUseType(slotItem.get_itemType())) {

            if(hasItem()) removeSlotItem();

            get_customBackground().setVisible(false);
            getHe().get_hudGestureListeners().get(get_BackgroundListenerOwner()).set_enabled(false);

            set_slotItem(slotItem);
            get_slotItem().setSize(90.0f,90.0f);
            get_slotItem().setPosition(get_slotPosition().x+2.5f, get_slotPosition().y+2.5f);

            this.addActor(get_slotItem());
            getHe().createItemActorGestureListener(get_slotItem(), _powerListenerOwner);
            get_slotItem().set_listenerKey(_powerListenerOwner);
        }
    }

    //REMOVE SLOT ITEM
    public void removeSlotItem() {
        if(hasItem()){

            this.removeActor(get_slotItem(), false);

            if(get_isSelected()){
                _powerSelected.setVisible(false);
                getHe().get_hudGestureListeners().get(_powerSelectedListenerOwner).set_enabled(false);
            }

            if(getHe().getHudActorDataComponent().inventory.get_dragSlotSource() == null){
                get_customBackground().setVisible(true);
                getHe().get_hudGestureListeners().get(get_BackgroundListenerOwner()).set_enabled(true);
                get_slotItem().set_listenerKey(null);
            }

            set_slotItem(null);
        }
    }

    //ADD ACTION ONLY TO IMAGES
    public void addActionOnlyToImages(Action action, Action action2){
        if(hasItem()){
            get_slotItem().addAction(action);
            if(get_isSelected())_powerSelected.addAction(action2);
        }
        else get_customBackground().addAction(action);
    }

    //ADD SLOT MOVE LIKE ITON
    public void addSlotMoveLikeIton(float x, float y, float duration){

        MoveByAction moveBackground = new MoveByAction();
        MoveByAction moveItemSelected = new MoveByAction();

        moveBackground.setAmount(x,y);
        moveItemSelected.setAmount(x,y);

        moveBackground.setDuration(duration);
        moveItemSelected.setDuration(duration);

        get_customBackground().addAction(moveBackground);
        _powerSelected.addAction(moveItemSelected);

        if(hasItem()){
            MoveByAction movePower = new MoveByAction();
            movePower.setAmount(x,y);
            movePower.setDuration(duration);
            get_slotItem().addAction(movePower);
        }
    }

    //PROGRAM ITON SLOT SHAKE
    public void programItonSlotShake (int pixelsMove, float duration, int numActions){

        boolean inverseMove =false;

        float initX ;
        float initY ;

        SequenceAction slotShake1 = new SequenceAction();
        SequenceAction slotShake2 = new SequenceAction();
        SequenceAction slotShake3 = new SequenceAction();

        for (int i=0; i<numActions; i += 1){
            MoveByAction move1 = new MoveByAction();
            MoveByAction move2 = new MoveByAction();
            MoveByAction move3 = new MoveByAction();

            initX = get_customBackground().getOriginX();
            initY = get_customBackground().getOriginY();
            if(inverseMove)move1.setAmount(initX, initY - pixelsMove);
            else move1.setAmount(initX, -initY + pixelsMove);
            move1.setDuration(duration/numActions);

            initX = _powerSelected.getOriginX();
            initY = _powerSelected.getOriginY();
            if(inverseMove) move2.setAmount(initX, initY - pixelsMove);
            else move2.setAmount(initX, -initY + pixelsMove);
            move2.setDuration(duration/numActions);

            if(hasItem()){
                initX = get_slotItem().getOriginX();
                initY = get_slotItem().getOriginY();
                if(inverseMove) move3.setAmount(initX, initY - pixelsMove);
                else move3.setAmount(initX, -initY + pixelsMove);
                move3.setDuration(duration/numActions);

                slotShake3.addAction(move3);
            }

            slotShake1.addAction(move1);
            slotShake2.addAction(move2);

            if(inverseMove)pixelsMove-= 2;
            inverseMove = !inverseMove;
        }

        get_customBackground().addAction(slotShake1);
        _powerSelected.addAction(slotShake2);
        if(hasItem())get_slotItem().addAction(slotShake3);
    }

    /*_______________________________________________________________________________________________________________

    //GETTERS

    public boolean get_isSelected() {
        return _isSelected;
    }

    public Image get_powerSelected() {
        return _powerSelected;
    }

    public String get_powerSelectedListenerOwner() {
        return _powerSelectedListenerOwner;
    }

    public String get_powerListenerOwner() {
        return _powerListenerOwner;
    }

    public boolean is_isSelected() {
        return _isSelected;
    }

    //SETTERS
    public void set_slotPosition(float x, float y) {
        this.get_slotPosition().x = x;
        this.get_slotPosition().y = y;

        get_customBackground().setPosition(x,y);
        _powerSelected.setPosition(x,y);

        if(hasItem()){
            if(get_slotItem().getWidth() < 95.0f)get_slotItem().setPosition(x+2.5f,y+2.5f);
            else get_slotItem().setPosition(x,y);
        }
        get_slotArea().setPosition(x + 22.5f ,y + 22.5f);
    }

    public void set_onlyImagesPosition(float x, float y){
        if(hasItem()){
            get_slotItem().setPosition(x,y);
            if(get_isSelected())_powerSelected.setPosition(x,y);
        }
        else get_customBackground().setPosition(x,y);
    }

    public void set_powerSelected(Image _powerSelected) {
        this._powerSelected = _powerSelected;
    }

    public void set_isSelected(boolean _isSelected) {
        this._isSelected = _isSelected;
    }

    public void set_powerSelectedListenerOwner(String _powerSelectedListenerOwner) {
        this._powerSelectedListenerOwner = _powerSelectedListenerOwner;
    }

    public void set_powerListenerOwner(String _powerListenerOwner) {
        this._powerListenerOwner = _powerListenerOwner;
    }
}
*/
