package com.example.spotpassapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.spotpassapp.R;

public class EventDetailsActivity extends AppCompatActivity {

    private TextView titleTextView, descriptionTextView, locationTextView, priceTextView, dateTextView, timeTextView;
    private ImageView eventImageView;
    private Button bookTicketButton, addToCartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        // Initialize views
        titleTextView = findViewById(R.id.eventTitle);
        descriptionTextView = findViewById(R.id.eventDescription);
        locationTextView = findViewById(R.id.eventLocation);
        priceTextView = findViewById(R.id.eventPrice);
        dateTextView = findViewById(R.id.eventDate);
        timeTextView = findViewById(R.id.eventTime);
        eventImageView = findViewById(R.id.eventImageView); // ImageView for event image
        bookTicketButton = findViewById(R.id.bookTicketButton);
        addToCartButton = findViewById(R.id.addToCartButton);

        // Get event details from intent
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            String description = intent.getStringExtra("description");
            String location = intent.getStringExtra("location");
            double price = intent.getDoubleExtra("price", 0.0);
            String date = intent.getStringExtra("date");
            String time = intent.getStringExtra("time");
            String imageUrl = intent.getStringExtra("imageUrl"); // Image URL passed from EventAdapter

            // Set event details
            titleTextView.setText(title);
            descriptionTextView.setText(description);
            locationTextView.setText(location);
            priceTextView.setText(String.format("$%.2f", price));
            dateTextView.setText(date);
            timeTextView.setText(time);

            // Load the event image using Glide
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(this)
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_event_placeholder) // Default placeholder image
                        .into(eventImageView);
            }
        }

        // Navigate to CheckoutActivity
        bookTicketButton.setOnClickListener(v -> {
            Intent checkoutIntent = new Intent(EventDetailsActivity.this, CheckoutActivity.class);
            checkoutIntent.putExtra("title", titleTextView.getText().toString());
            startActivity(checkoutIntent);
        });

        // Navigate to CartActivity
        addToCartButton.setOnClickListener(v -> {
            Intent cartIntent = new Intent(EventDetailsActivity.this, CartActivity.class);
            cartIntent.putExtra("title", titleTextView.getText().toString());
            startActivity(cartIntent);
        });
    }
}