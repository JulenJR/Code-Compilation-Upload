package com.mygdx.safe.EntitySystems;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.mygdx.safe.Components.HudDataComponent;
import com.mygdx.safe.Entities.HUD_Entity;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;
import com.mygdx.safe.Safe;
import com.mygdx.safe.SoundMusicMation;
import com.mygdx.safe.screens.MainGameScreen;

import static com.badlogic.gdx.Gdx.app;
import static com.mygdx.safe.screens.ProfileScren.ac;
import static java.lang.System.exit;

/**
 * Created by Boris.InspiratGames on 30/05/17.
 */

public class HUD_System extends EntitySystem {

    //TAG
    private static final String TAG = HUD_System.class.getSimpleName();

    //ASPECTS
    private GenericMethodsInputProcessor g;

    //CAMERA
    private Camera camera;

    //HUD ENTITY
    private HUD_Entity he;

    //RENDERERS
    private ShapeRenderer shapeRenderer;

    //COMPONENTS
    private HudDataComponent h;

    //DELTA & FRAMES VALUES
    private int frames=0;
    private float controldelta=0;
    private float controlframes=0;
    private double seconds=0;

    //CONTROL VALUES
    private float controlTriceptionMove=0;
    private float controlItonAction = 0;
    private float controlItemAction = 0;


    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public HUD_System(final GenericMethodsInputProcessor g,Camera camera,HUD_Entity hude) {

        this.g=g;
        he=hude;
        h = he.getHudActorDataComponent();
        this.camera=camera;
        shapeRenderer=g.getShapeRenderer();

     }


    public void update(float delta){

        g.m.he.sclientHud.updateClientSpritemations(delta);
        if(he.controlButton){
            he.execUpdateControlButtonTurtle(delta);
        }


            he.getStage().act(delta);
            he.getStageforHud().act(delta);
            he.getStage().draw();
            he.getStageforHud().draw();


    }


    /*_______________________________________________________________________________________________________________*/

    //GETTERS

    public Camera getCamera() {
        return camera;
    }

    public HUD_Entity getHe() {
        return he;
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    public HudDataComponent getH() {
        return h;
    }

    public int getFrames() {
        return frames;
    }

    public float getControldelta() {
        return controldelta;
    }

    public float getControlframes() {
        return controlframes;
    }

    public double getSeconds() {
        return seconds;
    }

    public float getControlTriceptionMove() {
        return controlTriceptionMove;
    }

    public float getControlItonAction() {
        return controlItonAction;
    }

    public float getControlItemAction() {
        return controlItemAction;
    }

    //SETTERS

    public void setFrames(int frames) {
        this.frames = frames;
    }

    public void setControldelta(float controldelta) {
        this.controldelta = controldelta;
    }

    public void setControlframes(float controlframes) {
        this.controlframes = controlframes;
    }

    public void setSeconds(double seconds) {
        this.seconds = seconds;
    }

    public void setControlTriceptionMove(float controlTriceptionMove) {
        this.controlTriceptionMove = controlTriceptionMove;
    }

    public void setControlItonAction(float controlItonAction) {
        this.controlItonAction = controlItonAction;
    }

    public void setControlItemAction(float controlItemAction) {
        this.controlItemAction = controlItemAction;
    }
}

