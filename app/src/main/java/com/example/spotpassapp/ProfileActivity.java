package com.example.spotpassapp;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;

    private TextView emailTextView;
    private EditText firstNameEditText, lastNameEditText, phoneEditText, addressEditText;
    private MaterialButton saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

        emailTextView = findViewById(R.id.emailTextView);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        addressEditText = findViewById(R.id.addressEditText);
        saveButton = findViewById(R.id.saveButton);

        loadProfileData();

        saveButton.setOnClickListener(v -> updateProfileData());
    }

    private void loadProfileData() {
        databaseReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DataSnapshot dataSnapshot = task.getResult();
                emailTextView.setText(dataSnapshot.child("email").getValue(String.class));
                firstNameEditText.setText(dataSnapshot.child("firstName").getValue(String.class));
                lastNameEditText.setText(dataSnapshot.child("lastName").getValue(String.class));
                phoneEditText.setText(dataSnapshot.child("phone").getValue(String.class));
                addressEditText.setText(dataSnapshot.child("address").getValue(String.class));
            } else {
                Toast.makeText(this, "Failed to load profile data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProfileData() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "First Name, Last Name, and Phone are mandatory.", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.child("firstName").setValue(firstName);
        databaseReference.child("lastName").setValue(lastName);
        databaseReference.child("phone").setValue(phone);
        databaseReference.child("address").setValue(address).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to update profile.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}