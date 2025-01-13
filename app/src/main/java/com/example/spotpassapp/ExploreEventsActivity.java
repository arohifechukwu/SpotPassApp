//package com.example.spotpassapp;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.SearchView;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//import com.example.spotpassapp.adapter.EventAdapter;
//import com.example.spotpassapp.model.Event;
//import java.util.ArrayList;
//import java.util.List;
//
//public class ExploreEventsActivity extends AppCompatActivity {
//
//    private List<Event> eventsList;
//    private EventAdapter eventAdapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_explore_events);
//
//        initializeViews();
//        setupRecyclerView();
//        setupSearchView();
//        setupBottomNavigation();
//    }
//
//    /**
//     * Initialize views and components.
//     */
//    private void initializeViews() {
//        eventsList = new ArrayList<>();
//        populateSampleData();
//    }
//
//    /**
//     * Populate sample event data.
//     */
//    private void populateSampleData() {
//        eventsList.add(new Event("Concert Night", "Music festival", "Montreal", 50.0, "2024-11-20", "19:00", String.valueOf(R.drawable.splash_image)));
//        eventsList.add(new Event("Art Expo", "Modern art exhibit", "Quebec", 30.0, "2024-11-25", "10:00", String.valueOf(R.drawable.splash_image)));
//    }
//
//    /**
//     * Setup RecyclerView and attach the EventAdapter.
//     */
//    private void setupRecyclerView() {
//        RecyclerView recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        eventAdapter = new EventAdapter(this, eventsList);
//        recyclerView.setAdapter(eventAdapter);
//    }
//
//    /**
//     * Setup SearchView to filter events.
//     */
//    private void setupSearchView() {
//        SearchView searchView = findViewById(R.id.searchView);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                eventAdapter.filter(query);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                eventAdapter.filter(newText);
//                return true;
//            }
//        });
//    }
//
//    /**
//     * Setup BottomNavigationView for navigation.
//     */




//package com.example.spotpassapp;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.SearchView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.spotpassapp.adapter.EventAdapter;
//import com.example.spotpassapp.model.Event;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ExploreEventsActivity extends AppCompatActivity {
//
//    private List<Event> eventsList;
//    private EventAdapter eventAdapter;
//    private BottomNavigationView bottomNavigationView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_explore_events);
//
//        initializeViews();
//        setupRecyclerView();
//        setupSearchView();
//        setupBottomNavigation();
//    }
//
//    /**
//     * Initialize views and components.
//     */
//    private void initializeViews() {
//        eventsList = new ArrayList<>();
//        bottomNavigationView = findViewById(R.id.bottom_navigation);
//        populateSampleData();
//    }
//
//    /**
//     * Populate sample event data.
//     */
//    private void populateSampleData() {
//        eventsList.add(new Event("Concert Night", "Music festival", "Montreal", 50.0, "2024-11-20", "19:00", String.valueOf(R.drawable.splash_image)));
//        eventsList.add(new Event("Art Expo", "Modern art exhibit", "Quebec", 30.0, "2024-11-25", "10:00", String.valueOf(R.drawable.splash_image)));
//        eventsList.add(new Event("Tech Conference", "Technology insights", "Toronto", 100.0, "2024-12-01", "09:00", String.valueOf(R.drawable.splash_image)));
//        eventsList.add(new Event("Food Festival", "Gastronomic delight", "Vancouver", 25.0, "2024-12-05", "11:00", String.valueOf(R.drawable.splash_image)));
//    }
//
//    /**
//     * Setup RecyclerView and attach the EventAdapter.
//     */
//    private void setupRecyclerView() {
//        RecyclerView recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        eventAdapter = new EventAdapter(this, eventsList);
//        recyclerView.setAdapter(eventAdapter);
//    }
//
//    /**
//     * Setup SearchView to filter events.
//     */
//    private void setupSearchView() {
//        SearchView searchView = findViewById(R.id.searchView);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                eventAdapter.filter(query);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                eventAdapter.filter(newText);
//                return true;
//            }
//        });
//    }
//
//    /**
//     * Setup BottomNavigationView for navigation.
//     */
//    private void setupBottomNavigation() {
//        bottomNavigationView.setOnItemSelectedListener(item -> {
//            if (item.getItemId() == R.id.navigation_home) {
//                startActivity(new Intent(this, HomeActivity.class));
//                return true;
//            } else if (item.getItemId() == R.id.navigation_search) {
//                startActivity(new Intent(this, ExploreEventsActivity.class));
//                return true;
//            } else if (item.getItemId() == R.id.navigation_favorites) {
//                startActivity(new Intent(this, FavoritesActivity.class));
//                return true;
//            } else if (item.getItemId() == R.id.navigation_profile) {
//                navigateToProfile();
//                return true;
//            }
//            return false;
//        });
//    }
//
//    private void navigateToProfile() {
//        startActivity(new Intent(this, UserDashboardActivity.class));
//    }
//}




