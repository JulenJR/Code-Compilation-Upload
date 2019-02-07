package com.mygdx.safe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.mygdx.safe.Entities.GameEntity;
import com.mygdx.safe.IA.ExtendedPos;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;

import java.util.HashMap;


/**
 * Created by Boris.InspiratGames on 22/02/18.
 */

public class sPritemationsHost {
    private static final String TAG=sPritemationsHost.class.getSimpleName();
    private GenericMethodsInputProcessor g;

    public HashMap<String, sPritemation> spritemations;
    private Array<sPritemation> spritemationsId;

    private HashMap<String,Image> spritemationImages;
    public Array<Image> arrayImages;

    private SpriteMationsConfig sc;
    private ExtendedPos hostClientExtendedPos =new ExtendedPos();

    private Array<SpritemationEntityConfig> selectedSpritemations = new Array<SpritemationEntityConfig>();

    private HashMap<String, SpritemationEntityConfig> hostClientSpriteMationEntityConfigHash = new HashMap<String, SpritemationEntityConfig>();

    //SPRITEMATIONCONFIG ARRAY FOR AUTO SPAWN
    private Array<SpritemationEntityConfig> hostClientEntitySpritAnim = new Array<SpritemationEntityConfig>();

    public sPritemationsHost(GenericMethodsInputProcessor g){
        this.g=g;
        //ALL SPRITEMATION CONTAINERS
        spritemations=new HashMap<String, sPritemation>();
        spritemationsId=new Array<sPritemation>();
        arrayImages=new Array<Image>();
        spritemationImages=new HashMap<String, Image>();
    }

    public Array<sPritemation> getSpritemationsId() {   return spritemationsId;  }

    public HashMap<String, Image> getSpritemationImages() { return spritemationImages;  }

    public void config(){
        Json json=new Json();
        sc=json.fromJson(SpriteMationsConfig.class, Gdx.files.internal(SpriteMationsConfig.getJsonPath()));
        spritemationsId=sc.getsPritemationArray(); //ARRAY SPRITEMATION CONTAINER
        for(sPritemation s:sc.getsPritemationArray()){ //HASHMAP NAME,SPRITEMATION CONTAINER
            s.config(g);
            spritemations.put(s.name,s);
            g.println(TAG +"   ID=" +s.Id + " \t   TYPE="+s.type +"\t   SPRITE NAME="+ s.name  );
        }

        // PROBES



        //setHostClientSpritemation("stone","stone",45,25,0);
        //setHostClientSpritemation("tape","tape",46,25,0);
        //setHostClientSpritemation("kok","kok",1,2,0);

        // unsetNotDependingEntitySpritemations("kok");
        // unsetNotDependingEntitySpritemations("tape");
        // setHostClientSpritemation("stone","stone2",45,26,0);
        // setHostClientSpritemation("tape","tape2",42,26,0);
        // setHostClientSpritemation("tape","tape",45,24,45);
        //setHostClientSpritemation("tape","tape16",800,200,0,-1,0);
        //setHostClientSpritemation("stars","stars3",200,200,0,-1,0);
        //setHostClientSpritemation("stars","stars4",43,22,0);





    }

    public void reset(){

        arrayImages=new Array<Image>();
        spritemationImages=new HashMap<String, Image>();
        selectedSpritemations = new Array<SpritemationEntityConfig>();
        hostClientSpriteMationEntityConfigHash = new HashMap<String, SpritemationEntityConfig>();

        hostClientExtendedPos = new ExtendedPos();

    }

