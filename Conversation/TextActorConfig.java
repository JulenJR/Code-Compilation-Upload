package com.mygdx.safe.Conversation;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.safe.Components.HudDataComponent;

/**
 * Created by Boris.Inspiratgames on 19/09/17.
 */

public class TextActorConfig {

    //TAG
    private static final String TAG = TextActorConfig.class.getSimpleName();

    //ASPECTS
    private com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g;

    //FONT
    private String selectedFont;
    private String normalFontTextPath;
    private String normalFontGlyphPath;
    private String smallFontTextPath;
    private String smallFontGlyphPath;
    private String dialogFontTextPath;
    private String dialogFontGlyphPath;
    private String dialogShadowFontTextPath;
    private String dialogShadowFontGlyphPath;
    private String hudFontTextPath;
    private String hudFontGlyphPath;
    private String lvlclrFontTextPath;
    private String lvlclrFontGlyphPath;


    //POSITION
    private Vector2 positionDistChanger;

    //BOOLEANS
    private boolean whitShape;
    private boolean isvisible;

    //DEFAULTS
    private float defaultScale;
    private String defaultText;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public TextActorConfig(){}

    //CONFIG
    public void Config(TextActor t, com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g){

        this.g = g;

        t.setPositionDistChanger(positionDistChanger);
        t.setTextActorScale(defaultScale);
        t.setTextActorText(defaultText);
        t.setVisible(isvisible);
        t.setWithShape(whitShape);
        if(selectedFont.equalsIgnoreCase("normal")){
            t.NewTexActorFont(normalFontTextPath,normalFontGlyphPath);
        }
        else if(selectedFont.equalsIgnoreCase("dialog")){
            t.NewTexActorFont(dialogFontTextPath,dialogFontGlyphPath);
        }
        else if(selectedFont.equalsIgnoreCase("dialogShadow")){
            t.NewTexActorFont(dialogShadowFontTextPath,dialogShadowFontGlyphPath);
        }
        else if(selectedFont.equalsIgnoreCase("hud")){
            t.NewTexActorFont(hudFontTextPath,hudFontGlyphPath);
        }
        else{
            t.NewTexActorFont(smallFontTextPath,smallFontGlyphPath);
        }

    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS

    public String getSelectedFont() {
        return selectedFont;
    }

    public String getNormalFontTextPath() {
        return normalFontTextPath;
    }

    public String getNormalFontGlyphPath() {
        return normalFontGlyphPath;
    }

    public String getSmallFontTextPath() {
        return smallFontTextPath;
    }

    public String getSmallFontGlyphPath() {
        return smallFontGlyphPath;
    }

    public Vector2 getPositionDistChanger() {
        return positionDistChanger;
    }

    public boolean isWhitShape() {
        return whitShape;
    }

    public boolean isvisible() {
        return isvisible;
    }

    public float getDefaultScale() {
        return defaultScale;
    }

    public String getDefaultText() {
        return defaultText;
    }

    //SETTERS

    public void SetAllActorConfig(com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g, Stage s, HudDataComponent h, com.mygdx.safe.Entities.HUD_Entity he){

        for (String key: g.getTextactors().keySet()){
              g.getTextactors().get(key).addTextActorToHUD(s);
            }
        he.add(h);
    }

    public void setSelectedFont(String selectedFont) {
        this.selectedFont = selectedFont;
    }

    public void setNormalFontTextPath(String normalFontTextPath) {
        this.normalFontTextPath = normalFontTextPath;
    }

    public void setNormalFontGlyphPath(String normalFontGlyphPath) {
        this.normalFontGlyphPath = normalFontGlyphPath;
    }

    public void setSmallFontTextPath(String smallFontTextPath) {
        this.smallFontTextPath = smallFontTextPath;
    }

    public void setSmallFontGlyphPath(String smallFontGlyphPath) {
        this.smallFontGlyphPath = smallFontGlyphPath;
    }

    public void setPositionDistChanger(Vector2 positionDistChanger) {
        this.positionDistChanger = positionDistChanger;
    }

    public void setWhitShape(boolean whitShape) {
        this.whitShape = whitShape;
    }

    public void setIsvisible(boolean isvisible) {
        this.isvisible = isvisible;
    }

    public void setDefaultScale(float defaultScale) {
        this.defaultScale = defaultScale;
    }

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }
}
