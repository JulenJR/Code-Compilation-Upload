package com.mygdx.safe;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Array;
import com.mygdx.safe.Entities.ParticleEntity;

import java.util.HashMap;
import java.util.Random;

import static com.badlogic.gdx.Gdx.app;
import static java.lang.System.exit;

/**
 * Created by Boris.Escajadillo on 23/02/18.
 */

public class SpritemationEntityConfig {

    private final static String TAG=SpritemationEntityConfig.class.getSimpleName();
    public int _value=0;
    public String _name;
    public String _withname;
    public Vector2 _mDistance=new Vector2(0,0);
    public float _rotation=0;
    public int _id_Spritemation=0;
    public Vector2 _position=new Vector2(0,0);
    public float _fakeFrame=-1;
    public int _systemType= 1;//1=Gamesystem //0=HudSystem
    public SpriteDrawable _spriteDrawable=new SpriteDrawable();
    private Random rnd=new Random();
    public float _countstatetime=rnd.nextFloat();
    public float _duration=1;
    public Sprite _currentSprite=null;
    public boolean with_particle=false;
    public ParticleEntity pe;

    // PARABOLIC MOVEMENT PARAMETERS
    public boolean parabolicMovementActivator=false;
    public float gravity= 9.8f;
    public float initialVelocity=0;
    public float initialVelocityX=0;
    public float initialVelocityY=0;
    public float initialX=0;

    public float initialY=0;
    public float finalX=0;
    public float finalY=0;
    public float movementTime=0;
   

    //Movement
    public Vector2 destiny = new Vector2();
    public boolean hasMovemnt = false;
    public float timeForMovement = 0;
    public Vector2 distanceToDestiny = new Vector2();

    //SIZE

    public float originalWidth = -1;
    public float originalHeight = -1;

    public float originalScaleX = 1;
    public float originalScaleY = 1;
    public float currentScaleX = 1;
    public float currentScaleY = 1;

    public float currentOriginalWidhScale = -1;
    public float currentOriginalHeightScale = -1;

    //BOOLEAN
    public boolean selected = false;
    public boolean scaled = false;
    public boolean visible = true;

    private ParallelAction parallelAction= new ParallelAction();

    public SpritemationEntityConfig(){
        //System.out.println(TAG + "*********************************************************************************" + _countstatetime);
    }

    public static int getExtendedPosName(String name,Array<SpritemationEntityConfig> secArray){
        for(int i=0;i< secArray.size;i++){
            if (secArray.get(i)._name.equalsIgnoreCase(name)){
                return i+1;
            }
        }
        return 0;
    }

    public void config(HashMap<String,sPritemation> spmtions){
        sPritemation sp=spmtions.get(_name);
        if(sp!=null){
            _id_Spritemation=sp.Id;
        }else{
            System.out.println(TAG +" ERROR!. NOT FOUND SPRITEMATION NAME:" + _name);
            System.out.println(TAG + spmtions.keySet().toString());
            app.exit();
        }
    }

    public void parabolicMovement(float timeforMovement,float initialX,float initialY,float finalX,float finalY){
        parabolicMovementActivator=true;

        this.timeForMovement =timeforMovement;

        this.finalX=finalX;
        this.finalY=finalY;

        this.initialVelocityX = (finalX - initialX) / timeforMovement ;
        this.initialVelocityY= ((finalY - initialY) + ((gravity/2) * (timeforMovement * timeforMovement))) /timeforMovement;

        this.initialVelocity = (float) Math.sqrt((double) ((initialVelocityX * initialVelocityX) + (initialVelocityY * initialVelocityY)));

        this.initialX=initialX;
        this.initialY=initialY;




    }

    public Vector2 updateParabolicMovement(float delta){
        movementTime+=delta;

        float actualX=  (initialX + (initialVelocityX * movementTime));
        float actualY = (initialY + (initialVelocityY * movementTime) - ((gravity * movementTime * movementTime)/2));

        

        if (movementTime>= timeForMovement) {
            parabolicMovementActivator = false;
            movementTime = 0;
        }


        return(new Vector2(actualX,actualY));

    }

    public ParallelAction getParallelAction() {
        return parallelAction;
    }

    public void setParallelAction(ParallelAction parallelAction) {
        this.parallelAction = parallelAction;
    }

    public float getOriginalWidth() {
        return originalWidth;
    }

    public void setOriginalWidth(float originalWidth) {
        this.originalWidth = originalWidth;
    }

    public float getOriginalHeight() {
        return originalHeight;
    }

    public void setOriginalHeight(float originalHeight) {
        this.originalHeight = originalHeight;
    }

    public void setOriginalSize(Image image){
        this.originalWidth = 0 + image.getWidth();
        this.originalHeight = 0 + image.getHeight();

    }

    public void setOriginalScale(float scalex, float scaley){
        this.originalScaleX = 0 + scalex;
        this.originalScaleY = 0 + scaley;
    }

    public void setCurrentOriginalSizeScale(float x, float y){
        this.currentOriginalWidhScale = x;
        this.currentOriginalHeightScale = y;
    }
}
