package com.mygdx.safe.MainGameGraph;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.mygdx.safe.CollisionManager;
import com.mygdx.safe.Entities.GameEntity;
import com.mygdx.safe.EntitySystems.GameSystem;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;
import com.mygdx.safe.InputProcessors.SpritemationListener;
import com.mygdx.safe.screens.ProfileScren;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

import static com.badlogic.gdx.Gdx.app;
import static com.mygdx.safe.screens.MainGameScreen.ac;
import static java.lang.System.exit;
import static java.lang.System.in;


/**
 * Created by Boris.Escajadillo on 27/09/17.
 * for file in *.png; do convert $file -resize 20% $file; done
 */

public class GameGraph {

    private static final  String TAG=GameGraph.class.getSimpleName();
    GenericMethodsInputProcessor g;

    Array<ActionChoice> actionsArray=new Array<com.mygdx.safe.MainGameGraph.ActionChoice>();
    HashMap<String, ToActionNode> nodes;
    HashMap<String,ArrayList<ActionChoice>> actions;
    HashMap<String,ProgrammedMovement> moveprogs = new HashMap<String,ProgrammedMovement> ();
    String currentNodeId =null;
    // SINTAX CONVENTION NAMEGRAPH:
    // LVL#numberOfLevel#numberOfSubLevel@LOOKSCREEN#lookScreenType("DARKER", "DARK", "STANDAR", "LIGHT")
    // EX: LVL#1#1@LOOKSCREEN#STANDAR
    String graphID;
    String rootID;
    HashMap <String, ActionChoice> pointers; //  ID NODE,TYPE OF NODE: TYPES: ROOT,SELECT, PROXIMITY

    // SEQUENCES MANAGER
    public HashMap<String, SequenceInstructions> _sequences;
    //TIMERS MANAGER
    HashMap<String, Timer> timers;

    //SHOOTER TREES
    HashMap<String,SpecialSuperTree>  allShooterTrees = new HashMap<String, SpecialSuperTree>();

    boolean firstRootExec=true;




    public GameGraph(String id, HashMap<String, ToActionNode> nodes, String rootID, GenericMethodsInputProcessor g){

        pointers =new HashMap<String, com.mygdx.safe.MainGameGraph.ActionChoice> ();
        this.g = g;
        this.graphID=id;
        this.rootID=rootID;
        this.nodes=new HashMap<String, ToActionNode>();
        actions=new HashMap<String,ArrayList<ActionChoice>> ();
        setNodes(nodes);
        setCurrentNode(rootID, "none");

        // SEQUENCE INSTRUCTIONS HASHMAP
        _sequences=new HashMap<String, SequenceInstructions> ();
        // TIMER INSTRUCTIONS HASHMAP
        timers=new HashMap<String, com.mygdx.safe.MainGameGraph.Timer>();
        setConditions(timers,this);
    }

    public void config(){
        //FIRST EXEC (FOR ALL POINTERS SETTING, WITH "NOT IMPORTANT" NODE ID FOR THIS)
        //BASIC VERIFICATION NODE CONECTION:
        if(!verifyNodeConection()){
            System.out.println("\n"+TAG + " NODE ERROR  AT GAMEGRAPH:" + graphID);
            g.m.ggMgr.comprobationResult = false;
            g.m.ggMgr.closeFiles();
            app.exit();
        }

        if(firstRootExec) {
            firstExecNodeExpansion(rootID);
            for(ToActionNode n: nodes.values()){
                createTrees(n);
            }
            firstRootExec = false;

            g.println(" [ ALL POINTERS LIST:  ] ");
                int i = 0;
                for (String s : pointers.keySet()) {
                    g.println("POINTER:" + i + " [" + s + "]");
                    i++;
                }
            g.println(TAG+" CONFIG : FIRSTEXECNODEEXPANSION CONFIG ENDS:\n\n\n");

            if(g.showingTrees) {
                for (SpecialSuperTree t : allShooterTrees.values()) {
                    g.println(t.showTree());
                }
            }
        }
    }

    public void createTrees(com.mygdx.safe.MainGameGraph.ToActionNode n) {

        com.mygdx.safe.MainGameGraph.Conditions c = n.getConditions();

        if (c.with_shooters) {
            for (String s : c._shooters.keySet()) {
                c.shooterTrees.put(s,new SpecialSuperTree(g, n.getID() + "%" +s));
                allShooterTrees.put(s + "%" + n.getID(), c.shooterTrees.get(s));

                c.shooterTrees.get(s).setRoot(false,s);
                childCreator(c.shooterTrees.get(s).getRootNode(), c,n);
            }
        }
    }

    public void childCreator(SpecialSuperTree.TreeNode node, com.mygdx.safe.MainGameGraph.Conditions c, com.mygdx.safe.MainGameGraph.ToActionNode gnode){

        if(g.showChildCreator) g.println(TAG + " *CHILD-CREATOR* ->CHILD[" + node.getName()+"]");

        String name = node.getName();
        String typeInstruction =node.getName().split("#")[0];

        if(typeInstruction.contains("SHOOTER")){
            for(int i=1; i<c._shooters.get(name).getSecond().length;i++){
                node.addChildNode(false,c._shooters.get(name).getSecond()[i]);
                // ALL SHOOTER INSTRUCTIONS AT CHILDS
                childCreator(node.getChild(node.getChildNodes().size-1), c,gnode);
            }
        }else if(typeInstruction.contains("DIRECT") && !typeInstruction.contains("DIRECTION")){

            if(g.showChildCreator) g.println(TAG + " *CHILD-CREATOR* **********" + gnode.getID() + "   " + typeInstruction + "   ");

            for(int i=1; i<c._directInstructions.get(name).length; i++){
                node.addChildNode(false, c._directInstructions.get(name)[i]);
                //ALL DIRECT INSTRUCTIONS AT CHILDS
                childCreator(node.getChild(node.getChildNodes().size-1), c,gnode);
            }
        }else if(typeInstruction.contains("SEQUENCE")){

            if(g.showChildCreator) g.println(TAG + " *CHILD-CREATOR* " + name + "   " + gnode.getID());

            for(int i=0; i<_sequences.get(name+"#"+gnode.getID()).getInstructions().length; i++){
                // ALL SEQUENCE INSTRUCTIONS AT CHILDS
                node.addChildNode(false, _sequences.get(name+"#"+gnode.getID()).getInstructions()[i]);
                childCreator(node.getChild(node.getChildNodes().size-1), c,gnode);
            }
        }else if(typeInstruction.contains("CHOICER")){

            if(g.showChildCreator) g.println(TAG + " *CHILD-CREATOR* *************************************" + typeInstruction + "     " + node.getName() + "    " + c._choicers.keySet().toString());

            node.addChildNode(false, c._choicers.get(typeInstruction).getInstruction());
            childCreator(node.getChild(node.getChildNodes().size-1), c, gnode);

            for(String s: c._choicers.get(typeInstruction).getInstructionChoices()){
                node.addChildNode(false, s);
                childCreator(node.getChild(node.getChildNodes().size-1), c, gnode);
            }
        }else if(typeInstruction.contains("EVALUATOR")){

            if(g.showChildCreator) g.println(TAG + "  *CHILD-CREATOR* " + typeInstruction + "    " + gnode.getID());

            for(int i=2;i<c._evaluators.get(name).length;i++){
                node.addChildNode(false,c._evaluators.get(name)[i]);
                // CHILD0=TRUE EVALUATION // CHILD1=FALSE EVALUATION
                childCreator(node.getChild(node.getChildNodes().size-1), c,gnode);
            }

        }else if(typeInstruction.contains("TIMER")){

            if(g.showChildCreator) g.println(TAG + " *CHILD-CREATOR*   TIMER: " + name+"#"+gnode.getID() );//+ timers.keySet().toString());

            for(int i=0;i<timers.get(name+"#"+gnode.getID()).getActions().length;i++){
                String s=timers.get(name+"#"+gnode.getID()).getActions()[i];
                node.addChildNode(false,s);
                // CHILD0=PREACTION // CHILD1=POSTACTION
                childCreator(node.getChild(node.getChildNodes().size-1), c,gnode);
            }

        }else if(typeInstruction.contains("COUNTER")){

            if(g.showChildCreator) g.print(TAG +" *CHILD-CREATOR*   NODE: " + gnode.getID()) ;

            for (String s : c.counterInstructions.get(name).keySet()) {
                for (int i = 0; i < c.counterInstructions.get(name).get(s).size; i++) {

                    String cname = name + "#" + s.split("%")[0] + "#" + s.split("%")[1] + "#" + c.counterInstructions.get(name).get(s).get(i)[0];
                    if(g.showChildCreator) g.print(cname+"\n");

                    //String cname = name + "#" + c.counterInstructions.get(name).get(p).get(i)[0] +  "#" + p.getFirst() + "#" + p.getSecond();
                    node.addChildNode(false, cname);

                    for(int j=1; j < c.counterInstructions.get(name).get(s).get(i).length; j++){

                        String CIname = c.counterInstructions.get(name).get(s).get(i)[j];
                        SpecialSuperTree.TreeNode currentChild = node.getChild(node.childNodes.size-1);


                        currentChild.addChildNode(false, CIname);
                        childCreator(currentChild.getChild(currentChild.getChildNodes().size-1), c,gnode);
                    }
                }
            }

        }else if(typeInstruction.contains("RANDOM")){

            if(g.showChildCreator) g.print(TAG +" *CHILD-CREATOR*   NODE: " + gnode.getID() + "  " + typeInstruction) ;

            for(int i = 2; i<c.randoms.get(name).length; i+=3){

                node.addChildNode(false, c.randoms.get(name)[i+2]);
                SpecialSuperTree.TreeNode child = node.getChild(node.getChildNodes().size-1);
                childCreator(child, c, gnode);
            }

        }else return;

    }

    public void setNodes(HashMap<String, com.mygdx.safe.MainGameGraph.ToActionNode> nodes){
        if(nodes.size()<0){
            throw new IllegalArgumentException("Can't have a negative amount of ToActionNodes");
        }
        this.nodes=nodes;
        this.actions = new HashMap<String,ArrayList<com.mygdx.safe.MainGameGraph.ActionChoice>>(actions.size());
        for(com.mygdx.safe.MainGameGraph.ToActionNode node: nodes.values()){
            actions.put(node.getID(),new ArrayList<com.mygdx.safe.MainGameGraph.ActionChoice>());
        }
    }


    public ToActionNode getNodeById(String id){
        if(!isValid(id)){
            g.println(TAG+" NodeId ["+id + "] is not valid ");
            return null;
        }
        return  nodes.get(id);
    }

    public void setCurrentNode(String id, String pointer){
        ToActionNode node=getNodeById(id);
        if(node==null) return;
        if(currentNodeId==null || currentNodeId.equalsIgnoreCase(id) || isReachable(currentNodeId,id, pointer)){
            currentNodeId=id;
        }else{
            g.println(TAG+" New ToActionNode [" + id + "] is not reachable from current node [" + currentNodeId+"]");
        }
    }

    public String getCurrentNodeId(){
        return this.currentNodeId;
    }


