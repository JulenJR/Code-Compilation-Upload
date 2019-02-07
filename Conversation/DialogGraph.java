package com.mygdx.safe.Conversation;

import com.badlogic.gdx.utils.Array;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Boris.InspiratGames on 28/09/17.
 */

public class DialogGraph {

    //TAG
    private static final String TAG = Dialog_Node.class.getSimpleName();

    //ASPECTS
    private GenericMethodsInputProcessor g;

    //HASHMAPS
    private HashMap<String,Dialog_Node> conversations;
    private HashMap<String,ArrayList<Choice_Arist>> associatedChoices;
    private HashMap <String,String> actions;

    //ID'S
    private String currentUniqueDialogId =null;
    private String dialogGraphID;
    private String rootID;

    //GO NODE STRING
    private String goNode;

    /*_______________________________________________________________________________________________________________*/

    public DialogGraph(GenericMethodsInputProcessor g){
        this.g = g;

        conversations=new HashMap<String,Dialog_Node>();
        associatedChoices=new HashMap<String,ArrayList<Choice_Arist>> ();
        actions=new HashMap<String,String>(); //SINTAX=<ACTION,NODE_GO>
    }

    public DialogGraph(String id, HashMap<String,Dialog_Node> convs, String rootID, GenericMethodsInputProcessor g){
        this.g = g;

        actions=new HashMap<String,String>(); //SINTAX=<ACTION,NODE_GO>
        this.dialogGraphID=id;
        this.rootID=rootID;
        setConversations(convs);
        setCurrentConversation(rootID);
    }

    /*_______________________________________________________________________________________________________________*/

    public boolean isValid(String uid){
        Dialog_Node d=conversations.get(uid);
        return d != null;
    }

    public boolean isReachable(String oID,String dID){
        if(!isValid(oID) || !isValid(dID)) return false;
        if(conversations.get(oID)==null) return false;
        ArrayList<Choice_Arist> list = associatedChoices.get(oID);

        if(list==null) return false;
        for(Choice_Arist choice: list){
            if(choice.getOriginID().equalsIgnoreCase(oID) &&
                    choice.getDestinyID().equalsIgnoreCase(dID)){
                return true;
            }
        }
        return false;
    }

    public void add_Choice(Choice_Arist c){
        ArrayList<Choice_Arist> list=associatedChoices.get(c.getOriginID());
        if(list==null) return;
        for(Choice_Arist ca: list){
            if (ca.getDestinyID().contains(c.getDestinyID())) return;
        }
        list.add(c);
    }

    public void addAllChoices(Array<Choice_Arist> choices){
        for(Choice_Arist c:choices){
            c.setG(g);
            add_Choice(c);
        }
    }

    public void passChoice(Choice_Arist c){
        String action=c.getActionEvent();
        actionAnalizer(action);
        g.println(TAG+ " CHOICE-ORIGIN ID:"+c.getOriginID()+"DESTINY ID:"+c.getDestinyID()+" EVENT:"+action);
    }

    public String displayCurrentDialog(){
        return conversations.get(currentUniqueDialogId).getContent();
    }

