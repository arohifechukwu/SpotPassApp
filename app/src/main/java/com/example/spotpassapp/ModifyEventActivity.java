package com.example.spotpassapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
public class ModifyEventActivity extends AppCompatActivity {

    private EditText searchEventId, eventTitle, eventDescription, eventLocation, eventDate, eventTime, eventPrice, eventImageUrl;
    private Button searchButton, updateEventButton, deleteEventButton, createEventButton;
    private DatabaseReference databaseRef;

    // Variable to store the event ID of the fetched event
    private String currentEventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_event);

        // Initialize Firebase
        databaseRef = FirebaseDatabase.getInstance().getReference("events");

        // Initialize Views
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

        // Set Button Listeners
        searchButton.setOnClickListener(v -> searchEvent());
        updateEventButton.setOnClickListener(v -> updateEvent());
        deleteEventButton.setOnClickListener(v -> deleteEvent());
        createEventButton.setOnClickListener(v -> createNewEvent());
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

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid price format.", Toast.LENGTH_SHORT).show();
            return;
        }

        String eventId = databaseRef.push().getKey(); // Generate a unique ID
        if (eventId != null) {
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("title", title);
            eventData.put("description", description);
            eventData.put("location", location);
            eventData.put("date", date);
            eventData.put("time", time);
            eventData.put("price", price);
            eventData.put("imageUrl", imageUrl);

            databaseRef.child(eventId).setValue(eventData).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Event created successfully!", Toast.LENGTH_SHORT).show();
                    clearFields();
                } else {
                    Toast.makeText(this, "Failed to create event.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void searchEvent() {
        String title = searchEventId.getText().toString().trim(); // Use searchEventId for title search
        if (TextUtils.isEmpty(title)) {
            Toast.makeText(this, "Enter Event Title.", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseRef.orderByChild("title").equalTo(title).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                        currentEventId = eventSnapshot.getKey(); // Store the event ID
                        eventTitle.setText(eventSnapshot.child("title").getValue(String.class));
                        eventDescription.setText(eventSnapshot.child("description").getValue(String.class));
                        eventLocation.setText(eventSnapshot.child("location").getValue(String.class));
                        eventDate.setText(eventSnapshot.child("date").getValue(String.class));
                        eventTime.setText(eventSnapshot.child("time").getValue(String.class));
                        eventPrice.setText(String.valueOf(eventSnapshot.child("price").getValue(Double.class)));
                        eventImageUrl.setText(eventSnapshot.child("imageUrl").getValue(String.class));
                        break; // Exit loop after first match
                    }
                } else {
                    Toast.makeText(ModifyEventActivity.this, "Event not found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ModifyEventActivity.this, "Failed to fetch event.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateEvent() {
        if (currentEventId == null) {
            Toast.makeText(this, "Search for an event first.", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> updates = new HashMap<>();
        updates.put("title", eventTitle.getText().toString().trim());
        updates.put("description", eventDescription.getText().toString().trim());
        updates.put("location", eventLocation.getText().toString().trim());
        updates.put("date", eventDate.getText().toString().trim());
        updates.put("time", eventTime.getText().toString().trim());
        updates.put("price", Double.parseDouble(eventPrice.getText().toString().trim()));
        updates.put("imageUrl", eventImageUrl.getText().toString().trim());

        databaseRef.child(currentEventId).updateChildren(updates).addOnCompleteListener(task -> {
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
        currentEventId = null; // Reset the current event ID
    }
}