package com.mygdx.safe;

import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.safe.Entities.GameEntity;
import com.mygdx.safe.IA.ExtendedPos;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;

/**
 * Created by Boris.InspiratGames on 26/02/18.
 */

public class sPritemationsClient {

    GenericMethodsInputProcessor g;
    ExtendedPos l;
    Array<SpritemationEntityConfig> secarray;
    GameEntity ge;
    sPritemationsHost sh;

    public sPritemationsClient(GenericMethodsInputProcessor g, GameEntity ge, Array<SpritemationEntityConfig> seca, sPritemationsHost shost){

        this.ge=ge;
        this.g=g;
        l=new com.mygdx.safe.IA.ExtendedPos();
        if(seca==null)
            this.secarray=new Array<SpritemationEntityConfig>();
        sh=shost;
    }

    public void setSpritemation(String name,String withname){
        setSpritemation(name,withname,0,0);
    }

    public void setSpritemation(String name,String withname,Vector2 masterdistance){
        setSpritemation(name,withname,masterdistance.x,masterdistance.y,0);
    }

    public void setSpritemation(String name,String withname,float mx,float my){
        setSpritemation(name,withname,mx,my,0);
    }

    public void setSpritemation(String name,String withname,Vector2 masterdistance,float rotation){
        setSpritemation(name,withname,masterdistance.x,masterdistance.y,rotation);
    }

    public void setSpritemation(String name,String withname,float mx,float my,float rotation){
        setSpritemation(name,withname,mx,my,rotation,-1);
    }

    public void setSpritemation(String name,String withname,float mx,float my,float rotation,float fakeFrame){
        setSpritemation(name,withname,mx,my,rotation,fakeFrame,1);
    }

    public void setSpritemation(String name,String withname,float mx,float my,float rotation,float fakeFrame,int sType){
        int i=sh.setSpritemations(name,withname,ge,secarray,l);
        if(i<secarray.size){
            secarray.get(i)._mDistance.x=mx;
            secarray.get(i)._mDistance.y=my;
            secarray.get(i)._rotation=rotation;
            if(sType==1 || sType==0) secarray.get(i)._systemType=sType;
            if(sType==0) sh.getSpritemationImages().get(name).setVisible(true);
            if(fakeFrame==-1 || (fakeFrame>=0 && fakeFrame<=100)) secarray.get(i)._fakeFrame=fakeFrame;
        }
    }

    public void setSpritemations(Array<SpritemationEntityConfig> secArray){
        sh.setSpritemations(secArray);
    }
    public void unsetSpritemation(String withname){
        sh.unsetSpritemation(withname,secarray,l);
    }

    public void updateClientSpritemations(PolygonSpriteBatch p,float delta){
        sh.update(p,delta,l,secarray,ge);
    }
}
