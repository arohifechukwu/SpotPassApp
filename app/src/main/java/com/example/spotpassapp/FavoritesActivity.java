package com.example.spotpassapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;
import android.content.res.ColorStateList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotpassapp.adapter.EventAdapter;
import com.example.spotpassapp.model.Event;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> favoriteEvents;
    private BottomNavigationView bottomNavigationView;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        initializeViews();
        setupRecyclerView();
        setupBottomNavigation();
        fetchFavoriteEventsFromDatabase(); // Fetch favorite events from Firebase
    }

    /**
     * Initialize views and components.
     */
    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerView);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        databaseRef = FirebaseDatabase.getInstance().getReference("favorites"); // Firebase database reference for favorites
        favoriteEvents = new ArrayList<>();
    }

    /**
     * Setup RecyclerView and attach the EventAdapter.
     */
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new EventAdapter(this, favoriteEvents);
        recyclerView.setAdapter(eventAdapter);
    }

    /**
     * Fetch favorite events from Firebase Realtime Database.
     */
    private void fetchFavoriteEventsFromDatabase() {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favoriteEvents.clear(); // Clear the list to avoid duplication
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    if (event != null) {
                        favoriteEvents.add(event);
                    }
                }
                eventAdapter.notifyDataSetChanged(); // Update the adapter with new data
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FavoritesActivity.this, "Failed to fetch favorite events.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Setup BottomNavigationView for navigation and item selection highlighting.
     */
    private void setupBottomNavigation() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                highlightSelectedItem(item);
                startActivity(new Intent(this, HomeActivity.class));
                return true;
            } else if (itemId == R.id.navigation_search) {
                highlightSelectedItem(item);
                startActivity(new Intent(this, ExploreEventsActivity.class));
                return true;
            } else if (itemId == R.id.navigation_favorites) {
                highlightSelectedItem(item); // Stay on FavoritesActivity
                return true;
            } else if (itemId == R.id.navigation_profile) {
                highlightSelectedItem(item);
                navigateToProfile();
                return true;
            }
            return false;
        });
    }

    /**
     * Highlight the selected item by changing its icon color.
     */
    private void highlightSelectedItem(@NonNull android.view.MenuItem item) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            item.setIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.primaryColor)));
        } else {
            DrawableCompat.setTint(item.getIcon(), getResources().getColor(R.color.primaryColor));
        }
    }

    /**
     * Navigate to the User's Profile.
     */
    private void navigateToProfile() {
        startActivity(new Intent(this, UserDashboardActivity.class));
    }
}