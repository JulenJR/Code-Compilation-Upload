package com.mygdx.safe.UI;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.safe.EntityConfig;

/**
 * Created by alumne_practiques on 06/07/17.
 */

public class InventoryItem extends Image{

    //TAG
    private static final String TAG = InventoryItem.class.getSimpleName();

    //ASPECTS
    private com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g;

    //ITEM INFO
    private String _itemID = "";
    private String _itemType = "";
    private String _itemShortDescription = "";

    //LISTENER
    private String _listenerKey;

    //ITEM VALUE & QUANTITY
    private int _itemValue;
    private int _itemQuantity;

    //INVENTORY POSITION
    private int _inventoryPosition = -1;

    //NCACHE
    private com.mygdx.safe.nCache nAnimCache;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public InventoryItem(){
        super();
    }

    public InventoryItem(EntityConfig e, com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g){
        super((TextureRegion) g.m.lvlMgr.get_nCacheMap().get(e.get_entityID()).get_nAnimation().getNProgram("IDLE", "DOWN", false).get_nanimation().getKeyFrame(1.0f));

        this.nAnimCache = g.m.lvlMgr.get_nCacheMap().get(e.get_entityID());
        this._itemID = e.get_entityID();
        this._itemType = e.getItemType();
        this._itemShortDescription = e.getItemShortDescription();
        this._itemValue = e.getValue();
        this._itemQuantity = e.getQuantity();
    }

    public InventoryItem(InventoryItem item, com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g){
        super((TextureRegion) g.m.lvlMgr.get_nCacheMap().get(item._itemID).get_nAnimation().getNProgram("IDLE" , "DOWN", false).get_nanimation().getKeyFrame(1.0f));

        this.nAnimCache = g.m.lvlMgr.get_nCacheMap().get(item.get_itemID());

        this._itemID = item._itemID;
        this._itemType = item._itemType;
        this._itemShortDescription = item._itemShortDescription;
        this._itemValue = 0 + item.get_itemValue();
        this._itemQuantity = 0 + item.get_itemQuantity();
    }

    /*_______________________________________________________________________________________________________________*/

    //IS SAME INVENTORY ITEM
    public boolean isSameInventoryItem(InventoryItem candidateInventoryItem){
        return this._itemID.equals(candidateInventoryItem.get_itemID());
    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS

    public String get_itemID() {return _itemID;}

    public String get_itemType() {return _itemType;}

    public String get_itemShortDescription() {return _itemShortDescription;}

    public int get_itemValue() {return _itemValue;}

    public int get_itemFactor() {return _itemQuantity;}

    public String get_listenerKey() {
        return _listenerKey;
    }

    public com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor getG() {
        return g;
    }

    public int get_itemQuantity() {
        return _itemQuantity;
    }

    public com.mygdx.safe.nCache getnAnimCache() {
        return nAnimCache;
    }

    public int get_inventoryPosition() {
        return _inventoryPosition;
    }

    //SETTERS

    public void set_itemID(String _itemID) {this._itemID = _itemID;}

    public void set_itemType(String _itemType) {this._itemType = _itemType;}

    public void set_itemShortDescription(String _itemShortDescription) {this._itemShortDescription = _itemShortDescription;}

    public void set_itemValue(int _itemValue) {this._itemValue = _itemValue;}

    public void set_itemFactor(int _itemFactor) {this._itemQuantity = _itemFactor;}

    public void set_listenerKey(String _listenerKey) {
        this._listenerKey = _listenerKey;
    }

    public void setG(com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g) {
        this.g = g;
    }

    public void set_itemQuantity(int _itemQuantity) {
        this._itemQuantity = _itemQuantity;
    }

    public void setnAnimCache(com.mygdx.safe.nCache nAnimCache) {
        this.nAnimCache = nAnimCache;
    }

    public void set_inventoryPosition(int _inventoryPosition) {
        this._inventoryPosition = _inventoryPosition;
    }




}