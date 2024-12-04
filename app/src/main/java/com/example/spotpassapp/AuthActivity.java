//package com.example.spotpassapp;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.material.button.MaterialButton;
//import com.google.android.material.textfield.TextInputEditText;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//
//public class AuthActivity extends AppCompatActivity {
//
//    private FirebaseAuth mAuth;
//    private TextInputEditText emailEditText, passwordEditText;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_auth);
//
//        mAuth = FirebaseAuth.getInstance();
//
//        emailEditText = findViewById(R.id.emailEditText);
//        passwordEditText = findViewById(R.id.passwordEditText);
//        MaterialButton signInButton = findViewById(R.id.signInButton);
//        MaterialButton signUpButton = findViewById(R.id.signUpButton);
//
//        signInButton.setOnClickListener(v -> signInUser());
//        signUpButton.setOnClickListener(v -> registerUser());
//    }
//
//    private void signInUser() {
//        String email = emailEditText.getText().toString().trim();
//        String password = passwordEditText.getText().toString().trim();
//
//        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
//            Toast.makeText(AuthActivity.this, "Email and Password are required", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, task -> {
//                    if (task.isSuccessful()) {
//                        Toast.makeText(AuthActivity.this, "Sign-in successful!", Toast.LENGTH_SHORT).show();
//                        navigateToLocation();
//                    } else {
//                        Toast.makeText(AuthActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    private void registerUser() {
//        String email = emailEditText.getText().toString().trim();
//        String password = passwordEditText.getText().toString().trim();
//
//        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
//            Toast.makeText(AuthActivity.this, "Email and Password are required", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, task -> {
//                    if (task.isSuccessful()) {
//                        Toast.makeText(AuthActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
//                        navigateToLocation();
//                    } else {
//                        Toast.makeText(AuthActivity.this, "Registration failed.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    private void navigateToLocation() {
//        Intent intent = new Intent(AuthActivity.this, LocationActivity.class);
//        startActivity(intent);
//        finish();
//    }
//}



package com.example.spotpassapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class AuthActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputEditText emailEditText, passwordEditText;
    private MaterialButton signInButton, signUpButton;
    private TextView guestLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signInButton = findViewById(R.id.signInButton);
        signUpButton = findViewById(R.id.signUpButton);
        guestLink = findViewById(R.id.guestLink);

        signInButton.setOnClickListener(v -> signInUser());
        signUpButton.setOnClickListener(v -> registerUser());
        guestLink.setOnClickListener(v -> navigateToExploreAsGuest());
    }

    private void signInUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(AuthActivity.this, "Email and Password are required", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AuthActivity.this, "Sign-in successful!", Toast.LENGTH_SHORT).show();
                        navigateToLocation();
                    } else {
                        Toast.makeText(AuthActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(AuthActivity.this, "Email and Password are required", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(AuthActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                        navigateToLocation();
                    } else {
                        Toast.makeText(AuthActivity.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void navigateToLocation() {
        Intent intent = new Intent(AuthActivity.this, LocationActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToExploreAsGuest() {
        Intent intent = new Intent(AuthActivity.this, ExploreEventsActivity.class);
        startActivity(intent);
        finish();
    }
}