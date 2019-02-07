package com.mygdx.safe.MainGameGraph;

import com.badlogic.gdx.utils.Array;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;

/**
 * Created by Boris.Escajadillo on 26/01/18.
 */

public class SpecialSuperTree {
    public static final String TAG=SpecialSuperTree.class.getSimpleName();
    protected String description="";
    protected Array<TreeNode> nodes;
    TreeNode rootNode=null;
    protected TreeNode currentNode;
    protected Integer numNodes=0;
    protected GenericMethodsInputProcessor g;

    public SpecialSuperTree() {

    }
    public SpecialSuperTree(GenericMethodsInputProcessor g, String description) {
        setGenericMethodInputProcessor(g);
        this.description = description;
        this.nodes = new Array<TreeNode>();
    }

    public void setGenericMethodInputProcessor(GenericMethodsInputProcessor g) {
        this.g = g;
    }

    public void setRoot(Boolean data, String name){
        if(nodes.size==0) {
            nodes.add(new TreeNode(data,name, null, 0));
            rootNode = nodes.get(0);
            currentNode = rootNode;

        }
    }

    public void markNodeToExec(int nodeID){
        for(TreeNode n:nodes){
            if(n.getNumNode()==nodeID){
                n.setToExecute(true);
            }
        }
    }

    public void resetTree(boolean increaseCounter){

        Conditions c = g.m.ggMgr.getCurrentgg().getNodeById(description.split("%")[0]).getConditions();

        for(String s: g.m.ggMgr.currentgg.timers.keySet()){
            if(s.contains(description.split("%")[0])) g.m.ggMgr.currentgg.timers.get(s).abort();
        }

        rootNode.resetNode();

        g.printlns(TAG + "    " + description);


        currentNode= rootNode;

        g.println(TAG + "  -- " + description + "  RESET TREE EXECUTION OK!!" + c._shooters.get(currentNode.getName()).getFirst() );

        if(increaseCounter){
            c._shooters.get(currentNode.getName()).setFirst(c._shooters.get(currentNode.getName()).getFirst() + 1);
            g.sendIntructionOK("EXECUTE#" +  description.split(("%"))[0] + "#" + currentNode.getName(), g.m.ggMgr.getPendingOKinstructions(), null);

        }
    }


    public boolean setCurrentNode(TreeNode node){
        boolean ok=false;
        for(TreeNode n:nodes){
            if (node.equals(n)){
                currentNode=n;
                ok=true;
            }
        }
        if(!ok){
            g.println(TAG+" ERROR: NOT CORRECT ASIGN NODE");
            g.println(showCurrentNode());
        }
        return ok;
    }

    public boolean setCurrentNode(Integer nodeNumber){
        boolean ok=false;
        for(TreeNode n:nodes){
            if(n.getNumNode().equals(nodeNumber)){ // REPLACED WITH EQUALS(...) (NOT ==) BY ANDROIDLINT
                currentNode=n;
                ok=true;

            }
        }
        if(!ok){
            g.println(TAG+" ERROR: NOT CORRECT ASIGN NODE");
            g.println(showCurrentNode());
        }
        return ok;
    }

    public boolean setCurrentNode(String name){
        boolean ok=false;
        for(TreeNode n:nodes){
            if(n.getName().equalsIgnoreCase(name)){
                currentNode=n;
                ok=true;
            }
        }
        if(!ok){
            g.println(TAG+" ERROR: NOT CORRECT ASIGN CURRENT NODE:");
            g.println(showCurrentNode());
        }
        return ok;
    }

    public SpecialSuperTree.TreeNode getNode(int numNode){
        return this.getNodes().get(numNode);
    }

    public String showTree(){
        return showSubTreeNode(rootNode)+"\n";
    }

