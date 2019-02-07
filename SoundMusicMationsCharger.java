package com.mygdx.safe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.io.File;

/**
 * Created by sensenom on 24/04/18.
 */

public class SoundMusicMationsCharger {
    SoundMusicMation m;
    int countm=0;

    public SoundMusicMationsCharger(){

    }
    public SoundMusicMation getSoundMusicMations(){

        if(countm==0)
        {
            //FileHandle file=new FileHandle("scripts/soundMusicMations.json");
            Json json=new Json();
            m = json.fromJson(SoundMusicMation.class, Gdx.files.internal("scripts/soundMusicMations.json"));
            countm++;
        }

        return m;
    }

}
