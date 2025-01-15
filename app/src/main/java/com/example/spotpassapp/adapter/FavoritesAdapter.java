package com.example.spotpassapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.spotpassapp.CheckoutActivity;
import com.example.spotpassapp.R;
import com.example.spotpassapp.model.Event;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesAdapter.FavoritesViewHolder> {

    private Context context;
    private List<Event> favoriteEvents;
    private DatabaseReference favoritesDatabase;

    public FavoritesAdapter(Context context, List<Event> favoriteEvents) {
        this.context = context;
        this.favoriteEvents = favoriteEvents;
        this.favoritesDatabase = FirebaseDatabase.getInstance().getReference("favorites");
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite_event, parent, false);
        return new FavoritesViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {
        Event event = favoriteEvents.get(position);

        holder.eventTitle.setText(event.getTitle());
        holder.eventPrice.setText(String.format("$%.2f", event.getPrice()));
        holder.eventDate.setText(event.getDate());
        holder.eventTime.setText(event.getTime());
        holder.eventLocation.setText(event.getLocation());
        holder.eventDescription.setText(event.getDescription());
        Glide.with(context).load(event.getImageUrl()).into(holder.eventImage);


        // Delete Button
        holder.deleteButton.setOnClickListener(v -> {
            String userId = FirebaseAuth.getInstance().getUid();
            if (userId != null) {
                favoritesDatabase.child(userId).child(event.getId()).removeValue().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Event removed from favorites.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Failed to remove event.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(context, "User not logged in!", Toast.LENGTH_SHORT).show();
            }
        });



        // Book Ticket Button
        holder.bookTicketButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, CheckoutActivity.class);
            intent.putExtra("title", event.getTitle());
            intent.putExtra("description", event.getDescription());
            intent.putExtra("location", event.getLocation());
            intent.putExtra("price", event.getPrice());
            intent.putExtra("date", event.getDate());
            intent.putExtra("time", event.getTime());
            intent.putExtra("imageUrl", event.getImageUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return favoriteEvents.size();
    }

    public static class FavoritesViewHolder extends RecyclerView.ViewHolder {
        TextView eventTitle, eventDescription, eventLocation, eventPrice, eventDate, eventTime;
        ImageView eventImage;
        Button deleteButton, bookTicketButton;

        public FavoritesViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.eventTitle);
            eventDescription = itemView.findViewById(R.id.eventDescription);
            eventLocation = itemView.findViewById(R.id.eventLocation);
            eventPrice = itemView.findViewById(R.id.eventPrice);
            eventDate = itemView.findViewById(R.id.eventDate);
            eventTime = itemView.findViewById(R.id.eventTime);
            eventImage = itemView.findViewById(R.id.eventImage);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            bookTicketButton = itemView.findViewById(R.id.bookTicketButton);
        }
    }
}