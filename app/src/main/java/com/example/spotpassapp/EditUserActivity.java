package com.example.spotpassapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.spotpassapp.model.User;
import com.example.spotpassapp.util.FirebaseUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditUserActivity extends AppCompatActivity {

    private EditText firstNameEdit, lastNameEdit, emailEdit, phoneEdit, addressEdit;
    private Button saveChangesButton;
    private DatabaseReference databaseRef;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        firstNameEdit = findViewById(R.id.firstNameEdit);
        lastNameEdit = findViewById(R.id.lastNameEdit);
        emailEdit = findViewById(R.id.emailEdit);
        phoneEdit = findViewById(R.id.phoneEdit);
        addressEdit = findViewById(R.id.addressEdit);
        saveChangesButton = findViewById(R.id.saveChangesButton);

        databaseRef = FirebaseDatabase.getInstance().getReference("users");
        userEmail = getIntent().getStringExtra("userEmail");

        loadUserData(FirebaseUtils.encodeUserEmail(userEmail));

        saveChangesButton.setOnClickListener(v -> saveChanges());
    }

    private void loadUserData(String emailKey) {
        databaseRef.child(emailKey).get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    firstNameEdit.setText(user.getFirstName());
                    lastNameEdit.setText(user.getLastName());
                    emailEdit.setText(user.getEmail());
                    phoneEdit.setText(user.getPhone());
                    addressEdit.setText(user.getAddress());
                }
            } else {
                showToast("User not found.");
                finish();
            }
        }).addOnFailureListener(e -> showToast("Failed to load data."));
    }

    private void saveChanges() {
        String firstName = firstNameEdit.getText().toString().trim();
        String lastName = lastNameEdit.getText().toString().trim();
        String phone = phoneEdit.getText().toString().trim();
        String address = addressEdit.getText().toString().trim();
        String emailKey = FirebaseUtils.encodeUserEmail(userEmail);

        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName)) {
            showToast("Fill all required fields.");
            return;
        }

        User updatedUser = new User(firstName, lastName, phone, address, userEmail, "user");

        databaseRef.child(emailKey).setValue(updatedUser).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                showToast("User updated!");
                finish();
            } else {
                showToast("Update failed.");
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(EditUserActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}