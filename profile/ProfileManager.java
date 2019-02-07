package com.mygdx.safe.profile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.ObjectMap;
import com.mygdx.safe.InputProcessors.GenericMethodsInputProcessor;

import java.util.Enumeration;
import java.util.Hashtable;

public class ProfileManager extends ProfileSubject {

    //TAG
    private static final String TAG = ProfileManager.class.getSimpleName();

    //ASPECTS
    private GenericMethodsInputProcessor g;

    //STATIC
    private static ProfileManager _profileManager;
    private static final String SAVEGAME_SUFFIX = ".sav";
    private static final String DEFAULT_PROFILE = "default";

    //JSON
    private Json _json;

    //PROFILE
    private String _profileName;
    private Hashtable<String,FileHandle> _profiles = null;
    private ObjectMap<String, Object> _profileProperties = new ObjectMap<String, Object>();

    /*_______________________________________________________________________________________________________________*/

    //STATIC
    public static final ProfileManager getInstance(){
        if( _profileManager == null){
            _profileManager = new ProfileManager();
        }
        return _profileManager;
    }

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    private ProfileManager(){
        _json = new Json();
        _profiles = new Hashtable<String,FileHandle>();
        _profiles.clear();
        _profileName = DEFAULT_PROFILE;
        storeAllProfiles();
    }

    /*_______________________________________________________________________________________________________________*/

    //SAVE PROFILE
    public void saveProfile(){
        notify(this, ProfileObserver.ProfileEvent.SAVING_PROFILE);
        String text = _json.prettyPrint(_json.toJson(_profileProperties));
        writeProfileToStorage(_profileName, text, true);
    }

    //LOAD PROFILE
    public void loadProfile(){
        String fullProfileFileName = _profileName+SAVEGAME_SUFFIX;
        boolean doesProfileFileExist = Gdx.files.internal(fullProfileFileName).exists();

        if( !doesProfileFileExist ){
            System.out.println("File doesn't exist!");
            return;
        }

        _profileProperties = _json.fromJson(ObjectMap.class, _profiles.get(_profileName));
        notify(this, ProfileObserver.ProfileEvent.PROFILE_LOADED);
    }

    //STORE ALL PROFILES
    public void storeAllProfiles(){
        if( Gdx.files.isLocalStorageAvailable() ){
            FileHandle[] files = Gdx.files.local(".").list(SAVEGAME_SUFFIX);

            for(FileHandle file: files) {
                _profiles.put(file.nameWithoutExtension(), file);
            }
        }else{
            //TODO: try external directory here
            return;
        }
    }

    //DOES PROFILE EXIST
    public boolean doesProfileExist(String profileName){
        return _profiles.containsKey(profileName);
    }

    //WRITE PROFILE TO STORAGE
    public void writeProfileToStorage(String profileName, String fileData, boolean overwrite){
        String fullFilename = profileName+SAVEGAME_SUFFIX;

        boolean localFileExists = Gdx.files.internal(fullFilename).exists();

        //If we cannot overwrite and the file exists, exit
        if( localFileExists && !overwrite ){
            return;
        }

        FileHandle file =  null;

        if( Gdx.files.isLocalStorageAvailable() ) {
            file = Gdx.files.local(fullFilename);
            file.writeString(fileData, !overwrite);
        }
        _profiles.put(profileName, file);
    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS
    public static ProfileManager get_profileManager() {
        return _profileManager;
    }

    public static String getSavegameSuffix() {
        return SAVEGAME_SUFFIX;
    }

    public static String getDefaultProfile() {
        return DEFAULT_PROFILE;
    }

    public Array<String> getProfileList(){
        Array<String> profiles = new Array<String>();
        for (Enumeration<String> e = _profiles.keys(); e.hasMoreElements();){
            profiles.add(e.nextElement());
        }
        return profiles;
    }

    public FileHandle getProfileFile(String profile){
        if( !doesProfileExist(profile) ){
            return null;
        }
        return _profiles.get(profile);
    }

    public <T extends Object> T getProperty(String key, Class<T> type){
        T property = null;
        if( !_profileProperties.containsKey(key) ){
            return property;
        }
        property = (T)_profileProperties.get(key);
        return property;
    }

    public Json get_json() {
        return _json;
    }

    public String get_profileName() {
        return _profileName;
    }

    public Hashtable<String, FileHandle> get_profiles() {
        return _profiles;
    }

    public ObjectMap<String, Object> get_profileProperties() {
        return _profileProperties;
    }

    //SETTERS

    public void setProperty(String key, Object object){
        _profileProperties.put(key, object);
    }

    public void setCurrentProfile(String profileName){
        if( doesProfileExist(profileName) ){
            _profileName = profileName;
        }else{
            _profileName = DEFAULT_PROFILE;
        }
    }

    public static void set_profileManager(ProfileManager _profileManager) {
        ProfileManager._profileManager = _profileManager;
    }

    public void set_json(Json _json) {
        this._json = _json;
    }

    public void set_profileName(String _profileName) {
        this._profileName = _profileName;
    }

    public void set_profiles(Hashtable<String, FileHandle> _profiles) {
        this._profiles = _profiles;
    }

    public void set_profileProperties(ObjectMap<String, Object> _profileProperties) {
        this._profileProperties = _profileProperties;
    }
}
