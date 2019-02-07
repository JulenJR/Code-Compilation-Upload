package com.mygdx.safe;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

/**
 * Created by Boris.InspiratGames on 8/03/18.
 */

public class AllConfigs {


    public String actualprofile="none";
    public String PlayerType="none";
    public String Age="0";
    public int points=0;
    public int Level=0;
    public String Lvl_id="";
    public Vector2 playerPosition=new Vector2(0,0);
    public Vector2 cameraposition=new Vector2(0,0);
    public Array<Pair<String,Vector2>> lvlEntitiesPosition=new Array<Pair<String, Vector2>>();
    public Array<Pair<String,Integer>> lvl_and_points = new Array<Pair<String, Integer>>();
    public HashMap<String, String> probes = new HashMap<String, String>();

    public Profile profileboy=new Profile();
    public Profile profilegirl=new Profile();

    public AllConfigs(){

    }

}
