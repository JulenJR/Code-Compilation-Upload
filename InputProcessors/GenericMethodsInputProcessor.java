package com.mygdx.safe.InputProcessors;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.CpuSpriteBatch;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.mygdx.safe.AllConfigs;
import com.mygdx.safe.AllConfigsCharger;
import com.mygdx.safe.Conversation.TextActor;
import com.mygdx.safe.Conversation.TextActorConfig;
import com.mygdx.safe.Entities.GameEntity;
import com.mygdx.safe.IA.MessageAccessClass;
import com.mygdx.safe.Safe;
import com.mygdx.safe.SoundMusicMation;
import com.mygdx.safe.screens.MainGameScreen;
import com.mygdx.safe.screens.ProfileScren;

import java.util.ArrayList;
import java.util.HashMap;

public final class GenericMethodsInputProcessor {

    // RELEASED SWITCH
    private boolean released=false; // ( TRUE == NEVER G.PRINTING, FALSE == ALL G.PRINTING )

    //TAG
    private static final String TAG = GenericMethodsInputProcessor.class.getSimpleName();

    //GAMEDATA GENERAL VARIABLES
    public String age="0";

    // SOUNDMUSICMATIONS SOUNDMUSIC BANK

    public SoundMusicMation smm;
    public Safe game;

    public boolean musicMute = false;


    public boolean soundMute = false;

    //CAMERA
    private OrthographicCamera camera=new OrthographicCamera();

    //RENDERER && BATCH
    private ShapeRenderer shapeRenderer;
    private SkeletonRenderer skelrender=new SkeletonRenderer();
    private PolygonSpriteBatch polspritbatch=new PolygonSpriteBatch();
    private CpuSpriteBatch spriteBatch;

    //NEW STAGE
    //private Stage s;

    //TEXT ACTORS
    private TextActorConfig textActorConfig= com.mygdx.safe.EntityFactory.GetTextActorConfig();
    private HashMap<String,TextActor> textactors;

    //MESSAGE ACCESS CLASS
    public MessageAccessClass m;

    //GRAPH MESSAGE
    public GraphMessage gm;

    //MESSAGE
    public com.mygdx.safe.InputProcessors.Message message;

    //COMMAND
    private CommandProcess command;
    //BOOLEAN
    private boolean up;
    private boolean down;
    private boolean right;
    private boolean left;
    private boolean control;
    private boolean space;

    private boolean hasinputs = false;
    private boolean returnCommand=false;
    private boolean hasStageInput;
    private boolean commandExecutor=false;
    private boolean blockCommandInput = false;
    private boolean specificPrint = false;

    // SHOWING SETTING
    public boolean showingConditions = false;
    public boolean showingTrees = false;
    public boolean showingGameGraphs = false;
    public boolean showChildCreator = true;
    public boolean showGameGraphsCreatedPointers = false;

    // SUPERESPECIFIC PRINT: IF TRUES, EVALUATE ANY PRINT STRING FOR FINDING THE SPLIT ("#) superspecificPrint String;
    // IF FALSE, SPECIFIC PRINT CONTROL ALL SPECIFIC OR NOT PRINT STRINGS
    private String superspecificPrint = "GAMEGRAPH#GameGraph";

    private boolean superspecificPrintb=false;
    private boolean _hasGraphCommand=false;
    private boolean zooming;
    private boolean _cameraWithPlayer =false;

    private boolean tapMovement=true;

    //KEY CODES
    private int keyUpCode;
    private int keyDownCode;
    private char keyTypedCode;

    //ZOOM VALUES
    private float zoom;
    private float zoomFactor;
    private float zoomFactorMinDEFAULT;
    private float zoomFactorDownDEFAULT;

    private float zoomVariance;
    private float desiredZoom = -1.0f;
    private float zoomingTime = -1.0f;

    //RESIZE FACTOR

    private float resizeFactorX = -1;
    private float resizeFactorY = -1;


    //INPUT
    private float inputx;
    private float inputy;

    int tempcounter = 0;

    // DELTA
    public static final float DELTAUNITY=1.0f/60.0f;

    public static AllConfigs ac;
    public static AllConfigsCharger acCharger;

    /*_______________________________________________________________________________________________________________*/

    //STATIC METHODS

    // UNIVERSAL (!BLOCKCOMMAND) PRINT
    public static void print(String s, GenericMethodsInputProcessor g) {
        if (!g.specificPrint && !g.blockCommandInput) { System.out.print(s); }
    }

