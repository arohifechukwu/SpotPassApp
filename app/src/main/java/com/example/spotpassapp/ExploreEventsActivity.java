package com.example.spotpassapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.spotpassapp.adapter.EventAdapter;
import com.example.spotpassapp.model.Event;
import java.util.ArrayList;
import java.util.List;

public class ExploreEventsActivity extends AppCompatActivity {

    private List<Event> eventsList;
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_events);

        initializeViews();
        setupRecyclerView();
        setupSearchView();
        setupBottomNavigation();
    }

    /**
     * Initialize views and components.
     */
    private void initializeViews() {
        eventsList = new ArrayList<>();
        populateSampleData();
    }

    /**
     * Populate sample event data.
     */
    private void populateSampleData() {
        eventsList.add(new Event("Concert Night", "Music festival", "Montreal", 50.0, "2024-11-20", "19:00", String.valueOf(R.drawable.splash_image)));
        eventsList.add(new Event("Art Expo", "Modern art exhibit", "Quebec", 30.0, "2024-11-25", "10:00", String.valueOf(R.drawable.splash_image)));
    }

    /**
     * Setup RecyclerView and attach the EventAdapter.
     */
    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new EventAdapter(this, eventsList);
        recyclerView.setAdapter(eventAdapter);
    }

    /**
     * Setup SearchView to filter events.
     */
    private void setupSearchView() {
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                eventAdapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                eventAdapter.filter(newText);
                return true;
            }
        });
    }

    /**
     * Setup BottomNavigationView for navigation.
     */
    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                startActivity(new Intent(this, HomeActivity.class));
            } else if (id == R.id.navigation_search) {
                startActivity(new Intent(this, SearchActivity.class));
            } else if (id == R.id.navigation_favorites) {
                startActivity(new Intent(this, FavoritesActivity.class));
            } else if (id == R.id.navigation_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
            }
            return true;
        });
    }
}