package com.example.spotpassapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spotpassapp.model.Event;

public class EventDetailsActivity extends AppCompatActivity {

    private TextView titleTextView, descriptionTextView, locationTextView, priceTextView, dateTextView, timeTextView;
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

            // Set event details
            titleTextView.setText(title);
            descriptionTextView.setText(description);
            locationTextView.setText(location);
            priceTextView.setText(String.format("$%.2f", price));
            dateTextView.setText(date);
            timeTextView.setText(time);
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