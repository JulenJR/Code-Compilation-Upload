package com.mygdx.safe;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;

import com.mygdx.safe.Components.InputComponent;
import com.mygdx.safe.Entities.GEActon;
import com.mygdx.safe.Entities.GameEntity;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;
import com.mygdx.safe.safe.EmoTypeConfig;
import com.mygdx.safe.safe.PowBarsConfig;

import java.util.HashMap;
import java.util.Iterator;

import static com.mygdx.safe.Entities.GameEntity.*;


/**
 * Created by Boris.InspitatGames on 13/06/17.
 */

public class EntityFactory {

    private static final String TAG = EntityFactory.class.getSimpleName();
    /*public enum EntityType{
        PLAYER,
        DEMO_PLAYER,
        NPC
    }*/

    //public static String PLAYER_CONFIG="scripts/playerbartolo.json";
    //public static String NPC_CONFIG="scripts/NPC.json";
    public static String TRICEPTION_CONFIG="scripts/safeT.json";
    public static String TRICEPTION_EMOTYPECONFIG="scripts/triceptionEmoTypeConfig.json";
    public static String POWBARSCONFIG="scripts/powbarscfg.json";
    //public static String HALO = "scripts/halo.json";
    public static String DIALOGJSONPATH="scripts/dialogConfig.json";

    EntityFactory(){

    }

    static public GameEntity getEntity(com.mygdx.safe.EntityConfig entityConfig, com.mygdx.safe.sCache sCache, HashMap<String, com.mygdx.safe.nCache>  nCacheList, GenericMethodsInputProcessor g, MapManager _mapmgr,
                                       OrthographicCamera camera, String ID){

        GameEntity gameentity;
        gameentity=new GameEntity(g,camera);

        gameentity.setEntityConfig(entityConfig);

        gameentity.ConfigGameEntity(InputComponent.InputMethod.ALL_INPUTS,_mapmgr.getPlayerStartUnitScaled(), sCache, nCacheList, ID);

        g.println(TAG + " GET ENTITY (GAME ENTITY MAKER): " +  gameentity.getEntityConfig().get_entityID());

        return gameentity;
    }

    static public GEActon getActonEntity(com.mygdx.safe.EntityConfig entityConfig, com.mygdx.safe.sCache sCache, HashMap<String, com.mygdx.safe.nCache>  nCacheList, GenericMethodsInputProcessor g, MapManager _mapmgr,
                                         OrthographicCamera camera, String ID){

        GEActon acton;
        acton=new GEActon(g,camera);

        acton.setEntityConfig(entityConfig);
        acton.ConfigGameEntity(InputComponent.InputMethod.ALL_INPUTS,_mapmgr.getPlayerStartUnitScaled(), sCache, nCacheList, ID);

        g.println(TAG + " GET ACTON ENTITY (GAME ENTITY MAKER): " +  acton.getEntityConfig().get_entityID());

        return acton;
    }



    static public synchronized GameEntity getNPCEntity(com.mygdx.safe.EntityConfig entityConfig, com.mygdx.safe.sCache sCache, HashMap<String, com.mygdx.safe.nCache>  nCacheList, GenericMethodsInputProcessor g, Vector2 positionNPC,
                                          OrthographicCamera camera, String ID){

        GameEntity gameentity;
        gameentity=new GameEntity(g,camera);

        gameentity.setEntityConfig(entityConfig);
        gameentity.ConfigGameEntity(InputComponent.InputMethod.AUTOMATIC,positionNPC, sCache, nCacheList, ID);

        g.println(TAG + " GET NPC ENTITY (GAME ENTITY MAKER): " +  gameentity.getEntityConfig().get_entityID());

        return gameentity;
    }


