package com.mygdx.safe;

import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;

/**
 * Created by alumne_practiques on 27/09/17.
 */

public class nCache {

    GenericMethodsInputProcessor g;

    private String _nAnimationID;
    private com.mygdx.safe.NanimationMap _nAnimation;


    //private Array<TextureAtlas> _textureAtlasList = new Array<TextureAtlas>();
    //private Array<String> _nAnimationKeys = new Array<String>();



    public nCache(String name, com.mygdx.safe.EntityConfig.nAnimationTriConfig nAnimConfig, GenericMethodsInputProcessor g){

        this.g = g;
        this._nAnimationID = name;

        this._nAnimation = new com.mygdx.safe.NanimationMap(nAnimConfig, g);


        //Create a texture atlas from the atlasPath of entityConfig when the entity have Nanimation and get a hashmap of all the animations

        /*for(EntityConfig.nAnimationTriConfig triConfig : entityConfig.get_nAnimationTriConfigs()){

            _nAnimationKeys.add(triConfig.get_nAnimationConfig().nAnimationName);
            _textureAtlasList.add(new TextureAtlas(triConfig.get_nAnimationConfig().getAtlasPath()));

        }*/
    }

    //Getters


    public com.mygdx.safe.NanimationMap get_nAnimation() { return _nAnimation; }


    public String get_nAnimationID() {
        return _nAnimationID;
    }

    //Setters




}
