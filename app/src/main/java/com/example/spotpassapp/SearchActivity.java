package com.example.spotpassapp;

import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotpassapp.adapter.EventAdapter;
import com.example.spotpassapp.model.Event;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private List<Event> eventsList;
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private SearchView searchView;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Initialize Firebase reference
        databaseRef = FirebaseDatabase.getInstance().getReference("events");

        // Initialize views
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize event list and adapter
        eventsList = new ArrayList<>();
        eventAdapter = new EventAdapter(this, eventsList);
        recyclerView.setAdapter(eventAdapter);

        // Fetch events from the database
        fetchEventsFromDatabase();

        // Set up search filter logic
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
                Toast.makeText(SearchActivity.this, "Failed to fetch events.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}