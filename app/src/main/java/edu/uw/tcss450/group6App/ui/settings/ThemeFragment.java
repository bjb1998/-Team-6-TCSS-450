package edu.uw.tcss450.group6App.ui.settings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import edu.uw.tcss450.group6App.R;

/**
 * A simple {@link Fragment} subclass.

 */
public class ThemeFragment extends Fragment {

    Button button_red_theme, button_dark_theme;
    private RadioGroup radioGroup;
    private TextView themeTV;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v=  inflater.inflate(R.layout.fragment_themes, container, false);

        radioGroup = v.findViewById(R.id.idRGgroup);
        themeTV = v.findViewById(R.id.idtvTheme);
//         button_red_theme = (Button) v.findViewById(R.id.idRBLight);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override

                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    // on radio button check change
                    switch (checkedId) {
                        case R.id.idRBLight:
                            // on below line we are checking the radio button with id.
                            // on below line we are setting the text to text view as light mode.
                            themeTV.setText("Light Theme");
                            // on below line we are changing the theme to light mode.
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                            break;
                        case R.id.idRBDark:
                            // this method is called when dark radio button is selected
                            // on below line we are setting dark theme text to our text view.
                            themeTV.setText("Dark Theme");
                            // on below line we are changing the theme to dark mode.
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                            break;
                    }

                }
            });
        return v;
            }


}