package com.example.spotpassapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotpassapp.adapter.EventAdapter;
import com.example.spotpassapp.model.Event;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList;
    private TextView locationText;
    private ImageView profileIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize Views
        locationText = findViewById(R.id.locationText);
        recyclerView = findViewById(R.id.recyclerView);
        profileIcon = findViewById(R.id.profileIcon);

        // Set Profile Icon Click Listener
        profileIcon.setOnClickListener(v -> navigateToProfile());

        // Get user location from intent
        Intent intent = getIntent();
        String userLocation = intent.getStringExtra("user_location");
        locationText.setText(String.format("Your Location: %s", userLocation != null ? userLocation : "Unknown"));

        // Populate the event list
        populateEventList();

        // Set up RecyclerView
        eventAdapter = new EventAdapter(this, eventList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(eventAdapter);
    }

    private void populateEventList() {
        eventList = new ArrayList<>();
        eventList.add(new Event("Concert Night", "Music festival", "Montreal", 50.0, "2024-11-20", "19:00", String.valueOf(R.drawable.circle_background)));
        eventList.add(new Event("Art Expo", "Modern art exhibit", "Quebec", 30.0, "2024-11-25", "10:00", String.valueOf(R.drawable.circle_background)));
        eventList.add(new Event("Tech Conference", "Technology insights", "Toronto", 100.0, "2024-12-01", "09:00", String.valueOf(R.drawable.circle_background)));
        eventList.add(new Event("Food Festival", "Gastronomic delight", "Vancouver", 25.0, "2024-12-05", "11:00", String.valueOf(R.drawable.circle_background)));
        eventList.add(new Event("Marathon Run", "Charity marathon", "Calgary", 15.0, "2024-12-10", "07:00", String.valueOf(R.drawable.circle_background)));
        eventList.add(new Event("Film Screening", "Independent films", "Ottawa", 20.0, "2024-12-15", "18:00", String.valueOf(R.drawable.circle_background)));
        eventList.add(new Event("Book Fair", "Literature expo", "Halifax", 10.0, "2024-12-20", "10:00", String.valueOf(R.drawable.circle_background)));
        eventList.add(new Event("Christmas Market", "Festive shopping", "Montreal", 0.0, "2024-12-25", "10:00", String.valueOf(R.drawable.circle_background)));
        eventList.add(new Event("New Year's Eve", "Countdown party", "Toronto", 75.0, "2024-12-31", "22:00", String.valueOf(R.drawable.circle_background)));
        eventList.add(new Event("Music Workshop", "Learn instruments", "Winnipeg", 40.0, "2025-01-05", "14:00", String.valueOf(R.drawable.circle_background)));
    }

    private void navigateToProfile() {
        Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
}