    public void update(PolygonSpriteBatch p, float delta){

        for(int i = 0; i< hostClientExtendedPos.getSize(); i++){
            // SELECT CONFIG OF ARRAY OF CONFIGS WITH THE LONG
            SpritemationEntityConfig e= hostClientEntitySpritAnim.get(
                    hostClientExtendedPos.getPositionArray().get(i)-1); // SELECT CONFIG OF SP_ARRAY WITH EXTENDEDPOS VALUES

            sPritemation s=spritemationsId.get(e._id_Spritemation); //SELECT SPRITEMATION USING ID FROM CONFIG
            Sprite sprite=s.getSprite(delta,e); // GET_SPRITE

            if (e._systemType==1) {

                Vector2 scale;
                if(e.scaled) scale = new Vector2(e.currentScaleX, e.currentScaleY);
                else scale = new Vector2(e.originalScaleX, e.originalScaleY);

                //UPDATE THE SPRITEMATION HOST CLIENT POSITION
                if (e.parabolicMovementActivator) {
                    e._position = e.updateParabolicMovement(delta);
                }
                else if(e.hasMovemnt) updateHostClientPosition(e,delta);

                float scx;
                float scy;

                // CASE HUD DIMENSION
                scx= s.getTxtRegion().getRegionWidth()/45.4f;
                scy= s.getTxtRegion().getRegionHeight()/46.47f;
                sprite.setSize(sprite.getWidth() + scx,sprite.getHeight() + scy);

                if(e.visible){

                    p.draw(sprite, e._position.x , e._position.y ,
                            sprite.getOriginX(), sprite.getOriginY(), sprite.getWidth(), sprite.getHeight(),
                            scale.x, scale.y, e._rotation);
                }
                /*else {
                    e._spriteDrawable.setSprite(sprite); // FOR HUD CASES
                    getSpritemationImages().get(e._withname).setDrawable(e._spriteDrawable);

                    getSpritemationImages().get(e._withname).setPosition(e._position.x + e._mDistance.x, e._position.y + e._mDistance.y);
                }
                */
            }

        }
    }

    public void updateHostClientPosition (SpritemationEntityConfig e, float delta){

        //g.println("*******************************" + e.destiny);

        float xDistance = e.destiny.x - e._position.x;
        float yDistance = e.destiny.y - e._position.y;

        if(xDistance !=0 || yDistance!=0){
            if(delta>e.timeForMovement){
                e._position.x = 0 + e.destiny.x;
                e._position.y = 0 + e.destiny.y;
                e.timeForMovement = 0;
                e.hasMovemnt = false;
            }
            else{
                e._position.x += ((xDistance * delta)/e.timeForMovement);
                e._position.y += ((yDistance * delta)/e.timeForMovement);

                e.timeForMovement -= delta;
            }
        }
    }

    public void update(PolygonSpriteBatch p, float delta, ExtendedPos ep, Array<com.mygdx.safe.SpritemationEntityConfig> spArray, GameEntity ge){

        for(int i=0;i<ep.getSize();i++){

            SpritemationEntityConfig e=spArray.get(ep.getPositionArray().get(i)-1); // SELECT CONFIG OF SP_ARRAY WITH EXTENDEDPOS VALUES

            sPritemation s=spritemationsId.get(e._id_Spritemation); //SELECT SPRITEMATION USING ID FROM CONFIG

            s.configWithEntityConfig(e,ge); // SETS SPRITEMATION VALUES WITH GE POSITION FOR RENDER

            Sprite sprite=s.getSprite(delta,e); // GET_SPRITE

            if (e._systemType==1){
                Image image = getSpritemationImages().get(e._withname);

                p.draw(sprite, image.getX(), image.getY(), sprite.getOriginX(), sprite.getOriginY(),
                        image.getWidth(), image.getHeight(), 1, 1, 0);
                /*p.draw(sprite, s.position.x,s.position.y, sprite.getOriginX(), sprite.getOriginY(),
                        sprite.getWidth(), sprite.getHeight(), s.scalex,s.scaley,s.rotation);*/
            }
            /*else
                e._spriteDrawable.setSprite(sprite); // FOR HUD CASES
                getSpritemationImages().get(e._withname).setDrawable(e._spriteDrawable); // SELECT IMAGES OF IMAGES ARRAY  WITH LONGPOS VALUES
                if(e.parabolicMovementActivator){ e._mDistance=e.updateParabolicMovement(delta);}
                getSpritemationImages().get(e._withname).setPosition(e._position.x+e._mDistance.x,e._position.y+e._mDistance.y);
            */
        }
    }

    public void setSpritemationAnimationDuration(String withname, float duration){
        for (int i = 0; i < hostClientExtendedPos.getSize(); i++) {
            SpritemationEntityConfig e = hostClientEntitySpritAnim.get(i);
            if (e._withname.equalsIgnoreCase(withname)) {

                e._duration=duration;
                g.println(TAG + " SET DURATION name:" + e._name + " for " + duration );

            }
        }
    }

