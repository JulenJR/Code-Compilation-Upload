package com.mygdx.safe.profile;

public interface ProfileObserver {

    enum ProfileEvent{
        PROFILE_LOADED,
        SAVING_PROFILE
    }

    /*_______________________________________________________________________________________________________________*/

    void onNotify(final ProfileManager profileManager, ProfileEvent event);
}