    public boolean isValid(String uid){

        ToActionNode node=nodes.get(uid);
        return node != null;
    }

    public boolean isReachable(String oID,String dID, String p){

        String OriginID = "";

        if(pointers.get(p) != null) OriginID = pointers.get(p).getOriginID();
        else OriginID = oID;

        if(!isValid(oID) || !isValid(dID)) return false;
        if(nodes.get(oID)==null) return false;
        ArrayList<com.mygdx.safe.MainGameGraph.ActionChoice> list = actions.get(OriginID);

        if(list==null) return false;

        for(com.mygdx.safe.MainGameGraph.ActionChoice action: list){
            //if(pointer.equalsIgnoreCase("none")) OriginID = action.getOriginID();
            if(action.getOriginID().equalsIgnoreCase(OriginID) && action.getDestinyID().equalsIgnoreCase(dID)) return true;
        }

        return false;
    }

    public void addAllActionChoices(Array<com.mygdx.safe.MainGameGraph.ActionChoice> actions){
        actionsArray=actions;
        for(com.mygdx.safe.MainGameGraph.ActionChoice action:actions){
            addActionChoice(action);
        }
    }

    public void setConditions(HashMap<String, com.mygdx.safe.MainGameGraph.Timer> timers, GameGraph gg){
        for(com.mygdx.safe.MainGameGraph.ToActionNode n: nodes.values()){
            n.conditions.setConditions(n,n._activateConditions,this,timers,_sequences,gg);
        }
    }


    public void addActionChoice(com.mygdx.safe.MainGameGraph.ActionChoice action){
        ArrayList<com.mygdx.safe.MainGameGraph.ActionChoice> list=actions.get(action.getOriginID());
        if(list==null) return;
        for(com.mygdx.safe.MainGameGraph.ActionChoice caction: list){
            if (caction.getDestinyID().equalsIgnoreCase(action.getDestinyID())) return;
        }
        list.add(action);
    }

    public String displayCurrentActionNode(){
        return nodes.get(currentNodeId).getContent();
    }


    public ArrayList<com.mygdx.safe.MainGameGraph.ActionChoice> get_actions(String id){
        return actions.get(id);
    }

    public HashMap<String, ProgrammedMovement> getMoveprogs() {
        return moveprogs;
    }

    public void setMoveprogs(HashMap<String, ProgrammedMovement> moveprogs) {
        this.moveprogs = moveprogs;
    }

    public String FindNode(String searchData){
        String id=null;
        id=FindIdNodeForName(searchData); // SEARCH AND FIND WITH SEARCHDATA=NODETYPEANDNAME
        // OTHER FIND CRITERIAS IF NECESARY
        return id;
    }

    public String FindIdNodeForName(String nodeTypeAndName){
        String id=null;

        for(com.mygdx.safe.MainGameGraph.ToActionNode node:nodes.values()){
            if(node._nodeTypeAndName.equalsIgnoreCase(nodeTypeAndName)){
                id=node.ID;
            }
        }
        return id;
    }

    public boolean exec(com.mygdx.safe.MainGameGraph.ActionChoice c, String shooter, String pointer){

        boolean executed=false;
        if (c==null){
            g.println(TAG + "ACTIONCHOICE IS NULL: PLEASE REVIEW SHOOTERS/POINTERS START AND STOPS. ACTUAL SHOOTER IS:"+shooter+" ****  ACTUAL POINTER IS :" +pointer);
        }
        if(c!=null) {
            String nid = c.getDestinyID();

            if (nid != null) {
                g.println(TAG + "           [>] NODE_ID=[" + nid + "]->NODETYPEANDNAME=[" + nodes.get(nid).get_nodeTypeAndName() + "] TRACE : EXECUTING...");
                executed = execNode(c, shooter, pointer);
                if (!executed)
                    //g.println(TAG +" ERROR: THE NODE "+getNodeById(nid).get_nodeTypeAndName()+ " IS NOT REACHABLE OR NOT POINTER");
                    g.println(TAG + ":NE:");

            } else {
                g.println(TAG + " ERROR: NULL NID pointer");
            }
        }
        return executed;
    }

    public String interNodePathComprobator(String nodeid1,String nodeid2){ // ONLY INTERNODE PATH
        return interNodePathComprobator(nodeid1,nodeid2,false);
    }


    public String interNodePathComprobator(String nodeid1,String nodeid2, boolean pointerComprobation){ //INTERNODE PATH && POINTER COMPROBATION

        String comprobation="#"+nodeid1;
        boolean inpointers=false;

        if(pointerComprobation){
            for(com.mygdx.safe.MainGameGraph.ActionChoice c:pointers.values()){
                String s=c.getDestinyID();
                if(nodeid1.equalsIgnoreCase(s) || nodeid2.equalsIgnoreCase(s)){
                    inpointers=true;
                }
            }
        }

        com.mygdx.safe.MainGameGraph.ToActionNode n1 = getNodeById(nodeid1);
        com.mygdx.safe.MainGameGraph.ToActionNode n2 = getNodeById(nodeid2);

        if(n1==null || n2==null || ( pointerComprobation && inpointers)){
            comprobation+="#END";
        }else {
            if (n1._blocked)
                comprobation += "#BLOCKED";
            if (nodeid1.equalsIgnoreCase(nodeid2)) {  // IF NODEID1 == NODEID2 : PATH TRACE ENDS AND TRUE
                comprobation += "#" + nodeid1 + "#TRUE";
            } else {
                ArrayList<com.mygdx.safe.MainGameGraph.ActionChoice> choices = get_actions(nodeid1);
                if (choices == null || choices.size()==0) {
                    comprobation += "#END";
                } else {
                    for (com.mygdx.safe.MainGameGraph.ActionChoice c : choices) {
                        String newNodeId1 = c.getDestinyID();
                        interNodePathComprobator(newNodeId1,nodeid2,pointerComprobation); // RECURSIVE APROXIMATION FOR INTERNODEPATH COMPROBATOR.
                    }
                }
            }
        }
        return comprobation;
    }

    public void firstExecNodeExpansion(String id){

        g.println("\n"+TAG+" FIRSTEXECNODEEXPANSION: ID: "+id);
        com.mygdx.safe.MainGameGraph.ToActionNode n = getNodeById(id);
        com.mygdx.safe.MainGameGraph.ToActionNode destinyNode;
        n._enable=true;
        g.println("NODE TYPE AND NAME: " +n.get_nodeTypeAndName());

        if (n.get_nodeTypeAndName().equalsIgnoreCase("ROOT") || n.get_nodeTypeAndName().equalsIgnoreCase("SELECT") || n.get_nodeTypeAndName().equalsIgnoreCase("PROXIMITY") ||
                n.get_nodeTypeAndName().equalsIgnoreCase("PORTAL_TAKE") ||  n.get_nodeTypeAndName().equalsIgnoreCase("EVENT_TAKE") ||  n.get_nodeTypeAndName().equalsIgnoreCase("LISTENER_EVENT")) {
            g.println(TAG+" EXPAND POINTER FROM id:["+id+"] AND TYPE NODE:["+ n.get_nodeTypeAndName() + "] TO: ");
            for (com.mygdx.safe.MainGameGraph.ActionChoice c : get_actions(n.ID)) {

                String TypeOfNextNode[]=c.get_instruction().split("%");
                g.print(TAG+" NODES:["+c.getOriginID()+"]-->["+c.getDestinyID()+"] EXPAND HIS ACTION CHOICE INSTRUCTION:");
                for(int i=0;i<TypeOfNextNode.length;i++){
                    g.print("["+TypeOfNextNode[i]+"]");
                }
                g.print("\n");


                destinyNode = getNodeById(c.getDestinyID());
                //pointers.put(c.getDestinyID(),TypeOfNextNode[0]);
                pointers.put(n.get_nodeTypeAndName()+"#"+destinyNode._nodeTypeAndName,c);


                //BUILD PROXIMITY RELATIONS IN EACH GAME ENTITY
                if(n.get_nodeTypeAndName().equalsIgnoreCase("PROXIMITY")){
                    GameEntity ge;

                    //NODES WITH PROXIMITY POINTER "ENTITY#NAMEENTITY"
                    String entityName = destinyNode._nodeTypeAndName.split("#")[1];

                    if(entityName.equalsIgnoreCase("PLAYER")) ge = g.m.lvlMgr.getPlayer();
                    else ge = g.m.lvlMgr.get_currentLvl().get_lvlEntities().get(entityName);

                    ge.fillProximityRelations(destinyNode.conditions);
                }

                firstExecNodeExpansion(c.getDestinyID());
            }
        }else {

            g.println(TAG+" NODE EXPANSION ENDS:\n");

        }
    }

    public boolean instructions_executor(com.mygdx.safe.MainGameGraph.ActionChoice choice, String shooter){
        com.mygdx.safe.MainGameGraph.Conditions c=getNodeById(choice.getDestinyID()).getConditions();
        boolean executed=false;
        boolean compare = false;
        g.println(TAG +"           [>] NODE_ID=["+choice.getDestinyID()+"] INSTRUCTIONS_EXECUTOR : TRACE ");
        if(c.with_idsCompare) {

            if (c.and_compare) { //COMPARE AND ID ACTIVATIONS
                compare=true;
                for (boolean b : c._id_activation.values()) {
                    compare = compare && b;
                }
            }
            if (c.or_compare) { //COMPARE OR ID ACTIVATIONS
                for (boolean b : c._id_activation.values()) {
                    compare = compare || b;
                }
            }

            // ID_CONDITIONS EXECUTION IS CHARGED AT COMPARE BOOLEAN VARIABLE
            if (compare) { // COMPARE DECIDED TO EXECUTED THE NODE ID INSTRUCTIONS AND SHOOTERS
                executed=true;
                boolean b;

                for (String s : c.idInstructions) {
                    // POSIBLY EVALUATION OF OTHER INSTRUCTION TYPES.
                    // AT MOMENT, ONLY "#"SPLITTYPE INSTRUCTIONS

                    b = InstructionSenderExecutor(choice,s, null);
                    executed=executed && b; //EXECUTOR SENDER FOR COMPARE CONDITION IDS TRUING
                }
                // PRE INCREMENTS COUNTER OF NODE EXECUTION
                getNodeById(choice.getDestinyID())._visitCounter++;
                g.println(TAG + "           [>] NODE_ID=[" + getNodeById(choice.getDestinyID()).getID() + "] VISIT_COUNTER: TRACE : " + getNodeById(choice.getDestinyID())._visitCounter);

                b = shooters_executor(false,choice,shooter);
                executed=executed && b;  // FALSE (NORMAL EXECUTION OF SHOOTERS)
            }else{// COMPARE FALSE, THEN ONLY SHOOTER#FORCED executes

                // PRE INCREMENTS COUNTER OF NODE EXECUTION
                getNodeById(choice.getDestinyID())._visitCounter++;
                g.println(TAG + "           [>] NODE_ID=[" + getNodeById(choice.getDestinyID()).getID() + "] VISIT_COUNTER: TRACE : " + getNodeById(choice.getDestinyID())._visitCounter);

                boolean b = shooters_executor(true,choice,shooter);
                executed=executed && b; // TRUE: FORCED THE "SHOOTER#FORCE" execution
            }
        }else{ // IF NO ID_COMPARE INSTRUCTIONS; ONLY SHOOTER EXECUTOR

            // PRE INCREMENTS COUNTER OF NODE EXECUTION
            getNodeById(choice.getDestinyID())._visitCounter++;
            g.println(TAG + "           [>] NODE_ID=[" + getNodeById(choice.getDestinyID()).getID() + "] VISIT_COUNTER: TRACE : " + getNodeById(choice.getDestinyID())._visitCounter);

            executed = shooters_executor(false,choice,shooter);
        }
        return executed;
    }