    public void setHostClientSpritemationPosition(String withname,float x,float y, boolean tranform){

        Vector2 pos;

        if(tranform) pos = new Vector2(hud_to_gsys_coordinateTransformation(x, y));
        else pos = new Vector2(x, y);

        boolean found=false;
        for (int i = 0; i < hostClientExtendedPos.getSize(); i++) {
            SpritemationEntityConfig e = hostClientEntitySpritAnim.get(i);
            if (e._withname.equalsIgnoreCase(withname)) {
                found=true;
                e._position.x = 0 + pos.x;
                e._position.y = 0 + pos.y;

                g.println(TAG + " SET POSITION name:" + e._name + " AS " + e._withname + " ARRAY ID:" + i + " Spritemation Id: " + e._id_Spritemation );

            }
        }
        if(!found) System.out.println(TAG + " SET POSITION ERROR!! " + withname + " NOT EXIST");

    }


    public void setHostClientSpritemation(String name, String withName){
        setHostClientSpritemation(name,withName,0,0);
    }

    public void setHostClientSpritemation(String name, String withName, float mdx, float mdy){
        setHostClientSpritemation(name,withName,mdx,mdy,0);
    }

    public void setHostClientSpritemation(String name, String withName, float mdx, float mdy, float rot){
        setHostClientSpritemation(name,withName,mdx,mdy,rot,-1);
    }

    public void setHostClientSpritemation(String name, String withName, float mdx, float mdy, float rot, float fakeframe){
        setHostClientSpritemation(name,withName,mdx,mdy,rot,fakeframe,1, false);
    }

    public void setHostClientSpritemation(String name, String withName, float x, float y, float rot, float fakeframe, int sType, boolean transform){
        boolean reset=false;
        Vector2 md;

        if(transform) md= new Vector2(hud_to_gsys_coordinateTransformation(x, y));
        else md = new Vector2(x, y);

            for(int i = 0; i< hostClientEntitySpritAnim.size; i++){
                SpritemationEntityConfig e= hostClientEntitySpritAnim.get(i);
                if(e._name.equalsIgnoreCase(name) && e._withname.equalsIgnoreCase(withName)){
                    hostClientExtendedPos.setPos(i + 1);
                    e._position.x = x;
                    e._position.y = y;
                    e._mDistance.x=0;
                    e._mDistance.y=0;
                    e.originalScaleX = 1;
                    e.originalScaleY = 1;
                    e._rotation=rot;
                    if(sType==1 || sType==0) e._systemType=sType;
                    if(sType==0) spritemationImages.get(name).setVisible(true);
                    if(fakeframe==-1 || (fakeframe>=0 && fakeframe<=100)) e._fakeFrame=fakeframe;
                    reset=true;
                    g.println(TAG + " RESET name:" + name +" AS " + withName + " ARRAY ID: "+ i +" Spritemation Id: " + e._id_Spritemation + " " + hostClientExtendedPos.toString());
                }
            }
            if(!reset) {
                if (spritemations.get(name) != null) {
                    int id = spritemations.get(name).Id;
                    SpritemationEntityConfig e = new SpritemationEntityConfig();
                    //SETTING CONFIG
                    e._id_Spritemation = id;
                    e._name = name;
                    e._withname = withName;
                    e._position.x = x;
                    e._position.y = y;
                    e._mDistance.x=0;
                    e._mDistance.y=0;
                    e.originalScaleX = 1;
                    e.originalScaleY = 1;
                    e._rotation=rot;

                    //ADD IMAGE
                    Image i=new Image((TextureRegion) spritemations.get(e._name).getSpritemation().getKeyFrame(0.00f));
                    i.setVisible(false);
                    i.setPosition(0,0);
                    i.setName(e._withname);
                    arrayImages.add(i); //IMAGE CONTAINER
                    g.m.he.getS().addActor(i); //HUD ADDING
                    spritemationImages.put(e._withname,i); //IMAGE CONTAINER2

                    if(sType==1 || sType==0) e._systemType=sType;
                    if(sType==0) spritemationImages.get(name).setVisible(true);
                    if(fakeframe==-1 || (fakeframe>=0 && fakeframe<=100)) e._fakeFrame=fakeframe;
                    //ADDING TO ARRAY && HASMAP
                    hostClientEntitySpritAnim.add(e);
                    hostClientSpriteMationEntityConfigHash.put(e._withname, e);

                    //SETING POS AT LONGPOS hostClientExtendedPos (POS FROM ARRAY NOTDEPENDING)...
                    hostClientExtendedPos.setPos(hostClientEntitySpritAnim.size);
                    g.println(TAG + " SET name:" + name + " AS " + withName + " ARRAY ID:" + (hostClientEntitySpritAnim.size-1)
                                           + " Spritemation Id: " + id + " " + hostClientExtendedPos.toString());

                } else {
                    System.out.println(TAG + " SET ERROR!! " + name + " NOT EXIST");
                }
            }


    }

