package com.example.spotpassapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private static final int EDIT_LOCATION_REQUEST_CODE = 1001;

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList;
    private TextView locationText;
    private ImageView profileIcon;
    private BottomNavigationView bottomNavigationView;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference();

        // Initialize Views
        locationText = findViewById(R.id.locationText);
        recyclerView = findViewById(R.id.recyclerView);
        profileIcon = findViewById(R.id.profileIcon);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        setupBottomNavigation();
        loadUserData();
        fetchEventsFromDatabase();

        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(this, eventList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(eventAdapter);

        // Set Click Listeners
        profileIcon.setOnClickListener(v -> navigateToProfile());
        locationText.setOnClickListener(v -> navigateToEditLocation());
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();  // Avoid multiple method calls
            if (itemId == R.id.navigation_home) {
                // Stay on HomeActivity
                return true;
            } else if (itemId == R.id.navigation_search) {
                startActivity(new Intent(this, ExploreEventsActivity.class));
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

    private void loadUserData() {
        String userId = mAuth.getCurrentUser().getUid();
        databaseRef.child("users").child(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                DataSnapshot snapshot = task.getResult();

                // Load Location
                String location = snapshot.child("location").getValue(String.class);
                locationText.setText(String.format("Your Location: %s", location != null ? location : "Unknown"));

                // Load Profile Picture
                String profileImageUrl = snapshot.child("profileImageUrl").getValue(String.class);
                if (!TextUtils.isEmpty(profileImageUrl)) {
                    Picasso.get()
                            .load(profileImageUrl)
                            .placeholder(R.drawable.ic_profile_placeholder)
                            .into(profileIcon);
                }
            } else {
                Toast.makeText(this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchEventsFromDatabase() {
        databaseRef.child("events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventList.clear(); // Clear the current list to avoid duplication
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    if (event != null) {
                        eventList.add(event);
                    }
                }
                eventAdapter.notifyDataSetChanged(); // Notify adapter about data changes
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, "Failed to fetch events.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToProfile() {
        String userId = mAuth.getCurrentUser().getUid();
        databaseRef.child("users").child(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                DataSnapshot snapshot = task.getResult();
                String role = snapshot.child("role").getValue(String.class);

                Intent intent = "Admin".equalsIgnoreCase(role)
                        ? new Intent(HomeActivity.this, AdminDashboardActivity.class)
                        : new Intent(HomeActivity.this, UserDashboardActivity.class);

                startActivity(intent);
            } else {
                Toast.makeText(this, "User data not found.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToEditLocation() {
        Intent intent = new Intent(HomeActivity.this, EditLocationActivity.class);
        startActivityForResult(intent, EDIT_LOCATION_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_LOCATION_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String newLocation = data.getStringExtra("selected_location");
            locationText.setText(String.format("Your Location: %s", newLocation));
            saveUserLocation(newLocation);
        }
    }

    private void saveUserLocation(String location) {
        String userId = mAuth.getCurrentUser().getUid();
        databaseRef.child("users").child(userId).child("location").setValue(location)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Location updated!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to update location.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}