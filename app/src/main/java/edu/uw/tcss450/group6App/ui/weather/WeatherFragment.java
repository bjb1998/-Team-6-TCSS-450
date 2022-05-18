package edu.uw.tcss450.group6App.ui.weather;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import edu.uw.tcss450.group6App.databinding.FragmentWeatherBinding;
import edu.uw.tcss450.group6App.model.LocationViewModel;
import edu.uw.tcss450.group6App.model.WeatherViewModel;

/**
 * Contains all weather related functionality.
 * @author Robert Beltran
 * A simple {@link Fragment} subclass.
 * Use the {@link WeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherFragment extends Fragment {


    private WeatherViewModel mWeatherModel;
    private LocationViewModel mLocationModel;

    private FragmentWeatherBinding binding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Stub empty constructor
     */
    public WeatherFragment() {}

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Weather.
     */
    public static WeatherFragment newInstance(String param1, String param2) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mWeatherModel = new ViewModelProvider(getActivity())
                .get(WeatherViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_weather, container, false);
        binding = FragmentWeatherBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // TODO: trying to get the location, but i need to use it for the weather
        // TODO: since the location is received here i need to pass it to weather model
        //FragmentLocationBinding binding = FragmentLocationBinding.bind(getView());
        mLocationModel = new ViewModelProvider(requireActivity())
                .get(LocationViewModel.class);
        // TODO: lines below displayed the lat and long of location from lab 6
        //mLocationModel.addLocationObserver(getViewLifecycleOwner(), location ->
                //binding.textLatLong.setText(location.toString()));

        // TODO location doesn't have to be displayed in app, remove code that does that
        mWeatherModel.setLocationModel(mLocationModel);

        // pressing a button code
        mWeatherModel.addResponseObserver(getViewLifecycleOwner(), result ->
        {
            // TODO this is how you access the json object data; now split and add to weather display
            try {
                final JSONArray arr = result.getJSONArray("data");
                final JSONObject obj = arr.getJSONObject(0);
                // location
                final StringBuilder local = new StringBuilder();
                local.append(obj.getString("city_name") + ", " + obj.getString("state_code"));

                // temp
                final StringBuilder temp = new StringBuilder();
                double t = obj.getDouble("temp");
                t = t * 1.8 + 32;
                final String degree = "" + (char) 176;
                temp.append("Temp: " + String.format("%.2f", t) + degree +" F");

                binding.textResponseOutput.setText(temp.toString());
                binding.textLatLong.setText(local.toString());
            } catch (final JSONException e) {
                e.printStackTrace();
            }
        });
        binding.todayButton.setOnClickListener(button -> mWeatherModel.connectGet());

    }


}