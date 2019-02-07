package com.mygdx.safe.UI;

public class InventoryItemLocation {

    //TAG
    private static final String TAG = InventoryItemLocation.class.getSimpleName();

    //ASPECTS
    private com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g;

    //LOCATION
    private int locationIndex;
    private String itemTypeAtLocation;
    private int numberItemsAtLocation;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public InventoryItemLocation(){}

    public InventoryItemLocation( int locationIndex, String itemTypeAtLocation, int numberItemsAtLocation){
        this.locationIndex = locationIndex;
        this.itemTypeAtLocation = itemTypeAtLocation;
        this.numberItemsAtLocation = numberItemsAtLocation;
    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS
    public String getItemTypeAtLocation() {
        return itemTypeAtLocation;
    }

    public int getLocationIndex() {
        return locationIndex;
    }

    public int getNumberItemsAtLocation() {
        return numberItemsAtLocation;
    }

    //SETTERS
    public void setItemTypeAtLocation(String itemTypeAtLocation) {
        this.itemTypeAtLocation = itemTypeAtLocation;
    }

    public void setLocationIndex(int locationIndex) {
        this.locationIndex = locationIndex;
    }

    public void setNumberItemsAtLocation(int numberItemsAtLocation) {
        this.numberItemsAtLocation = numberItemsAtLocation;
    }
}