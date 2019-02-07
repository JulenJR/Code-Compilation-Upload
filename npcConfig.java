package com.mygdx.safe;

import com.badlogic.gdx.utils.Array;

/**
 * Created by sensenom on 27/06/17.
 */

public class npcConfig {
    Array<EntityConfig> NPCS;
    public npcConfig(){

    }
    Array<EntityConfig> getNpcArray(){
        return NPCS;
    }

    public void setNpcArray(Array<EntityConfig> npcArray) {
        this.NPCS = npcArray;
    }
}
