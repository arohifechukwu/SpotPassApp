package com.example.spotpassapp;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.spotpassapp.adapter.EventAdapter;
import com.example.spotpassapp.model.Event;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private List<Event> eventsList;
    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Sample data setup
        eventsList = new ArrayList<>();
        eventsList.add(new Event("Concert Night", "Music festival", "Montreal", 50.0, "2024-11-20", "19:00", "Music"));
        eventsList.add(new Event("Art Expo", "Modern art exhibit", "Quebec", 30.0, "2024-11-25", "10:00", "Art"));

        eventAdapter = new EventAdapter(this, eventsList);
        recyclerView.setAdapter(eventAdapter);

        // Search filter logic
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
}