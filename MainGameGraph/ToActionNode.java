package com.mygdx.safe.MainGameGraph;

import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;

/**
 * Created by Boris.InspiratGames on 30/10/17.
 */

public class ToActionNode {

    public static final String TAG=ToActionNode.class.getSimpleName();


    String ID;

    boolean _blocked =false; // GRAPH BLOCKED OR UNBLOCKED TO THIS PATH . IF BLOCKED, THE NODE IS NOT VISIBLE.
    boolean _enable=false;    // POSIBLE OPTION or DISABLE OPTION // ENABLE IN ALL TRACED ROUTE AT GRAPH
    boolean _active=false;    // _ACTIVE POINT IN THIS GRAPH;

    String _nodeTypeAndName;  // Select, Proximity, Inventory, item,acton, action_connector,entity,portal,DialogGraph#NameOfNode
    String _data="none";
    String _text="none";
    String _prevID="none";
    int _visitCounter=0;
    int _quantity =0;
    String _activateConditions;
    Conditions conditions;
    GenericMethodsInputProcessor g;

    public ToActionNode(){

    }

    public void setToActionNode(com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g){
        this.g=g;
        conditions=new Conditions(g);
    }

    public String getContent(){

        return " [ ID:"+ ID + " ] [ TYPE:" + _nodeTypeAndName + " ] [ " +
                (_blocked ?" UNBLOCKED ":" BLOCKED ")+ " ] [ " +
                (_enable?" ENABLED ":" DISABLED ")+ " ] [ " +
                (_active?" ACTIVATED ":" NOT ACTIVATED ")+ "]\n" +
                " [ ACTIVATE_CONDITIONS: "+_activateConditions+" ]\n"+
                " [ CONDITIONS:"+conditions.show(g.m.ggMgr.currentgg.getTimers(), g.m.ggMgr.currentgg.get_sequences())+" ]\n"+
                " ::::: [ TEXT:"+_text+" ]\n";
    }


    public String get_nodeTypeAndName() {
        return _nodeTypeAndName;
    }

    public void set_nodeTypeAndName(String _nodeTypeAndName) {
        this._nodeTypeAndName = _nodeTypeAndName;
    }

    public String get_data() {
        return _data;
    }

    public void set_data(String _data) {
        this._data = _data;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public boolean is_blocked() {
        return _blocked;
    }

    public void set_blocked(boolean _blocked) {
        this._blocked = _blocked;
    }

    public boolean is_enable() {
        return _enable;
    }

    public void set_enable(boolean _enable) {
        this._enable = _enable;
    }

    public boolean is_active() {
        return _active;
    }

    public void set_active(boolean _active) {
        this._active = _active;
    }

    public String get_nodeType() {
        return _nodeTypeAndName;
    }

    public void set_nodeType(String _nodeType) {
        this._nodeTypeAndName = _nodeType;
    }

    public String get_text() {
        return _text;
    }

    public void set_text(String _text) {
        this._text = _text;
    }

    public String get_activateConditions() {
        return _activateConditions;
    }

    public void set_activateConditions(String _activateConditions) {
        this._activateConditions = _activateConditions;
    }

    public Conditions getConditions() {
        return conditions;
    }

    public void setConditions(Conditions conditions) {
        this.conditions = conditions;
    }
}
