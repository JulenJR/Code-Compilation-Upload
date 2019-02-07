package com.mygdx.safe.MainGameGraph;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;


/**
 * Created by Boris.InspiratGames on 1/11/17.
 */

public class GameGraphsCharger {
    public static String PATH="scripts/GameGraphs.json";

    public GameGraphsCharger(){

    }

    static public GameGraphs GetGameGraphs(String path){
        Json json = new Json();
        return json.fromJson(GameGraphs.class, Gdx.files.internal(path));
    }



}
