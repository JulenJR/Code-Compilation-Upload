package com.mygdx.safe;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Boris.InspiratGames on 11/07/17.
 */

public class EmoConfig {


    Array<EmoSaveValues> evalues;
    Array<EntityConfig> emos;
    int _floor;
    int _top;
    int _TBD;


    public EmoConfig(){

    }

    public void setEmoArray(Array<EntityConfig> emos) {
        this.emos = emos;
    }

    public Array<EntityConfig> getEmoArray(){   return emos;   }

    public Array<EmoSaveValues> getEmovalues() {    return evalues;  }


}