    public void setHostClientEntitySpritAnim(Array<SpritemationEntityConfig> hostClientEntitySpritAnim) {
        this.hostClientEntitySpritAnim = hostClientEntitySpritAnim;
    }

    public void setHostClientExtendedPos(ExtendedPos hostClientExtendedPos) {
        this.hostClientExtendedPos = hostClientExtendedPos;
    }

    public void setSelectedSpritemations(Array<SpritemationEntityConfig> selectedSpritemations) {
        this.selectedSpritemations = selectedSpritemations;
    }

    public void moveClientHostSpritMationToPosition(String withname, float x, float y, float time, boolean transform){

        Vector2 d;

        if(transform) d = new Vector2(hud_to_gsys_coordinateTransformation(x, y));
        else d = new Vector2(x, y);

        boolean found=false;
        for (int i = 0; i < hostClientExtendedPos.getSize() && !found; i++) {
            SpritemationEntityConfig e = hostClientEntitySpritAnim.get(i);
            if (e._withname.equalsIgnoreCase(withname)) {
                found=true;

                e.destiny.x = d.x;
                e.destiny.y = d.y;
                e.timeForMovement = time;
                e.hasMovemnt = true;

                g.println(TAG + " SET VISIBILITY name:" + e._name + " AS " + e._withname + " ARRAY ID:" + i + " Spritemation Id: "
                        + e._id_Spritemation + " " + hostClientEntitySpritAnim.toString());

            }
        }
    }

    public void scaleSpritemation(String withname,float scx,float scy){

        SpritemationEntityConfig e = null;

        boolean found=false;
        for(int i = 0; i< hostClientExtendedPos.getSize() && !found; i++){
            e = hostClientEntitySpritAnim.get(i);
            if(e._withname.equalsIgnoreCase(withname)) {
                found = true;
            }
        }

        if(e != null) {

            e.currentScaleX = scx;
            e.currentScaleY = scy;

            //At the first scale configuration the values of original sizes are allways -1
            e.scaled = !(e.originalScaleX == scx && e.originalScaleY == scy);

            Image image = getSpritemationImages().get(withname);

            image.setSize(image.getWidth()*scx, image.getHeight()*scy);
        }
        else {
            g.println(TAG + " SCALE SPRITEMATION ERROR: SPRITEMATION " + withname + " DOESN'T EXIST.." );
        }
    }

    public void setSpritemationVisibility(String withname, boolean isVisible){

        boolean found=false;
        for (int i = 0; i < hostClientExtendedPos.getSize() && !found; i++) {
            SpritemationEntityConfig e = hostClientEntitySpritAnim.get(i);

            g.println(withname + "------------------------------------" + e._withname);

            if (e._withname.equalsIgnoreCase(withname)) {
                found=true;
                e.visible = isVisible;

                g.println(TAG + " SET VISIBILITY name:" + e._name + " AS " + e._withname + " ARRAY ID:" + i + " Spritemation Id: "
                        + e._id_Spritemation + " " + hostClientEntitySpritAnim.toString());

            }
        }
        if(!found) System.out.println(TAG + " SET VISIBILITY ERROR!! " + withname + " NOT EXIST");

    }

    public void setSpritemationFakeFrame(String withname, float fakeframe){
        boolean found=false;
        for (int i = 0; i < hostClientExtendedPos.getSize() && !found; i++) {
            SpritemationEntityConfig e = hostClientEntitySpritAnim.get(i);
            if (e._withname.equalsIgnoreCase(withname)) {
                found=true;
               e._fakeFrame=fakeframe;

                g.println(TAG + " SET FAKEFRAME name:" + e._name + " AS " + e._withname + " ARRAY ID:" + i + " Spritemation Id: "
                        + e._id_Spritemation + " " + hostClientEntitySpritAnim.toString());

            }
        }
        if(!found) System.out.println(TAG + " SET FAKEFRAME ERROR!! " + withname + " NOT EXIST");
    }

