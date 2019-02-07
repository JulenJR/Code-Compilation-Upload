package com.mygdx.safe.UI;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Array;
import com.mygdx.safe.Components.HudDataComponent;

import java.util.HashMap;

/**
 * Created by alumne_practiques on 10/07/17.
 */
/*
public class InventoryUI {

    //TAG
    private static final String TAG = InventoryUI.class.getSimpleName();

    //ASPECTS
    private com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g;

    //INVENTORY
    private Array<String> _inventory = new Array<String>();
    private HashMap<String, InventoryItem> _completeItemList = new HashMap<String, InventoryItem>();
    private hotBar _hotBar;
    private Wallet _wallet;

    //HUD
    private com.mygdx.safe.Entities.HUD_Entity he;
    private HudDataComponent h;

    //SOURCE & TARGET
    private InventorySlotSource _dragSlotSource;
    private InventorySlotTarget _dragSlotTarget;

    //HASHMAPS
    private HashMap<Integer, inventoryPowerSlot> _powerSlots;
    private HashMap<Integer, inventoryItemSlot> _itemSlots;
    private HashMap <Integer, Float>_dragInitialsX = new HashMap<Integer, Float>();

    //PHYSICS VALUES
    private float _previousX;
    private float _previousY;
    private float _xVariation=0;
    private float _yVariation=0;
    private float _firstSlotX;
    private float _oldFirstSlotX;
    private float _oldDragItemX;
    private float _oldDragItemY;
    private int _firstSlotIndex = 0;

    //BOOLEANS
    private boolean _comesFromUpdate = false;
    private boolean _notMove = false;

    //CURRENT TREE & NODE
    private String TreeID;
    private int TreeNumNode;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    /*
    public InventoryUI(HudDataComponent h, com.mygdx.safe.Entities.HUD_Entity he, com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g) {

        this.g = g;
        this.h = h;
        this.he = he;

        _powerSlots = new HashMap<Integer, inventoryPowerSlot>();
        _itemSlots = new HashMap<Integer, inventoryItemSlot>();
    }

    //CONFIG
    public void config(){

        _hotBar = new hotBar(he,h.PIX_ITEM_PLACEHOLDER, null, g);

        //CREATE INITIAL ARRAY OF POWERS SLOTS & INITIAL ARRAY OF ITEMS
        for (int i = 0; i < 6; i++) {
            inventoryPowerSlot powerSlot = new inventoryPowerSlot(he, i, h.POS_POWER_PLACEHOLDER.x + (h._powerSlotsDistance * i), h.POS_POWER_PLACEHOLDER.y,
                    null, h.PIX_POWER_PLACEHOLDER_01, h.PIX__POWER_PLACEHOLDER_WHITE, false);

            _powerSlots.put(i, powerSlot);
        }

        for (int i = 0; i < 3; i++){
            for ( int j = 0; j < 6; j++ ){
                inventoryItemSlot itemSlot = new inventoryItemSlot(he, i*6+j, h.POS_ITEM_PLACEHOLDER.x + (h._itemSlotsDistance.x * j ),
                        h.POS_ITEM_PLACEHOLDER.y - (h._itemSlotsDistance.y * i ),h.PIX_ITEM_PLACEHOLDER, null, g);

                _itemSlots.put(i*6+j, itemSlot);

            }
        }

        for(String s: _completeItemList.keySet()){
            if(_completeItemList.get(s).get_itemQuantity() > 0 )add(s, _completeItemList.get(s).get_itemQuantity());
        }
        _wallet = new Wallet(_completeItemList.get("ITEM_COIN"), g);
    }

    //UPDATE SLOTS
    //This method is called by HUD_System one time each frame, before stage.act() and stage.draw(). When the _xVariation is bigger or smaller enough, the method manage and set
    //the positions of the slots, sets the _oldFirstSlotX, the _comesFromUpdate boolean and the _xVariation to 0. If not, only sets _notMove to true.
    public void updateSlots(float delta) {

        if(_dragSlotSource !=null && !h.GoBackToSourceAction && !h.powerSwapAction){

            InventoryItem item = _dragSlotSource.get_draggItem();

            if(item.get_itemType().contains("POWER")) updatePowerDrag(delta, item);
            else if (item.get_itemType().contains("ITEM")) updateItemDrag(delta, item);
        }
        else if(h.iton_change_state && !h.GoBackToSourceAction && !h.powerSwapAction && !h.itonActionMove && !h.itonActionShake && !h.itonActionDeploy){
            updatePowerSlotsMovement(delta);
        }

        _xVariation = 0;
        _yVariation = 0;

    }

    //UPDATE POWER SLOTS MOVEMENT
    public void updatePowerSlotsMovement(float delta){

        if(h.itonSlotOnFling){
            set_xVariationWithFling(delta);
        }

        if(_xVariation >0.5f || _xVariation < -0.5f) {
            manageFirstSlotX();

            _oldFirstSlotX = _powerSlots.get(_firstSlotIndex).get_slotPosition().x;

            updateSlotPositions();
            _comesFromUpdate = true;
        }
        else{
            _notMove = true;
        }
    }

    //UPDATE POWER DRAG
    public void updatePowerDrag(float delta, InventoryItem item){

        Rectangle dragArea = _dragSlotSource.get_itemArea();

        checkPowerOverlaps(dragArea, delta);

        if((_xVariation>0.5f || _xVariation < -0.5f) && item.get_itemType().contains("POWER")){

            float nextPosition = item.getX() + _xVariation;
            _oldDragItemX = item.getX();

            if ((nextPosition > h._minXtoFirstSlot+item.getWidth()) && (nextPosition < h.X_ITON_POWERS_00_END - item.getWidth())){
                _dragSlotSource.setPosition(nextPosition, item.getY());
            }
            else if(nextPosition < h._minXtoFirstSlot + item.getWidth()){
                _xVariation = h._minXtoFirstSlot - item.getX(); //?????
                _dragSlotSource.setPosition(h._minXtoFirstSlot + item.getWidth(), item.getY());
            }
            else if(nextPosition > h.X_ITON_POWERS_00_END -item.getWidth()){
                _xVariation = h.X_ITON_POWERS_00_END - item.getX();
                _dragSlotSource.setPosition(h.X_ITON_POWERS_00_END - item.getWidth(), item.getY());
            }

            _comesFromUpdate = true;
        }
        else _notMove = true;
    }

    //UPDATE SLOT POSITIONS
    public void updateSlotPositions() {

        int currentIndex;

        for (int i = 0; i < _powerSlots.size(); i++) {
            currentIndex = (i + _firstSlotIndex) % _powerSlots.size();
            _powerSlots.get(currentIndex).set_slotPosition(_powerSlots.get(currentIndex).get_slotPosition().x + _xVariation, _powerSlots.get(currentIndex).get_slotPosition().y);
        }
    }

    //UPDATE ITEM DRAG
    public void updateItemDrag (float delta, InventoryItem item){

        Rectangle dragArea = _dragSlotSource.get_itemArea();

        checkItemOverlaps(dragArea, delta);

        if((_xVariation>0.5f || _xVariation < -0.5f || _yVariation>0.5f || _yVariation < -0.5f) && item.get_itemType().contains("ITEM")){

            Vector2 nextPosition = new Vector2();

            nextPosition.x = item.getX() + _xVariation;
            nextPosition.y = item.getY() + _yVariation;
            _oldDragItemX = item.getX();
            _oldDragItemY = item.getY();

            _dragSlotSource.setPosition(nextPosition.x, nextPosition.y);

            _comesFromUpdate = true;

        }
        else _notMove = true;
    }

    /*_______________________________________________________________________________________________________________*/

    //RECEIVE
    /*
    public void receive(String[]  message, String treeID, int treeNumNode){

        if(message[0].equalsIgnoreCase("REMOVE")){
            if(message[1].equalsIgnoreCase("ITEM")){

                if(message[3].equalsIgnoreCase("ALL")){
                    remove(message[2], _completeItemList.get(message[2]).get_itemQuantity());
                    g.gm.sendMessage(g.messageOK(treeID, treeNumNode),treeID, treeNumNode);

                }else{
                    remove(message[2], Integer.valueOf(message[3]));
                    g.gm.sendMessage(g.messageOK(treeID, treeNumNode),treeID, treeNumNode);
                }
            }
        }
        else if(message[0].equalsIgnoreCase("ADD") && message.length > 3){
            if(message[1].equalsIgnoreCase("ITEM")){
                add(message[2], Integer.valueOf(message[3]));
                g.gm.sendMessage(g.messageOK(treeID, treeNumNode),treeID, treeNumNode);
            }
        }
        else if(message[0].equalsIgnoreCase("BUY") && message.length > 3){
            if(message[1].equalsIgnoreCase("ITEM")){
                buy(message[2], Integer.valueOf(message[3]));
                g.gm.sendMessage(g.messageOK(treeID, treeNumNode),treeID, treeNumNode);
            }
        }
        /*else if(message[0].equalsIgnoreCase("SWAP")){
            if(message[1].equalsIgnoreCase("ITEM")){
                if(_dragSlotSource == null) swapItemSlots(_itemSlots.get(Integer.valueOf(message[2])), _itemSlots.get(Integer.valueOf(message[3])));
                else swapItemSlots(_dragSlotSource, _itemSlots.get(Integer.valueOf(message[2])));
            }

        }*/
    //}

    /*_______________________________________________________________________________________________________________*/

    //ADD
    /*
    public boolean add(String itemName, int quantity){

        int invPos = _completeItemList.get(itemName).get_inventoryPosition();
        InventoryItem item = _completeItemList.get(itemName);

        if(invPos == -1){

            for(int i=0; i<_itemSlots.size(); i++){

                if(!_itemSlots.get(i).hasItem()){
                    _itemSlots.get(i).addSlotItem(item);
                    item.set_inventoryPosition(i);
                    item.set_itemQuantity(quantity);

                    return true;
                }
            }
            return true;
        }
        else if(invPos > -1 && invPos < _itemSlots.size()){
            _completeItemList.get(itemName).set_itemQuantity(_completeItemList.get(itemName).get_itemQuantity() + quantity);
        }
        return false;
    }

    //REMOVE
    public boolean remove(String itemName, int quantity){

        if(quantity == 0 || quantity < 0) return false;

        InventoryItem item = _completeItemList.get(itemName);
        int invPos = _completeItemList.get(itemName).get_inventoryPosition();

        if(invPos > -1 && invPos < _itemSlots.size()){

            if(item.get_itemQuantity() < quantity) return false;
            else if(item.get_itemQuantity() - quantity == 0){
                item.set_itemQuantity(0);
                _itemSlots.get(invPos).removeSlotItem();
                item.set_inventoryPosition(-1);
            }
            else item.set_itemQuantity(item.get_itemQuantity() - quantity);
        }
        return true;
    }

    //BUY
    private void buy(String item, int quantity){

        int price = quantity * _completeItemList.get(item).get_itemValue();

        if(price > _wallet.get_money().get_itemQuantity()) g.println(TAG+" ERROR: NOT ENOUGH MONEY");
        else {
            _wallet.remove(price);
            add(item, quantity);
        }
    }

    //MANAGE OF VARIATIONS
    public void managePowerPan_Variation(float currentX, boolean firstDrag, String type) {

        float x;

        //If the listenerOwner is not from POWER_SLOT type the variable _comesFromUpdate allways have to be false
        if (!type.contains("POWER_SLOT")) {
            _comesFromUpdate = false;
        }

        if (!firstDrag && !_comesFromUpdate){
            _xVariation += (currentX - _previousX);
        }
        else if (_comesFromUpdate && !_notMove) {

            if(_dragSlotSource == null) x = currentX + (_powerSlots.get(_firstSlotIndex).get_slotPosition().x - _oldFirstSlotX);
            else x = currentX + (_dragSlotSource.get_draggItem().getX() - _oldDragItemX);

            _xVariation = (x - _previousX);
        }

        _previousX = currentX; //The currentX becomes _previousX at the end of the method

        //The booleans are seted to its default value
        _comesFromUpdate = false;
        _notMove = false;
    }

    public void manageItemPan_Variation(float currentX, float currentY, boolean firstDrag, String type) {

        float x;
        float y;

        //If the listenerOwner is not from POWER_SLOT type the variable _comesFromUpdate allways have to be false
        if (!type.contains("ITEM_SLOT")) {
            _comesFromUpdate = false;
        }

        if (!firstDrag && !_comesFromUpdate){
            _xVariation += (currentX - _previousX);
            _yVariation += (currentY - _previousY);
        }
        else if(_comesFromUpdate && !_notMove){

            x = currentX + (_dragSlotSource.get_draggItem().getX() - _oldDragItemX);
            y = currentY + (_dragSlotSource.get_draggItem().getY() - _oldDragItemY);

            _xVariation = (x - _previousX);
            _yVariation = (y - _previousY);
        }

        _previousX = currentX; //The currentX becomes _previousX at the end of the method
        _previousY = currentY;

        //The booleans are seted to its default value
        _comesFromUpdate = false;
        _notMove = false;
    }

    //MANAGE FIRST SLOT X
    public void manageFirstSlotX() {

        float x = _powerSlots.get(_firstSlotIndex).get_slotPosition().x + _xVariation;

        _firstSlotX = x;

        if (_firstSlotX < h._minXtoFirstSlot) {

            _powerSlots.get(_firstSlotIndex).get_slotPosition().x += (((h._powerSlotsDistance*_powerSlots.size())/ 1362.0f) * h.IMG_HUD.getWidth());

            _firstSlotIndex = (_firstSlotIndex + 1) % _powerSlots.size();
            _firstSlotX += h._powerSlotsDistance;

            manageFirstSlotX();

        } else if (_firstSlotX > h._maxXtoFirstSlot) {

            if (_firstSlotIndex == 0) _firstSlotIndex = _powerSlots.size()-1;
            else _firstSlotIndex = (_firstSlotIndex - 1);

            _powerSlots.get(_firstSlotIndex).get_slotPosition().x += (((-h._powerSlotsDistance*_powerSlots.size())/ 1362.0f) * h.IMG_HUD.getWidth());

            manageFirstSlotX();
        }
    }

    //CHECK POWER OVERLAPS
    public void checkPowerOverlaps(Rectangle dragArea, float delta){

        for(int i = 0; i<_powerSlots.size(); i++){

            inventoryPowerSlot current = _powerSlots.get(i);

            if(dragArea.overlaps(current.get_slotArea())){

                current.set_dragItemOverlapTime(current.get_dragItemOverlapTime() + delta);
            }
            else if(current.get_dragItemOverlapTime() != 0){
                current.set_dragItemOverlapTime(0);
            }
        }
    }

    //CHECK ITEM OVERLAPS
    public void checkItemOverlaps(Rectangle dragArea, float delta){

        for(int i = 0; i<_itemSlots.size(); i++){
            inventoryItemSlot current = _itemSlots.get(i);

            if(Intersector.intersectRectangles(dragArea, current.get_slotArea(), new Rectangle())) current.set_dragItemOverlapTime(current.get_dragItemOverlapTime() + delta);
            else if(current.get_dragItemOverlapTime() != 0)current.set_dragItemOverlapTime(0);
        }

        //When the item is dragged out of the iton. Close iton
        if( !dragArea.overlaps(h.RECTANGLE_ITON_POWERS_00) && !dragArea.overlaps(h.RECTANGLE_ITON_ITEMS) &&
                h.iton_change_state && !h.itonActionDeploy && !h.itonActionShake && !h.itonActionMove){
            he.programItonDeploy(0.6f);
        }
    }

    //SWAP POWER SLOTS
    public void swapPowerSlots(InventorySlotSource dragSource, InventorySlot slotTarget){

        inventoryPowerSlot slotSource = (inventoryPowerSlot)dragSource.getSourceSlot();
        int index = slotSource.get_inventoryPosition();
        boolean toTheLeft;

        if(slotSource.get_inventoryPosition() == slotTarget.get_inventoryPosition()){
            power_goBackToSource(slotSource);
            return;
        }

        if(slotSource.get_slotPosition().x < slotTarget.get_slotPosition().x)toTheLeft = true;
        else toTheLeft = false;

        do{
            inventoryPowerSlot source = _powerSlots.get(index);
            inventoryPowerSlot next;

            if(toTheLeft) next = _powerSlots.get((index + 1) % _powerSlots.size());
            else next = _powerSlots.get((index - 1 + _powerSlots.size()) %_powerSlots.size());

            if(next.hasItem()){
                if(next.get_isSelected()){

                    MoveToAction movePowerSelected = new MoveToAction();

                    source.get_powerSelected().setVisible(true);
                    he.get_hudGestureListeners().get(source.get_powerSelectedListenerOwner()).set_enabled(true);

                    source.get_powerSelected().setPosition(next.get_slotPosition().x, next.get_slotPosition().y);
                    movePowerSelected.setPosition(source.get_slotPosition().x, source.get_slotPosition().y);
                    movePowerSelected.setDuration(h.SWAP_SLOTS_DURATION);

                    source.get_powerSelected().addAction(movePowerSelected);
                }

                MoveToAction moveItem = new MoveToAction();

                source.addSlotItem(_completeItemList.get(next.get_slotItem().get_itemID()));
                next.removeSlotItem();

                source.get_slotItem().setPosition(next.get_slotPosition().x, next.get_slotPosition().y);
                moveItem.setPosition(source.get_slotPosition().x, source.get_slotPosition().y);
                moveItem.setDuration(h.SWAP_SLOTS_DURATION);
                source.get_slotItem().addAction(moveItem);
            }
            else{

                MoveToAction moveBackground = new MoveToAction();

                source.get_customBackground().setVisible(true);
                he.get_hudGestureListeners().get(source.get_BackgroundListenerOwner()).set_enabled(true);

                source.get_customBackground().setPosition(next.get_slotPosition().x, next.get_slotPosition().y);
                moveBackground.setPosition(source.get_slotPosition().x, source.get_slotPosition().y);
                moveBackground.setDuration(h.SWAP_SLOTS_DURATION);
                source.get_customBackground().addAction(moveBackground);
            }

            if(toTheLeft)index = (index + 1) % _powerSlots.size();
            else index = (index - 1 + _powerSlots.size()) % _powerSlots.size();

        }while((_powerSlots.get(index).get_slotPosition().x < slotTarget.get_slotPosition().x && toTheLeft)
                || (_powerSlots.get(index).get_slotPosition().x > slotTarget.get_slotPosition().x && !toTheLeft));


        slotTarget.addSlotItem(_completeItemList.get(_dragSlotSource.get_draggItem().get_itemID()));
        slotTarget.get_slotItem().setPosition(_dragSlotSource.get_draggItem().getX(), slotTarget.get_slotPosition().y);

        MoveToAction moveDragItem = new MoveToAction();
        moveDragItem.setPosition(slotTarget.get_slotPosition().x, slotTarget.get_slotPosition().y);
        moveDragItem.setDuration(h.SWAP_SLOTS_DURATION);

        removeDragSlotSource();
        slotTarget.get_slotItem().addAction(moveDragItem);

        h.powerSwapAction = true;
    }

    //SWAP ITEM SLOTS
    public void swapItemSlots(InventorySlotSource dragSource, InventorySlot slotTarget){

        inventoryItemSlot slotSource = (inventoryItemSlot)dragSource.getSourceSlot();
        String auxItem= "";

        if(slotSource.get_inventoryPosition() == slotTarget.get_inventoryPosition()){
            item_goBackToSource(slotSource);
            return;
        }

        //MANAGE SLOT TARGET
        if(slotTarget.hasItem()){
            auxItem = slotTarget.get_slotItem().get_itemID();
            slotTarget.removeSlotItem();
        }

        slotTarget.addSlotItem(_completeItemList.get(dragSource.get_draggItem().get_itemID()));

        //MANAGE SLOT SOURCE
        slotSource.removeSlotItem();

        if(!auxItem.equals("")){
            slotSource.addSlotItem(_completeItemList.get(auxItem));
        }

        removeDragSlotSource();
        h.powerSwapAction = true;

    }

    //SWAP ITEM SLOTS
    public void swapItemSlots(InventorySlot slotSource, InventorySlot slotTarget){

        String auxItem= "";

        if(slotSource.get_inventoryPosition() == slotTarget.get_inventoryPosition()){
            return;
        }

        //MANAGE SLOT TARGET
        if(slotTarget.hasItem()){
            auxItem = slotTarget.get_slotItem().get_itemID();
            slotTarget.get_slotItem().set_inventoryPosition(slotSource.get_inventoryPosition());
            slotTarget.removeSlotItem();
        }

        if(slotSource.hasItem()){
            slotTarget.addSlotItem(_completeItemList.get(slotSource.get_slotItem().get_itemID()));
            slotSource.get_slotItem().set_inventoryPosition(slotTarget.get_inventoryPosition());

            //MANAGE SLOT SOURCE
            slotSource.removeSlotItem();
        }

        if(!auxItem.equals("")){
            slotSource.addSlotItem(_completeItemList.get(auxItem));
        }
    }

    //PROGRAM POWER SLOTS MOVE
    public void programPowerSlotsMove(float x, float y, float duration) {

        for (int i = 0; i < _powerSlots.size(); i++) {
            _powerSlots.get(i).addSlotMoveLikeIton(x, y, duration);
        }
    }

    //PROGRAM ITEM SLOTS MOVE
    public void programItemSlotsMove(float x, float y, float duration) {

        for ( int i = 0; i < _itemSlots.size(); i++){
            _itemSlots.get(i).addSlotMoveLikeIton(x, y, duration);
        }
    }

    //RESET VALUES
    public void resetValues(){
        _xVariation = 0;
        _firstSlotIndex = 0;

        for (int i = 0; i < _powerSlots.size(); i++) {

            _powerSlots.get(i).set_slotPosition(h.POS_POWER_PLACEHOLDER.x + (h._powerSlotsDistance * i), h.POS_POWER_PLACEHOLDER.y);
        }
    }

    //POWER GO BACK TO SOURCE
    public void power_goBackToSource(inventoryPowerSlot source){

        float xToInitial = source.get_slotPosition().x - _dragInitialsX.get(source.get_inventoryPosition());

        float slotsXDifference = xToInitial/4;

        for(int i=0; i<_powerSlots.size(); i++){

            inventoryPowerSlot current = _powerSlots.get(i);
            MoveToAction move = new MoveToAction();

            if(source.get_inventoryPosition() == current.get_inventoryPosition()){

                current.set_slotPosition(_dragInitialsX.get(current.get_inventoryPosition()), current.get_slotPosition().y);

                move.setPosition(current.get_slotPosition().x, current.get_slotPosition().y);
                move.setDuration(h.ITEM_GOBACKTOSLOT_DURATION);

                _dragSlotSource.get_draggItem().addAction(move);
            }
            else if((xToInitial>0 && current.get_slotPosition().x > source.get_slotPosition().x)
                    || xToInitial < 0 && current.get_slotPosition().x < source.get_slotPosition().x){
                current.set_slotPosition(_dragInitialsX.get(current.get_inventoryPosition()), current.get_slotPosition().y);
                current.get_customBackground().setPosition(current.get_slotPosition().x + slotsXDifference, current.get_slotPosition().y);

                move.setPosition(current.get_slotPosition().x, current.get_slotPosition().y);
                move.setDuration(h.ITEM_GOBACKTOSLOT_DURATION);

                current.get_customBackground().addAction(move);
            }
        }

        _dragSlotSource.get_draggItem().clearListeners();
    }

    //ITEM GO BACK TO SOURCE
    public void item_goBackToSource(inventoryItemSlot source){

        MoveToAction move = new MoveToAction();

        move.setPosition(source.get_slotPosition().x, source.get_slotPosition().y);
        move.setDuration(h.ITEM_GOBACKTOSLOT_DURATION);

        _dragSlotSource.get_draggItem().addAction(move);

        _dragSlotSource.get_draggItem().clearListeners();
    }

    //DELETE SOURCE AND TARGET
    public void deleteSourceAndTarget(){
        _dragSlotSource = null;
        _dragSlotTarget = null;
    }

    //CREATE DRAG SLOT SOURCE
    public void createDragSlotSource (InventorySlot slot){

        if(slot.hasItem()) {
            if (slot.get_slotItem().get_itemType().contains("POWER"))_dragSlotSource = new InventorySlotSource(slot, _completeItemList.get(slot.get_slotItem().get_itemID()));
            else _dragSlotSource = new InventorySlotSource(slot, _completeItemList.get(slot.get_slotItem().get_itemID()));
        }
    }

    //INITIALIZE DRAG INITAILS X
    public void initializeDragInitialsX() {

        for(int i=0; i<_powerSlots.size(); i++){
            _dragInitialsX.remove(i);
            _dragInitialsX.put(i, _powerSlots.get(i).get_slotPosition().x);
        }
    }

    //REMOVE DRAG SLOT SOURCE
    public void removeDragSlotSource(){
        _dragSlotSource = null;
    }

    //ADD INVENTORY ITEM ACTORS
    public void addInvetoryItemActors(Stage s) {
        for (int i = 0; i < _itemSlots.size(); i++) {
            s.addActor(_itemSlots.get(i));
        }
    }
    //ADD INVENTORY POWER ACTORS
    public void addInventoryPowerActors(Stage s) {
        for (int i = 0; i < _powerSlots.size(); i++) {
            s.addActor(_powerSlots.get(i));
        }
    }

    //ADD DRAGG ITEM TO STAGE
    public void add_draggItemToStage(){
        he.getStage().addActor(get_dragSlotSource().getActor());
    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS

    /*

    public InventorySlotSource get_dragSlotSource(){
        return _dragSlotSource;
    }

    public InventorySlotTarget get_dragSlotTarget() {
        return _dragSlotTarget;
    }

    public HashMap<Integer, Float> get_dragInitialsX() {
        return _dragInitialsX;
    }

    public HashMap<Integer, inventoryPowerSlot> get_powerSlots() {
        return _powerSlots;
    }

    public HashMap<Integer, inventoryItemSlot> get_itemSlots(){
        return _itemSlots;
    }

    public Array<String> get_inventory() {
        return _inventory;
    }

    public HashMap<String, InventoryItem> get_completeItemList() {
        return _completeItemList;
    }

    public hotBar get_hotBar() {
        return _hotBar;
    }

    public Wallet get_wallet() {
        return _wallet;
    }

    public com.mygdx.safe.Entities.HUD_Entity getHe() {
        return he;
    }

    public HudDataComponent getH() {
        return h;
    }

    public float get_previousX() {
        return _previousX;
    }

    public float get_previousY() {
        return _previousY;
    }

    public float get_xVariation() {
        return _xVariation;
    }

    public float get_yVariation() {
        return _yVariation;
    }

    public float get_firstSlotX() {
        return _firstSlotX;
    }

    public float get_oldFirstSlotX() {
        return _oldFirstSlotX;
    }

    public float get_oldDragItemX() {
        return _oldDragItemX;
    }

    public float get_oldDragItemY() {
        return _oldDragItemY;
    }

    public int get_firstSlotIndex() {
        return _firstSlotIndex;
    }

    public boolean is_comesFromUpdate() {
        return _comesFromUpdate;
    }

    public boolean is_notMove() {
        return _notMove;
    }

    //SETTERS

    public void set_xVariationWithFling (float delta){

        if(get_dragSlotSource() == null)_xVariation = h.ITON_X_SLOT_VELOCITY * delta;

        h.ITON_X_SLOT_VELOCITY -= h.ITON_X_SLOT_VELOCITY/10;

        if(h.ITON_X_SLOT_VELOCITY < 5 && h.ITON_X_SLOT_VELOCITY > -5){
            if(get_dragSlotSource() == null){
                h.ITON_X_SLOT_VELOCITY = 0;
                h.itonSlotOnFling = false;
            }
        }
    }

    public void set_previousX(float _previousX) {
        this._previousX = _previousX;
    }

    public void set_xVariation(float _xVariation) {
        this._xVariation = _xVariation;
    }

    public void setHUD_Entity(com.mygdx.safe.Entities.HUD_Entity he) {
        this.he = he;
    }

    public void set_dragSlotTarget(InventorySlotTarget _dragSlotTarget) {
        this._dragSlotTarget = _dragSlotTarget;
    }

    public void set_inventory(Array<String> _inventory) {
        this._inventory = _inventory;
    }

    public void set_completeItemList(HashMap<String, InventoryItem> _completeItemList) {
        this._completeItemList = _completeItemList;
    }

    public void set_hotBar(hotBar _hotBar) {
        this._hotBar = _hotBar;
    }

    public void set_wallet(Wallet _wallet) {
        this._wallet = _wallet;
    }

    public void set_dragSlotSource(InventorySlotSource _dragSlotSource) {
        this._dragSlotSource = _dragSlotSource;
    }

    public void set_powerSlots(HashMap<Integer, inventoryPowerSlot> _powerSlots) {
        this._powerSlots = _powerSlots;
    }

    public void set_itemSlots(HashMap<Integer, inventoryItemSlot> _itemSlots) {
        this._itemSlots = _itemSlots;
    }

    public void set_dragInitialsX(HashMap<Integer, Float> _dragInitialsX) {
        this._dragInitialsX = _dragInitialsX;
    }

    public void set_previousY(float _previousY) {
        this._previousY = _previousY;
    }

    public void set_yVariation(float _yVariation) {
        this._yVariation = _yVariation;
    }

    public void set_firstSlotX(float _firstSlotX) {
        this._firstSlotX = _firstSlotX;
    }

    public void set_oldFirstSlotX(float _oldFirstSlotX) {
        this._oldFirstSlotX = _oldFirstSlotX;
    }

    public void set_oldDragItemX(float _oldDragItemX) {
        this._oldDragItemX = _oldDragItemX;
    }

    public void set_oldDragItemY(float _oldDragItemY) {
        this._oldDragItemY = _oldDragItemY;
    }

    public void set_firstSlotIndex(int _firstSlotIndex) {
        this._firstSlotIndex = _firstSlotIndex;
    }

    public void set_comesFromUpdate(boolean _comesFromUpdate) {
        this._comesFromUpdate = _comesFromUpdate;
    }

    public void set_notMove(boolean _notMove) {
        this._notMove = _notMove;
    }
}
        */
