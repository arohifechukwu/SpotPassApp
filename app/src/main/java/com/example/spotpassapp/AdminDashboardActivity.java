//package com.example.spotpassapp;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//import com.google.firebase.auth.FirebaseAuth;
//
//public class AdminDashboardActivity extends AppCompatActivity {
//
//    private Button manageUsersButton, updateItemsButton, logoutButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_admin_dashboard);
//
//        manageUsersButton = findViewById(R.id.manageUsersButton);
//        updateItemsButton = findViewById(R.id.updateItemsButton);
//        logoutButton = findViewById(R.id.logoutButton);
//
//        manageUsersButton.setOnClickListener(v ->
//                startActivity(new Intent(this, ManageUsersActivity.class))
//        );
//
//        updateItemsButton.setOnClickListener(v ->
//                startActivity(new Intent(this, UpdateItemsActivity.class))
//        );
//
//        logoutButton.setOnClickListener(v -> {
//            FirebaseAuth.getInstance().signOut();
//            startActivity(new Intent(this, AuthActivity.class));
//            finish();
//        });
//    }
//}



package com.example.spotpassapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class AdminDashboardActivity extends AppCompatActivity {

    private Button manageUsersButton, uploadEventButton, modifyEventButton, resetUserPasswordButton, addUserButton, logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Initialize Buttons
        manageUsersButton = findViewById(R.id.manageUsersButton);
        uploadEventButton = findViewById(R.id.uploadEventButton);
        modifyEventButton = findViewById(R.id.modifyEventButton);
        resetUserPasswordButton = findViewById(R.id.resetUserPasswordButton);
        addUserButton = findViewById(R.id.addUserButton);
        logoutButton = findViewById(R.id.logoutButton);

        // Set Button Click Listeners
        manageUsersButton.setOnClickListener(v -> startActivity(new Intent(this, ManageUsersActivity.class)));

        uploadEventButton.setOnClickListener(v -> startActivity(new Intent(this, UploadEventActivity.class)));

        modifyEventButton.setOnClickListener(v -> startActivity(new Intent(this, ModifyEventActivity.class)));

        resetUserPasswordButton.setOnClickListener(v -> startActivity(new Intent(this, ResetUserPasswordActivity.class)));

        addUserButton.setOnClickListener(v -> startActivity(new Intent(this, AddUserActivity.class)));

        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, AuthActivity.class));
            finish();
        });
    }
}