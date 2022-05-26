package edu.uw.tcss450.group6App.ui.settings;

import android.annotation.SuppressLint;
import android.content.Intent;
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
//         button_red_theme = (Button) v.findViewById(R.id.idRBLight);


        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                Log.d("THEME", checkedId + "");
                // on radio button check change
                switch (checkedId) {
                    case R.id.button_theme_light:
                        // on below line we are checking the radio button with id.
                        // on below line we are setting the text to text view as light mode.
                        themeTV.setText("Light Theme");
                        // on below line we are changing the theme to light mode.
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        group.clearCheck();
                        Log.d("THEME", "Hello Light!");
                        break;
                    case R.id.button_theme_dark:
                        // this method is called when dark radio button is selected
                        // on below line we are setting dark theme text to our text view.
                        themeTV.setText("Dark Theme");
                        // on below line we are changing the theme to dark mode.
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        group.clearCheck();
                        Log.d("THEME", "Hello Dark!");
                        break;
                }

            });
        return binding.getRoot();
            }


}