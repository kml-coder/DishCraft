package com.example.pizzaapp.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzaapp.R;
import com.example.pizzaapp.pizza.Pizza;

import java.util.List;

/**
 * PizzasAdapter is a RecyclerView adapter to display a list of pizzas.
 * Each pizza shows its description.
 * Also supports selecting a pizza by clicking it.
 * The selected pizza is highlighted.
 */
public class PizzasAdapter extends RecyclerView.Adapter<PizzasAdapter.PizzaViewHolder> {

    private final List<Pizza> pizzas;
    private int selectedPosition = -1; // track selected item if needed

    public PizzasAdapter(List<Pizza> pizzas) {
        this.pizzas = pizzas;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    @NonNull
    @Override
    public PizzaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pizza_item, parent, false);
        return new PizzaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PizzaViewHolder holder, int position) {
        Pizza pizza = pizzas.get(position);
        holder.bind(pizza, position == selectedPosition);

        holder.itemView.setOnClickListener(v -> {
            int oldPosition = selectedPosition;
            selectedPosition = position;
            if (oldPosition != -1) {
                notifyItemChanged(oldPosition);
            }
            notifyItemChanged(selectedPosition);
        });
    }

    @Override
    public int getItemCount() {
        return pizzas.size();
    }

    static class PizzaViewHolder extends RecyclerView.ViewHolder {
        TextView pizzaDescription;

        public PizzaViewHolder(@NonNull View itemView) {
            super(itemView);
            pizzaDescription = itemView.findViewById(R.id.pizzaDescription);
        }

        public void bind(Pizza pizza, boolean isSelected) {
            pizzaDescription.setText(pizza.toString() + " - $" + String.format("%.2f", pizza.price()));
            itemView.setBackgroundColor(isSelected ? 0xFFE0E0E0 : 0xFFFFFFFF); // highlight if selected
        }
    }
}
