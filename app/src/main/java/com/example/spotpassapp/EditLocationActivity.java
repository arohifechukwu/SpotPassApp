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
    private String selectedCity;
    private Button addCityButton, setLocationButton, searchLocationButton;
    private TextInputEditText locationSearchInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location);

        // Initialize Views
        locationSearchInput = findViewById(R.id.locationSearchInput);
        addCityButton = findViewById(R.id.addCityButton);
        setLocationButton = findViewById(R.id.setLocationButton);
        searchLocationButton = findViewById(R.id.searchLocationButton);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Button Listeners
        searchLocationButton.setOnClickListener(v -> searchLocation());
        setLocationButton.setOnClickListener(v -> returnSelectedCity());
        addCityButton.setOnClickListener(v -> setCityFromInput());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(latLng -> {
            mMap.clear();
            getCityFromLatLng(latLng);
            mMap.addMarker(new MarkerOptions().position(latLng).title(selectedCity));
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

                selectedCity = getCityFromAddress(address);
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title(selectedCity));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            } else {
                Toast.makeText(this, "Location not found.", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error fetching location.", Toast.LENGTH_SHORT).show();
        }
    }

    private void getCityFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (!addresses.isEmpty()) {
                selectedCity = getCityFromAddress(addresses.get(0));
            } else {
                selectedCity = "Unknown City";
            }
        } catch (IOException e) {
            e.printStackTrace();
            selectedCity = "Error Fetching City";
        }
    }

    private String getCityFromAddress(Address address) {
        return address.getLocality() != null ? address.getLocality() : "Unknown City";
    }

    private void returnSelectedCity() {
        if (selectedCity == null || selectedCity.isEmpty()) {
            Toast.makeText(this, "Please select a city on the map.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra("user_city", selectedCity);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void setCityFromInput() {
        String inputCity = locationSearchInput.getText().toString().trim();
        if (inputCity.isEmpty()) {
            Toast.makeText(this, "Please enter a city name.", Toast.LENGTH_SHORT).show();
            return;
        }

        selectedCity = inputCity;

        Intent resultIntent = new Intent();
        resultIntent.putExtra("user_city", selectedCity);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
