package com.mygdx.safe.MainGameGraph;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;
import com.mygdx.safe.Pair;
import com.mygdx.safe.SpritemationEntityConfig;
import com.mygdx.safe.screens.MainGameScreen;


import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Pattern;



import static com.mygdx.safe.screens.MainGameScreen.ac;
import static com.mygdx.safe.screens.ProfileScren.acCharger;
import static java.lang.System.exit;
import static java.lang.System.nanoTime;

/**
 * Created by Boris.InspitatGames on 1/11/17.
 */

public class GameGraphsManager {


    private static final String TAG = GameGraphsManager.class.getSimpleName();

    com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g;
    public String[] lookScreen = new String[]{"DARK", "STANDAR", "LIGHT"};
    public static String NameFile="GameLog.txt";
    public static String NameGraphFile="scripts/GameGraphs.json";

    GameGraphs ggs;
    GameGraph currentgg;

    // GENERAL DATA
    public String completeFile;
    public float generalTime;
    private float laptick;
    public Array<String> allEvents; // CACHING ALL DATA EVENTS OF THIS GAME: (PAIR EVENT::'VALUE')
    public float distance=0;
    public int totalSelections=0;
    public int totalProximitys=0;
    public long lastmodifiedGamegraph=0;
    public boolean isNecesaryConditionsBasicCheck=true;
    public boolean comprobationResult=false;
    public HashMap<String,HashMap<String,Integer>> fourthComprobation=new HashMap<String, HashMap<String, Integer>>();

    //INSTRUCTIONS PENDING FOR OK
    private ArrayList<String> pendingOKinstructions = new ArrayList<String>();

    private HashMap<String, Float> pendingAutoTimes = new HashMap<String, Float>();

    private Array<Pair<String, Float>> autoTimeToAdd = new Array<Pair<String, Float>>();

    private boolean pendingAutoTimesIterate = false;
    private boolean ATtoAdd = false;

    // FILE MANAGING
    boolean isLocAvailable =  Gdx. files . isLocalStorageAvailable();

    FileHandle handleNameFile=null;

    boolean append;



    public GameGraphsManager(com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g){
        this.g=g;
        this.g.getMessageAccessClass().setGamegraphManager(this);

    }
    public void load(){
        // FILEHANDLING

        if(isLocAvailable){
            if( Gdx.files.isLocalStorageAvailable() ) {

            }
            handleNameFile=Gdx.files.local(NameFile);
            if(Gdx.files.internal(NameGraphFile)!=null){
                lastmodifiedGamegraph=Gdx.files.internal(NameGraphFile).lastModified();
            }

            append=handleNameFile.exists();
            boolean b=false;
            if(!append) {

                handleNameFile.writeString("#.\n#.LIVEMOTION\n" +
                                           "#.==========\n#.\n#.\n#." , append);
                append=true;
                b=true;
            }

            if(append){

                completeFile=handleNameFile.readString();
                analizingForConditionsComprobation(completeFile,lastmodifiedGamegraph);


                if(!b && !(completeFile.substring(completeFile.length()-4,completeFile.length()).contains("####"))) {
                    g.println("THE PREVIOUS GAME, DO NOT CLOSE PROPERLY");
                    handleNameFile.writeString("\n#.\n#..\n#.....THE PREVIOUS GAME, DO NOT CLOSE PROPERLY.\n", append);
                }



                Date date=new Date();
                SimpleDateFormat ft = new SimpleDateFormat("E yyyy.M.dd 'at' hh:mm:ss a zzz");
                handleNameFile.writeString("\n#.\n",append);
                handleNameFile.writeString("#.newDateGameInit::" + ft.format(date) + "\n" ,append);
                handleNameFile.writeString("#.\n",append);
                handleNameFile.writeString("#.#UDG#"+lastmodifiedGamegraph+"\n",append);
            }


        }else{
            g.print("ERROR: NOT STORAGE AVAILABLE");

        }



        // GRAPH MANAGING
        // CHARGING ALL GAMEGRAPHS
        ggs= GameGraphsCharger.GetGameGraphs(GameGraphsCharger.PATH);
        //CONFIGS ALL GAMEGRAPHS WITH TIMERS
        ggs.config(g);
        //SELECT FIRST GAMEGRAPH
        currentgg=ggs.getCurrentGameGraph();


    }

    public void update(float delta){
        // LAPTICK GAMEGRAPH CONTROL (0.1 SECOND)
        laptick+=delta;
        generalTime+=delta;

        updateAutoTimes(delta);

        if(laptick>=0.1){
             /*if(timers.size()>0) {
               for(Timer t:timers.values()){
                    t.update(delta);

            }}*/
            if(g.is_hasGraphCommand())
                receive(null, -1);
            //g.println("***********************GRAPH-TICK: ["+ laptick + "] SECONDS");
            laptick=0;

        }
    }

    public void updateAutoTimes(float delta){

        ArrayList<String> autoTimesToDelete = new ArrayList<String>();

        //Update PendingAutoTimes
        pendingAutoTimesIterate = true;
        for(String autoTime: pendingAutoTimes.keySet()){

            pendingAutoTimes.put(autoTime, pendingAutoTimes.get(autoTime) - delta);

            if(pendingAutoTimes.get(autoTime) <= 0.0){
                autoTimesToDelete.add(autoTime);
            }
        }
        pendingAutoTimesIterate = false;

        //Delete Finished AutoTimes
        for(String s: autoTimesToDelete){
            pendingAutoTimes.remove(s);
            g.sendIntructionOK(s, pendingOKinstructions, null);
        }

        //add all the autoTimes in cue
        if(ATtoAdd){

            for(int i=0; i<autoTimeToAdd.size; i++){
                pendingAutoTimes.put("" + autoTimeToAdd.get(i).getFirst(), autoTimeToAdd.get(i).getSecond() + 0);

                if(g.findInstruction(autoTimeToAdd.get(i).getFirst(), pendingOKinstructions)){

                    g.removeInstructionType(autoTimeToAdd.get(i).getFirst(),pendingOKinstructions,null);
                }

                pendingOKinstructions.add(autoTimeToAdd.get(i).getFirst());
            }

            ATtoAdd = false;
        }
    }


