package com.mygdx.safe;

public class LVL_1_Map extends Map {



    private static String _mapPath = "maps/levels/01_01/01_01.tmx";



    LVL_1_Map(com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g) {
        super(1, _mapPath, g);

    }

    public static String get_mapPath() {
        return _mapPath;
    }

    public static void set_mapPath(String _mapPath) {
        LVL_1_Map._mapPath = _mapPath;
    }


}




