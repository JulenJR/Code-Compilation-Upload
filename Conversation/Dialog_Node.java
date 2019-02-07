package com.mygdx.safe.Conversation;

/**
 * Created by Boris_Escajadillo on 28/09/17.
 */

public class Dialog_Node {

    //TAG
    private static final String TAG = Dialog_Node.class.getSimpleName();

    //VERTEX
    private String uniqueID;
    private String content;
    private TextActor textactor;
    private String actionEvent;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public Dialog_Node(){textactor=null;}

    // UNIQUEID FORMAT:
    // ENTITY_ORIGIN_ID#ENTITY_DESTINY_ID#CONV_CONVERSATION_NUMBER

    /*_______________________________________________________________________________________________________________*/

    //GETTERS

    public String getContent() { return content; }

    public TextActor getTextactor() {  return textactor; }

    public String getID(){ return uniqueID; }

    public String getActionEvent() {   return actionEvent;  }

    public String getUniqueID() {
        return uniqueID;
    }

    //SETTERS

    public void setContent(String content) {   this.content = content; }

    public void setTextactor(TextActor textactor) {  this.textactor = textactor; }

    public void setID(String uniqueID) { this.uniqueID = uniqueID;}

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public void setActionEvent(String actionEvent) {
        this.actionEvent = actionEvent;
    }

    public void setNode(String ui,String cnt,TextActor t){
        this.uniqueID =ui;
        this.content =cnt;
        this.textactor=t;
    }

    //TO STRING
    public String toString() {  return uniqueID +" : "+ content;  }

}
