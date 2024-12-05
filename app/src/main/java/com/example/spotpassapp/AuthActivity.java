//package com.example.spotpassapp;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.material.button.MaterialButton;
//import com.google.android.material.textfield.TextInputEditText;
//import com.google.firebase.auth.FirebaseAuth;
//
//public class AuthActivity extends AppCompatActivity {
//
//    private FirebaseAuth mAuth;
//    private TextInputEditText emailEditText, passwordEditText;
//    private MaterialButton signInButton;
//    private TextView forgotPasswordLink, signUpLink;
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
//        signInButton = findViewById(R.id.signInButton);
//        forgotPasswordLink = findViewById(R.id.forgotPasswordLink);
//        signUpLink = findViewById(R.id.signUpLink);
//
//        signInButton.setOnClickListener(v -> signInUser());
//        forgotPasswordLink.setOnClickListener(v -> navigateToPasswordReset());
//        signUpLink.setOnClickListener(v -> navigateToSignup());
//    }
//
//    private void signInUser() {
//        String email = emailEditText.getText().toString().trim();
//        String password = passwordEditText.getText().toString().trim();
//
//        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
//            Toast.makeText(this, "Email and Password are required", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, task -> {
//                    if (task.isSuccessful()) {
//                        Toast.makeText(this, "Sign-in successful!", Toast.LENGTH_SHORT).show();
//                        navigateToProfile();
//                    } else {
//                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    private void navigateToPasswordReset() {
//        Intent intent = new Intent(AuthActivity.this, PasswordResetActivity.class);
//        startActivity(intent);
//    }
//
//    private void navigateToSignup() {
//        Intent intent = new Intent(AuthActivity.this, SignupActivity.class);
//        startActivity(intent);
//    }
//
//    private void navigateToProfile() {
//        Intent intent = new Intent(AuthActivity.this, ProfileActivity.class);
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
    private MaterialButton signInButton;
    private TextView forgotPasswordLink, signUpLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        mAuth = FirebaseAuth.getInstance();

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signInButton = findViewById(R.id.signInButton);
        forgotPasswordLink = findViewById(R.id.forgotPasswordLink);
        signUpLink = findViewById(R.id.signUpLink);

        signInButton.setOnClickListener(v -> signInUser());
        forgotPasswordLink.setOnClickListener(v -> navigateToPasswordReset());
        signUpLink.setOnClickListener(v -> navigateToSignup());
    }

    private void signInUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Email and Password are required", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Sign-in successful!", Toast.LENGTH_SHORT).show();
                        navigateToLocationActivity();
                    } else {
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
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

    private void navigateToLocationActivity() {
        Intent intent = new Intent(AuthActivity.this, LocationActivity.class);
        startActivity(intent);
        finish();
    }
}