package edu.uw.tcss450.group6App.ui.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import edu.uw.tcss450.group6App.R;
import edu.uw.tcss450.group6App.databinding.FragmentThemesBinding;
import edu.uw.tcss450.group6App.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.

 */
public class ThemeFragment extends Fragment {

    private FragmentThemesBinding binding;
    private UserInfoViewModel viewModel;
    private RadioGroup radioGroup;
    private TextView themeTV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentThemesBinding.inflate(inflater);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        viewModel = provider.get(UserInfoViewModel.class);

        radioGroup = binding.getRoot().findViewById(R.id.idRGgroup);
        themeTV = binding.getRoot().findViewById(R.id.idtvTheme);

        SharedPreferences pref = getActivity().getSharedPreferences("Theme", Context.MODE_PRIVATE);
        boolean isNightMode = pref.getBoolean("isNight", true);

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                // on radio button check change
                switch (checkedId) {
                    case R.id.button_theme_light:
                        // on below line we are checking the radio button with id.
                        // on below line we are setting the text to text view as light mode.
                        themeTV.setText("Light Theme");
                        if(!isNightMode)
                            Toast.makeText(getActivity(), "Already in Light Mode!", Toast.LENGTH_SHORT).show();
                        // on below line we are changing the theme to light mode.
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        group.clearCheck();
                        pref.edit().putBoolean("isNight", false).apply();
                        break;
                    case R.id.button_theme_dark:
                        // this method is called when dark radio button is selected
                        // on below line we are setting dark theme text to our text view.
                        themeTV.setText("Dark Theme");
                        if(isNightMode)
                            Toast.makeText(getActivity(), "Already in Dark Mode!", Toast.LENGTH_SHORT).show();
                        // on below line we are changing the theme to dark mode.
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        group.clearCheck();
                        pref.edit().putBoolean("isNight", true).apply();
                        break;
                }

            });
        return binding.getRoot();
            }


}