package com.mygdx.safe;

import com.badlogic.gdx.Gdx;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;

import java.util.HashMap;


public abstract class Map {
    private static final String TAG = Map.class.getSimpleName();
    protected GenericMethodsInputProcessor g;

    public final static float UNIT_SCALE  = 1/64f;
    public  int MAP_WIDHT;
    public  int MAP_HEIGHT;

    //Map layers

    protected final static String MAP_COLLISION_LAYER = "MAP_COLLISION";
    protected final static String MAP_SPAWNS_LAYER = "MAP_SPAWNS";
    protected final static String MAP_PORTAL_LAYER = "MAP_PORTAL";
    protected final static String MAP_CIRCUNSCRIPTION_LAYER = "MAP_CIRCUNSCRIPTION_LAYER";

    protected MapLayer _collisionLayer = null;
    protected MapLayer _portalLayer = null;
    protected MapLayer _spawnsLayer = null;
    protected MapLayer _circunscriptionLayer = null;

    //Starting locations

    protected final static String PLAYER_START = "PLAYER_START";
    protected final static String NPC_START = "NPC_START";

    protected Json _json;

    protected Vector2 _playerStartPositionRect;
    protected Vector2 _closestPlayerStartPosition;
    protected Vector2 _convertedUnits;
    protected TiledMap _currentMap = null;
    protected Vector2 _playerStart;
    protected Array<Vector2> _npcStartPositions;
    protected Array<String> _npcNameEntities;
    protected Array<Rectangle> collisionRectangles = new Array<Rectangle> ();
    protected HashMap<String, Array<Rectangle>> portalRectangles = new HashMap<String, Array<Rectangle>>();
    protected HashMap<String, Rectangle> circunscriptionRectangles = new HashMap<String, Rectangle>();

    protected HashMap<String, Boolean> eventStates = new HashMap<String, Boolean>();

    protected static boolean isloaded;

    protected Integer _currentMapType;

    protected int [] _layersFirst;
    protected int [] _layersGhost;
    protected int [] _layersLast;


