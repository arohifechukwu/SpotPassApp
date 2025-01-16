package com.example.spotpassapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotpassapp.adapter.UserAdapter;
import com.example.spotpassapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageUsersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList;
    private DatabaseReference databaseRef;
    private Button addUserButton, viewUsersButton;
    private boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerView);
        addUserButton = findViewById(R.id.addUserButton);
        viewUsersButton = findViewById(R.id.viewUsersButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();

        // Reference to the users node in the database
        databaseRef = FirebaseDatabase.getInstance("https://spotpassapp-default-rtdb.firebaseio.com")
                .getReference("users");

        // Validate if the current user is an admin
        validateAdmin();

        addUserButton.setOnClickListener(v -> startActivity(new Intent(this, AddUserActivity.class)));

        viewUsersButton.setOnClickListener(v -> {
            if (isAdmin) {
                recyclerView.setVisibility(View.VISIBLE);
                fetchRegisteredUsers();
            } else {
                Toast.makeText(this, "Access denied. Admins only.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validateAdmin() {
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId != null) {
            // Check the role of the current user
            databaseRef.child(userId).child("role").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String role = snapshot.getValue(String.class);
                        if ("Admin".equalsIgnoreCase(role)) {
                            isAdmin = true;
                        } else {
                            isAdmin = false;
                            Toast.makeText(ManageUsersActivity.this, "Access denied. Admins only.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ManageUsersActivity.this, "User role not found.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ManageUsersActivity.this, "Failed to verify role: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
        }
    }



    private void fetchRegisteredUsers() {
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        // Store the Firebase key in the User model
                        user.setKey(snapshot.getKey());
                        userList.add(user);
                    }
                }

                if (userList.isEmpty()) {
                    Toast.makeText(ManageUsersActivity.this, "No users found.", Toast.LENGTH_SHORT).show();
                } else {
                    if (userAdapter == null) {
                        userAdapter = new UserAdapter(ManageUsersActivity.this, userList);
                        recyclerView.setAdapter(userAdapter);
                    } else {
                        userAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ManageUsersActivity.this, "Failed to load users: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}