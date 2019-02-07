package com.mygdx.safe.MainGameGraph;


import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;

/**
 * Created by Boris.InspiratGames on 30/10/17.
 */

public class Timer {

    private static final String TAG = Timer.class.getSimpleName();


    GenericMethodsInputProcessor g;

    private static Integer i=0;
    private float time,resetTime;
    private String timeId;
    private String name;

    private ActionChoice actionChoice;
    private ToActionNode node;
    private String TreeID;
    private int TreeNumNode;

    //FORMAT: PREACTION#ENTITY @ POSTACTION#ENTITY

    // # <- Instruction Commands separator
    // $ <- Diferent type Instruction Separator
    // @ <- Same type instruction Separator

    private String preAndPostAction ="none@none";
    private String currentAction;
    private String[] actions;
    private boolean active=false;
    private boolean done=false;
    private float init,resetInit;
    private float limit,resetLimit;
    private int step,resetStep;
    private boolean increase;
    private boolean resetIncrease;// INCREMENT OR DECREMENT (-1 or +1)
    private boolean seted=false;

    public Timer(com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g){

        this.g = g;

        init=resetInit=0;
        limit=resetLimit=1;
        step=resetStep=0;
        increase = false;
        resetIncrease = false;
        time=init=resetTime;
        timeId=i.toString();
        i++;
    }

    public void launch(ActionChoice choice, String treeID){

        this.TreeID = "" + treeID;
        this.TreeNumNode = 0 + g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).currentNode.numNode;

