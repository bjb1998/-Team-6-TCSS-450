package edu.uw.tcss450.group6App.ui.weather;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.group6App.R;
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

    /**
     * Used to determine what method to call.
     */
    private int buttonChoice = 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Stub constructor
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
        mWeatherModel = new ViewModelProvider(requireActivity())
                .get(WeatherViewModel.class);
        mLocationModel = new ViewModelProvider(requireActivity())
                .get(LocationViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_weather, container, false);
        binding = FragmentWeatherBinding.inflate(inflater);
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //FragmentLocationBinding binding = FragmentLocationBinding.bind(getView());

        // TODO: lines below displayed the lat and long of location from lab 6
        //mLocationModel.addLocationObserver(getViewLifecycleOwner(), location ->
                //binding.textLatLong.setText(location.toString()));

        mWeatherModel.setLocationModel(mLocationModel);

        binding.todayButton.setOnClickListener(button -> {
            clearText();
            buttonChoice = 0;
            mWeatherModel.connectGetDaily();
        });

        // pressing a button code
        mWeatherModel.addResponseObserver(getViewLifecycleOwner(), result ->
        {
            switch (buttonChoice) {
                case 0:
                    loadTodayInfo(result);
                    break;
                case 1:
                    loadTenDayInfo(result);
                    break;
            }
        });

        mWeatherModel.connectGetDaily();
        //binding.todayButton.setOnClickListener(button -> mWeatherModel.connectGetDaily());

        // TODO 10 day forecast
        binding.tenDaysButton.setOnClickListener(button -> {
            clearText();
            buttonChoice = 1;
            mWeatherModel.connectGet10Day();
        });
    }

    /**
     * Clears all the TextViews
     */
    private void clearText() {
        binding.time.setText("");
        binding.sunrise.setText("");
        binding.sunset.setText("");
        binding.temperature.setText("");
        //binding.location.setText("");
        binding.description.setText("");

    }

    /**
     * Loads the daily weather info into the text views
     */
    private void loadTodayInfo(final JSONObject result) {
        try {
            final JSONArray arr = result.getJSONArray("data");
            final JSONObject obj = arr.getJSONObject(0);

            // time
            final StringBuilder time = new StringBuilder();
            time.append(obj.getString("ob_time"));

            // sunrise
            final StringBuilder sunrise = new StringBuilder();
            sunrise.append("Sunrise: " + obj.getString("sunrise"));

            // sunset
            final StringBuilder sunset = new StringBuilder();
            sunset.append("Sunset: " + obj.getString("sunset"));

            // location
            final StringBuilder local = new StringBuilder();
            local.append(obj.getString("city_name") + ", " + obj.getString("state_code"));

            // weather
            final JSONObject w = obj.getJSONObject("weather");
            final StringBuilder weather = new StringBuilder();
            weather.append(w.getString("description"));

            // icon TODO: implement dynamic set of icon


            // temp
            final StringBuilder temp = new StringBuilder();
            double t = obj.getDouble("temp");
            t = t * 1.8 + 32;
            final String degree = "" + (char) 176;
            temp.append("Temp: " + String.format("%.2f", t) + degree +" F");

            binding.time.setText(time.toString());
            binding.sunrise.setText(sunrise.toString());
            binding.sunset.setText(sunset.toString());
            binding.temperature.setText(temp.toString());
            binding.location.setText(local.toString());
            binding.description.setText(weather.toString());
            binding.weatherIcon.setImageResource(R.mipmap.ic_clear_sky);
        } catch (final JSONException e) {
            e.printStackTrace();
        }

    }

    private void loadTenDayInfo(final JSONObject result) {
        try {
            final JSONArray arr = result.getJSONArray("data");
            final JSONObject obj = arr.getJSONObject(0);
            final JSONObject obj2 = arr.getJSONObject(1);
            Log.d("THE WEATHER 1", obj.toString());
            Log.d("THE WEATHER 2", obj2.toString());
        } catch (final JSONException e) {
            e.printStackTrace();
        }
    }


}