    public boolean searchInstructionInSequences(String instruction){
         return true;
    }

    public boolean shooters_executor(boolean forced, com.mygdx.safe.MainGameGraph.ActionChoice choice, String shooter){
        if(searchInstructionInSequences(shooter)){}

        boolean executed=true;

        com.mygdx.safe.MainGameGraph.Conditions c=getNodeById(choice.getDestinyID()).getConditions();
        g.println(TAG +"           [>] NODE_ID=["+choice.getDestinyID()+"]"+" SHOOTER_EXECUTOR : TRACE : PRE-TRANSFORMATED SHOOTER= ["+ shooter + "]");
        if (c.with_shooters) {

            if(forced){
                shooter="SHOOTER#FORCED";
            }

            if(c._shooters.get(shooter)==null){
                shooter="SHOOTER";
            }

            String TreeID = shooter + "%" + choice.getDestinyID();

            g.println(TAG + "  SHOOTERs EXECUTOR: TREE ID : " + TreeID);


            g.println(TAG + "  TREE-ID: " + TreeID);// + "    ALL-SHOOTER-TREES.KEYSET():  " + allShooterTrees.keySet().toString() + "   SHOOTER:   " + shooter);


            g.println(shooter + "   " + c._shooters.keySet().toString());
            String[] instructions = c._shooters.get(shooter).getSecond();

            if(c._shooters.get(shooter)!=null) {

                // Increment +1 internal shooter counter
                //c._shooters.get(shooter).setFirst(c._shooters.get(shooter).getFirst()+1);
                // instructions[0]=name

                if(!allShooterTrees.get(TreeID).currentNode.getNumNode().equals(allShooterTrees.get(TreeID).rootNode.getNumNode())){ // REPLACED WITH !EQUALS(...) (NOT !=) BY ANDROIDLINT


                    allShooterTrees.get(TreeID).resetTree(false);
                }



                int TreeNumNode = 0 + allShooterTrees.get(TreeID).currentNode.numNode;


                g.println(TAG +"           [>] NODE_ID=["+choice.getDestinyID()+"]"+" SHOOTER_EXECUTOR : TRACE : instructions[0](Shooter Name) : [" + instructions[0]+"]");
                if(instructions.length>1) {
                    for (int i = 1; i < instructions.length; i++) {
                        g.println(TAG +"           [>] NODE_ID=["+choice.getDestinyID()+"]"+" SHOOTER_EXECUTOR : TRACE: INSTRUCTION["+i+"]=["+instructions[i]+"]");
                        boolean b;

                         //INSTRUCTIONS THERE ARE AT [1] AND NEXTs

                        g.println(TAG + "  SHOOTER EXECUTION --- TREE ID: " + TreeID +
                                "  --  NUM CHILDS: " + allShooterTrees.get(TreeID).getNode(TreeNumNode).getChildNodes().size + "   " +
                                "  --  SHOOTER:  " + allShooterTrees.get(TreeID).getNode(TreeNumNode).getName());

                        g.println("  --  SHOOTER CHILD TO EXECUTE: " + allShooterTrees.get(TreeID).getNode(TreeNumNode).getChildNodes().get(i-1).getName());

                        allShooterTrees.get(TreeID).setCurrentNode(allShooterTrees.get(TreeID).getNode(TreeNumNode).getChild(i-1));

                        if (instructions[i].contains("DIRECT") && !instructions[i].contains("DIRECTION")){
                            //DirectExecutor
                            b = direct_executor(choice, c, instructions[i], TreeID);
                            executed = executed && b;
                        } else if (instructions[i].contains("TIMER")) {
                            //TimerExecutor
                            b = timer_executor(choice,c, instructions[i], TreeID);
                            executed = executed && b;
                        } else if (instructions[i].contains("RANDOM")) {
                            //RandomExecutor
                            b = random_executor(choice,c, instructions[i], TreeID);
                            executed = executed && b;
                        } else if (instructions[i].contains("EVALUATOR")) {
                            //EvaluatorExecutor
                            b = evaluator_executor(choice,c, instructions[i], TreeID);
                            executed = executed && b;
                        } else if (instructions[i].contains("COUNTER")) {
                            //CounterExecutor
                            b = counter_executor(choice,c, instructions[i], TreeID);
                            executed = executed && b;
                        }  else if(instructions[i].contains("SEQUENCE")){
                            b = sequence_executor(choice,  instructions[i], TreeID);
                            executed = executed && b;
                        } else if(instructions[i].contains("CHOICER")) {
                            b = choicer_executor(choice, c , instructions[i], TreeID);
                            executed = executed && b;
                        } else {
                            //SimpleInstructionSplitter#Executor
                            b = InstructionSenderExecutor(choice, instructions[i], TreeID);
                            executed = executed && b;
                        }
                    }

                }else{
                    g.println("THE SHOOTER INSTRUCTION IS BADLY BUILT. ["+shooter+"]");
                }
            }else{
                g.println("NULL SHOOTER INSTRUCTION: ["+shooter+"]");
            }
        }


        return executed;
    }

    public boolean counter_executor(com.mygdx.safe.MainGameGraph.ActionChoice choice, com.mygdx.safe.MainGameGraph.Conditions c, String counter, String treeID){
        g.println(TAG +"           [>] NODE_ID=["+choice.getDestinyID()+"]"+" COUNTER_EXECUTOR : TRACE : NAME : [" + counter+"]");
        boolean executed=true;
        // THE COUNTER NAME IS ALREADY COUNTER OR COUNTER1 OR OTHER COUNTERXXXXX NAME , BUT EVER COMPARES ACTUAL [NODE . _VISITCOUNTER] VALUE TO CASES PROGRAMMED AT THIS COUNTERXXXX

        String TreeID = "" + treeID;
        int TreeNumNode = 0 + allShooterTrees.get(TreeID).currentNode.numNode;


        if(counter.contains("COUNTER")) {
            com.mygdx.safe.MainGameGraph.ToActionNode n=getNodeById(choice.getDestinyID());

            int vCounter=n._visitCounter;

            if(n.getConditions().counterShooterRelation.get(counter)!=null){
                String shooter=n.getConditions().counterShooterRelation.get(counter);
                vCounter=n.getConditions()._shooters.get(shooter).getFirst();
            }

            g.println(TAG +"_VISITCOUNTER =" + n._visitCounter);

            Array<String[]> inst;
            //Array<String[]> instructions =new Array<String[]>();
            if(n!=null && n.conditions.with_counters) {
                // SELECTING THE COUNTER COMPARATOR CASE TO CATCH AND ADD INSTRUCTIONS TO ARRAY
                // THE NEW ARRAY, AT NEXT STEP IS EXECUTED

                if(c.counterInstructions.get(counter)!=null) {

                    g.println(TAG + "  COUNTER EXECUTION -- TREE ID: " + TreeID +
                            "  --  COUNTER:  " + allShooterTrees.get(TreeID).getNode(TreeNumNode).getName() +
                            "  --  NUM CHILDS: " + allShooterTrees.get(TreeID).getNode(TreeNumNode).getChildNodes().size);

                    for (String ValueToCompare : c.counterInstructions.get(counter).keySet()) {
                        inst = c.counterInstructions.get(counter).get(ValueToCompare);
                        if (inst != null) {
                            for (int j = 0; j < inst.size; j++) {

                                int value1 = Integer.valueOf(ValueToCompare.split("%")[0]);
                                int value2 = Integer.valueOf(ValueToCompare.split("%")[1]);

                                String childCName = counter + "#"+ value1 + "#" + value2 + "#" + inst.get(j)[0];
                                boolean b;

                                if ((inst.get(j)[0].equalsIgnoreCase("==") && vCounter == value2) ||
                                        (inst.get(j)[0].equalsIgnoreCase("<") && vCounter < value2) ||
                                        (inst.get(j)[0].equalsIgnoreCase("<=") && vCounter <= value2) ||
                                        (inst.get(j)[0].equalsIgnoreCase(">") && vCounter > value2) ||
                                        (inst.get(j)[0].equalsIgnoreCase(">=") && vCounter >= value2) ||
                                        (inst.get(j)[0].equalsIgnoreCase("[]") && vCounter >= value2 && vCounter <= value1)){

                                    allShooterTrees.get(TreeID).getNode(TreeNumNode).getChild(childCName).setToExecute(true);
                                }
                            }
                        }
                    }

                    for(int i = 0; i<allShooterTrees.get(TreeID).getNode(TreeNumNode).getChildNodes().size; i++){
                        if(allShooterTrees.get(TreeID).getNode(TreeNumNode).getChildNodes().get(i).toExecute){

                            g.println(TAG + "  COUNTER EXECUTION -- TREE ID: " + TreeID +
                                    "  --  COUNTER:  " + allShooterTrees.get(TreeID).getNode(TreeNumNode).getName() +
                                    "  --  NUM CHILDS: " + allShooterTrees.get(TreeID).getNode(TreeNumNode).getChildNodes().size + "   " +
                                    "  --  COUNTER CHILD TO EXECUTE: " + allShooterTrees.get(TreeID).getNode(TreeNumNode).getChildNodes().get(i).getName());

                            allShooterTrees.get(TreeID).setCurrentNode(allShooterTrees.get(TreeID).getNode(TreeNumNode).getChildNodes().get(i));
                            counterComparationInstructions_executor(choice, c, TreeID);
                        }
                    }
                }
            }
        }

        return executed;
    }

    /*public boolean counterJumpingCurrentNode(ActionChoice choice, Conditions c, SpecialSuperTree currentTree){

        boolean b;

        currentTree.setCurrentNode(currentTree.currentNode.getCurrentNodeIDFromChild(childCName));

        b = counterComparationInstructions_executor(choice, c, currentTree);

        if(currentTree.currentNode != currentTree.getRootNode())
            currentTree.setCurrentNode(currentTree.currentNode.getParentNode().getNumNode());

        else return false;

        return b;
    }*/

