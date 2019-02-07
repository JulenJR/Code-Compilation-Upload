package com.mygdx.safe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.mygdx.safe.AllConfigs;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;
import com.mygdx.safe.jsonexport;

import java.util.HashMap;

/**
 * Created by Boris.InspiratGames on 8/03/18.
 */

public class AllConfigsCharger {
    private static final String TAG=AllConfigsCharger.class.getSimpleName();
    private AllConfigs ac=null;
    private String fileExt=".json";
    private String file="AllConfigs";
    public static GenericMethodsInputProcessor g;
    private jsonexport<AllConfigs> j;


    public AllConfigsCharger(){

        j=new jsonexport<AllConfigs>();

    }

    public AllConfigs getAllConfigs(){
        if(ac==null){
            Json json=new Json();
            if(!Gdx.files.local((file+fileExt)).exists()){
                ac=new AllConfigs();
                j.set(ac);
                j.export(file);
            }else {
                String s=file + fileExt;
                ac = json.fromJson(AllConfigs.class, Gdx.files.local(s));
            }
        }
        System.out.println(TAG + " ACTUALPROFILE="+ac.actualprofile);
        if (ac.actualprofile.equalsIgnoreCase("girl")){
            System.out.println(TAG + " GIRL");

            ac.profilegirl.PlayerType=ac.PlayerType;
            ac.profilegirl.Age=ac.Age;
            ac.profilegirl.points=ac.points;
            ac.profilegirl.Level=ac.Level;
            ac.profilegirl.Lvl_id=ac.Lvl_id;
            ac.profilegirl.playerPosition=ac.playerPosition;
            ac.profilegirl.cameraposition=ac.cameraposition;
            ac.profilegirl.lvlEntitiesPosition=ac.lvlEntitiesPosition;
            ac.profilegirl.probes=ac.probes;
            ac.profilegirl.lvl_and_points=ac.lvl_and_points;
            ac.profilegirl.setprofile=true;

        }else if (ac.actualprofile.equalsIgnoreCase("boy")){

            System.out.println(TAG + " BOY");
            ac.profileboy.PlayerType=ac.PlayerType;
            ac.profileboy.Age=ac.Age;
            ac.profileboy.points=ac.points;
            ac.profileboy.Level=ac.Level;
            ac.profileboy.Lvl_id=ac.Lvl_id;
            ac.profileboy.playerPosition=ac.playerPosition;
            ac.profileboy.cameraposition=ac.cameraposition;
            ac.profileboy.lvlEntitiesPosition=ac.lvlEntitiesPosition;
            ac.profileboy.probes=ac.probes;
            ac.profileboy.lvl_and_points=ac.lvl_and_points;
            ac.profileboy.setprofile=true;
        }

        return ac;
    }

    public void saveAllConfigs(){


        if(Gdx.files.local((file+fileExt)).exists()){
            Gdx.files.local((file+fileExt)).delete();
            System.out.println( "Deleting file:" + file+fileExt);        }
        System.out.println("Saving file:"+file+fileExt);
        j.set(ac);
        System.out.println(TAG +" AC is: ac.age=" + ac.Age + "  ac.PlayerType=" + ac.PlayerType + "  ac.Level=" + ac.Level );
        j.export(file);
    }

    public void saveAllConfigs(AllConfigs ac){
        this.ac=ac;
        saveAllConfigs();
    }

}
