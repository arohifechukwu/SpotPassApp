//
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
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.example.spotpassapp.model.User; // Import the User model
//
//public class SignupActivity extends AppCompatActivity {
//
//    private FirebaseAuth mAuth;
//    private DatabaseReference databaseRef;
//    private TextInputEditText emailEditText, firstNameEditText, lastNameEditText, phoneEditText, addressEditText, passwordEditText, confirmPasswordEditText;
//    private MaterialButton signUpButton;
//    private TextView signInLink;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_signup);
//
//        mAuth = FirebaseAuth.getInstance();
//        databaseRef = FirebaseDatabase.getInstance().getReference("users");
//
//        emailEditText = findViewById(R.id.emailEditText);
//        firstNameEditText = findViewById(R.id.firstNameEditText);
//        lastNameEditText = findViewById(R.id.lastNameEditText);
//        phoneEditText = findViewById(R.id.phoneEditText);
//        addressEditText = findViewById(R.id.addressEditText);
//        passwordEditText = findViewById(R.id.passwordEditText);
//        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
//        signUpButton = findViewById(R.id.signUpButton);
//        signInLink = findViewById(R.id.signInLink);
//
//        signUpButton.setOnClickListener(v -> registerUser());
//        signInLink.setOnClickListener(v -> navigateToSignIn());
//    }
//
//    private void registerUser() {
//        String email = emailEditText.getText().toString().trim();
//        String password = passwordEditText.getText().toString().trim();
//        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
//        String firstName = firstNameEditText.getText().toString().trim();
//        String lastName = lastNameEditText.getText().toString().trim();
//        String phone = phoneEditText.getText().toString().trim();
//        String address = addressEditText.getText().toString().trim();
//
//        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) ||
//                TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(phone)) {
//            Toast.makeText(this, "All mandatory fields are required", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (!password.equals(confirmPassword)) {
//            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, task -> {
//                    if (task.isSuccessful()) {
//                        User user = new User(firstName, lastName, phone, address, email);
//                        databaseRef.child(mAuth.getCurrentUser().getUid()).setValue(user)
//                                .addOnCompleteListener(dbTask -> {
//                                    if (dbTask.isSuccessful()) {
//                                        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
//                                        navigateToSignIn();
//                                    } else {
//                                        Toast.makeText(this, "Failed to save user data.", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//                    } else {
//                        Toast.makeText(this, "Registration failed.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    private void navigateToSignIn() {
//        Intent intent = new Intent(SignupActivity.this, AuthActivity.class);
//        startActivity(intent);
//        finish();
//    }
//}



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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.spotpassapp.model.User;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;

    private TextInputEditText emailEditText, firstNameEditText, lastNameEditText, phoneEditText, addressEditText, passwordEditText, confirmPasswordEditText;
    private Spinner roleSpinner;
    private MaterialButton signUpButton;
    private TextView signInLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("users");

        // Initialize Views
        emailEditText = findViewById(R.id.emailEditText);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        addressEditText = findViewById(R.id.addressEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        roleSpinner = findViewById(R.id.roleSpinner);
        signUpButton = findViewById(R.id.signUpButton);
        signInLink = findViewById(R.id.signInLink);

        // Populate Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.roles, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        // Set OnClickListeners
        signUpButton.setOnClickListener(v -> registerUser());
        signInLink.setOnClickListener(v -> navigateToSignIn());
    }

    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        String role = roleSpinner.getSelectedItem().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) ||
                TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(address)) {
            Toast.makeText(this, "All fields are mandatory.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        User user = new User(firstName, lastName, phone, address, email, role);
                        databaseRef.child(mAuth.getCurrentUser().getUid()).setValue(user)
                                .addOnCompleteListener(dbTask -> {
                                    if (dbTask.isSuccessful()) {
                                        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                                        navigateToSignIn();
                                    } else {
                                        Toast.makeText(this, "Failed to save user data.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(this, "Registration failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void navigateToSignIn() {
        Intent intent = new Intent(SignupActivity.this, AuthActivity.class);
        startActivity(intent);
        finish();
    }
}