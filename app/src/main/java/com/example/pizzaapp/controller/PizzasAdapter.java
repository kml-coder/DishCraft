package com.example.pizzaapp.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzaapp.R;
import com.example.pizzaapp.pizza.BBQChicken;
import com.example.pizzaapp.pizza.BuildYourOwn;
import com.example.pizzaapp.pizza.Deluxe;
import com.example.pizzaapp.pizza.Meatzza;
import com.example.pizzaapp.pizza.Pizza;

import java.util.List;

/**
 * PizzasAdapter is a RecyclerView adapter to display a list of pizzas.
 * Each pizza shows its description and an associated image.
 * We replicate the logic from OrderPizzaActivity's updatePizzaImage() and Pizza.toString()
 * to derive the type and style for image naming without needing a getType() method.
 */
public class PizzasAdapter extends RecyclerView.Adapter<PizzasAdapter.PizzaViewHolder> {

    private List<Pizza> pizzas;
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

    public void setPizzas(List<Pizza> pizzas) {
        this.pizzas = pizzas; // Update the adapter's internal data reference
        notifyDataSetChanged(); // Notify the adapter about the change
    }

    static class PizzaViewHolder extends RecyclerView.ViewHolder {
        TextView pizzaDescription;
        ImageView pizzaImage;

        public PizzaViewHolder(@NonNull View itemView) {
            super(itemView);
            pizzaDescription = itemView.findViewById(R.id.pizzaDescription);
            pizzaImage = itemView.findViewById(R.id.pizzaImage);
        }

        public void bind(Pizza pizza, boolean isSelected) {
            // Show the pizza description from pizza.toString()
            pizzaDescription.setText(pizza.toString() + " - $" + String.format("%.2f", pizza.price()));
            itemView.setBackgroundColor(isSelected ? 0xFFE0E0E0 : 0xFFFFFFFF); // highlight if selected

            // Determine pizzaType based on the instance of the pizza:
            String pizzaType;
            if (pizza instanceof Deluxe) {
                pizzaType = "Deluxe";
            } else if (pizza instanceof BBQChicken) {
                pizzaType = "BBQ Chicken";
            } else if (pizza instanceof Meatzza) {
                pizzaType = "Meatzza";
            } else if (pizza instanceof BuildYourOwn) {
                pizzaType = "Build Your Own";
            } else {
                pizzaType = "Unknown";
            }

            // Determine style based on crust, same logic as in Pizza.toString()
            // For reference from toString():
            // Deluxe: DEEP_DISH = Chicago else NY
            // BBQChicken: PAN = Chicago else NY
            // Meatzza: STUFFED = Chicago else NY
            // BuildYourOwn: PAN = Chicago else NY
            // Others: Unknown (default to "ny" or "chicago"?)
            String crustName = pizza.getCrust().toString(); // e.g. "DEEP_DISH", "PAN", "STUFFED", etc.
            String style;
            if (pizza instanceof Deluxe) {
                style = crustName.equals("DEEP_DISH") ? "chicago" : "ny";
            } else if (pizza instanceof BBQChicken) {
                style = crustName.equals("PAN") ? "chicago" : "ny";
            } else if (pizza instanceof Meatzza) {
                style = crustName.equals("STUFFED") ? "chicago" : "ny";
            } else if (pizza instanceof BuildYourOwn) {
                style = crustName.equals("PAN") ? "chicago" : "ny";
            } else {
                style = "unknown";
            }

            // Construct imageName like updatePizzaImage():
            // style + "_" + pizzaType with spaces removed + ".jpg"
            String imageName = style + "_" + pizzaType.replaceAll(" ", "") + ".jpg";
            String resourceName = imageName.toLowerCase().replaceAll(" ", "").replace(".jpg", "");
            int resId = itemView.getContext().getResources().getIdentifier(resourceName, "drawable", itemView.getContext().getPackageName());

            if (resId == 0) {
                // No matching resource, set null or default
                pizzaImage.setImageDrawable(null);
            } else {
                pizzaImage.setImageResource(resId);
            }
        }
    }
}
