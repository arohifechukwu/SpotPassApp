package com.example.spotpassapp.model;

public class Event {
    private String title;
    private String description;
    private String imageUrl;
    private double price;
    private String date;
    private String location;
    private String time;

    // Constructor with all fields
    public Event(String title, String description, String location, double price, String date, String time, String imageUrl) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.price = price;
        this.date = date;
        this.time = time;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}