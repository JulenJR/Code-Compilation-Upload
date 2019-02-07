package com.mygdx.safe.safe;

import com.badlogic.gdx.utils.Array;
import com.mygdx.safe.EmoType;

/**
 * Created by Boris.Inspirat on 1/08/17.
 */

public class EmoTypeConfig{

    //TAG
    private static final String TAG = EmoTypeConfig.class.getSimpleName();

    //EMO
    private Array<com.mygdx.safe.EmoTypeRange> _emorange;
    private Array<EmoType> _emotions;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public EmoTypeConfig(){
        _emorange=new Array<com.mygdx.safe.EmoTypeRange>();
        _emotions=new Array<EmoType>();
    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS
    public Array<EmoType> get_emotions() { return _emotions; }

    public Array<com.mygdx.safe.EmoTypeRange> get_emorange() { return _emorange; }

    //SETTERS
    public void set_emotions(Array<EmoType> _emotions) { this._emotions = _emotions; }

    public void set_emorange(Array<com.mygdx.safe.EmoTypeRange> _emorange) { this._emorange = _emorange; }






}
