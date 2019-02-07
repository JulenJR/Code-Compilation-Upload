package com.mygdx.safe.MainGameGraph;

import com.badlogic.gdx.utils.*;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;

import java.util.HashMap;

/**
 * Created by Boris.InspiratGames on 2/11/17.
 */

public class GameGraphs {

    HashMap<String,GameGraph> gameGraphs;
    Array<NodeAndChoiceGameGraphs> nodeAndChoiceGameGraphs;
    GenericMethodsInputProcessor g;
    GameGraph currentgg;
    private static final String TAG = GameGraphs.class.getSimpleName();



    GameGraphs(){

    }

    public void config(GenericMethodsInputProcessor g){
        this.g=g;
        if(gameGraphs==null) gameGraphs= new HashMap<String,GameGraph> ();
        for(NodeAndChoiceGameGraphs ncgg:nodeAndChoiceGameGraphs){
            gameGraphs.put(ncgg.graphName,new GameGraph(ncgg.graphName,ncgg.getHashMapNodes(g),ncgg.rootId,g));
            gameGraphs.get(ncgg.graphName).addAllActionChoices(ncgg.getActions());
        }
        if(currentgg==null){
            currentgg=gameGraphs.get("LVL#1#1#LOOKSCREEN#STANDAR");
            g.m.ggMgr.currentgg = currentgg;
        }

        currentgg.config();
        g.println(TAG + " CREATED POINTERS:");
        for(String s:currentgg.pointers.keySet()){
            if (g.showGameGraphsCreatedPointers) g.println(TAG + " ["+ s+ "]");
        }

        if(g.showingGameGraphs) {
            for (NodeAndChoiceGameGraphs ncgg : nodeAndChoiceGameGraphs) {
                g.print(gameGraphs.get(ncgg.graphName).toString());
            }
        }

    }

    public void setCurrentGameGraph(GameGraph currentgg) {    this.currentgg = currentgg; }

    public GameGraph getCurrentGameGraph() {   return currentgg;  }







}
