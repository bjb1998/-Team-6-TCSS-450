package edu.uw.tcss450.group6App.model;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import edu.uw.tcss450.group6App.ui.chat.ChatFragment;

/**
 * A message {@link ViewModel} subclass.
 * keeps tracks of the users unread messages
 */
public class NewMessageCountViewModel extends ViewModel {
    private MutableLiveData<Integer> mNewMessageCount;

    public NewMessageCountViewModel() {
        mNewMessageCount = new MutableLiveData<>();
        mNewMessageCount.setValue(0);
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
    public void increment() {
        mNewMessageCount.setValue(mNewMessageCount.getValue() + 1);
    }

    /**
     * reset the message count
     */
    public void reset() {
        mNewMessageCount.setValue(0);
    }
}
