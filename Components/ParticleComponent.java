package com.mygdx.safe.Components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;


/**
 * Created by practicas on 12/12/17.
 */

public class ParticleComponent implements Component {

    Vector2 position;

    public ParticleComponent(){
        this.position = new Vector2();
    }

}