        if(!done) {
            active = true;
            currentAction = actions[0];
            step = 0;

            actionChoice = choice;

            node = g.m.ggMgr.currentgg.getNodeById(actionChoice.getDestinyID());

            g.println(TAG + " LAUNCHING...   Limit:" +  limit + "   Time:" + time);

            g.println(TAG + "  LAUNCH TIMER:  " + g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).getNode(TreeNumNode).getName());
            g.println(TAG + "  TIMER EXECUTION -- TREE ID: " + TreeID +
                    "  --  TIMER:  " + g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).getNode(TreeNumNode).getName() +
                    "  --  NUM CHILDS: " + g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).getNode(TreeNumNode).getChildNodes().size +
                    "  --  STEP: PREINSTRUCTION");

            g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).setCurrentNode(
                    g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).getNode(TreeNumNode).getChild(step));

            instruction_executor(node.getConditions());
        }
    }

    public boolean instruction_executor(Conditions c){

        boolean executed = true;
        boolean b;

        g.println(TAG + " TIMER CHILD TO EXECUTE: " +
                g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).getNode(TreeNumNode).getName());

        if (currentAction.contains("DIRECT") && !currentAction.contains("DIRECTION")) {
            //DirectExecutor
            b = g.m.ggMgr.currentgg.direct_executor(actionChoice, c, currentAction, TreeID);
            executed = executed && b;
        } else if (currentAction.contains("TIMER")) {
            //TimerExecutor
            b = g.m.ggMgr.currentgg.timer_executor(actionChoice, c, currentAction, TreeID);
            executed = executed && b;
        } else if (currentAction.contains("RANDOM")) {
            //RandomExecutor
            b = g.m.ggMgr.currentgg.random_executor(actionChoice, c, currentAction, TreeID);
            executed = executed && b;
        } else if (currentAction.contains("EVALUATOR")) {
            //EvaluatorExecutor
            b = g.m.ggMgr.currentgg.evaluator_executor(actionChoice, c, currentAction, TreeID);
            executed = executed && b;
        } else if (currentAction.contains("COUNTER")) {
            //CounterExecutor
            b = g.m.ggMgr.currentgg.counter_executor(actionChoice, c, currentAction, TreeID);
            executed = executed && b;
        } else if (currentAction.contains("MOVEPROG")) {
            //MoveProgExecutor
            b = g.m.ggMgr.currentgg.InstructionSenderExecutor(actionChoice, currentAction, TreeID);
            executed = executed && b;
        } else if(currentAction.contains("SEQUENCE")){
            b = g.m.ggMgr.currentgg.sequence_executor(actionChoice, currentAction, TreeID);
            executed = executed && b;
        }else if(currentAction.contains("CHOICER")) {
            b = g.m.ggMgr.currentgg.choicer_executor(actionChoice, c, currentAction, TreeID);
            executed = executed && b;
        } else {
            b = g.m.ggMgr.currentgg.InstructionSenderExecutor(actionChoice, currentAction, TreeID);
            executed = executed && b;
        }

        return executed;
    }

    public void abort(){

        active = false;
        init = resetInit;
        limit = resetLimit;
        increase = resetIncrease;
        time = resetTime;
        currentAction = actions[1];
        step = 1;
        done = false;

        /*done=true;
        active = false;
        init = resetInit;
        limit = resetLimit;
        step = resetStep;
        time = resetTime;
        currentAction = actions[1];
        //g.m.ggMgr.currentgg.InstructionSenderExecutor(actionChoice, currentAction, currentTree);*/

    }

    public void invert(){
        float aux = limit;
        limit = init;
        init = aux;
        increase = !increase;
    }

    public void update(float delta){

        if(!done) {
            if (active) {
                if (increase) {
                    time += delta;
                    if (time > limit){
                        done = true;
                    }

                } else {
                    time -= delta;

                    if (time < limit){
                        done = true;
                    }

                }
            }
        }else {
            active = false;
            init = resetInit;
            limit = resetLimit;
            increase = resetIncrease;
            time = resetTime;
            currentAction = actions[1];
            step = 1;
            done = false;

            g.println(TAG + "  LAUNCH TIMER:  " + g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).getNode(TreeNumNode).getName());
            g.println(TAG + "  TIMER EXECUTION -- TREE ID: " + TreeID +
                    "  --  TIMER:  " + g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).getNode(TreeNumNode).getName() +
                    "  --  NUM CHILDS: " + g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).getNode(TreeNumNode).getChildNodes().size +
                    "  --  STEP: POSTINSTRUCTION");

            g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).setCurrentNode(
                    g.m.ggMgr.currentgg.allShooterTrees.get(TreeID).getNode(TreeNumNode).getChild(1));

            instruction_executor(node.getConditions());

        }

    }

    public void set(String name,float init, float limit, boolean increase,String preAndPostAction,boolean active){
        this.name=name;

        this.time = 0 + init; this.init = 0 + init; this.resetTime = 0 + init;
        this.limit= 0 + limit; this.resetLimit = 0 + limit;
        this.increase = increase;
        this.preAndPostAction = preAndPostAction;
        this.active = active;
        actions = preAndPostAction.split("@");
        currentAction = actions[0];
        seted = true;
    }

    public void set(String name,float time,String preAndPostAction){
        this.name=name;
        this.init = 0 + time; this.resetInit = 0 + time;
        this.time = 0 + time; this.resetTime = 0 + time;
        this.limit = 0; this.resetLimit = 0;
        this.active=false;
        setPreAndPostAction(preAndPostAction);
        seted = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrentAction() {
        return currentAction;
    }

    public void setCurrentAction(String currentAction) {
        this.currentAction = currentAction;
    }

    public float getTime() {
        return time;
    }


    public String getPreAndPostAction() {
        return preAndPostAction;
    }

    public void setPreAndPostAction(String preAndPostAction) {
        this.preAndPostAction = preAndPostAction;
        actions=preAndPostAction.split("@");
        currentAction=actions[0];
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public float getInit() {
        return init;
    }

    public void setInit(float init) {
        this.init = init;
        time=init;
    }

    public float getLimit() {
        return limit;
    }

    public void setLimit(float limit) {
        this.limit = limit;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String show(){
        String s="";
        if(seted) {
            s += "[NAME:" + name + "][INIT:" + init + "][TIME:" + time + "][LIMIT:" + limit + "][STEP:" + (step > 0 ? "increase" : "decrease") + "][ACTIVE:" + active + "]\n";
            s += "[ACTIONS: (pre and post]\n";
            s += actions[0]+","+actions[1];
            s += "\n";
        }

        if(s.length()==0){
            s="Not configured";

        }
        return s;
    }

    public String[] getActions() {
        return actions;
    }

    public ActionChoice getActionChoice() {
        return actionChoice;
    }

    public void setActionChoice(ActionChoice actionChoice) {
        this.actionChoice = actionChoice;
    }
}