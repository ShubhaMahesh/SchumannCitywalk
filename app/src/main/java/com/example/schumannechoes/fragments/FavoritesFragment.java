package com.example.schumannechoes.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.schumannechoes.R;
import com.example.schumannechoes.FavoritesAdapter;
import com.example.schumannechoes.SharedViewModel;
import com.example.schumannechoes.Sight;
import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {

    private RecyclerView favoritesRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);


        Toolbar toolbar = view.findViewById(R.id.favoriteToolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, new HomeFragment()) // Replace with your container ID
                    .addToBackStack(null)
                    .commit();
        });
        favoritesRecyclerView = view.findViewById(R.id.favoritesRecyclerView);

// Use GridLayoutManager with 2 columns (or adjust the span count as needed)
        GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
        favoritesRecyclerView.setLayoutManager(gridLayoutManager);

// Load favorite sights
        List<Sight> favoriteSights = getFavoriteSights();
        FavoritesAdapter adapter = new FavoritesAdapter(favoriteSights);
        favoritesRecyclerView.setAdapter(adapter);

        return view;
    }

    private List<Sight> getFavoriteSights() {
        SharedPreferences preferences = requireContext().getSharedPreferences("favorites", Context.MODE_PRIVATE);
        SharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        List<Sight> sights = viewModel.getSights(requireContext());
        List<Sight> allSights =sights; // Replace with your existing method to fetch all sights
        List<Sight> favoriteSights = new ArrayList<>();

        for (Sight sight : allSights) {
            if (preferences.getBoolean(sight.getName(), false)) {
                favoriteSights.add(sight);
            }
        }
        return favoriteSights;
    }
}