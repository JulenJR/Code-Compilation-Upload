package com.mygdx.safe;

import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

/**
 * Created by alumne_practiques on 09/10/17.
 */

public class LevelConfig {

    private String _levelID;
    private HashMap<String, String> _MapPathHashMap;
    private HashMap<String, String> _entityJsonPaths;


    private String _ItemPowerJsonPath;


    //private String _MapID;
    //private String _MapPath;
    LevelConfig(){
        this._entityJsonPaths = new HashMap<String, String>();
        this._MapPathHashMap = new HashMap<String, String>();
    }

    //Getters

    public String get_levelID() {
        return _levelID;
    }

    public HashMap<String, String> get_MapPathHashMap() {
        return _MapPathHashMap;
    }



    public String get_ItemPowerJsonPath() {
        return _ItemPowerJsonPath;
    }

    public HashMap<String, String> get_entityJsonPaths() {
        return _entityJsonPaths;
    }

    //Setters


    public void set_levelID(String _levelID) {
        this._levelID = _levelID;
    }

    public void set_MapPathHashMap(HashMap<String, String> _MapPathHashMap) {
        this._MapPathHashMap = _MapPathHashMap;
    }

    public void set_ItemPowerJsonPath(String _ItemPowerJsonPath) {
        this._ItemPowerJsonPath = _ItemPowerJsonPath;
    }

    public void set_entityJsonPaths(HashMap<String, String> _entityJsonPaths) {
        this._entityJsonPaths = _entityJsonPaths;
    }
}
