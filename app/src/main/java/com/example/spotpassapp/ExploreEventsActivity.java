package com.example.spotpassapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ExploreEventsActivity extends AppCompatActivity {

    private List<Event> eventsList, filteredEvents;
    private EventAdapter eventAdapter;
    private BottomNavigationView bottomNavigationView;
    private DatabaseReference databaseRef;
    private TextView filterLink;
    private String lastQuery = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_events);

        initializeViews();
        setupRecyclerView();
        setupSearchView();
        setupBottomNavigation();
        fetchEventsFromDatabase();
    }

    private void initializeViews() {
        eventsList = new ArrayList<>();
        filteredEvents = new ArrayList<>();
        filterLink = findViewById(R.id.filterLink); // TextView for "Filter Results"
        filterLink.setVisibility(View.GONE); // Initially hidden
        filterLink.setOnClickListener(v -> showFilterOptions());

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        databaseRef = FirebaseDatabase.getInstance().getReference("events");
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventAdapter = new EventAdapter(this, filteredEvents);
        recyclerView.setAdapter(eventAdapter);
    }



    private void setupSearchView() {
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterEvents(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterEvents(newText);
                return true;
            }
        });

        searchView.setOnCloseListener(() -> {
            revertToDefaultDisplay();
            return false;
        });
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setSelectedItemId(R.id.navigation_search); // Highlight the "Search" tab

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, HomeActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.navigation_search) {
                return true; // Stay on this activity
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

    private void highlightSelectedItem(@NonNull android.view.MenuItem item) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            item.setIconTintList(getResources().getColorStateList(R.color.primaryColor, null));
        } else {
            DrawableCompat.setTint(item.getIcon(), getResources().getColor(R.color.primaryColor));
        }
    }

    private void navigateToProfile() {
        startActivity(new Intent(this, UserDashboardActivity.class));
    }

    private void fetchEventsFromDatabase() {
        databaseRef.child("events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventsList.clear();
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    if (event != null) {
                        eventsList.add(event);
                    }
                }
                revertToDefaultDisplay();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ExploreEventsActivity.this, "Failed to fetch events.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterEvents(String query) {
        lastQuery = query;
        filteredEvents.clear();

        for (Event event : eventsList) {
            if (event.getTitle().toLowerCase().contains(query.toLowerCase())
                    || event.getLocation().toLowerCase().contains(query.toLowerCase())) {
                filteredEvents.add(event);
            }
        }

        if (!filteredEvents.isEmpty()) {
            filterLink.setVisibility(View.VISIBLE); // Show "Filter Results" link
        } else {
            filterLink.setVisibility(View.GONE); // Hide if no results
        }

        eventAdapter.notifyDataSetChanged();
    }

    private void showFilterOptions() {
        String[] options = {"Lowest Amount", "Highest Amount"};

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Filter Results");
        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                sortFilteredEventsByPrice(true); // Lowest amount
            } else {
                sortFilteredEventsByPrice(false); // Highest amount
            }
        });
        builder.show();
    }

    private void sortFilteredEventsByPrice(boolean lowestFirst) {
        Collections.sort(filteredEvents, new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                return lowestFirst ? Double.compare(e1.getPrice(), e2.getPrice()) : Double.compare(e2.getPrice(), e1.getPrice());
            }
        });

        if (filteredEvents.size() > 10) {
            filteredEvents = filteredEvents.subList(0, 10); // Show only first 10
        }

        eventAdapter.notifyDataSetChanged();
    }

    private void revertToDefaultDisplay() {
        lastQuery = "";
        filterLink.setVisibility(View.GONE); // Hide "Filter Results" link
        filteredEvents.clear();
        filteredEvents.addAll(eventsList); // Restore the default list
        eventAdapter.notifyDataSetChanged();
    }
}