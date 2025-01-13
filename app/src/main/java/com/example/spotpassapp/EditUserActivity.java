//package com.example.spotpassapp;
//
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.example.spotpassapp.model.User;
//
//public class EditUserActivity extends AppCompatActivity {
//
//    private EditText firstNameEdit, lastNameEdit, emailEdit, phoneEdit, addressEdit;
//    private Button saveChangesButton;
//    private DatabaseReference databaseRef;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_user);
//
//        // Initialize Views
//        firstNameEdit = findViewById(R.id.firstNameEdit);
//        lastNameEdit = findViewById(R.id.lastNameEdit);
//        emailEdit = findViewById(R.id.emailEdit);
//        phoneEdit = findViewById(R.id.phoneEdit);
//        addressEdit = findViewById(R.id.addressEdit);
//        saveChangesButton = findViewById(R.id.saveChangesButton);
//
//        databaseRef = FirebaseDatabase.getInstance().getReference("users");
//
//        saveChangesButton.setOnClickListener(v -> saveChanges());
//    }
//
//    private void saveChanges() {
//        String firstName = firstNameEdit.getText().toString().trim();
//        String lastName = lastNameEdit.getText().toString().trim();
//        String email = emailEdit.getText().toString().trim();
//        String phone = phoneEdit.getText().toString().trim();
//        String address = addressEdit.getText().toString().trim();
//
//        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(email)) {
//            Toast.makeText(this, "Fill all required fields.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        databaseRef.child(email).setValue(new User(firstName, lastName, phone, address, email, "user"))
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Toast.makeText(this, "User updated!", Toast.LENGTH_SHORT).show();
//                        finish();
//                    } else {
//                        Toast.makeText(this, "Update failed.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//}





//package com.example.spotpassapp;
//
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.spotpassapp.model.User;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//public class EditUserActivity extends AppCompatActivity {
//
//    private EditText firstNameEdit, lastNameEdit, emailEdit, phoneEdit, addressEdit;
//    private Button saveChangesButton;
//    private DatabaseReference databaseRef;
//    private String userEmail;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_user);
//
//        // Initialize Views
//        firstNameEdit = findViewById(R.id.firstNameEdit);
//        lastNameEdit = findViewById(R.id.lastNameEdit);
//        emailEdit = findViewById(R.id.emailEdit);
//        phoneEdit = findViewById(R.id.phoneEdit);
//        addressEdit = findViewById(R.id.addressEdit);
//        saveChangesButton = findViewById(R.id.saveChangesButton);
//
//        databaseRef = FirebaseDatabase.getInstance().getReference("users");
//
//        // Get the selected user's email from the intent
//        userEmail = getIntent().getStringExtra("userEmail");
//
//        if (!TextUtils.isEmpty(userEmail)) {
//            loadUserData(userEmail);
//        }
//
//        saveChangesButton.setOnClickListener(v -> saveChanges());
//    }
//
//    private void loadUserData(String email) {
//        databaseRef.child(email).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    User user = snapshot.getValue(User.class);
//                    if (user != null) {
//                        firstNameEdit.setText(user.getFirstName());
//                        lastNameEdit.setText(user.getLastName());
//                        emailEdit.setText(user.getEmail());
//                        phoneEdit.setText(user.getPhone());
//                        addressEdit.setText(user.getAddress());
//                    }
//                } else {
//                    Toast.makeText(EditUserActivity.this, "User not found.", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(EditUserActivity.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void saveChanges() {
//        String firstName = firstNameEdit.getText().toString().trim();
//        String lastName = lastNameEdit.getText().toString().trim();
//        String phone = phoneEdit.getText().toString().trim();
//        String address = addressEdit.getText().toString().trim();
//
//        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(userEmail)) {
//            Toast.makeText(this, "Fill all required fields.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        User updatedUser = new User(firstName, lastName, phone, address, userEmail, "user");
//
//        databaseRef.child(userEmail).setValue(updatedUser).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                Toast.makeText(this, "User updated!", Toast.LENGTH_SHORT).show();
//                finish();
//            } else {
//                Toast.makeText(this, "Update failed.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}




