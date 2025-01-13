//package com.example.spotpassapp;
//
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//
//public class AddUserActivity extends AppCompatActivity {
//
//    private EditText newUserEmail, newUserPassword;
//    private Button addUserButton;
//    private FirebaseAuth auth;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_user);
//
//        auth = FirebaseAuth.getInstance();
//
//        newUserEmail = findViewById(R.id.newUserEmail);
//        newUserPassword = findViewById(R.id.newUserPassword);
//        addUserButton = findViewById(R.id.addUserButton);
//
//        addUserButton.setOnClickListener(v -> addUser());
//    }
//
//    private void addUser() {
//        String email = newUserEmail.getText().toString().trim();
//        String password = newUserPassword.getText().toString().trim();
//
//        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
//            Toast.makeText(this, "Fill in all fields.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                Toast.makeText(this, "User created.", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "User creation failed.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}



package com.example.spotpassapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spotpassapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddUserActivity extends AppCompatActivity {

    private EditText firstName, lastName, phone, address, email, password;
    private Spinner roleSpinner;
    private Button addUserButton;

    private FirebaseAuth auth;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("users");

        // Initialize Views
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        email = findViewById(R.id.newUserEmail);
        password = findViewById(R.id.newUserPassword);
        roleSpinner = findViewById(R.id.roleSpinner);
        addUserButton = findViewById(R.id.addUserButton);

        // Setup Role Selection Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.roles, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        addUserButton.setOnClickListener(v -> addUser());
    }

    private void addUser() {
        String firstNameStr = firstName.getText().toString().trim();
        String lastNameStr = lastName.getText().toString().trim();
        String phoneStr = phone.getText().toString().trim();
        String addressStr = address.getText().toString().trim();
        String emailStr = email.getText().toString().trim();
        String passwordStr = password.getText().toString().trim();
        String roleStr = roleSpinner.getSelectedItem().toString().toLowerCase();

        if (TextUtils.isEmpty(emailStr) || TextUtils.isEmpty(passwordStr) ||
                TextUtils.isEmpty(firstNameStr) || TextUtils.isEmpty(lastNameStr) ||
                TextUtils.isEmpty(phoneStr) || TextUtils.isEmpty(roleStr)) {

            Toast.makeText(this, "All fields are required.", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = task.getResult().getUser();
                if (firebaseUser != null) {
                    String userId = firebaseUser.getUid();

                    // Create a new user
                    User user = new User(firstNameStr, lastNameStr, phoneStr, addressStr, emailStr, roleStr);
                    databaseRef.child(userId).setValue(user).addOnCompleteListener(dbTask -> {
                        if (dbTask.isSuccessful()) {
                            Toast.makeText(this, "User added successfully!", Toast.LENGTH_SHORT).show();
                            clearFields();
                        } else {
                            Toast.makeText(this, "Failed to add user.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Toast.makeText(this, "Failed to create user account.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearFields() {
        firstName.setText("");
        lastName.setText("");
        phone.setText("");
        address.setText("");
        email.setText("");
        password.setText("");
        roleSpinner.setSelection(0);
    }
}