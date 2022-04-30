package edu.uw.tcss450.group6App.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import edu.uw.tcss450.group6App.R;
import edu.uw.tcss450.group6App.databinding.FragmentThirdHomeBinding;
import edu.uw.tcss450.group6App.model.UserInfoViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ThirdHomeFragment extends Fragment {

    public ThirdHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_third_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Local access to the ViewBinding object. No need to create as Instance Var as it is only
        //used here.
        FragmentThirdHomeBinding binding = FragmentThirdHomeBinding.bind(getView());

        //Note argument sent to the ViewModelProvider constructor. It is the Activity that
        //holds this fragment.
        UserInfoViewModel model = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);
        binding.textEmail.setText("Welcome to your third home " + model.getEmail() + "!");


        //binding.textEmail.setText("Welcome to your third home!"); // og line
    }
}
