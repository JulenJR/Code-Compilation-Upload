package com.mygdx.safe.Entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.mygdx.safe.Components.HudDataComponent;

import java.util.HashMap;

/**
 * Created by alumne_practiques on 11/11/17.
 */

public class GEActon extends GameEntity {

    //TAG
    private static final String TAG = GEActon.class.getSimpleName();

    //BOOLEANS
    boolean active = false;
    boolean actonIsDialog = false;
    protected boolean firstAdjustedTextBox = true;

    private String TreeID;
    private int TreeNumNode;

    //HUDDATACOMPONENT
    HudDataComponent hComp;

    //POSITIONS
    Vector2 talkingEntityPos = new Vector2();
    Vector2 dialog_1Pos = new Vector2();
    Vector2 dialog_2Pos= new Vector2();
    Vector2 dialog_3Pos= new Vector2();
    Vector2 img1Pos =new Vector2();
    Vector2 img2Pos =new Vector2();
    Vector2 img3Pos =new Vector2();

    //SIZES
    Vector2 talkingEntitySize= new Vector2();
    Vector2 dialog_1Size= new Vector2();
    Vector2 dialog_2Size= new Vector2();
    Vector2 dialog_3Size= new Vector2();
    Vector2 img1Size =new Vector2();
    Vector2 img2Size =new Vector2();
    Vector2 img3Size =new Vector2();


    /*_______________________________________________________________________________________________________________*/

    public GEActon(com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g, OrthographicCamera camera){
        super(g, camera);
        hComp = g.m.he.getHudActorDataComponent();
    }

    public boolean ConfigGameEntity(com.mygdx.safe.Components.InputComponent.InputMethod i, Vector2 _playerposition, com.mygdx.safe.sCache sCache, HashMap<String, com.mygdx.safe.nCache> nCacheList, String ID){
        return super.ConfigGameEntity(i, _playerposition, sCache, nCacheList, ID);
    }

    /*_______________________________________________________________________________________________________________*/

