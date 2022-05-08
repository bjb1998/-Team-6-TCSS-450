package edu.uw.tcss450.group6App.ui.contacts.search;

import androidx.annotation.Nullable;
import java.io.Serializable;

public class ContactInfo implements Serializable {

    private final String mFName;
    private final String mLName;
    private final String mDisplayName;
    private final String mEmail;

    public ContactInfo(String fName, String lName, String displayName, String Email){
        mFName = fName;
        mLName = lName;
        mDisplayName = displayName;
        mEmail = Email;
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

    public String getEmail() {
        return mEmail;
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
