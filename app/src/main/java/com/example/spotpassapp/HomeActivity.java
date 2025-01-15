package com.example.spotpassapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private static final int EDIT_LOCATION_REQUEST_CODE = 1001;
    private static final int SCROLL_INTERVAL = 2000; // 2 seconds
    private static final int MORE_EVENTS_INTERVAL = 3000; // 3 seconds

    private RecyclerView nearbyRecyclerView, upcomingRecyclerView, moreRecyclerView;
    private EventAdapter nearbyAdapter, upcomingAdapter, moreAdapter;
    private List<Event> nearbyEvents, upcomingEvents, moreEvents;
    private List<Event> moreEventPool;

    private TextView locationText, viewMoreNearby, viewMoreUpcoming;
    private ImageView profileIcon;
    private BottomNavigationView bottomNavigationView;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;

    private Handler autoScrollHandler = new Handler();
    private Handler moreEventsHandler = new Handler();

    private int currentEventIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference();

        // Initialize Views
        locationText = findViewById(R.id.locationText);
        profileIcon = findViewById(R.id.profileIcon);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        viewMoreNearby = findViewById(R.id.viewMoreNearby);
        viewMoreUpcoming = findViewById(R.id.viewMoreUpcoming);

        nearbyRecyclerView = findViewById(R.id.nearbyRecyclerView);
        upcomingRecyclerView = findViewById(R.id.upcomingRecyclerView);
        moreRecyclerView = findViewById(R.id.moreRecyclerView);

        setupBottomNavigation();
        loadUserData();

        // Initialize Adapters and Lists
        nearbyEvents = new ArrayList<>();
        upcomingEvents = new ArrayList<>();
        moreEvents = new ArrayList<>();
        moreEventPool = new ArrayList<>();

        nearbyAdapter = new EventAdapter(this, nearbyEvents);
        upcomingAdapter = new EventAdapter(this, upcomingEvents);
        moreAdapter = new EventAdapter(this, moreEvents);

        setupRecyclerViews();
        fetchNearbyEvents("Montreal");
        fetchUpcomingEvents();
        fetchMoreEvents();

        // View More Links

        // Set up "View More" for Nearby Events
        viewMoreNearby.setOnClickListener(v -> {
            Intent intent = new Intent(this, EventsNearYouActivity.class);
            intent.putExtra("user_city", locationText.getText().toString().replace("Your Location: ", "").trim());
            startActivity(intent);
        });

        // Set up "View More" for Upcoming Events
        viewMoreUpcoming.setOnClickListener(v -> {
            Intent intent = new Intent(this, UpcomingEventsActivity.class);
            startActivity(intent);
        });

        // Profile Icon
        profileIcon.setOnClickListener(v -> navigateToProfile());

        // Location Text
        locationText.setOnClickListener(v -> navigateToEditLocation());
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
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

    private void setupRecyclerViews() {
        // "Events Near You" with vertical scrolling
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        nearbyRecyclerView.setLayoutManager(verticalLayoutManager);
        nearbyRecyclerView.setAdapter(nearbyAdapter);

        // "Upcoming Events"
        upcomingRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        upcomingRecyclerView.setAdapter(upcomingAdapter);

        // "More Events"
        moreRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        moreRecyclerView.setAdapter(moreAdapter);
    }

    private void loadUserData() {
        String userId = mAuth.getCurrentUser().getUid();
        databaseRef.child("users").child(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                DataSnapshot snapshot = task.getResult();
                String location = snapshot.child("location").getValue(String.class);

                if (location != null && !location.isEmpty()) {
                    locationText.setText(String.format("Your Location: %s", location));
                    // Pass the location to fetchNearbyEvents
                    fetchNearbyEvents(location);
                } else {
                    locationText.setText("Your Location: Unknown");
                    fetchNearbyEvents("Montreal"); // Default location
                }

                String profileImageUrl = snapshot.child("profileImageUrl").getValue(String.class);
                if (!TextUtils.isEmpty(profileImageUrl)) {
                    Picasso.get().load(profileImageUrl).placeholder(R.drawable.ic_profile_placeholder).into(profileIcon);
                }
            } else {
                Toast.makeText(this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
                fetchNearbyEvents("Montreal"); // Default location
            }
        });
    }



    private void fetchNearbyEvents(String userCity) {
        if (TextUtils.isEmpty(userCity)) {
            userCity = "Montreal"; // Default to Montreal only if no input or saved location
        }

        locationText.setText(String.format("Your Location: %s", userCity));

        databaseRef.child("events").child("events")
                .orderByChild("location")
                .equalTo(userCity)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        nearbyEvents.clear();
                        for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                            Event event = eventSnapshot.getValue(Event.class);
                            if (event != null) {
                                nearbyEvents.add(event);
                            }
                        }

                        if (nearbyEvents.isEmpty()) {
                            Toast.makeText(HomeActivity.this, "No events found for this location.", Toast.LENGTH_SHORT).show();
                        }

                        nearbyAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(HomeActivity.this, "Failed to load events.", Toast.LENGTH_SHORT).show();
                    }
                });
    }




    private void fetchUpcomingEvents() {
        long currentTime = System.currentTimeMillis();
        long thirtyDaysLater = currentTime + (30L * 24 * 60 * 60 * 1000);

        databaseRef.child("events").child("events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                upcomingEvents.clear();
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    if (event != null && isWithinNext30Days(event.getDate(), currentTime, thirtyDaysLater)) {
                        upcomingEvents.add(event);
                    }
                }
                upcomingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, "Failed to load upcoming events.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchMoreEvents() {
        databaseRef.child("events").child("events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                moreEventPool.clear();
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    Event event = eventSnapshot.getValue(Event.class);
                    if (event != null) {
                        moreEventPool.add(event);
                    }
                }
                shuffleAndDisplayMoreEvents();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeActivity.this, "Failed to load more events.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void shuffleAndDisplayMoreEvents() {
        moreEventsHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Collections.shuffle(moreEventPool);
                moreEvents.clear();
                moreEvents.addAll(moreEventPool.subList(0, Math.min(moreEventPool.size(), 10)));
                moreAdapter.notifyDataSetChanged();
                moreEventsHandler.postDelayed(this, MORE_EVENTS_INTERVAL);
            }
        }, MORE_EVENTS_INTERVAL);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_LOCATION_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String newLocation = data.getStringExtra("user_city");
            if (!TextUtils.isEmpty(newLocation)) {
                locationText.setText(String.format("Your Location: %s", newLocation));
                saveUserLocation(newLocation);
                fetchNearbyEvents(newLocation);
            }
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
}

