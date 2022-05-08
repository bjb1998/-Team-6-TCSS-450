package edu.uw.tcss450.group6App.ui.contacts.search;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.group6App.R;
import edu.uw.tcss450.group6App.databinding.FragmentChatBinding;
import edu.uw.tcss450.group6App.databinding.FragmentContactsBinding;
import edu.uw.tcss450.group6App.databinding.FragmentContactSearchBinding;
import edu.uw.tcss450.group6App.databinding.FragmentHomeBinding;
import edu.uw.tcss450.group6App.model.UserInfoViewModel;
import edu.uw.tcss450.group6App.ui.chat.ChatRecyclerViewAdapter;
import edu.uw.tcss450.group6App.ui.contacts.ContactsFragmentDirections;

public class ContactsSearchFragment extends Fragment {

    private FragmentContactSearchBinding binding;
    private ContactsSearchViewModel mContactsSearchViewModel;

    public ContactsSearchFragment(){
        //Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContactsSearchViewModel = new ViewModelProvider(getActivity())
                .get(ContactsSearchViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentContactSearchBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserInfoViewModel model = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);

        String currentEmail = model.getEmail();
        mContactsSearchViewModel.setCurrentEmail(currentEmail);

        binding.buttonSearch.setOnClickListener(this::search);
        mContactsSearchViewModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);

        binding.recyclerContacts.setAdapter(
                new ContactsRecyclerViewAdapter(
                        mContactsSearchViewModel.convertToList(new JSONObject())
                )
        );
    }

    private void search(@NonNull View view) {
        mContactsSearchViewModel.searchUsers(binding.editSearch.getText().toString(),
                binding.spinnerOptions.getSelectedItem().toString());
    }

    private void observeResponse(final JSONObject response) {
        if (response.length() <= 0) {
                    binding.editSearch.setError(
                            "No results: ");
        } else {
            Log.d("HERES THE SHIT!!!!!", response.toString());
            binding.recyclerContacts.setAdapter(
                    new ContactsRecyclerViewAdapter(
                            mContactsSearchViewModel.convertToList(response)
                    )
            );
        }
    }
}