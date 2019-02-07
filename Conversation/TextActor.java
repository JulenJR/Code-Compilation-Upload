package com.mygdx.safe.Conversation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;
import com.mygdx.safe.Map;
import com.mygdx.safe.screens.MainGameScreen;

/**
 * Created by Boris.InspiratGames on 15/09/17.
 */

public class TextActor extends Actor {

    //TAG
    private static final String TAG = TextActor.class.getSimpleName();

    //ASPECTS
    private com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g;

    //MATRIX
    private static Matrix4 IdentityMatrix = new Matrix4();
    private Matrix4 matrix=new Matrix4();

    //CAMERA
    private Camera camera;

    //POSITIONS & SIZE & OTHER VALUES
    private Vector2 position;
    private Vector2 positionDistChanger;
    private Vector2 initialSize;
    private float scale;

    //COLORS
    private Color color;
    private Color background;
    private BitmapFont font;
    private BitmapFontCache bitmapFontCache;
    private GlyphLayout glplayout;

    //SHAPE RANDERER
    private ShapeRenderer shapeRenderer;

    //BOOLEANS
    private boolean visible=false;
    private boolean isHUD=false;
    private boolean withShape=true;
    private boolean filledShape=false;
    private boolean hasMoveTo=false;

    public float amountMoveToX;
    public float amountMoveToY;
    public float durationMoveTo;
    public float destinyMoveToX;
    public float destinyMoveToY;

    //STRINGS
    private String text;
    private String fontNamePath;
    private String glplayoutPath;
    private String TexActorExtraInfo="";
    private Color tint=null;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public TextActor(Camera cam, com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g){
        this.g=g;
        camera=cam;

        shapeRenderer=new ShapeRenderer();
        color=new Color(Color.GOLD);
        background=new Color(Color.GRAY);

        position=new Vector2(0,0);
        positionDistChanger= new Vector2(0,0);
        initialSize=new Vector2();
    }

    //UPDATE
    public void updateActor(){

        if(!isHUD) {
            position = convertToRealCoordinates(position);
            float scalez=scale/g.m.gsys.getCamera().zoom;
            setScale(scalez);
        }
        if(hasMoveTo){
            if((Math.abs(destinyMoveToX-position.x))>0.01) position.x=position.x+amountMoveToX;
            if((Math.abs(destinyMoveToY-position.y))>0.01) position.y=position.y+amountMoveToY;
            if((Math.abs(destinyMoveToX-position.x))<=0.01 && (Math.abs(destinyMoveToY-position.y))<=0.01){

                position.x=destinyMoveToX;
                position.y=destinyMoveToY;
                destinyMoveToX=0;
                destinyMoveToY=0;
                amountMoveToX=0;
                amountMoveToY=0;
                durationMoveTo=0;
                hasMoveTo=false;

            }else {
                durationMoveTo -= (1 / 60);
            }
        }
    }

    //DRAW
    @Override
    public void draw(Batch batch, float alpha){
        updateActor();

        setPosition(position.x,position.y);
        setVisible(visible);


            matrix = new Matrix4(camera.projection);
            matrix.idt();
            matrix.translate(getX(), getY(), 0);
            matrix.rotate(0, 0, 1, getRotation());
            matrix.scale(getScaleX(), getScaleY(), 1);

            matrix.translate(getOriginX(), -getOriginY(), 0);
            batch.setTransformMatrix(matrix);

            bitmapFontCache.setColor(color.r, color.g, color.b, color.a * alpha);
            if (tint != null)
                    bitmapFontCache.tint(tint);

            bitmapFontCache.draw(batch);




        //Resets manually the TransformMatrix
        batch.setTransformMatrix(IdentityMatrix);
    }

    public void setTint(Color tint){
        this.tint=tint;

    }

    public void moveTo(float dx,float dy,float duration){

        durationMoveTo=duration;
        destinyMoveToX=dx;
        destinyMoveToY=dy;
        amountMoveToX=(dx-position.x)/(60*duration);
        amountMoveToY=(dy-position.y)/(60*duration);
        g.println(TAG + " NAME=" +getName() + " glplayout.hegiht="+ glplayout.height + " positionx=  " + position.x + " positiony=" + position.y + " dx=" + dx + " dy=" +dy + "AMOUNTX=" + amountMoveToX + "AMOUNTY="+ amountMoveToY);
        hasMoveTo=true;

    }


/*_______________________________________________________________________________________________________________*/

