
package edu.uw.tcss450.group6App.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import edu.uw.tcss450.group6App.R;
import edu.uw.tcss450.group6App.databinding.FragmentSettingsBinding;
import edu.uw.tcss450.group6App.model.PushyTokenViewModel;
import edu.uw.tcss450.group6App.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSettingsThemes.setOnClickListener(button ->
                Navigation.findNavController(requireView()).navigate(
                        SettingsFragmentDirections.actionNavigationSettingsToTheme()
                ));
        binding.buttonLogoutSetting.setOnClickListener(this::onClickLogOut);

    }
    private void onClickLogOut(View v){

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        //Setting message manually and performing action on button click
        builder.setMessage("Are you sure you want to log out?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    SharedPreferences prefs =
                            requireActivity().getSharedPreferences(
                                    getString(R.string.keys_shared_prefs),
                                    Context.MODE_PRIVATE);
                    prefs.edit().remove(getString(R.string.keys_prefs_jwt)).apply();
                    //End the app completely
                    requireActivity().finishAndRemoveTask();

                    PushyTokenViewModel model = new ViewModelProvider(requireActivity())
                            .get(PushyTokenViewModel.class);
                    //when we hear back from the web service quit
                    model.addResponseObserver(requireActivity(), result -> requireActivity().finishAndRemoveTask());
                    model.deleteTokenFromWebservice(
                            new ViewModelProvider(requireActivity())
                                    .get(UserInfoViewModel.class)
                                    .getJwt()
                    );

                })
                .setNegativeButton("Cancel", (dialog, id) -> {
                    //  Action for 'Cancel' Button
                    dialog.cancel();
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("LOG OUT");
        alert.show();
    }
}