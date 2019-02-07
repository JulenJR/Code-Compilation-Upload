package com.mygdx.safe.Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

/**
 * Created by Boris.Escajadillo on 17/02/18.
 */

public class HudConfig {
    // CONTAINS NAMES OF ACTONS, and Path
    public Array<HudConfigData> imagesHudActon;
    private HashMap<String,String> imagesHudActonHash;

    public HudConfig(){
        HashMap<String,String> imagesHudActon;
    }

    public void makeHudActonHash(){
        if(imagesHudActon!=null && imagesHudActonHash==null){
            imagesHudActonHash=new HashMap<String,String>();
            for(HudConfigData h:imagesHudActon){
                imagesHudActonHash.put(h.name,h.path);
            }
        }
    }

    public  Array<com.mygdx.safe.Pair<String,Image>> generateDataImages() {
        if(imagesHudActonHash==null){
            makeHudActonHash();
        }
        if (imagesHudActonHash != null) {
            Array<com.mygdx.safe.Pair<String, Image>> imageDataArray = new Array<com.mygdx.safe.Pair<String, Image>>();
            for (String s : imagesHudActonHash.keySet()) {
                Image img = new Image(new Texture(new Pixmap(Gdx.files.internal(imagesHudActonHash.get(s)))));
                imageDataArray.add( new com.mygdx.safe.Pair<String,Image>(s,img));
            }
            return imageDataArray;
        } else return null;
    }
}