    public void NewTexActorFont(String fntNmPth, String pngNmPth){
        if(font!=null){
            g.println("\n"+TAG +" ************** RE-CREATE FONT ************* \n");
        }

        setTextActorGlyphLayoutPath(pngNmPth);
        setTextActorPath(fntNmPth);
        NewTextActorFont();

    }

    private void  NewTextActorFont(){

        font = new BitmapFont(Gdx.files.internal(fontNamePath));
        bitmapFontCache=new BitmapFontCache(font);

        glplayout=bitmapFontCache.setText(text, 0, 0);
        setPosition(position.x,position.y);
        float scalez=scale/g.getZoom(); // /g.m.mGS.get_camera().zoom;
        setScale(scalez);
        setVisible(visible);
        initialSize.x=glplayout.width; initialSize.y=glplayout.height;
        // setOrigin(glplayout.width / 2, -glplayout.height/2);
        // g.println("\nBITMAP FONT ACTOR CREATED****************************\n\n");
    }

    public Vector2 convertToRealCoordinates(Vector2 pos){

        float x,y,Crelx,Crely,Originx,Originy;

        float screenWidth = MainGameScreen.VIEWPORT.viewportWidth * g.m.gsys.getCamera().zoom ;
        float screenHeight = MainGameScreen.VIEWPORT.viewportHeight * g.m.gsys.getCamera().zoom;

        Originx=(g.m.gsys.getCamera().position.x- screenWidth/2);
        Originy=(g.m.gsys.getCamera().position.y- screenHeight/2);

        Crelx=pos.x-Originx;
        Crely=pos.y-Originy;

        x=Crelx*g.m.he.getStage().getWidth() / screenWidth;
        y=Crely*g.m.he.getStage().getHeight() / screenHeight;

        return new Vector2(x,y);
    }

    public static Vector2 convertToRealCoordinates(Vector2 pos, GenericMethodsInputProcessor g){


        float x,y,Crelx,Crely,Originx,Originy;

        float screenWidth = MainGameScreen.VIEWPORT.viewportWidth * g.m.gsys.getCamera().zoom ;
        float screenHeight = MainGameScreen.VIEWPORT.viewportHeight * g.m.gsys.getCamera().zoom;

        Originx=(g.m.gsys.getCamera().position.x- screenWidth/2);
        Originy=(g.m.gsys.getCamera().position.y- screenHeight/2);

        Crelx=pos.x-Originx;
        Crely=pos.y-Originy;

        x=Crelx*g.m.he.getStage().getWidth() / screenWidth;
        y=Crely*g.m.he.getStage().getHeight() / screenHeight;

        return new Vector2(x,y);
    }

    public Vector2 convertToGameSystemCoordinates(Vector2 pos){

        float x,y,Crelx,Crely,Originx,Originy;

        float screenWidth = com.mygdx.safe.screens.MainGameScreen.VIEWPORT.viewportWidth * g.m.gsys.getCamera().zoom;
        float screenHeight = com.mygdx.safe.screens.MainGameScreen.VIEWPORT.viewportHeight * g.m.gsys.getCamera().zoom;

        Crelx=(pos.x)/(g.m.he.getStage().getWidth()/ screenWidth);
        Crely=(pos.y)/(g.m.he.getStage().getHeight() / screenHeight);

        Originx=g.m.gsys.getCamera().position.x- screenWidth/2;
        Originy=g.m.gsys.getCamera().position.y- screenHeight/2;

        x=Crelx+Originx;
        y=Crely+Originy;

        return new Vector2(x,y);
    }

    public Vector2 convertStageToLocal(Vector2 posStage){ return super.stageToLocalCoordinates(posStage);  }

    public Vector2 convertLocaltoStage(Vector2 posLocal){  return super.localToStageCoordinates(posLocal);  }

    public void addTextActorToHUD(Stage s){ s.addActor(this); }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS

    @Override
    public boolean isVisible() {   return visible;   }

