package com.mygdx.safe.UI;

import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;

/**
 * Created by alumne_practiques on 27/11/17.
 */

public class Wallet {

    //TAG
    private static final String TAG = Wallet.class.getSimpleName();

    //ASPECTS
    private GenericMethodsInputProcessor g;

    //MONEY
    private InventoryItem _money;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public Wallet(InventoryItem money, GenericMethodsInputProcessor g){
        this.g = g;
        this._money = money;
    }

    /*_______________________________________________________________________________________________________________*/

    //ADD
    public void add(int quantity){
        _money.set_itemQuantity(_money.get_itemQuantity() + quantity);
    }

    //REMOVE
    public void remove(int quantity){
        //if(_money.get_itemQuantity() < quantity) g.println("ERROR: NOT ENOUGH MONEY");
        //else g.m.invUI.remove("ITEM_COIN", quantity);
    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS
    public InventoryItem get_money() {
        return _money;
    }

    //SETTERS
    public void set_money(InventoryItem _money) {
        this._money = _money;
    }
}

