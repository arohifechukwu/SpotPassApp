package com.example.spotpassapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spotpassapp.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class EditUserActivity extends AppCompatActivity {

    private EditText firstNameEdit, lastNameEdit, emailEdit, phoneEdit, addressEdit;
    private Spinner roleSpinner;
    private Button saveChangesButton;
    private DatabaseReference databaseRef;
    private String userKey; // Stores the original key of the user entry in the database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        // Initialize views
        firstNameEdit = findViewById(R.id.firstNameEdit);
        lastNameEdit = findViewById(R.id.lastNameEdit);
        emailEdit = findViewById(R.id.emailEdit);
        phoneEdit = findViewById(R.id.phoneEdit);
        addressEdit = findViewById(R.id.addressEdit);
        roleSpinner = findViewById(R.id.roleSpinner);
        saveChangesButton = findViewById(R.id.saveChangesButton);

        databaseRef = FirebaseDatabase.getInstance().getReference("users");

        // Retrieve user data and key
        populateUserData();

        saveChangesButton.setOnClickListener(v -> saveChanges());
    }

    private void populateUserData() {
        // Retrieve user details and the original key
        String firstName = getIntent().getStringExtra("firstName");
        String lastName = getIntent().getStringExtra("lastName");
        String email = getIntent().getStringExtra("email");
        String phone = getIntent().getStringExtra("phone");
        String address = getIntent().getStringExtra("address");
        String role = getIntent().getStringExtra("role");

        // Retrieve and store the user's database key
        userKey = getIntent().getStringExtra("userKey");

        // Populate the form with user details
        firstNameEdit.setText(firstName);
        lastNameEdit.setText(lastName);
        emailEdit.setText(email);
        phoneEdit.setText(phone);
        addressEdit.setText(address);

        // Set up the role spinner
        setupRoleSpinner(role);
    }

    private void setupRoleSpinner(String role) {
        String[] roles = {"Admin", "Organizer", "User"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Arrays.asList(roles));
        roleSpinner.setAdapter(adapter);

        if (role != null) {
            int position = Arrays.asList(roles).indexOf(role);
            if (position >= 0) {
                roleSpinner.setSelection(position);
            }
        }
    }

    private void saveChanges() {
        String firstName = firstNameEdit.getText().toString().trim();
        String lastName = lastNameEdit.getText().toString().trim();
        String phone = phoneEdit.getText().toString().trim();
        String address = addressEdit.getText().toString().trim();
        String email = emailEdit.getText().toString().trim();
        String role = roleSpinner.getSelectedItem().toString();

        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName)) {
            showToast("Fill all required fields.");
            return;
        }

        if (userKey == null) {
            showToast("User key not found. Cannot update user.");
            return;
        }

        // Update the user in the database
        User updatedUser = new User(firstName, lastName, phone, address, email, role);

        databaseRef.child(userKey).setValue(updatedUser).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                showToast("User updated successfully!");
                finish();
            } else {
                showToast("Failed to update user.");
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(EditUserActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}