package com.example.schumannechoes;



import android.content.Context;
import android.content.SharedPreferences;

import com.example.schumannechoes.LocationModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LocationUtils {
    private static final String PREFS_NAME = "visited_locations_prefs";
    private static final String KEY_LOCATIONS = "locations_json";

    public static void saveLocations(Context context, ArrayList<LocationModel> locations) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        JSONArray arr = new JSONArray();
        for (LocationModel loc : locations) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("name", loc.getName());
                obj.put("latitude", loc.getLatitude());
                obj.put("longitude", loc.getLongitude());
                obj.put("visited", loc.isVisited());
                arr.put(obj);
            } catch (JSONException e) { e.printStackTrace(); }
        }
        prefs.edit().putString(KEY_LOCATIONS, arr.toString()).apply();
    }

    public static ArrayList<LocationModel> loadLocations(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_LOCATIONS, null);
        ArrayList<LocationModel> locations = new ArrayList<>();
        if (json != null) {
            try {
                JSONArray arr = new JSONArray(json);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    locations.add(new LocationModel(
                            obj.getString("name"),
                            obj.getDouble("latitude"),
                            obj.getDouble("longitude"),
                            obj.getBoolean("visited")
                    ));
                }
            } catch (JSONException e) { e.printStackTrace(); }
        }
        return locations;
    }

    public static void initLocationsIfNeeded(Context context) {
        if (loadLocations(context).isEmpty()) {
            ArrayList<LocationModel> list = new ArrayList<>();
            // TODO: Replace with your real 14 locations:
            list.add(new LocationModel(context.getString(R.string.sight1_title), 50.7174, 12.4961, false));
            list.add(new LocationModel(context.getString(R.string.sight2_title), 50.7214, 12.4385, false));
            list.add(new LocationModel(context.getString(R.string.sight3_title), 50.7165,12.4945, false));
            list.add(new LocationModel(context.getString(R.string.sight4_title), 50.7176, 12.4946, false));
            list.add(new LocationModel(context.getString(R.string.sight5_title), 50.7179, 12.4951, false));
            list.add(new LocationModel(context.getString(R.string.sight6_title), 52.5076, 13.3904, false));
            list.add(new LocationModel(context.getString(R.string.sight7_title), 50.7177, 12.4973, false));
            list.add(new LocationModel(context.getString(R.string.sight8_title), 50.7296,12.4998, false));
            list.add(new LocationModel(context.getString(R.string.sight9_title), 50.7176, 12.4981, false));
            list.add(new LocationModel(context.getString(R.string.sight10_title), 52.5138, 13.3923, false));
            list.add(new LocationModel(context.getString(R.string.sight11_title), 50.7177, 12.4973, false));
            list.add(new LocationModel(context.getString(R.string.sight12_title), 50.7174, 12.4961, false));
            list.add(new LocationModel(context.getString(R.string.sight13_title), 50.7209, 12.5000, false));
            list.add(new LocationModel(context.getString(R.string.sight14_title), 50.7178, 12.5018, false));
            saveLocations(context, list);
        }
    }
}