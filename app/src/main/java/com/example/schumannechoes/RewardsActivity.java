package com.example.schumannechoes;



import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RewardsActivity extends AppCompatActivity {

    private RecyclerView visitedRecyclerView, unvisitedRecyclerView;
    private TextView locationsCountText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rewards);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        visitedRecyclerView = findViewById(R.id.visitedRecyclerView);
        unvisitedRecyclerView = findViewById(R.id.unvisitedRecyclerView);
        locationsCountText = findViewById(R.id.locations_count_text);

        ImageButton backButton = findViewById(R.id.rewardsBackButton);
        backButton.setOnClickListener(v -> finish());

        // Ensure locations are initialized
        LocationUtils.initLocationsIfNeeded(this);


        List<LocationModel> allLocations = LocationUtils.loadLocations(this);
        List<LocationModel> visited = new ArrayList<>();
        List<LocationModel> unvisited = new ArrayList<>();
        for (LocationModel loc : allLocations) {
            if (loc.isVisited()) visited.add(loc);
            else unvisited.add(loc);
        }

        locationsCountText.setText(getString(R.string.locations_count_message, visited.size(), allLocations.size()));

        visitedRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        visitedRecyclerView.setAdapter(new LocationItemAdapter(this, visited, true));

        unvisitedRecyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        unvisitedRecyclerView.setAdapter(new LocationItemAdapter(this, unvisited, false));
    }
}
