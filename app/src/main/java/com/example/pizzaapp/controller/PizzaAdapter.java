package com.example.pizzaapp.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PizzaAdapter extends RecyclerView.Adapter<PizzaAdapter.PizzaViewHolder> {

    private final List<String> pizzas;

    public PizzaAdapter(List<String> pizzas) {
        this.pizzas = pizzas;
    }

    @NonNull
    @Override
    public PizzaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new PizzaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PizzaViewHolder holder, int position) {
        String pizza = pizzas.get(position);
        holder.pizzaTextView.setText(pizza);
    }

    @Override
    public int getItemCount() {
        return pizzas.size();
    }

    static class PizzaViewHolder extends RecyclerView.ViewHolder {
        TextView pizzaTextView;

        PizzaViewHolder(View itemView) {
            super(itemView);
            pizzaTextView = itemView.findViewById(android.R.id.text1);
        }
    }
}
