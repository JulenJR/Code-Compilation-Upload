package com.mygdx.safe.Conversation;

import com.mygdx.safe.Components.InputComponent;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;

/**
 * Created by Boris.Escajadillo on 29/09/17.
 */

public class Choice_Arist{

    //TAG
    private static final String TAG = InputComponent.class.getSimpleName();

    //ASPECTS
    private GenericMethodsInputProcessor g;

    private String OriginID;
    private String DestinyID;
    private String choiceContent;
    private String actionEvent;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public Choice_Arist(){
    }

    public String toString(){
        return DestinyID +" : "+ choiceContent;
    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS

    public GenericMethodsInputProcessor getG() {
        return g;
    }

    public String getOriginID() {
        return OriginID;
    }

    public String getDestinyID() {
        return DestinyID;
    }

    public String getChoiceContent() {
        return choiceContent;
    }

    public String getActionEvent() {
        return actionEvent;
    }

    //SETTERS

    public void setG(GenericMethodsInputProcessor g) {
        this.g = g;
    }

    public void setOriginID(String originID) {
        OriginID = originID;
    }

    public void setDestinyID(String destinyID) {
        DestinyID = destinyID;
    }

    public void setChoiceContent(String choiceContent) {
        this.choiceContent = choiceContent;
    }

    public void setActionEvent(String actionEvent) {
        this.actionEvent = actionEvent;
    }

}