//package com.example.spotpassapp;
//
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.spotpassapp.model.User;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import static com.example.spotpassapp.FirebaseUtils.encodeUserEmail;
//
//public class EditUserActivity extends AppCompatActivity {
//
//    private EditText firstNameEdit, lastNameEdit, emailEdit, phoneEdit, addressEdit;
//    private Button saveChangesButton;
//    private DatabaseReference databaseRef;
//    private String userEmail;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_user);
//
//        // Initialize Views
//        firstNameEdit = findViewById(R.id.firstNameEdit);
//        lastNameEdit = findViewById(R.id.lastNameEdit);
//        emailEdit = findViewById(R.id.emailEdit);
//        phoneEdit = findViewById(R.id.phoneEdit);
//        addressEdit = findViewById(R.id.addressEdit);
//        saveChangesButton = findViewById(R.id.saveChangesButton);
//
//        databaseRef = FirebaseDatabase.getInstance().getReference("users");
//
//        // Get selected user's email from the intent
//        userEmail = getIntent().getStringExtra("userEmail");
//
//        if (!TextUtils.isEmpty(userEmail)) {
//            loadUserData(userEmail);
//        }
//
//        saveChangesButton.setOnClickListener(v -> saveChanges());
//    }
//
//    private void loadUserData(String email) {
//        String encodedEmail = encodeUserEmail(email);
//        databaseRef.child(encodedEmail).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    User user = snapshot.getValue(User.class);
//                    if (user != null) {
//                        firstNameEdit.setText(user.getFirstName());
//                        lastNameEdit.setText(user.getLastName());
//                        emailEdit.setText(user.getEmail());
//                        phoneEdit.setText(user.getPhone());
//                        addressEdit.setText(user.getAddress());
//                    }
//                } else {
//                    Toast.makeText(EditUserActivity.this, "User not found.", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(EditUserActivity.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void saveChanges() {
//        String firstName = firstNameEdit.getText().toString().trim();
//        String lastName = lastNameEdit.getText().toString().trim();
//        String phone = phoneEdit.getText().toString().trim();
//        String address = addressEdit.getText().toString().trim();
//
//        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(userEmail)) {
//            Toast.makeText(this, "Fill all required fields.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        User updatedUser = new User(firstName, lastName, phone, address, userEmail, "user");
//        String encodedEmail = encodeUserEmail(userEmail);
//
//        databaseRef.child(encodedEmail).setValue(updatedUser).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                Toast.makeText(this, "User updated!", Toast.LENGTH_SHORT).show();
//                finish();
//            } else {
//                Toast.makeText(this, "Update failed.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}




