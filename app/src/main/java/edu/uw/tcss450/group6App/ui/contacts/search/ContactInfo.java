package edu.uw.tcss450.group6App.ui.contacts.search;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class ContactInfo implements Serializable {

    private final String mFName;
    private final String mLName;
    private final String mDisplayName;

    public ContactInfo(String fName, String lName, String displayName){
        mFName = fName;
        mLName = lName;
        mDisplayName = displayName;
    }

    public String getFName() {
        return mFName;
    }

    public String getLName() {
        return mLName;
    }

    public String getUsername() {
        return mDisplayName;
    }

    /**
     * Provides equality solely based on username.
     * @param other the other object to check for equality
     * @return true if other username matches this username, false otherwise
     */
    @Override
    public boolean equals(@Nullable Object other) {
        boolean result = false;
        if (other instanceof ContactInfo) {
            result = mDisplayName.equals(((ContactInfo) other).mDisplayName);
        }
        return result;
    }

}
