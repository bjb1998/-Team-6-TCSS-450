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

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.group6App.R;
import edu.uw.tcss450.group6App.databinding.FragmentChatBinding;
import edu.uw.tcss450.group6App.databinding.FragmentContactsBinding;
import edu.uw.tcss450.group6App.databinding.ContactsSearchFragmentBinding;
import edu.uw.tcss450.group6App.ui.chat.ChatRecyclerViewAdapter;
import edu.uw.tcss450.group6App.ui.contacts.ContactsFragmentDirections;

public class ContactsSearchFragment extends Fragment {

    private ContactsSearchFragmentBinding binding;
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
        binding = ContactsSearchFragmentBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //SetRefreshing shows the internal Swiper view progress bar. Show this until messages load
        //binding.swipeContainer.setRefreshing(true);

        binding.buttonSearch.setOnClickListener(this::search);
        mContactsSearchViewModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);

        //todo make this shit work
        //final RecyclerView rv = binding.recyclerMessages;
        //Set the Adapter to hold a reference to the list FOR THIS chat ID that the ViewModel
        //holds.

    }

    private void search(@NonNull View view) {
        mContactsSearchViewModel.searchUsers(binding.editSearch.getText().toString());
    }

    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    final String msg = response.getJSONObject("data").getString("message");
                    if(msg.equals("Email exists"))
                        binding.editSearch.setError(
                                "Error Authenticating: " +
                                        response.getJSONObject("data").getString("message"));
                    else if(msg.equals("Username exists")){
                        binding.editSearch.setError(
                                "Error Authenticating: " +
                                        response.getJSONObject("data").getString("message"));
                    }
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                Log.d("HERES THE SHIT!!!!!", response.toString());
            }
        } else {
            binding.editSearch.setError("No Users Found");
        }
    }


}