
package edu.uw.tcss450.group6App.ui.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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
import edu.uw.tcss450.group6App.databinding.FragmentMenuBinding;
import edu.uw.tcss450.group6App.model.PushyTokenViewModel;
import edu.uw.tcss450.group6App.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

    private FragmentMenuBinding binding;
    private UserInfoViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMenuBinding.inflate(inflater);
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
    @SuppressLint("ResourceType")
    private void onClickLogOut(View v){

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
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
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Log out");
        alert.show();
    }
}