    Map(Integer mapType, String fullMapPath, GenericMethodsInputProcessor g){
        this.g = g;
        _json = new Json();
        _currentMapType = mapType;
        _playerStart = new Vector2(0,0);
        _playerStartPositionRect = new Vector2(0,0);
        _closestPlayerStartPosition = new Vector2(0,0);
        _convertedUnits = new Vector2(0,0);
        _npcNameEntities=new Array<String>();

        isloaded =false;

        if( fullMapPath == null || fullMapPath.isEmpty() ) {
            Gdx.app.debug(TAG, "Map is invalid");
            return;
        }

        Utility.loadMapAsset(fullMapPath);
        if( Utility.isAssetLoaded(fullMapPath) ) {
            _currentMap = Utility.getMapAsset(fullMapPath);
            g.println(TAG+" Map from Asset Load System loaded");

        }else{
            g.println(TAG+" Map Asset Load System NOT loaded");
            return;
        }

        // NUMBER OF LAYERS
        // GETS THE 0 LAYER
        TiledMapTileLayer layer= (TiledMapTileLayer) _currentMap.getLayers().get("BACKGROUND");
        MAP_WIDHT=layer.getWidth();
        MAP_HEIGHT=layer.getHeight();
        g.println("The Map Has "+_currentMap.getLayers().getCount()+ " layers");
        for(int i=0;i<_currentMap.getLayers().getCount();i++){
            g.println("Layer: "+i+ " Layer Name:" +_currentMap.getLayers().get(i).getName());
        }
        g.println("Map WIDTH : "+MAP_WIDHT+ ", MAP HEIGHT: "+MAP_HEIGHT);

        // GET LAYERS
        _collisionLayer = _currentMap.getLayers().get(MAP_COLLISION_LAYER);
        if( _collisionLayer == null ){
            g.println(TAG+" No collision layer!");
        }else{
            g.println(TAG+ " Collision layer loaded");
        }

        _portalLayer = _currentMap.getLayers().get(MAP_PORTAL_LAYER);
        if( _portalLayer == null ){
            g.println(TAG+" No portal layer!");
        }else{
            g.println(TAG+" Portal layer loaded");
        }

        _spawnsLayer = _currentMap.getLayers().get(MAP_SPAWNS_LAYER);
        if( _spawnsLayer == null ){
            g.println(TAG+" No spawn layer!");
        }else{
            setClosestStartPosition(_playerStart);
            g.println(TAG+ " Spawn Layer Loaded");
        }

        _circunscriptionLayer = _currentMap.getLayers().get(MAP_CIRCUNSCRIPTION_LAYER);
        if( _circunscriptionLayer == null ){
            g.println(TAG+" No circunscription layer!");
        }else{
            g.println(TAG+" Circunscription layer Loaded");
        }

        _npcStartPositions = getNPCStartPositions();

        g.println(TAG+" npc Start Positions loaded");

        setCollisionRectangles();       //SETTING           //
        setPortalRectangles();          //RECTANGLES        //
        setCircunsciptionRectangles();  //CIRCUNSCRIPTION   //

        g.println(TAG + circunscriptionRectangles.keySet().toString());


        int totalLayers = 0;
        int firstNoRenderLayers = 0;
        int ghostNoRenderLayers = 0;

        int preGhosLayerPosition = 0;
        int i= 0;
        for(MapLayer currentlayer: _currentMap.getLayers()){

            if(currentlayer.getName().contains("GHOST")) preGhosLayerPosition = i-1;
            i++;

        }

        i=0;
        for (MapLayer currentlayer: _currentMap.getLayers()){

            if ( currentlayer.getName().contains("MAP") && _layersFirst == null) firstNoRenderLayers++;
            else if ( currentlayer.getName().contains("MAP") && _layersGhost == null ) ghostNoRenderLayers++;

            totalLayers++;

            if (i == preGhosLayerPosition && _layersFirst == null) {

                _layersFirst = new int[totalLayers-1- firstNoRenderLayers];
                int index = 0;

                for (int j = 0; j < _layersFirst.length + firstNoRenderLayers; j++) {
                    if (!_currentMap.getLayers().get(j).getName().contains("MAP")) {
                        _layersFirst[index] = j;

                        index++;
                    }
                }
            }

            else if (currentlayer.getName().contains("LAST") && _layersGhost == null){

                _layersGhost = new int[totalLayers-1-_layersFirst.length-firstNoRenderLayers-ghostNoRenderLayers];
                int index = 0;

                for(int j = 0; j < _layersGhost.length + ghostNoRenderLayers ; j++){
                    if (!_currentMap.getLayers().get(j+_layersFirst.length+firstNoRenderLayers).getName().contains("MAP")){
                        _layersGhost[index] = j+_layersFirst.length;
                        index++;
                    }
                }
                _layersLast = new int[1];
                _layersLast[0] = totalLayers-1;
            }

            i++;
        }

        //for (Integer i : _layersFirst)  g.println("Layer: "+i+ " Layer Name:   " +_currentMap.getLayers().get(i).getName() + "*****_layersFirst");
        //for (Integer i : _layersGhost)  g.println("Layer: "+i+ " Layer Name:   " +_currentMap.getLayers().get(i).getName()+ "*****_layersGhost");
        //for (Integer i : _layersLast)   g.println("Layer: "+i+ " Layer Name:   " +_currentMap.getLayers().get(i).getName()+ "*****_layersLast");

    }

    public Array<String> get_npcNameEntities() {
        return _npcNameEntities;
    }

//public Array<Vector2> get_npcStartPositions() {   return _npcStartPositions;  }


    public int[] get_layersFirst() {
        return _layersFirst;
    }

    public int[] get_layersGhost() {
        return _layersGhost;
    }

    public int[] get_layersLast() {
        return _layersLast;
    }

    public Vector2 getPlayerStart() {
        return _playerStart;
    }

    public void setPlayerStart(Vector2 playerStart) {
        this._playerStart = playerStart;
    }

    public HashMap<String, Rectangle> getCircunscriptionRectangles() {
        return circunscriptionRectangles;
    }