    public static void println(String s, GenericMethodsInputProcessor g) {
        if (!g.specificPrint && !g.blockCommandInput) { System.out.println(s); }
    }

    // UNIVERSAL SPECIFIC PRINT
    public static void prints(String s, GenericMethodsInputProcessor g) {
        if (!g.blockCommandInput && g.specificPrint) { System.out.print(s); }
    }

    public static void printlns(String s, GenericMethodsInputProcessor g) {
        if (!g.blockCommandInput && g.specificPrint) { System.out.println(s); }
    }
    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public GenericMethodsInputProcessor(Safe s) {


        game=s;
        spriteBatch=game.spriteBatch;
        this.smm=s.smm;
        ac= ProfileScren.ac;
        acCharger= ProfileScren.acCharger;

        this.shapeRenderer=new ShapeRenderer();
        this.textactors=new HashMap<String, TextActor>();

        zoom=0.9f;

        gm= new GraphMessage((this)); //GENERIC CUE FOR GRAPH MESSAGE OPERATIONS
        message = new com.mygdx.safe.InputProcessors.Message(this); // SUPER JSON->STRING STRING->JSON METHOD DATA INTERCHANGE
        command = new CommandProcess(this);

        m=new com.mygdx.safe.IA.MessageAccessClass(this);
        smm.searchForExist(this);
    }

    //UPDATE PROCESSOR
    public void updateprocessor() {

        if (zooming) {

            tempcounter++;
            hasinputs = true;


            // CONTROLLING ZOOMING
            if (zoomFactor == 1){

                printlns("**************** tempcounter: " + tempcounter);
                tempcounter = 0;
                zooming = false;
            }
            else {
                if (zoomFactor < 1 && camera.zoom <= 1/*0.85000193*/) {//camera.zoom<1.7) {zoom<0.85000193

                    zoom = ((desiredZoom==-1.0f)?(camera.zoom += 0.01):(camera.zoom += zoomVariance));
                }
                if (zoomFactor > 1 && camera.zoom >= 0.1) {//0.5250061) {//camera.zoom>0.3) {zoom>0.5250061

                    zoom = ((desiredZoom==-1.0f)?(camera.zoom -= 0.01):(camera.zoom -= zoomVariance));
                }

                if(desiredZoom != -1 && ((zoomFactor < 1 && zoom >= desiredZoom) || (zoomFactor > 1 && zoom <= desiredZoom))){

                    zoom = (camera.zoom = 0 + desiredZoom);
                    desiredZoom = -1;
                    zoomFactor = 1;
                    sendIntructionOK("CAMERA#ZOOM", m.gsys.getPendingOKinstructions(), null);
                }

                MainGameScreen.VIEWPORT.viewportWidth = MainGameScreen.VIEWPORT.virtualWidth * camera.zoom;
                MainGameScreen.VIEWPORT.viewportHeight = MainGameScreen.VIEWPORT.virtualHeight * camera.zoom;


            }


        }
    }

    /*_______________________________________________________________________________________________________________*/

    //MESSAGE OK
    public String messageOK (String treeID, int treeNumNode){


        if(treeID != null && treeNumNode != -1)
            this.m.ggMgr.getCurrentgg().getAllShooterTrees().get(treeID).getNode(treeNumNode).setData(true);

        return "GAMEGRAPH#OK";
    }

    //PENDING INSTRUCTIONS
    public void sendIntructionOK(String instructionType, ArrayList<String> pendingOKinstructions, GameEntity ge){

        String TreeID = "";
        int TreeNumNode = -1;
        int i = 0;



        for(String s: pendingOKinstructions){

            if(s.contains(instructionType)){

                TreeID = s.split("@")[1];
                TreeNumNode = Integer.valueOf(s.split("@")[2]);

                println(TAG + " SEND INSTRUCTION OK STRING:  " + s);

                if(ge != null) removeInstructionType(instructionType, pendingOKinstructions, ge);
                else pendingOKinstructions.remove(i);

                gm.sendMessage(messageOK(TreeID, TreeNumNode), TreeID, TreeNumNode);


                return;

            }
            i++;
        }
    }

    public boolean findInstruction (String instructionType, ArrayList<String> pendingOKinstructions){

        for(String s: pendingOKinstructions) if(s.contains(instructionType)) return true;

        return false;
    }

