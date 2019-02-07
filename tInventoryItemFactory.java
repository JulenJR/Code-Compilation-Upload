package com.mygdx.safe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.safe.UI.InventoryItem;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by alumne_practiques on 06/07/17.
 */

public class tInventoryItemFactory {

    private Json _json = new Json(); //player json
    private final String INVENTORY_ITEM = "scripts/inventory_items.json";
    private static tInventoryItemFactory _instance = null;
    private HashMap<String,InventoryItem> _inventoryItemList;

    public static tInventoryItemFactory getInstance() {
        if (_instance == null) {
            _instance = new tInventoryItemFactory();
        }
        return _instance;
    }

    private tInventoryItemFactory() {
        ArrayList<JsonValue> list = _json.fromJson(ArrayList.class, Gdx.files.internal(INVENTORY_ITEM));
        _inventoryItemList = new HashMap<String, InventoryItem>();

        for (JsonValue jsonVal : list) {
            InventoryItem inventoryItem = _json.readValue(InventoryItem.class, jsonVal);
            _inventoryItemList.put(inventoryItem.get_itemID(), inventoryItem);
        }
    }

    /*public InventoryItem getInventoryItem(String itemID) {
        InventoryItem item = new InventoryItem(_inventoryItemList.get(itemID));
        item.setDrawable(new TextureRegionDrawable(Utility.ITEMS_TEXTUREATLAS.findRegion(item.get_itemID().toString())));
        item.setScaling(Scaling.none);
        return item;
    }*/
}