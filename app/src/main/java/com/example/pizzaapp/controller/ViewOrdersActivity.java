package com.example.pizzaapp.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzaapp.R;
import com.example.pizzaapp.pizza.Order;
import com.example.pizzaapp.pizza.OrderManager;
import com.example.pizzaapp.pizza.Pizza;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import java.util.List;

/**
 * ViewOrdersActivity replaces the JavaFX ViewOrdersController.
 * It displays orders in a RecyclerView, allows canceling orders, and exporting them to a text file.
 */
public class ViewOrdersActivity extends AppCompatActivity implements OrdersAdapter.OnOrderClickListener {

    private RecyclerView ordersRecyclerView;
    private OrdersAdapter ordersAdapter;
    private TextView outputArea;

    private Button cancelOrderButton;
    private Button exportOrdersButton;

    // This will keep track of the currently selected order (if any)
    private Order selectedOrder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
        outputArea = findViewById(R.id.textOutput);
        cancelOrderButton = findViewById(R.id.buttonCancelOrder);
        exportOrdersButton = findViewById(R.id.buttonExportOrders);

        ordersAdapter = new OrdersAdapter(this);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ordersRecyclerView.setAdapter(ordersAdapter);

        // Observe LiveData from OrderManager
        OrderManager.getInstance().getOrders().observe(this, new Observer<List<Order>>() {
            @Override
            public void onChanged(List<Order> orders) {
                ordersAdapter.setOrders(orders);
            }
        });

        cancelOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCancelOrder();
            }
        });

        exportOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleExportOrders();
            }
        });
    }

    /**
     * Called when an order is clicked in the RecyclerView.
     * We store the selected order to potentially cancel it.
     */
    @Override
    public void onOrderClick(Order order) {
        selectedOrder = order;
        logMessage("Selected Order: " + order.getNumber());
    }

    /**
     * Handles cancellation of the currently selected order.
     */
    private void handleCancelOrder() {
        if (selectedOrder != null) {
            OrderManager.getInstance().removeOrder(selectedOrder);
            logMessage("Order cancelled: " + selectedOrder.getNumber());
            selectedOrder = null; // reset selection
        } else {
            logMessage("No order selected to cancel.");
        }
    }

    /**
     * Handles exporting all orders to a text file.
     */
    private void handleExportOrders() {
        List<Order> orders = OrderManager.getInstance().getOrders().getValue();
        if (orders == null || orders.isEmpty()) {
            logMessage("No orders to export.");
            return;
        }

        // Exporting orders to a file in internal storage named "orders_export.txt"
        try (FileOutputStream fos = openFileOutput("orders_export.txt", Context.MODE_PRIVATE);
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos))) {

            for (Order order : orders) {
                writer.write("Order Number: " + order.getNumber() + "\n");
                writer.write("Total Price: " + String.format("%.2f", order.getTotalPrice()) + "\n");
                writer.write("Pizzas:\n");
                for (Pizza pizza : order.getPizzas()) {
                    writer.write(pizza.toString() + "\n");
                }
                writer.write("\n");
            }
            logMessage("Orders exported successfully to internal storage file: orders_export.txt");
        } catch (IOException e) {
            logMessage("Error exporting orders: " + e.getMessage());
        }
    }

    private void logMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        outputArea.append(message + "\n");
    }
}
