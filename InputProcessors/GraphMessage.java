package com.mygdx.safe.InputProcessors;

import com.badlogic.gdx.utils.Queue;

/**
 * Created by Boris.InspiratGames on 23/11/17.
 */

public class GraphMessage {

    //TAG
    private static final String TAG = GraphMessage.class.getSimpleName();

    //ASPECTS
    private GenericMethodsInputProcessor g;

    //MESSAGE QUEUE
    private Queue<String> messageQueue;

    /*_______________________________________________________________________________________________________________*/

    public GraphMessage(GenericMethodsInputProcessor g){
        this.g=g;
        messageQueue=new Queue<String>();

    }

    //SEND MESSAGE
    public void sendMessage(String message, String treeID, int treeNumNode){
        g.set_hasGraphCommand(true);
        messageQueue.addLast(message );

        if(treeID != null && treeNumNode !=-1)
            g.println(TAG + " SENDING MESSAGE:  " + message+ "     " +
                    treeID + "   " + g.m.ggMgr.getCurrentgg().getAllShooterTrees().get(treeID).getNode(treeNumNode).getName());
        g.m.ggMgr.receive(treeID, treeNumNode);
    }

    public void sendMessage(String[] message, String treeID, int treeNumNode){
        String nonSplit = "";

        if(message.length > 0) nonSplit += message[1];
        for (int i=1; i<message.length; i++){
            nonSplit += "#" + message[i];
        }

        g.println(TAG + " SENDING MESSAGE:  " + nonSplit+ "     " + treeID + "   " + g.m.ggMgr.getCurrentgg().getAllShooterTrees().get(treeID).getNode(treeNumNode).getName());
        sendMessage(nonSplit, treeID, treeNumNode);
    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS

    public Queue<String> getMessageQueue() { return messageQueue; }

    public String getFirst(){
        String s=messageQueue.first();
        messageQueue.removeFirst();
        return s;
    }

    //SETTERS

    public void setMessageQueue(Queue<String> messageQueue) {
        this.messageQueue = messageQueue;
    }
}