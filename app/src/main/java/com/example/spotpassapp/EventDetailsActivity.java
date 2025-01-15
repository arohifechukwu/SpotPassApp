package com.example.spotpassapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.spotpassapp.model.Event;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EventDetailsActivity extends AppCompatActivity {

    private TextView titleTextView, descriptionTextView, locationTextView, priceTextView, dateTextView, timeTextView;
    private ImageView eventImageView;
    private Button bookTicketButton, addToCartButton, addToFavoritesButton;

    private DatabaseReference favoritesDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        // Initialize Firebase database reference for favorites
        favoritesDatabase = FirebaseDatabase.getInstance().getReference("favorites");

        // Initialize views
        eventImageView = findViewById(R.id.eventImageView);
        titleTextView = findViewById(R.id.eventTitle);
        descriptionTextView = findViewById(R.id.eventDescription);
        locationTextView = findViewById(R.id.eventLocation);
        priceTextView = findViewById(R.id.eventPrice);
        dateTextView = findViewById(R.id.eventDate);
        timeTextView = findViewById(R.id.eventTime);
        bookTicketButton = findViewById(R.id.bookTicketButton);
        addToCartButton = findViewById(R.id.addToCartButton);
        addToFavoritesButton = findViewById(R.id.addToFavoritesButton);

        // Initialize Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        setupBottomNavigation(bottomNavigationView);

        // Get event details from intent
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            String description = intent.getStringExtra("description");
            String location = intent.getStringExtra("location");
            double price = intent.getDoubleExtra("price", 0.0); // Ensure price is a double
            String date = intent.getStringExtra("date");
            String time = intent.getStringExtra("time");
            String imageUrl = intent.getStringExtra("imageUrl");

            // Set event details
            titleTextView.setText(title);
            descriptionTextView.setText(description);
            locationTextView.setText(location);
            priceTextView.setText(String.format("$%.2f", price)); // Format price for display
            dateTextView.setText(date);
            timeTextView.setText(time);
            Glide.with(this).load(imageUrl).into(eventImageView);

            // Add event to favorites
            addToFavoritesButton.setOnClickListener(v -> {
                Event event = new Event(title, description, location, date, time, price, imageUrl);
                favoritesDatabase.push().setValue(event)
                        .addOnCompleteListener(task -> Toast.makeText(this, "Event added to Favorites!", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(this, "Failed to add to Favorites.", Toast.LENGTH_SHORT).show());
            });

            // Pass details to CheckoutActivity
            bookTicketButton.setOnClickListener(v -> {
                Intent checkoutIntent = new Intent(EventDetailsActivity.this, CheckoutActivity.class);
                checkoutIntent.putExtra("title", title);
                checkoutIntent.putExtra("description", description);
                checkoutIntent.putExtra("location", location);
                checkoutIntent.putExtra("price", price);
                checkoutIntent.putExtra("date", date);
                checkoutIntent.putExtra("time", time);
                checkoutIntent.putExtra("imageUrl", imageUrl);
                startActivity(checkoutIntent);
            });
        }
    }

    private void setupBottomNavigation(BottomNavigationView bottomNavigationView) {
        // Get the source activity from intent
        String sourceActivity = getIntent().getStringExtra("sourceActivity");

        // Highlight the correct tab based on the source activity
        if ("HomeActivity".equals(sourceActivity)) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        } else if ("ExploreEventsActivity".equals(sourceActivity)) {
            bottomNavigationView.setSelectedItemId(R.id.navigation_search);
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, HomeActivity.class));
                return true;
            } else if (itemId == R.id.navigation_search) {
                startActivity(new Intent(this, ExploreEventsActivity.class));
                return true;
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