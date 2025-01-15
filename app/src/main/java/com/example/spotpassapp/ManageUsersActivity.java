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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        recyclerView = findViewById(R.id.recyclerView);
        addUserButton = findViewById(R.id.addUserButton);
        viewUsersButton = findViewById(R.id.viewUsersButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();
        databaseRef = FirebaseDatabase.getInstance().getReference("users");

        addUserButton.setOnClickListener(v -> startActivity(new Intent(this, AddUserActivity.class)));

        viewUsersButton.setOnClickListener(v -> {
            recyclerView.setVisibility(View.VISIBLE);
            fetchRegisteredUsers();
        });
    }

    private void fetchRegisteredUsers() {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        userList.add(user);
                    }
                }
                userAdapter = new UserAdapter(ManageUsersActivity.this, userList);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ManageUsersActivity.this, "Failed to load users.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}