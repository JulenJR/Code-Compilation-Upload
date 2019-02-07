package com.mygdx.safe;

import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;

import java.util.HashMap;

public class MapFactory {
    GenericMethodsInputProcessor g;

    //All maps for the game
    private static HashMap<String,Map> _mapTable = new HashMap<String, Map>(); //was a hashtable

    static public Map getMap(String mapNum, GenericMethodsInputProcessor g){
        Map map = null;

        if(mapNum.equalsIgnoreCase("0")){
            map = _mapTable.get("0");
            if( map == null ){
                map = new LVL_0_Map(g);
                _mapTable.put("0",  map);
            }
        } else if(mapNum.equalsIgnoreCase("1")){
            map = _mapTable.get("1");
            if( map == null ){
                map = new LVL_1_Map(g);
                _mapTable.put("1",  map);
            }
        } else if(mapNum.equalsIgnoreCase("3")){
            map = _mapTable.get("3");
            if( map == null ){
                map = new LVL_3_Map(g);
                _mapTable.put("3", map);
            }
        } else if(mapNum.equalsIgnoreCase("8")){
            map = _mapTable.get("8");
            if( map == null ){
                map = new LVL_8_Map(g);
                _mapTable.put("8", map);
            }
        } else if(mapNum.equalsIgnoreCase("8B")){
            map = _mapTable.get("8B");
            if( map == null ){
                map = new LVL_8B_Map(g);
                _mapTable.put("8B", map);
            }
        } else if(mapNum.equalsIgnoreCase("11")){
            map = _mapTable.get("11");
            if( map == null ){
                map = new LVL_11_Map(g);
                _mapTable.put("11", map);
            }
        }

        return map;
    }

}
