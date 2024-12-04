package com.example.spotpassapp;

import android.content.Intent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        locationText = findViewById(R.id.locationText);
        recyclerView = findViewById(R.id.recyclerView);

        // Get user location from intent
        Intent intent = getIntent();
        double latitude = intent.getDoubleExtra("user_latitude", 0);
        double longitude = intent.getDoubleExtra("user_longitude", 0);
        locationText.setText(String.format("Your Location: Lat %.2f, Lon %.2f", latitude, longitude));

        // Sample data for events
        eventList = new ArrayList<>();
        eventList.add(new Event("Concert Night", "Music festival", "Montreal", 50.0, "2024-11-20", "19:00", String.valueOf(R.drawable.splash_image)));
        eventList.add(new Event("Art Expo", "Modern art exhibit", "Quebec", 30.0, "2024-11-25", "10:00", String.valueOf(R.drawable.splash_image)));

        eventAdapter = new EventAdapter(this, eventList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(eventAdapter);
    }
}