package edu.uw.tcss450.group6App.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import static edu.uw.tcss450.group6App.model.NewMessageCountViewModel.chatIdMap;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.uw.tcss450.group6App.R;
import edu.uw.tcss450.group6App.databinding.FragmentHomeBinding;
import edu.uw.tcss450.group6App.databinding.FragmentWeatherBinding;
import edu.uw.tcss450.group6App.model.LocationViewModel;
import edu.uw.tcss450.group6App.model.UserInfoViewModel;
import edu.uw.tcss450.group6App.model.WeatherViewModel;
import edu.uw.tcss450.group6App.ui.chat.chatMenu.ChatInfo;
import edu.uw.tcss450.group6App.ui.chat.chatMenu.ChatMenuRecyclerViewAdapater;
import edu.uw.tcss450.group6App.ui.chat.chatMenu.ChatMenuViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private WeatherViewModel mWeatherModel;
    private LocationViewModel mLocationModel;
    private ArrayList<String> notifcationList = new ArrayList<>();
    private FragmentHomeBinding binding;
    public static ChatMenuViewModel mChatMenuViewModel;
    private View mView;
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
        mChatMenuViewModel = new ViewModelProvider(getActivity())
                .get(ChatMenuViewModel.class);
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
        mView = view;
        FragmentHomeBinding.bind(getView()).textHello.setText("Hello " + model.getEmail());

        mWeatherModel.setLocationModel(mLocationModel);


        grabDate();
        binding.datehome.setText(grabDate());
        ((TextView)(getView().findViewById(R.id.datehome))).setText(grabDate());

        setWeather();
        mWeatherModel.connectGetDaily();

        mChatMenuViewModel.setCurrentEmail(model.getEmail());

        mChatMenuViewModel.setmStatus("");
        final Observer<String> errorObserver = newName -> {
            if(!newName.equals(""))
                Snackbar.make(view,
                        mChatMenuViewModel.getmStatus().getValue(),
                        Snackbar.LENGTH_SHORT).show();
        };

        mChatMenuViewModel.getmStatus().observe(getViewLifecycleOwner(), errorObserver);

        binding.recyclerChats.setAdapter(
                new ChatMenuRecyclerViewAdapater(
                        mChatMenuViewModel.convertToList(new JSONObject())
                )
        );

        mChatMenuViewModel.addResponseObserver(getViewLifecycleOwner(),
                this::observeResponse);

        mChatMenuViewModel.getChat();

    }

    private void observeResponse(final JSONObject response) {
        if (response.length() <= 0) {
            //todo have something here show up in the app to show no results
            Log.d("NO RESULTS", "No results");
        } else {
            binding.recyclerChats.setAdapter(
                    new ChatMenuRecyclerViewAdapater(
                            mChatMenuViewModel.convertToList(filter(response))
                    )
            );
        }
    }

    private JSONObject filter(JSONObject unfiltered){
        try {
            JSONArray messages = unfiltered.getJSONObject("message").getJSONArray("rows");
            for(int i = 0; i < messages.length(); i++) {
                JSONObject message = messages.getJSONObject(i);
                if(!chatIdMap.containsKey(message.getInt("chatid"))){
                    messages.remove(i);
                    i -= 1;
                }
            }
            return unfiltered;
            //inform observers of the change (setValue)
        }catch (JSONException e) {
            Log.e("JSON PARSE ERROR", "Found in Home page message filter");
            Log.e("JSON PARSE ERROR", "Error: " + e.getMessage());
        }
        return unfiltered;
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
    private String grabDate() {
        StringBuilder sb = new StringBuilder();
        System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter= new SimpleDateFormat("MM dd, yyyy");
        Date date = new Date(System.currentTimeMillis());

        if (formatter.format(date).charAt(1) == '5') sb.append("May");
        else sb.append("June");

        sb.append(formatter.format(date).substring(2, 11));
        return sb.toString();
    }
}
