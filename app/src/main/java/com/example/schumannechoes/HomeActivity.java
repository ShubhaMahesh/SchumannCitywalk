package com.example.schumannechoes;



import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.schumannechoes.fragments.ChallengesFragment;
import com.example.schumannechoes.fragments.FavoritesFragment;
import com.example.schumannechoes.fragments.HomeFragment;
import com.example.schumannechoes.fragments.MapFragment;
import com.example.schumannechoes.fragments.SightsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        // Set titles for navigation items using R.string
        bottomNavigationView.getMenu().findItem(R.id.nav_home).setTitle(getString(R.string.nav_home));
        bottomNavigationView.getMenu().findItem(R.id.nav_map).setTitle(getString(R.string.nav_map));
        bottomNavigationView.getMenu().findItem(R.id.nav_rewards).setTitle(getString(R.string.nav_Rewards));
        bottomNavigationView.getMenu().findItem(R.id.nav_sights).setTitle(getString(R.string.nav_sights));
        bottomNavigationView.getMenu().findItem(R.id.nav_favorites).setTitle(getString(R.string.nav_favorites));
        loadFragment(new HomeFragment());


        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (id == R.id.nav_map) {
                selectedFragment = new MapFragment();
            } else if (id == R.id.nav_rewards) {
                selectedFragment = new ChallengesFragment();
            } else if (id == R.id.nav_sights) {
                selectedFragment = new SightsFragment();
            } else if (id == R.id.nav_favorites) {
                selectedFragment = new FavoritesFragment();
            }
            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            return true;
        });

    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .commit();
    }

    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (currentFragment instanceof com.example.schumannechoes.fragments.HomeFragment) {
            finishAffinity(); // Closes the app
        } else {
            super.onBackPressed();
        }
    }

}