//package com.example.spotpassapp;
//
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.spotpassapp.model.User;
//import com.example.spotpassapp.util.FirebaseUtils;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//public class EditUserActivity extends AppCompatActivity {
//
//    private EditText firstNameEdit, lastNameEdit, emailEdit, phoneEdit, addressEdit;
//    private Button saveChangesButton;
//    private DatabaseReference databaseRef;
//    private String userEmail;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_user);
//
//        // Initialize Views
//        firstNameEdit = findViewById(R.id.firstNameEdit);
//        lastNameEdit = findViewById(R.id.lastNameEdit);
//        emailEdit = findViewById(R.id.emailEdit);
//        phoneEdit = findViewById(R.id.phoneEdit);
//        addressEdit = findViewById(R.id.addressEdit);
//        saveChangesButton = findViewById(R.id.saveChangesButton);
//
//        databaseRef = FirebaseDatabase.getInstance().getReference("users");
//
//        // Get the selected user's email from the intent
//        userEmail = getIntent().getStringExtra("userEmail");
//
//        if (!TextUtils.isEmpty(userEmail)) {
//            loadUserData(userEmail);
//        }
//
//        saveChangesButton.setOnClickListener(v -> saveChanges());
//    }
//
//    private void loadUserData(String email) {
//        databaseRef.child(email).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    User user = snapshot.getValue(User.class);
//                    if (user != null) {
//                        firstNameEdit.setText(user.getFirstName());
//                        lastNameEdit.setText(user.getLastName());
//                        emailEdit.setText(FirebaseUtils.decodeUserEmail(email));
//                        phoneEdit.setText(user.getPhone());
//                        addressEdit.setText(user.getAddress());
//                    }
//                } else {
//                    Toast.makeText(EditUserActivity.this, "User not found.", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(EditUserActivity.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void saveChanges() {
//        String firstName = firstNameEdit.getText().toString().trim();
//        String lastName = lastNameEdit.getText().toString().trim();
//        String phone = phoneEdit.getText().toString().trim();
//        String address = addressEdit.getText().toString().trim();
//
//        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(userEmail)) {
//            Toast.makeText(this, "Fill all required fields.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        User updatedUser = new User(firstName, lastName, phone, address, FirebaseUtils.decodeUserEmail(userEmail), "user");
//
//        databaseRef.child(userEmail).setValue(updatedUser).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                Toast.makeText(this, "User updated!", Toast.LENGTH_SHORT).show();
//                finish();
//            } else {
//                Toast.makeText(this, "Update failed.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}



//package com.example.spotpassapp;
//
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.spotpassapp.model.User;
//import com.example.spotpassapp.util.FirebaseUtils;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//public class EditUserActivity extends AppCompatActivity {
//
//    private EditText firstNameEdit, lastNameEdit, emailEdit, phoneEdit, addressEdit;
//    private Button saveChangesButton;
//    private DatabaseReference databaseRef;
//    private String userEmail;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_user);
//
//        firstNameEdit = findViewById(R.id.firstNameEdit);
//        lastNameEdit = findViewById(R.id.lastNameEdit);
//        emailEdit = findViewById(R.id.emailEdit);
//        phoneEdit = findViewById(R.id.phoneEdit);
//        addressEdit = findViewById(R.id.addressEdit);
//        saveChangesButton = findViewById(R.id.saveChangesButton);
//
//        databaseRef = FirebaseDatabase.getInstance().getReference("users");
//
//        userEmail = getIntent().getStringExtra("userEmail");
//
//        if (!TextUtils.isEmpty(userEmail)) {
//            loadUserData(FirebaseUtils.decodeUserEmail(userEmail));
//        }
//
//        saveChangesButton.setOnClickListener(v -> saveChanges());
//    }
//
//    private void loadUserData(String email) {
//        String emailKey = FirebaseUtils.encodeUserEmail(email);
//        databaseRef.child(emailKey).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    User user = snapshot.getValue(User.class);
//                    if (user != null) {
//                        firstNameEdit.setText(user.getFirstName());
//                        lastNameEdit.setText(user.getLastName());
//                        emailEdit.setText(user.getEmail());
//                        phoneEdit.setText(user.getPhone());
//                        addressEdit.setText(user.getAddress());
//                    }
//                } else {
//                    Toast.makeText(EditUserActivity.this, "User not found.", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(EditUserActivity.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void saveChanges() {
//        String firstName = firstNameEdit.getText().toString().trim();
//        String lastName = lastNameEdit.getText().toString().trim();
//        String phone = phoneEdit.getText().toString().trim();
//        String address = addressEdit.getText().toString().trim();
//        String emailKey = FirebaseUtils.encodeUserEmail(userEmail);
//
//        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName)) {
//            Toast.makeText(this, "Fill all required fields.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        User updatedUser = new User(firstName, lastName, phone, address, userEmail, "user");
//
//        databaseRef.child(emailKey).setValue(updatedUser).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                Toast.makeText(this, "User updated!", Toast.LENGTH_SHORT).show();
//                finish();
//            } else {
//                Toast.makeText(this, "Update failed.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}



