package com.mygdx.safe.UI;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.mygdx.safe.Utility;

public class StatusUI extends Window {

    //TAG
    private static final String TAG = StatusUI.class.getSimpleName();

    //ASPECTS
    private com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor g;

    //IMAGE
    private Image _hpBar;
    private Image _mpBar;
    private Image _xpBar;
    private ImageButton _inventoryButton;

    //ATTRIBUTES
    private int _levelVal = 1;
    private int _goldVal = 0;
    private int _hpVal = 50;
    private int _mpVal = 50;
    private int _xpVal = 0;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public StatusUI(){
        super("stats", Utility.STATUSUI_SKIN);

        //groups
        WidgetGroup group = new WidgetGroup();
        WidgetGroup group2 = new WidgetGroup();
        WidgetGroup group3 = new WidgetGroup();

        //images
        _hpBar = new Image(Utility.STATUSUI_TEXTUREATLAS.findRegion("HP_Bar"));
        Image bar = new Image(Utility.STATUSUI_TEXTUREATLAS.findRegion("Bar"));
        _mpBar = new Image(Utility.STATUSUI_TEXTUREATLAS.findRegion("MP_Bar"));
        Image bar2 = new Image(Utility.STATUSUI_TEXTUREATLAS.findRegion("Bar"));
        _xpBar = new Image(Utility.STATUSUI_TEXTUREATLAS.findRegion("XP_Bar"));
        Image bar3 = new Image(Utility.STATUSUI_TEXTUREATLAS.findRegion("Bar"));

        //labels
        Label hpLabel = new Label(" hp:", Utility.STATUSUI_SKIN);
        Label hp = new Label(String.valueOf(_hpVal), Utility.STATUSUI_SKIN);
        Label mpLabel = new Label(" mp:", Utility.STATUSUI_SKIN);
        Label mp = new Label(String.valueOf(_mpVal), Utility.STATUSUI_SKIN);
        Label xpLabel = new Label(" xp:", Utility.STATUSUI_SKIN);
        Label xp = new Label(String.valueOf(_xpVal), Utility.STATUSUI_SKIN);
        Label levelLabel = new Label(" lv:", Utility.STATUSUI_SKIN);
        Label levelVal = new Label(String.valueOf(_levelVal), Utility.STATUSUI_SKIN);
        Label goldLabel = new Label(" gp:", Utility.STATUSUI_SKIN);
        Label goldVal = new Label(String.valueOf(_goldVal), Utility.STATUSUI_SKIN);

        //buttons
        _inventoryButton= new ImageButton(Utility.STATUSUI_SKIN, "inventory-button");
        _inventoryButton.getImageCell().size(32, 32);

        //Align images
        _hpBar.setPosition(3, 6);
        _mpBar.setPosition(3, 6);
        _xpBar.setPosition(3, 6);

        //add to widget groups
        group.addActor(bar);
        group.addActor(_hpBar);
        group2.addActor(bar2);
        group2.addActor(_mpBar);
        group3.addActor(bar3);
        group3.addActor(_xpBar);

        //Add to layout
        defaults().expand().fill();

        //account for the title padding
        this.pad(this.getPadTop() + 10, 10, 10, 10);

        this.add();
        this.add();
        this.add(_inventoryButton).align(Align.right);
        this.row();

        this.add(group).size(bar.getWidth(), bar.getHeight());
        this.add(hpLabel);
        this.add(hp).align(Align.left);
        this.row();

        this.add(group2).size(bar2.getWidth(), bar2.getHeight());
        this.add(mpLabel);
        this.add(mp).align(Align.left);
        this.row();

        this.add(group3).size(bar3.getWidth(), bar3.getHeight());
        this.add(xpLabel);
        this.add(xp).align(Align.left);
        this.row();

        this.add(levelLabel).align(Align.left);
        this.add(levelVal).align(Align.left);
        this.row();
        this.add(goldLabel);
        this.add(goldVal).align(Align.left);

        //this.debug();
        this.pack();
    }

    public ImageButton getInventoryButton() {
        return _inventoryButton;
    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS

    public Image get_hpBar() {
        return _hpBar;
    }

    public Image get_mpBar() {
        return _mpBar;
    }

    public Image get_xpBar() {
        return _xpBar;
    }

    public ImageButton get_inventoryButton() {
        return _inventoryButton;
    }

    public int get_levelVal() {
        return _levelVal;
    }

    public int get_goldVal() {
        return _goldVal;
    }

    public int get_hpVal() {
        return _hpVal;
    }

    public int get_mpVal() {
        return _mpVal;
    }

    public int get_xpVal() {
        return _xpVal;
    }

    //SETTERS


    public void set_hpBar(Image _hpBar) {
        this._hpBar = _hpBar;
    }

    public void set_mpBar(Image _mpBar) {
        this._mpBar = _mpBar;
    }

    public void set_xpBar(Image _xpBar) {
        this._xpBar = _xpBar;
    }

    public void set_inventoryButton(ImageButton _inventoryButton) {
        this._inventoryButton = _inventoryButton;
    }

    public void set_levelVal(int _levelVal) {
        this._levelVal = _levelVal;
    }

    public void set_goldVal(int _goldVal) {
        this._goldVal = _goldVal;
    }

    public void set_hpVal(int _hpVal) {
        this._hpVal = _hpVal;
    }

    public void set_mpVal(int _mpVal) {
        this._mpVal = _mpVal;
    }

    public void set_xpVal(int _xpVal) {
        this._xpVal = _xpVal;
    }
}
