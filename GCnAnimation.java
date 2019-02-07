package com.mygdx.safe;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.safe.Conversation.TextActor;

/**
 * Created by alumne_practiques on 29/09/17.
 */

public class GCnAnimation {

    private TextActor textactor;
    private Vector2 rpTextActor;
    private  Animation _currentAnimation;
    private float _frameDuration;
    private int _totalFrames;
    private Sprite _sprite;
    //private float _scale;


    public GCnAnimation(){
        this._currentAnimation =  new Animation(1.0f, 0);
        this._sprite = new Sprite();
    }
    /*public void set(Animation newAnimation, float newFrameDuration, Sprite newSprite){

        set_currentAnimation(newAnimation);
        set_frameDuration(newFrameDuration);
        set_totalFrames(newAnimation.getKeyFrames().length);
        set_sprite(newSprite);

        g.println("******************************* " + newSprite.getWidth());
    }*/

    //Getters

    public TextActor getTextactor() {     return textactor;  }

    public Vector2 getRpTextActor() {    return rpTextActor; }


    public Animation get_currentAnimation() {
        return _currentAnimation;
    }

    public Sprite get_sprite() {
        return _sprite;
    }

    public float get_frameDuration() {
        return _frameDuration;
    }

    public int get_totalFrames() {
        return _totalFrames;
    }

    //Setters

    public void setRpTextActor(Vector2 rpTextActor) {    this.rpTextActor = rpTextActor;  }

    public void setTextactor(TextActor textactor) {    this.textactor = textactor;  }

    public void set_frameDuration(float _frameDuration) {
        this._frameDuration = _frameDuration;
    }

    public void set_totalFrames(int _totalFrames) {
        this._totalFrames = _totalFrames;
    }

    public void set_currentAnimation(Animation _currentAnimation) {
        this._currentAnimation = _currentAnimation;
    }

    public void set_sprite(Sprite _sprite) {
        this._sprite = _sprite;
    }


}
