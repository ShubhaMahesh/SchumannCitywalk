package com.example.schumannechoes.fragments;



import static android.content.Intent.getIntent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import com.example.schumannechoes.R;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SightInfoFragment extends Fragment {

    private ImageView sightImageView;
    private TextView sightTitleTextView, sightDescriptionTextView, weatherText;
    private Button viewMapButton;
    private ImageView favoriteButton;
    private MediaPlayer mediaPlayer;
    private FusedLocationProviderClient fusedLocationClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sight_info, container, false);

        // Initialize views
        sightImageView = view.findViewById(R.id.sightImageView);
        sightTitleTextView = view.findViewById(R.id.sightTitleTextView);
        sightDescriptionTextView = view.findViewById(R.id.sightDescriptionTextView);
        weatherText = view.findViewById(R.id.weatherInfoText);
        viewMapButton = view.findViewById(R.id.viewMapButton);
        favoriteButton = view.findViewById(R.id.favIcon);
        ImageButton playMusicButton = view.findViewById(R.id.playMusicButton);


        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> requireActivity().getSupportFragmentManager().popBackStack());


        TextView sightInfoHeader = view.findViewById(R.id.sightInfoHeader);
        sightInfoHeader.setText(getString(R.string.sightInfoHeader));
        // Get data from arguments
        if (getArguments() != null) {
            final String title = getArguments().getString("title");
            String description = getArguments().getString("description");
            int imageResId = getArguments().getInt("imageResId");
            int musicResId = getArguments().getInt("musicResId", -1);
            double latitude = getArguments().getDouble("latitude", 0.0);
            double longitude = getArguments().getDouble("longitude", 0.0);


            // Set the data to views
            sightTitleTextView.setText(title);
            sightDescriptionTextView.setText(description);
            sightImageView.setImageResource(imageResId);

            // Load saved favorite state
            final SharedPreferences preferences = requireContext().getSharedPreferences("favorites", Context.MODE_PRIVATE);
            boolean isFavorite = preferences.getBoolean(title, false);
            favoriteButton.setImageResource(isFavorite ? R.drawable.ic_fav : R.drawable.ic_fav_border);

            // Handle favorite button click
            favoriteButton.setOnClickListener(v -> {
                boolean currentState = preferences.getBoolean(title, false);
                boolean newState = !currentState;

                // Save the new state
                preferences.edit().putBoolean(title, newState).apply();

                // Update the favorite icon
                favoriteButton.setImageResource(newState ? R.drawable.ic_fav : R.drawable.ic_fav_border);
            });
            if (musicResId == -1) {
                playMusicButton.setVisibility(View.GONE); // or View.INVISIBLE
                playMusicButton.setOnClickListener(null);
            } else {
                playMusicButton.setVisibility(View.VISIBLE);
                playMusicButton.setEnabled(true);
                playMusicButton.setAlpha(1.0f);
                playMusicButton.setImageResource(R.drawable.ic_music_off);
                playMusicButton.setOnClickListener(v -> {
                    if (mediaPlayer == null) {
                        mediaPlayer = MediaPlayer.create(requireContext(), musicResId);
                    }
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        playMusicButton.setImageResource(R.drawable.ic_music_off);
                    } else {
                        mediaPlayer.start();
                        playMusicButton.setImageResource(R.drawable.ic_music);
                    }
                });
            }

        }

        // Handle View Map button click
        viewMapButton.setOnClickListener(v -> {
            // Handle map navigation logic here
            double latitude = getArguments().getDouble("latitude", 0.0);
            double longitude = getArguments().getDouble("longitude", 0.0);
            final String title = getArguments().getString("title");
            Bundle args = new Bundle();
            args.putString("toTitle", title);
            args.putDouble("toLat", latitude);
            args.putDouble("toLng", longitude);

            Fragment mapFragment = new MapFragment();
            mapFragment.setArguments(args);

            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, mapFragment)
                    .addToBackStack(null)
                    .commit();
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        fetchWeatherForCurrentLocation();

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
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}