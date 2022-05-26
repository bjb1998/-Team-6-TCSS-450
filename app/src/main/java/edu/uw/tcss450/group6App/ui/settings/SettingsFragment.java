
package edu.uw.tcss450.group6App.ui.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.badge.BadgeUtils;

import edu.uw.tcss450.group6App.R;
import edu.uw.tcss450.group6App.databinding.FragmentSettingsBinding;
import edu.uw.tcss450.group6App.model.PushyTokenViewModel;
import edu.uw.tcss450.group6App.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private UserInfoViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        viewModel = provider.get(UserInfoViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSettingsThemes.setOnClickListener(button ->
                Navigation.findNavController(getView()).navigate(
                        SettingsFragmentDirections.actionNavigationSettingsToTheme()
                ));
        binding.buttonLogoutSetting.setOnClickListener(this::onClickLogOut);

    }
    private void onClickLogOut(View v){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //Setting message manually and performing action on button click
        builder.setMessage("Are you sure you want to log out?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences prefs =
                                getActivity().getSharedPreferences(
                                        getString(R.string.keys_shared_prefs),
                                        Context.MODE_PRIVATE);
                        prefs.edit().remove(getString(R.string.keys_prefs_jwt)).apply();
                        //End the app completely
                        getActivity().finishAndRemoveTask();

                        PushyTokenViewModel model = new ViewModelProvider(getActivity())
                                .get(PushyTokenViewModel.class);
                        //when we hear back from the web service quit
                        model.addResponseObserver(getActivity(), result -> getActivity().finishAndRemoveTask());
                        model.deleteTokenFromWebservice(
                                new ViewModelProvider(getActivity())
                                        .get(UserInfoViewModel.class)
                                        .getJwt()
                        );

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'Cancel' Button
                        dialog.cancel();
                    }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        alert.setTitle("Are You Sure ?");
        alert.show();
    }
}