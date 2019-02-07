package com.mygdx.safe.Conversation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

/**
 * Created by Boris.InspiratGames on 2/10/17.
 */

public class DialogsGraphCharger {

    //TAG
    private static final String TAG = DialogsGraphCharger.class.getSimpleName();

    //PATH
    public static String PATH="scripts/dialogGraphs.json";

    /*_______________________________________________________________________________________________________________*/

    //STATIC METHODS
    static public DialogsGraphs GetDialogsGraphs(String path){
        Json json = new Json();
        return json.fromJson(DialogsGraphs.class, Gdx.files.internal(path));
    }

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public DialogsGraphCharger(){}

}
