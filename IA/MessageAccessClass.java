package com.mygdx.safe.IA;

import com.mygdx.safe.Conversation.DialogsGraphManager;
import com.mygdx.safe.Entities.HUD_Entity;
import com.mygdx.safe.EntitySystems.GameSystem;
import com.mygdx.safe.InputProcessors.CameraInputProcessor;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;
import com.mygdx.safe.LevelManager;
//import com.mygdx.safe.UI.InventoryUI;
import com.mygdx.safe.MainGameGraph.GameGraphsManager;
import com.mygdx.safe.screens.MainGameScreen;

/**
 * Created by sensenom on 16/10/17.
 */

public class MessageAccessClass {

    //TAG
    private static final String TAG = MessageAccessClass.class.getSimpleName();

    //ASPECTS
    private GenericMethodsInputProcessor g;

    //REGISTERED G.PROCESS CLASS
    public DialogsGraphManager dgMgr;
    public LevelManager lvlMgr;
    public GameSystem gsys;
    public HUD_Entity he;
    //public InventoryUI invUI;
    public CameraInputProcessor cInProcess;
    public MainGameScreen mGS;
    public GameGraphsManager ggMgr;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public MessageAccessClass(GenericMethodsInputProcessor g){
        this.g = g;
    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS
    public GameGraphsManager getGamegraphManager() {
        return ggMgr;
    }

    public DialogsGraphManager getDgMgr() {
        return dgMgr;
    }

    public LevelManager getLvlMgr() {
        return lvlMgr;
    }

    public GameSystem getGsys() {
        return gsys;
    }

    public HUD_Entity getHe() {
        return he;
    }

    public CameraInputProcessor getcInProcess() {
        return cInProcess;
    }

    public MainGameScreen getmGS() {
        return mGS;
    }

    //public InventoryUI getInvUI() {     return invUI;   }

    //SETTERS
    public void SetMessageAccessClass(
            DialogsGraphManager dgMgr,
            LevelManager lvlMgr,
            GameSystem gsys,
            HUD_Entity he,
            //InventoryUI invUI,
            CameraInputProcessor cInProcess,
            MainGameScreen mGS,
            GameGraphsManager ggmgr

    ){
        this.dgMgr=dgMgr;
        this.lvlMgr=lvlMgr;
        this.gsys=gsys;
        this.he=he;
        //this.invUI=invUI;
        this.cInProcess=cInProcess;
        this.mGS=mGS;
        this.ggMgr=ggmgr;
    }

    public void setGamegraphManager(GameGraphsManager ggMgr) {
        this.ggMgr = ggMgr;
    }

    public void setDgMgr(DialogsGraphManager dgMgr) {
        this.dgMgr = dgMgr;
    }

    public void setLvlMgr(LevelManager lvlMgr) {
        this.lvlMgr = lvlMgr;
    }

    public void setGsys(GameSystem gsys) {
        this.gsys = gsys;
    }

    public void setHe(HUD_Entity he) {
        this.he = he;
    }

    public void setcInProcess(CameraInputProcessor cInProcess) {
        this.cInProcess = cInProcess;
    }

    public void setmGS(MainGameScreen mGS) {
        this.mGS = mGS;
    }

    //public void setInvUI(InventoryUI invUI) {      this.invUI = invUI;  }
}