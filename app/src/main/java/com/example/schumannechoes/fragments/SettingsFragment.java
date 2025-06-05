package com.example.schumannechoes.fragments;



import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.schumannechoes.LanguageActivity;
import com.example.schumannechoes.R;

public class SettingsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        View backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.nav_host_fragment, new HomeFragment())
                    .commit();
        });

        // Reference all TextViews
        TextView settingsTitle = view.findViewById(R.id.settings_title);
        TextView languageText = view.findViewById(R.id.language_text);
        TextView accountText = view.findViewById(R.id.account_text);
        TextView helpText = view.findViewById(R.id.help_text);
        TextView aboutText = view.findViewById(R.id.about_text);

        // Set text dynamically
        settingsTitle.setText(getString(R.string.settings));
        languageText.setText(getString(R.string.language));
        accountText.setText(getString(R.string.account));
        helpText.setText(getString(R.string.help));
        aboutText.setText(getString(R.string.about));
        // Initialize each setting item
        LinearLayout languageItem = view.findViewById(R.id.language_item);
        LinearLayout accountItem = view.findViewById(R.id.account_item);
        LinearLayout helpItem = view.findViewById(R.id.help_item);
        LinearLayout aboutItem = view.findViewById(R.id.about_item);

        // Set click listeners for each item
        languageItem.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), LanguageActivity.class);
            startActivity(intent);
        });

        accountItem.setOnClickListener(v -> {
            // Handle Account click
            Toast.makeText(getActivity(), "Account clicked", Toast.LENGTH_SHORT).show();
        });

        helpItem.setOnClickListener(v -> {
            // Handle Help and Support click
            Toast.makeText(getActivity(), "Help and Support clicked", Toast.LENGTH_SHORT).show();
        });

        aboutItem.setOnClickListener(v -> {
            // Handle About click
            Toast.makeText(getActivity(), "About clicked", Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}