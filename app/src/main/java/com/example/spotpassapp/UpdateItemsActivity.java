package com.example.spotpassapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateItemsActivity extends AppCompatActivity {

    // Declare Views
    private EditText itemNameEditText, itemDescriptionEditText, itemPriceEditText;
    private Button updateItemButton, deleteItemButton;

    // Firebase Database Reference
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_items);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("items");

        // Bind Views
        itemNameEditText = findViewById(R.id.itemNameEditText);
        itemDescriptionEditText = findViewById(R.id.itemDescriptionEditText);
        itemPriceEditText = findViewById(R.id.itemPriceEditText);
        updateItemButton = findViewById(R.id.updateItemButton);
        deleteItemButton = findViewById(R.id.deleteItemButton);

        // Button Click Listeners
        updateItemButton.setOnClickListener(v -> updateItem());
        deleteItemButton.setOnClickListener(v -> deleteItem());
    }

    private void updateItem() {
        String itemName = itemNameEditText.getText().toString().trim();
        String itemDescription = itemDescriptionEditText.getText().toString().trim();
        String itemPrice = itemPriceEditText.getText().toString().trim();

        if (itemName.isEmpty() || itemDescription.isEmpty() || itemPrice.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference itemRef = databaseReference.child(itemName);
        itemRef.child("description").setValue(itemDescription);
        itemRef.child("price").setValue(itemPrice).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Item updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to update item.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteItem() {
        String itemName = itemNameEditText.getText().toString().trim();

        if (itemName.isEmpty()) {
            Toast.makeText(this, "Please enter the item name", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.child(itemName).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Item deleted successfully!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to delete item.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}