//package com.example.spotpassapp;
//
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.spotpassapp.model.User;
//import com.example.spotpassapp.util.FirebaseUtils;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//public class EditUserActivity extends AppCompatActivity {
//
//    private EditText firstNameEdit, lastNameEdit, emailEdit, phoneEdit, addressEdit;
//    private Spinner roleSpinner;
//    private Button saveChangesButton;
//    private DatabaseReference databaseRef;
//    private String userEmail;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_user);
//
//        firstNameEdit = findViewById(R.id.firstNameEdit);
//        lastNameEdit = findViewById(R.id.lastNameEdit);
//        emailEdit = findViewById(R.id.emailEdit);
//        phoneEdit = findViewById(R.id.phoneEdit);
//        addressEdit = findViewById(R.id.addressEdit);
//        roleSpinner = findViewById(R.id.roleSpinner);
//        saveChangesButton = findViewById(R.id.saveChangesButton);
//
//        databaseRef = FirebaseDatabase.getInstance().getReference("users");
//        userEmail = getIntent().getStringExtra("userEmail");
//
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
//                this, R.array.roles, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        roleSpinner.setAdapter(adapter);
//
//        if (!TextUtils.isEmpty(userEmail)) {
//            loadUserData(FirebaseUtils.decodeUserEmail(userEmail));
//        }
//
//        saveChangesButton.setOnClickListener(v -> saveChanges());
//    }
//
//    private void loadUserData(String email) {
//        String emailKey = FirebaseUtils.encodeUserEmail(email);
//        databaseRef.child(emailKey).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    User user = snapshot.getValue(User.class);
//                    if (user != null) {
//                        firstNameEdit.setText(user.getFirstName());
//                        lastNameEdit.setText(user.getLastName());
//                        emailEdit.setText(user.getEmail());
//                        phoneEdit.setText(user.getPhone());
//                        addressEdit.setText(user.getAddress());
//                        roleSpinner.setSelection(user.getRole().equalsIgnoreCase("admin") ? 1 : 0);
//                    }
//                } else {
//                    Toast.makeText(EditUserActivity.this, "User not found.", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(EditUserActivity.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void saveChanges() {
//        String firstName = firstNameEdit.getText().toString().trim();
//        String lastName = lastNameEdit.getText().toString().trim();
//        String phone = phoneEdit.getText().toString().trim();
//        String address = addressEdit.getText().toString().trim();
//        String role = roleSpinner.getSelectedItem().toString();
//        String emailKey = FirebaseUtils.encodeUserEmail(userEmail);
//
//        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName)) {
//            Toast.makeText(this, "Fill all required fields.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        User updatedUser = new User(firstName, lastName, phone, address, userEmail, role);
//
//        databaseRef.child(emailKey).setValue(updatedUser).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                Toast.makeText(this, "User updated!", Toast.LENGTH_SHORT).show();
//                finish();
//            } else {
//                Toast.makeText(this, "Update failed.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}


