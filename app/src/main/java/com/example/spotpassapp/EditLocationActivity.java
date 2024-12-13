//package com.example.spotpassapp;
//
//import android.content.Intent;
//import android.location.Address;
//import android.location.Geocoder;
//import android.os.Bundle;
//import android.view.inputmethod.EditorInfo;
//import android.widget.Button;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.material.textfield.TextInputEditText;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Locale;
//
//public class EditLocationActivity extends AppCompatActivity implements OnMapReadyCallback {
//
//    private GoogleMap mMap;
//    private String selectedLocationName;
//    private Button selectLocationButton;
//    private TextInputEditText locationSearchInput;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_edit_location);
//
//        locationSearchInput = findViewById(R.id.locationSearchInput);
//        selectLocationButton = findViewById(R.id.selectLocationButton);
//
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        if (mapFragment != null) {
//            mapFragment.getMapAsync(this);
//        }
//
//        locationSearchInput.setOnEditorActionListener((v, actionId, event) -> {
//            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                searchLocation();
//                return true;
//            }
//            return false;
//        });
//
//        selectLocationButton.setOnClickListener(v -> returnSelectedLocation());
//    }
//
//    @Override
//    public void onMapReady(@NonNull GoogleMap googleMap) {
//        mMap = googleMap;
//        mMap.setOnMapClickListener(latLng -> {
//            mMap.clear();
//            getAddressFromLatLng(latLng);
//            mMap.addMarker(new MarkerOptions().position(latLng).title(selectedLocationName));
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
//        });
//    }
//
//    private void searchLocation() {
//        String location = locationSearchInput.getText().toString().trim();
//        if (location.isEmpty()) {
//            Toast.makeText(this, "Please enter a location.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//        try {
//            List<Address> addressList = geocoder.getFromLocationName(location, 1);
//            if (!addressList.isEmpty()) {
//                Address address = addressList.get(0);
//                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
//
//                selectedLocationName = getFormattedAddress(address);
//                mMap.clear();
//                mMap.addMarker(new MarkerOptions().position(latLng).title(selectedLocationName));
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
//            } else {
//                Toast.makeText(this, "Location not found.", Toast.LENGTH_SHORT).show();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            Toast.makeText(this, "Error fetching location.", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void getAddressFromLatLng(LatLng latLng) {
//        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//        try {
//            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
//            if (!addresses.isEmpty()) {
//                selectedLocationName = getFormattedAddress(addresses.get(0));
//            } else {
//                selectedLocationName = "Unknown Location";
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            selectedLocationName = "Error Fetching Location";
//        }
//    }
//
//    private String getFormattedAddress(Address address) {
//        String city = address.getLocality() != null ? address.getLocality() : "";
//        String state = address.getAdminArea() != null ? address.getAdminArea() : "";
//        String country = address.getCountryName() != null ? address.getCountryName() : "";
//        return String.format("%s, %s, %s", city, state, country).trim().replaceAll(", ,", ",");
//    }
//
//    private void returnSelectedLocation() {
//        if (selectedLocationName == null || selectedLocationName.isEmpty()) {
//            Toast.makeText(this, "Please select a location.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        Intent resultIntent = new Intent();
//        resultIntent.putExtra("selected_location", selectedLocationName);
//        setResult(RESULT_OK, resultIntent);
//        finish();
//    }
//}



package com.example.spotpassapp;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class EditLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String selectedLocationName;
    private Button selectLocationButton, searchLocationButton;
    private TextInputEditText locationSearchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location);

        // Initialize Views
        locationSearchInput = findViewById(R.id.locationSearchInput);
        selectLocationButton = findViewById(R.id.selectLocationButton);
        searchLocationButton = findViewById(R.id.searchLocationButton);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        searchLocationButton.setOnClickListener(v -> searchLocation());
        selectLocationButton.setOnClickListener(v -> returnSelectedLocation());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(latLng -> {
            mMap.clear();
            getAddressFromLatLng(latLng);
            mMap.addMarker(new MarkerOptions().position(latLng).title(selectedLocationName));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        });
    }

    private void searchLocation() {
        String location = locationSearchInput.getText().toString().trim();
        if (location.isEmpty()) {
            Toast.makeText(this, "Please enter a location.", Toast.LENGTH_SHORT).show();
            return;
        }

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocationName(location, 1);
            if (!addressList.isEmpty()) {
                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                selectedLocationName = getFormattedAddress(address);
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title(selectedLocationName));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            } else {
                Toast.makeText(this, "Location not found.", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error fetching location.", Toast.LENGTH_SHORT).show();
        }
    }

    private void getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (!addresses.isEmpty()) {
                selectedLocationName = getFormattedAddress(addresses.get(0));
            } else {
                selectedLocationName = "Unknown Location";
            }
        } catch (IOException e) {
            e.printStackTrace();
            selectedLocationName = "Error Fetching Location";
        }
    }

    private String getFormattedAddress(Address address) {
        String city = address.getLocality() != null ? address.getLocality() : "";
        String state = address.getAdminArea() != null ? address.getAdminArea() : "";
        String country = address.getCountryName() != null ? address.getCountryName() : "";
        return String.format("%s, %s, %s", city, state, country).trim().replaceAll(", ,", ",");
    }

    private void returnSelectedLocation() {
        if (selectedLocationName == null || selectedLocationName.isEmpty()) {
            Toast.makeText(this, "Please select a location.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra("selected_location", selectedLocationName);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}