    public void setCircunscriptionRectangles(HashMap<String, Rectangle> circunscriptionRectangles) {
        this.circunscriptionRectangles = circunscriptionRectangles;
    }

    public MapLayer getCollisionLayer(){
        return _collisionLayer;
    }

    public MapLayer getPortalLayer(){
        return _portalLayer;
    }

    public MapLayer getCircunscriptionLayer(){  return _circunscriptionLayer;   }

    public TiledMap getCurrentTiledMap() {
        return _currentMap;
    }

    public Vector2 getPlayerStartUnitScaled(){
        Vector2 playerStart = _playerStart.cpy();
        playerStart.set(_playerStart.x * UNIT_SCALE, _playerStart.y * UNIT_SCALE);
        return playerStart;
    }

    private Array<Vector2> getNPCStartPositions(){
        Array<Vector2> npcStartPositions = new Array<Vector2>();

        for( MapObject object: _spawnsLayer.getObjects()){
            String objectName = object.getName();

            if( objectName == null || objectName.isEmpty() ){
                continue;
            }

            if( objectName.contains(NPC_START) ){
                //Get center of rectangle
                _npcNameEntities.add(objectName);
                float x = ((RectangleMapObject)object).getRectangle().getX();
                float y = ((RectangleMapObject)object).getRectangle().getY();
                float width = ((RectangleMapObject)object).getRectangle().getWidth();

                x += width / 2;

                //scale by the unit to convert from map coordinates
                x *= UNIT_SCALE;
                y *= UNIT_SCALE;

                npcStartPositions.add(new Vector2(x,y));
            }
        }
        return npcStartPositions;
    }

    private void setCollisionRectangles(){
            if(!isloaded) {
                Rectangle rectangle = new Rectangle();
                Vector2 position = new Vector2();
                Vector2 size = new Vector2();
                for (MapObject object : _collisionLayer.getObjects()) {
                    if (object instanceof RectangleMapObject) {

                        rectangle = ((RectangleMapObject) object).getRectangle();
                        rectangle.getPosition(position);
                        rectangle.getSize(size);

                        position.x *= UNIT_SCALE;
                        position.y *= UNIT_SCALE;
                        size.x *= UNIT_SCALE;
                        size.y *= UNIT_SCALE;

                        rectangle.set(position.x, position.y, size.x, size.y);
                        collisionRectangles.add(rectangle);

                        g.println(" Collision Rectangle=" + collisionRectangles.get(collisionRectangles.size-1));

                    }

                }
                g.println(TAG + " Collision Rectangles Loaded");
            }
    }

    private void setPortalRectangles(){
        if(!isloaded) {
            Rectangle rectangle = new Rectangle();
            Vector2 position = new Vector2();
            Vector2 size = new Vector2();
            for (MapObject object : _portalLayer.getObjects()) {
                if (object instanceof RectangleMapObject) {

                    String rName = object.getName();

                    rectangle = ((RectangleMapObject) object).getRectangle();
                    rectangle.getPosition(position);
                    rectangle.getSize(size);

                    position.x *= UNIT_SCALE;
                    position.y *= UNIT_SCALE;
                    size.x *= UNIT_SCALE;
                    size.y *= UNIT_SCALE;

                    rectangle.set(position.x, position.y, size.x, size.y);

                    if(portalRectangles.get(rName) == null) portalRectangles.put(rName, new Array<Rectangle>());
                    portalRectangles.get(rName).add(rectangle);



                    g.println("Rectangle=" + rName + "         " + portalRectangles.get(rName));

                }

                if(object.getName().contains("EVENT_")) eventStates.put(object.getName(),false);

            }
            g.println(TAG+ " Portal and Event Rectangles Loaded");

        }
    }

