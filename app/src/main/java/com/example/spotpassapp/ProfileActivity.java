package com.example.spotpassapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private ImageView profileImage;
    private TextView emailTextView;
    private TextInputEditText firstNameEditText, lastNameEditText, phoneEditText, addressEditText;
    private MaterialButton saveButton, homeButton, logoutButton, exploreEventsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        storageReference = FirebaseStorage.getInstance().getReference("profile_images/" + userId + ".jpg");

        profileImage = findViewById(R.id.profileImage);
        emailTextView = findViewById(R.id.emailTextView);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        addressEditText = findViewById(R.id.addressEditText);
        saveButton = findViewById(R.id.saveButton);
        homeButton = findViewById(R.id.homeButton);
        logoutButton = findViewById(R.id.logoutButton);
        exploreEventsButton = findViewById(R.id.exploreEventsButton);

        profileImage.setOnClickListener(v -> openImagePicker());
        saveButton.setOnClickListener(v -> updateProfileData());
        homeButton.setOnClickListener(v -> navigateToHome());
        logoutButton.setOnClickListener(v -> logout());
        exploreEventsButton.setOnClickListener(v -> navigateToExploreEvents());

        loadProfileData();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void navigateToHome() {
        Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void logout() {
        mAuth.signOut();
        Intent intent = new Intent(ProfileActivity.this, AuthActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToExploreEvents() {
        Intent intent = new Intent(ProfileActivity.this, ExploreEventsActivity.class);
        startActivity(intent);
    }

    private void loadProfileData() {
        databaseReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                DataSnapshot dataSnapshot = task.getResult();
                emailTextView.setText(mAuth.getCurrentUser().getEmail());
                firstNameEditText.setText(dataSnapshot.child("firstName").getValue(String.class));
                lastNameEditText.setText(dataSnapshot.child("lastName").getValue(String.class));
                phoneEditText.setText(dataSnapshot.child("phone").getValue(String.class));
                addressEditText.setText(dataSnapshot.child("address").getValue(String.class));

                String profileImageUrl = dataSnapshot.child("profileImageUrl").getValue(String.class);
                if (profileImageUrl != null) {
                    Glide.with(this).load(profileImageUrl).circleCrop().into(profileImage);
                }
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
        databaseReference.child("address").setValue(address)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Failed to update profile.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}