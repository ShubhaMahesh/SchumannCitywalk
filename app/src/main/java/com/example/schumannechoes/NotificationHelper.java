package com.example.schumannechoes;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class NotificationHelper {
    private static final String CHANNEL_ID = "visited_channel";
    private static final String CHANNEL_NAME = "Visited Locations";
    private static final int NOTIFICATION_ID = 42;

    public static void showVisitedNotification(Context context, String placeName) {
        createNotificationChannel(context);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);


        // Optional: Open app when tapped
        Intent intent = new Intent(context, MainActivity.class); // Update to your main activity
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Place Visited")
                .setContentText("Visited: " + placeName)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        manager.notify(NOTIFICATION_ID, builder.build());
        NotificationStorage.addNotification(context,
                new AppNotification("Place Visited", "Congratulations! You have collected a new rewards as you have Visited: " + placeName, System.currentTimeMillis()));
        // After NotificationStorage.addNotification(...)
        context.getSharedPreferences("notifications_prefs", Context.MODE_PRIVATE)
                .edit().putBoolean("has_new_notification", true).apply();
    }

    // Java
    private static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Notifications for visited locations");
            manager.createNotificationChannel(channel);
        }
    }

}