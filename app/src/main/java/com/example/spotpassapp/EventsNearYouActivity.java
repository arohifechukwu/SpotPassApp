package com.example.spotpassapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotpassapp.adapter.EventAdapter;
import com.example.spotpassapp.model.Event;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventsNearYouActivity extends AppCompatActivity {

    private RecyclerView eventsRecyclerView;
    private TextView emptyTextView;
    private EventAdapter eventsAdapter;
    private List<Event> eventsList;
    private DatabaseReference databaseRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_near_you);

        // Initialize views
        eventsRecyclerView = findViewById(R.id.eventsRecyclerView);
        emptyTextView = findViewById(R.id.emptyTextView);

        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventsList = new ArrayList<>();
        eventsAdapter = new EventAdapter(this, eventsList);
        eventsRecyclerView.setAdapter(eventsAdapter);

        // Initialize Firebase
        databaseRef = FirebaseDatabase.getInstance().getReference();

        // Get user city from intent
        String userCity = getIntent().getStringExtra("user_city");
        if (userCity == null || userCity.isEmpty()) {
            userCity = "Montreal"; // Default to Montreal if no city provided
        }

        // Fetch events for the user's city
        fetchNearbyEvents(userCity);

        // Setup Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        setupBottomNavigation(bottomNavigationView);
    }

    private void fetchNearbyEvents(String city) {
        databaseRef.child("events").child("events")
                .orderByChild("location")
                .equalTo(city)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        eventsList.clear();
                        for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                            Event event = eventSnapshot.getValue(Event.class);
                            if (event != null) {
                                eventsList.add(event);
                            }
                        }

                        if (eventsList.isEmpty()) {
                            emptyTextView.setVisibility(View.VISIBLE);
                            eventsRecyclerView.setVisibility(View.GONE);
                        } else {
                            emptyTextView.setVisibility(View.GONE);
                            eventsRecyclerView.setVisibility(View.VISIBLE);
                        }

                        eventsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(EventsNearYouActivity.this, "Failed to load events.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupBottomNavigation(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setSelectedItemId(R.id.navigation_search);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, HomeActivity.class));
                return true;
            } else if (itemId == R.id.navigation_search) {
                return true; // Stay on this activity
            } else if (itemId == R.id.navigation_favorites) {
                startActivity(new Intent(this, FavoritesActivity.class));
                return true;
            } else if (itemId == R.id.navigation_profile) {
                startActivity(new Intent(this, UserDashboardActivity.class));
                return true;
            }
            return false;
        });
    }
}