    public boolean counterComparationInstructions_executor(com.mygdx.safe.MainGameGraph.ActionChoice choice, com.mygdx.safe.MainGameGraph.Conditions c, String treeID) {

        boolean executed=true;

        String TreeID = "" + treeID;
        int TreeNumNode = 0 + allShooterTrees.get(TreeID).currentNode.numNode;

        if (allShooterTrees.get(TreeID).getNode(TreeNumNode).getChildNodes().size > 0) {
            for (int i = 0; i < allShooterTrees.get(TreeID).getNode(TreeNumNode).getChildNodes().size; i++) {
                // EXECUTION OF INSTRUCTION:
                boolean b;
                //EVALUATE counterinstruction


                g.println(TAG + "  COUNTER EXECUTION -- TREE ID: " + TreeID +
                        "  --  COUNTER COMPARATION:  " + allShooterTrees.get(TreeID).getNode(TreeNumNode).getName() +
                        "  --  NUM CHILDS: " + allShooterTrees.get(TreeID).getNode(TreeNumNode).getChildNodes().size + "   " +
                        "  --  COUNTER COMPARATION CHILD TO EXECUTE: " + allShooterTrees.get(TreeID).getNode(TreeNumNode).getChildNodes().get(i).getName());

                allShooterTrees.get(TreeID).setCurrentNode(allShooterTrees.get(TreeID).getNode(TreeNumNode).getChild(i));

                String childName = allShooterTrees.get(TreeID).currentNode.name.split("#")[0];
                String instruction = allShooterTrees.get(TreeID).currentNode.name;

                if (childName.contains("DIRECT") && !childName.contains("DIRECTION")) {
                    //DirectExecutor
                    b = direct_executor(choice, c, instruction, TreeID);
                    executed = executed && b;
                } else if (childName.contains("TIMER")) {
                    //TimerExecutor
                    b = timer_executor(choice, c, instruction, TreeID);
                    executed = executed && b;
                } else if (childName.contains("RANDOM")) {
                    //RandomExecutor
                    b = random_executor(choice, c, instruction, TreeID);
                    executed = executed && b;
                } else if (childName.contains("EVALUATOR")) {
                    //EvaluatorExecutor
                    b = evaluator_executor(choice, c, instruction, TreeID);
                    executed = executed && b;
                } else if (childName.contains("COUNTER")) {
                    //CounterExecutor
                    b = counter_executor(choice, c, instruction, TreeID);
                    executed = executed && b;
                } else if (childName.contains("SEQUENCE")) {
                    b = sequence_executor(choice, instruction, TreeID);
                    executed = executed && b;
                } else if(childName.contains("CHOICER")) {
                    b = choicer_executor(choice, c, instruction, TreeID);
                    executed = executed && b;
                } else {
                    //SimpleInstructionSplitter#Executor
                    b = InstructionSenderExecutor(choice, instruction, TreeID);
                    executed = executed && b;
                }
            }
        }

        return executed;
    }

    public String[] getInstructionsCounter(String[] s){
        String ret="";
        if(s.length>0){
            ret+=s[1];
            if(s.length>1) {
                for (int i = 2; i < s.length; i++) {
                    ret += "%" + s[i];
                }
            }
        }
        return ret.split("%");
    }

    public boolean timer_executor(com.mygdx.safe.MainGameGraph.ActionChoice choice, com.mygdx.safe.MainGameGraph.Conditions c, String timer, String treeID){


        boolean executed=true;
        // //TIMER_NAME TIME PRE INSTRUCTION POST INSTRUCTION
        //      0         1          2                3

        String TreeID = "" + treeID;
        int TreeNumNode = 0 + allShooterTrees.get(TreeID).currentNode.numNode;

        if(c.with_timers){

            g.println(TAG + "  TREE ID: " + TreeID +
                    "  --  TIMER:  " + allShooterTrees.get(TreeID).getNode(TreeNumNode).getName() +
                    "  --  NUM CHILDS: " + allShooterTrees.get(TreeID).getNode(TreeNumNode).getChildNodes().size);

            com.mygdx.safe.MainGameGraph.Timer tim = timers.get(timer+"#"+choice.getDestinyID());
            if(tim != null){
                g.println(TAG +"           [>] NODE_ID=["+choice.getDestinyID()+"]"+" TIMER_EXECUTOR : TRACE : NAME : [" + timer +"]");
                g.println("TIMER="+tim.show());
                //inst[0]=name
                //inst[1]=time
                //ints[2]=PRE CONDITION
                //inst[3]=POST CONDITION

                if((tim.getName() !=null) && (tim.getTime() > 0) && (tim.getActions().length == 2)) {

                    //tim.setActionChoice(choice);
                    tim.launch(choice, treeID);

                }else{

                    executed = false;
                    g.println("THE TIMER IS BADLY BUILT. [" + tim + "]");

                    if(tim.getTime() <= 0){
                        g.println("THE TIME ISN'T CORRECT. [" + tim.getTime() + "]");
                    }
                    if(tim.getActions().length != 2) {
                        g.println("THE PREANDPOSTCONDITIONS ARE INCORRECT. [" + tim.getPreAndPostAction() + "]");
                    }
                }
            }
            else{
                executed=false;
                g.println("NULL TIMER: [" + timer + "]");
            }
        }
        else{
            executed=false;
            g.println("THERE ARE NO TIMERS");

        }

        return executed;
    }

    public boolean random_executor(com.mygdx.safe.MainGameGraph.ActionChoice choice, com.mygdx.safe.MainGameGraph.Conditions c, String random, String treeID){
        //FORMAT: //RANDOM_NAME randomNumber RND RandomElection ActionConsequence RND RandomElection ActionConsequence
        //      0         1         2      3             4               5           6        7
        boolean executed=true;

        String TreeID = "" + treeID;
        int TreeNumNode = 0 + allShooterTrees.get(TreeID).currentNode.numNode;

        if(c.with_randoms){
            String[] inst=c.randoms.get(random);
            if(c.randoms.get(random)!=null){
                g.println(TAG +"           [>] NODE_ID=["+choice.getDestinyID()+"]"+" RANDOM_EXECUTOR : TRACE : NAME : [" + inst[0]+"]");
                //inst[0]=name
                //inst[1]=Random Number Generator Parameter
                //ints[2]=RND
                //inst[3]=Random Election
                //inst[4]=ActionConsequence
                if(inst.length>4) {

                    Random rnd = new Random();
                    int result = rnd.nextInt(Integer.valueOf(inst[1]));// <------------------------THIS IS THE RESULT OF RANDOM

                    g.println(" RND RESULT=" + result);

                    for (int i = 2; i < inst.length; i = i + 3) {
                        if(inst[i].contains("RND")){
                            if(result==Integer.valueOf(inst[i+1])){
                                boolean b;
                                //EVALUATE inst[i+2]

                                g.println(TAG + "  RANDOM EXECUTION -- TREE ID: " + TreeID +
                                        "  --  NUM CHILDS: " + allShooterTrees.get(TreeID).getNode(TreeNumNode).getChildNodes().size + "   " +
                                        "  --  RANDOM:  " + allShooterTrees.get(TreeID).getNode(TreeNumNode).getName() +
                                        "  --  RANDOM CHILD TO EXECUTE: " + allShooterTrees.get(TreeID).getNode(TreeNumNode).getChildNodes().get(result).getName());

                                allShooterTrees.get(TreeID).setCurrentNode(allShooterTrees.get(TreeID).getNode(TreeNumNode).getChild(result));

                                if (inst[i+2].contains("DIRECT") && !inst[i+2].contains("DIRECTION")) {
                                    //DirectExecutor
                                    b =direct_executor(choice, c, inst[i+2], TreeID);
                                    executed = executed && b;
                                } else if (inst[i+2].contains("TIMER")) {
                                    //TimerExecutor
                                    b = timer_executor(choice, c, inst[i+2], TreeID);
                                    executed = executed && b;
                                } else if (inst[i+2].contains("RANDOM")) {
                                    //RandomExecutor
                                    b = random_executor(choice, c, inst[i+2], TreeID);
                                    executed = executed && b;
                                } else if (inst[i+2].contains("EVALUATOR")) {
                                    //EvaluatorExecutor
                                    b = evaluator_executor(choice, c, inst[i+2], TreeID);
                                    executed = executed && b;
                                } else if (inst[i+2].contains("COUNTER")) {
                                    //CounterExecutor
                                    b = counter_executor(choice, c, inst[i+2], TreeID);
                                    executed = executed && b;
                                }  else if(inst[i+2].contains("SEQUENCE")){
                                    b = sequence_executor(choice, inst[i+2], TreeID);
                                    executed = executed && b;
                                } else if(inst[i+2].contains("CHOICER")) {
                                    b = choicer_executor(choice, c, inst[i+2], TreeID);
                                    executed = executed && b;
                                } else {
                                    //SimpleInstructionSplitter#Executor
                                    b =InstructionSenderExecutor(choice, inst[i+2], TreeID);
                                    executed = executed && b;
                                }

                            }
                        }else{
                            executed=false;
                            g.println("THE RANDOM INSTRUCTION IS BADLY BUILT. [" + random + "]");
                            break;
                        }
                    }
                }else{
                    executed=false;
                    g.println("THE RANDOM INSTRUCTION IS BADLY BUILT. [" + random + "]");
                }
            }else{
                executed=false;
                g.println("NULL RANDOM INSTRUCTION: [" + random + "]");
            }
        }else{
            executed=false;
            g.println("THERE ARE NO RANDOM INSTRUCTIONS");

        }
        return executed;
    }

