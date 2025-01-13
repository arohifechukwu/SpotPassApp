package com.example.spotpassapp.model;

public class User {
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private String email;
    private String role;  // Role of the user ("admin" or "user")
    private boolean disabled;  // To track account status

    // Default constructor required for Firebase
    public User() { }

    // Parameterized constructor
    public User(String firstName, String lastName, String phone, String address, String email, String role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.role = role;
        this.disabled = false;  // Default to active
    }

    // Getters and setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
}