    public void receive(String treeID, int treeNumNode) {

        String TreeID = "" + treeID;
        int TreeNumNode = 0 + treeNumNode;

        while (g.gm.getMessageQueue().size > 0) {
            String str = g.gm.getFirst();
            String m[] = str.split("#"); //M IS MESSAGE
            String pointer = "";


            if (m.length > 1) {
                if (m[0].equalsIgnoreCase("GAMEGRAPH") || m[0].equalsIgnoreCase("GGRAPH")) {
                    // PROCESS GAMEGRAPH INSTRUCTION
                    if (m[1].equalsIgnoreCase("OK") && treeNumNode != -1 && treeID !=null) {

                        int parentTreeNumNode= g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).getNode(TreeNumNode).getParentNode().numNode;
                        String parentTypeName = g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).getNode(parentTreeNumNode).getName();
                        SpecialSuperTree.TreeNode parent = g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).getNode(TreeNumNode).getParentNode();
                        SpecialSuperTree.TreeNode currentNode = g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).getNode(TreeNumNode);

                        g.println(TAG + "  RECEIVE:  TREE EXECUTION:  " + treeID + "  --  CHECK PARENT:  " + parentTypeName);


                        if (parentTypeName.contains("SEQUENCE")) {

                            boolean find = false;
                            int childIndexForExecute = -1;

                            for (int i = 0; i < parent.getChildNodes().size && !find; i++) {
                                if (!parent.getChild(i).getData()) {
                                    find = true;
                                    childIndexForExecute = i;
                                }
                            }
                            // SEQUENCE EXCLUSIVE CONTROLLER: THE OK EXECUTED NUMBER CHILD NEVER IS GREATER OVER NOT EXECUTED NUMBER CHILD

                            if(!find){
                                g.gm.sendMessage(g.messageOK(TreeID, parentTreeNumNode), TreeID, parentTreeNumNode);
                            }
                            else{

                                boolean previousChildsExecuted = true;

                                for(int i=childIndexForExecute-1; i>-1 && previousChildsExecuted; i--){

                                    g.println(TAG + " SEQUENCE EXCLUSIVE CONTROLLER: CHILD FOR EXECUTE: " + i + "  PREVIOUSCHILDEXECUTED:  " + previousChildsExecuted + "  CHILINDEXFOR EXECUTE:  " + childIndexForExecute);

                                    if(parent.getChildNodes().get(i).data == false) previousChildsExecuted = false;
                                }

                                if(previousChildsExecuted){

                                    g.println(TAG + "  PREVIOUSCHILDSEXECUTED=TRUE:  FIND CHILD SEQUENCE:");

                                    String seqName = parent.getName() + "#" + treeID.split("%")[1];
                                    SequenceInstructions sequence = g.m.ggMgr.currentgg._sequences.get(seqName);

                                    sequence.nextInstruction(childIndexForExecute);
                                }
                            }

                            /*if (!find) {
                                g.gm.sendMessage(g.messageOK(TreeID, parentTreeNumNode), TreeID, parentTreeNumNode);
                            }
                            else{
                                String seqName = parent.getName() + "#" + TreeID.split("%")[1];
                                SequenceInstructions sequence = g.m.ggMgr.currentgg._sequences.get(seqName);

                                sequence.nextInstruction(childIndexForExecute);
                            }*/



                        } else if (parentTypeName.contains("DIRECT") || parentTypeName.contains("TIMER") ||
                                (parent.getName().contains("COUNTER") && parent.getName().contains("#")) || parentTypeName.contains("SHOOTER")) {

                            boolean find = false;
                            for (int i = 0; i < parent.getChildNodes().size && !find; i++) {
                                if (! parent.getChild(i).getData()) {
                                    find = true; //childIndexForExecute = i;
                                }
                            }

                            if (!find) {

                                if(parentTypeName.contains("SHOOTER")) g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).resetTree(true);//INSTRUCTIONS PENDING FOR OK
                                else g.gm.sendMessage(g.messageOK(TreeID, parentTreeNumNode), TreeID, parentTreeNumNode);
                            }
                        } else if (parentTypeName.contains("EVALUATOR") || (parentTypeName.contains("RANDOM"))) {

                            boolean find = false;

                            for (int i = 0; i < parent.getChildNodes().size && !find; i++) {
                                if (parent.getChild(i).getData()) {
                                    find = true;
                                }
                            }
                            if (find) {
                                g.gm.sendMessage(g.messageOK(TreeID, parentTreeNumNode), TreeID, parentTreeNumNode);
                            }
                        } else if (parent.getName().contains("COUNTER") && (!parent.getName().contains("#"))) {
                            boolean find = false;
                            for (int i = 0; i < parent.getChildNodes().size && !find; i++) {
                                if (!parent.getChild(i).getData() && parent.getChild(i).isToExecute()) {
                                    find = true; //childIndexForExecute = i;
                                }
                            }
                            if (!find) {
                                g.gm.sendMessage(g.messageOK(TreeID, parentTreeNumNode), TreeID, parentTreeNumNode);
                            }
                        } else if(parent.getName().contains("CHOICER")){

                            boolean find = false;
                            com.mygdx.safe.MainGameGraph.ToActionNode currentNodeGraph = g.m.ggMgr.currentgg.getNodeById(TreeID.split("%")[1]);
                            Choicer choicer = currentNodeGraph.getConditions()._choicers.get(parent.getName());
                            int childToExecute = -1;

                            if(g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).getNode(TreeNumNode).name.equalsIgnoreCase(choicer.getInstruction())){
                                for(int i =1 ; i<parent.getChildNodes().size && childToExecute==-1; i++){
                                    if(parent.getChildNodes().get(i).isToExecute()) childToExecute = i;
                                }
                            }

