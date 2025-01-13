//package com.example.spotpassapp;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Address;
//import android.location.Geocoder;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationCallback;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationResult;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Locale;
//
//public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback {
//
//    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
//
//    private FusedLocationProviderClient fusedLocationClient;
//    private GoogleMap mMap;
//    private LatLng userLocation;
//    private String userAddress;
//
//    private LocationRequest locationRequest;
//    private LocationCallback locationCallback;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_location);
//
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        if (mapFragment != null) {
//            mapFragment.getMapAsync(this);
//        }
//
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//
//        checkLocationPermission();
//    }
//
//    private void checkLocationPermission() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            fetchLocation();
//        } else {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    LOCATION_PERMISSION_REQUEST_CODE);
//        }
//    }
//
//    private void fetchLocation() {
//        locationRequest = LocationRequest.create()
//                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                .setInterval(2000)  // Location check every 2 seconds
//                .setFastestInterval(1000)
//                .setSmallestDisplacement(5);  // Trigger update after moving 5 meters
//
//        locationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(@NonNull LocationResult locationResult) {
//                if (locationResult == null || locationResult.getLastLocation() == null) {
//                    Toast.makeText(LocationActivity.this, "Unable to detect location", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                double latitude = locationResult.getLastLocation().getLatitude();
//                double longitude = locationResult.getLastLocation().getLongitude();
//
//                userLocation = new LatLng(latitude, longitude);
//
//                if (mMap != null) {
//                    mMap.clear();
//                    mMap.addMarker(new MarkerOptions()
//                            .position(userLocation)
//                            .title("You are here"));
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 17));
//                }
//
//                getAddressFromLocation(latitude, longitude);
//
//                fusedLocationClient.removeLocationUpdates(locationCallback);
//
//                new Handler().postDelayed(() -> {
//                    Intent intent = new Intent(LocationActivity.this, HomeActivity.class);
//                    intent.putExtra("user_location", userAddress);
//                    startActivity(intent);
//                    finish();
//                }, 3000);  // Wait 3 seconds before navigating
//            }
//        };
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
//        }
//    }
//
//    private void getAddressFromLocation(double latitude, double longitude) {
//        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//        try {
//            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
//            if (addresses != null && !addresses.isEmpty()) {
//                Address address = addresses.get(0);
//                userAddress = String.format("%s, %s, %s",
//                        address.getThoroughfare() != null ? address.getThoroughfare() : "Street Unknown",
//                        address.getLocality() != null ? address.getLocality() : "City Unknown",
//                        address.getAdminArea() != null ? address.getAdminArea() : "Province Unknown");
//            } else {
//                userAddress = "Unknown Location";
//            }
//        } catch (IOException e) {
//            Log.e("LocationError", "Error fetching location", e);
//            userAddress = "Location fetch error";
//        }
//    }
//
//    @Override
//    public void onMapReady(@NonNull GoogleMap googleMap) {
//        mMap = googleMap;
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            fetchLocation();
//        } else {
//            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
//        }
//    }
//}




package com.example.spotpassapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;

    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap mMap;
    private LatLng userLocation;
    private String userAddress;

    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        checkLocationPermission();
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fetchLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void fetchLocation() {
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(2000)  // Location check every 2 seconds
                .setFastestInterval(1000)
                .setSmallestDisplacement(5);  // Trigger update after moving 5 meters

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult == null || locationResult.getLastLocation() == null) {
                    Toast.makeText(LocationActivity.this, "Unable to detect location", Toast.LENGTH_SHORT).show();
                    return;
                }

                double latitude = locationResult.getLastLocation().getLatitude();
                double longitude = locationResult.getLastLocation().getLongitude();

                userLocation = new LatLng(latitude, longitude);

                if (mMap != null) {
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions()
                            .position(userLocation)
                            .title("You are here"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 17));
                }

                getAddressFromLocation(latitude, longitude);

                fusedLocationClient.removeLocationUpdates(locationCallback);

                new Handler().postDelayed(() -> {
                    Intent intent = new Intent(LocationActivity.this, HomeActivity.class);
                    intent.putExtra("user_location", userAddress);
                    startActivity(intent);
                    finish();
                }, 3000);  // Wait 3 seconds before navigating
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }

    private void getAddressFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);

                // Extract only street and city
                String street = address.getThoroughfare() != null ? address.getThoroughfare() : "Unknown Street";
                String city = address.getLocality() != null ? address.getLocality() : "Unknown City";

                userAddress = String.format("%s, %s", street, city);
            } else {
                userAddress = "Unknown Location";
            }
        } catch (IOException e) {
            Log.e("LocationError", "Error fetching location", e);
            userAddress = "Location fetch error";
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            fetchLocation();
        } else {
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
        }
    }
}