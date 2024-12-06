package com.example.pizzaapp.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzaapp.R;
import com.example.pizzaapp.pizza.Order;
import com.example.pizzaapp.pizza.Pizza;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for RecyclerView to display a list of Orders.
 */
public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    private List<Order> orders = new ArrayList<>();
    private final OnOrderClickListener listener;

    public OrdersAdapter(OnOrderClickListener listener) {
        this.listener = listener;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrdersAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.bind(order, listener);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderNumberView;
        TextView orderTotalView;
        TextView orderPizzasView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderNumberView = itemView.findViewById(R.id.orderNumberView);
            orderTotalView = itemView.findViewById(R.id.orderTotalView);
            orderPizzasView = itemView.findViewById(R.id.orderPizzasView);
        }

        public void bind(Order order, OnOrderClickListener listener) {
            orderNumberView.setText("Order Number: " + order.getNumber());
            orderTotalView.setText("Total Price: " + String.format("%.2f", order.getTotalPrice()));

            // Convert pizza list to a multiline string
            StringBuilder sb = new StringBuilder();
            List<Pizza> pizzas = order.getPizzas();
            if (pizzas.isEmpty()) {
                sb.append("No pizzas");
            } else {
                for (Pizza p : pizzas) {
                    sb.append(p.toString()).append("\n");
                }
            }
            orderPizzasView.setText(sb.toString().trim());

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onOrderClick(order);
                }
            });
        }
    }
}
