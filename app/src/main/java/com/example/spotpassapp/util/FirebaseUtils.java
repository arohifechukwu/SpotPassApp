package com.example.spotpassapp.util;

public class FirebaseUtils {
    public static String encodeUserEmail(String email) {
        return email.replace(".", ",");
    }

    public static String decodeUserEmail(String emailKey) {
        return emailKey.replace(",", ".");
    }
}