    public boolean isHUD() {   return isHUD; }

    public float getTextActorScale() {   return scale;  }

    public String getText() {  return text; }

    public String getTexActorExtraInfo() {   return TexActorExtraInfo;  }

    public Vector2 getPositionDistChanger() { return positionDistChanger; }

    public Vector2 getPosition() { return position; }

    public boolean isWithShape() { return withShape; }

    public Vector2 getInitialSize() {    return initialSize;  }

    public Matrix4 getMatrix() { return matrix;}

    public float getAlpha(){
        return this.getColor().a;
    }

    public GlyphLayout getGlplayout() { return glplayout; }

    @Override
    public Color getColor() { return color; }

    public Color getBackground() { return background; }

    public boolean isFilledShape() { return filledShape; }

    //SETTERS

    public void setAdjustingText(String newFontText,float width,float height){


        setTextActorText(newFontText);
        boolean adjusting=false;
        int i=0;
        while(!adjusting) {

            glplayout = bitmapFontCache.setText(text, 0, 0, 0, newFontText.length(), width, Align.center, true,"");
            if(glplayout.height*getScaleY()>height || glplayout.width>width){
                g.println(" SE HA PASAOOOOOOOOO :glplayout.height=" + glplayout.height + " heigth= "+height);
                g.println(" SE HA PASAOOOOOOOOO :glplayout.Widht=" + glplayout.width + " Width= "+width);
                g.println(TAG + text + " getScaleX()="+getScaleX()+"           ************************        getScaleY()=" +getScaleY());
                g.println(" SCALE =" + scale + "I=" + i);
                float tempScaleX=getScaleX();
                float tempScaleY=getScaleY();

                tempScaleX-=0.001;
                tempScaleY-=0.001;
                i++;

                setScale(tempScaleX,tempScaleY);

            }else adjusting=true;
            g.println(" ESTA AJUSTAITO :glplayout.height=" + glplayout.height + " heigth= "+height);
            g.println(" ESTA AJUSTAITO :glplayout.Widht=" + glplayout.width + " Width= "+width);
            g.println(TAG + text + " getScaleX()="+getScaleX()+"           ************************        getScaleY()=" +getScaleY());

            g.println(" SCALE =" + scale + "I=" + i);

        }

        setVisible(visible);

    }

    public void setText(String newFontText) {
        setTextActorText(newFontText);
        glplayout = bitmapFontCache.setText(text, 0, 0);

        setVisible(visible);
    }

    public void setAlpha(int a) {
        Color color = getColor();
        setColor(color.r, color.g, color.b, a);
    }

    public void setPositionDistChanger(float x, float y) {
        this.positionDistChanger.x = x;
        this.positionDistChanger.y = y;
    }

    public void setPositionDistChanger(Vector2 positionDistChanger) {
        this.positionDistChanger.x = 0+ positionDistChanger.x;
        this.positionDistChanger.y = 0 + positionDistChanger.y;
    }

    public void setTextActorPosition(float x, float y){
        position.x=x+positionDistChanger.x* Map.UNIT_SCALE;
        position.y=y+positionDistChanger.y* Map.UNIT_SCALE;
    }

    @Override
    public void setVisible(boolean visible) { this.visible = visible; }

    public void setHUD(boolean HUD) {   isHUD = HUD;  }

    public void setTextActorScale(float scale) {  this.scale = scale;  }

    public void setTexActorExtraInfo(String texActorExtraInfo) {    TexActorExtraInfo = texActorExtraInfo; }

    public void setTextActorText(String txt){ this.text=txt+this.TexActorExtraInfo; }

    public void setTextActorPath(String path){ this.fontNamePath=path; }

    public void setTextActorPosition(Vector2 pos){ setTextActorPosition(pos.x,pos.y);  }

    public void setWithShape(boolean withShape) { this.withShape = withShape; }

    public void setTextActorGlyphLayoutPath(String path){
        this.glplayoutPath=path;
    }

    @Override
    public void setColor(Color color) { this.color = color; }

    public void setBackground(Color background) { this.background = background; }

    public void setFilledShape(boolean filledShape) { this.filledShape = filledShape; }

