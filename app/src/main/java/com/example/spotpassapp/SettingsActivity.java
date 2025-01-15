package com.example.spotpassapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFERENCES_NAME = "SettingsPreferences";
    private static final String KEY_NOTIFICATIONS = "notifications";
    private static final String KEY_DARK_MODE = "dark_mode";

    private Switch notificationSwitch, darkModeSwitch;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize Views
        notificationSwitch = findViewById(R.id.notificationSwitch);
        darkModeSwitch = findViewById(R.id.darkModeSwitch);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFERENCES_NAME, MODE_PRIVATE);

        // Restore saved preferences
        restorePreferences();

        // Initialize Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        setupBottomNavigation(bottomNavigationView);

        // Set up listeners for switches
        setupSwitchListeners();
    }

    private void setupSwitchListeners() {
        notificationSwitch.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            savePreference(KEY_NOTIFICATIONS, isChecked);
            String message = isChecked ? "Notifications Enabled" : "Notifications Disabled";
            Toast.makeText(SettingsActivity.this, message, Toast.LENGTH_SHORT).show();
        });

        // Dark mode switch will not apply dark mode, only save the toggle state
        darkModeSwitch.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            savePreference(KEY_DARK_MODE, isChecked);
            // Display a toast but take no effect
            Toast.makeText(this, "Dark Mode setting saved (No effect).", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupBottomNavigation(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile); // Highlight current tab
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
                startActivity(new Intent(this, UserDashboardActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }

    private void savePreference(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private void restorePreferences() {
        boolean notificationsEnabled = sharedPreferences.getBoolean(KEY_NOTIFICATIONS, true); // Default: enabled
        boolean darkModeEnabled = sharedPreferences.getBoolean(KEY_DARK_MODE, false); // Default: disabled

        notificationSwitch.setChecked(notificationsEnabled);
        darkModeSwitch.setChecked(darkModeEnabled);
    }
}