//package com.example.spotpassapp;
//
//import android.content.Intent;
//import android.os.Build;
//import android.os.Bundle;
//import android.widget.SearchView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.drawable.DrawableCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.spotpassapp.adapter.EventAdapter;
//import com.example.spotpassapp.model.Event;
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ExploreEventsActivity extends AppCompatActivity {
//
//    private List<Event> eventsList;
//    private EventAdapter eventAdapter;
//    private BottomNavigationView bottomNavigationView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_explore_events);
//
//        initializeViews();
//        setupRecyclerView();
//        setupSearchView();
//        setupBottomNavigation();
//    }
//
//    /**
//     * Initialize views and components.
//     */
//    private void initializeViews() {
//        eventsList = new ArrayList<>();
//        bottomNavigationView = findViewById(R.id.bottom_navigation);
//        populateSampleData();
//    }
//
//    /**
//     * Populate sample event data.
//     */
//    private void populateSampleData() {
//        eventsList.add(new Event("Concert Night", "Music festival", "Montreal", 50.0, "2024-11-20", "19:00", String.valueOf(R.drawable.splash_image)));
//        eventsList.add(new Event("Art Expo", "Modern art exhibit", "Quebec", 30.0, "2024-11-25", "10:00", String.valueOf(R.drawable.splash_image)));
//        eventsList.add(new Event("Tech Conference", "Technology insights", "Toronto", 100.0, "2024-12-01", "09:00", String.valueOf(R.drawable.splash_image)));
//        eventsList.add(new Event("Food Festival", "Gastronomic delight", "Vancouver", 25.0, "2024-12-05", "11:00", String.valueOf(R.drawable.splash_image)));
//    }
//
//    /**
//     * Setup RecyclerView and attach the EventAdapter.
//     */
//    private void setupRecyclerView() {
//        RecyclerView recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        eventAdapter = new EventAdapter(this, eventsList);
//        recyclerView.setAdapter(eventAdapter);
//    }
//
//    /**
//     * Setup SearchView to filter events.
//     */
//    private void setupSearchView() {
//        SearchView searchView = findViewById(R.id.searchView);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                eventAdapter.filter(query);
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                eventAdapter.filter(newText);
//                return true;
//            }
//        });
//    }
//
//    /**
//     * Setup BottomNavigationView for navigation.
//     */
//    private void setupBottomNavigation() {
//        bottomNavigationView.setOnItemSelectedListener(item -> {
//            int itemId = item.getItemId();
//            highlightSelectedItem(item);  // Highlight the selected item
//
//            if (itemId == R.id.navigation_home) {
//                startActivity(new Intent(this, HomeActivity.class));
//                return true;
//            } else if (itemId == R.id.navigation_search) {
//                startActivity(new Intent(this, ExploreEventsActivity.class));
//                return true;
//            } else if (itemId == R.id.navigation_favorites) {
//                startActivity(new Intent(this, FavoritesActivity.class));
//                return true;
//            } else if (itemId == R.id.navigation_profile) {
//                navigateToProfile();
//                return true;
//            }
//            return false;
//        });
//    }
//
//    /**
//     * Highlight the selected item by changing its icon color.
//     */
//    private void highlightSelectedItem(@NonNull android.view.MenuItem item) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            // For API level 26 and above, use setIconTintList to change the icon color
//            item.setIconTintList(getResources().getColorStateList(R.color.primaryColor, null)); // Set the icon color to primary color
//        } else {
//            // For lower API levels, use DrawableCompat to tint the icon
//            DrawableCompat.setTint(item.getIcon(), getResources().getColor(R.color.primaryColor)); // Set the icon color to primary color
//        }
//    }
//
//    /**
//     * Navigate to the User's Profile.
//     */
//    private void navigateToProfile() {
//        startActivity(new Intent(this, UserDashboardActivity.class));
//    }
//}




package com.example.spotpassapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.SearchView;
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
import java.util.List;

public class ExploreEventsActivity extends AppCompatActivity {

    private List<Event> eventsList;
    private EventAdapter eventAdapter;
    private BottomNavigationView bottomNavigationView;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_events);

        initializeViews();
        setupRecyclerView();
        setupSearchView();
        setupBottomNavigation();
        fetchEventsFromDatabase(); // Fetch events from Firebase
    }

    /**
     * Initialize views and components.
     */
    private void initializeViews() {
        eventsList = new ArrayList<>();
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        databaseRef = FirebaseDatabase.getInstance().getReference("events"); // Firebase database reference
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
     * Fetch events from Firebase Realtime Database.
     */
    private void fetchEventsFromDatabase() {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventsList.clear(); // Clear the list to avoid duplication
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    if (event != null) {
                        eventsList.add(event);
                    }
                }
                eventAdapter.notifyDataSetChanged(); // Update the adapter with new data
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ExploreEventsActivity.this, "Failed to fetch events.", Toast.LENGTH_SHORT).show();
            }
        });
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
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            highlightSelectedItem(item);  // Highlight the selected item

            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, HomeActivity.class));
                return true;
            } else if (itemId == R.id.navigation_search) {
                // Stay on ExploreEventsActivity
                return true;
            } else if (itemId == R.id.navigation_favorites) {
                startActivity(new Intent(this, FavoritesActivity.class));
                return true;
            } else if (itemId == R.id.navigation_profile) {
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
            item.setIconTintList(getResources().getColorStateList(R.color.primaryColor, null));
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