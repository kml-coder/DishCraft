package com.example.pizzaapp.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzaapp.R;

import java.util.List;

/**
 * ToppingsAdapter is a RecyclerView adapter to display a list of toppings.
 * Each topping is displayed as text only.
 * Clicking an item notifies the OnToppingClickListener.
 */
public class ToppingsAdapter extends RecyclerView.Adapter<ToppingsAdapter.ToppingViewHolder> {

    public interface OnToppingClickListener {
        void onToppingClick(String topping, boolean isFromAvailable);
    }

    private final List<String> toppings;
    private final OnToppingClickListener listener;
    private final boolean isAvailableList;
    private boolean enabled = true;

    public ToppingsAdapter(List<String> toppings, OnToppingClickListener listener, boolean isAvailableList) {
        this.toppings = toppings;
        this.listener = listener;
        this.isAvailableList = isAvailableList;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ToppingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.topping_item, parent, false);
        return new ToppingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToppingViewHolder holder, int position) {
        String topping = toppings.get(position);
        holder.bind(topping, listener, isAvailableList, enabled);
    }

    @Override
    public int getItemCount() {
        return toppings.size();
    }

    static class ToppingViewHolder extends RecyclerView.ViewHolder {
        TextView toppingName;

        public ToppingViewHolder(@NonNull View itemView) {
            super(itemView);
            toppingName = itemView.findViewById(R.id.toppingName);
        }

        public void bind(String topping, ToppingsAdapter.OnToppingClickListener listener, boolean isAvailableList, boolean enabled) {
            toppingName.setText(topping);
            itemView.setAlpha(enabled ? 1.0f : 0.5f);

            itemView.setOnClickListener(v -> {
                if (enabled && listener != null) {
                    listener.onToppingClick(topping, isAvailableList);
                }
            });
        }
    }
}
