package com.example.schumannechoes.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.schumannechoes.AppNotification;
import com.example.schumannechoes.NotificationAdapter;
import com.example.schumannechoes.NotificationStorage;
import com.example.schumannechoes.R;

import java.util.List;

public class NotificationFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        ImageButton backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> requireActivity().onBackPressed());

        RecyclerView recyclerView = view.findViewById(R.id.recycler_notifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<AppNotification> notifications = NotificationStorage.loadNotifications(getContext());
        recyclerView.setAdapter(new NotificationAdapter(notifications));

        // Add your notification UI here
        LottieAnimationView anim = view.findViewById(R.id.newNotificationAnim);
        SharedPreferences prefs = requireContext().getSharedPreferences("notifications_prefs", Context.MODE_PRIVATE);
        boolean hasNew = prefs.getBoolean("has_new_notification", false);
        if (hasNew) {
            anim.setAnimation("visited_animation.json"); // set the animation from assets
            anim.setVisibility(View.VISIBLE);
            anim.playAnimation();
            anim.postDelayed(() -> anim.setVisibility(View.GONE), 8000);
            prefs.edit().putBoolean("has_new_notification", false).apply(); // reset flag
        }

        return view;
    }
}