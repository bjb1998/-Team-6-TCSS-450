package edu.uw.tcss450.group6App.model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.HashMap;
import java.util.Map;

import edu.uw.tcss450.group6App.ui.chat.ChatFragment;

/**
 * A message {@link ViewModel} subclass.
 * keeps tracks of the users unread messages
 */
public class NewMessageCountViewModel extends ViewModel {
    private MutableLiveData<Integer> mNewMessageCount;
    public static HashMap<Integer, Integer> chatIdMap;

    public NewMessageCountViewModel() {
        mNewMessageCount = new MutableLiveData<>();
        mNewMessageCount.setValue(0);
        chatIdMap = new HashMap<>();
    }

    /**
     * Create a new observer
     */
    public void addMessageCountObserver(@NonNull LifecycleOwner owner,
                                        @NonNull Observer<? super Integer> observer) {
        mNewMessageCount.observe(owner, observer);
    }

    /**
     * Add one to the message count
     */
    public void increment(int chatId) {
        mNewMessageCount.setValue(mNewMessageCount.getValue() + 1);
        addChatCount(chatId);
    }

    private void addChatCount(int chatId){
        chatIdMap.merge(chatId, 1, Integer::sum);
    }

    /**
     * reset the message count
     */
    public void decrease(int chatId) {
        Integer messagesSeen = chatIdMap.get(chatId);
        if(messagesSeen != null) {
            mNewMessageCount.setValue(mNewMessageCount.getValue() - messagesSeen);
            chatIdMap.remove(chatId);
        }
    }
}