    static public synchronized void load(GenericMethodsInputProcessor g, HashMap<String, com.mygdx.safe.sCache> sCacheHashMap,
                            HashMap<String, com.mygdx.safe.nCache> nCacheHashMap, HashMap<String, com.mygdx.safe.EntityConfig> entityConfigHashMap, HashMap<String, String> configPathsMap){

        com.mygdx.safe.EntityConfig entityConfig = GameEntity.GetEntityConfig(configPathsMap.get("PLAYER_CONFIG"));
        com.mygdx.safe.EntityConfig haloConfig = GameEntity.GetEntityConfig(configPathsMap.get("HALO_CONFIG"));
        com.mygdx.safe.EntityConfig actonConfig = GameEntity.GetEntityConfig(configPathsMap.get("ACTON_CONFIG"));
        com.mygdx.safe.EntityConfig verjaConfig = GameEntity.GetEntityConfig(configPathsMap.get("VERJA_CONFIG"));
        com.mygdx.safe.EntityConfig stoneConfig = GameEntity.GetEntityConfig(configPathsMap.get("STONE_CONFIG"));
        com.mygdx.safe.EntityConfig tapeConfig = GameEntity.GetEntityConfig(configPathsMap.get("TAPE_CONFIG"));
        com.mygdx.safe.EntityConfig triceptionConfig = GameEntity.GetEntityConfig(configPathsMap.get("TRICEPTION_CONFIG"));
        com.mygdx.safe.EmoConfig emoconfig = GetEmoConfig(configPathsMap.get("EMO_CONFIG"));
        com.mygdx.safe.npcConfig npcConfig = GetArrayEntityConfig(configPathsMap.get("NPC_CONFIG"));
        com.mygdx.safe.npcConfig itemsConfig = GetArrayEntityConfig(configPathsMap.get("ITEMS_CONFIG"));


        Iterator<String> itr = null;
        String name = "";
        String nCacheName = "";

        //PLAYER

        if(entityConfig.is_isSpine()){
            g.println(TAG+" LOAD: SPINE PLAYER ENTITY LOADING: [" + entityConfig.get_entityID() +"]");
            sCacheHashMap.put(entityConfig.get_entityID(), new com.mygdx.safe.sCache(entityConfig,g));
        }
        if(entityConfig.is_hasNAnimation()){

            entityConfig.g=g;
            itr = entityConfig.get_nAnimationTriConfigs().keySet().iterator();

            while (itr.hasNext()){

                //get the name of the nAnimation (only the name not the number)
                name = itr.next();
                nCacheName = name;
                g.println(TAG+" LOAD: NANIMATION PLAYER ENTITY LOADING: [" + name +"]");
                if(nCacheHashMap.get(nCacheName) == null) {
                    nCacheHashMap.put(nCacheName, new com.mygdx.safe.nCache(nCacheName, entityConfig.get_nAnimationTriConfigs().get(name), g));
                }
            }
        }

        entityConfigHashMap.put(entityConfig.get_entityID(), entityConfig);

        //NPC
        for(EntityConfig e: npcConfig.getNpcArray()){

            if(e.is_isSpine()){
                g.println(TAG+" LOAD: SPINE NPC ENTITY LOADING: [" + e.get_entityID()+"]");
                sCacheHashMap.put(e.get_entityID(), new sCache(e,g));
            }
            if(e.is_hasNAnimation()){
                e.g=g;

                itr = e.get_nAnimationTriConfigs().keySet().iterator();

                while (itr.hasNext()){

                    //get the name of the nAnimation (only the name not the number)
                    name = itr.next();
                    nCacheName = name;
                    g.println(TAG+" LOAD: NANIMATION NPC ENTITY LOADING: [" + name +"]");

                    if(nCacheHashMap.get(nCacheName) == null) {
                        nCacheHashMap.put(nCacheName, new com.mygdx.safe.nCache(nCacheName, e.get_nAnimationTriConfigs().get(name), g));
                    }
                }
            }
            entityConfigHashMap.put(e.get_entityID(), e);
        }

        //ITEMS
        for(com.mygdx.safe.EntityConfig e: itemsConfig.getNpcArray()){

            if(e.is_hasNAnimation()){
                e.g=g;
                itr = e.get_nAnimationTriConfigs().keySet().iterator();

                while (itr.hasNext()){

                    //get the name of the nAnimation (only the name not the number)
                    name = itr.next();
                    nCacheName = name;


                    if(nCacheHashMap.get(nCacheName) == null) {

                        g.println(TAG+ " LOAD: NANIMATION ITEM ENTITY LOADING: "+ name);
                        nCacheHashMap.put(e.get_entityID(), new com.mygdx.safe.nCache(e.get_entityID(), e.get_nAnimationTriConfigs().get(name), g));
                    }
                }
            }

            entityConfigHashMap.put(e.get_entityID(), e);
            //g.m.he.getHudActorDataComponent().inventory.get_completeItemList().put(e.get_entityID(), new com.mygdx.safe.UI.InventoryItem(e, g));
        }

        //EMOS
        for(com.mygdx.safe.EntityConfig e: emoconfig.getEmoArray()){

            if(e.is_isSpine()){
                g.println(TAG+ " LOAD: SPINE EMO ENTITY LOADING: "+ e.get_entityID());
                sCacheHashMap.put(e.get_entityID(), new com.mygdx.safe.sCache(e,g));
            }
            if(e.is_hasNAnimation()){
                e.g=g;

                itr = e.get_nAnimationTriConfigs().keySet().iterator();

                while (itr.hasNext()){

                    //get the name of the nAnimation (only the name not the number)
                    name = itr.next();
                    nCacheName = name;
                    g.println(TAG+ " LOAD: NANIMATION EMO ENTITY LOADING: "+ name);

                    if(nCacheHashMap.get(nCacheName) == null) {
                        nCacheHashMap.put(nCacheName, new com.mygdx.safe.nCache(nCacheName, e.get_nAnimationTriConfigs().get(name), g));
                    }
                }
            }
            entityConfigHashMap.put(e.get_entityID(), e);
        }

        //HALO

        if(haloConfig.is_hasNAnimation()){

            haloConfig.g=g;
            itr = haloConfig.get_nAnimationTriConfigs().keySet().iterator();

            while (itr.hasNext()){

                //get the name of the nAnimation (only the name not the number)

                name = itr.next();
                nCacheName = name;
                g.println(TAG+ " LOAD: NANIMATION ENTITY LOADING: "+ name);

                if(nCacheHashMap.get(nCacheName) == null) {
                    nCacheHashMap.put(nCacheName, new com.mygdx.safe.nCache(nCacheName, haloConfig.get_nAnimationTriConfigs().get(name), g));
                }
            }
        }

        entityConfigHashMap.put(haloConfig.get_entityID(), haloConfig);

        //ACTON

        if(actonConfig.is_hasNAnimation()){

            actonConfig.g=g;
            itr = actonConfig.get_nAnimationTriConfigs().keySet().iterator();

            while(itr.hasNext()){

                name = itr.next();
                nCacheName = name;
                g.println(TAG+ " LOAD: NANIMATION ENTITY LOADING: "+ name);

                if(nCacheHashMap.get(nCacheName) == null){
                    nCacheHashMap.put(nCacheName, new com.mygdx.safe.nCache(nCacheName, actonConfig.get_nAnimationTriConfigs().get(name), g));
                }

            }

        }

        entityConfigHashMap.put(actonConfig.get_entityID(), actonConfig);

        //VERJA

        if(verjaConfig.is_hasNAnimation()){

            verjaConfig.g=g;
            itr = verjaConfig.get_nAnimationTriConfigs().keySet().iterator();

            while (itr.hasNext()){

                name = itr.next();
                nCacheName = name;
                g.println(TAG+ " LOAD: NANIMATION ENTITY LOADING: "+ name);

                if(nCacheHashMap.get(nCacheName) == null){
                    nCacheHashMap.put(nCacheName, new com.mygdx.safe.nCache(nCacheName, verjaConfig.get_nAnimationTriConfigs().get(name), g));
                }
            }
        }

        entityConfigHashMap.put(verjaConfig.get_entityID(), verjaConfig);

        //STONE

        if(stoneConfig.is_hasNAnimation()){

            stoneConfig.g=g;
            itr = stoneConfig.get_nAnimationTriConfigs().keySet().iterator();

            while (itr.hasNext()){

                name = itr.next();
                nCacheName = name;
                g.println(TAG+ " LOAD: NANIMATION ENTITY LOADING: "+ name);

                if(nCacheHashMap.get(nCacheName) == null){
                    nCacheHashMap.put(nCacheName, new com.mygdx.safe.nCache(nCacheName, stoneConfig.get_nAnimationTriConfigs().get(name), g));
                }
            }
        }

        entityConfigHashMap.put(stoneConfig.get_entityID(), stoneConfig);

        //TAPE

        if(tapeConfig.is_hasNAnimation()){

            tapeConfig.g=g;
            itr = tapeConfig.get_nAnimationTriConfigs().keySet().iterator();

            while (itr.hasNext()){

                name = itr.next();
                nCacheName = name;
                g.println(TAG+ " LOAD: NANIMATION ENTITY LOADING: "+ name);

                if(nCacheHashMap.get(nCacheName) == null){
                    nCacheHashMap.put(nCacheName, new com.mygdx.safe.nCache(nCacheName, tapeConfig.get_nAnimationTriConfigs().get(name), g));
                }
            }
        }

        entityConfigHashMap.put(tapeConfig.get_entityID(), tapeConfig);

        // TRICEPTION

        if(triceptionConfig.is_hasNAnimation()){

            triceptionConfig.g=g;
            itr = triceptionConfig.get_nAnimationTriConfigs().keySet().iterator();

            while (itr.hasNext()){

                name = itr.next();
                nCacheName = name;
                g.println(TAG+ " LOAD: NANIMATION ENTITY LOADING: "+ name);

                if(nCacheHashMap.get(nCacheName) == null){
                    nCacheHashMap.put(nCacheName, new com.mygdx.safe.nCache(nCacheName, triceptionConfig.get_nAnimationTriConfigs().get(name), g));
                }
            }
        }

        entityConfigHashMap.put(triceptionConfig.get_entityID(), triceptionConfig);

    }