    public boolean evaluator_executor(com.mygdx.safe.MainGameGraph.ActionChoice choice, com.mygdx.safe.MainGameGraph.Conditions c, String evaluator, String treeID){
        //FORMAT: evalXX evaluation TrueInstruction FalseInstruction
        //           0        1           2                3

        String TreeID = "" + treeID;
        int TreeNumNode = 0 + allShooterTrees.get(TreeID).currentNode.numNode;

        boolean executed=true;
        if(c.with_evaluators) {
            String[] inst = c._evaluators.get(evaluator);

            if (c._evaluators.get(evaluator)!= null) {

                g.println(TAG + "  EVALUATOR EXECUTION -- TREE ID: " + TreeID +
                        "  --  NUM CHILDS: " + allShooterTrees.get(TreeID).getNode(TreeNumNode).getChildNodes().size + "   " +
                        "  --  EVALUATOR:  " + allShooterTrees.get(TreeID).getNode(TreeNumNode).getName());

                g.println(TAG +"           [>] NODE_ID=["+choice.getDestinyID()+"]"+" EVALUATOR_EXECUTOR : TRACE : NAME : [" + inst[0]+"]");
                if (inst.length == 4) {

                    String[] evaluations = inst[1].split("-");
                    int i=3;//FALSE

                    //NAME AT [0]
                    //EVALUATION AT [1]

                    //EVALUATION ZONE:

                    //INSTRUCTION YES AT [2]
                    //INSTRUCTION FALSE AT[3]
                    if(evaluations.length == 1 || evaluations[0].contains("EXP_EVAL")){
                        if(special_evaluator_condition(choice,c,inst[1])){  // TRUE
                            i=2;
                        }
                    }
                    else if(evaluations.length > 1){
                        boolean bool = true;

                        if(evaluations[0].equalsIgnoreCase("AND")){
                            bool = true;
                            g.println(TAG + "EVALUATIONS AND EVALUATOR");
                            for(int j=1; j<evaluations.length; j++){
                                boolean aux = special_evaluator_condition(choice, c, evaluations[j]);
                                bool = bool && aux;
                            }
                        }
                        else if(evaluations[0].equalsIgnoreCase("OR")){
                            bool = false;
                            g.println(TAG + "EVALUATIONS OR EVALUATOR");
                            for(int j=1; j<evaluations.length; j++){
                                boolean aux = special_evaluator_condition(choice, c, evaluations[j]);
                                bool = bool || aux;
                            }
                        }
                        else{
                            g.println("BAD EVALUATIONS");
                            return false;
                        }

                        if(bool) i=2; //TRUE
                    }

                    //END OF EVALUATION ZONE
                    //EXECUTES TRUE or FALSE INSTRUCTION

                    boolean b;

                    if(i==2) g.println(TAG + "  RESULT OF EVALUATION : TRUE  --  EVALUATOR INSTRUCTION EXECUTION: ");
                    else g.println(TAG + "  RESULT OF EVALUATION : FALSE  --  EVALUATOR INSTRUCTION EXECUTION: ");


                    g.println(TAG + "  EVALUATOR:  " + allShooterTrees.get(TreeID).getNode(TreeNumNode).getName() +
                            "  --  SHOOTER CHILD TO EXECUTE: " + allShooterTrees.get(TreeID).getNode(TreeNumNode).getChildNodes().get(i-2).getName());

                    allShooterTrees.get(TreeID).setCurrentNode(allShooterTrees.get(TreeID).getNode(TreeNumNode).getChild(i-2));

                    if (inst[i].contains("DIRECT") && !inst[i].contains("DIRECTION")) {
                        //DirectExecutor
                        b = direct_executor(choice, c, inst[i], TreeID);
                        executed = executed && b;
                    } else if (inst[i].contains("TIMER")) {
                        //TimerExecutor
                        b = timer_executor(choice, c, inst[i], TreeID);
                        executed = executed && b;
                    } else if (inst[i].contains("RANDOM")) {
                        //RandomExecutor
                        b = random_executor(choice, c, inst[i], TreeID);
                        executed = executed && b;
                    } else if (inst[i].contains("EVALUATOR")) {
                        //EvaluatorExecutor
                        b = evaluator_executor(choice, c, inst[i], TreeID);
                        executed = executed && b;
                    } else if (inst[i].contains("COUNTER")) {
                        //CounterExecutor
                        b = counter_executor(choice, c, inst[i], TreeID);
                        executed = executed && b;
                    } else if(inst[i].contains("SEQUENCE")){
                        b = sequence_executor(choice, inst[i], TreeID);
                        executed = executed && b;
                    } else if(inst[i].contains("CHOICER")) {
                        b = choicer_executor(choice, c, inst[i], TreeID);
                        executed = executed && b;
                    } else {
                        //SimpleInstructionSplitter#Executor
                        b = InstructionSenderExecutor(choice, inst[i], TreeID);
                        executed = executed && b;
                    }


                } else {
                    executed=false;
                    g.println("THE EVALUATOR INSTRUCTION IS BADLY BUILT. [" + evaluator + "]");
                }
            } else {
                executed=false;
                g.println("NULL EVALUATOR INSTRUCTION: [" + evaluator + "]");
            }
        }else{
            executed=false;
            g.println("THERE ARE NO EVALUATOR INSTRUCTIONS");
        }

        return executed;
    }

