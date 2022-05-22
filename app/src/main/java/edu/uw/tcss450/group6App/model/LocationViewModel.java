package edu.uw.tcss450.group6App.model;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

/**
 * Model for the users locations used for the weather service or other location services.
 * @author Robert Beltran
 */
public class LocationViewModel extends ViewModel {

    private MutableLiveData<Location> mLocation;

    public LocationViewModel() {
        Location defaultLocation = new Location("");
        defaultLocation .setLatitude(47.253078d);
        defaultLocation .setLongitude(-122.441528d);

        mLocation = new MutableLiveData<Location>(defaultLocation);
    }

    public void addLocationObserver(@NonNull LifecycleOwner owner,
                                    @NonNull Observer<? super Location> observer) {
        mLocation.observe(owner, observer);
    }

    public void setLocation(final Location location) {
        if (!location.equals(mLocation.getValue())) {
            mLocation.setValue(location);
        }
    }

    public Location getCurrentLocation() {
        return new Location(mLocation.getValue());
    }
}
