package com.example.schumannechoes.fragments;



import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import com.example.schumannechoes.BuildConfig;
import com.example.schumannechoes.LocationModel;
import com.example.schumannechoes.LocationUtils;
import com.example.schumannechoes.NotificationHelper;
import com.example.schumannechoes.R;
import com.google.android.gms.location.*;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import android.os.Looper;
import org.json.*;

import java.io.IOException;
import java.util.*;
import okhttp3.*;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "MapDebug";
    private static final String API_KEY = BuildConfig.maps_api_key;
    private GoogleMap mMap;
    private PlacesClient placesClient;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private AutoCompleteTextView fromView, toView;
    private Button fromCurrent, toCurrent;
    private TextView routeInfo;
    private Spinner modeSpinner;
    private ArrayAdapter<String> fromAdapter, toAdapter;
    private List<AutocompletePrediction> fromPredictions = new ArrayList<>();
    private List<AutocompletePrediction> toPredictions = new ArrayList<>();
    private LatLng fromLatLng = null, toLatLng = null;
    private Polyline currentPolyline;
    private OkHttpClient httpClient = new OkHttpClient();

    private final String[] apiModes = {"walking", "driving", "bicycling", "transit"};
    private String totitlesight = "Destination";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            double toLat = args.getDouble("toLat", 0);
            double toLng = args.getDouble("toLng", 0);
            String toTitle = args.getString("toTitle", "Destination");
            if (toTitle != null) totitlesight = toTitle;

            if (toLat != 0 && toLng != 0) {
                toLatLng = new LatLng(toLat, toLng);
                toView.setText(toTitle);
                setFromCurrentLocation();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);

        if (!Places.isInitialized()) {
            Places.initialize(requireContext().getApplicationContext(), API_KEY);
        }
        placesClient = Places.createClient(requireContext());
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
        fromView = root.findViewById(R.id.from_view);
        toView = root.findViewById(R.id.to_view);
        fromCurrent = root.findViewById(R.id.from_current);
        toCurrent = root.findViewById(R.id.to_current);
        routeInfo = root.findViewById(R.id.route_info);
        modeSpinner = root.findViewById(R.id.mode_spinner);

        fromAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line);
        fromView.setAdapter(fromAdapter);
        fromView.setThreshold(1);

        toAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line);
        toView.setAdapter(toAdapter);
        toView.setThreshold(1);

        setAutocompleteListeners();

        fromCurrent.setOnClickListener(v -> setFromCurrentLocation());
        toCurrent.setOnClickListener(v -> setToCurrentLocation());

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.mode_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeSpinner.setAdapter(spinnerAdapter);
        modeSpinner.setSelection(0);

        modeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                drawRouteIfReady();
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        setupDynamicLocationUpdates();

        return root;
    }

    private void setAutocompleteListeners() {
        fromView.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int st, int b, int c) {
                if (TextUtils.isEmpty(s)) return;
                FindAutocompletePredictionsRequest req = FindAutocompletePredictionsRequest.builder()
                        .setQuery(s.toString()).build();
                placesClient.findAutocompletePredictions(req)
                        .addOnSuccessListener(r -> {
                            fromAdapter.clear(); fromPredictions.clear();
                            for (AutocompletePrediction p : r.getAutocompletePredictions()) {
                                fromAdapter.add(p.getFullText(null).toString());
                                fromPredictions.add(p);
                            }
                            fromAdapter.notifyDataSetChanged();
                        })
                        .addOnFailureListener(e -> Log.e(TAG, "From autocomplete fail: "+e.getMessage()));
            }
        });
        toView.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int st, int b, int c) {
                if (TextUtils.isEmpty(s)) return;
                FindAutocompletePredictionsRequest req = FindAutocompletePredictionsRequest.builder()
                        .setQuery(s.toString()).build();
                placesClient.findAutocompletePredictions(req)
                        .addOnSuccessListener(r -> {
                            toAdapter.clear(); toPredictions.clear();
                            for (AutocompletePrediction p : r.getAutocompletePredictions()) {
                                toAdapter.add(p.getFullText(null).toString());
                                toPredictions.add(p);
                            }
                            toAdapter.notifyDataSetChanged();
                        })
                        .addOnFailureListener(e -> Log.e(TAG, "To autocomplete fail: "+e.getMessage()));
            }
        });

        fromView.setOnItemClickListener((parent, view, pos, id) -> {
            if (pos >= 0 && pos < fromPredictions.size())
                fetchPlaceLatLng(fromPredictions.get(pos).getPlaceId(), true);
        });
        toView.setOnItemClickListener((parent, view, pos, id) -> {
            if (pos >= 0 && pos < toPredictions.size())
                fetchPlaceLatLng(toPredictions.get(pos).getPlaceId(), false);
        });
    }

    private void setFromCurrentLocation() {
        getCurrentLocation(latlng -> {
            fromLatLng = latlng;
            fromView.setText("Current Location");
            drawRouteIfReady();
        });
    }
    private void setToCurrentLocation() {
        getCurrentLocation(latlng -> {
            toLatLng = latlng;
            toView.setText("Current Location");
            drawRouteIfReady();
        });
    }

    private void fetchPlaceLatLng(String placeId, boolean isFrom) {
        List<Place.Field> fields = Collections.singletonList(Place.Field.LAT_LNG);
        FetchPlaceRequest req = FetchPlaceRequest.builder(placeId, fields).build();
        placesClient.fetchPlace(req)
                .addOnSuccessListener(resp -> {
                    LatLng latLng = resp.getPlace().getLatLng();
                    if (latLng != null) {
                        if (isFrom) fromLatLng = latLng; else toLatLng = latLng;
                        drawRouteIfReady();
                    }
                })
                .addOnFailureListener(e -> Log.e(TAG, "FetchPlace failed: "+e.getMessage()));
    }

    private void getCurrentLocation(SimpleLatLngCallback callback) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1001);
            Toast.makeText(requireContext(), "Location permission needed", Toast.LENGTH_SHORT).show();
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(loc -> {
            if (loc != null) callback.onLatLng(new LatLng(loc.getLatitude(), loc.getLongitude()));
            else Toast.makeText(requireContext(), "Location unavailable", Toast.LENGTH_SHORT).show();
        });
    }

    private void drawRouteIfReady() {
        if (fromLatLng != null && toLatLng != null) {
            if (currentPolyline != null) currentPolyline.remove();
            mMap.clear();
            mMap.addMarker(new MarkerOptions()
                    .position(fromLatLng)
                    .title("From")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            mMap.addMarker(new MarkerOptions().position(toLatLng).title("To"));
            LatLngBounds bounds = new LatLngBounds.Builder().include(fromLatLng).include(toLatLng).build();
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 120));

            fetchRouteAndDraw(fromLatLng, toLatLng);
        }
    }

    private void fetchRouteAndDraw(LatLng origin, LatLng dest) {
        int selectedIndex = modeSpinner.getSelectedItemPosition();
        if (selectedIndex < 0 || selectedIndex >= apiModes.length) {
            Toast.makeText(requireContext(), "Please select a travel mode", Toast.LENGTH_SHORT).show();
            return;
        }
        String selectedMode = apiModes[selectedIndex];

        String url = "https://maps.googleapis.com/maps/api/directions/json?"
                + "origin=" + origin.latitude + "," + origin.longitude
                + "&destination=" + dest.latitude + "," + dest.longitude
                + "&mode=" + selectedMode
                + "&alternatives=false"
                + "&departure_time=now"
                + "&traffic_model=best_guess"
                + "&key=" + API_KEY;

        Request req = new Request.Builder().url(url).build();
        httpClient.newCall(req).enqueue(new okhttp3.Callback() {
            @Override public void onFailure(@NonNull Call call, @NonNull IOException e) {
                requireActivity().runOnUiThread(() ->
                        Toast.makeText(requireContext(), "Directions fail: "+e.getMessage(), Toast.LENGTH_SHORT).show());
                Log.e(TAG, "Directions fail: "+e.getMessage());
            }
            @Override public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String body = response.body().string();
                Log.d(TAG, "Directions API response: "+body);
                try {
                    JSONObject json = new JSONObject(body);
                    JSONArray routes = json.getJSONArray("routes");
                    if (routes.length() == 0) throw new Exception("No routes found");
                    JSONObject route = routes.getJSONObject(0);
                    JSONObject leg = route.getJSONArray("legs").getJSONObject(0);

                    String modeLabel = modeSpinner.getSelectedItem().toString();
                    String duration = leg.getJSONObject("duration").getString("text");
                    String distance = leg.getJSONObject("distance").getString("text");
                    final String[] durationInTraffic = {""};

                    if ("driving".equals(selectedMode) && leg.has("duration_in_traffic")) {
                        durationInTraffic[0] = leg.getJSONObject("duration_in_traffic").getString("text");
                    }

                    String polyline = route.getJSONObject("overview_polyline").getString("points");
                    List<LatLng> points = decodePolyline(polyline);

                    requireActivity().runOnUiThread(() -> {
                        currentPolyline = mMap.addPolyline(new PolylineOptions()
                                .addAll(points)
                                .width(12)
                                .color(0xFF1976D2)
                                .geodesic(true));
                        mMap.setTrafficEnabled("driving".equals(selectedMode));
                        String info = "Mode: " + modeLabel +
                                "\nDuration: " + duration;

                        if ("driving".equals(selectedMode) && !durationInTraffic[0].isEmpty()) {
                            info += " (in traffic: " + durationInTraffic[0] + ")";
                        }
                        info += "\nDistance: " + distance;

                        routeInfo.setText(info);
                    });
                } catch (Exception e) {
                    requireActivity().runOnUiThread(() ->
                            Toast.makeText(requireContext(), "Parse route failed", Toast.LENGTH_SHORT).show());
                    Log.e(TAG, "Parse route failed: "+e.getMessage());
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        mMap.setTrafficEnabled(true);

        // Enable Google's built-in zoom controls:
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Move camera to current location, fallback to Zwickau if unavailable
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null) {
                    LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 14f));
                } else {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(50.7163, 12.4954), 11f));
                }
            });
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(50.7163, 12.4954), 11f));
        }
    }

    private void setupDynamicLocationUpdates() {
        locationRequest = LocationRequest.create()
                .setInterval(5000)
                .setFastestInterval(2000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) return;
                Location location = locationResult.getLastLocation();
                if (location != null && fromView.getText().toString().equalsIgnoreCase("Current Location")) {
                    fromLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    drawRouteIfReady();
                }
                checkIfArrivedAtDestination();
            }
        };
    }
    private void checkIfArrivedAtDestination() {
        if (fromLatLng == null || toLatLng == null) return;

        float[] results = new float[1];
        Location.distanceBetween(
                fromLatLng.latitude, fromLatLng.longitude,
                toLatLng.latitude, toLatLng.longitude,
                results
        );
        if (results[0] <= 50.0) {
            LocationUtils.initLocationsIfNeeded(requireContext());
            ArrayList<LocationModel> locationList = LocationUtils.loadLocations(requireContext());
            if (locationList != null) {
                boolean updated = false;
                String destTitle =totitlesight  ;
                for (LocationModel loc : locationList) {
                    if (!loc.isVisited()
                            && loc.getName() != null
                            && loc.getName().trim().equalsIgnoreCase(destTitle)) {
                        loc.setVisited(true);
                        updated = true;
                        NotificationHelper.showVisitedNotification(requireContext(), loc.getName());
                    }
                }
                if (updated) {
                    LocationUtils.saveLocations(requireContext(), locationList);

                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startDynamicLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopDynamicLocationUpdates();
    }

    private void startDynamicLocationUpdates() {
        if (locationRequest == null || locationCallback == null) return;
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }
    }

    private void stopDynamicLocationUpdates() {
        if (locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    private List<LatLng> decodePolyline(String encoded) {
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do { b = encoded.charAt(index++) - 63; result |= (b & 0x1f) << shift; shift += 5; }
            while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0; result = 0;
            do { b = encoded.charAt(index++) - 63; result |= (b & 0x1f) << shift; shift += 5; }
            while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            poly.add(new LatLng(lat / 1E5, lng / 1E5));
        }
        return poly;
    }

    private abstract static class SimpleTextWatcher implements android.text.TextWatcher {
        public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
        public void afterTextChanged(android.text.Editable s) {}
    }

    private interface SimpleLatLngCallback { void onLatLng(LatLng latlng); }
}
