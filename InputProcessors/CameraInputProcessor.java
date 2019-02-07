package com.mygdx.safe.InputProcessors;



import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

public class CameraInputProcessor implements InputProcessor {

    //TAG
    private static final String TAG = CameraInputProcessor.class.getSimpleName();

    //ASPECTS
    private GenericMethodsInputProcessor g;

    //HUD ENTITY
    private com.mygdx.safe.Entities.HUD_Entity h;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public CameraInputProcessor( com.mygdx.safe.Entities.HUD_Entity h){
        this.g=h.getGenericMethodsInputProcessor(); this.h=h;
    }

    /*_______________________________________________________________________________________________________________*/

    //KEY DOWN
    @Override
    public boolean keyDown(int keycode) {

        if(g.m.lvlMgr.getPlayer().getPhysicsComponent().isStopPhysicsUpdate()) g.m.lvlMgr.getPlayer().getPhysicsComponent().setStopPhysicsUpdate (false);

        g.setKeyDownCode(keycode);
        if(!g.isBlockCommandInput()) {
            if (keycode == Input.Keys.A)
                g.setLeft(true);
            if (keycode == Input.Keys.D)
                g.setRight(true);
            if (keycode == Input.Keys.W)
                g.setUp(true);
            if (keycode == Input.Keys.S)
                g.setDown(true);
            if (keycode == Input.Keys.CONTROL_LEFT){
                g.setControl(true);
            }
            if (keycode == Input.Keys.SPACE){
                g.setSpace(true);
            }
            /*
            if (keycode == Input.Keys.Z && !g.m.gsys.isDialogMode()) {
                g.setZoomFactor(2);
                g.setZooming(true);
            }
            if (keycode == Input.Keys.X && !g.m.gsys.isDialogMode()) {
                g.setZoomFactor(-2);
                g.setZooming(true);
            }
            */
        }
        return true;
    }

    //KEY UP
    @Override
    public boolean keyUp(int keycode) {

        if(g.m.lvlMgr.getPlayer().getPhysicsComponent().isStopPhysicsUpdate()) g.m.lvlMgr.getPlayer().getPhysicsComponent().setStopPhysicsUpdate(false);

        g.setKeyUpCode(keycode);
        if(g.isBlockCommandInput()){
            if( g.getKeyUpCode() == Input.Keys.ENTER) {
                if(g.isReturnCommand()) {
                    System.out.println("[ NEXT COMMAND ]");
                }else{
                    g.setBlockCommandInput(false);
                }
            }
        } else if(g.getKeyUpCode()==Input.Keys.I) {
            System.out.println("[ INIT COMMAND ]");
            g.setBlockCommandInput(true);
        }

        if(keycode == Input.Keys.D) g.setRight(false);
        if(keycode == Input.Keys.W) g.setUp(false);
        if(keycode == Input.Keys.S) g.setDown(false);
        if(keycode == Input.Keys.A) g.setLeft(false);
        if(keycode == Input.Keys.CONTROL_LEFT) g.setControl(false);
        if(keycode == Input.Keys.SPACE) g.setSpace(false);

        g.setZoomFactor(1);
        g.setZooming(false);
        return true;
    }

    //KEY TYPED
    @Override
    public boolean keyTyped(char character) {
        g.setKeyTypedCode(character);
        g.getCommand().commandInput();
        return true;
    }

    //TOUCH DOWN
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {return true;}

    //TOUCH UP
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        /*if(g.m.lvlMgr.getPlayer().getPhysicsComponent().isStopPhysicsUpdate()) g.m.lvlMgr.getPlayer().getPhysicsComponent().setStopPhysicsUpdate (false);

        if(!g.isHasStageInput()  ) {

            Vector2 hit=new Vector2(h.getStage().screenToStageCoordinates(new Vector2(screenX,screenY)));
            //g.printlns("SCREEN TOUCH DOWN: X="+screenX+",Y="+screenY + "+++++" + hit);
            if (AlphaHitDetector(h.getHudActorDataComponent().PIX_HUD.getPixel((int)hit.x,(int)hit.y))) {
                g.println("   CHANNEL ALPHA   ");
                g.setRight(false);
                g.setUp(false);
                g.setDown(false);
                g.setLeft(false);
                g.setZoomFactor(1);
                g.setZooming(false);
                return true;
            }
        }*/
        return false;
    }

    //TOUCH DRAGGED
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {return true;}

    //MOUSE MOVED
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    //SCROLLED
    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    //ALPHA HIT DETECTOR
    public boolean AlphaHitDetector(int pixel){
        return (pixel & 0x000000ff) == 0;
    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS
    public com.mygdx.safe.Entities.HUD_Entity getH() {
        return h;
    }

    //SETTERS
    public void setH(com.mygdx.safe.Entities.HUD_Entity h) {
        this.h = h;
    }

}