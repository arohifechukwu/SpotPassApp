//package com.example.spotpassapp;
//
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.widget.EditText;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.spotpassapp.adapter.EventAdapter;
//import com.example.spotpassapp.model.Event;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class ViewEventsActivity extends AppCompatActivity {
//
//    private RecyclerView recyclerView;
//    private EditText searchField;
//    private EventAdapter eventAdapter;
//    private List<Event> eventList;
//    private DatabaseReference databaseRef;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_view_events);
//
//        // Initialize views
//        recyclerView = findViewById(R.id.recyclerView);
//        searchField = findViewById(R.id.searchField);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        eventList = new ArrayList<>();
//        databaseRef = FirebaseDatabase.getInstance().getReference("events").child("events");
//
//        // Load all events
//        fetchAllEvents();
//
//        // Search functionality
//        searchField.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                filterEvents(s.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) { }
//        });
//    }
//
//    private void fetchAllEvents() {
//        databaseRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                eventList.clear();
//                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
//                    Event event = eventSnapshot.getValue(Event.class);
//                    if (event != null) {
//                        event.setKey(eventSnapshot.getKey());
//                        eventList.add(event);
//                    }
//                }
//                eventAdapter = new EventAdapter(ViewEventsActivity.this, eventList);
//                recyclerView.setAdapter(eventAdapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle database error
//            }
//        });
//    }
//
//    private void filterEvents(String query) {
//        List<Event> filteredList = new ArrayList<>();
//        for (Event event : eventList) {
//            if (event.getTitle().toLowerCase().contains(query.toLowerCase()) ||
//                    event.getLocation().toLowerCase().contains(query.toLowerCase()) ||
//                    event.getDate().toLowerCase().contains(query.toLowerCase()) ||
//                    event.getTime().toLowerCase().contains(query.toLowerCase())) {
//                filteredList.add(event);
//            }
//        }
//        eventAdapter.updateList(filteredList);
//    }
//}