//package com.example.spotpassapp;
//
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.spotpassapp.model.User;
//import com.example.spotpassapp.util.FirebaseUtils;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.ValueEventListener;
//
//public class EditUserActivity extends AppCompatActivity {
//
//    private EditText firstNameEdit, lastNameEdit, emailEdit, phoneEdit, addressEdit;
//    private Spinner roleSpinner;
//    private Button saveChangesButton;
//    private DatabaseReference databaseRef;
//    private String userEmail;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_user);
//
//        // Initialize Views
//        firstNameEdit = findViewById(R.id.firstNameEdit);
//        lastNameEdit = findViewById(R.id.lastNameEdit);
//        emailEdit = findViewById(R.id.emailEdit);
//        phoneEdit = findViewById(R.id.phoneEdit);
//        addressEdit = findViewById(R.id.addressEdit);
//        roleSpinner = findViewById(R.id.roleSpinner);
//        saveChangesButton = findViewById(R.id.saveChangesButton);
//
//        // Initialize Firebase Database Reference
//        databaseRef = FirebaseDatabase.getInstance().getReference("users");
//
//        // Populate Role Spinner
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
//                this, R.array.roles, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        roleSpinner.setAdapter(adapter);
//
//        // Load User Data from Intent
//        userEmail = getIntent().getStringExtra("userEmail");
//        if (!TextUtils.isEmpty(userEmail)) {
//            loadUserData(FirebaseUtils.decodeUserEmail(userEmail));
//        }
//
//        // Save Changes Button Listener
//        saveChangesButton.setOnClickListener(v -> saveChanges());
//    }
//
//    private void loadUserData(String email) {
//        String emailKey = FirebaseUtils.encodeUserEmail(email);
//        databaseRef.child(emailKey).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    User user = snapshot.getValue(User.class);
//                    if (user != null) {
//                        firstNameEdit.setText(user.getFirstName());
//                        lastNameEdit.setText(user.getLastName());
//                        emailEdit.setText(user.getEmail());
//                        phoneEdit.setText(user.getPhone());
//                        addressEdit.setText(user.getAddress());
//
//                        // Set role selection
//                        if ("admin".equalsIgnoreCase(user.getRole())) {
//                            roleSpinner.setSelection(1);  // Admin position in the spinner
//                        } else {
//                            roleSpinner.setSelection(0);  // User position in the spinner
//                        }
//                    }
//                } else {
//                    Toast.makeText(EditUserActivity.this, "User not found.", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(EditUserActivity.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void saveChanges() {
//        String firstName = firstNameEdit.getText().toString().trim();
//        String lastName = lastNameEdit.getText().toString().trim();
//        String phone = phoneEdit.getText().toString().trim();
//        String address = addressEdit.getText().toString().trim();
//        String selectedRole = roleSpinner.getSelectedItem().toString();
//        String emailKey = FirebaseUtils.encodeUserEmail(userEmail);
//
//        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName)) {
//            Toast.makeText(this, "Please fill all required fields.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        User updatedUser = new User(firstName, lastName, phone, address, userEmail, selectedRole);
//
//        databaseRef.child(emailKey).setValue(updatedUser).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                Toast.makeText(this, "User updated!", Toast.LENGTH_SHORT).show();
//                finish();
//            } else {
//                Toast.makeText(this, "Update failed.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}



//package com.example.spotpassapp;
//
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.spotpassapp.model.User;
//import com.example.spotpassapp.util.FirebaseUtils;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//public class EditUserActivity extends AppCompatActivity {
//
//    private EditText firstNameEdit, lastNameEdit, emailEdit, phoneEdit, addressEdit;
//    private Spinner roleSpinner;
//    private Button saveChangesButton;
//    private DatabaseReference databaseRef;
//    private String userEmail;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_user);
//
//        firstNameEdit = findViewById(R.id.firstNameEdit);
//        lastNameEdit = findViewById(R.id.lastNameEdit);
//        emailEdit = findViewById(R.id.emailEdit);
//        phoneEdit = findViewById(R.id.phoneEdit);
//        addressEdit = findViewById(R.id.addressEdit);
//        roleSpinner = findViewById(R.id.roleSpinner);
//        saveChangesButton = findViewById(R.id.saveChangesButton);
//
//        databaseRef = FirebaseDatabase.getInstance().getReference("users");
//
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
//                this, R.array.roles, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        roleSpinner.setAdapter(adapter);
//
//        userEmail = getIntent().getStringExtra("userEmail");
//        if (!TextUtils.isEmpty(userEmail)) {
//            loadUserData(FirebaseUtils.decodeUserEmail(userEmail));
//        }
//
//        saveChangesButton.setOnClickListener(v -> saveChanges());
//    }
//
//    private void loadUserData(String email) {
//        String emailKey = FirebaseUtils.encodeUserEmail(email);
//        databaseRef.child(emailKey).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    User user = snapshot.getValue(User.class);
//                    if (user != null) {
//                        firstNameEdit.setText(user.getFirstName());
//                        lastNameEdit.setText(user.getLastName());
//                        emailEdit.setText(user.getEmail());
//                        phoneEdit.setText(user.getPhone());
//                        addressEdit.setText(user.getAddress());
//                        roleSpinner.setSelection(user.getRole().equals("admin") ? 1 : 0);
//                    }
//                } else {
//                    Toast.makeText(EditUserActivity.this, "User not found.", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(EditUserActivity.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void saveChanges() {
//        String firstName = firstNameEdit.getText().toString().trim();
//        String lastName = lastNameEdit.getText().toString().trim();
//        String phone = phoneEdit.getText().toString().trim();
//        String address = addressEdit.getText().toString().trim();
//        String selectedRole = roleSpinner.getSelectedItem().toString();
//        String emailKey = FirebaseUtils.encodeUserEmail(userEmail);
//
//        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName)) {
//            Toast.makeText(this, "Fill all required fields.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        User updatedUser = new User(firstName, lastName, phone, address, userEmail, selectedRole);
//
//        databaseRef.child(emailKey).setValue(updatedUser).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                Toast.makeText(this, "User updated!", Toast.LENGTH_SHORT).show();
//                finish();
//            } else {
//                Toast.makeText(this, "Update failed.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//}