    public void unsetNotDependingEntitySpritemations(String withname) {
        boolean found=false;
        for (int i = 0; i < hostClientEntitySpritAnim.size; i++) {
            SpritemationEntityConfig e = hostClientEntitySpritAnim.get(i);
            if (e._withname.equalsIgnoreCase(withname)) {
                found=true;
                hostClientExtendedPos.unsetPos(i + 1);
                g.println(TAG + " UNSET name:" + e._name + " AS " + e._withname + " ARRAY ID:" + i + " Spritemation Id: "
                                   + e._id_Spritemation + " " + hostClientExtendedPos.toString());

            }
        }
        if(!found) System.out.println(TAG + " UNSET ERROR!! " + withname + " NOT EXIST");

    }

    public ExtendedPos setSpritemations(Array<SpritemationEntityConfig> secArray) {
        ExtendedPos l = new ExtendedPos();

            for (int i = 0; i < secArray.size; i++) {
                SpritemationEntityConfig e = secArray.get(i);
                if (spritemations.get(e._name) != null) {
                    int id = spritemations.get(e._name).Id;
                    l.setPos(i + 1);
                    g.println(TAG + " SET name: " + e._name + " ARRAY ID: " + i +" Spritemation Id: " + id + " " + l.toString());

                } else {
                    System.out.println(TAG + " SET ERROR!! " + e._name + " NOT EXIST");
                }
            }

        return l;
    }

    public int basicSetSpritemations(String name, String withName, Array<SpritemationEntityConfig> secarray, HashMap<String, SpritemationEntityConfig> secarrayHash, ExtendedPos lp){
        boolean reset=false;
        int i=0;

        for(i=0;i<secarray.size;i++){
            SpritemationEntityConfig s=secarray.get(i);
            if(s._name.equalsIgnoreCase(name) && s._withname.equalsIgnoreCase(withName)){
                lp.setPos(i + 1);
                reset=true;
                g.println(TAG + "RESET name:" + name +" AS " + withName + " ARRAY ID: "+ i +" Spritemation Id: " + s._id_Spritemation + " " + lp.toString());
            }
        }
        if(!reset) {
            if (spritemations.get(name) != null) {
                int id = spritemations.get(name).Id;
                SpritemationEntityConfig e = new SpritemationEntityConfig();

                // BASIC SETTING CONFIG
                e._id_Spritemation = id;
                e._name = name;
                e._withname = withName;

                //ADDING TO ARRAY
                secarray.add(e);
                secarrayHash.put(e._withname, e);
                //SETING POS AT LONGPOS lp (POS FROM ARRAY SECARRAY)...
                lp.setPos(secarray.size);
                g.println(TAG + " SET name:" + name + " AS " + withName + " ARRAY ID:" + (secarray.size-1)
                        + " Spritemation Id: " + id + " " + lp.toString());

            } else {
                System.out.println(TAG + " SET ERROR!! " + name + " NOT EXIST");
            }
        }

        return i;
    }

    public int setSpritemations(String name, String withName, GameEntity ge, Array<SpritemationEntityConfig> secarray, ExtendedPos lp){
        boolean reset=false;
        int i=0;

            for(i=0;i<secarray.size;i++){
                SpritemationEntityConfig s=secarray.get(i);
                if(s._name.equalsIgnoreCase(name) && s._withname.equalsIgnoreCase(withName)){
                    lp.setPos(i + 1);
                    reset=true;
                    g.println(TAG + "RESET name:" + name +" AS " + withName + " ARRAY ID: "+ i +" Spritemation Id: " + s._id_Spritemation + " " + lp.toString());
                }
            }
            if(!reset) {
                if (spritemations.get(name) != null) {
                    int id = spritemations.get(name).Id;
                    SpritemationEntityConfig e = new SpritemationEntityConfig();
                    //SETTING CONFIG
                    e._id_Spritemation = id;
                    e._name = name;
                    e._withname = withName;
                    e._position.x = 0 + ge.getposition().x;
                    e._position.y = 0 + ge.getposition().y;

                    //ADDING TO ARRAY
                    secarray.add(e);
                    //SETING POS AT LONGPOS lp (POS FROM ARRAY SECARRAY)...
                    lp.setPos(secarray.size);
                    g.println(TAG + " SET name:" + name + " AS " + withName + " ARRAY ID:" + (secarray.size-1)
                            + " Spritemation Id: " + id + " " + lp.toString());

                } else {
                    System.out.println(TAG + " SET ERROR!! " + name + " NOT EXIST");
                }
            }

        return i;
    }

