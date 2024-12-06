package com.example.pizzaapp.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ToppingAdapter extends RecyclerView.Adapter<ToppingAdapter.ToppingViewHolder> {

    private final List<String> toppings;
    private String selectedTopping;

    public ToppingAdapter(List<String> toppings) {
        this.toppings = toppings;
    }

    @NonNull
    @Override
    public ToppingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ToppingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToppingViewHolder holder, int position) {
        String topping = toppings.get(position);
        holder.toppingTextView.setText(topping);
        holder.itemView.setOnClickListener(v -> selectedTopping = topping);
    }

    @Override
    public int getItemCount() {
        return toppings.size();
    }

    public String getSelectedTopping() {
        return selectedTopping;
    }

    static class ToppingViewHolder extends RecyclerView.ViewHolder {
        TextView toppingTextView;

        ToppingViewHolder(View itemView) {
            super(itemView);
            toppingTextView = itemView.findViewById(android.R.id.text1);
        }
    }
}