    static public com.mygdx.safe.EmoConfig GetEmoConfig(String path){
        Json json = new Json();
        return json.fromJson(com.mygdx.safe.EmoConfig.class, Gdx.files.internal(path));
    }

    static public EmoTypeConfig GetEmoTypeConfig(String path){
        Json json = new Json();
        return json.fromJson(EmoTypeConfig.class, Gdx.files.internal(path));
    }

    static public PowBarsConfig GetPowerBarsConfig(String path){
        Json json = new Json();
        return json.fromJson(PowBarsConfig.class, Gdx.files.internal(path));
    }

    static public com.mygdx.safe.Conversation.TextActorConfig GetTextActorConfig(){
        Json json =new Json();
        return json.fromJson(com.mygdx.safe.Conversation.TextActorConfig.class, Gdx.files.internal(DIALOGJSONPATH));
    }

    /*public static void setHudTextActors(GenericMethodsInputProcessor g,Camera camera, Array<TextActor> _t, DataActors d) {

        for (int i = 0; i < d._dataActorsArray.size; i++) {
            _t.add(new TextActor(camera,g));
            _t.get(i).setTextActorText(d._dataActorsArray.get(i).dataActortext);
            _t.get(i).setTextActorPosition((int) d._dataActorsArray.get(i).dataActorposition.x, (int) d._dataActorsArray.get(i).dataActorposition.y);
            _t.get(i).setTextActorScale(d._dataActorsArray.get(i).dataActorscale);
            if(d._dataActorsArray.get(i).textType.contains("smallFont"))
                _t.get(i).NewTexActorFont(d.smallFontTextPath, d.smallFontGlyphPath);
            else if(d._dataActorsArray.get(i).textType.contains("normalFont"))
                _t.get(i).NewTexActorFont(d.normalFontTextPath, d.normalFontGlyphPath);
        }
    }*/


}
