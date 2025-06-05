package com.example.schumannechoes;



import android.os.Bundle;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_language);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up the toolbar with a title and navigation icon
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Select Language");
        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> {
            finish(); // Go back to the previous activity in the stack
        });

        // Get references to the language selection buttons
        LinearLayout btnEnglish = findViewById(R.id.btnEnglish);
        LinearLayout btnGerman = findViewById(R.id.btnGerman);

        // Set click listener for the English button
        btnEnglish.setOnClickListener(v -> {
            // Change the app's locale to English
            setLocale("en");
            // Navigate to HomeActivity
            Intent intent = new Intent(LanguageActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

        // Set click listener for the German button
        btnGerman.setOnClickListener(v -> {
            // Change the app's locale to German
            setLocale("de");
            // Navigate to HomeActivity
            Intent intent = new Intent(LanguageActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }

    // Method to change the app's locale
    private void setLocale(String languageCode) {
        // Create a new Locale object with the specified language code
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        // Update the app's configuration with the new locale
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        config.setLocale(locale);
        resources.updateConfiguration(config, displayMetrics);

        // Restart the activity to apply the changes
        recreate();
    }
}


