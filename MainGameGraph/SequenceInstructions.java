package com.mygdx.safe.MainGameGraph;

import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;


/**
 * Created by Boris.Escajadillo on 24/01/18.
 */

public class SequenceInstructions {

    public static String TAG=SequenceInstructions.class.getSimpleName();
    private GenericMethodsInputProcessor g;
    //public enum Status{DISABLED,ENABLED,LAUNCHED,FINISHED,PROCESSING,ABORTED}
    //public enum Result{NOT_EXECUTED,OK,FAIL}

    private String[] instructions;
    //private Result[] results;
    //private Status[] status;
    private boolean continueWithFails=false;
    private Integer step=0;
    private boolean seted = false;
    private boolean done=false;
    private boolean active=false;

    private String name;
    private String currentInstruction;

    private com.mygdx.safe.MainGameGraph.ToActionNode node;
    private ActionChoice ac;

    private String TreeID;
    private int TreeNumNode;

    // CONSTRUCTOR
    public SequenceInstructions(String name, String[] instructions, com.mygdx.safe.MainGameGraph.ToActionNode n, GenericMethodsInputProcessor g) {
        this.name = name;
        this.g=g;
        node=n;
        setInstructions(instructions);
    }

    // INSTRUCTION_SEQUENCE_EXECUTOR
    public boolean instructionSequenceExecutor(ActionChoice actionChoice){
        g.println(TAG +"NODE_ID=["+actionChoice.getDestinyID()+"]"+" SEQUENCE_INSTRUCTION_EXECUTOR : TRACE : NAME : [" + currentInstruction+"]");
        boolean executed = true;
        Conditions c=node.getConditions();
        if(!done && active) {

            boolean b;
            String currentInstructionType=currentInstruction.split("#")[0];

            g.println(TAG + "  SEQUENCE EXECUTION :  --  TREE ID: " + TreeID +
                    "  --  SEQUENCE:  " + g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).getNode(TreeNumNode).getName() +
                    "  --  NUM CHILDS: " + g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).getNode(TreeNumNode).getChildNodes().size + "   " +
                    "  --  STEP: " + step +
                    "  --  SEQUENCE CHILD TO EXECUTE: " + g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).getNode(TreeNumNode).getChildNodes().get(step).getName());

            g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).setCurrentNode(
                    g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).getNode(TreeNumNode).getChild(step));

            if (currentInstructionType.contains("DIRECT") && !currentInstructionType.contains("DIRECTION")) {
                //DirectExecutor
                b = g.m.ggMgr.currentgg.direct_executor(actionChoice, c, currentInstruction,TreeID);
                executed = executed && b;
            } else if (currentInstructionType.contains("TIMER")) {
                //TimerExecutor
                b = g.m.ggMgr.currentgg.timer_executor(actionChoice, c, currentInstruction,TreeID);
                executed = executed && b;
            } else if (currentInstructionType.contains("RANDOM")) {
                //RandomExecutor
                b = g.m.ggMgr.currentgg.random_executor(actionChoice, c, currentInstruction,TreeID);
                executed = executed && b;
            } else if (currentInstructionType.contains("EVALUATOR")) {
                //EvaluatorExecutor
                b = g.m.ggMgr.currentgg.evaluator_executor(actionChoice, c, currentInstruction,TreeID);
                executed = executed && b;
            } else if (currentInstructionType.contains("COUNTER")) {
                //CounterExecutor
                b = g.m.ggMgr.currentgg.counter_executor(actionChoice, c, currentInstruction,TreeID);
                executed = executed && b;
            } else if (currentInstructionType.contains("MOVEPROG")) {
                //MoveProgExecutor
                b = g.m.ggMgr.currentgg.InstructionSenderExecutor(actionChoice, currentInstruction,TreeID);
                executed = executed && b;
            } else if (currentInstructionType.contains("SEQUENCE")) {
                b = g.m.ggMgr.currentgg.sequence_executor(actionChoice, currentInstruction,TreeID);
                executed = executed && b;
            } else if (currentInstructionType.contains("CHOICER")) {
                b = g.m.ggMgr.currentgg.choicer_executor(actionChoice, c,  currentInstruction,TreeID);
                executed = executed && b;
            }  else {
                b = g.m.ggMgr.currentgg.InstructionSenderExecutor(actionChoice, currentInstruction,TreeID );
                executed = executed && b;
            }
        }
        return executed;

    }

    public void launch(ActionChoice ac, String treeID){

        this.TreeID = "" + treeID;
        this.TreeNumNode = 0 + g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).currentNode.numNode;

        if(!done) {
            active=true;
            setActionChoice(ac);

            this.step = 0;
            currentInstruction=instructions[0];

            g.println(TAG + "  LAUNCH SEQUENCE:  " + g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).getNode(TreeNumNode).getName());

            instructionSequenceExecutor(ac);
        }
    }

    public void nextInstruction(int instructionIndex){
        if(active){
            this.step = instructionIndex;
            currentInstruction = instructions[instructionIndex];

            g.println(TAG + "  SEQUENCE NEXT INSTRUCTION ----------- TREE ID: " + TreeID +
                    "  --  NUM CHILDS: " + g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).getNode(TreeNumNode).getChildNodes().size + "   " +
                    "  --  SEQUENCE:  " + g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).getNode(TreeNumNode).getName());

            instructionSequenceExecutor(ac);
        }
    }



    public void okInstruction(String message){
        String[] okMessage=message.split("#");

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

    public void setActionChoice(ActionChoice ac) {   this.ac = ac;  }

    public void setInstructions(String[] instructions) {
        this.instructions = instructions;
        //results=new Result[instructions.length];
        //status=new Status[instructions.length];
        //for(int i=0;i<instructions.length;i++){
        //    results[i]=NOT_EXECUTED;
        //    status[i]=ENABLED;
        //}
        this.seted = true;
    }

    public String show(){
        String s="";
        if(seted) {
            s += "[NAME:" + name + "]\n";
            s += "[INSTRUCTIONS:]\n";
            for(int i=0; i<instructions.length; i++){
                s += (i+1) + " : " + instructions[i] + "\n";
            }
        }

        if(s.length()==0){
            s="Not configured";
        }
        return s;
    }

    public String[] getInstructions() {
        return instructions;
    }
    //
    //public Result[] getResults() {
    //    return results;
    //}
    //
    //public void setResults(Result[] results) {
    //    this.results = results;
    //}
    //
    //public Status[] getStatus() {
    //    return status;
    //}
    //
    //public void setStatus(Status[] status) {
    //    this.status = status;
    //}

    public boolean isContinueWithFails() {
        return continueWithFails;
    }

    public void setContinueWithFails(boolean continueWithFails) {
        this.continueWithFails = continueWithFails;
    }

    public Integer getStep() {
        return step;
    }

    public void setStep(Integer step) {
        this.step = step;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public com.mygdx.safe.MainGameGraph.ToActionNode getNode() {
        return node;
    }

    public void setNode(com.mygdx.safe.MainGameGraph.ToActionNode node) {
        this.node = node;
    }

    public ActionChoice getAc() {
        return ac;
    }

    public void setAc(ActionChoice ac) {
        this.ac = ac;
    }


}
