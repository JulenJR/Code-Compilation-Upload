package com.mygdx.safe.MainGameGraph;

import com.mygdx.safe.MapManager;

/**
 * Created by Boris.InspitatGames on 4/01/18.
 */

public class GameGraphEditor {

    public static final String TAG=GameGraphEditor.class.getSimpleName();
    com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g;
    GameGraphs allGameGraphs;


    public GameGraphEditor(com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g){
        this.g=g;
        g.println(TAG+ " CHARGING GAMEGRAPH EDITOR PLATFORM");
        for(int i=0;i<TAG.length();i++) g.print("="); g.println("===================================");


    }

    public void CommandInterface(){

    }

    public void ChargeGameGraphs(){

    }

    public void selectGameGraph(String gg){

    }

    public void createGraphFromMap(MapManager mpMgr){

    }

    public void saveGameGraph(String gg){

    }

    public void saveAllGameGraphs(){

    }



}
