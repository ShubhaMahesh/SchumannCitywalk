package com.example.schumannechoes;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationStorage {
    private static final String PREFS_NAME = "notifications_prefs";
    private static final String KEY_NOTIFICATIONS = "notifications_json";

    public static void addNotification(Context context, AppNotification notification) {
        List<AppNotification> list = loadNotifications(context);
        list.add(0, notification); // Add newest at top
        saveNotifications(context, list);
    }

    public static void saveNotifications(Context context, List<AppNotification> notifications) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        JSONArray arr = new JSONArray();
        for (AppNotification n : notifications) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("title", n.getTitle());
                obj.put("message", n.getMessage());
                obj.put("timestamp", n.getTimestamp());
                arr.put(obj);
            } catch (JSONException e) { e.printStackTrace(); }
        }
        prefs.edit().putString(KEY_NOTIFICATIONS, arr.toString()).apply();
    }

    public static List<AppNotification> loadNotifications(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_NOTIFICATIONS, null);
        List<AppNotification> list = new ArrayList<>();
        if (json != null) {
            try {
                JSONArray arr = new JSONArray(json);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    list.add(new AppNotification(
                            obj.getString("title"),
                            obj.getString("message"),
                            obj.getLong("timestamp")
                    ));
                }
            } catch (JSONException e) { e.printStackTrace(); }
        }
        return list;
    }
}