    public void actionAnalizer(String action){
        if(action.contains("@")){
            String[] actionHash=action.split("@");
            for(int i=0; i<actionHash.length; i++) actions.put(actionHash[i].split("#")[0], actionHash[i].split("#")[1]);
        }else{
            if(actions!=null && actions.get(action)!=null){
                goNode=actions.get(action);
            }
        }
        g.println(TAG + " GONODE=["+goNode+"]- HASHMAP ACTIONS = ["+actions.keySet().toString()+"]");
    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS

    public Dialog_Node getConversationById(String id){
        if(!isValid(id)){
            g.println("UniqueId "+id + "is not valid");
            return null;
        }
        return  conversations.get(id);
    }

    public String getCurrentConversation(){
        return this.currentUniqueDialogId;
    }

    public ArrayList<Choice_Arist> get_choices(String id){
        return associatedChoices.get(id);
    }

    public HashMap<String, ArrayList<Choice_Arist>> getAssociatedChoices() {
        return associatedChoices;
    }

    public String getRootID() {    return rootID;  }

    public HashMap<String, Dialog_Node> getConversations() {
        return conversations;
    }

    public HashMap<String, String> getActions() {
        return actions;
    }

    public String getCurrentUniqueDialogId() {
        return currentUniqueDialogId;
    }

    public String getDialogGraphID() {
        return dialogGraphID;
    }

    public String getGoNode() {
        return goNode;
    }

    //SETTERS

    public void setConversations(HashMap<String,Dialog_Node> convs){
         if(convs.size()<0){
             throw new IllegalArgumentException("Can't have a negative amount of conversations");
         }
         this.conversations=convs;
         this.associatedChoices = new HashMap<String,ArrayList<Choice_Arist>>(conversations.size());
        for(Dialog_Node d: conversations.values()){
             associatedChoices.put(d.getID(),new ArrayList<Choice_Arist>());
        }
    }

    public void setCurrentConversation(String id){
        Dialog_Node d =getConversationById(id);
        if(conversations==null) return;
        if(currentUniqueDialogId==null || currentUniqueDialogId.equalsIgnoreCase(id) || isReachable(currentUniqueDialogId,id)){
            currentUniqueDialogId=id;
            if(conversations.get(currentUniqueDialogId).getActionEvent().equalsIgnoreCase("none") || conversations.get(currentUniqueDialogId).getActionEvent()==null){
                String action=conversations.get(currentUniqueDialogId).getActionEvent();
                actionAnalizer(action);
                g.println(TAG+" DESTINY ID:"+id+" EVENT:"+action);
            }
        }else{
           g.println(TAG +" New conversation Dialog_Node [" + id + "] is not reachable from current node [" + currentUniqueDialogId+"]");
        }
    }

    public void setForcedId(String id){
        if(id.equalsIgnoreCase(rootID)){
            String finalInstruction[]=conversations.get(currentUniqueDialogId).getActionEvent().split("#");

            g.println(TAG+ "  "+ conversations.get(currentUniqueDialogId).getActionEvent());

            if(finalInstruction[0].equalsIgnoreCase("LOAD")){
                if(finalInstruction.length>1){
                    g.gm.sendMessage("GAMEGRAPH#LOAD#"+finalInstruction[1], null, -1);
                }else {
                    ///
                    if(goNode.length()>0) g.gm.sendMessage("GAMEGRAPH#LOAD#"+goNode, null, -1);
                    actions.clear();
                    goNode="";
                }
            }else{
                actions.clear();
                goNode="";
            }
        }
        currentUniqueDialogId=id;
        g.println(TAG +" SET FORCED (REACHABLING FUCK) ID="+id+ conversations.get(id).getContent());
    }

    public void setAssociatedChoices(HashMap<String, ArrayList<Choice_Arist>> associatedChoices) {
        this.associatedChoices = associatedChoices;
    }

    public void setActions(HashMap<String, String> actions) {
        this.actions = actions;
    }

    public void setCurrentUniqueDialogId(String currentUniqueDialogId) {
        this.currentUniqueDialogId = currentUniqueDialogId;
    }

    public void setDialogGraphID(String dialogGraphID) {
        this.dialogGraphID = dialogGraphID;
    }

    public void setRootID(String rootID) {
        this.rootID = rootID;
    }

    public void setGoNode(String goNode) {
        this.goNode = goNode;
    }

    //TO STRING
    @Override
    public String toString(){
        StringBuilder outstring=new StringBuilder();
        int numberTotalChoices=0;
        Set<String> keys =associatedChoices.keySet();
        for(String id: keys) {
            outstring.append(String.format("[%s]:", id));

            for (Choice_Arist choice : associatedChoices.get(id)) {
                numberTotalChoices++;
                outstring.append(String.format(" %s", choice.getDestinyID()));
            }
            outstring.append(System.getProperty("line.separator"));
        }
            outstring.append(String.format("Number conversations: %d",conversations.size()));
            outstring.append(String.format(", Number of choices: %d",numberTotalChoices));
            outstring.append(System.getProperty("line.separator"));

            return outstring.toString();
    }
}

