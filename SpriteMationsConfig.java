package com.mygdx.safe;

import com.badlogic.gdx.utils.Array;

/**
 * Created by Boris.InspiratGames on 22/02/18.
 */

public class SpriteMationsConfig {
    private Array<sPritemation> sPritemationArray;
    private static String jsonPath="scripts/spritemation.json";
    private SpriteMationsConfig(){}

    public Array<sPritemation> getsPritemationArray() {
        return sPritemationArray;
    }

    public static String getJsonPath() {
        return jsonPath;
    }
}
