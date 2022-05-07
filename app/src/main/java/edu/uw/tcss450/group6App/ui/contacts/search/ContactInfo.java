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

    /**
     * Static factory method to turn a properly formatted JSON String into a
     * ContactInfo object.
     * @param cmAsJson the String to be parsed into a ContactInfo Object.
     * @return a ContactInfo Object with the details contained in the JSON String.
     * @throws JSONException when cmAsString cannot be parsed into a ContactInfo.
     */
    public static ContactInfo createFromJsonString(final String cmAsJson) throws JSONException {
        final JSONObject msg = new JSONObject(cmAsJson);
        return new ContactInfo(
                msg.getString("firstname"),
                msg.getString("lastname"),
                msg.getString("username"));
    }

    public String getFName() {
        return mFName;
    }

    public String getLName() {
        return mLName;
    }

    public String getDisplayName() {
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
