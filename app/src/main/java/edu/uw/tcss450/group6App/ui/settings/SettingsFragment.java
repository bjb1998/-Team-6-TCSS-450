package edu.uw.tcss450.group6App.ui.settings;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import edu.uw.tcss450.group6App.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.

 */
public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        TextView textView = new TextView(getActivity());
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        Button button_theme = (Button) v.findViewById(R.id.button_theme);
        button_theme.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                ThemeFragment themeFragment = new ThemeFragment();
                FragmentManager manager = getFragmentManager();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, themeFragment, "theme").addToBackStack(null).commit();
//                manager.beginTransaction().replace(R.id.container, ).commit();
            }
        });
    return v;

    }


}