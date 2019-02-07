package com.mygdx.safe.MainGameGraph;

import com.badlogic.gdx.utils.Array;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;

/**
 * Created by sensenom on 5/02/18.
 */

public class Choicer {

    public static String TAG=Choicer.class.getSimpleName();
    private GenericMethodsInputProcessor g;

    private Array<String> instructionChoices = new Array<String>();
    private String instruction;
    private String name;

    private boolean done = false;
    private boolean active = false;
    private boolean seted =false;

    private ToActionNode node;
    private com.mygdx.safe.MainGameGraph.ActionChoice ac;

    //private int childNumToExec;

    private String TreeID;
    private int TreeNumNode;

    // CONSTRUCTOR
    public Choicer(String choicer, GenericMethodsInputProcessor g) {
        this.name = choicer.split("@")[0];
        this.g=g;
        setInstructions(choicer.split("@"));
    }

    public void setInstructions(String[] instructions) {
        this.instruction = instructions[1];
        for(int i=2; i<instructions.length; i++){
            instructionChoices.add(instructions[i]);
        }

        seted = true;
    }

    public void launch(com.mygdx.safe.MainGameGraph.ActionChoice ac, String treeID){

        this.TreeID = "" + treeID;
        this.TreeNumNode = 0 + g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).currentNode.numNode;

        if(!done) {
            active=true;
            setActionChoice(ac);
            this.node = g.m.ggMgr.currentgg.getNodeById(ac.getDestinyID());

            instructionSequenceExecutor(ac, 0);
        }
    }

    // INSTRUCTION_SEQUENCE_EXECUTOR
    public boolean instructionSequenceExecutor(com.mygdx.safe.MainGameGraph.ActionChoice actionChoice, int childToExec){
        boolean executed = true;
        com.mygdx.safe.MainGameGraph.Conditions c=node.getConditions();
        if(!done && active) {

            boolean b;

            String inst;

            if(childToExec == 0) inst = instruction;
            else inst = instructionChoices.get(childToExec-1);

            g.println(TAG + "  CHOICER EXECUTION :  --  TREE ID: " + TreeID +
                    "  --  CHOICER:  " + g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).getNode(TreeNumNode).getName() +
                    "  --  NUM CHILDS: " + g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).getNode(TreeNumNode).getChildNodes().size +
                    "  --  CHOICER CHILD TO EXECUTE: " + g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).getNode(TreeNumNode).getChildNodes().get(childToExec).getName());

            g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).setCurrentNode(
                    g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).getNode(TreeNumNode).getChild(childToExec));

            String instType = inst.split("#")[0];

            if (instType.contains("DIRECT") && !instType.contains("DIRECTION")) {
                //DirectExecutor
                b = g.m.ggMgr.currentgg.direct_executor(actionChoice, c, inst,TreeID);
                executed = executed && b;
            } else if (instType.contains("TIMER")) {
                //TimerExecutor
                b = g.m.ggMgr.currentgg.timer_executor(actionChoice, c, inst,TreeID);
                executed = executed && b;
            } else if (instType.contains("RANDOM")) {
                //RandomExecutor
                b = g.m.ggMgr.currentgg.random_executor(actionChoice, c, inst,TreeID);
                executed = executed && b;
            } else if (inst.contains("EVALUATOR")) {
                //EvaluatorExecutor
                b = g.m.ggMgr.currentgg.evaluator_executor(actionChoice, c, inst,TreeID);
                executed = executed && b;
            } else if (instType.contains("COUNTER")) {
                //CounterExecutor
                b = g.m.ggMgr.currentgg.counter_executor(actionChoice, c, inst,TreeID);
                executed = executed && b;
            } else if (instType.contains("MOVEPROG")) {
                //MoveProgExecutor
                b = g.m.ggMgr.currentgg.InstructionSenderExecutor(actionChoice, inst,TreeID);
                executed = executed && b;
            } else if (instType.contains("SEQUENCE")) {
                b = g.m.ggMgr.currentgg.sequence_executor(actionChoice, inst,TreeID);
                executed = executed && b;
            } else if (inst.contains("CHOICER")) {
                b = g.m.ggMgr.currentgg.choicer_executor(actionChoice, c,  inst,TreeID);
                executed = executed && b;
            } else if(inst.contains("VAR") && inst.split("#").length==1 && !inst.contains("=")){
                int varNum;
                String varType = c._varMap.get(inst).getSecond();


                if(varType.equalsIgnoreCase("Integer")){
                    varNum= Integer.valueOf(c._varMap.get(inst).getFirst());
                    markChoiceToExecute(varNum);
                    g.gm.sendMessage(g.messageOK(TreeID, g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).currentNode.numNode),
                            TreeID, g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).currentNode.numNode);
                }
                else {
                    g.println(TAG + "   ERROR: BADLY TYPE FOR A VARIABLE IN TO ACTION NODE");
                }

            } else {
                b = g.m.ggMgr.currentgg.InstructionSenderExecutor(actionChoice, inst,TreeID );
                executed = executed && b;
            }
        }

        return executed;

    }

    public void markChoiceToExecute(int choiceNum){
        g.println(TAG + "  CHOICER INSTRUCTION TO EXECUTE: " + choiceNum + " ----------- TREE ID: " + TreeID +
                "  --  CHOICER:  " + g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).getNode(TreeNumNode).getName() +
                "  --  NUM CHILDS: " + g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).getNode(TreeNumNode).getChildNodes().size);

        if(active && (g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).getNode(TreeNumNode).getChildNodes().get(choiceNum+1) != null)){

            g.println(TAG + "  CHOICER INSTRUCTION TO EXECUTE: " + choiceNum + " ----------- TREE ID: " + TreeID +
                    "  --  CHOICER:  " + g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).getNode(TreeNumNode).getName() +
                    "  --  NUM CHILDS: " + g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).getNode(TreeNumNode).getChildNodes().size);

            g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).getNode(TreeNumNode).getChildNodes().get(choiceNum+1).setToExecute(true);
        }
    }


    public void finished(){
        if(!done) {
            done=true;
            active=false;
        }
    }


    public void setGenericMethodsInputProcessor(GenericMethodsInputProcessor g) {
        this.g = g;
    }

    public void setActionChoice(com.mygdx.safe.MainGameGraph.ActionChoice ac) {   this.ac = ac;  }

    public String getName() {
        return name;
    }

    public String getInstruction() {
        return instruction;
    }

    public Array<String> getInstructionChoices() {
        return instructionChoices;
    }

    public com.mygdx.safe.MainGameGraph.ActionChoice getAc() {
        return ac;
    }

}
