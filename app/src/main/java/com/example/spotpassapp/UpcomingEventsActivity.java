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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UpcomingEventsActivity extends AppCompatActivity {

    private RecyclerView eventsRecyclerView;
    private TextView emptyTextView;
    private EventAdapter eventsAdapter;
    private List<Event> eventsList;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_events);

        // Initialize views
        eventsRecyclerView = findViewById(R.id.eventsRecyclerView);
        emptyTextView = findViewById(R.id.emptyTextView);

        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventsList = new ArrayList<>();
        eventsAdapter = new EventAdapter(this, eventsList);
        eventsRecyclerView.setAdapter(eventsAdapter);

        // Initialize Firebase
        databaseRef = FirebaseDatabase.getInstance().getReference();

        // Fetch upcoming events
        fetchUpcomingEvents();

        // Setup Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        setupBottomNavigation(bottomNavigationView);
    }

    private void fetchUpcomingEvents() {
        long currentTime = System.currentTimeMillis();
        long thirtyDaysLater = currentTime + (30L * 24 * 60 * 60 * 1000); // 30 days in milliseconds

        databaseRef.child("events").child("events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventsList.clear();
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    if (event != null && isWithinNext30Days(event.getDate(), currentTime, thirtyDaysLater)) {
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
                Toast.makeText(UpcomingEventsActivity.this, "Failed to load events.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isWithinNext30Days(String date, long currentTime, long thirtyDaysLater) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            long eventDate = dateFormat.parse(date).getTime();
            return eventDate >= currentTime && eventDate <= thirtyDaysLater;
        } catch (Exception e) {
            return false;
        }
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