    public boolean special_evaluator_condition(ActionChoice choice,Conditions c, String evaluation){
        boolean resultEvaluation=false;

        g.println(TAG +"           [>] NODE_ID=["+choice.getDestinyID()+"]"+" SPECIAL_EVALUATOR_CONDITION : TRACE : EVALUATION=[" + evaluation+"]");
        String[] ev = evaluation.split("#");


        //unchecked
        /*if(ev[0].equalsIgnoreCase("INVENTORY") && ev.length>1) {

              // PRINTS THE KEYSET INVENTORY LIST
              for(String s: g.m.invUI.get_completeItemList().keySet()){
                g.printlns( TAG + " ITEM:" +s);
            }
            if (ev[1].equalsIgnoreCase("HAS_ITEM") && g.m.invUI.get_completeItemList()!=null && g.m.invUI.get_completeItemList().get(ev[2])!=null){
                if(ev.length == 3 && g.m.invUI.get_completeItemList().get(ev[2]).get_itemQuantity() > 0) {
                    resultEvaluation = true;
                } else if(ev.length == 4){
                    resultEvaluation = (g.m.invUI.get_completeItemList().get(ev[2]).get_itemQuantity() >= Integer.valueOf(ev[3]));
                }
            }
        }
        //checked
        else*/
        if(ev[0].equalsIgnoreCase("ENTITY") && ev.length > 2){

            GameEntity ge;

            if(ev[1].equalsIgnoreCase("PLAYER")) ge = g.m.lvlMgr.getPlayer();
            else ge = g.m.lvlMgr.get_currentLvl().get_lvlEntities().get(ev[1]);

            if(ev[2].equalsIgnoreCase("IS_INSIDE_CIRCUNSCRIPTION") && ev.length == 4){

                Rectangle boundingBox = ge.getPhysicsComponent().get_collisionBoundingBox();
                Rectangle circuns = g.m.lvlMgr.get_mapManager().get_currentMap().getCircunscriptionRectangles().get(ge.getID() + "#" + ev[3]);


                Rectangle intersectionRectangle = new Rectangle();
                Intersector.intersectRectangles(circuns, ge.getPhysicsComponent().get_collisionBoundingBox(), intersectionRectangle);

                resultEvaluation = Math.abs(intersectionRectangle.width-boundingBox.width) < 0.01 &&
                        Math.abs(intersectionRectangle.height - boundingBox.height) < 0.01;

            }else if(ev[2].equalsIgnoreCase("IS_INSIDE_EVENT_AREA") && ev.length == 4){

                g.println(TAG + "      " +  (ge == null));

                Rectangle boundingBox = ge.getPhysicsComponent().get_collisionBoundingBox();
                Array<Rectangle> eventArea = g.m.lvlMgr.get_mapManager().get_currentMap().getPortalRectangles().get(ev[3]);

                for(Rectangle r: eventArea){
                    resultEvaluation = Intersector.intersectRectangles(boundingBox, r, new Rectangle());
                    if(resultEvaluation) break;
                }

            }else if(ev[2].equalsIgnoreCase("IS_CURRENT_CIRCUNSCRIPTION") && ev.length == 4){
                String currentCircuns = ge.getPhysicsComponent().get_currentCircuns();

                if(currentCircuns != null){
                    resultEvaluation = (currentCircuns.equalsIgnoreCase(ev[1] + "#" + ev[3]));
                }

            }else if(ev[2].equalsIgnoreCase("IS_STATE")){
                resultEvaluation = ge.getPhysicsComponent().get_currentState().equalsIgnoreCase(ev[3]);

            }else if(ev[2].equalsIgnoreCase("IS_DIRECTION")) {
                resultEvaluation = ge.getPhysicsComponent().get_currentDirection().equalsIgnoreCase(ev[3]);

            }else if(ev[2].equalsIgnoreCase("IS_POSITION")){

                if(ev[3].equalsIgnoreCase("PORTAL_LAYER")){
                    Rectangle r = g.m.lvlMgr.get_mapManager().get_currentMap().getPortalRectangles().get(ev[4]).get(0);
                    Vector2 position = new Vector2(r.x, r.y);
                    float dst = ge.getposition().dst2(position);

                    resultEvaluation = (Math.abs(dst) < 0.2);
                }
            }else if(ev[2].equalsIgnoreCase("IS_TARGET")){
                if(ge.getPhysicsComponent().getTarget() != null){
                    if(ev[3].equalsIgnoreCase("PLAYER")) resultEvaluation = (ge.getPhysicsComponent().getTarget().getID().contains("PLAYER"));
                    else resultEvaluation = (ge.getPhysicsComponent().getTarget().getID().equalsIgnoreCase(ev[3]));
                }
            }else if(ev[2].equalsIgnoreCase("IS_BOY")){
                    g.println(TAG + "********************** ISBOY =" + ac.PlayerType);
                    resultEvaluation= !(ProfileScren.ac.PlayerType.equalsIgnoreCase("girl"));

            }else if(ev[2].equalsIgnoreCase("IS_ALREADY_CREATED")){
                resultEvaluation= (ge != null);
            }

        }else if(ev[0].equalsIgnoreCase("GAMEGRAPH") && ev.length>1){

        }else if(ev[0].equalsIgnoreCase("ALL_CONFIGS_LEVEL")) {
            if (ev[1].equalsIgnoreCase("==")) {
                resultEvaluation = (ac.Level == Integer.valueOf(ev[2]));

            }
            else if(ev[1].equalsIgnoreCase("HAS_PROBES")){
                g.printlns(TAG + "  *********************" + ac.probes.toString());

                resultEvaluation = (ac.probes.size() != 0);
            }
        }
        else if(ev[0].equalsIgnoreCase("NODE")){
            if(ev[2].contains("COUNTER")){

                com.mygdx.safe.MainGameGraph.ToActionNode n=getNodeById(ev[1]);
                String evalSign = "";
                String counterName;

                if (ev[2].contains("==")) evalSign = "==";
                else if (ev[2].contains("<=")) evalSign = "<=";
                else if (ev[2].contains("<")) evalSign = "<";
                else if (ev[2].contains(">=")) evalSign = ">=";
                else if (ev[2].contains(">")) evalSign = ">";
                else {
                    g.println(TAG + "     ERROR:: WRONG SIGN COMPARATOR");
                }

                counterName= ev[2].split(evalSign)[0];

                int counterValue=n._visitCounter;

                if(n.getConditions().counterShooterRelation.get(counterName)!=null){
                    String shooter=n.getConditions().counterShooterRelation.get(counterName);
                    counterValue=n.getConditions()._shooters.get(shooter).getFirst();
                }

                resultEvaluation = var_evaluation((float)counterValue, Float.valueOf(ev[2].split(evalSign)[1]), evalSign);

            }
            else if(ev[2].equalsIgnoreCase("IS_BLOCKED")) {
                resultEvaluation = g.m.ggMgr.currentgg.getNodeById(ev[1])._blocked;
            }
            else if(ev[2].equalsIgnoreCase("IS_UNBLOCKED")){
                resultEvaluation = !g.m.ggMgr.currentgg.getNodeById(ev[1])._blocked;
            }
            else if(ev[2].equalsIgnoreCase("IS_ENABLED")){
                resultEvaluation =  g.m.ggMgr.currentgg.getNodeById(ev[1])._enable;
            }
        }else if(ev.length>2 && ev[0].equalsIgnoreCase("IS") && ev[2].equalsIgnoreCase("PROXIM_TO")){

            GameEntity ge1;
            GameEntity ge2;

            if(ev[1].contains("PLAYER")) ge1 = g.m.lvlMgr.getPlayer();
            else ge1 = g.m.lvlMgr.get_currentLvl().get_lvlEntities().get(ev[1]);

            if(ev[3].contains("PLAYER")) ge2 = g.m.lvlMgr.getPlayer();
            else ge2 = g.m.lvlMgr.get_currentLvl().get_lvlEntities().get(ev[3]);

            if(ge1 != null || ge2 != null)
                if(ge1.NumId != ge2.NumId) resultEvaluation = CollisionManager.checkPuntualProximityCollision(ge1,ge2);

        }

        else if(ev[0].equalsIgnoreCase("SOUNDMUSICMATION") && ev.length == 3){
            if (ev[2].equalsIgnoreCase("IS_PLAYING")){
                resultEvaluation = g.smm.getMusic(ev[1]).isPlaying();
            }
        }
        //unchecked

        else if(ev[0].equalsIgnoreCase("LEVEL_MANAGER") && ev.length>1) {

            if (ev[1].equalsIgnoreCase("IS_SELECTED") && g.m.lvlMgr.get_currentLvl().getSelectedGE()!=null && g.m.lvlMgr.get_currentLvl().getSelectedGE().getID().equalsIgnoreCase(ev[2])){
                resultEvaluation = true;
            }
            if (ev[1].equalsIgnoreCase("IS_CURRENT_MAP")){

                g.println("********************************" + g.m.lvlMgr.get_currentLvl().get_lvlID().split("_")[1]);
                resultEvaluation = (ev[2].equalsIgnoreCase(g.m.lvlMgr.get_currentLvl().get_lvlID().split("_")[1]));
            }

        }

        else if(ev[0].equalsIgnoreCase("HUD")){
            if(ev[1].equalsIgnoreCase("SPRITEMATION")){

                if(g.m.he.sclientHud.getSpritemationListenersHash().get(ev[2]) != null){
                    if(ev[3].equalsIgnoreCase("IS_SELECTED")){
                        resultEvaluation = g.m.he.getSclientHud().getSpritemationListenersHash().get(ev[2]).isSelected();
                    }
                    else if(ev[3].equalsIgnoreCase("IS_VISIBLE")){
                        resultEvaluation = g.m.he.sclientHud.getSpritemationListenersHash().get(ev[2]).getImage().isVisible();
                    }
                }

            }
            else if(ev[1].equalsIgnoreCase("SPRITEMATIONS")){
                if(ev[2].equalsIgnoreCase("EXIST_SELECTED"))
                    resultEvaluation = (g.m.he.sclientHud.getSelectedSpritemationListeners().size > 0);
            } //@HUD#SPRITEMATION_SELECTED#CONTAINS_AT_NAME#electronic
            else if(ev[1].equalsIgnoreCase("SPRITEMATION_SELECTED")){
                if(ev[2].equalsIgnoreCase("CONTAINS_AT_NAME")){
                    resultEvaluation=(g.m.he.sclientHud.getSelectedSpritemationListeners().size>0);



                    for(SpritemationListener l: g.m.he.sclientHud.getSelectedSpritemationListeners()){

                        g.printlns(TAG + "   " + l.getListenerOwner());
                        if(!l.getListenerOwner().contains(ev[3])){
                            resultEvaluation = false;
                            break;
                        }
                    }
                }
            }

        }else if(ev[0].equalsIgnoreCase("HOST_CLIENT") && ev.length>1){
            if(ev[1].equalsIgnoreCase("SPRITEMATION") && ev.length>3){
                if(ev[3].equalsIgnoreCase("IS_EXIST")){
                    resultEvaluation=false;
                    for(int i=0;i<g.m.gsys.spritemationsHost.getHostClientEntitySpritAnim().size;i++) {
                        resultEvaluation = resultEvaluation || g.m.gsys.spritemationsHost.getHostClientEntitySpritAnim().get(i)._withname.contains(ev[2]);
                    }
                }
            }
        }


        /*else if(ev[0].equalsIgnoreCase("HOTBAR") && ev.length>1){
            if(ev[1].equalsIgnoreCase("IS_ITEM") && g.m.invUI.get_hotBar().get_slotItem()!=null){
                resultEvaluation = g.m.invUI.get_hotBar().get_slotItem().get_itemID().equalsIgnoreCase(ev[2]);
            }
        }*/
        else if(evaluation.contains("VAR") && ev.length==1) {

            String[] evalinst;
            String evalSign = "";

            String typeVar1="";
            String typeVar2="";

            String var1="";
            String var2="";

            if (evaluation.contains("==")) evalSign = "==";
            else if (evaluation.contains("<=")) evalSign = "<=";
            else if (evaluation.contains("<")) evalSign = "<";
            else if (evaluation.contains(">=")) evalSign = ">=";
            else if (evaluation.contains(">")) evalSign = ">";
            else {
                g.println(TAG + "     ERROR:: WRONG SIGN COMPARATOR");
                exit(1);
            }

            if (!evalSign.equalsIgnoreCase("")) {

                evalinst = evaluation.split(evalSign);
                /*
                for(String st:evalinst){
                    g.println( TAG +" *************** EVALINST ************: " +st );
                }
                for(String st:c._varMap.keySet()){
                    g.println(TAG + " ********** c._varMap.keySet()=" + st + "   c._varMap.get(st).getFirst()=" + c._varMap.get(st).getFirst() + "  c._varMap.get(st).getSecond()=" +c._varMap.get(st).getSecond());
                }
                */

                if (evalinst.length == 2) {
                    if(evalinst[0].contains("VAR")) {

                        String[] prevar1 = evalinst[0].split("!");
                        if (prevar1.length == 1) {
                            typeVar1 = c._varMap.get(evalinst[0]).getSecond();
                            var1 = c._varMap.get(evalinst[0]).getFirst();
                        } else if (prevar1.length == 2) {
                            ToActionNode n = getNodeById(prevar1[1]);
                            typeVar1 = n.getConditions()._varMap.get(prevar1[0]).getSecond();
                            var1 = n.getConditions()._varMap.get(prevar1[0]).getFirst();

                        } else {
                            g.println(TAG + " FATAL ERROR!! EVALUATION OF MALFORMED FIRST VAR");
                            exit(1);
                        }

                        if (evalinst[1].contains("VAR")) {
                            String[] prevar2 = evalinst[1].split("!");
                            if(prevar2.length==1) {
                                typeVar2 = c._varMap.get(evalinst[1]).getSecond();
                                var2 = c._varMap.get(evalinst[1]).getFirst();
                            }else if(prevar2.length==2){
                                ToActionNode n = getNodeById(prevar2[1]);
                                typeVar2 = n.getConditions()._varMap.get(prevar2[0]).getSecond();
                                var2 = n.getConditions()._varMap.get(prevar2[0]).getFirst();

                            }else{
                                g.println(TAG + " FATAL ERROR!! EVALUATION OF MALFORMED SECOND VAR");
                                exit(1);
                            }
                        } else {
                            typeVar2 = typeVar1;
                            var2 = evalinst[1];
                        }


                        if ((var1 != null) && (var2 != null)) {
                            if (typeVar1.equalsIgnoreCase("Integer") || typeVar1.equalsIgnoreCase("Float"))
                                resultEvaluation = var_evaluation(Float.valueOf(var1.toString()), Float.valueOf(var2.toString()), evalSign);
                            else if (typeVar1.equalsIgnoreCase("String"))
                                resultEvaluation = var_evaluation(var1, var2, evalSign);
                        }
                    }else{
                        g.println(TAG + " FATAL ERROR!! EVALUATION NOT CONTAINS VALID VAR");
                        exit(1);
                    }

                }else{
                    g.println(TAG + " FATAL ERROR!! LENGHT ERROR. MALFORMED EVALUATION COMPARATION");
                    exit(1);
                }
            }
        }
        else if(ev[0].equalsIgnoreCase("EXP_EVAL") && ev.length>1){
            String expSign = ev[1];
            String expresion ="";
            float evalNum = 0.0f ;
            String evalSign = "";
            float result = 0;

            //GET A STRING WITHOUT THE EXPRESION SIGN AND THE KEY WORD EXP_EVAL
            for(int i=2; i< ev.length; i++){
                if(i!=2) expresion += "#"+ev[i];
                else expresion += ev[i];
            }

            expresion.substring(0, expresion.length()-2);

            String subexpresions[] = expresion.split("-");

            for(String s: subexpresions){

                if(s.contains("<=")) {
                    evalSign = "<=";
                    evalNum = Float.valueOf(s.split(evalSign)[1]);
                }else if(s.contains(">=")){
                    evalSign = ">=";
                    evalNum = Float.valueOf(s.split(evalSign)[1]);
                }else if(s.contains(">")){
                    evalSign = ">";
                    evalNum = Float.valueOf(s.split(evalSign)[1]);
                }else if(s.contains("<")){
                    evalSign = "<";
                    evalNum = Float.valueOf(s.split(evalSign)[1]);
                }else if(s.contains("==")){
                    evalSign = "==";
                    evalNum = Float.valueOf(s.split(evalSign)[1]);
                }

                String ss[];

                if(!evalSign.equalsIgnoreCase("")) ss = s.split(evalSign)[0].split("#");
                else ss= s.split("#");

                if(ss[0].equalsIgnoreCase("NODE")){
                    ToActionNode n = getNodeById(ss[1]);

                    if (ss[2].contains("COUNTER")) {
                        String counterName = ss[2];

                        int counterValue = n._visitCounter;

                        if (n.getConditions().counterShooterRelation.get(counterName) != null) {
                            String shooter = n.getConditions().counterShooterRelation.get(counterName);
                            counterValue = n.getConditions()._shooters.get(shooter).getFirst();
                        }


                        result = calculateOp(expSign, result, counterValue);
                    }

                }

                if(evalSign.equalsIgnoreCase("<=")) resultEvaluation = result <= evalNum;
                else if(evalSign.equalsIgnoreCase(">="))resultEvaluation = result >= evalNum;
                else if(evalSign.equalsIgnoreCase("<")) resultEvaluation = result < evalNum;
                else if(evalSign.equalsIgnoreCase(">"))resultEvaluation = result > evalNum;
                else if(evalSign.equalsIgnoreCase("=="))resultEvaluation = result == evalNum;
            }



        }
        return resultEvaluation;

    }

    public float calculateOp (String expSign, float temporalResult, float newValue){
        float result = 0;

        if(expSign.equalsIgnoreCase("+")) result = temporalResult + newValue;
        else if(expSign.equalsIgnoreCase("-")) result = temporalResult - newValue;
        else if(expSign.equalsIgnoreCase("*")) result = temporalResult * newValue;
        else if(expSign.equalsIgnoreCase("/")) result = temporalResult / newValue;

        return result;
    }



    public boolean var_evaluation(float var1, float var2, String evalSign){
        boolean resultEvaluation=false;

        if(evalSign.contains("==")){
            resultEvaluation= (var1 == var2);
        }else if(evalSign.contains("<=")){
            resultEvaluation= (var1 <= var2);
        }else if(evalSign.contains("<")){
            resultEvaluation= (var1 < var2);
        }else if(evalSign.contains(">=")){
            resultEvaluation= (var1 >= var2);
        }else if(evalSign.contains(">")){
            resultEvaluation= (var1 > var2);
        }

        return resultEvaluation;
    }

    public boolean var_evaluation(String var1, String var2, String evalSign){
        boolean resultEvaluation=false;

        if(evalSign.contains("==")){
            resultEvaluation=(var1.equalsIgnoreCase(var2));
        }

        return resultEvaluation;
    }

