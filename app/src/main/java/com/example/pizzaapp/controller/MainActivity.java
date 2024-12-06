package com.example.pizzaapp.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pizzaapp.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Use the provided main.xml layout

        TextView statusLabel = findViewById(R.id.statusLabel);
        Button orderButton = findViewById(R.id.orderButton);
        Button orderviewButton = findViewById(R.id.orderviewButton);

        // If you want to set text dynamically:
        // statusLabel.setText("Welcome to RU Pizzeria!");

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleOrderPizza();
            }
        });

        orderviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleViewOrders();
            }
        });
    }

    private void handleOrderPizza() {
        Intent intent = new Intent(this, OrderPizzaActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Navigating to Order Pizza view...", Toast.LENGTH_SHORT).show();
    }

    private void handleViewOrders() {
        Intent intent = new Intent(this, ViewOrdersActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Navigating to View Orders view...", Toast.LENGTH_SHORT).show();
    }
}
