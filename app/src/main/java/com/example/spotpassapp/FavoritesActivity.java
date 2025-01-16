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

import com.example.spotpassapp.adapter.FavoritesAdapter;
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

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView favoritesRecyclerView;
    private TextView emptyTextView;
    private FavoritesAdapter favoritesAdapter;
    private List<Event> favoriteEvents;
    private DatabaseReference favoritesDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        // Initialize views
        favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView);
        emptyTextView = findViewById(R.id.emptyTextView);

        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        favoriteEvents = new ArrayList<>();
        favoritesAdapter = new FavoritesAdapter(this, favoriteEvents);
        favoritesRecyclerView.setAdapter(favoritesAdapter);

        // Initialize Firebase
        favoritesDatabase = FirebaseDatabase.getInstance().getReference("favorites");

        // Load favorite events from the database
        loadFavorites();

        // Setup Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        setupBottomNavigation(bottomNavigationView);
    }


    private void loadFavorites() {
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId != null) {
            favoritesDatabase.child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    favoriteEvents.clear();
                    for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                        Event event = eventSnapshot.getValue(Event.class);
                        if (event != null) {
                            event.setKey(eventSnapshot.getKey()); // Save key for deletion
                            favoriteEvents.add(event);
                        }
                    }

                    if (favoriteEvents.isEmpty()) {
                        showEmptyState(true);
                    } else {
                        showEmptyState(false);
                    }

                    favoritesAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(FavoritesActivity.this, "Failed to load favorites.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
        }
    }

    private void showEmptyState(boolean isEmpty) {
        if (isEmpty) {
            emptyTextView.setVisibility(View.VISIBLE);
            favoritesRecyclerView.setVisibility(View.GONE);
        } else {
            emptyTextView.setVisibility(View.GONE);
            favoritesRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void setupBottomNavigation(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setSelectedItemId(R.id.navigation_favorites);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, HomeActivity.class));
                return true;
            } else if (itemId == R.id.navigation_search) {
                startActivity(new Intent(this, ExploreEventsActivity.class));
                return true;
            } else if (itemId == R.id.navigation_favorites) {
                return true; // Stay on this activity
            } else if (itemId == R.id.navigation_profile) {
                startActivity(new Intent(this, UserDashboardActivity.class));
                return true;
            }
            return false;
        });
    }
}