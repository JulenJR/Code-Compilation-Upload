package com.mygdx.safe;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

import jdk.nashorn.internal.parser.JSONParser;
import jdk.nashorn.internal.runtime.JSONFunctions;

import static com.badlogic.gdx.Gdx.files;



/**
 * Created by Boris.InspiratGames on 28/04/17.
 */


public class jsonexport<T> {

    private Json json;
    private T t;
    private FileHandle miarchivojson;
    boolean isLocAvailable = files.isLocalStorageAvailable();


    public jsonexport() {
        json = new Json();


    }

    public void set(T t) {
        this.t = t;
        System.out.println("Setting object " + t.getClass().toString().replace(" ", "-") + " done.");
    }

    public T get() {
        System.out.println("Loading object: " + t.getClass().toString().replace(" ", "-"));
        return t;
    }

    public void export() {
        String s = t.getClass().toString().replace(" ", "-");
        export(s);
    }

    public void export(String s) {

        if (!isLocAvailable) {
            System.out.println("Error, External FileTipe System not available\n");
        } else {
            if (files.local(s + ".json").exists()) {
                System.out.println("The " + s + ".json file is already exists.");
            } else {
                System.out.println("The " + s + ".json file is created.");
                json.setOutputType(JsonWriter.OutputType.json);
                json.setIgnoreUnknownFields(false);
                json.setIgnoreDeprecated(false);
                json.setUsePrototypes(true);
                json.setEnumNames(true);

                miarchivojson = files.local(s + ".json");


                System.out.println("Writing " + s + ".json ...");

                                String text = json.prettyPrint(t,0);
                System.out.println("TEXT LENGTH:"+ text.length());
                miarchivojson.writeString(text, false);


                System.out.println( s + ".json done.");
            }
        }
    }
}

