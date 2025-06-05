package com.example.schumannechoes.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.schumannechoes.CategorySpinnerAdapter;
import com.example.schumannechoes.LanguageActivity;
import com.example.schumannechoes.MainActivity;
import com.example.schumannechoes.R;
import com.example.schumannechoes.SharedViewModel;
import com.example.schumannechoes.Sight;
import com.example.schumannechoes.SightsAdapter;

import java.util.ArrayList;
import java.util.List;

public class SightsFragment extends Fragment {

    private RecyclerView recyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sights, container, false);

        TextView sightstoolbarTitle = view.findViewById(R.id.sightstoolbarTitle);
        sightstoolbarTitle.setText(getString(R.string.sightstoolbarTitle));
        Toolbar toolbar = view.findViewById(R.id.sightsToolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, new HomeFragment()) // Replace with your container ID
                    .addToBackStack(null)
                    .commit();
        });
        Spinner categorySpinner = view.findViewById(R.id.categorySpinner);
        recyclerView = view.findViewById(R.id.sightsRecyclerView);


        String[] dropdownItems = {"All", "Music", "Architecture"};
        CategorySpinnerAdapter spinnerAdapter = new CategorySpinnerAdapter(requireContext(), dropdownItems);
        categorySpinner.setAdapter(spinnerAdapter);


        SharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        List<Sight> sights = viewModel.getSights(requireContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SightsAdapter adapter = new SightsAdapter(sights);
        recyclerView.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<Sight> filteredSights;
                if (position == 1) { // Music
                    filteredSights = filterSightsByCategory(sights, "Music");
                } else if (position == 2) { // Architecture
                    filteredSights = filterSightsByCategory(sights, "Architecture");
                } else { // All
                    filteredSights = sights;
                }
                adapter.updateSights(filteredSights);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                adapter.updateSights(sights);
            }
        });
        return view;
    }

    private List<Sight> filterSightsByCategory(List<Sight> sights, String category) {
        List<Sight> filteredSights = new ArrayList<>();
        for (Sight sight : sights) {
            if (category.equals("Music") && isMusicRelated(sight)) {
                filteredSights.add(sight);
            } else if (category.equals("Architecture") && isArchitectureRelated(sight)) {
                filteredSights.add(sight);
            }
        }
        return filteredSights;
    }

    private boolean isMusicRelated(Sight sight) {
        return (sight.getName().contains("Schumann") || sight.getName().contains("Gewandhaus"))
                && sight.getMusicResId() != -1;
    }

    private boolean isArchitectureRelated(Sight sight) {
        return !isMusicRelated(sight);
    }
}