    private String showSubTreeNode(TreeNode node){
        String s="";
        if(node!=null) {
            s+=showNode(node);
            for (int i = 0; i < node.getChildNodes().size; i++) {
                s+=showSubTreeNode(node.getChild(i));
            }
        }
        return s+"\n";
    }
    public String showNode(TreeNode node,int spacer){
        String sspacer="";
        String showString="";
        if(node.getNumNode()!=0) showString+="+";
        if(node.getNumNode()==0) {
            showString+=" ";
        }

        for(int i=0;i<spacer;i++){
            if(node.getNumNode()!=0) {
                sspacer+= "\t";
            }
        }

        showString +=  sspacer + " [ ID: " + node.getNumNode() + " ] \n" +
                       sspacer + " [ NAME: " + node.getName() + " ] \n" +
                       sspacer + " [ LEVEL: " +  node.getLevel() + "] \n" +
                       sspacer + " [ DATA: " + node.getData() + "] \n" +
                       sspacer + " [ PARENTNODE: " + (node.getParentNode()!=null?node.getParentNode().getName():"PARENTNODE: ROOTNODE DOESN'T HAVE PARENT") + "] \n" +
                       sspacer + " [ NUM OF CHILDS: : " + node.childNodes.size + "] \n" +
                       sspacer + " [ OK: " + node.getData() +  "] \n";
        return showString;
    }

    public String showNode(TreeNode node){
        return showNode(node,node.level);
    }

    public String showCurrentNode(){
        return showNode(currentNode,currentNode.level);
    }


        public Integer getNumNodes() {
        return numNodes;
    }

    public TreeNode getRootNode() {
        return rootNode;
    }

    public String getDescription() {
        return description;
    }

    public Array<TreeNode> getNodes() {
        return nodes;
    }

    // TREENODE_CLASS
    public class TreeNode{
        Boolean data = false;
        String name = "";
        TreeNode parentNode=null;
        Integer numNode=0;
        protected Integer level;
        Array<TreeNode> childNodes=new Array<TreeNode>();

        //COUNTER EXECUTION MARK
        protected boolean toExecute = false;

        public TreeNode(Boolean data, String name, TreeNode parentNode, Integer level){
            this.data = data;
            this.name = name;
            this.parentNode = parentNode;
            this.level=level;

            numNode = numNodes;
            numNodes++;


        }

        public void resetNode(){
            if(childNodes.size > 0){
                for(TreeNode child: childNodes){
                    child.resetNode();
                }
            }

            if(toExecute) toExecute = false;
            data = false;
        }

        public void addChildNode(Boolean data,String name){
            nodes.add(new TreeNode(data,name,this, this.level+1));
            this.childNodes.add(nodes.get(nodes.size-1));
        }

        public int getCurrentNodeIDFromChild (String name){
            int ID= -1;
            for(TreeNode child: getChildNodes()){
                if(child.getName().equalsIgnoreCase(name) && !child.getData()) {
                    ID = child.getNumNode();
                }
            }
            return ID;
        }

        public Integer getLevel() {
            return level;
        }

        public Boolean getData() {
            return data;
        }

        public String getName() {
            return name;
        }

        public Integer getNumNode() {
            return numNode;
        }

        public Array<TreeNode> getChildNodes() {
            return childNodes;
        }

        public TreeNode getChild(Integer i){

            //g.printlns(TAG + "     ::::  " + this.childNodes.size + "      ::::  " + i + "    ::::    " + this.getName());
            return this.childNodes.get(i);
        }

        public TreeNode getChild(String name){
            for(TreeNode child: getChildNodes()){
                if(child.getName().equalsIgnoreCase(name)) {
                    return child;
                }
            }
            return null;
        }

        public TreeNode getParentNode() {
            return parentNode;
        }

        public boolean isToExecute() {
            return toExecute;
        }

        public void setData(Boolean data) {
            this.data = data;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setParentNode(TreeNode parentNode) {
            this.parentNode = parentNode;
        }

        public void setToExecute(boolean toExecute) {
            this.toExecute = toExecute;
        }

        public void setLevel(Integer level) {
            this.level = level;
        }

        }

}