    public boolean sequence_executor(com.mygdx.safe.MainGameGraph.ActionChoice choice, String sequence, String treeID){

        int TreeNumNode = 0 + allShooterTrees.get(treeID).currentNode.numNode;

        g.println(TAG + "  SEQUENCE EXECUTION --- TREE ID: " + treeID +
                "  --  NUM CHILDS: " + allShooterTrees.get(treeID).getNode(TreeNumNode).getChildNodes().size + "   " +
                "  --  SEQUENCE:  " + allShooterTrees.get(treeID).getNode(TreeNumNode).getName());

        boolean executed=false;
        if(getNodeById(choice.getDestinyID()).getConditions().with_sequences) {
            com.mygdx.safe.MainGameGraph.SequenceInstructions sinstructions = _sequences.get(sequence + "#" + choice.getDestinyID());
            if (sinstructions != null) {
                sinstructions.launch(choice, treeID);
            }
        }
        return executed;
    }

    public boolean choicer_executor(com.mygdx.safe.MainGameGraph.ActionChoice choice, com.mygdx.safe.MainGameGraph.Conditions c, String choicer, String treeID){

        boolean executed=false;

        int TreeNumNode = 0 + allShooterTrees.get(treeID).currentNode.numNode;

        g.println(TAG + "  CHOICER EXECUTION --  TREE ID: " + treeID +
                "  --  CHOICER:  " + allShooterTrees.get(treeID).getNode(TreeNumNode).getName() +
                "  --  NUM CHILDS: " + allShooterTrees.get(treeID).getNode(TreeNumNode).getChildNodes().size);

        c._choicers.get(choicer).launch(choice, treeID);

        return executed;
    }

    public boolean direct_executor(com.mygdx.safe.MainGameGraph.ActionChoice choice, com.mygdx.safe.MainGameGraph.Conditions c, String direct, String treeID){

        //FORMAT: DIRECTxx inst1 inst2 inst3 ...
        //             0     1      2     3
        boolean executed=true;
        boolean badlybuilt=false;

        String TreeID = "" + treeID;
        int TreeNumNode = 0 + allShooterTrees.get(TreeID).currentNode.numNode;

        if(c.with_directInstructions) {

            g.println(TAG+" DIRECT EXECUTOR : INIT TRACE " + direct);
            String[] inst = c._directInstructions.get(direct);
            //NAME AT inst[0]

            if (c._directInstructions.get(direct) != null) {
                if (inst.length > 1) {
                    g.println(TAG +"           [>] NODE_ID=["+choice.getDestinyID()+"]"+" DIRECT_EXECUTOR : TRACE : NAME : [" + inst[0]+"]");
                    for (int i = 1; i < inst.length; i++) {
                        //INSTRUCTIONS AT inst[1] and NEXTs
                        boolean b;

                        String instType = inst[i].split("#")[0];

                        g.println(TAG + "  DIRECT EXECUTION -- TREE ID: " + TreeID +
                                "  --  DIRECT:  " + allShooterTrees.get(TreeID).getNode(TreeNumNode).getName() +
                                "  --  NUM CHILDS: " + allShooterTrees.get(TreeID).getNode(TreeNumNode).getChildNodes().size + "   " +
                                "  --  DIRECT CHILD TO EXECUTE: " + allShooterTrees.get(TreeID).getNode(TreeNumNode).getChildNodes().get(i-1).getName());

                        allShooterTrees.get(TreeID).setCurrentNode(allShooterTrees.get(TreeID).getNode(TreeNumNode).getChild(i-1));

                        if (instType.contains("DIRECT") && !instType.contains("DIRECTION")) {
                            //DirectExecutor
                            b = direct_executor(choice, c, inst[i], TreeID);
                            executed = executed && b;
                            //executed=false;
                            //badlybuilt=true;
                        } else if (instType.contains("TIMER")) {
                            //TimerExecutor
                            b = timer_executor(choice, c, inst[i], TreeID);
                            executed = executed && b;
                            //executed=false;
                            //badlybuilt=true;
                        } else if (instType.contains("RANDOM")) {
                            //RandomExecutor
                            b = random_executor(choice, c, inst[i], TreeID);
                            executed = executed && b;
                            //executed=false;
                            //badlybuilt=true;
                        } else if (instType.contains("EVALUATOR")) {
                            //EvaluatorExecutor
                            b = evaluator_executor(choice, c, inst[i], TreeID);
                            executed = executed && b;
                            //executed=false;
                            //badlybuilt=true;
                        } else if (instType.contains("COUNTER")) {
                            //CounterExecutor
                            b = counter_executor(choice, c, inst[i], TreeID);
                            executed = executed && b;
                            //executed=false;
                            //badlybuilt=true;
                        } else if(instType.contains("SEQUENCE")){
                            b = sequence_executor(choice, inst[i], TreeID);
                            executed = executed && b;
                        } else if(instType.contains("CHOICER")) {
                            b = choicer_executor(choice, c, inst[i], TreeID);
                            executed = executed && b;
                        }  else{

                            //SimpleInstructionSplitter#Executor
                            b = InstructionSenderExecutor(choice, inst[i], TreeID);
                            executed = executed && b;
                        }
                    }
                    if(badlybuilt){
                        executed=false;
                        g.println("THE DIRECT INSTRUCTION IS BADLY BUILT.: [" + direct + "]");
                    }
                } else {
                    executed=false;
                    g.println("THE DIRECT INSTRUCTION IS BADLY BUILT.: [" + direct + "]");
                }
            } else {
                executed=false;
                g.println("NULL DIRECT INSTRUCTION: [" + direct + "]");
            }
        }else{
            executed=false;
            g.println("THERE ARE NO DIRECT INSTRUCTIONS");
        }
        return executed;
    }



    public boolean execNode(com.mygdx.safe.MainGameGraph.ActionChoice choice, String shooter, String pointer){
        // EVALUATE ACCESS CONDITIONS, EXECUTE CONDITIONSID, EXECUTE COUNTERS, EXECUTE TIMERS, EXECUTE RANDOMS, EVALUATE ACCESS CONDITIONS

        boolean executed=false;
        if(choice!=null) {


            com.mygdx.safe.MainGameGraph.ToActionNode n = getNodeById(choice.getDestinyID());

            if(n!=null) {
                g.println(TAG +"           [>] NODE_ID=["+n.getID()+"]"+" EXEC_NODE : NODETYPEANDNAME: [" + n.get_nodeTypeAndName() +"]" );
                //IDS LOGIC: AT ENTRANCE IN THIS NODE, TRUES THE ID_ACTIVATION TABLE.
                //MODIFY ID ACTIVATION_HASHMAP
                //IF EXIST (choice.ORIGIN_ID() ) at TABLE, CHANGE THIS TO TRUE
                com.mygdx.safe.MainGameGraph.Conditions c=n.getConditions();
                if(c.with_idsCompare) {


                    if (c._id_activation.get(choice.getOriginID()) != null) {
                        c._id_activation.put(choice.getOriginID(), true);
                    }

                }

                // THE NODE IS ONLY EXECUTED IF NOT BLOCKED.
                if (!n._blocked) {
                    // IF NOT BLOCKED; THE NODE EXECUTED FORCE SHOOTER IF ID_COMPARATOR IS FALSE.
                    // IF NOT BLOCKED; THE NODE ONLY EXECUTED IF ID_CONDITIONS COMPARATION IS TRUE
                    // EXECUTE CONDITIONSID
                    g.println(TAG+ "           [>] NODE_ID=["+n.getID()+"]"+"EXEC_NODE: TRACE: UNBLOCKED : [" + n.get_nodeTypeAndName()+"]");
                    n._active=true;

                    setCurrentNode(n.ID, pointer);

                    /*if(shooter.contains("SELECT"))setCurrentNode(n.ID, "SELECT");
                    else if(shooter.contains("PROXIMITY")) setCurrentNode(n.ID, "PROXIMITY");
                    else if(shooter.contains("PORTAL_TAKE")) setCurrentNode(n.ID, "PORTAL_TAKE");
                    else setCurrentNode(n.ID, "none");*/

                    com.mygdx.safe.MainGameGraph.ActionChoice nextChoice=null;

                    //EVALUATE CHOICES OF NODE
                    if(get_actions(n.ID).size()==0){
                        // NONE
                    }else if(get_actions(n.ID).size()==1) {
                        nextChoice=get_actions(n.ID).get(0);
                    }



                    // EXECUTE INSTRUCTIONS IN THE NODE
                    executed = instructions_executor(choice, shooter);

                    // DECREMENTS THE COUNTER OF NODE IF NOT EXECUTED
                    if(!executed) {
                        n._visitCounter--;
                        g.println(TAG + "  DECREMENT (--) THE COUNTER OF NODE IF NOT EXECUTED :            [>] NODE_ID=[" + n.getID() + "] VISIT_COUNTER: TRACE : " + n._visitCounter);
                    }

                    g.println(TAG+ "EXEC-NODE: EXECUTED = " + executed);

                    if(executed && nextChoice!=null){

                        executed=ActionChoiceExecutor(nextChoice);
                    }
                } else {
                    g.println(TAG + "           [>] NODE_ID=["+n.getID()+"]"+" EXEC_NODE: TRACE, NODE" + n._nodeTypeAndName + "IS BLOCKED, AND NOT EXECUTE THIS");
                }
            }
        }
        // EXECUTE ALL POSIBLE CONTINUES: ACTIONCHOICES EXCEPT BLOCKEDS AND ITEMS ACTON, AND SPECIAL SELECTIONS Y PROXIMITYS
        return executed;
    }


