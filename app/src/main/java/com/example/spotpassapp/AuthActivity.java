package com.example.spotpassapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AuthActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;
    private TextInputEditText emailEditText, passwordEditText;
    private Spinner roleSpinnerAuth;
    private MaterialButton signInButton;
    private TextView forgotPasswordLink, signUpLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("users");

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        roleSpinnerAuth = findViewById(R.id.roleSpinnerAuth);
        signInButton = findViewById(R.id.signInButton);
        forgotPasswordLink = findViewById(R.id.forgotPasswordLink);
        signUpLink = findViewById(R.id.signUpLink);

        // Setup Role Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinnerAuth.setAdapter(adapter);

        signInButton.setOnClickListener(v -> signInUser());
        forgotPasswordLink.setOnClickListener(v -> navigateToPasswordReset());
        signUpLink.setOnClickListener(v -> navigateToSignup());
    }

    private void signInUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String selectedRole = roleSpinnerAuth.getSelectedItem().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Email and Password are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedRole.equals("Choose Your Role")) {
            Toast.makeText(this, "Please select your role", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        validateUserRole(selectedRole);
                    } else {
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void validateUserRole(String selectedRole) {
        String userId = mAuth.getCurrentUser().getUid();
        databaseRef.child(userId).get().addOnCompleteListener(userTask -> {
            if (userTask.isSuccessful() && userTask.getResult().exists()) {
                DataSnapshot snapshot = userTask.getResult();
                String registeredRole = snapshot.child("role").getValue(String.class);

                if (registeredRole != null && registeredRole.equalsIgnoreCase(selectedRole)) {
                    if ("Organizer".equalsIgnoreCase(registeredRole)) {
                        // If the user is an Organizer, navigate to OrganizerDashboardActivity
                        Intent organizerIntent = new Intent(this, OrganizerDashboardActivity.class);
                        startActivity(organizerIntent);
                        finish();
                    } else {
                        // For other roles (e.g., User, Admin)
                        Intent locationIntent = new Intent(this, LocationActivity.class);
                        locationIntent.putExtra("userRole", registeredRole);
                        startActivity(locationIntent);
                        finish();
                    }
                } else {
                    Toast.makeText(this, "Invalid role selection.", Toast.LENGTH_SHORT).show();
                    mAuth.signOut();
                }
            } else {
                Toast.makeText(this, "User validation failed.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToPasswordReset() {
        Intent intent = new Intent(AuthActivity.this, PasswordResetActivity.class);
        startActivity(intent);
    }

    private void navigateToSignup() {
        Intent intent = new Intent(AuthActivity.this, SignupActivity.class);
        startActivity(intent);
    }
}