    private void setCircunsciptionRectangles() {
        if (!isloaded) {
            Rectangle rectangle = new Rectangle();
            Vector2 position = new Vector2();
            Vector2 size = new Vector2();
            for (MapObject object : _circunscriptionLayer.getObjects()) {
                if (object instanceof RectangleMapObject) {

                    String rName = object.getName();

                    rectangle = ((RectangleMapObject) object).getRectangle();
                    rectangle.getPosition(position);
                    rectangle.getSize(size);

                    position.x *= UNIT_SCALE;
                    position.y *= UNIT_SCALE;
                    size.x *= UNIT_SCALE;
                    size.y *= UNIT_SCALE;

                    rectangle.set(position.x, position.y, size.x, size.y);

                    circunscriptionRectangles.put(rName, rectangle);

                    g.println("Rectangle=" + rName + "         " + circunscriptionRectangles.get(rName));

                }
            }
            g.println(TAG + " Circunscription Rectangles Loaded");
            isloaded = true;
        }
    }


    public Array<Rectangle> getCollisionRectangles() {  return collisionRectangles;  }

    public HashMap<String, Array<Rectangle>> getPortalRectangles(){ return portalRectangles;  }

    private void setClosestStartPosition(final Vector2 position){
         g.println(TAG+" setClosestStartPosition INPUT: (" + position.x + "," + position.y + ") " + _currentMapType.toString());

        //Get last known position on this map
        _playerStartPositionRect.set(0,0);
        _closestPlayerStartPosition.set(0,0);
        float shortestDistance = 0f;

        //Go through all player start positions and choose closest to last known position
        for( MapObject object: _spawnsLayer.getObjects()){
            String objectName = object.getName();

            if( objectName == null || objectName.isEmpty() ){
                continue;
            }

            if( objectName.equalsIgnoreCase(PLAYER_START) ){
                ((RectangleMapObject)object).getRectangle().getPosition(_playerStartPositionRect);
                float distance = position.dst2(_playerStartPositionRect);

                g.println(TAG+ " DISTANCE: " + distance + " for " + _currentMapType.toString());

                if( distance < shortestDistance || shortestDistance == 0 ){
                    _closestPlayerStartPosition.set(_playerStartPositionRect);
                    shortestDistance = distance;
                    g.println(TAG+ " closest START is: (" + _closestPlayerStartPosition.x + "," + _closestPlayerStartPosition.y + ") " +  _currentMapType.toString());
                }
            }
        }
        _playerStart =  _closestPlayerStartPosition.cpy();
    }

    public void setClosestStartPositionFromScaledUnits(Vector2 position){
        if( UNIT_SCALE <= 0 )
            return;

        _convertedUnits.set(position.x/UNIT_SCALE, position.y/UNIT_SCALE);
        setClosestStartPosition(_convertedUnits);
    }

    public void actualizeGhostLayers(String layerID){

        boolean found = false;
        int[] _newLayersGhost;
        int layerPosition = 0;
        int index = 0;

        //The layerID must contain "GHOST" ****NOT IMPLEMENTED YET
       /* if(!layerID.contains("GHOST")){
            g.println("INVALID GHOSTLAYER ID");
            return;
        }*/

        //Search the layer in the Map. LayerPosition save the position of the layer in the MapLayers of the Map
        for(MapLayer layer : _currentMap.getLayers()){
            if(layerID.contains(layer.getName()))break;
            layerPosition++;
        }

        if(layerPosition == _currentMap.getLayers().getCount()){
            g.println("The layer " + layerID + " doesn't exist in this map!!");
            return;
        }

        //Search in the current _layerGhost Array the number that corresponds to the layerID
        for(Integer i :_layersGhost){
            if(i == layerPosition){
                found = true; break;
            }
        }

        //Create a new layerGhost for each situation
        if(found){

            _newLayersGhost = new int[_layersGhost.length-1];

            for(Integer i :_layersGhost){
                if(i != layerPosition){
                    _newLayersGhost[index] = i;
                    index++;
                }
            }
        }
        else{

            _newLayersGhost = new int[_layersGhost.length+1];

            for(Integer i : _layersGhost){
                _newLayersGhost[index] = i;
                index++;
            }

            _newLayersGhost[index] = layerPosition;
        }

        //The _layersGhost gets the contained object in _newLayersGhost
        _layersGhost = _newLayersGhost;
    }
}
