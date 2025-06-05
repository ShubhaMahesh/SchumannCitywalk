package com.example.schumannechoes.fragments;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.schumannechoes.LanguageActivity;
import com.example.schumannechoes.R;
import com.example.schumannechoes.RewardsActivity;
import com.example.schumannechoes.SharedViewModel;
import com.example.schumannechoes.Sight;
import com.example.schumannechoes.SightsAdapter;
import android.location.Location;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import android.content.pm.PackageManager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView weatherText;
    private FusedLocationProviderClient fusedLocationClient;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        SharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        List<Sight> sights = viewModel.getSights(requireContext());
        Collections.shuffle(sights);
        List<Sight> limitedSights = sights.subList(0, Math.min(sights.size(), 6));

        TextView recommendationsTitle = view.findViewById(R.id.recommendationsTitle);
        TextView settingsTextView = view.findViewById(R.id.settingsButton).findViewById(R.id.settingsText);
        TextView rewardsTextView = view.findViewById(R.id.accountButton).findViewById(R.id.rewardsText);

        weatherText = view.findViewById(R.id.weatherInfo);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        fetchWeatherForCurrentLocation();

        recommendationsTitle.setText(getString(R.string.recommendationsTitle));
        settingsTextView.setText(getString(R.string.settings));
        rewardsTextView.setText(getString(R.string.rewards));
        LinearLayout settingsButton = view.findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToSettingsFragment();
            }
        });

        // Get the accountButton layout
        LinearLayout accountButton = view.findViewById(R.id.accountButton);
        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RewardsActivity.class);
                startActivity(intent);
            }
        });

        ImageButton notificationButton = view.findViewById(R.id.notificationButton);
        notificationButton.setOnClickListener(v -> {
            NotificationFragment notificationFragment = new NotificationFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, notificationFragment)
                    .addToBackStack(null)
                    .commit();
        });
        LottieAnimationView notificationAnim = view.findViewById(R.id.notificationAnim);
        notificationAnim.setOnClickListener(v -> {
            NotificationFragment notificationFragment = new NotificationFragment();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, notificationFragment)
                    .addToBackStack(null)
                    .commit();
        });

        recyclerView = view.findViewById(R.id.recommendationsRecyclerView);
        recyclerView.setVerticalScrollBarEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        SightsAdapter adapter = new SightsAdapter(limitedSights);


        recyclerView.setAdapter(adapter);
        return view;
    }

    private void fetchWeatherForCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            weatherText.setText("Location permission not granted");
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location != null) {
                fetchWeather(location.getLatitude(), location.getLongitude());
            } else {
                weatherText.setText("Location unavailable");
            }
        });
    }


    private void fetchWeather(double lat, double lon) {
        new Thread(() -> {
            try {
                String apiKey = "b38eeb0b07e93abc4ed38cbddb4ac839";
                String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&units=metric&appid=" + apiKey;
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).build();
                Response response = client.newCall(request).execute();
                String json = response.body().string();
                JSONObject obj = new JSONObject(json);
                double temp = obj.getJSONObject("main").getDouble("temp");
                requireActivity().runOnUiThread(() -> weatherText.setText( temp + "°C in Zwickau"));
            } catch (Exception e) {
                requireActivity().runOnUiThread(() -> weatherText.setText("Weather load failed"));
            }
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = requireContext().getSharedPreferences("notifications_prefs", Context.MODE_PRIVATE);
        boolean hasNew = prefs.getBoolean("has_new_notification", false);
        ImageButton notificationButton = getView().findViewById(R.id.notificationButton);
        LottieAnimationView notificationAnim = getView().findViewById(R.id.notificationAnim);
        if (hasNew) {
            notificationButton.setVisibility(View.GONE);
            notificationAnim.setAnimation("notification.json");
            notificationAnim.setVisibility(View.VISIBLE);
            notificationAnim.playAnimation();
        } else {
            notificationAnim.setVisibility(View.GONE);
            notificationButton.setVisibility(View.VISIBLE);
            notificationButton.setImageResource(R.drawable.ic_notifications);
        }
    }


    private void navigateToSettingsFragment() {
        SettingsFragment settingsFragment = new SettingsFragment();

        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, settingsFragment)
                .addToBackStack(null)
                .commit();
    }


}