    public void removeInstructionType (String instructionType, ArrayList<String> pendingOKinstructions, GameEntity ge){

        int i = 0;

        for(String s: pendingOKinstructions){
            if(s.contains(instructionType)) {
                if(instructionType.equalsIgnoreCase("CHANGE_CIRCUNSCRIPTION")) {
                    ge.getPhysicsComponent().setChangingCircuns(false);
                }

                pendingOKinstructions.remove(i);
                return;
            }
            i++;
        }
        return;
    }

    //UPDATE HAS INPUTS
    public void updateHasInputs() {
        hasinputs = (up || down || left || right || zooming);
    }

    //PENFROID ROSE
    public boolean penfroidRose(float screenx, float screeny, int pointerdeaqui, int button) {
/*
        Vector2 playerPosition = m.lvlMgr.getPlayer().getPhysicsComponent().getGeCurrentPos();
        Vector2 centerCam = m.gsys.getCameraCenter();
        Vector2 screenPlayerPosition = new Vector2((playerPosition.x - centerCam.x + Gdx.graphics.getWidth()/(2/Map.UNIT_SCALE)) / Map.UNIT_SCALE,
                (playerPosition.y - centerCam.y + Gdx.graphics.getHeight()/(2/Map.UNIT_SCALE)) / Map.UNIT_SCALE);

        float tapmovx=screenx-screenPlayerPosition.x;
        float tapmovy=screenPlayerPosition.y-screeny;
        double angle=Math.atan2(tapmovy,tapmovx);

        if(angle>-0.34 && angle <0.34){
            right=true;
        }else if(angle>0.33 && angle<1.22){
            right=true;up=true;
        } else if(angle>1.21 && angle <1.91){
            up=true;
        }else if(angle>1.90 && angle <2.79){
            up=true;left=true;
        }else if((angle>2.78 && angle <3.14) || (angle>-3.14 && angle <-2.78)){
            left=true;
        }else if(angle>-2.79 && angle <-1.90){
            left=true;down=true;
        }else if(angle>-1.91 && angle <-1.21){
            down=true;
        }else if(angle>-1.22 && angle < -0.33){
            down=true;right=true;
        }
 */
       return true;
    }


    public String penfroidRose(float ox,float oy,float dx,float dy){

        //  PENFROID ROSE DIRECTION FOR EVENTS
        // dx, dy = FOR ACTION POSITION
        // ox, oy = THE OTHER  POSITION


        String str="";

        float tapmovx=dx-ox;
        float tapmovy=dy-oy;

        double angle=Math.atan2(tapmovy,tapmovx);

        if(angle>-0.34 && angle <0.34){
            str="RIGHT";
        }else if(angle>0.33 && angle<1.22){
            str="UPRIGHT";
        } else if(angle>1.21 && angle <1.91){
            str="UP";
        }else if(angle>1.90 && angle <2.79){
            str="UPLEFT";
        }else if((angle>2.78 && angle <3.14) || (angle>-3.14 && angle <-2.78)){
            str="LEFT";
        }else if(angle>-2.79 && angle <-1.90){
            str="DOWNLEFT";
        }else if(angle>-1.91 && angle <-1.21){
            str="DOWN";
        }else if(angle>-1.22 && angle < -0.33){
            str="DOWNRIGHT";
        }
        println(TAG+ " PENFROID ROSE POS: "+ str);
        return str;

    }

    /*public String penfroidRose(GameEntity ge1, GameEntity ge2 ) {

        //  PENFROID ROSE DIRECTION FOR EVENTS
        // x2, y2 = ENTITY FOR ACTION POSITION
        // x1,y1 = THE OTHER ENTITY POSITION

        // POSITION OF G1;
        float x1= ge1.getPhysicsComponent().get_eventArea().x;// + ge1.getPhysicsComponent().get_eventArea().width/2;
        float y1= ge1.getPhysicsComponent().get_eventArea().y;// + ge1.getPhysicsComponent().get_eventArea().height/2;
        // POSITON OF G2; (the entity around G1)
        float x2= ge2.getPhysicsComponent().get_eventArea().x;// + ge2.getPhysicsComponent().get_eventArea().width/2;
        float y2= ge2.getPhysicsComponent().get_eventArea().y;// + ge2.getPhysicsComponent().get_eventArea().height/2;

        return penfroidRose(x1,y1,x2,y2);

    }*/