    public int unsetSpritemation(String withname, Array<SpritemationEntityConfig> secArray, ExtendedPos l){
        boolean found=false;
        int i=0;
        for (i = 0; i < secArray.size; i++) {
            SpritemationEntityConfig e = secArray.get(i);
            if (e._withname.equalsIgnoreCase(withname)) {
                found=true;
                l.unsetPos(i + 1);
                g.println(TAG + " UNSET name:" + e._name + " AS " + e._withname + " ARRAY ID:" + i + " Spritemation Id: "
                        + e._id_Spritemation + " " + l.toString());

            }
        }
        if(!found) System.out.println(TAG + " UNSET ERROR!! " + withname + " NOT EXIST");

        return i;

    }

    public int resetSpritemation(String name, String withname, Array<SpritemationEntityConfig> secArray, ExtendedPos l){
        boolean found=false;
        int i=0;
        for (i = 0; i < secArray.size; i++) {
            SpritemationEntityConfig e = secArray.get(i);
            if (e._name.equalsIgnoreCase(name) && e._withname.equalsIgnoreCase(withname)) {
                found=true;
                l.setPos(i + 1);
                g.println(TAG + " RESET name:" + e._name + " AS " + e._withname + " ARRAY ID:" + i + " Spritemation Id: "
                        + e._id_Spritemation + " " + l.toString());

            }
        }
        if(!found) System.out.println(TAG + " RESET ERROR!! NAME: " + name+ " AS " + withname + " NOT EXIST");
        return i;
    }

    public HashMap<String, sPritemation> getSpritemations() {
        return spritemations;
    }


    public Vector2 hud_to_gsys_coordinateTransformation(float x, float y){

        Vector2 mapPos=new Vector2(0,0);

        float camViewportHeight=g.getCamera().viewportHeight*g.getCamera().zoom;
        float camViewportWidth=g.getCamera().viewportWidth * g.getCamera().zoom;

        float HudWidth=1362;
        float HudHeight=790;

        g.println(TAG + " HUD_TO_GSYS: HUD_WIDTH="+ HudWidth + " HUD_HEIGHT="+ HudHeight);



        float auxX= x*camViewportWidth/Map.UNIT_SCALE/HudWidth;
        float auxY= y*camViewportHeight/Map.UNIT_SCALE/HudHeight;

        g.println(TAG + " HUD_TO_GSYS: auxX="+auxX+" AuxY="+auxY + " CamViewportWidth="+camViewportWidth + " CamViewportHeight="+camViewportHeight);

        auxX=auxX*Map.UNIT_SCALE;
        auxY=auxY*Map.UNIT_SCALE;

        g.println(TAG + " HUD_TO_GSYS: MAP UNIT SCALED:::auxX="+auxX+" MAP UNIT SCALED:::AuxY="+auxY);

        float posCamX=g.getCamera().position.x;
        float posCamY=g.getCamera().position.y;

        g.println(TAG + " HUD_TO_GSYS: POSCAMX="+posCamX+ " POSCAMY="+posCamY);

        float auxCamX=posCamX-camViewportWidth/2;
        float auxCamY=posCamY+camViewportHeight/2;

        g.println(TAG + " HUD_TO_GSYS: CamAuxX="+auxCamX+ " CamAuxY="+auxCamY);

        float mapx=auxCamX+auxX;
        float mapy=auxCamY-auxY;

        g.println(TAG + " HUD_TO_GSYS: MAPX="+mapx+ " MAPY="+mapy);

        mapPos.x=mapx;
        mapPos.y=mapy;

        return mapPos;

    }

    public ExtendedPos getHostClientExtendedPos() {
        return hostClientExtendedPos;
    }

    public Array<SpritemationEntityConfig> getHostClientEntitySpritAnim() {
        return hostClientEntitySpritAnim;
    }

    public Array<SpritemationEntityConfig> getSelectedSpritemations() {
        return selectedSpritemations;
    }

    public HashMap<String, SpritemationEntityConfig> getHostClientSpriteMationEntityConfigHash() {
        return hostClientSpriteMationEntityConfigHash;
    }

    public void setHostClientSpriteMationEntityConfigHash(HashMap<String, SpritemationEntityConfig> hostClientSpriteMationEntityConfigHash) {
        this.hostClientSpriteMationEntityConfigHash = hostClientSpriteMationEntityConfigHash;
    }
}