    public boolean InstructionSenderExecutor(com.mygdx.safe.MainGameGraph.ActionChoice choice, String noSplitedInstructions, String treeID){

        g.println(TAG + "  INSTRUCTION SENDER EXECUTOR:::NOSPLITEDINSTRUCTIONS=["   + noSplitedInstructions+"]");

        boolean executed=false;
        if(noSplitedInstructions.contains("VAR")) noSplitedInstructions=var_replace(noSplitedInstructions,choice,treeID);
        String[] instructions= noSplitedInstructions.split("#");



        String TreeID = "" + treeID;
        int TreeNumNode = 0 + allShooterTrees.get(TreeID).currentNode.numNode;

        g.println(TAG + "            [>] NODE_ID=["+choice.getDestinyID()+"]"+" INSTRUCTION_SENDER: TRACE: noSplitedInstructions: [" +  noSplitedInstructions+"]");
        // EXECUTES INSTRUCTIONS CONTAINED IN s STRING[]
        if(instructions!=null) {
            g.println(TAG + "           [>] NODE_ID=["+choice.getDestinyID()+"]"+" INSTRUCTIONS:");
            for (int i = 0; i < instructions.length; i++) {
                g.print("[" + instructions[i] + "]");
            }
            g.print("\n");
            // PASS INSTRUCTION: LENGHT=1
            if (instructions.length == 1){
                if(instructions[0].equalsIgnoreCase("PASS") || instructions[0].equalsIgnoreCase("none")){
                    g.gm.sendMessage(g.messageOK(TreeID, TreeNumNode), TreeID, TreeNumNode);
                    return true;
                }
            }

            if(instructions.length > 1 && instructions[0].equalsIgnoreCase("GAMEGRAPH")){
                g.gm.sendMessage(noSplitedInstructions,TreeID, TreeNumNode);
                return true;
            }

            // GAMESYSTEM INSTRUCTION
            if(instructions.length >1) {
                String message="";
                if (instructions[0].contains("ENTITY") || instructions[0].contains("LEVEL_MANAGER") || instructions[0].contains("ENTITIES") ||
                        instructions[0].contains("INVENTORY") || instructions[0].equalsIgnoreCase("GAMESYSTEM") || instructions[0].equalsIgnoreCase("HOST_CLIENT")) {

                    //MESSAGE FOR GAMESYSTEM;
                    message = instructions[0];
                    for (int i = 1; i < instructions.length; i++) {
                        message +="#"+ instructions[i];
                    }
                    g.println(TAG + "            [>] NODE_ID=["+choice.getDestinyID()+"]"+"INSTRUCTION_SENDER: TRACE: MESSAGE FOR GAMESYSTEM: ["+message+"]");
                    g.m.gsys.receive(message,TreeID, TreeNumNode);
                    return true;
                } else if(instructions[0].contains("HUD")){
                    //MESSAGE FOR HUD ENTITY;

                    g.println(TAG + "            [>] NODE_ID=["+choice.getDestinyID()+"]"+"INSTRUCTION_SENDER: TRACE: MESSAGE FOR HUD: ["+message+"]");
                    g.m.he.receive(g.getPartialSplittedMessage(instructions, 1),TreeID, TreeNumNode);
                    return true;

                } else if(instructions.length==2 && instructions[0].contains("LOAD_ACTON")){

                    message+="ENTITY#ACTON#"+instructions[1]+"#"+"FALSE";
                    for(com.mygdx.safe.MainGameGraph.ActionChoice c:actions.get(choice.getDestinyID())){
                        if(!nodes.get(c.getDestinyID())._blocked){
                            String[] aux =nodes.get(c.getDestinyID())._nodeTypeAndName.split("#");
                            message+="#"+aux[aux.length-1];
                        }
                    }
                    g.println(TAG + "           [>] NODE_ID=["+choice.getDestinyID()+"]"+" MESSAGE FOR ACTON: [" +message +"]");
                    g.m.gsys.receive(message,TreeID, TreeNumNode);
                    return true;
                } else if(instructions[0].contains("DIALOG_GRAPH") && instructions.length>5){

                    //g.m.dgMgr.receive(GameSystem.getOrderMessage(instructions,1),TreeID, TreeNumNode);
                    return true;

                } /*else if(instructions[0].equalsIgnoreCase("INVENTORY")){
                    g.m.invUI.receive(GameSystem.getOrderMessage(instructions,1),TreeID, TreeNumNode);
                    return true;

                }*/ else{
                    message = instructions[0].split("_")[0];
                    for (int i = 1; i < instructions.length; i++) {
                        message +="#"+ instructions[i];
                    }
                    g.println(TAG + "           [>] NODE_ID=["+choice.getDestinyID()+"]"+" INSTRUCTION_SENDER: TRACE: BAD DIALOG_GRAPH MESSAGE: ["+ message+"]");
                }
            }
        }
        return executed;
    }

    public boolean ActionChoiceExecutor(com.mygdx.safe.MainGameGraph.ActionChoice choice){

        if(choice!=null) {
            String[] shooter = choice.get_instruction().split("%");
            // EVALUATOR OF shooter[1] AND ][2]
            g.println(TAG + "ACTION CHOICE EXECUTOR: TRACE : ID: " + choice.getID());

            return execNode(choice, shooter[0], "none");
        }
        else return false;
    }


    public boolean verifyNodeConection(){
        boolean verify=true;
        for(com.mygdx.safe.MainGameGraph.ActionChoice ac:actionsArray){
            g.println(TAG+" VERIFING NODE CONECTION :[" +ac.getID()+"]");
            if(getNodeById(ac.getOriginID())==null){
                System.out.println("\n"+TAG +" NODE VERIFY ERROR:["+ac.getOriginID()+"] NOT ORIGIN_ID EXISTS");
                verify=false;
                break;
            }
            if(getNodeById(ac.getDestinyID())==null){
                System.out.println("\n"+TAG +" NODE VERIFY ERROR:["+ac.getDestinyID()+"] NOT DESTINY_ID EXISTS");
                verify=false;
                break;
            }
        }

        return verify;
    }



    @Override
    public String toString(){
        StringBuilder outstring=new StringBuilder();
        int numberTotalactions=0;
        Set<String> keys =nodes.keySet();
        for(String id: keys) {
            outstring.append(String.format("[%s]:", id));
            outstring.append(nodes.get(id).getContent());

            for (com.mygdx.safe.MainGameGraph.ActionChoice action : actions.get(id)) {
                numberTotalactions++;
                outstring.append(String.format("-[%s]", action.getDestinyID()));
                outstring.append(action.getContent());
            }
            outstring.append(System.getProperty("line.separator"));
        }
        outstring.append(String.format("Number ToActionNodes: %d",nodes.size()));
        outstring.append(String.format(", Number of ActionChoices: %d",numberTotalactions));
        outstring.append(System.getProperty("line.separator"));

        return outstring.toString();

    }

    public HashMap<String, com.mygdx.safe.MainGameGraph.ActionChoice> getPointers() {
        return pointers;
    }

    public String toJson(){
        Json json=new Json();
        return json.prettyPrint(this);
    }

    public HashMap<String, com.mygdx.safe.MainGameGraph.SequenceInstructions> get_sequences() {
        return _sequences;
    }

    public void set_sequences(HashMap<String, com.mygdx.safe.MainGameGraph.SequenceInstructions> _sequences) {
        this._sequences = _sequences;
    }

    public HashMap<String, com.mygdx.safe.MainGameGraph.Timer> getTimers() {
        return timers;
    }

    public void setTimers(HashMap<String, com.mygdx.safe.MainGameGraph.Timer> timers) {
        this.timers = timers;
    }

    public HashMap<String, SpecialSuperTree> getAllShooterTrees() {
        return allShooterTrees;
    }

    public String var_replace(String nosplitedInstructions,ActionChoice c,String treeId) {
        String[] instructions = nosplitedInstructions.split("#");
        String nps = "";
        if (
                nosplitedInstructions.contains("ASIGN_VAR_VALUE") ||
                nosplitedInstructions.contains("GPRINT") ||
                nosplitedInstructions.contains("EXP_EVAL") ||
                nosplitedInstructions.contains("<") ||
                nosplitedInstructions.contains(">") ||
                nosplitedInstructions.contains("=")
            ){
            return nosplitedInstructions;
        }

        boolean varReplacing=false;
        if (instructions.length > 0) {
            for (int i = 0; i < instructions.length; i++) {

                if ( instructions[i].contains("VAR"))
                {
                        if(instructions[i].contains("++") || instructions[i].contains("--"))
                        {
                            if((instructions[i].endsWith("++")) || instructions[i].endsWith("--")){
                                String oper="";
                                //
                                //String[] init_split= (instructions[i].endsWith("++")?
                                //                                                (instructions[i].split(Pattern.quote("++"))):
                                //                                                (instructions[i].split(Pattern.quote("--"))));

                                String[] init_split=null;

                                if(instructions[i].endsWith("++")) init_split=instructions[i].split(Pattern.quote("++"));
                                else if(instructions[i].endsWith("--")) init_split=instructions[i].split(Pattern.quote("--"));

                                if(instructions[i].endsWith("++")) oper="++";
                                else if (instructions[i].endsWith("--")) oper="--";

                                if(init_split!=null && !oper.equalsIgnoreCase("")){
                                    varReplacing=true;
                                    String[] inst_split=init_split[0].split("!");
                                    if(inst_split.length<2){
                                        ToActionNode n = getNodeById(c.getDestinyID());
                                        g.print(TAG + "**** [ SINGLE VAR-REPLACING ] **** n="+c.getDestinyID()+" TOACTIONNODE N IS NULL="+(n==null));
                                        g.println(" **** [" + instructions[i] + "] **** ["+ n.getConditions()._varMap.get(instructions[i]).getFirst()+"]");
                                        String type=n.getConditions()._varMap.get(instructions[i]).getSecond();
                                        if(type.equalsIgnoreCase("Float") || type.equalsIgnoreCase("Integer")){
                                            Float f1=Float.valueOf(n.getConditions()._varMap.get(instructions[i]).getFirst());
                                            if (oper.equalsIgnoreCase("++")) f1++;
                                            else if (oper.equalsIgnoreCase("--")) f1--;
                                            instructions[i] = f1.toString();
                                        }
                                    }else if(inst_split.length==2){
                                        ToActionNode n = getNodeById(inst_split[1]);
                                        g.print(TAG + "  **** [ NODE ("+inst_split[1]+") VAR-REPLACING ] **** TOACTIONNODE N IS NULL="+(n==null));
                                        g.println(" **** [" + instructions[i] + "] **** ["+ n.getConditions()._varMap.get(inst_split[0]).getFirst()+"]");
                                        String type=n.getConditions()._varMap.get(instructions[i]).getSecond();
                                        if(type.equalsIgnoreCase("Float") || type.equalsIgnoreCase("Integer")){
                                            Float f1=Float.valueOf(n.getConditions()._varMap.get(instructions[i]).getFirst());
                                            if (oper.equalsIgnoreCase("++")) f1++;
                                            else if (oper.equalsIgnoreCase("--")) f1--;
                                            instructions[i] = f1.toString();
                                            n.getConditions()._varMap.get(instructions[i]).setFirst(instructions[i]);
                                        }
                                    }else{
                                        varReplacing=false;
                                    }
                                }

                            }else {
                                varReplacing = false;
                            }

                    }else{
                        varReplacing=true;
                        String[] inst_split=instructions[i].split("!");
                        if(inst_split.length<2){
                            ToActionNode n = getNodeById(c.getDestinyID());
                            g.print(TAG + "**** [ SINGLE VAR-REPLACING ] **** n="+c.getDestinyID()+" TOACTIONNODE N IS NULL="+(n==null));
                            g.println(TAG + " **** [" + instructions[i] + "] **** ["+ n.getConditions()._varMap.get(instructions[i]).getFirst()+"]");
                            instructions[i] = n.getConditions()._varMap.get(instructions[i]).getFirst();

                        }else if(inst_split.length==2){
                            ToActionNode n = getNodeById(inst_split[1]);
                            g.print(TAG + "  **** [ NODE ("+inst_split[1]+") VAR-REPLACING ] **** TOACTIONNODE N IS NULL="+(n==null));
                            g.println(TAG +  " **** [" + instructions[i] + "] **** ["+ n.getConditions()._varMap.get(inst_split[0]).getFirst()+"]");
                            instructions[i] = n.getConditions()._varMap.get(inst_split[0]).getFirst();
                        }else{
                            varReplacing=false;
                        }
                    }


                }
            }
            nps += instructions[0];
        }
        if (instructions.length > 1) {
            for (int i = 1; i < instructions.length; i++) {
                nps = nps + "#" + instructions[i];
            }
        }
        if(varReplacing) g.println(TAG + " **** [[[[ VAR - REPLACED ]]] ****  NEW NOSPLITED INSTRUCTIONS:" + nosplitedInstructions);
        return nps;
    }

}

