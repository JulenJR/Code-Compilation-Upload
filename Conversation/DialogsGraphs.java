package com.mygdx.safe.Conversation;

import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

/**
 * Created by Boris.InspiratGames on 2/10/17.
 */

public class DialogsGraphs {

    //TAG
    private static final String TAG = DialogsGraphManager.class.getSimpleName();

    //ASPECTS
    com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g;

    //CHOICE AND DIALOGS GRAPHSES
    HashMap<String,DialogGraph>  allChoiceAndDialogsGraphses;
    Array<ChoiceAndDialogGraphs> choiceAndDialogGraphses;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public void DialogGraphs(com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g) {}

    //CONFIG
    public void config(com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g){

        this.g = g;

        if(allChoiceAndDialogsGraphses==null) allChoiceAndDialogsGraphses=new HashMap<String,DialogGraph> ();
        for (ChoiceAndDialogGraphs c: choiceAndDialogGraphses){

            allChoiceAndDialogsGraphses.put(c.getConversationId(),new DialogGraph(c.getConversationId(),c.getHashMapConversations(),c.getRootId(), g));
            allChoiceAndDialogsGraphses.get(c.getConversationId()).addAllChoices(c.getArrayChoices());
        }
    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS

    public DialogGraph getDialogGraph(String conversationID){
        // SELECT ENTIRE CONVERSATION ID
        for(String s: allChoiceAndDialogsGraphses.keySet()){
            if (s.contains(conversationID))
                return allChoiceAndDialogsGraphses.get(s);
        }
        return allChoiceAndDialogsGraphses.get(conversationID);
    }

    public DialogGraph getDialogGraph(int lvl, int n_conv, String nameGE,String visualGameState){
        // SELECT LEVEL
        Array<String> list = new Array<String>();
        for(String s: allChoiceAndDialogsGraphses.keySet()){
            if (s.contains("LVL_"+lvl))
                list.add(s);
        }
        // SELECT CONVERSATION NUMBER
        Array<String> list2 = new Array<String> ();
        for(String s: list){
            if(s.contains("CONV_"+n_conv)){
                list2.add(s);
            }
        }
        // SELECT NAME OF GAME ENTITY
        Array<String> list3=new Array<String> ();
        for(String s: list2){
            if(s.contains(nameGE)){
                list3.add(s);
            }
        }
        // SELECT VISUALGAMESTATE
        for(String s: list3){
            if(s.contains(visualGameState)){
                return allChoiceAndDialogsGraphses.get(s);
            }
        }
        return null;
    }

    public HashMap<String, DialogGraph> getAllChoiceAndDialogsGraphses() {
        return allChoiceAndDialogsGraphses;
    }

    public Array<ChoiceAndDialogGraphs> getChoiceAndDialogGraphses() {
        return choiceAndDialogGraphses;
    }

    //SETTERS

    public void setAllChoiceAndDialogsGraphses(HashMap<String, DialogGraph> allChoiceAndDialogsGraphses) {
        this.allChoiceAndDialogsGraphses = allChoiceAndDialogsGraphses;
    }

    public void setChoiceAndDialogGraphses(Array<ChoiceAndDialogGraphs> choiceAndDialogGraphses) {
        this.choiceAndDialogGraphses = choiceAndDialogGraphses;
    }
}
