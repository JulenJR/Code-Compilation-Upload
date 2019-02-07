package com.mygdx.safe.profile;

import com.badlogic.gdx.utils.Array;

public class ProfileSubject {

    //TAG
    private static final String TAG = ProfileSubject.class.getSimpleName();

    //PROFILE OBSERVER
    private Array<ProfileObserver> _observers;

    /*_______________________________________________________________________________________________________________*/

    //CONSTRUCTOR
    public ProfileSubject(){
        _observers = new Array<ProfileObserver>();
    }

    /*_______________________________________________________________________________________________________________*/

    //ADD OBSERVER
    public void addObserver(ProfileObserver profileObserver){
        _observers.add(profileObserver);
    }

    //REMOVE OBSERVER
    public void removeObserver(ProfileObserver profileObserver){
        _observers.removeValue(profileObserver, true);
    }

    //NOTIFY
    protected void notify(final ProfileManager profileManager, ProfileObserver.ProfileEvent event){
        for(ProfileObserver observer: _observers){
            observer.onNotify(profileManager, event);
        }
    }

    /*_______________________________________________________________________________________________________________*/

    //GETTERS

    public Array<ProfileObserver> get_observers() {
        return _observers;
    }

    //SETTERS

    public void set_observers(Array<ProfileObserver> _observers) {
        this._observers = _observers;
    }

}
