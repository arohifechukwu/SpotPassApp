package com.example.spotpassapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.spotpassapp.model.Event;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ModifyEventActivity extends AppCompatActivity {

    private EditText searchEventId, eventTitle, eventDescription, eventLocation, eventDate, eventTime, eventPrice, eventImageUrl;
    private Button searchButton, updateEventButton, deleteEventButton, createEventButton;
    private DatabaseReference databaseRef;

    private String currentEventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_event);

        databaseRef = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://spotpassapp-default-rtdb.firebaseio.com/events/events");

        searchEventId = findViewById(R.id.searchEventTitle);
        eventTitle = findViewById(R.id.eventTitle);
        eventDescription = findViewById(R.id.eventDescription);
        eventLocation = findViewById(R.id.eventLocation);
        eventDate = findViewById(R.id.eventDate);
        eventTime = findViewById(R.id.eventTime);
        eventPrice = findViewById(R.id.eventPrice);
        eventImageUrl = findViewById(R.id.eventImageUrl);
        searchButton = findViewById(R.id.searchButton);
        updateEventButton = findViewById(R.id.updateEventButton);
        deleteEventButton = findViewById(R.id.deleteEventButton);
        createEventButton = findViewById(R.id.createEventButton);

        searchButton.setOnClickListener(v -> searchEvent());
        updateEventButton.setOnClickListener(v -> updateEvent());
        deleteEventButton.setOnClickListener(v -> deleteEvent());
        createEventButton.setOnClickListener(v -> createNewEvent());

        // Check for intent extras to populate fields
        if (getIntent() != null && getIntent().hasExtra("event")) {
            Event event = (Event) getIntent().getSerializableExtra("event");
            populateFields(event);
        }
    }

    private void createNewEvent() {
        String title = eventTitle.getText().toString().trim();
        String description = eventDescription.getText().toString().trim();
        String location = eventLocation.getText().toString().trim();
        String date = eventDate.getText().toString().trim();
        String time = eventTime.getText().toString().trim();
        String priceStr = eventPrice.getText().toString().trim();
        String imageUrl = eventImageUrl.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || TextUtils.isEmpty(location) ||
                TextUtils.isEmpty(date) || TextUtils.isEmpty(time) || TextUtils.isEmpty(priceStr) || TextUtils.isEmpty(imageUrl)) {
            Toast.makeText(this, "Please fill out all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);
        String eventId = databaseRef.push().getKey();
        Event newEvent = new Event(eventId, title, description, location, date, time, price, imageUrl);

        databaseRef.child(eventId).setValue(newEvent).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Event created successfully!", Toast.LENGTH_SHORT).show();
                clearFields();
            } else {
                Toast.makeText(this, "Failed to create event.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchEvent() {
        String title = searchEventId.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "Enter Event Title.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(ModifyEventActivity.this, SearchActivity.class);
        intent.putExtra("searchQuery", title);
        startActivity(intent);
    }

    private void updateEvent() {
        if (currentEventId == null) {
            Toast.makeText(this, "Search for an event first.", Toast.LENGTH_SHORT).show();
            return;
        }

        Event updatedEvent = new Event(currentEventId,
                eventTitle.getText().toString().trim(),
                eventDescription.getText().toString().trim(),
                eventLocation.getText().toString().trim(),
                eventDate.getText().toString().trim(),
                eventTime.getText().toString().trim(),
                Double.parseDouble(eventPrice.getText().toString().trim()),
                eventImageUrl.getText().toString().trim());

        databaseRef.child(currentEventId).setValue(updatedEvent).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Event updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to update event.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteEvent() {
        if (currentEventId == null) {
            Toast.makeText(this, "Search for an event first.", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseRef.child(currentEventId).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Event deleted successfully!", Toast.LENGTH_SHORT).show();
                clearFields();
            } else {
                Toast.makeText(this, "Failed to delete event.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearFields() {
        searchEventId.setText("");
        eventTitle.setText("");
        eventDescription.setText("");
        eventLocation.setText("");
        eventDate.setText("");
        eventTime.setText("");
        eventPrice.setText("");
        eventImageUrl.setText("");
        currentEventId = null;
    }

    private void populateFields(Event event) {
        currentEventId = event.getKey();
        eventTitle.setText(event.getTitle());
        eventDescription.setText(event.getDescription());
        eventLocation.setText(event.getLocation());
        eventDate.setText(event.getDate());
        eventTime.setText(event.getTime());
        eventPrice.setText(String.valueOf(event.getPrice()));
        eventImageUrl.setText(event.getImageUrl());
    }
}