    /*public String OppossitePenfroidRose(GameEntity ge1, GameEntity ge2 ){
        String s=penfroidRose(ge1,ge2);
        String r="";
        if(s.contains("RIGHT")){
            r="LEFT";
        }else if(s.contains("LEFT")){
            r="RIGHT";
        }else if(s.contains("UP")){
            r="DOWN";
        }else if(s.contains("DOWN")){
            r="UP";
        }else if(s.contains("UPLEFT")){
            r="DOWNRIGHT";
        }else if(s.contains("DOWNRIGHT")){
            r="UPLEFT";
        }else if(s.contains("DOWNLEFT")){
            r="UPRIGHT";
        }else if(s.contains("UPRIGHT")){
            r="DOWNLEFT";
        }
        printlns(TAG+ " OPPOSITE PENFROID ROSE POS: "+ r);
        return r;
    }*/

    //PARTIAL SPLITTED INSTRUCTIONS
    public String[] getPartialSplittedMessage (String[] fullMessage, int first, int last){

        int lenght = 0;

        if(first == last) lenght = 1;
        else if(first < last) lenght = last - first + 1;
        else{
            lenght = 0 + first;
            first = 0 + last;
            last = 0 + lenght;

            lenght = last - first + 1;
        }

        String splittedMessage[]= new String[lenght];

        for(int i=0; i<lenght; i++){
            splittedMessage[i] = fullMessage[first+i];
        }

        return splittedMessage;
    }

    public String[] getPartialSplittedMessage (String[] fullMessage, int first){
        return getPartialSplittedMessage (fullMessage,first, fullMessage.length-1);
    }

    public String StringMaker(String[] m, int init, int end){
        String s="";
        for(int i=init;i<end;i++){
            s+=m[i];
            if(i<end-1) s+="#";
        }
        return s;
    }


    /*_______________________________________________________________________________________________________________*/

    //GETTERS
/*
    public Stage getS() {
        return s;
    }
*/
    public com.mygdx.safe.InputProcessors.Message getMessage() {
        return message;
    }

    public com.mygdx.safe.IA.MessageAccessClass getMessageAccessClass() { return m; }

    public CpuSpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public boolean isHasStageInput() { return hasStageInput; }

    public boolean isUp() { return up; }

    public boolean isDown() { return down; }

    public boolean isLeft() { return left; }

    public boolean isRight() { return right;}

    public boolean isControl() { return control; }

    public boolean isCommandExecutor() { return commandExecutor; }

    public CommandProcess getCommand() {
        return command;
    }

    public boolean isSpace() {
        return space;
    }

    public boolean isHasinputs() {
        return hasinputs;
    }

    public boolean isReturnCommand() {
        return returnCommand;
    }

    public boolean isBlockCommandInput() {
        return blockCommandInput;
    }

    public boolean isSpecificPrint() {
        return specificPrint;
    }

    public boolean is_hasGraphCommand() {
        return _hasGraphCommand;
    }

    public boolean isZooming() {
        return zooming;
    }

    public boolean isTapMovement() {
        return tapMovement;
    }

    public int getKeyUpCode() {
        return keyUpCode;
    }

    public int getKeyDownCode() {
        return keyDownCode;
    }

    public char getKeyTypedCode() {
        return keyTypedCode;
    }

    public float getZoom() {
        return zoom;
    }

    public float getZoomFactor() {
        return zoomFactor;
    }

    public float getZoomFactorMinDEFAULT() {
        return zoomFactorMinDEFAULT;
    }

    public float getZoomFactorDownDEFAULT() {
        return zoomFactorDownDEFAULT;
    }

    public float getInputx() {
        return inputx;
    }

