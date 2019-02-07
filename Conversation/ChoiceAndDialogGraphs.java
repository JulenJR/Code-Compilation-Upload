package com.mygdx.safe.Conversation;

import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

/**
 * Created by Boris.Escajadillo on 5/10/17.
 */

public class ChoiceAndDialogGraphs{

    //TAG
    private static final String TAG = ChoiceAndDialogGraphs.class.getSimpleName();

    //CHOICE_ARISTS & DIALOG_NODES
    private Array<Choice_Arist> choices;
    private Array<Dialog_Node> dialogs;

    //ROOT ID & CONVERSATION ID
    private String rootID;
    private String conversationId; //DIALOG_GRAPH_ID

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    ChoiceAndDialogGraphs(){}

    /*_______________________________________________________________________________________________________________*/

    //GETTERS

    public HashMap<String,Dialog_Node> getHashMapConversations(){ // SETS ALL DIALOGSGRAPHSES
        HashMap<String,Dialog_Node> conversations= new HashMap<String,Dialog_Node> ();
        for(Dialog_Node d: dialogs){
            conversations.put(d.getID(),d);
        }
        return conversations;
    }

    public Array<Choice_Arist> getArrayChoices (){
        return choices;
    }

    public String getRootId(){
        return rootID;
    }

    public String getConversationId(){
        return conversationId;
    }

}