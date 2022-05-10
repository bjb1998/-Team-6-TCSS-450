package edu.uw.tcss450.group6App.ui.chat.chatMenu;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.group6App.R;
import edu.uw.tcss450.group6App.databinding.FragmentChatMenuBinding;
import edu.uw.tcss450.group6App.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ChatMenuFragment extends Fragment {

    private FragmentChatMenuBinding binding;
    private ChatMenuViewModel mChatMenuViewModel;

    public ChatMenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChatMenuViewModel = new ViewModelProvider(getActivity())
                .get(ChatMenuViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChatMenuBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserInfoViewModel model = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);

        mChatMenuViewModel.setCurrentEmail(model.getEmail());

        binding.recyclerChats.setAdapter(
                new ChatMenuRecyclerViewAdapater(
                        mChatMenuViewModel.convertToList(new JSONObject())
                )
        );

        mChatMenuViewModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);

        mChatMenuViewModel.getChat();

    }

    private void observeResponse(final JSONObject response) {
        if (response.length() <= 0) {
            //todo have something here show up in the app to show no results
            Log.d("NO RESULTS", "No results");
        } else {
            binding.recyclerChats.setAdapter(
                    new ChatMenuRecyclerViewAdapater(
                            mChatMenuViewModel.convertToList(response)
                    )
            );
        }
    }
}