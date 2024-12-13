package com.example.spotpassapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.spotpassapp.EventDetailsActivity;
import com.example.spotpassapp.R;
import com.example.spotpassapp.model.Event;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private Context context;
    private List<Event> eventList;
    private List<Event> eventListFull; // Full copy for filtering

    public EventAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
        this.eventListFull = new ArrayList<>(eventList); // Initialize full list
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.eventTitle.setText(event.getTitle());
        holder.eventDescription.setText(event.getDescription());
        holder.eventPrice.setText(String.format("$%.2f", event.getPrice()));
        holder.eventDate.setText(event.getDate());
        holder.eventLocation.setText(event.getLocation());

        Glide.with(context).load(event.getImageUrl()).into(holder.eventImage);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EventDetailsActivity.class);
            intent.putExtra("title", event.getTitle());
            intent.putExtra("description", event.getDescription());
            intent.putExtra("location", event.getLocation());
            intent.putExtra("price", event.getPrice());
            intent.putExtra("date", event.getDate());
            intent.putExtra("time", event.getTime());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public void filter(String query) {
        eventList.clear();
        if (query.isEmpty()) {
            eventList.addAll(eventListFull);
        } else {
            String filterPattern = query.toLowerCase().trim();
            for (Event event : eventListFull) {
                if (event.getTitle().toLowerCase().contains(filterPattern) ||
                        event.getDescription().toLowerCase().contains(filterPattern) ||
                        event.getLocation().toLowerCase().contains(filterPattern)) {
                    eventList.add(event);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        ImageView eventImage;
        TextView eventTitle;
        TextView eventDescription;
        TextView eventPrice;
        TextView eventDate;
        TextView eventLocation;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            eventImage = itemView.findViewById(R.id.eventImage);
            eventTitle = itemView.findViewById(R.id.eventTitle);
            eventDescription = itemView.findViewById(R.id.eventDescription);
            eventPrice = itemView.findViewById(R.id.eventPrice);
            eventDate = itemView.findViewById(R.id.eventDate);
            eventLocation = itemView.findViewById(R.id.eventLocation);
        }
    }
}