    //RECEIVE
    @Override
    public void receive (String[] message, String treeID, int treeNumNode){

        String s = "";
        //GENERAL FACTOR SCALE FOR TEXTACTORS

        float f=1.0f;
        // FACTOR SCALE TALKING ENTITY
        float dtxtx=1.2f;
        float dtxty=0.8f;
        // FACTOR SCALE DIALOG TEXTS
        float tktxtx=0.8f;
        float tktxty=0.6f;



        for(int i=0; i<message.length; i++){
            s+=message[i] + "#";
        }

        s = s.substring(0, s.length()-1);

        this.TreeID = treeID;
        this.TreeNumNode = treeNumNode;

        active = true;

        if(message[0].equalsIgnoreCase("LOAD_DIALOG")){


            //SET SIZE
            talkingEntitySize.set(200.0f, 60.0f);
            hComp.IMG_TALKING_ENTITY.setSize(talkingEntitySize.x, talkingEntitySize.y);

            dialog_1Size.set(800.0f, 200.0f);
            hComp.IMG_DIALOG_1.setSize(dialog_1Size.x, dialog_1Size.y);

            //SET POSITIONS
            talkingEntityPos.set(250.0f, 280.0f);
            hComp.IMG_TALKING_ENTITY.setPosition(talkingEntityPos.x, talkingEntityPos.y );
            g.getTextactors().get("TALKING_ENTITY").setTextActorPosition(talkingEntityPos.x , talkingEntityPos.y + (talkingEntitySize.y * 0.70f));

            dialog_1Pos.set(250.0f, 60.0f);
            hComp.IMG_DIALOG_1.setPosition(dialog_1Pos.x, dialog_1Pos.y);
            g.getTextactors().get("DIALOG_1").setTextActorPosition(dialog_1Pos.x , dialog_1Pos.y + (dialog_1Size.y * 0.75f));




            //SCALE
            g.getTextactors().get("TALKING_ENTITY").setScale(tktxtx*f,tktxty*f);
            g.getTextactors().get("DIALOG_1").setScale(dtxtx*f,dtxty*f);

            //SET TEXTS
            g.getTextactors().get("TALKING_ENTITY").setAdjustingText(message[1],talkingEntitySize.x,talkingEntitySize.y);
            g.getTextactors().get("DIALOG_1").setAdjustingText(message[2],dialog_1Size.x,dialog_1Size.y);

            //SET COLOR
            g.getTextactors().get("TALKING_ENTITY").setTint(Color.BLACK);
            g.getTextactors().get("DIALOG_1").setTint(Color.BLACK);

            //SET VISIBILITY
            hComp.IMG_TALKING_ENTITY.setVisible(true);
            hComp.IMG_DIALOG_1.setVisible(true);
            g.getTextactors().get("TALKING_ENTITY").setVisible(true);
            g.getTextactors().get("DIALOG_1").setVisible(true);

            //ADD AS PENDING INSTRUCTION "instruction@treeID@treeNumNode"
            if(getPendingOKinstructions().size() >0) getPendingOKinstructions().remove(0);
            getPendingOKinstructions().add(s + "@" + treeID + "@" + treeNumNode);


        }else if(message[0].equalsIgnoreCase("LOAD_DIALOG_CHOICES")){

            //SET TALKING ENTITY
            talkingEntitySize.set(200.0f, 60.0f);
            hComp.IMG_TALKING_ENTITY.setSize(talkingEntitySize.x, talkingEntitySize.y);

            talkingEntityPos.set(250.0f, 280.0f);
            hComp.IMG_TALKING_ENTITY.setPosition(talkingEntityPos.x, talkingEntityPos.y );

            g.getTextactors().get("TALKING_ENTITY").setTextActorPosition(talkingEntityPos.x , talkingEntityPos.y + (talkingEntitySize.y * 0.70f));
            g.getTextactors().get("TALKING_ENTITY").setScale(tktxtx*f,tktxty*f);
            g.getTextactors().get("TALKING_ENTITY").setAdjustingText(message[1],talkingEntitySize.x,talkingEntitySize.y);
            g.getTextactors().get("TALKING_ENTITY").setTint(Color.BLACK);

            hComp.IMG_TALKING_ENTITY.setVisible(true);
            g.getTextactors().get("TALKING_ENTITY").setVisible(true);


            if(message.length == 4){

                //SET DIALOG_1
                dialog_1Size.set(375.0f, 200.0f);
                hComp.IMG_DIALOG_1.setSize(dialog_1Size.x, dialog_1Size.y);

                dialog_1Pos.set(250.0f, 60.0f);
                hComp.IMG_DIALOG_1.setPosition(dialog_1Pos.x, dialog_1Pos.y);

                g.getTextactors().get("DIALOG_1").setTextActorPosition(dialog_1Pos.x , dialog_1Pos.y + (dialog_1Size.y * 0.75f));
                g.getTextactors().get("DIALOG_1").setScale(dtxtx*f,dtxty*f);
                g.getTextactors().get("DIALOG_1").setAdjustingText(message[2],dialog_1Size.x,dialog_1Size.y);
                g.getTextactors().get("DIALOG_1").setTint(Color.BLACK);

                hComp.IMG_DIALOG_1.setVisible(true);
                g.getTextactors().get("DIALOG_1").setVisible(true);

                //SET DIALOG_2
                dialog_2Size.set(375.0f, 200.0f);
                hComp.IMG_DIALOG_2.setSize(dialog_2Size.x, dialog_2Size.y);

                dialog_2Pos.set(675.0f, 60.0f);
                hComp.IMG_DIALOG_2.setPosition(dialog_2Pos.x, dialog_2Pos.y);

                g.getTextactors().get("DIALOG_2").setTextActorPosition(dialog_2Pos.x , dialog_2Pos.y + (dialog_2Size.y * 0.75f));
                g.getTextactors().get("DIALOG_2").setScale(dtxtx*f,dtxty*f);
                g.getTextactors().get("DIALOG_2").setAdjustingText(message[3],dialog_2Size.x,dialog_2Size.y);
                g.getTextactors().get("DIALOG_2").setTint(Color.BLACK);

                hComp.IMG_DIALOG_2.setVisible(true);
                g.getTextactors().get("DIALOG_2").setVisible(true);
            }
            else if(message.length == 5){

                //SET DIALOG_1
                dialog_1Size.set(233.0f, 200.0f);
                hComp.IMG_DIALOG_1.setSize(dialog_1Size.x, dialog_1Size.y);

                dialog_1Pos.set(250.0f, 60.0f);
                hComp.IMG_DIALOG_1.setPosition(dialog_1Pos.x, dialog_1Pos.y);

                g.getTextactors().get("DIALOG_1").setTextActorPosition(dialog_1Pos.x , dialog_1Pos.y + (dialog_1Size.y * 0.75f));
                g.getTextactors().get("DIALOG_1").setScale(dtxtx*f,dtxty*f);
                g.getTextactors().get("DIALOG_1").setAdjustingText(message[2],dialog_1Size.x,dialog_1Size.y);
                g.getTextactors().get("DIALOG_1").setTint(Color.BLACK);


                hComp.IMG_DIALOG_1.setVisible(true);
                g.getTextactors().get("DIALOG_1").setVisible(true);

                //SET DIALOG_2
                dialog_2Size.set(234.0F, 200.0f);
                hComp.IMG_DIALOG_2.setSize(dialog_2Size.x, dialog_2Size.y);

                dialog_2Pos.set(513.0f, 60.0f);
                hComp.IMG_DIALOG_2.setPosition(dialog_2Pos.x, dialog_2Pos.y);

                g.getTextactors().get("DIALOG_2").setTextActorPosition(dialog_2Pos.x , dialog_2Pos.y + (dialog_2Size.y * 0.75f));
                g.getTextactors().get("DIALOG_2").setScale(dtxtx*f,dtxty*f);
                g.getTextactors().get("DIALOG_2").setAdjustingText(message[3],dialog_2Size.x,dialog_2Size.y);
                g.getTextactors().get("DIALOG_2").setTint(Color.BLACK);


                hComp.IMG_DIALOG_2.setVisible(true);
                g.getTextactors().get("DIALOG_2").setVisible(true);

                //SET DIALOG_3
                dialog_3Size.set(233.0F, 200.0f);
                hComp.IMG_DIALOG_3.setSize(dialog_3Size.x, dialog_3Size.y);

                dialog_3Pos.set(777.0f, 60.0f);
                hComp.IMG_DIALOG_3.setPosition(dialog_3Pos.x, dialog_3Pos.y);

                g.getTextactors().get("DIALOG_3").setTextActorPosition(dialog_3Pos.x , dialog_3Pos.y + (dialog_3Size.y * 0.75f));
                g.getTextactors().get("DIALOG_3").setScale(dtxtx*f,dtxty*f);
                g.getTextactors().get("DIALOG_3").setAdjustingText(message[4],dialog_3Size.x,dialog_3Size.y);
                g.getTextactors().get("DIALOG_3").setTint(Color.BLACK);


                hComp.IMG_DIALOG_3.setVisible(true);
                g.getTextactors().get("DIALOG_3").setVisible(true);
            }

            //ADD AS PENDING INSTRUCTION "instruction@treeID@treeNumNode"
            if(getPendingOKinstructions().size() >0) getPendingOKinstructions().remove(0);
            getPendingOKinstructions().add(s + "@" + treeID + "@" + treeNumNode);

            //g.printlns(TAG + "   " + getPendingOKinstructions().size() + "    " + getPendingOKinstructions().get(0).toString());

        }else if(message[0].equalsIgnoreCase("LOAD_ACTON")){

            Vector2 imgHudSize=new Vector2(hComp.IMG_HUD.getWidth(),hComp.IMG_HUD.getHeight());
            float ypos=200.0f;
            float imageSizex=190f;
            float imageSizey=190f;

            if(message.length ==3){
                // SET IMAGE1
                g.println(TAG + " ACTON WITH 1 ITEM 1: "+ message[2]);
                hComp.imageActonHash.get("ICON_"+message[2]).setFirst(0);
                hComp.imageActonHash.get("ICON_"+message[2]).getSecond().setVisible(true);
                //hComp.imageActonHash.get("ICON_"+message[2]).getSecond().setTouchable(Touchable.enabled);
                hComp.imageActonHash.get("ICON_"+message[2]).getSecond().setOrigin(Align.center);
                img1Size.set(imageSizex,
                             imageSizey
                            );
                hComp.imageActonHash.get("ICON_"+message[2]).getSecond().setSize(img1Size.x,img1Size.y);
                img1Pos.set((imgHudSize.x-img1Size.x)/2,ypos);
                hComp.imageActonHash.get("ICON_"+message[2]).getSecond().setPosition(img1Pos.x,img1Pos.y);



            }else if(message.length == 4){
                // SET IMAGE1
                g.println(TAG + " ACTON WITH 2 ITEM 1: "+ message[2]);
                hComp.imageActonHash.get("ICON_"+message[2]).setFirst(0);
                hComp.imageActonHash.get("ICON_"+message[2]).getSecond().setVisible(true);
                //hComp.imageActonHash.get("ICON_"+message[2]).getSecond().setTouchable(Touchable.enabled);
                hComp.imageActonHash.get("ICON_"+message[2]).getSecond().setOrigin(Align.center);
                img1Size.set(imageSizex,
                             imageSizey
                            );
                hComp.imageActonHash.get("ICON_"+message[2]).getSecond().setSize(img1Size.x,img1Size.y);
                img1Pos.set((imgHudSize.x-img1Size.x)/3,ypos);
                hComp.imageActonHash.get("ICON_"+message[2]).getSecond().setPosition(img1Pos.x,img1Pos.y);

                // SET IMAGE2
                g.println(TAG + " ACTON WITH 2 ITEM 2: "+ message[3]);
                hComp.imageActonHash.get("ICON_"+message[3]).setFirst(1);
                hComp.imageActonHash.get("ICON_"+message[3]).getSecond().setVisible(true);
                //hComp.imageActonHash.get("ICON_"+message[3]).getSecond().setTouchable(Touchable.enabled);
                hComp.imageActonHash.get("ICON_"+message[3]).getSecond().setOrigin(Align.center);
                img2Size.set(imageSizex,
                             imageSizey
                            );
                hComp.imageActonHash.get("ICON_"+message[3]).getSecond().setSize(img2Size.x,img2Size.y);

                img2Pos.set((imgHudSize.x-img2Size.x)/3*2,ypos);
                hComp.imageActonHash.get("ICON_"+message[3]).getSecond().setPosition(img2Pos.x,img2Pos.y);



            }else if(message.length == 5){
                // SET IMAGE1
                g.println(TAG + " ACTON WITH 3 ITEM 1: "+ message[2]);
                hComp.imageActonHash.get("ICON_"+message[2]).setFirst(0);
                hComp.imageActonHash.get("ICON_"+message[2]).getSecond().setVisible(true);
                //hComp.imageActonHash.get("ICON_"+message[2]).getSecond().setTouchable(Touchable.enabled);
                hComp.imageActonHash.get("ICON_"+message[2]).getSecond().setOrigin(Align.center);
                img1Size.set(imageSizex,
                             imageSizey
                            );
                hComp.imageActonHash.get("ICON_"+message[2]).getSecond().setSize(img1Size.x,img1Size.y);
                img1Pos.set((imgHudSize.x-img1Size.x)/4,ypos);
                hComp.imageActonHash.get("ICON_"+message[2]).getSecond().setPosition(img1Pos.x,img1Pos.y);

                // SET IMAGE2
                g.println(TAG + " ACTON WITH 3 ITEM 2: "+ message[3]);
                hComp.imageActonHash.get("ICON_"+message[3]).setFirst(1);
                hComp.imageActonHash.get("ICON_"+message[3]).getSecond().setVisible(true);
                //hComp.imageActonHash.get("ICON_"+message[3]).getSecond().setTouchable(Touchable.enabled);
                hComp.imageActonHash.get("ICON_"+message[3]).getSecond().setOrigin(Align.center);
                img2Size.set(imageSizex,
                             imageSizey
                            );
                hComp.imageActonHash.get("ICON_"+message[3]).getSecond().setSize(img2Size.x,img2Size.y);
                img2Pos.set((imgHudSize.x-img2Size.x)/4*2,ypos);
                hComp.imageActonHash.get("ICON_"+message[3]).getSecond().setPosition(img2Pos.x,img2Pos.y);

                // SET IMAGE3
                g.println(TAG + " ACTON WITH 3 ITEM 3: "+ message[4]);
                hComp.imageActonHash.get("ICON_"+message[4]).setFirst(2);
                hComp.imageActonHash.get("ICON_"+message[4]).getSecond().setVisible(true);
                //hComp.imageActonHash.get("ICON_"+message[4]).getSecond().setTouchable(Touchable.enabled);
                hComp.imageActonHash.get("ICON_"+message[4]).getSecond().setOrigin(Align.center);
                img3Size.set(imageSizex,
                             imageSizey
                            );
                hComp.imageActonHash.get("ICON_"+message[4]).getSecond().setSize(img3Size.x,img3Size.y);
                img3Pos.set((imgHudSize.x-img3Size.x)/4*3,ypos);
                hComp.imageActonHash.get("ICON_"+message[4]).getSecond().setPosition(img3Pos.x,img3Pos.y);

            }

            //ADD AS PENDING INSTRUCTION "instruction@treeID@treeNumNode"
            if(getPendingOKinstructions().size() >0) getPendingOKinstructions().remove(0);
            getPendingOKinstructions().add(s + "@" + treeID + "@" + treeNumNode);

        }
    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS


    public boolean isDialogON() {
        return actonIsDialog;
    }


    public boolean isFirstAdjustedTextBox() {
        return firstAdjustedTextBox;
    }


    public String getTreeID() {
        return TreeID;
    }

    public int getTreeNumNode() {
        return TreeNumNode;
    }

    //SETTERS

    public void setDialogON(boolean dialogON) {
        this.actonIsDialog = dialogON;
    }

    public void setFirstAdjustedTextBox(boolean firstAdjustedTextBox) {
        this.firstAdjustedTextBox = firstAdjustedTextBox;
    }

    public void setTreeID(String treeID) {
        TreeID = treeID;
    }

    public void setTreeNumNode(int treeNumNode) {
        TreeNumNode = treeNumNode;
    }

    public boolean isActive() {
        return active;
    }
}
