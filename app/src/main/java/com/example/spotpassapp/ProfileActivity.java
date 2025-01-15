package com.example.spotpassapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private static final int IMAGE_PICK_CODE = 1000;

    private TextInputEditText firstNameEditText, lastNameEditText, phoneEditText, addressEditText;
    private MaterialButton saveButton, homeButton, exploreEventsButton, logoutButton;
    private ImageView profileImage;
    private TextView emailTextView;

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private StorageReference storageRef;

    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, AuthActivity.class));
            finish();
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        storageRef = FirebaseStorage.getInstance().getReference("profile_pictures").child(userId);

        initializeViews();
        loadUserProfile();

        // Setup Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        setupBottomNavigation(bottomNavigationView);

        // Click Listeners
        profileImage.setOnClickListener(v -> openImageSourceDialog());
        saveButton.setOnClickListener(v -> saveUserProfile());
        homeButton.setOnClickListener(v -> navigateTo(HomeActivity.class));
        exploreEventsButton.setOnClickListener(v -> navigateTo(ExploreEventsActivity.class));
        logoutButton.setOnClickListener(v -> logoutUser());
    }

    private void setupBottomNavigation(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setSelectedItemId(R.id.navigation_profile); // Highlight current tab
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, HomeActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.navigation_search) {
                startActivity(new Intent(this, ExploreEventsActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.navigation_favorites) {
                startActivity(new Intent(this, FavoritesActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.navigation_profile) {
                return true; // Stay on this activity
            }
            return false;
        });
    }

    private void initializeViews() {
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        addressEditText = findViewById(R.id.addressEditText);
        emailTextView = findViewById(R.id.emailTextView);
        profileImage = findViewById(R.id.profileImage);

        saveButton = findViewById(R.id.saveButton);
        homeButton = findViewById(R.id.homeButton);
        exploreEventsButton = findViewById(R.id.exploreEventsButton);
        logoutButton = findViewById(R.id.logoutButton);
    }

    private void loadUserProfile() {
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                DataSnapshot snapshot = task.getResult();

                String email = snapshot.child("email").getValue(String.class);
                String firstName = snapshot.child("firstName").getValue(String.class);
                String lastName = snapshot.child("lastName").getValue(String.class);
                String phone = snapshot.child("phone").getValue(String.class);
                String address = snapshot.child("address").getValue(String.class);
                String profileImageUrl = snapshot.child("profileImageUrl").getValue(String.class);

                emailTextView.setText(String.format("Email: %s", email));
                firstNameEditText.setText(firstName);
                lastNameEditText.setText(lastName);
                phoneEditText.setText(phone);
                addressEditText.setText(address);

                if (!TextUtils.isEmpty(profileImageUrl)) {
                    Picasso.get()
                            .load(profileImageUrl)
                            .placeholder(R.drawable.ic_profile_placeholder)
                            .into(profileImage);
                }
            } else {
                Toast.makeText(this, "Failed to load user data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveUserProfile() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();

        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "All fields except address are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> updates = new HashMap<>();
        updates.put("firstName", firstName);
        updates.put("lastName", lastName);
        updates.put("phone", phone);
        updates.put("address", address);

        userRef.updateChildren(updates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to update profile.", Toast.LENGTH_SHORT).show();
            }
        });

        if (imageUri != null) {
            uploadProfileImage();
        }
    }

    private void openImageSourceDialog() {
        String[] options = {"Upload from Device", "Enter Image URL", "Reset Profile Picture"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Image Source");
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0:
                    openImagePicker();
                    break;
                case 1:
                    promptForImageUrl();
                    break;
                case 2:
                    resetProfileImage();
                    break;
            }
        });
        builder.show();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    private void promptForImageUrl() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Image URL");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_URI);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String imageUrl = input.getText().toString().trim();
            if (!TextUtils.isEmpty(imageUrl)) {
                Picasso.get().load(imageUrl).into(profileImage);
                saveImageUrlToFirebase(imageUrl);
            } else {
                Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void saveImageUrlToFirebase(String imageUrl) {
        userRef.child("profileImageUrl").setValue(imageUrl).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Profile picture updated!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to update profile picture.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void resetProfileImage() {
        profileImage.setImageResource(R.drawable.ic_profile_placeholder);
        userRef.child("profileImageUrl").removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Profile picture reset successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to reset profile picture.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            Picasso.get().load(imageUri).into(profileImage);
            uploadProfileImage();
        }
    }

    private void uploadProfileImage() {
        storageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot ->
                storageRef.getDownloadUrl().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        saveImageUrlToFirebase(task.getResult().toString());
                    }
                })).addOnFailureListener(e ->
                Toast.makeText(this, "Failed to upload image.", Toast.LENGTH_SHORT).show());
    }

    private void navigateTo(Class<?> destination) {
        startActivity(new Intent(this, destination));
        finish();
    }

    private void logoutUser() {
        mAuth.signOut();
        startActivity(new Intent(this, AuthActivity.class));
        finish();
    }
}




