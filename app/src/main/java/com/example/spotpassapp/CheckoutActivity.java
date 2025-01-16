package com.example.spotpassapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

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

    private double eventPrice;
    private double totalPrice;
    private int quantity = 1;

    private PaymentSheet paymentSheet;
    private String paymentIntentClientSecret;

    private String publishableKey = "pk_test_51QhbeV05xk3YjsYEpnnzNgiUPtz6ogl3ABraQcAh4AQSVugxWDuVlMqkc7llvoI9f8xBMEnL9o22eVx5bVQ5nxOv00LGQ1l3jQ"; // Replace with your test publishable key
    private String secretKey = "sk_test_51QhbeV05xk3YjsYEZA2eRnKYQJATOh8TIBUyBuHvZ81ZVXSKju5d1AYgHyiXHkCFbhFtDGrtA3R9KQ3JNyYGHBCE00gbxNiDvG"; // Replace with your test secret key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Initialize Stripe Payment
        PaymentConfiguration.init(this, publishableKey);
        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);

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

        // Get event details
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            String description = intent.getStringExtra("description");
            String location = intent.getStringExtra("location");
            eventPrice = intent.getDoubleExtra("price", 0.0);
            String imageUrl = intent.getStringExtra("imageUrl");

            titleTextView.setText(title);
            descriptionTextView.setText(description);
            locationTextView.setText(location);
            Glide.with(this).load(imageUrl).into(eventImageView);

            calculateTotals(location);
        }

        // Quantity management
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

        // Payment Button Listener
        payButton.setOnClickListener(v -> createPaymentIntent());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        setupBottomNavigation(bottomNavigationView);
    }

    private void calculateTotals(String city) {
        double subtotal = eventPrice * quantity;
        double federalTax = subtotal * FEDERAL_TAX;
        double provincialTax = subtotal * getProvincialTax(city);
        totalPrice = subtotal + federalTax + provincialTax;

        quantityTextView.setText(String.format(Locale.getDefault(), "%d", quantity));
        priceTextView.setText(String.format(Locale.getDefault(), "$%.2f", subtotal));
        federalTaxTextView.setText(String.format(Locale.getDefault(), "Federal Tax: $%.2f", federalTax));
        provincialTaxTextView.setText(String.format(Locale.getDefault(), "Provincial Tax: $%.2f", provincialTax));
        totalTextView.setText(String.format(Locale.getDefault(), "Total: $%.2f", totalPrice));
    }

    private double getProvincialTax(String city) {
        if ("Quebec".equalsIgnoreCase(city)) {
            return PROVINCIAL_TAX_QC;
        } else if ("Ontario".equalsIgnoreCase(city)) {
            return PROVINCIAL_TAX_ON;
        }
        return 0.0;
    }

    private void createPaymentIntent() {
        StringRequest request = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/payment_intents",
                response -> {
                    try {
                        JSONObject responseJson = new JSONObject(response);
                        paymentIntentClientSecret = responseJson.getString("client_secret");
                        showPaymentSheet();
                    } catch (JSONException e) {
                        Toast.makeText(this, "Error parsing payment intent", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> Toast.makeText(this, "Error creating payment intent", Toast.LENGTH_SHORT).show()) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + secretKey);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("amount", String.valueOf((int) (totalPrice * 100))); // Convert dollars to cents
                params.put("currency", "cad");
                params.put("automatic_payment_methods[enabled]", "true");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void showPaymentSheet() {
        PaymentSheet.Configuration configuration = new PaymentSheet.Configuration("SpotPass App");
        paymentSheet.presentWithPaymentIntent(paymentIntentClientSecret, configuration);
    }

    private void onPaymentSheetResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            Toast.makeText(this, "Payment successful!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Payment failed or canceled", Toast.LENGTH_SHORT).show();
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