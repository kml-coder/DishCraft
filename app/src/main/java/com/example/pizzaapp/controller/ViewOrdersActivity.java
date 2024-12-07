package com.example.pizzaapp.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzaapp.R;
import com.example.pizzaapp.pizza.Order;
import com.example.pizzaapp.pizza.OrderManager;
import com.example.pizzaapp.pizza.Pizza;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewOrdersActivity displays orders in a RecyclerView.
 * Allows canceling orders and now also shows selected order's pizzas in a ListView.
 */
public class ViewOrdersActivity extends AppCompatActivity implements OrdersAdapter.OnOrderClickListener {

    private RecyclerView ordersRecyclerView;
    private OrdersAdapter ordersAdapter;
    private TextView outputArea;
    private Button cancelOrderButton;
    private ListView pizzasListView; // ListView 추가

    private Order selectedOrder = null;
    private ArrayAdapter<String> pizzasAdapter; // ListView용 Adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
        outputArea = findViewById(R.id.textOutput);
        cancelOrderButton = findViewById(R.id.buttonCancelOrder);
        pizzasListView = findViewById(R.id.pizzasListView);

        ordersAdapter = new OrdersAdapter(this);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        ordersRecyclerView.setAdapter(ordersAdapter);

        // 초기엔 선택된 주문이 없으므로 빈 리스트로 ListView 세팅
        pizzasAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        pizzasListView.setAdapter(pizzasAdapter);

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
    }

    @Override
    public void onOrderClick(Order order) {
        selectedOrder = order;
        logMessage("Selected Order: " + order.getNumber());

        // 선택된 주문의 피자 목록을 ListView에 표시
        updatePizzasListView(selectedOrder);
    }

    private void updatePizzasListView(Order order) {
        if (order == null) {
            pizzasAdapter.clear();
            return;
        }
        List<String> pizzaStrings = new ArrayList<>();
        for (Pizza p : order.getPizzas()) {
            pizzaStrings.add(p.toString());
        }
        pizzasAdapter.clear();
        pizzasAdapter.addAll(pizzaStrings);
        pizzasAdapter.notifyDataSetChanged();
    }

    private void handleCancelOrder() {
        if (selectedOrder != null) {
            OrderManager.getInstance().removeOrder(selectedOrder);
            logMessage("Order cancelled: " + selectedOrder.getNumber());
            selectedOrder = null;
            updatePizzasListView(null); // 선택 취소 시 ListView 비움
        } else {
            logMessage("No order selected to cancel.");
        }
    }

    private void logMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        outputArea.append(message + "\n");
    }
}
