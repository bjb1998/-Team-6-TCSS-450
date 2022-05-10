package edu.uw.tcss450.group6App.ui.contacts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.group6App.databinding.FragmentContactsBinding;
import edu.uw.tcss450.group6App.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ContactsFragment extends Fragment {

    private FragmentContactsBinding binding;
    private ContactsViewModel mContactsViewModel;

    public ContactsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContactsViewModel = new ViewModelProvider(getActivity())
                .get(ContactsViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentContactsBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserInfoViewModel model = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);

        mContactsViewModel.setCurrentEmail(model.getEmail());

        binding.buttonNewContact.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        ContactsFragmentDirections.actionNavigationContactsToNavigationContactsSearch()
                ));

        binding.recyclerContactsPending.setAdapter(
                new ContactsPendingRecyclerViewAdapter(
                        mContactsViewModel.convertToList(new JSONObject())
                )
         );

        binding.recyclerContactsVerified.setAdapter(
                new ContactsVerifiedRecyclerViewAdapter(
                        mContactsViewModel.convertToList(new JSONObject())
                )
        );

        mContactsViewModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);

        mContactsViewModel.getContacts(0);
        mContactsViewModel.getContacts(1);

    }

    private void observeResponse(final JSONObject response) {
        if (response.length() <= 0) {
            //todo have something here show up in the app to show no results
            Log.d("NO RESULTS", "No results");
        } else {

            try {
                int context = response.getInt("context");
                if(context == 1)
                    binding.recyclerContactsVerified.setAdapter(
                            new ContactsVerifiedRecyclerViewAdapter(
                                    mContactsViewModel.convertToList(response)
                            )
                    );
                else
                    binding.recyclerContactsPending.setAdapter(
                            new ContactsPendingRecyclerViewAdapter(
                                    mContactsViewModel.convertToList(response)
                            )
                    );
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}