//package com.example.spotpassapp;
//
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.spotpassapp.model.User;
//import com.example.spotpassapp.util.FirebaseUtils;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//public class EditUserActivity extends AppCompatActivity {
//
//    private EditText firstNameEdit, lastNameEdit, emailEdit, phoneEdit, addressEdit;
//    private Spinner roleSpinner;
//    private Button saveChangesButton;
//    private DatabaseReference databaseRef;
//    private String userEmail;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_user);
//
//        firstNameEdit = findViewById(R.id.firstNameEdit);
//        lastNameEdit = findViewById(R.id.lastNameEdit);
//        emailEdit = findViewById(R.id.emailEdit);
//        phoneEdit = findViewById(R.id.phoneEdit);
//        addressEdit = findViewById(R.id.addressEdit);
//        roleSpinner = findViewById(R.id.roleSpinner);
//        saveChangesButton = findViewById(R.id.saveChangesButton);
//
//        databaseRef = FirebaseDatabase.getInstance().getReference("users");
//
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
//                this, R.array.roles, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        roleSpinner.setAdapter(adapter);
//
//        userEmail = getIntent().getStringExtra("userEmail");
//        if (!TextUtils.isEmpty(userEmail)) {
//            loadUserData(FirebaseUtils.decodeUserEmail(userEmail));
//        }
//
//        saveChangesButton.setOnClickListener(v -> saveChanges());
//    }
//
//    private void loadUserData(String email) {
//        String emailKey = FirebaseUtils.encodeUserEmail(email);
//        databaseRef.child(emailKey).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    User user = snapshot.getValue(User.class);
//                    if (user != null) {
//                        firstNameEdit.setText(user.getFirstName());
//                        lastNameEdit.setText(user.getLastName());
//                        emailEdit.setText(user.getEmail());
//                        phoneEdit.setText(user.getPhone());
//                        addressEdit.setText(user.getAddress());
//                        roleSpinner.setSelection(user.getRole().equals("admin") ? 1 : 0);
//                    }
//                } else {
//                    Toast.makeText(EditUserActivity.this, "User not found.", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(EditUserActivity.this, "Failed to load data.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void saveChanges() {
//        String firstName = firstNameEdit.getText().toString().trim();
//        String lastName = lastNameEdit.getText().toString().trim();
//        String phone = phoneEdit.getText().toString().trim();
//        String address = addressEdit.getText().toString().trim();
//        String selectedRole = roleSpinner.getSelectedItem().toString();
//        String emailKey = FirebaseUtils.encodeUserEmail(userEmail);
//
//        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName)) {
//            Toast.makeText(this, "Fill all required fields.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        User updatedUser = new User(firstName, lastName, phone, address, userEmail, selectedRole);
//
//        databaseRef.child(emailKey).setValue(updatedUser).addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                Toast.makeText(this, "User updated!", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        });
//    }
//}



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