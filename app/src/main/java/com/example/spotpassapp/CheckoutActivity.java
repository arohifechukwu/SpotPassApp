package com.example.spotpassapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import java.util.HashMap;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {

    private TextView titleTextView, descriptionTextView, locationTextView, priceTextView, taxTextView, totalTextView;
    private ImageView eventImageView;
    private MaterialButton payButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private PaymentSheet paymentSheet;
    private String paymentIntentClientSecret;

    private static final double FEDERAL_TAX = 0.05;
    private static final double PROVINCIAL_TAX_QC = 0.09975;
    private static final double PROVINCIAL_TAX_ON = 0.08;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        // Initialize Firebase and Stripe
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        PaymentConfiguration.init(this, "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");
        paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);

        // Initialize views
        eventImageView = findViewById(R.id.eventImageView);
        titleTextView = findViewById(R.id.eventTitle);
        descriptionTextView = findViewById(R.id.eventDescription);
        locationTextView = findViewById(R.id.eventLocation);
        priceTextView = findViewById(R.id.eventPrice);
        taxTextView = findViewById(R.id.taxDetails);
        totalTextView = findViewById(R.id.totalPrice);
        payButton = findViewById(R.id.payButton);

        // Get event details from intent
        Intent intent = getIntent();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            String description = intent.getStringExtra("description");
            String location = intent.getStringExtra("location");
            double price = intent.getDoubleExtra("price", 0.0);
            String imageUrl = intent.getStringExtra("imageUrl");

            // Set event details
            titleTextView.setText(title);
            descriptionTextView.setText(description);
            locationTextView.setText(location);
            priceTextView.setText(String.format("$%.2f", price));
            Glide.with(this).load(imageUrl).into(eventImageView);

            // Calculate taxes
            double provincialTax = getProvincialTax(location);
            double federalTax = price * FEDERAL_TAX;
            double totalTax = price * provincialTax + federalTax;
            double totalPrice = price + totalTax;

            // Display taxes and total price
            taxTextView.setText(String.format("Tax (Federal + Provincial): $%.2f", totalTax));
            totalTextView.setText(String.format("Total: $%.2f", totalPrice));

            // Set up payment button
            payButton.setOnClickListener(v -> createStripePaymentIntent(totalPrice));
        }
    }

    private double getProvincialTax(String location) {
        if (location.equalsIgnoreCase("Quebec")) {
            return PROVINCIAL_TAX_QC;
        } else if (location.equalsIgnoreCase("Ontario")) {
            return PROVINCIAL_TAX_ON;
        }
        return 0.0; // Default tax for other provinces
    }

    private void createStripePaymentIntent(double totalPrice) {
        String uid = mAuth.getCurrentUser().getUid();
        Map<String, Object> paymentData = new HashMap<>();
        paymentData.put("amount", (int) (totalPrice * 100)); // Stripe expects amount in cents
        paymentData.put("currency", "usd");
        paymentData.put("payment_method_types", new String[]{"card"});

        // Save payment data to Firestore
        db.collection("customers")
                .document(uid)
                .collection("payments")
                .add(paymentData)
                .addOnSuccessListener(documentReference -> {
                    documentReference.get()
                            .addOnSuccessListener(documentSnapshot -> {
                                paymentIntentClientSecret = documentSnapshot.getString("client_secret");
                                presentPaymentSheet();
                            })
                            .addOnFailureListener(e -> Toast.makeText(CheckoutActivity.this, "Failed to retrieve client secret.", Toast.LENGTH_SHORT).show());
                })
                .addOnFailureListener(e -> Toast.makeText(CheckoutActivity.this, "Failed to create PaymentIntent.", Toast.LENGTH_SHORT).show());
    }

    private void presentPaymentSheet() {
        PaymentSheet.Configuration configuration = new PaymentSheet.Configuration(
                "SpotPass Checkout"
        );

        paymentSheet.presentWithPaymentIntent(paymentIntentClientSecret, configuration);
    }

    private void onPaymentSheetResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            Toast.makeText(this, "Payment successful!", Toast.LENGTH_LONG).show();
            finish(); // Close activity after successful payment
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Toast.makeText(this, "Payment failed. Please try again.", Toast.LENGTH_LONG).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Toast.makeText(this, "Payment canceled.", Toast.LENGTH_LONG).show();
        }
    }
}