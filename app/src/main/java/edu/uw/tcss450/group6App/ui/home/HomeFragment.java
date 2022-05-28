package edu.uw.tcss450.group6App.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.group6App.R;
import edu.uw.tcss450.group6App.databinding.FragmentHomeBinding;
import edu.uw.tcss450.group6App.databinding.FragmentWeatherBinding;
import edu.uw.tcss450.group6App.model.LocationViewModel;
import edu.uw.tcss450.group6App.model.UserInfoViewModel;
import edu.uw.tcss450.group6App.model.WeatherViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private WeatherViewModel mWeatherModel;
    private LocationViewModel mLocationModel;

    private FragmentHomeBinding binding;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mWeatherModel = new ViewModelProvider(requireActivity())
                .get(WeatherViewModel.class);
        mLocationModel = new ViewModelProvider(requireActivity())
                .get(LocationViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        UserInfoViewModel model = new ViewModelProvider(getActivity())
                .get(UserInfoViewModel.class);

        FragmentHomeBinding.bind(getView()).textHello.setText("Hello " + model.getEmail());

        mWeatherModel.setLocationModel(mLocationModel);

        setWeather();
        mWeatherModel.connectGetDaily();


    }

    @SuppressLint("SetTextI18n")
    private void setWeather(){
        mWeatherModel.addResponseObserver(getViewLifecycleOwner(), result ->
        {
            // TODO this is how you access the json object data; now split and add to weather display
            try {
                final JSONArray arr = result.getJSONArray("data");
                final JSONObject obj = arr.getJSONObject(0);
                final JSONObject w = obj.getJSONObject("weather");

                // icon TODO: implement
                //final Bitmap bImage = BitmapFactory.decodeResource(.getLifecycle(), R.mipmap.ic_launcher_round);
                binding.weatherIconHome.setImageResource(R.mipmap.ic_clear_sky);
                //binding.weatherIcon.setImageBitmap(bImage);
                //binding.weatherIcon.setVisibility(View.VISIBLE);

                // temp
                final StringBuilder temp = new StringBuilder();
                double t = obj.getDouble("temp");
                t = t * 1.8 + 32;
                final String degree = "" + (char) 176;
                temp.append("Temp: " + String.format("%.2f", t) + degree +" F");

                binding.timeHome.setText(obj.getString("ob_time"));
                binding.sunriseHome.setText("Sunrise: " + obj.getString("sunrise"));
                binding.sunsetHome.setText("Sunset: " + obj.getString("sunset"));
                binding.temperatureHome.setText(temp.toString());
                binding.locationHome.setText(obj.getString("city_name") + ", " + obj.getString("state_code"));
                binding.descriptionHome.setText(w.getString("description"));
            } catch (final JSONException e) {
                e.printStackTrace();
            }
        });
    }
}
