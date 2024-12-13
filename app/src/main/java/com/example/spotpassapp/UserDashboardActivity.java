package com.example.spotpassapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class UserDashboardActivity extends AppCompatActivity {

    private Button viewProfileButton, exploreEventsButton, logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        viewProfileButton = findViewById(R.id.viewProfileButton);
        exploreEventsButton = findViewById(R.id.exploreEventsButton);
        logoutButton = findViewById(R.id.logoutButton);

        viewProfileButton.setOnClickListener(v ->
                startActivity(new Intent(this, ProfileActivity.class))
        );

        exploreEventsButton.setOnClickListener(v ->
                startActivity(new Intent(this, ExploreEventsActivity.class))
        );

        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, AuthActivity.class));
            finish();
        });
    }
}