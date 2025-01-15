package com.example.spotpassapp.model;

public class Event {
    private String id; // New field for storing the event's ID
    private String title;
    private String description;
    private String location;
    private String date;
    private String time;
    private double price;
    private String imageUrl;

    // No-argument constructor (required for Firebase)
    public Event() {}

    // Constructor with all fields
    public Event(String title, String description, String location, String date, String time, double price, String imageUrl) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.date = date;
        this.time = time;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    // Getter and setter for id
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    // Getters and setters for other fields
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}