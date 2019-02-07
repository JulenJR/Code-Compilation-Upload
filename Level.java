package com.mygdx.safe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Json;
import com.mygdx.safe.Entities.GameEntity;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;

import java.util.HashMap;

/**
 * Created by alumne_practiques on 09/10/17.
 */

public class Level {

    //LEVEL
    private static final String TAG = Level.class.getSimpleName();
    private GenericMethodsInputProcessor g;
    private String _lvlID;
    private LevelConfig _lvlConfig;
    private boolean _setLvlConfig = false;

    //PLAYER & OTHER ENTITIES

    private HashMap<String, GameEntity> _lvlMapEntities;
    private GameEntity selectedGE = null;
    private GameEntity previousSelectedGE = null;

    public Level(GenericMethodsInputProcessor g){
        this._lvlMapEntities = new HashMap<String, GameEntity>();
    }

    public void config(GenericMethodsInputProcessor g, OrthographicCamera camera, LevelManager lvlMgr){
        if(_setLvlConfig){

            this.g=g;

            this._lvlID = _lvlConfig.get_levelID();
            //lvlMgr.get_mapManager().loadMap(3);


            for(String ss: lvlMgr.get_entityConfigMap().keySet()) g.println(TAG +" PRINTING ENTITYCONFIG HASHMAP KEYSET ELEMENT NAME: ["+ss+"]");

            g.m.lvlMgr.addNPCEntities();
        }
    }

    //Getters




    public GameEntity getSelectedGE() {
        return selectedGE;
    }

    public String get_lvlID() {
        return _lvlID;
    }

    public com.mygdx.safe.LevelConfig get_lvlConfig() {
        return _lvlConfig;
    }

    public HashMap<String, GameEntity> get_lvlEntities() {
        return _lvlMapEntities;
    }



    public boolean is_setLvlConfig() {
        return _setLvlConfig;
    }

    public GameEntity getPreviousSelectedGE() {
        return previousSelectedGE;
    }

    //Setters

    public void setSelectedGE(GameEntity selectedGE) {

        if(selectedGE!=null) {
            if (this.previousSelectedGE == null) this.previousSelectedGE = selectedGE;
        }else{
            if(this.previousSelectedGE!=null && this.selectedGE!=null) {
                if (!(this.previousSelectedGE.getID().contains(this.selectedGE.getID()))) {
                    this.previousSelectedGE=this.selectedGE;
                }
            }
        }

        this.selectedGE = selectedGE;
        /*
        if(this.previousSelectedGE!=null ) g.printlns(TAG + "******************* PREVIOUS ENTITY:" + previousSelectedGE.getID());
        else g.printlns(TAG+ "********************* PREVIOUS ENTITY: NULL");

        if(this.selectedGE!=null ) g.printlns(TAG+ "********************* CURRENT ENTITY: " +this.selectedGE.getID());
        else g.printlns(TAG+ "********************* CURRENT ENTITY: NULL");
        */
    }

    public void set_lvlID(String _lvlID) {
        this._lvlID = _lvlID;
    }

    public void set_lvlConfig(LevelConfig _lvlConfig) {
        this._lvlConfig = _lvlConfig; this._setLvlConfig = true;
    }

    //JSON serialization
    static public LevelConfig GetLevelConfig(String path){
        Json json = new Json();
        return json.fromJson(com.mygdx.safe.LevelConfig.class, Gdx.files.internal(path));
    }
}