    public float getInputy() {
        return inputy;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    public SkeletonRenderer getSkelrender() {
        return skelrender;
    }

    public PolygonSpriteBatch getPolspritbatch() {
        return polspritbatch;
    }
 //NEW STAGE
    private Stage s;
    public TextActorConfig getTextActorConfig() {
        return textActorConfig;
    }

    public HashMap<String, TextActor> getTextactors() {
        return textactors;
    }

    public float getDesiredZoom() {
        return desiredZoom;
    }

    public float getZoomVariance() {
        return zoomVariance;
    }

    public float getResizeFactorX() {
        return resizeFactorX;
    }

    public float getResizeFactorY() {
        return resizeFactorY;
    }

    public float getZoomingTime() {
        return zoomingTime;
    }

    public boolean is_cameraWithPlayer() {
        return _cameraWithPlayer;
    }

    //SETTERS
/*
    public void setS(Stage s) {
        this.s = s;
    }
*/
    public void setMessage(com.mygdx.safe.InputProcessors.Message message) {
        this.message = message;
    }

    public void setSpriteBatch(CpuSpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
    }

    public void setInputx(float inputx) { this.inputx = inputx; }

    public void setInputy(float inputy) { this.inputy = inputy; }

    public void setHasStageInput(boolean hasStageInput) { this.hasStageInput = hasStageInput; }

    public void setUp(boolean up) { this.up = up; }

    public void setDown(boolean down) { this.down = down; }

    public void setLeft(boolean left) { this.left = left; }

    public void setRight(boolean right) { this.right = right; }

    public void setCommand(CommandProcess command) {
        this.command = command;
    }

    public void setControl(boolean control) {
        this.control = control;
    }

    public void setSpace(boolean space) {
        this.space = space;
    }

    public void setHasinputs(boolean hasinputs) {
        this.hasinputs = hasinputs;
    }

    public void setReturnCommand(boolean returnCommand) {
        this.returnCommand = returnCommand;
    }

    public void setCommandExecutor(boolean commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    public void setBlockCommandInput(boolean blockCommandInput) {
        this.blockCommandInput = blockCommandInput;
    }

    public void setSpecificPrint(boolean specificPrint) {
        this.specificPrint = specificPrint;
    }

    public void set_hasGraphCommand(boolean _hasGraphCommand) {
        this._hasGraphCommand = _hasGraphCommand;
    }

    public void setZooming(boolean zooming) {
        this.zooming = zooming;
    }

    public void setTapMovement(boolean tapMovement) {
        this.tapMovement = tapMovement;
    }

    public void setKeyUpCode(int keyUpCode) {
        this.keyUpCode = keyUpCode;
    }

    public void setKeyDownCode(int keyDownCode) {
        this.keyDownCode = keyDownCode;
    }

    public void setKeyTypedCode(char keyTypedCode) {
        this.keyTypedCode = keyTypedCode;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public void setZoomFactor(float zoomFactor) {
        this.zoomFactor = zoomFactor;
    }

    public void setZoomFactorMinDEFAULT(float zoomFactorMinDEFAULT) {
        this.zoomFactorMinDEFAULT = zoomFactorMinDEFAULT;
    }

    public void setZoomFactorDownDEFAULT(float zoomFactorDownDEFAULT) {
        this.zoomFactorDownDEFAULT = zoomFactorDownDEFAULT;
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    public void setShapeRenderer(ShapeRenderer shapeRenderer) {
        this.shapeRenderer = shapeRenderer;
    }

    public void setSkelrender(SkeletonRenderer skelrender) {
        this.skelrender = skelrender;
    }

    public void setPolspritbatch(PolygonSpriteBatch polspritbatch) {
        this.polspritbatch = polspritbatch;
    }

    public void setTextActorConfig(TextActorConfig textActorConfig) {
        this.textActorConfig = textActorConfig;
    }

    public void setTextactors(HashMap<String, TextActor> textactors) {
        this.textactors = textactors;
    }

    public void setDesiredZoom(float desiredZoom) {
        this.desiredZoom = desiredZoom;
    }

    public void setZoomVariance(float zoomVariance) {
        this.zoomVariance = zoomVariance;
    }

    public void setResizeFactorX(float resizeFactorX) {
        this.resizeFactorX = resizeFactorX;
    }

    public void setResizeFactorY(float resizeFactorY) {
        this.resizeFactorY = resizeFactorY;
    }

    public void setZoomingTime(float zoomingTime) {
        this.zoomingTime = zoomingTime;
    }

    public void set_cameraWithPlayer(boolean _cameraWithPlayer) {
        this._cameraWithPlayer = _cameraWithPlayer;
    }

    // UNIVERSAL (!BLOCKCOMMAND) PRINT
    public void println(String s) {
        if(!released && (supespecificPrintEvaluator(s) || !specificPrint) && !blockCommandInput) {
                System.out.println(s);
        }
    }

    public void print(String s) {
        if(!released && (supespecificPrintEvaluator(s) || !specificPrint ) && !blockCommandInput) {
                System.out.print(s);
        }
    }

    // UNIVERSAL SPECIFIC PRINT
    public void prints(String s) {
      if (!released && !blockCommandInput && specificPrint) {
                System.out.print(s);
      }

    }

    public void printlns(String s) {
        if (!released && !blockCommandInput && specificPrint) {
            System.out.println(s);
        }
    }

    public boolean supespecificPrintEvaluator(String imp){
        if(superspecificPrintb) {
            boolean ev = false;

            String[] sp = superspecificPrint.split("#");
            for(int i=0;i<sp.length;i++){
                if(imp.contains(sp[i])){
                    return true;
                }
            }
            return ev;
        }else return false;
    }
















}