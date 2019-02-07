package com.mygdx.safe;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

public class MapManager {

    private static final String TAG = MapManager.class.getSimpleName();

    private com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g;

    private boolean _mapChanged = false;
    private Camera _camera;
    private Map _currentMap;

    public MapManager(com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g){
        this.g = g;
    }

    /*@Override
    public void onNotify(ProfileManager profileManager, ProfileEvent event) {
        switch(event){
            case PROFILE_LOADED:
                String currentMap = profileManager.getProperty("currentMapType", String.class);
                Integer mapNum;
                if( currentMap == null || currentMap.isEmpty() ){
                    mapNum = 1;
                }else{
                    mapNum = 1;
                }
                //loadMap(mapNum);

                Vector2 lvl_3_MapStartPosition = profileManager.getProperty("LVL_3_MapStartPosition", Vector2.class);
                if( lvl_3_MapStartPosition != null ){
                    MapFactory.getMap(3, g).setPlayerStart(lvl_3_MapStartPosition);
                }

                Vector2 lvl_8_MapStartPosition = profileManager.getProperty("LVL_8_MapStartPosition", Vector2.class);
                if( lvl_8_MapStartPosition != null ){
                    MapFactory.getMap(8, g).setPlayerStart(lvl_8_MapStartPosition);
                }

                Vector2 lvl_1_MapStartPosition = profileManager.getProperty("LVL_1_MapStartPosition", Vector2.class);
                if( lvl_1_MapStartPosition != null ){
                    MapFactory.getMap(1, g).setPlayerStart(lvl_1_MapStartPosition);
                }

                break;
            case SAVING_PROFILE:
                //profileManager.setProperty("currentMapType", _currentMap._currentMapType.toString());
                profileManager.setProperty("LVL_3_MapStartPosition", MapFactory.getMap(3, g).getPlayerStart() );
                profileManager.setProperty("LVL_8_MapStartPosition", MapFactory.getMap(8, g).getPlayerStart() );
                profileManager.setProperty("LVL_1_MapStartPosition", MapFactory.getMap(1, g).getPlayerStart() );
                break;
            default:
                break;
        }
    }*/

    public void loadMap(String mapNum){

        Map map = MapFactory.getMap(mapNum, g);


        if( map == null ){
            g.println(TAG+" Map does not exist!  ");
            return;
        }

        _currentMap = null;
        _currentMap = map;
        _mapChanged = true;
        g.println(TAG+ " MAP LEVEL NUMBER = "+ mapNum);
        g.println(TAG+" Player Start: (" + _currentMap.getPlayerStart().x + "," + _currentMap.getPlayerStart().y + ")");
    }

    public void setClosestStartPositionFromScaledUnits(Vector2 position) {
        _currentMap.setClosestStartPositionFromScaledUnits(position);
    }

    public Array<String> getNpcNameEntities(){ return _currentMap.get_npcNameEntities(); }

    public MapLayer getCollisionLayer(){
        return _currentMap.getCollisionLayer();
    }

    public MapLayer getPortalLayer(){
        return _currentMap.getPortalLayer();
    }
    public Vector2 getPlayerStartUnitScaled() {
        return _currentMap.getPlayerStartUnitScaled();
    }

    public int getMAP_WIDHT(){ return _currentMap.MAP_WIDHT; }

    public int getMAP_HEIGHT(){ return _currentMap.MAP_HEIGHT; }

    public Map get_currentMap() {
        return _currentMap;
    }


    public TiledMap getCurrentTiledMap(){
        if( _currentMap == null ) {
            loadMap("1");
        }
        return _currentMap.getCurrentTiledMap();
    }
    public Array<Rectangle> getCollisionRectangles(){
        return _currentMap.getCollisionRectangles();
    }

    public HashMap<String, Array<Rectangle>> getPortalRectangles(){
        return _currentMap.getPortalRectangles();
    }

    public Array<Vector2> getNpcStartPositions(){
        return _currentMap._npcStartPositions;
    }

    public void setCamera(Camera camera){
        this._camera = camera;
    }

    public Camera getCamera(){
        return _camera;
    }

    public boolean hasMapChanged(){
        return _mapChanged;
    }

    public void setMapChanged(boolean hasMapChanged){
        this._mapChanged = hasMapChanged;
    }
}
