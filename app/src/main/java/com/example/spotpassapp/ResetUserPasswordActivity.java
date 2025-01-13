package com.example.spotpassapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ResetUserPasswordActivity extends AppCompatActivity {

    private EditText userEmailInput;
    private Button resetPasswordButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_user_password);

        auth = FirebaseAuth.getInstance();

        userEmailInput = findViewById(R.id.userEmailInput);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);

        resetPasswordButton.setOnClickListener(v -> resetUserPassword());
    }

    private void resetUserPassword() {
        String email = userEmailInput.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Enter user email.", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Password reset email sent.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to send reset email.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}