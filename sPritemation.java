package com.mygdx.safe;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.safe.Entities.GameEntity;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;

import static com.mygdx.safe.Components.GraphicsComponent.selectFrame;

/**
 * Created by Boris.InpiratGames on 22/02/18.
 */

public class sPritemation {

    public Animation spritemation;
    public float originalFrameDuration;
    public float animationSpeed = 1;

    public String atlasName;
    public String pathAtlas;
    public String pathImage;
    public boolean loop=false;
    public float frameDuration=1;
    Vector2 position=new Vector2(0,0);
    public Rectangle boundingBox=new Rectangle();
    public int Id=0;
    public float scalex=1;
    public float scaley=1;
    public String name="";
    public String type="image";
    public float timeScale=1;
    public float velocity=1;
    public boolean flipx=false;
    private Sprite sprite;
    //private Image image;
    //private TextureRegion ti;
    private float countStateTime=0;
    static int countId =0;
    private TextureAtlas t;
    private GenericMethodsInputProcessor g;
    public float rotation=0;
    public int fakeFrame = 0;

    public int hostType = 0;

    public TextureRegion txtRegion;




    void SpriteMation(){

    }

    void config(GenericMethodsInputProcessor g){
        this.g=g;
        Id= countId;
        countId++;
        sprite=new Sprite();

        if(type.equals("sprite")) {
            t = new TextureAtlas(pathAtlas);
            Array<TextureAtlas.AtlasRegion> listFrames = new Array<TextureAtlas.AtlasRegion>();

            int index = 1;
            while (t.findRegion(name, index) != null) {
                listFrames.add(t.findRegion(name, index));
                index++;
            }
            spritemation = new Animation(1.0f / listFrames.size, listFrames);
        }
        else{
            Texture t =(new Texture(pathImage));
            TextureRegion tr=new TextureRegion(t,t.getWidth(),t.getHeight());
            spritemation=  new Animation(1.0f,tr);

        }
        spritemation.setPlayMode(Animation.PlayMode.LOOP);
        originalFrameDuration = spritemation.getFrameDuration();
        //image=new Image((TextureRegion) spritemation.getKeyFrame(0.5f));
        //ti=(TextureRegion) spritemation.getKeyFrame(0.5f);


    }

    public Sprite getSprite(float delta, SpritemationEntityConfig e){


        if(e._fakeFrame==-1) {

            // SET ANIMATION DURATION
            spritemation.setFrameDuration(e._duration/spritemation.getKeyFrames().length);
            txtRegion = (TextureRegion) spritemation.getKeyFrame(e._countstatetime);
            sprite.setRegion(txtRegion);
        }
        else
        {
            txtRegion = (TextureRegion) spritemation.getKeyFrame(e._fakeFrame/100);
            sprite.setRegion(txtRegion);
        }

        if (e._countstatetime > spritemation.getAnimationDuration()){
            if(spritemation.getPlayMode()==Animation.PlayMode.LOOP || spritemation.getPlayMode()==Animation.PlayMode.LOOP_PINGPONG ||
                    spritemation.getPlayMode()== Animation.PlayMode.LOOP_RANDOM || spritemation.getPlayMode()== Animation.PlayMode.LOOP_REVERSED)
                e._countstatetime = e._countstatetime- spritemation.getAnimationDuration();

        } else e._countstatetime += delta;

        sprite.setSize(1.0f, 1.0f);
        sprite.flip(flipx,false);
        sprite.rotate(rotation);
        return sprite;
    }

    public void UpdateSpriteBoundingBox(){
        boundingBox=sprite.getBoundingRectangle();
    }


    public void setPosition(float x,float y){
        position.x=0+x;
        position.y=0+y;

    }

    public Vector2 getPosition() {
        return position;
    }

    public void configWithEntityConfig(SpritemationEntityConfig e, GameEntity ge){
        position.x=ge.getposition().x+e._mDistance.x;
        position.y=ge.getposition().y+e._mDistance.y;
        rotation=e._rotation;
    }

    public Animation getSpritemation() {
        return spritemation;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public TextureRegion getTxtRegion() {
        return txtRegion;
    }
}
