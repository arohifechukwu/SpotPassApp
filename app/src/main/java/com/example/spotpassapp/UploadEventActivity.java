package com.example.spotpassapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;

public class UploadEventActivity extends AppCompatActivity {

    private EditText eventTitle, eventDescription, eventLocation, eventDate, eventTime, eventPrice, eventImageUrl;
    private Button uploadEventButton;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_event);

        // Initialize Views
        eventTitle = findViewById(R.id.eventTitle);
        eventDescription = findViewById(R.id.eventDescription);
        eventLocation = findViewById(R.id.eventLocation);
        eventDate = findViewById(R.id.eventDate);
        eventTime = findViewById(R.id.eventTime);
        eventPrice = findViewById(R.id.eventPrice);
        eventImageUrl = findViewById(R.id.eventImageUrl);
        uploadEventButton = findViewById(R.id.uploadEventButton);

        // Initialize Firebase
        databaseRef = FirebaseDatabase.getInstance().getReference("events");

        uploadEventButton.setOnClickListener(v -> uploadEvent());
    }

    private void uploadEvent() {
        String title = eventTitle.getText().toString().trim();
        String description = eventDescription.getText().toString().trim();
        String location = eventLocation.getText().toString().trim();
        String date = eventDate.getText().toString().trim();
        String time = eventTime.getText().toString().trim();
        String price = eventPrice.getText().toString().trim();
        String imageUrl = eventImageUrl.getText().toString().trim();

        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) || TextUtils.isEmpty(location) ||
                TextUtils.isEmpty(date) || TextUtils.isEmpty(time) || TextUtils.isEmpty(price) || TextUtils.isEmpty(imageUrl)) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        String eventId = databaseRef.push().getKey();
        HashMap<String, String> eventMap = new HashMap<>();
        eventMap.put("title", title);
        eventMap.put("description", description);
        eventMap.put("location", location);
        eventMap.put("date", date);
        eventMap.put("time", time);
        eventMap.put("price", price);
        eventMap.put("imageUrl", imageUrl);

        databaseRef.child(eventId).setValue(eventMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Event uploaded successfully!", Toast.LENGTH_SHORT).show();
                clearFields();
            } else {
                Toast.makeText(this, "Failed to upload event.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearFields() {
        eventTitle.setText("");
        eventDescription.setText("");
        eventLocation.setText("");
        eventDate.setText("");
        eventTime.setText("");
        eventPrice.setText("");
        eventImageUrl.setText("");
    }
}