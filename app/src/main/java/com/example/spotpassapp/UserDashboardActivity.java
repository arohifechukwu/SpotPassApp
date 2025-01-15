package com.example.spotpassapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class UserDashboardActivity extends AppCompatActivity {

    private Button viewProfileButton, exploreEventsButton, logoutButton, viewSettingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        // Initialize Views
        viewProfileButton = findViewById(R.id.viewProfileButton);
        exploreEventsButton = findViewById(R.id.exploreEventsButton);
        logoutButton = findViewById(R.id.logoutButton);
        viewSettingsButton = findViewById(R.id.viewSettingsButton);

        // Initialize Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        setupBottomNavigation(bottomNavigationView);

        // Button Click Listeners
        viewProfileButton.setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class))
        );

        exploreEventsButton.setOnClickListener(v ->
                startActivity(new Intent(this, ExploreEventsActivity.class))
        );

        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, AuthActivity.class));
            finish();
        });

        viewSettingsButton.setOnClickListener(v ->
                startActivity(new Intent(this, SettingsActivity.class))
        );
    }

    private void setupBottomNavigation(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile); // Highlight the "Profile" tab

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, HomeActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.navigation_search) {
                startActivity(new Intent(this, ExploreEventsActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.navigation_favorites) {
                startActivity(new Intent(this, FavoritesActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.navigation_profile) {
                return true; // Stay on this activity
            }
            return false;
        });
    }

}