    public void setAdjustTextBox(float numCharWidth,float numCharHeight) {

        // FIRST POSIBILITY (WITHOUT FORCE-ADJUSTING, WITHOUT SCALING)

        g.println(TAG + ": ADJUSTING TEXT BOX");

        String definitiveText = "";
        String lineText = "";
        int countHeight = 0;
        boolean okAdjustText = true;
        String[] texts = text.split(" ");

        for (String s : texts) {
            if (lineText.length() + s.length() <= numCharWidth) {
                lineText += s + " ";
                g.println("PARTIAL LINETEXT:" + lineText);
            } else {
                definitiveText += lineText + "\n";
                countHeight++;
                lineText = s + " ";
                g.println("PARTIAL DEFINITIVELINETEXT:\n" + definitiveText);
            }
        }

        if (lineText.length() > 0) {
            definitiveText += lineText;
            g.println("PARTIAL DEFINITIVELINETEXT:\n" + definitiveText);
            countHeight++;
            lineText = "";
        }

        if (countHeight > numCharHeight) okAdjustText = false;

        if(okAdjustText==false){
            //g.println(TAG+ ": NOT ADJUSTED");
        }else g.println(TAG+ ": ADJUSTED");

        //g.println(TAG+ " PRE TEXT:");
        //g.print( text);
        //g.print("\n");

        text=definitiveText;

        //g.println(TAG+ " POST TEXT:");
        //g.print( text);
        //g.print("\n");
        setText(text);

    }

    public void setAdjustForcingTextBox(float numCharWidth,float numCharHeight){
        // SECOND POSIBILITY (WITH FORCE-ADJUNSTING, WITHOUT SCALING)

        String definitiveText="";
        String lineText="";
        int countHeight=0;
        boolean okAdjustText=true;
        String[] texts=text.split(" ");


        if(okAdjustText==false){
            g.println("\n....TOO LARGE...\n\n"+TAG+": SECOND ADJUSTING TEXT BOX (FORCE-ADJUSTING)");

            definitiveText="";
            lineText="";
            countHeight=0;
            okAdjustText=true;
            for(String s:texts){
                if(lineText.length()+s.length()<numCharWidth){
                    lineText+=s+" ";
                    g.println("PARTIAL LINETEXT:"+lineText);
                }else{
                    int dif=Math.abs(lineText.length()+s.length()-(int) numCharWidth);
                    definitiveText+=lineText+s.substring(0,s.length()-dif)+"\n";
                    lineText="";
                    lineText+=s.substring(s.length()-dif,s.length())+" ";
                    if(lineText.equalsIgnoreCase(" ")) lineText="";
                    countHeight++;
                    g.println("PARTIAL DEFINITIVELINETEXT:\n"+definitiveText);
                }
            }

            if(lineText.length()>0) {
                definitiveText+=lineText;
                g.println("PARTIAL DEFINITIVELINETEXT:\n"+definitiveText);
                countHeight++;
                lineText="";
            }
        }

        if(okAdjustText==false){
            //g.println(TAG+ ": NOT ADJUSTED");
        }else g.println(TAG+ ": ADJUSTED");

        //g.println(TAG+ " PRE TEXT:");
        //g.print( text);
        //g.print("\n");

        text=definitiveText;

        //g.println(TAG+ " POST TEXT:");
        //g.print( text);
        //g.print("\n");
        setText(text);

    }

