package edu.uw.tcss450.group6App.ui.chat.chatMenu;

import androidx.annotation.Nullable;

import java.io.Serializable;

import edu.uw.tcss450.group6App.ui.contacts.search.ContactInfo;

public class ChatInfo implements Serializable {

    private final String mChatName;
    private final int mChatId;

    public ChatInfo(String ChatName, int chatId){
        mChatName = ChatName;
        mChatId = chatId;
    }

    public String getName() {
        return mChatName;
    }

    public int getChatId() {
        return mChatId;
    }

    /**
     * Provides equality solely based on username.
     * @param other the other object to check for equality
     * @return true if other username matches this username, false otherwise
     */
    @Override
    public boolean equals(@Nullable Object other) {
        boolean result = false;
        if (other instanceof ChatInfo) {
            result = mChatId == (((ChatInfo) other).mChatId);
        }
        return result;
    }
}