                            if(childToExecute == -1) g.gm.sendMessage(g.messageOK(TreeID, parentTreeNumNode), TreeID, parentTreeNumNode);
                            else choicer.instructionSequenceExecutor(choicer.getAc(), childToExecute);

                        }


                        //check active sequences ---and next instruction

                    }
                    else if(m.length == 3 && m[1].equalsIgnoreCase("RESET_COUNTER")){
                        ToActionNode n=currentgg.getNodeById(m[2]);
                        if(n!=null){
                            int prev=n._visitCounter;
                            n._visitCounter=0;
                            g.println("\n"+ TAG +" RE-SETTING THE NODE : ["+ n.getID()+ "] COUNTER (PREV)="+prev+" COUNTER NOW!= " + n._visitCounter +"\n");
                        }else{
                            g.println(TAG + " ERROR!!!!!! NOT RESET. NODE ["+ m[2]+ "] NOT FOUND");
                        }

                        g.gm.sendMessage(g.messageOK(TreeID, TreeNumNode), TreeID, TreeNumNode);

                    }
                    else if(m.length == 4 && m[1].equalsIgnoreCase("RESET_COUNTER")){
                        ToActionNode n=currentgg.getNodeById(m[2]);
                        if(n!=null){
                            String[] shooter_transform=m[3].split(Pattern.quote("*"));
                            String newShooter=shooter_transform[0];
                            if(shooter_transform.length>1){
                                for(int i=1;i<shooter_transform.length;i++){
                                    newShooter+="#"+shooter_transform[i];
                                }
                            }
                            if(n.getConditions()._shooters.get(newShooter)!=null){
                                int prev=n.getConditions()._shooters.get(newShooter).getFirst();
                                n.getConditions()._shooters.get(newShooter).setFirst(0);
                                g.println("\n"+ TAG +" RE-SETTING THE NODE : ["+ n.getID()+ "] COUNTER SHOOTER (PREV)="+prev+" COUNTER SHOOTER NOW!= "+ newShooter + " = " +  n.getConditions()._shooters.get(newShooter).getFirst() + "\n");

                            }else{
                                g.println(TAG + " ERROR!!!!!! NOT RESET. SHOOTER ["+ newShooter+ "] NOT FOUND");
                            }
                        }else{
                            g.println(TAG + " ERROR!!!!!! NOT RESET. NODE ["+ m[2]+ "] NOT FOUND");

                        }

                        g.gm.sendMessage(g.messageOK(TreeID, TreeNumNode), TreeID, TreeNumNode);

                    }

                    else if (m.length > 0 && (m[1].equalsIgnoreCase("GO") || m[1].equalsIgnoreCase("LOAD"))) {

                        ActionChoice actionchoice = new ActionChoice();
                        int indexForShotter = 3;

                        actionchoice.setDestinyID(m[2]);

                        if(m.length> 3){
                            if (m[3].equalsIgnoreCase("FROM")) {
                                actionchoice.setOriginID(m[4]);
                                indexForShotter = 5;
                            } else actionchoice.setOriginID(currentgg.getCurrentNodeId());
                        }

                        if (m.length > indexForShotter) {
                            if (m[indexForShotter].contains("SHOOTER")) {
                                String shooter = g.StringMaker(m, indexForShotter, m.length);
                                g.gm.sendMessage(g.messageOK(TreeID, TreeNumNode), TreeID, TreeNumNode);
                                currentgg.exec(actionchoice, shooter, null);
                            }
                        } else {
                            g.gm.sendMessage(g.messageOK(TreeID, TreeNumNode), TreeID, TreeNumNode);
                            currentgg.exec(actionchoice, "SHOOTER#GO", null);
                        }




                    }else if(m.length > 0 && m[1].equalsIgnoreCase("EXECUTE")){

                        ActionChoice actionchoice = new ActionChoice();
                        int indexForShotter = 3;

                        actionchoice.setDestinyID(m[2]);

                        if (m.length > indexForShotter) {
                            if (m[indexForShotter].contains("SHOOTER")) {
                                String shooter = g.StringMaker(m, indexForShotter, m.length);
                                pendingOKinstructions.add(str + "@" + treeID + "@" + treeNumNode);
                                currentgg.exec(actionchoice, shooter, null);
                            }
                        }

                    }
                    else if (m.length > 0 && m[1].equalsIgnoreCase("BLOCK")) {
                        for (int i = 2; i < m.length; i++)
                            currentgg.getNodeById(m[i])._blocked = true;
                        g.gm.sendMessage(g.messageOK(TreeID, TreeNumNode), TreeID, TreeNumNode);

                    } else if (m.length > 0 && m[1].equalsIgnoreCase("UNBLOCK")) {
                        for (int i = 2; i < m.length; i++)
                            currentgg.getNodeById(m[i])._blocked = false;
                        g.gm.sendMessage(g.messageOK(TreeID, TreeNumNode), TreeID, TreeNumNode);

                    }

                    //unchecked
                    else if (m.length > 0 && m[1].equalsIgnoreCase("PROXIMITY")) {
                        g.println(m[1] + "#" + m[2] + "#" + m[3] + "#" + m[4]);

                        if (m.length > 1 && (m[3].equalsIgnoreCase("START") || m[3].equalsIgnoreCase("STOP"))) {
                            if (m.length > 3 && m[4].equalsIgnoreCase("ENTITY")) {
                                pointer = m[1] + "#" + m[4] + "#" + m[5];
                                currentgg.exec(currentgg.pointers.get(pointer), "SHOOTER#" + m[1] + "#" + m[2] + "#" + m[3], pointer); //EX: PROXIMITY#ENTITY#RATDOG_00, SHOOTER#PROXIMITY#START
                            }
                        }
                    }


                    //EXECUTE NODE AFTER SPRITEMATION LISTENER EVENT
                    else if (m.length > 0 && m[1].equalsIgnoreCase("LISTENER_EVENT")) {
                        g.println(m[1] + "#" + m[2] + "#" + m[3] + "#" + m[4]);
                        if (m.length > 1 && m[2].equalsIgnoreCase("SPRITEMATION")) {
                            pointer = m[1] + "#" + m[2] + "#" + m[3];
                            currentgg.exec(currentgg.pointers.get(pointer), "SHOOTER#" + m[4], pointer); //EX: LISTENER_EVENT#SPRITEMATION#TAPE, SHOOTER#TOUCH_UP
                        }
                    } else if (m.length > 0 && m[1].equalsIgnoreCase("INTERSECT")) {
                        if (m.length > 1 && m[2].equalsIgnoreCase("START")) {
                            // NONE AT BETA

                        } else if (m.length > 1 && m[2].equalsIgnoreCase("STOP")) {
                            // NONE AT BETA
                        }

                    } else if (m.length > 0 && m[1].equalsIgnoreCase("SELECT")) {
                        if(m.length > 3 && m[2].equalsIgnoreCase("ACTION-CONECTOR-CHECKER")){
                            pointer = m[1] + "#" + m[2];
                            currentgg.exec(currentgg.pointers.get(pointer), "SHOOTER#CHECK#SELECT#ENTITY#" + m[3], pointer);
                        }else if (m.length > 3 && (m[2].equalsIgnoreCase("START") || m[2].equalsIgnoreCase("STOP"))) {
                            if (m[3].equalsIgnoreCase("ENTITY")) {
                                pointer = m[1] + "#" + m[3] + "#" + m[4]; // EX: SELECT#ENTITY#HOTDOGUER_00,SHOOTER#SELECT#STOP
                                g.println("EXECUTING:" + pointer);
                                currentgg.exec(currentgg.pointers.get(pointer), "SHOOTER#" + m[1] + "#" + m[2], pointer);
                            }
                        } else if (m.length > 2 && m[2].equalsIgnoreCase("ENTITY")) {
                            pointer = m[1] + "#" + m[2] + "#" + m[3]; // EX: SELECT#ITEM#BAR,SHOOTER#SELECT
                            g.println("EXECUTING:" + pointer);
                            currentgg.exec(currentgg.pointers.get(pointer), "SHOOTER#" + m[1], pointer);
                        } else if (m.length > 3 && m[2].equalsIgnoreCase("ACTON")) {
                            pointer = m[1] + "#" + m[2] + "#" + m[3] + "#" + m[4]; // EX: SELECT#ACTON#GUARD_00#HIT,SHOOTER#SELECT
                            g.println("EXECUTING:" + pointer);
                            currentgg.exec(currentgg.pointers.get(pointer), "SHOOTER#" + m[1], pointer);
                        }/* else if (m.length > 2 && m[2].equalsIgnoreCase("HOTBAR")) {
                            if (g.m.invUI.get_hotBar().hasItem()) {
                                // HOTBAR ITEM ANIMATION
                                float x = g.m.invUI.get_hotBar().get_slotItem().getOriginX();
                                float y = g.m.invUI.get_hotBar().get_slotItem().getOriginX();
                                g.m.invUI.get_hotBar().get_slotItem().setOrigin(g.m.invUI.get_hotBar().get_slotItem().getWidth() / 2,
                                        g.m.invUI.get_hotBar().get_slotItem().getHeight() / 2);
                                g.m.getHe().MonoScaleImagetRotationRapidAndNormal(g.m.invUI.get_hotBar().get_slotItem(), 1, 1, 0.7f);
                            }
                            pointer = m[1] + "#" + m[2];
                            g.println("EXECUTING:" + pointer);
                            currentgg.exec(currentgg.pointers.get(pointer), "SHOOTER#" + m[1], pointer);
                        }*/


                    } /*else if (m.length > 1 && m[1].equalsIgnoreCase("MOVEPROG")) {
                        if(m[2].equalsIgnoreCase("OK")){
                            currentgg.nextMoveProg(true);
                        }else if (m[2].equalsIgnoreCase("FAIL")) {
                            if(!currentgg.currentMoveProg.getSecond().getSecond().getFirst()){
                                currentgg.nextMoveProg(false);
                            }else{
                                currentgg.finishMoveProg(false);
                            }
                        }
                        //PROCESS MOVEPROG NEXT INSTRUCTION
                    }*/ else if (m.length > 0 && m[1].equalsIgnoreCase("CHANGECAMERA")) {
                        //NONE AT BETA // NOT AT GRAPH
                    } else if (m.length > 0 && m[1].equalsIgnoreCase("CHANGEDIRECTION")) {
                        // NOT AT GRAPH
                    } else if (m.length > 0 && m[1].equalsIgnoreCase("CHANGESTATE")) {
                        // NOT AT GRAPH
                    } else if (m.length > 0 && m[1].equalsIgnoreCase("AUTOTIME")) {

                        if(pendingAutoTimesIterate){
                            autoTimeToAdd.add(new Pair<String,
                                    Float>(str + "@" + treeID + "@" + treeNumNode, Float.valueOf(m[3])));

                            ATtoAdd = true;
                        }else{
                            pendingAutoTimes.put(str + "@" + treeID + "@" + treeNumNode, Float.valueOf(m[3]));

                            if(g.findInstruction(str + "@" + treeID + "@" + treeNumNode, pendingOKinstructions)){

                                g.removeInstructionType(str + "@" + treeID + "@" + treeNumNode,pendingOKinstructions,null);
                            }

                            pendingOKinstructions.add(str + "@" + treeID + "@" + treeNumNode);
                        }

                    } else if (m[1].equalsIgnoreCase("GPRINT") && m.length == 3) {

                        // M2 <---VAR  !!!!
                        String m2="";
                        ToActionNode nm2=null;
                        Conditions cm2=null;
                        String[] prevarm2=m[2].split("!");

                        if(m[2].contains("VAR")) {
                            if (prevarm2.length == 1) {
                                m2 = m[2];
                            } else if (prevarm2.length == 2) {
                                m2 = prevarm2[0];
                                nm2 = g.m.ggMgr.currentgg.getNodeById(prevarm2[1]);
                            } else {
                                g.println(TAG + " !!!!!!!!!!!!!!!! VAR ERROR: MALFORMED VAR M2 !!!!!!!!!!!");
                            }

                            if (nm2 == null) {
                                cm2 = g.m.ggMgr.currentgg.getNodeById(String.valueOf(treeID.split("%")[1])).getConditions();
                            } else {
                                cm2 = nm2.getConditions();
                            }
                            g.println(" GAMEGRAPH GPRINT MESSAGE: VAR["+m2+"]["+cm2._varMap.get(m2).getFirst() +"] [TYPE ="+cm2._varMap.get(m2).getSecond()+"]");
                            g.printlns(" GAMEGRAPH GPRINT MESSAGE: VAR["+m2+"]["+cm2._varMap.get(m2).getFirst() +"] [TYPE ="+cm2._varMap.get(m2).getSecond()+"]");

                        }else{
                            g.println(" GAMEGRAPH GPRINT MESSAGE : [" + m[1] +"]");
                            g.printlns(" GAMEGRAPH GPRINT MESSAGE : [" + m[1] +"]");


                        }

                        g.gm.sendMessage(g.messageOK(TreeID, TreeNumNode), TreeID, TreeNumNode);


                        // NOT AT GRAPH
                    } else if (m[1].equalsIgnoreCase("ASIGN_VAR_VALUE")){

                        if(m[3].equalsIgnoreCase("DISTANCE") && m.length==6){

                            // M2 <---VAR  !!!!
                            String m2="";
                            ToActionNode nm2=null;
                            Conditions cm2=null;
                            String[] prevarm2=m[2].split("!");

                            if(m[2].contains("VAR")) {
                                if (prevarm2.length == 1) {
                                    m2 = m[2];
                                } else if (prevarm2.length == 2) {
                                    m2 = prevarm2[0];
                                    nm2 = g.m.ggMgr.currentgg.getNodeById(prevarm2[1]);
                                } else {
                                    g.println(TAG + " !!!!!!!!!!!!!!!! VAR ERROR: MALFORMED VAR M2 !!!!!!!!!!!");
                                }

                                if (nm2 == null) {
                                    cm2 = g.m.ggMgr.currentgg.getNodeById(String.valueOf(treeID.split("%")[1])).getConditions();
                                } else {
                                    cm2 = nm2.getConditions();
                                }

                                // M4
                                String m4 = "";
                                ToActionNode nm4 = null;
                                Conditions cm4 = null;
                                String[] prevarm4 = m[4].split("!");
                                float numm4 = 0;

                                if (m[4].contains("VAR")) {
                                    if (prevarm4.length == 1) {
                                        m4 = m[4];
                                    } else if (prevarm4.length == 2) {
                                        m4 = prevarm4[0];
                                        nm4 = g.m.ggMgr.currentgg.getNodeById(prevarm4[1]);
                                    } else {
                                        g.println(TAG + " !!!!!!!!!!!!!!!! VAR ERROR: MALFORMED VAR M4 !!!!!!!!!!!");

                                    }
                                    if (nm4 == null) {
                                        cm4 = g.m.ggMgr.currentgg.getNodeById(String.valueOf(treeID.split("%")[1])).getConditions();
                                    } else {
                                        cm4 = nm4.getConditions();
                                    }
                                    numm4 = Float.valueOf(cm4._varMap.get(m4).getFirst());
                                } else numm4 = Float.valueOf(m4);

                                // M5
                                String m5 = "";
                                ToActionNode nm5 = null;
                                Conditions cm5 = null;
                                String[] prevarm5 = m[5].split("!");
                                float numm5 = 0;

                                if (m[5].contains("VAR")) {
                                    if (prevarm5.length == 1) {
                                        m5 = m[5];

                                    } else if (prevarm5.length == 2) {
                                        m5 = prevarm5[0];
                                        nm5 = g.m.ggMgr.currentgg.getNodeById(prevarm5[1]);
                                    } else {
                                        g.println(TAG + " !!!!!!!!!!!!!!!! VAR ERROR: MALFORMED VAR M5 !!!!!!!!!!!");
                                    }
                                    if (nm5 == null) {
                                        cm5 = g.m.ggMgr.currentgg.getNodeById(String.valueOf(treeID.split("%")[1])).getConditions();
                                    } else {
                                        cm5 = nm5.getConditions();
                                    }
                                    numm5 = Float.valueOf(cm5._varMap.get(m5).getFirst());
                                } else numm5 = Float.valueOf(m5);

                                // CALC
                                float distance = Math.abs(numm4 - numm5);

                                // ASIGN TO M2
                                cm2._varMap.get(m2).setFirst(String.valueOf(distance));

                            }else{
                                g.println(TAG + " !!!!!!!!!!! FATAL ERROR: M2 IS NOT A VAR !!!!!!! : NOT ASIGNMENT, ... (OK) RETURNED ");
                            }


                    }
                        else if(m[3].equalsIgnoreCase("ADITION") && m.length==6){


                            // M2 <---VAR  !!!!
                            String m2="";
                            ToActionNode nm2=null;
                            Conditions cm2=null;
                            String[] prevarm2=m[2].split("!");
                            String typem2="";

                            if(m[2].contains("VAR")) {
                                if (prevarm2.length == 1) {
                                    m2 = m[2];
                                } else if (prevarm2.length == 2) {
                                    m2 = prevarm2[0];
                                    nm2 = g.m.ggMgr.currentgg.getNodeById(prevarm2[1]);
                                } else {
                                    g.println(TAG + " !!!!!!!!!!!!!!!! VAR ERROR: MALFORMED VAR M2 !!!!!!!!!!!");
                                }

                                if (nm2 == null) {
                                    cm2 = g.m.ggMgr.currentgg.getNodeById(String.valueOf(treeID.split("%")[1])).getConditions();
                                } else {
                                    cm2 = nm2.getConditions();
                                }
                                typem2= cm2._varMap.get(m2).getSecond();

                                // M4
                                String m4 = "";
                                ToActionNode nm4 = null;
                                Conditions cm4 = null;
                                String[] prevarm4 = m[4].split("!");
                                float numm4 = 0;
                                String numm4aux="";
                                String typenumm4="";

                                if (m[4].contains("VAR")) {
                                    if (prevarm4.length == 1) {
                                        m4 = m[4];
                                    } else if (prevarm4.length == 2) {
                                        m4 = prevarm4[0];
                                        nm4 = g.m.ggMgr.currentgg.getNodeById(prevarm4[1]);
                                    } else {
                                        g.println(TAG + " !!!!!!!!!!!!!!!! VAR ERROR: MALFORMED VAR M4 !!!!!!!!!!!");

                                    }
                                    if (nm4 == null) {
                                        cm4 = g.m.ggMgr.currentgg.getNodeById(String.valueOf(treeID.split("%")[1])).getConditions();
                                    } else {
                                        cm4 = nm4.getConditions();
                                    }

                                    typenumm4= cm4._varMap.get(m4).getSecond();
                                    if(typenumm4.equalsIgnoreCase("Float") || typenumm4.equalsIgnoreCase("Integer")){
                                        numm4 = Float.valueOf(cm4._varMap.get(m4).getFirst());
                                    }else{
                                        numm4aux = cm4._varMap.get(m4).getFirst();
                                    }

                                } else {
                                    m4=m[4];
                                    typenumm4=typem2;
                                    if(typenumm4.equalsIgnoreCase("Float") || typenumm4.equalsIgnoreCase("Integer")) {
                                        numm4 = Float.valueOf(m4);
                                    }else{
                                        numm4aux = m4;
                                    }
                                }

                                // M5
                                String m5 = "";
                                ToActionNode nm5 = null;
                                Conditions cm5 = null;
                                String[] prevarm5 = m[5].split("!");
                                float numm5 = 0;
                                String numm5aux="";
                                String typenumm5="";

                                if (m[5].contains("VAR")) {
                                    if (prevarm5.length == 1) {
                                        m5 = m[5];

                                    } else if (prevarm5.length == 2) {
                                        m5 = prevarm5[0];
                                        nm5 = g.m.ggMgr.currentgg.getNodeById(prevarm5[1]);
                                    } else {
                                        g.println(TAG + " !!!!!!!!!!!!!!!! VAR ERROR: MALFORMED VAR M5 !!!!!!!!!!!");
                                    }
                                    if (nm5 == null) {
                                        cm5 = g.m.ggMgr.currentgg.getNodeById(String.valueOf(treeID.split("%")[1])).getConditions();
                                    } else {
                                        cm5 = nm5.getConditions();
                                    }

                                    typenumm5= cm5._varMap.get(m5).getSecond();
                                    if(typenumm5.equalsIgnoreCase("Float") || typenumm5.equalsIgnoreCase("Integer")){
                                        numm5 = Float.valueOf(cm5._varMap.get(m5).getFirst());
                                    }else{
                                        numm5aux = cm5._varMap.get(m5).getFirst();
                                    }
                                } else {
                                    m5=m[5];
                                    typenumm5=typem2;
                                    if(typenumm5.equalsIgnoreCase("Float") || typenumm5.equalsIgnoreCase("Integer")) {
                                        numm5 = Float.valueOf(m5);
                                    }else{
                                        numm5aux = m5;
                                    }

                                }

                                // CALC && ASIGN TO M2

                                if(typem2.equalsIgnoreCase("Float") || typem2.equalsIgnoreCase("Integer")){

                                    float sum = numm4 + numm5;
                                    // ASIGN TO M2
                                    cm2._varMap.get(m2).setFirst(String.valueOf(sum));
                                }else{
                                    String sum = numm4aux + numm5aux;
                                    // ASIGN TO M2
                                    cm2._varMap.get(m2).setFirst(sum);
                                }


                            }else{
                                g.println(TAG + " !!!!!!!!!!! FATAL ERROR: M2 IS NOT A VAR !!!!!!! : NOT ASIGNMENT, ... (OK) RETURNED ");
                            }



                        }
                        else if(m[3].equalsIgnoreCase("DIFERENCE") && m.length==6){

                            // M2 <---VAR  !!!!
                            String m2="";
                            ToActionNode nm2=null;
                            Conditions cm2=null;
                            String[] prevarm2=m[2].split("!");
                            String typem2="";

                            if(m[2].contains("VAR")) {
                                if (prevarm2.length == 1) {
                                    m2 = m[2];
                                } else if (prevarm2.length == 2) {
                                    m2 = prevarm2[0];
                                    nm2 = g.m.ggMgr.currentgg.getNodeById(prevarm2[1]);
                                } else {
                                    g.println(TAG + " !!!!!!!!!!!!!!!! VAR ERROR: MALFORMED VAR M2 !!!!!!!!!!!");
                                }

                                if (nm2 == null) {
                                    cm2 = g.m.ggMgr.currentgg.getNodeById(String.valueOf(treeID.split("%")[1])).getConditions();
                                } else {
                                    cm2 = nm2.getConditions();
                                }
                                typem2= cm2._varMap.get(m2).getSecond();

                                // M4
                                String m4 = "";
                                ToActionNode nm4 = null;
                                Conditions cm4 = null;
                                String[] prevarm4 = m[4].split("!");
                                float numm4 = 0;
                                String numm4aux="";
                                String typenumm4="";

                                if (m[4].contains("VAR")) {
                                    if (prevarm4.length == 1) {
                                        m4 = m[4];
                                    } else if (prevarm4.length == 2) {
                                        m4 = prevarm4[0];
                                        nm4 = g.m.ggMgr.currentgg.getNodeById(prevarm4[1]);
                                    } else {
                                        g.println(TAG + " !!!!!!!!!!!!!!!! VAR ERROR: MALFORMED VAR M4 !!!!!!!!!!!");

                                    }
                                    if (nm4 == null) {
                                        cm4 = g.m.ggMgr.currentgg.getNodeById(String.valueOf(treeID.split("%")[1])).getConditions();
                                    } else {
                                        cm4 = nm4.getConditions();
                                    }

                                    typenumm4= cm4._varMap.get(m4).getSecond();
                                    if(typenumm4.equalsIgnoreCase("Float") || typenumm4.equalsIgnoreCase("Integer")){
                                        numm4 = Float.valueOf(cm4._varMap.get(m4).getFirst());
                                    }else{
                                        numm4aux = cm4._varMap.get(m4).getFirst();
                                    }

                                } else {
                                    m4=m[4];
                                    typenumm4=typem2;
                                    if(typenumm4.equalsIgnoreCase("Float") || typenumm4.equalsIgnoreCase("Integer")) {
                                        numm4 = Float.valueOf(m4);
                                    }else{
                                        numm4aux = m4;
                                    }
                                }

                                // M5
                                String m5 = "";
                                ToActionNode nm5 = null;
                                Conditions cm5 = null;
                                String[] prevarm5 = m[5].split("!");
                                float numm5 = 0;
                                String numm5aux="";
                                String typenumm5="";

                                if (m[5].contains("VAR")) {
                                    if (prevarm5.length == 1) {
                                        m5 = m[5];

                                    } else if (prevarm5.length == 2) {
                                        m5 = prevarm5[0];
                                        nm5 = g.m.ggMgr.currentgg.getNodeById(prevarm5[1]);
                                    } else {
                                        g.println(TAG + " !!!!!!!!!!!!!!!! VAR ERROR: MALFORMED VAR M5 !!!!!!!!!!!");
                                    }
                                    if (nm5 == null) {
                                        cm5 = g.m.ggMgr.currentgg.getNodeById(String.valueOf(treeID.split("%")[1])).getConditions();
                                    } else {
                                        cm5 = nm5.getConditions();
                                    }

                                    typenumm5= cm5._varMap.get(m5).getSecond();
                                    if(typenumm5.equalsIgnoreCase("Float") || typenumm5.equalsIgnoreCase("Integer")){
                                        numm5 = Float.valueOf(cm5._varMap.get(m5).getFirst());
                                    }else{
                                        numm5aux = cm5._varMap.get(m5).getFirst();
                                    }
                                } else {
                                    m5=m[5];
                                    typenumm5=typem2;
                                    if(typenumm5.equalsIgnoreCase("Float") || typenumm5.equalsIgnoreCase("Integer")) {
                                        numm5 = Float.valueOf(m5);
                                    }else{
                                        numm5aux = m5;
                                    }

                                }

                                // CALC && ASIGN TO M2

                                if(typem2.equalsIgnoreCase("Float") || typem2.equalsIgnoreCase("Integer")){

                                    float dif = numm4 - numm5;
                                    // ASIGN TO M2
                                    cm2._varMap.get(m2).setFirst(String.valueOf(dif));
                                }else{

                                    g.println(TAG + " ERROR: NOT DIFERENCE CALCULATE AT STRINGS");
                                }


                            }else{
                                g.println(TAG + " !!!!!!!!!!! FATAL ERROR: M2 IS NOT A VAR !!!!!!! : NOT ASIGNMENT, ... (OK) RETURNED ");
                            }
                        }
                        else if(m[3].equalsIgnoreCase("MULTIPLICATION") && m.length==6){

                            // M2 <---VAR  !!!!
                            String m2="";
                            ToActionNode nm2=null;
                            Conditions cm2=null;
                            String[] prevarm2=m[2].split("!");
                            String typem2="";

                            if(m[2].contains("VAR")) {
                                if (prevarm2.length == 1) {
                                    m2 = m[2];
                                } else if (prevarm2.length == 2) {
                                    m2 = prevarm2[0];
                                    nm2 = g.m.ggMgr.currentgg.getNodeById(prevarm2[1]);
                                } else {
                                    g.println(TAG + " !!!!!!!!!!!!!!!! VAR ERROR: MALFORMED VAR M2 !!!!!!!!!!!");
                                }

                                if (nm2 == null) {
                                    cm2 = g.m.ggMgr.currentgg.getNodeById(String.valueOf(treeID.split("%")[1])).getConditions();
                                } else {
                                    cm2 = nm2.getConditions();
                                }
                                typem2= cm2._varMap.get(m2).getSecond();

                                // M4
                                String m4 = "";
                                ToActionNode nm4 = null;
                                Conditions cm4 = null;
                                String[] prevarm4 = m[4].split("!");
                                float numm4 = 0;
                                String numm4aux="";
                                String typenumm4="";

                                if (m[4].contains("VAR")) {
                                    if (prevarm4.length == 1) {
                                        m4 = m[4];
                                    } else if (prevarm4.length == 2) {
                                        m4 = prevarm4[0];
                                        nm4 = g.m.ggMgr.currentgg.getNodeById(prevarm4[1]);
                                    } else {
                                        g.println(TAG + " !!!!!!!!!!!!!!!! VAR ERROR: MALFORMED VAR M4 !!!!!!!!!!!");

                                    }
                                    if (nm4 == null) {
                                        cm4 = g.m.ggMgr.currentgg.getNodeById(String.valueOf(treeID.split("%")[1])).getConditions();
                                    } else {
                                        cm4 = nm4.getConditions();
                                    }

                                    typenumm4= cm4._varMap.get(m4).getSecond();
                                    if(typenumm4.equalsIgnoreCase("Float") || typenumm4.equalsIgnoreCase("Integer")){
                                        numm4 = Float.valueOf(cm4._varMap.get(m4).getFirst());
                                    }else{
                                        numm4aux = cm4._varMap.get(m4).getFirst();
                                    }

                                } else {
                                    m4=m[4];
                                    typenumm4=typem2;
                                    if(typenumm4.equalsIgnoreCase("Float") || typenumm4.equalsIgnoreCase("Integer")) {
                                        numm4 = Float.valueOf(m4);
                                    }else{
                                        numm4aux = m4;
                                    }
                                }

                                // M5
                                String m5 = "";
                                ToActionNode nm5 = null;
                                Conditions cm5 = null;
                                String[] prevarm5 = m[5].split("!");
                                float numm5 = 0;
                                String numm5aux="";
                                String typenumm5="";

                                if (m[5].contains("VAR")) {
                                    if (prevarm5.length == 1) {
                                        m5 = m[5];

                                    } else if (prevarm5.length == 2) {
                                        m5 = prevarm5[0];
                                        nm5 = g.m.ggMgr.currentgg.getNodeById(prevarm5[1]);
                                    } else {
                                        g.println(TAG + " !!!!!!!!!!!!!!!! VAR ERROR: MALFORMED VAR M5 !!!!!!!!!!!");
                                    }
                                    if (nm5 == null) {
                                        cm5 = g.m.ggMgr.currentgg.getNodeById(String.valueOf(treeID.split("%")[1])).getConditions();
                                    } else {
                                        cm5 = nm5.getConditions();
                                    }

                                    typenumm5= cm5._varMap.get(m5).getSecond();
                                    if(typenumm5.equalsIgnoreCase("Float") || typenumm5.equalsIgnoreCase("Integer")){
                                        numm5 = Float.valueOf(cm5._varMap.get(m5).getFirst());




                                    }else{
                                        numm5aux = cm5._varMap.get(m5).getFirst();
                                    }
                                } else {
                                    m5=m[5];
                                    typenumm5=typem2;
                                    if(typenumm5.equalsIgnoreCase("Float") || typenumm5.equalsIgnoreCase("Integer")) {
                                        numm5 = Float.valueOf(m5);
                                    }else{
                                        numm5aux = m5;
                                    }

                                }

                                // CALC && ASIGN TO M2

                                if(typem2.equalsIgnoreCase("Float") || typem2.equalsIgnoreCase("Integer")){

                                    float mul = numm4 * numm5;
                                    // ASIGN TO M2
                                    cm2._varMap.get(m2).setFirst(String.valueOf(mul));
                                }else{

                                    g.println(TAG + " ERROR: NOT MULTIPLICATION CALCULATE AT STRINGS");
                                }


                            }else{
                                g.println(TAG + " !!!!!!!!!!! FATAL ERROR: M2 IS NOT A VAR !!!!!!! : NOT ASIGNMENT, ... (OK) RETURNED ");
                            }
                        }
                        else if(m[3].equalsIgnoreCase("DIVISION") && m.length==6){

                            // M2 <---VAR  !!!!
                            String m2="";
                            ToActionNode nm2=null;
                            Conditions cm2=null;
                            String[] prevarm2=m[2].split("!");
                            String typem2="";

                            if(m[2].contains("VAR")) {
                                if (prevarm2.length == 1) {
                                    m2 = m[2];
                                } else if (prevarm2.length == 2) {
                                    m2 = prevarm2[0];
                                    nm2 = g.m.ggMgr.currentgg.getNodeById(prevarm2[1]);
                                } else {
                                    g.println(TAG + " !!!!!!!!!!!!!!!! VAR ERROR: MALFORMED VAR M2 !!!!!!!!!!!");
                                }

                                if (nm2 == null) {
                                    cm2 = g.m.ggMgr.currentgg.getNodeById(String.valueOf(treeID.split("%")[1])).getConditions();
                                } else {
                                    cm2 = nm2.getConditions();
                                }
                                typem2= cm2._varMap.get(m2).getSecond();

                                // M4
                                String m4 = "";
                                ToActionNode nm4 = null;
                                Conditions cm4 = null;
                                String[] prevarm4 = m[4].split("!");
                                float numm4 = 0;
                                String numm4aux="";
                                String typenumm4="";

                                if (m[4].contains("VAR")) {
                                    if (prevarm4.length == 1) {
                                        m4 = m[4];
                                    } else if (prevarm4.length == 2) {
                                        m4 = prevarm4[0];
                                        nm4 = g.m.ggMgr.currentgg.getNodeById(prevarm4[1]);
                                    } else {
                                        g.println(TAG + " !!!!!!!!!!!!!!!! VAR ERROR: MALFORMED VAR M4 !!!!!!!!!!!");

                                    }
                                    if (nm4 == null) {
                                        cm4 = g.m.ggMgr.currentgg.getNodeById(String.valueOf(treeID.split("%")[1])).getConditions();
                                    } else {
                                        cm4 = nm4.getConditions();
                                    }

                                    typenumm4= cm4._varMap.get(m4).getSecond();
                                    if(typenumm4.equalsIgnoreCase("Float") || typenumm4.equalsIgnoreCase("Integer")){
                                        numm4 = Float.valueOf(cm4._varMap.get(m4).getFirst());
                                    }else{
                                        numm4aux = cm4._varMap.get(m4).getFirst();
                                    }

                                } else {
                                    m4=m[4];
                                    typenumm4=typem2;
                                    if(typenumm4.equalsIgnoreCase("Float") || typenumm4.equalsIgnoreCase("Integer")) {
                                        numm4 = Float.valueOf(m4);
                                    }else{
                                        numm4aux = m4;
                                    }
                                }

                                // M5
                                String m5 = "";
                                ToActionNode nm5 = null;
                                Conditions cm5 = null;
                                String[] prevarm5 = m[5].split("!");
                                float numm5 = 0;
                                String numm5aux="";
                                String typenumm5="";

                                if (m[5].contains("VAR")) {
                                    if (prevarm5.length == 1) {
                                        m5 = m[5];

                                    } else if (prevarm5.length == 2) {
                                        m5 = prevarm5[0];
                                        nm5 = g.m.ggMgr.currentgg.getNodeById(prevarm5[1]);
                                    } else {
                                        g.println(TAG + " !!!!!!!!!!!!!!!! VAR ERROR: MALFORMED VAR M5 !!!!!!!!!!!");
                                    }
                                    if (nm5 == null) {
                                        cm5 = g.m.ggMgr.currentgg.getNodeById(String.valueOf(treeID.split("%")[1])).getConditions();
                                    } else {
                                        cm5 = nm5.getConditions();
                                    }

                                    typenumm5= cm5._varMap.get(m5).getSecond();
                                    if(typenumm5.equalsIgnoreCase("Float") || typenumm5.equalsIgnoreCase("Integer")){
                                        numm5 = Float.valueOf(cm5._varMap.get(m5).getFirst());
                                    }else{
                                        numm5aux = cm5._varMap.get(m5).getFirst();
                                    }
                                } else {
                                    m5=m[5];
                                    typenumm5=typem2;
                                    if(typenumm5.equalsIgnoreCase("Float") || typenumm5.equalsIgnoreCase("Integer")) {
                                        numm5 = Float.valueOf(m5);
                                    }else{
                                        numm5aux = m5;
                                    }
                                }

                                // CALC && ASIGN TO M2

                                if(typem2.equalsIgnoreCase("Float") || typem2.equalsIgnoreCase("Integer")){

                                    float div = 0;
                                    if (numm5!=0){
                                        div=numm4/numm5;
                                    }else{
                                        g.println(TAG + " FATAL ERROR: ( /0 ) DIVISION");
                                        exit(0);
                                    }

                                    // ASIGN TO M2
                                    cm2._varMap.get(m2).setFirst(String.valueOf(div));
                                }else{

                                    g.println(TAG + " ERROR: NOT DIVISION CALCULATE AT STRINGS");
                                }

                            }else{
                                g.println(TAG + " !!!!!!!!!!! FATAL ERROR: M2 IS NOT A VAR !!!!!!! : NOT ASIGNMENT, ... (OK) RETURNED ");
                            }

                        }
                        else if(m[3].equalsIgnoreCase("ASIGN")) {

                            // M2 <---VAR  !!!!
                            String m2 = "";
                            ToActionNode nm2 = null;
                            Conditions cm2 = null;
                            String[] prevarm2 = m[2].split("!");
                            String typem2 = "";

                            if (m[2].contains("VAR")) {

                                if (prevarm2.length == 1) {
                                    m2 = m[2];
                                } else if (prevarm2.length == 2) {
                                    m2 = prevarm2[0];
                                    nm2 = g.m.ggMgr.currentgg.getNodeById(prevarm2[1]);
                                } else {
                                    g.println(TAG + " !!!!!!!!!!!!!!!! VAR ERROR: MALFORMED VAR M2 !!!!!!!!!!!");
                                }

                                if (nm2 == null) {
                                    cm2 = g.m.ggMgr.currentgg.getNodeById(String.valueOf(treeID.split("%")[1])).getConditions();
                                } else {
                                    cm2 = nm2.getConditions();
                                }
                                typem2 = cm2._varMap.get(m2).getSecond();

                                // M5
                                String m5 = "";
                                ToActionNode nm5 = null;
                                Conditions cm5 = null;
                                String[] prevarm5;
                                float numm5 = 0;
                                String numm5aux="";
                                String typenumm5="";
                                if(m.length>5) {
                                    prevarm5= m[5].split("!");
                                    if (m[5].contains("VAR")) {
                                        if (prevarm5.length == 1) {
                                            m5 = m[5];

                                        } else if (prevarm5.length == 2) {
                                            m5 = prevarm5[0];
                                            nm5 = g.m.ggMgr.currentgg.getNodeById(prevarm5[1]);
                                        } else {
                                            g.println(TAG + " !!!!!!!!!!!!!!!! VAR ERROR: MALFORMED VAR M5 !!!!!!!!!!!");
                                        }
                                        if (nm5 == null) {
                                            cm5 = g.m.ggMgr.currentgg.getNodeById(String.valueOf(treeID.split("%")[1])).getConditions();
                                        } else {
                                            cm5 = nm5.getConditions();
                                        }

                                        typenumm5 = cm5._varMap.get(m5).getSecond();
                                        if (typenumm5.equalsIgnoreCase("Float") || typenumm5.equalsIgnoreCase("Integer")) {
                                            numm5 = Float.valueOf(cm5._varMap.get(m5).getFirst());
                                            numm5aux = cm5._varMap.get(m5).getFirst();
                                        } else {
                                            numm5aux = cm5._varMap.get(m5).getFirst();
                                        }
                                    } else {
                                        m5 = m[5];
                                        typenumm5 = "String";//typem2;
                                        if (typenumm5.equalsIgnoreCase("Float") || typenumm5.equalsIgnoreCase("Integer")) {
                                            numm5 = Float.valueOf(m5);
                                            numm5aux = m5;
                                        } else {
                                            numm5aux = m5;
                                        }
                                    }
                                }


                                // M4
                                String m4 = "";
                                ToActionNode nm4 = null;
                                Conditions cm4 = null;
                                String[] prevarm4 = m[4].split("!");
                                float numm4 = 0;
                                String numm4aux = "";
                                String typenumm4 = "";



                                if (m[4].contains("VAR") && m.length == 5) {
                                    if (prevarm4.length == 1) {
                                        m4 = m[4];
                                    } else if (prevarm4.length == 2) {
                                        m4 = prevarm4[0];
                                        nm4 = g.m.ggMgr.currentgg.getNodeById(prevarm4[1]);
                                    } else {
                                        g.println(TAG + " !!!!!!!!!!!!!!!! VAR ERROR: MALFORMED VAR M4 !!!!!!!!!!!");

                                    }
                                    if (nm4 == null) {
                                        cm4 = g.m.ggMgr.currentgg.getNodeById(String.valueOf(treeID.split("%")[1])).getConditions();
                                    } else {
                                        cm4 = nm4.getConditions();
                                    }

                                    typenumm4 = cm4._varMap.get(m4).getSecond();
                                    if (typenumm4.equalsIgnoreCase("Float") || typenumm4.equalsIgnoreCase("Integer")) {
                                        numm4 = Float.valueOf(cm4._varMap.get(m4).getFirst());
                                    } else {
                                        numm4aux = cm4._varMap.get(m4).getFirst();
                                    }

                                }
                                else if(m[4].equalsIgnoreCase("GET_SPRITEMATION_CENTER_POSITION_X") && m.length == 6){

                                    Image image = g.m.he.sclientHud.getSpritemationListenersHash().get(numm5aux).getImage();

                                    numm4 = image.getX() + (image.getWidth()/2);

                                }else if(m[4].equalsIgnoreCase("GET_SPRITEMATION_CENTER_POSITION_Y") && m.length == 6){

                                    Image image = g.m.he.sclientHud.getSpritemationListenersHash().get(numm5aux).getImage();
                                    numm4 = image.getY() + (image.getHeight()/2);

                                }
                                else if(m[4].equalsIgnoreCase("GET_SPRITEMATION_POSITION_X") && m.length == 6){

                                    Image image = g.m.he.sclientHud.getSpritemationListenersHash().get(numm5aux).getImage();
                                    numm4 = image.getX();

                                }else if(m[4].equalsIgnoreCase("GET_SPRITEMATION_POSITION_Y") && m.length == 6){

                                    Image image = g.m.he.sclientHud.getSpritemationListenersHash().get(numm5aux).getImage();
                                    numm4 = image.getY();

                                }

                                else if(m[4].equalsIgnoreCase("GET_SPRITEMATION_LAST_TOUCH_HUD_X") && m.length == 6){

                                    numm4 =  g.m.he.sclientHud.getSpritemationListenersHash().get(numm5aux).getHudActualX();

                                }
                                else if(m[4].equalsIgnoreCase("GET_SPRITEMATION_LAST_TOUCH_HUD_Y") && m.length == 6){

                                    numm4 =  g.m.he.sclientHud.getSpritemationListenersHash().get(numm5aux).getHudActualY();

                                }
                                else if(m[4].equalsIgnoreCase("CAMERA_X_POSITION") && m.length == 5){

                                    numm4 =  g.getCamera().position.x;

                                }
                                else if(m[4].equalsIgnoreCase("CAMERA_Y_POSITION") && m.length == 5){

                                    numm4 =  g.getCamera().position.y;

                                }
                                else if(m[4].equalsIgnoreCase("PLAYER_X_POSITION") && m.length == 5){

                                    numm4 =  g.m.lvlMgr.getPlayer().getpositionx();

                                }
                                else if(m[4].equalsIgnoreCase("PLAYER_Y_POSITION") && m.length == 5){

                                    numm4 =  g.m.lvlMgr.getPlayer().getpositiony();

                                }
                                else if(m[4].equalsIgnoreCase("CURRENT_LEVEL") && m.length == 5){

                                    numm4 = ac.Level ;
                                    numm4aux = String.valueOf(ac.Level);

                                }
                                else if(m[4].equalsIgnoreCase("RANDOM_NUM") && m.length == 6){

                                    Random rnd=new Random();
                                    rnd.setSeed(nanoTime());
                                    int x=rnd.nextInt(Integer.valueOf(numm5aux));

                                    numm4 = x;
                                    numm4aux = String.valueOf(x);

                                }
                                else if( m.length == 5) {
                                    m4 = m[4];
                                    typenumm4 = typem2;
                                    if (typenumm4.equalsIgnoreCase("Float") || typenumm4.equalsIgnoreCase("Integer")) {
                                        numm4 = Float.valueOf(m4);
                                    } else {
                                        numm4aux = m4;
                                    }
                                }

                                // ASIGN TO M2
                                if(typem2.equalsIgnoreCase("Float") || typem2.equalsIgnoreCase("Integer")){
                                    cm2._varMap.get(m2).setFirst(String.valueOf(numm4));
                                }else {
                                    cm2._varMap.get(m2).setFirst(numm4aux);
                                }


                            }else{
                                g.println(TAG + " !!!!!!!!!!! FATAL ERROR: M2 IS NOT A VAR !!!!!!! : NOT ASIGNMENT, ... (OK) RETURNED ");
                            }
                        }

                        g.gm.sendMessage(g.messageOK(TreeID, TreeNumNode), TreeID, TreeNumNode);

                    }
                    else if (m.length ==3 && m[1].equalsIgnoreCase("SET_LEVEL_AT_ALL_CONFIGS")) {
                        ac.Level = Integer.valueOf(m[2]);
                        g.m.ggMgr.saveALlConfigs(g);
                        g.println( TAG + " SETTING LEVEL ");

                        g.gm.sendMessage(g.messageOK(TreeID, TreeNumNode), TreeID, TreeNumNode);

                    }else if (m.length > 0 && m[1].equalsIgnoreCase("PORTAL_TAKE")) {
                        if (m.length > 1 && (m[2].equalsIgnoreCase("PORTAL"))) {
                            pointer = m[1] + "#" + m[2] + "#" + m[3]; // EX: PORTALTAKE#PORTAL#LVL_1_3,SHOOTER#PORTALTAKE
                            g.println("EXECUTING:" + pointer);

                            //g.println(TAG + "  ALL POINTERS KEYSET=   " + currentgg.pointers.keySet().toString());
                            //g.println(TAG +  "  SHOOTER#" + m[1] + ",,,,  POINTER :::  " + pointer);

                            if (currentgg.pointers.get(pointer) != null)
                                currentgg.exec(currentgg.pointers.get(pointer), "SHOOTER#" + m[1], pointer);
                        }
                    } else if (m.length > 0 && m[1].equalsIgnoreCase("EVENT_TAKE")) {
                        if (m.length > 1 && m[2].equalsIgnoreCase("START")) {
                            pointer = m[1] + "#" + m[3] + "#" + m[4];
                            currentgg.exec(currentgg.pointers.get(pointer), "SHOOTER#" + m[1] + "#" + m[2], pointer);
                        } else if (m.length > 1 && m[2].equalsIgnoreCase("STOP")) {
                            pointer = m[1] + "#" + m[3] + "#" + m[4];
                            currentgg.exec(currentgg.pointers.get(pointer), "SHOOTER#" + m[1] + "#" + m[2], pointer);
                        }

                    }

                } else {
                    g.println(TAG + " MESSAGE: " + m + " SINTAX ERROR");
                }
            }

            g.set_hasGraphCommand(false);

        }
    }



    public void closeFiles(){
        saveALlConfigs(g);



        if(allEvents!=null && allEvents.size>0) {
            for (String s : allEvents) {
                handleNameFile.writeString("#.EVENT:: " + s + "\n", append);
            }
        }
        handleNameFile.writeString("#.#UDG2#"+comprobationResult+"\n",append);
        handleNameFile.writeString("#.\n",append);
        handleNameFile.writeString("#.TotalTimeInSeconds::"+ generalTime +"\n",append);
        handleNameFile.writeString("#.TotaldistanceInPixels::" + distance + "\n",append);
        handleNameFile.writeString("#.TotalSelections::" + totalSelections + "\n",append);
        handleNameFile.writeString("#.TotalProximitys::" + totalProximitys + "\n",append);
        Date date=new Date();
        SimpleDateFormat ft = new SimpleDateFormat("E yyyy.M.dd 'at' hh:mm:ss a zzz");
        handleNameFile.writeString("#.\n",append);
        handleNameFile.writeString("#.newDateGameEnd::" + ft.format(date) + "\n" ,append);
        handleNameFile.writeString("#.####",append);


    }

    public GameGraph getCurrentgg() {
        return currentgg;
    }

    public void setCurrentgg(GameGraph currentGraph){ currentgg = currentGraph;}

    public void analizingForConditionsComprobation(String completeFile, long lastmodified){
        long lm=0;
        for(String s:completeFile.split("\n")){
            if (s.contains("UDG#")) {
                lm = Long.valueOf(s.split("UDG#")[1]);
                //System.out.println(TAG +" LAST MODIFIED == " + lm + " AND LASTMODIFIEDGAMEGRAPH=" + lastmodified);
                comprobationResult=false;
            }
            if(s.contains("UDG2#")) {
                comprobationResult=Boolean.valueOf(s.split("UDG2#")[1]);
                //System.out.println(TAG +" COMPROBATION RESULT=" + comprobationResult);
            }

        }
        if(lm==lastmodified ) {
            System.out.print("\n\n\n\n"+TAG + " GAMEGRAPH ARE'NT CHANGED."+"\n\n");
                if(comprobationResult) {
                    isNecesaryConditionsBasicCheck = false;
                    System.out.print(" NOT'S NECESARY CONDITIONS BASIC CHECK\n");
                }else{
                    System.out.print(" BUT PREVIOUS RESULT ISN'T GOOD CHECK. IS NECESARY CONDITIONS BASIC CHECK\n");
                }

        }else{
                System.out.println(TAG + " GAMEGRAPH ARE CHANGED. IS NECESARY CONDITIONS BASIC CHECK");
        }


    }
    public void saveALlConfigs( GenericMethodsInputProcessor g){
        // SAVE ALL CONFIGURATIONS
        // ALL CONFIGS
        ac.playerPosition=g.m.lvlMgr.getPlayer().getposition();
        ac.cameraposition=new Vector2(g.getCamera().position.x,g.getCamera().position.y);
        ac.points=g.m.he.getEMO();
        ac.lvlEntitiesPosition.clear();
        for(String name:g.m.getLvlMgr().get_currentLvl().get_lvlEntities().keySet()){
            ac.lvlEntitiesPosition.add(new Pair<String,Vector2>(name,g.m.getLvlMgr().get_currentLvl().get_lvlEntities().get(name).getposition()));        }
        acCharger.saveAllConfigs();


    }

    public ArrayList<String> getPendingOKinstructions() {
        return pendingOKinstructions;
    }

    public void setPendingOKinstructions(ArrayList<String> pendingOKinstructions) {
        this.pendingOKinstructions = pendingOKinstructions;
    }
}