    public void setAdjustScalingTextBox(float numCharWidth,float numCharHeight){

        String definitiveText="";
        String lineText="";
        int countHeight=0;
        boolean okAdjustText=false;
        String[] texts=text.split(" ");

        int forceadjustingScale=0;
        while(okAdjustText==false){

            //FORCE SCALING:
            forceadjustingScale++;
            scale-=(0.046/12);
            setScale(scale);
            // FIRST POSIBILITY (WITHOUT FORCE-ADJUSTING, WITHOUT SCALING)

            //g.println(TAG+": ADJUSTING SCALING TEXT BOX (FORCE-ADJUSTING)");
            //g.println("NUM: " +forceadjustingScale);
            //g.println("SCALE: " +scale);

            definitiveText="";
            lineText="";
            countHeight=0;
            okAdjustText=true;
            numCharHeight+=(1.4/12);
            numCharWidth+=(5.6/12);

            g.println("NEW CHARWIDHT: " +numCharWidth);
            g.println("NEW CHARHEIGHT: " +numCharHeight);

            for(String s:texts){
                if(lineText.length()+s.length()<=numCharWidth){
                    lineText+=s+" ";
                    g.println("PARTIAL LINETEXT:"+lineText);
                }
                else {
                    definitiveText+=lineText+"\n";
                    countHeight++;
                    lineText=s+" ";
                    g.println("PARTIAL DEFINITIVELINETEXT:\n"+definitiveText);
                }
            }

            if(lineText.length()>0) {
                definitiveText+=lineText;
                g.println("PARTIAL DEFINITIVELINETEXT:\n"+definitiveText);
                countHeight++;
                lineText="";
            }
            if(countHeight>numCharHeight) okAdjustText=false;
        }

        if(okAdjustText==false){
            //g.println(TAG+ ": NOT ADJUSTED");
        }else g.println(TAG+ ": ADJUSTED");

        //g.println(TAG+ " PRE TEXT:");
        //g.print( text);
        //g.print("\n");

        text=definitiveText;

        //g.println(TAG+ " POST TEXT:");
        //g.print( text);
        //g.print("\n");
        setText(text);
    }

    public void setAdjustTexBoxAtSizedBox(float width,float height) {

        float tempWidth=0;
        float tempHeight=0;

        String definitiveText="";
        String lineText="";
        String[] texts=text.split(" ");
        boolean okAdjustText=false;
        int forceadjustingScale=0;

        while(!okAdjustText) {

            if(!okAdjustText){
                //g.println(TAG+ ": NOT ADJUSTED");
                //FORCE SCALING:
                forceadjustingScale++;
                scale-=0.001;
                setScale(scale);
                definitiveText="";
                lineText="";
                // FIRST POSIBILITY (WITHOUT FORCE-ADJUSTING, WITHOUT SCALING)

                //g.println(TAG+": ADJUSTING SCALING TEXT BOX (FORCE-ADJUSTING)");
                //g.println("NUM: " +forceadjustingScale);
                //g.println("SCALE: " +scale);

            }else g.println(TAG+ ": ADJUSTED");

            okAdjustText=true;

            for (String s : texts) {
                setText(definitiveText + lineText + s + " ");
                tempWidth = getGlplayout().width * scale;
                tempHeight = getGlplayout().height * scale;
                if (tempWidth < width) {
                    lineText+=s + " ";

                  /*g.println("PARTIAL LINETEXT:" + lineText);
                    g.println("ACTUAL-LAYOUT: WIDHT=" +getGlplayout().width+ "HEIGHT="+getGlplayout().height);
                    g.println("ACTUAL LIMIT SIZE: X="+ width * scale + "Y=" + height * scale);
                   */

                } else {
                    definitiveText += lineText + "\n";
                    setText(definitiveText + s + " ");
                    lineText = s + " ";
                    tempWidth = getGlplayout().width * scale;
                    tempHeight = getGlplayout().height * scale;

                    if (tempHeight > height) {
                        okAdjustText = false;

                    }
                }
            }

            if(lineText.length()>0) {
                definitiveText += lineText;
                setText(definitiveText);
                tempWidth = getGlplayout().width * scale;
                tempHeight = getGlplayout().height * scale;

                if (tempHeight > height) {
                    okAdjustText = false;
                }
            }
        }

        //if(g.m.lvlMgr.get_acton().getPhysicsComponent().get_boundingBoxes().get("TextActorTalkingEntity") != null)
        //    g.m.lvlMgr.get_acton().getPhysicsComponent().get_boundingBoxes().get("TextActorTalkingEntity").setWidth(tempWidth * (Map.UNIT_SCALE/g.m.gsys.getCamera().zoom));

        //g.println(TAG+ " PRE TEXT:");
        //g.print( text);
        //g.print("\n");

        text=definitiveText;

        //g.println(TAG+ " POST TEXT:");
        //g.print( text);
        //g.print("\n");
        setText(text);
    }

    public BitmapFontCache getBitmapFontCache() {
        return bitmapFontCache;
    }
}
