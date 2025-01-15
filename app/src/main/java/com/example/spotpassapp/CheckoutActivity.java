package com.example.spotpassapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    private TextView titleTextView, descriptionTextView, locationTextView, priceTextView, federalTaxTextView, provincialTaxTextView, totalTextView, quantityTextView;
    private ImageView eventImageView;
    private MaterialButton payButton, incrementButton, decrementButton;

    private static final double FEDERAL_TAX = 0.05;
    private static final double PROVINCIAL_TAX_QC = 0.09975;
    private static final double PROVINCIAL_TAX_ON = 0.08;
    private static final double PROVINCIAL_TAX_MB = 0.07; // Manitoba tax example

    private double eventPrice;
    private double totalPrice;
    private int quantity = 1;

    private static final Map<String, String> CITY_TO_PROVINCE = new HashMap<>();

    static {
        Map<String, String[]> provincesCities = new HashMap<>();
        provincesCities.put("Alberta", new String[]{"Edmonton", "Calgary", "Red Deer", "Lethbridge", "Medicine Hat", "Grande Prairie", "Fort McMurray", "Airdrie", "St. Albert"});
        provincesCities.put("British Columbia", new String[]{"Victoria", "Vancouver", "Surrey", "Burnaby", "Richmond", "Kelowna", "Kamloops", "Nanaimo", "Prince George", "Abbotsford"});
        provincesCities.put("Manitoba", new String[]{"Winnipeg", "Brandon", "Steinbach", "Thompson", "Portage la Prairie", "Dauphin", "Selkirk"});
        provincesCities.put("New Brunswick", new String[]{"Fredericton", "Moncton", "Saint John", "Bathurst", "Miramichi", "Edmundston", "Dieppe", "Campbellton"});
        provincesCities.put("Newfoundland and Labrador", new String[]{"St. John’s", "Corner Brook", "Mount Pearl", "Gander", "Happy Valley-Goose Bay", "Labrador City"});
        provincesCities.put("Nova Scotia", new String[]{"Halifax", "Sydney", "Dartmouth", "Truro", "New Glasgow", "Amherst", "Bridgewater"});
        provincesCities.put("Ontario", new String[]{"Toronto", "Ottawa", "Mississauga", "Brampton", "Hamilton", "London", "Windsor", "Kitchener", "Sudbury", "Thunder Bay", "Guelph", "Kingston", "Niagara Falls"});
        provincesCities.put("Prince Edward Island", new String[]{"Charlottetown", "Summerside", "Stratford", "Cornwall"});
        provincesCities.put("Quebec", new String[]{"Quebec City", "Montreal", "Laval", "Gatineau", "Sherbrooke", "Trois-Rivières", "Saguenay", "Lévis", "Drummondville"});
        provincesCities.put("Saskatchewan", new String[]{"Regina", "Saskatoon", "Prince Albert", "Moose Jaw", "Swift Current", "Yorkton", "North Battleford"});
        provincesCities.put("Northwest Territories", new String[]{"Yellowknife", "Hay River", "Inuvik", "Fort Smith"});
        provincesCities.put("Nunavut", new String[]{"Iqaluit", "Rankin Inlet", "Arviat", "Cambridge Bay", "Pond Inlet"});
        provincesCities.put("Yukon", new String[]{"Whitehorse", "Dawson City", "Watson Lake", "Haines Junction"});

        for (Map.Entry<String, String[]> entry : provincesCities.entrySet()) {
            String province = entry.getKey();
            for (String city : entry.getValue()) {
                CITY_TO_PROVINCE.put(city.trim().toLowerCase(Locale.ROOT), province);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Initialize views
        eventImageView = findViewById(R.id.eventImageView);
        titleTextView = findViewById(R.id.eventTitle);
        descriptionTextView = findViewById(R.id.eventDescription);
        locationTextView = findViewById(R.id.eventLocation);
        priceTextView = findViewById(R.id.eventPrice);
        federalTaxTextView = findViewById(R.id.federalTax);
        provincialTaxTextView = findViewById(R.id.provincialTax);
        totalTextView = findViewById(R.id.totalPrice);
        quantityTextView = findViewById(R.id.quantityTextView);
        payButton = findViewById(R.id.payButton);
        incrementButton = findViewById(R.id.incrementButton);
        decrementButton = findViewById(R.id.decrementButton);

        // Get event details from intent
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            String description = intent.getStringExtra("description");
            String location = intent.getStringExtra("location");
            eventPrice = intent.getDoubleExtra("price", 0.0);
            String imageUrl = intent.getStringExtra("imageUrl");

            // Set event details
            titleTextView.setText(title);
            descriptionTextView.setText(description);
            locationTextView.setText(location);
            Glide.with(this).load(imageUrl).into(eventImageView);

            // Calculate initial totals
            calculateTotals(location);
        }

        // Increment and decrement quantity
        incrementButton.setOnClickListener(v -> {
            quantity++;
            calculateTotals(locationTextView.getText().toString());
        });

        decrementButton.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                calculateTotals(locationTextView.getText().toString());
            }
        });

        // Set up payment button
        payButton.setOnClickListener(v -> Toast.makeText(this, "Proceeding to Payment", Toast.LENGTH_SHORT).show());

        // Set up Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        setupBottomNavigation(bottomNavigationView);
    }

    private void calculateTotals(String city) {
        double subtotal = eventPrice * quantity;
        double federalTax = subtotal * FEDERAL_TAX;
        double provincialTax = subtotal * getProvincialTax(city);
        totalPrice = subtotal + federalTax + provincialTax;

        // Update UI
        quantityTextView.setText(String.format(Locale.getDefault(), "%d", quantity));
        priceTextView.setText(String.format(Locale.getDefault(), "$%.2f", subtotal));
        federalTaxTextView.setText(String.format(Locale.getDefault(), "Federal Tax: $%.2f", federalTax));
        provincialTaxTextView.setText(String.format(Locale.getDefault(), "Provincial Tax: $%.2f", provincialTax));
        totalTextView.setText(String.format(Locale.getDefault(), "Total: $%.2f", totalPrice));
    }

    private double getProvincialTax(String city) {
        if (city == null || city.isEmpty()) {
            return 0.0;
        }

        String province = CITY_TO_PROVINCE.get(city.trim().toLowerCase(Locale.ROOT));
        if (province == null) {
            return 0.0; // Default to no provincial tax if city is not found
        }

        switch (province) {
            case "Quebec":
                return PROVINCIAL_TAX_QC;
            case "Ontario":
                return PROVINCIAL_TAX_ON;
            case "Manitoba":
                return PROVINCIAL_TAX_MB;
            // Add more provinces and their respective tax rates as needed
            default:
                return 0.0;
        }
    }

    private void setupBottomNavigation(BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, HomeActivity.class));
                return true;
            } else if (itemId == R.id.navigation_search) {
                startActivity(new Intent(this, ExploreEventsActivity.class));
                return true;
            } else if (itemId == R.id.navigation_favorites) {
                startActivity(new Intent(this, FavoritesActivity.class));
                return true;
            } else if (itemId == R.id.navigation_profile) {
                startActivity(new Intent(this, UserDashboardActivity.class));
                return true;
            }
            return false;
        });
    }
}