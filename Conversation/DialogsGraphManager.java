package com.mygdx.safe.Conversation;

import com.badlogic.gdx.utils.Array;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;

/**
 * Created by Boris.Escajadillo on 5/10/17.
 */

public class DialogsGraphManager {

    //TAG
    private static final String TAG = DialogsGraphManager.class.getSimpleName();

    //ASPECTS
    GenericMethodsInputProcessor g;

    //DIALOG GRAPH
    DialogsGraphs ds;
    DialogGraph d;

    //CURRENT TREE & NODE
    private String TreeID;
    private int TreeNumNode;
    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public DialogsGraphManager(GenericMethodsInputProcessor g) {
        this.g=g;
    }

    //LOAD
    public void load() {
        ds = DialogsGraphCharger.GetDialogsGraphs(DialogsGraphCharger.PATH);
        ds.config(g);
    }

    /*_______________________________________________________________________________________________________________*/

    public void selectDialogGraph(String s) {
        g.println("DIALOG GRAHPSES " + s);
        d = ds.getDialogGraph(s);
        d.setCurrentConversation(d.getRootID());
    }

    /*_______________________________________________________________________________________________________________*/

    //RECEIVE
    public void receive(String[] message,  String treeID, int treeNumNode){

        this.TreeID = treeID;
        this.TreeNumNode = treeNumNode;

        if(message[0].equalsIgnoreCase("LOAD")){
            selectDialogGraph(message[1] + "#" + message[2] + "#" + message[3] + "#" + message[4]);
            send(message[0], message[3], treeID, treeNumNode);
        }
        else if(message[0].equalsIgnoreCase("SHOW_CHOICES"))send(message[0], message[1], treeID, treeNumNode);
        else if(message[0].equalsIgnoreCase("SELECT_CHOICES")){
            d.setCurrentConversation(message[2]);
            send(message[0], message[1], treeID, treeNumNode);
        }


    }

    //SEND
    public void send(String message, String geID, String treeID, int treeNumNode){

        String inst = "";

        if(message.equalsIgnoreCase("LOAD") || message.equalsIgnoreCase("SELECT_CHOICES")){

            inst = geID.split("_")[0] + "#true#" + d.getConversations().get(d.getCurrentUniqueDialogId()).getContent();

            g.m.lvlMgr.get_acton().receive(inst.split("#"), treeID, treeNumNode);
        }
        else if (message.equalsIgnoreCase("SHOW_CHOICES")){

            Array<Choice_Arist> list=new Array<Choice_Arist>();
            for(Choice_Arist c: d.get_choices(d.getCurrentUniqueDialogId())){
                list.add(c);
            }

            if(list.size == 0) {
                g.m.he.addToEMO(50);
                if(g.m.he.getEMO()>200) g.m.he.setEMO(200);
                //g.m.he.getHudActorDataComponent().safeT.checkState();
                //g.gm.sendMessage("GAMEGRAPH#SELECT#START#ENTITY#" + g.m.lvlMgr.get_currentLvl().getSelectedGE().getID(), null, null);
                //SETROOTID
                g.m.dgMgr.getD().setForcedId(g.m.dgMgr.getD().getRootID());
            }
            else{
                inst = "PLAYER#true";
                for(Choice_Arist c: list)inst += "#" + c.getChoiceContent();
                g.m.lvlMgr.get_acton().receive(inst.split("#"), treeID ,treeNumNode);
            }
        }
    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS

    public DialogsGraphs getDs() {
        return ds;
    }

    public DialogGraph getD() {
        return d;
    }

    //SETTERS

    public void setDs(DialogsGraphs ds) {
        this.ds = ds;
    }

    public void setD(DialogGraph d) {
        this.d = d;
    }

}