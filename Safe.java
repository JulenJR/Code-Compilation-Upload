package com.mygdx.safe;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

import com.badlogic.gdx.graphics.g2d.CpuSpriteBatch;

import com.mygdx.safe.screens.AgeScreen;
import com.mygdx.safe.screens.CentralGameScreen;
import com.mygdx.safe.screens.MainGameScreen;
import com.mygdx.safe.screens.ProfileScren;
import com.mygdx.safe.screens.StartGameScreen;
import com.mygdx.safe.screens.GeneralMenuScreen;

/**
 *
 *
 *   ./gradlew jar dist PARA GENERAR EL DESKTOP JAR
 *
 */

public class Safe extends Game {

	private static MainGameScreen _mainGameScreen;
	private static StartGameScreen _startGameScreen;
	private static AgeScreen _ageScreen;
	private static GeneralMenuScreen _generalMenuScreen;
	private static ProfileScren _profileScren;
	private static CentralGameScreen _centralGameScreen;

	public CpuSpriteBatch spriteBatch;

	private static boolean _isAndroid;

	private static boolean _loadingMainGame=false;
	private static boolean _loadingStartGame=false;
	private static boolean _loadingAge=false;
	private static boolean _loadingGeneralMenu =false;
	private static boolean _loadingProfile=false;
	private static boolean _loadingCentralGameScreen=false;
	public static boolean createProcess=false;
	public static ScreenType typScreen;
	private boolean notSelectScreen=true;

	private static Safe safeinstance=null;

	// SOUNDMUSICMATIONS SOUNDMUSIC BANK

	public SoundMusicMationsCharger smC;
	public SoundMusicMation smm;


	public Safe(boolean _isAndroid){
		Safe._isAndroid = _isAndroid;
		safeinstance=this;

	}

	public static boolean is_loadingCentralGameScreen() {
		return _loadingCentralGameScreen;
	}

	public static void set_loadingCentralGameScreen(boolean _loadingCentralGameScreen) {
        Safe._loadingCentralGameScreen = _loadingCentralGameScreen;
    }


    public void setSmC(SoundMusicMationsCharger smC) {
		this.smC = smC;
	}

	public void setSmm(SoundMusicMation smm) {
		this.smm = smm;
	}

	public static enum ScreenType{

		StartGameScren,
		AgeScreen,
		MainGame,
		ProfileScreen,
		GeneralMenuScreen,
		CentralGameScreen

	}

    public void createMainGameScreen(){
		_mainGameScreen=new MainGameScreen(this);

	}

	public void disposeAndCreateCentralGameScreen(){
		createProcess=true;
		_centralGameScreen.dispose();
		_centralGameScreen=new CentralGameScreen(this);
	}

	public void disposeAndCreateGeneralMenuScreen(){
		createProcess=true;
		_generalMenuScreen.dispose();
		_generalMenuScreen=new GeneralMenuScreen(this);
	}



	public static Safe getInstance(){
		return safeinstance;

	}

	public void configMainGameScreen(){
		if(_mainGameScreen!=null){
			_mainGameScreen.config();
		}
	}

	public ProfileScren get_ProfileScreen() {

		return _profileScren;
	}

	public static MainGameScreen get_mainGameScreen() {
		return _mainGameScreen;
	}

	public static void set_mainGameScreen(MainGameScreen _mainGameScreen) {
		Safe._mainGameScreen = _mainGameScreen;
	}

	public static StartGameScreen get_startGameScreen() {
		return _startGameScreen;
	}

	public static void set_startGameScreen(StartGameScreen _startGameScreen) {
		Safe._startGameScreen = _startGameScreen;
	}

	public static AgeScreen get_ageScreen() {
		return _ageScreen;
	}

	public static void set_ageScreen(AgeScreen _ageScreen) {
		Safe._ageScreen = _ageScreen;
	}

	public static GeneralMenuScreen get_generalMenuScreen() {
		return _generalMenuScreen;
	}

	public static void set_generalMenuScreen(GeneralMenuScreen _generalMenuScreen) {
		Safe._generalMenuScreen = _generalMenuScreen;
	}

	public static ProfileScren get_profileScren() {
		return _profileScren;
	}

	public static void set_profileScren(ProfileScren _profileScren) {
		Safe._profileScren = _profileScren;
	}

	public static boolean is_isAndroid() {
		return _isAndroid;
	}

	public static void set_isAndroid(boolean _isAndroid) {
		Safe._isAndroid = _isAndroid;
	}

	public static boolean is_loadingMainGame() {
		return _loadingMainGame;
	}

	public static void set_loadingMainGame(boolean _loadingMainGame) {
		Safe._loadingMainGame = _loadingMainGame;
	}

	public static void set_centralGameScreen(boolean _loadingCentralGameScreen){
		Safe._loadingCentralGameScreen=_loadingCentralGameScreen;
	}

	public static boolean is_loadingStartGame() {
		return _loadingStartGame;
	}

	public static void set_loadingStartGame(boolean _loadingStartGame) {
		Safe._loadingStartGame = _loadingStartGame;
	}

	public static boolean is_loadingAge() {
		return _loadingAge;
	}

	public static void set_loadingAge(boolean _loadingAge) {
		Safe._loadingAge = _loadingAge;
	}

	public static boolean is_loadingGeneralMenu() {
		return _loadingGeneralMenu;
	}

	public static void set_loadingGeneralMenu(boolean _loadingGeneralMenu) {
		Safe._loadingGeneralMenu = _loadingGeneralMenu;
	}

	public static boolean is_loadingProfile() {
		return _loadingProfile;
	}

	public static void set_loadingProfile(boolean _loadingProfile) {
		Safe._loadingProfile = _loadingProfile;
	}

	public Screen getScreenType(ScreenType screenType){
		switch(screenType){

			case StartGameScren:
				return _startGameScreen;
			case MainGame:
				_mainGameScreen.initGameGraphMainGame();
				return _mainGameScreen;
			case GeneralMenuScreen:
				return _generalMenuScreen;
			case AgeScreen:
				return _ageScreen;
			case ProfileScreen:
				return _profileScren;
			case CentralGameScreen:
				return _centralGameScreen;


			default:
				return _startGameScreen;
		}




	}


	public boolean isAndroid(){ return Safe._isAndroid; }

	public static boolean is_loadedMainGame() {		return _loadingMainGame; 	}

	public static void set_loadedMainGame(boolean _loadingMainGame) { Safe._loadingMainGame = _loadingMainGame;	}

	@Override
	public void pause(){  // By Boris.INSPIRATGAMES

	}

	@Override
	public void create(){

        spriteBatch=new CpuSpriteBatch();


		_startGameScreen = new StartGameScreen(this);
		_ageScreen = new AgeScreen(this);
		_generalMenuScreen = new GeneralMenuScreen(this);
		_profileScren = new ProfileScren(this);
        _centralGameScreen = new CentralGameScreen(this);
		setScreen(_startGameScreen);

	}

	/*

	@Override
	public void render() {

		if (notSelectScreen){
			if(is_loadingStartGame() && is_loadingProfile() && is_loadingAge() && is_loadingGeneralMenu()) {
					setScreen(_startGameScreen);
				    notSelectScreen=false;
			}
		}else {
			super.render();
		}

	}


	*/


	@Override
	public void dispose(){

		_startGameScreen.dispose();

		_ageScreen.dispose();

		_generalMenuScreen.dispose();
		_mainGameScreen.getG().m.ggMgr.closeFiles();
		_mainGameScreen.dispose();

		_profileScren.dispose();
	}

}
