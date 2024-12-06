package com.example.pizzaapp.controller;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pizzaapp.R;
import com.example.pizzaapp.pizza.ChicagoPizza;
import com.example.pizzaapp.pizza.NYPizza;
import com.example.pizzaapp.pizza.Order;
import com.example.pizzaapp.pizza.OrderManager;
import com.example.pizzaapp.pizza.Pizza;
import com.example.pizzaapp.pizza.PizzaFactory;
import com.example.pizzaapp.pizza.Size;
import com.example.pizzaapp.pizza.Topping;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OrderPizzaActivity extends AppCompatActivity implements ToppingsAdapter.OnToppingClickListener {

    private Spinner styleSpinner;
    private Spinner typeSpinner;
    private Spinner sizeSpinner;

    private TextView sizeLabel;
    private TextView typeLabel;
    private TextView subtotalLabel;
    private TextView crustLabel;
    private TextView TotalLabel;
    private ImageView pizzaImageView;
    private Button removeSelectedPizzaButton;

    private RecyclerView toppingsRecyclerView;
    private RecyclerView selectedToppingsRecyclerView;
    private RecyclerView addedPizzasRecyclerView;

    private ToppingsAdapter toppingsAdapter;
    private ToppingsAdapter selectedToppingsAdapter;
    private PizzasAdapter addedPizzasAdapter;

    private ArrayAdapter<String> sizeAdapter;

    private List<String> availableToppings;
    private final ArrayList<String> selectedToppings = new ArrayList<>();
    private Order currentOrder = new Order(); // Order holds a list of Pizzas
    // The PizzasAdapter will reflect currentOrder.getPizzas()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderpizza);

        // Initialize UI components
        styleSpinner = findViewById(R.id.styleSpinner);
        typeSpinner = findViewById(R.id.typeSpinner);
        sizeSpinner = findViewById(R.id.sizeSpinner);

        sizeLabel = findViewById(R.id.sizeLabel);
        typeLabel = findViewById(R.id.typeLabel);
        subtotalLabel = findViewById(R.id.priceValue);
        crustLabel = findViewById(R.id.crustValue);
        TotalLabel = findViewById(R.id.totalpriceValue);
        pizzaImageView = findViewById(R.id.pizzaImageview);
        removeSelectedPizzaButton = findViewById(R.id.removepizzaButton);

        toppingsRecyclerView = findViewById(R.id.toppingsRecyclerView);
        selectedToppingsRecyclerView = findViewById(R.id.selectedToppingsRecyclerView);
        addedPizzasRecyclerView = findViewById(R.id.addedpizzaRecyclerView);

        initialize();
        setupListeners();
    }

    public void initialize() {
        // Add a prompt as the first item in style and type lists
        List<String> styleOptions = new ArrayList<>();
        styleOptions.add(getString(R.string.select_style));
        styleOptions.add(getString(R.string.chicago));
        styleOptions.add(getString(R.string.ny));

        ArrayAdapter<String> styleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, styleOptions);
        styleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        styleSpinner.setAdapter(styleAdapter);
        styleSpinner.setSelection(0, false);

        List<String> typeOptions = new ArrayList<>();
        typeOptions.add(getString(R.string.select_type));
        typeOptions.add(getString(R.string.deluxe));
        typeOptions.add(getString(R.string.bbq_chicken));
        typeOptions.add(getString(R.string.meatzza));
        typeOptions.add(getString(R.string.build_your_own));

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, typeOptions);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        typeSpinner.setSelection(0, false);

        sizeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                Arrays.asList(getString(R.string.small), getString(R.string.medium), getString(R.string.large)));
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(sizeAdapter);
        sizeSpinner.setSelection(0, false);

        availableToppings = Arrays.stream(Topping.values()).map(Enum::name).collect(Collectors.toList());

        // Setup Toppings RecyclerView
        toppingsAdapter = new ToppingsAdapter(availableToppings, this, true);
        toppingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        toppingsRecyclerView.setAdapter(toppingsAdapter);

        // Setup Selected Toppings RecyclerView
        selectedToppingsAdapter = new ToppingsAdapter(selectedToppings, this, false);
        selectedToppingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectedToppingsRecyclerView.setAdapter(selectedToppingsAdapter);

        // Setup Added Pizzas RecyclerView
        addedPizzasAdapter = new PizzasAdapter(currentOrder.getPizzas());
        addedPizzasRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        addedPizzasRecyclerView.setAdapter(addedPizzasAdapter);

        typeLabel.setVisibility(View.GONE);
        typeSpinner.setVisibility(View.GONE);
        sizeLabel.setVisibility(View.GONE);
        sizeSpinner.setVisibility(View.GONE);

        // Initially disable toppings if not "Build Your Own"
        toppingsAdapter.setEnabled(false);
        selectedToppingsAdapter.setEnabled(false);
    }

    private void setupListeners() {
        styleSpinner.setOnItemSelectedListener(new SimpleItemSelectedListener(this::handleStyleSelection));
        typeSpinner.setOnItemSelectedListener(new SimpleItemSelectedListener(this::handleTypeSelection));
        sizeSpinner.setOnItemSelectedListener(new SimpleItemSelectedListener(this::handleSizeSelection));

        findViewById(R.id.addpizzaButton).setOnClickListener(v -> handleAddPizza());
        removeSelectedPizzaButton.setOnClickListener(v -> handleRemoveSelectedPizza());
        findViewById(R.id.finalizeButton).setOnClickListener(v -> handleFinalizeOrder());

        // If you still want to keep these topping buttons, you can. Otherwise, remove them.
        findViewById(R.id.addtoppingButton).setOnClickListener(v -> handleAddTopping());
        findViewById(R.id.removetoppingButton).setOnClickListener(v -> handleRemoveTopping());
    }

    private Size getSize() {
        try {
            String selected = sizeSpinner.getSelectedItem().toString().toUpperCase();
            return Size.valueOf(selected);
        } catch (Exception e) {
            return null;
        }
    }

    private PizzaFactory createPizzaFactory(String style) {
        if (style.equals(getString(R.string.chicago))) {
            return new ChicagoPizza();
        } else if (style.equals(getString(R.string.ny))) {
            return new NYPizza();
        }
        return null;
    }

    private Pizza createPizza(PizzaFactory pizzaFactory, String type) {
        if (type.equals(getString(R.string.deluxe))) {
            return pizzaFactory.createDeluxe();
        } else if (type.equals(getString(R.string.bbq_chicken))) {
            return pizzaFactory.createBBQChicken();
        } else if (type.equals(getString(R.string.meatzza))) {
            return pizzaFactory.createMeatzza();
        } else if (type.equals(getString(R.string.build_your_own))) {
            return pizzaFactory.createBuildYourOwn();
        }
        return null;
    }

    private void handleAddPizza() {
        String style = (String) styleSpinner.getSelectedItem();
        String type = (String) typeSpinner.getSelectedItem();
        Size size = getSize();

        if (style == null || style.equals(getString(R.string.select_style)) ||
                type == null || type.equals(getString(R.string.select_type)) ||
                size == null) {
            logMessage(getString(R.string.error_select_options));
            return;
        }

        PizzaFactory pizzaFactory = createPizzaFactory(style);
        if (pizzaFactory == null) {
            logMessage(getString(R.string.error_select_valid_style));
            return;
        }

        Pizza pizza = createPizza(pizzaFactory, type);
        if (pizza == null) {
            logMessage(getString(R.string.error_select_valid_type));
            return;
        }

        if (type.equals(getString(R.string.build_your_own))) {
            // Add selected toppings to the pizza
            for (String topping : selectedToppings) {
                try {
                    pizza.getToppings().add(Topping.valueOf(topping));
                } catch (Exception e) {
                    // ignore invalid topping
                }
            }
            selectedToppings.clear();
            selectedToppingsAdapter.notifyDataSetChanged();
        }

        pizza.changeSize(size);
        currentOrder.addPizza(pizza);
        addedPizzasAdapter.notifyDataSetChanged();
        updateTotal();
        logMessage(getString(R.string.pizza_added) + " " + getPizzaDescription(pizza));
        updateSubtotal();
    }

    private void handleRemoveSelectedPizza() {
        int selectedIndex = addedPizzasAdapter.getSelectedPosition();
        if (selectedIndex == -1 || selectedIndex >= currentOrder.getPizzas().size()) {
            logMessage(getString(R.string.no_pizza_to_finalize));
            return;
        }
        Pizza pizzaToRemove = currentOrder.getPizzas().get(selectedIndex);
        currentOrder.removePizza(pizzaToRemove);
        addedPizzasAdapter.notifyDataSetChanged();
        double total = currentOrder.getPizzas().stream().mapToDouble(Pizza::price).sum();
        TotalLabel.setText(String.format("%.2f", total * 1.06625));
        logMessage(getString(R.string.pizza_removed) + " " + pizzaToRemove.toString());
    }

    private void handleStyleSelection() {
        String style = (String) styleSpinner.getSelectedItem();
        if (style != null && !style.equals(getString(R.string.select_style))) {
            typeLabel.setVisibility(View.VISIBLE);
            typeSpinner.setVisibility(View.VISIBLE);
        }

        selectedToppings.clear();
        selectedToppingsAdapter.notifyDataSetChanged();

        String type = (String) typeSpinner.getSelectedItem();
        if (type != null && !type.equals(getString(R.string.select_type)) &&
                style != null && !style.equals(getString(R.string.select_style))) {
            boolean isCustomizable = type.equals(getString(R.string.build_your_own));
            toppingsAdapter.setEnabled(isCustomizable);
            selectedToppingsAdapter.setEnabled(isCustomizable);

            Pizza pizza = createPizza(createPizzaFactory(style), type);
            if (pizza != null) {
                if (pizza.getToppings() != null) {
                    selectedToppings.addAll(
                            pizza.getToppings().stream().map(Enum::name).collect(Collectors.toList())
                    );
                    selectedToppingsAdapter.notifyDataSetChanged();
                }
                crustLabel.setText(pizza.getCrust().toString());
                updatePizzaImage(style + "_" + type.replaceAll(" ", "") + ".jpg");
            }
            updateSubtotal();
        }
    }

    private void handleSizeSelection() {
        updateSubtotal();
    }

    private void handleTypeSelection() {
        String type = (String) typeSpinner.getSelectedItem();
        String style = (String) styleSpinner.getSelectedItem();
        if (type != null && !type.equals(getString(R.string.select_type))) {
            sizeLabel.setVisibility(View.VISIBLE);
            sizeSpinner.setVisibility(View.VISIBLE);
        }

        selectedToppings.clear();
        selectedToppingsAdapter.notifyDataSetChanged();

        if (type != null && !type.equals(getString(R.string.select_type)) &&
                style != null && !style.equals(getString(R.string.select_style))) {
            boolean isCustomizable = type.equals(getString(R.string.build_your_own));
            toppingsAdapter.setEnabled(isCustomizable);
            selectedToppingsAdapter.setEnabled(isCustomizable);

            Pizza pizza = createPizza(createPizzaFactory(style), type);
            if (pizza != null) {
                if (pizza.getToppings() != null) {
                    selectedToppings.addAll(
                            pizza.getToppings().stream().map(Enum::name).collect(Collectors.toList())
                    );
                    selectedToppingsAdapter.notifyDataSetChanged();
                }
                crustLabel.setText(pizza.getCrust().toString());
                updatePizzaImage(style + "_" + type.replaceAll(" ", "") + ".jpg");
            }
            updateSubtotal();
        }
    }

    private void handleFinalizeOrder() {
        if (currentOrder.getPizzas().isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.no_pizza_cancel_alert_title))
                    .setMessage(getString(R.string.no_pizza_cancel_alert_message))
                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // just close dialog
                        }
                    })
                    .show();
        } else {
            OrderManager.getInstance().addOrder(currentOrder);
            logMessage(String.format(getString(R.string.order_finalized), currentOrder.getPizzas().size()));
            currentOrder = new Order();
            addedPizzasAdapter.notifyDataSetChanged();
            TotalLabel.setText("0.00");
        }
    }

    private void handleAddTopping() {
        String sizeStr = sizeSpinner.getSelectedItem() == null ? null : sizeSpinner.getSelectedItem().toString();
        if (sizeStr == null || sizeStr.equals(getString(R.string.select_type))) {
            logMessage(getString(R.string.error_select_all_options));
            return;
        }

        // Since we now rely on clicking toppings in RecyclerView,
        // this button is optional. If still used:
        // Just show a Toast asking user to tap a topping instead
        logMessage(getString(R.string.error_select_topping));
    }

    private void handleRemoveTopping() {
        String sizeStr = sizeSpinner.getSelectedItem() == null ? null : sizeSpinner.getSelectedItem().toString();
        if (sizeStr == null || sizeStr.equals(getString(R.string.select_type))) {
            logMessage(getString(R.string.error_select_all_options));
            return;
        }
        // Same as handleAddTopping, removing topping via button might not be needed
        // since tapping selected toppings remove them. If you want:
        logMessage(getString(R.string.error_select_topping));
    }

    private void updateSubtotal() {
        String type = (String) typeSpinner.getSelectedItem();
        String style = (String) styleSpinner.getSelectedItem();
        if (type == null || style == null ||
                type.equals(getString(R.string.select_type)) || style.equals(getString(R.string.select_style))) {
            subtotalLabel.setText("0.00");
            return;
        }
        Size s = getSize();
        if (s == null) {
            subtotalLabel.setText("0.00");
            return;
        }

        PizzaFactory pizzaFactory = createPizzaFactory(style);
        if (pizzaFactory == null) {
            subtotalLabel.setText("0.00");
            return;
        }

        Pizza pizza = createPizza(pizzaFactory, type);
        if (pizza == null) {
            subtotalLabel.setText("0.00");
            return;
        }

        if (type.equals(getString(R.string.build_your_own))) {
            pizza.getToppings().clear();
            for (String topping : selectedToppings) {
                try {
                    pizza.getToppings().add(Topping.valueOf(topping));
                } catch (Exception e) {
                    // ignore invalid
                }
            }
        }

        pizza.changeSize(s);
        double subtotal = pizza.price();
        subtotalLabel.setText(String.format("%.2f", subtotal));
    }

    private void updateTotal() {
        double total = currentOrder.getPizzas().stream().mapToDouble(Pizza::price).sum();
        TotalLabel.setText(String.format("%.2f", total * 1.06625));
    }

    private String getPizzaDescription(Pizza pizza) {
        return String.format("%s - $%.2f", pizza.toString(), pizza.price());
    }

    private void updatePizzaImage(String imageName) {
        String resourceName = imageName.toLowerCase().replaceAll(" ", "").replace(".jpg", "");
        int resId = getResources().getIdentifier(resourceName, "drawable", getPackageName());
        if (resId == 0) {
            logMessage("Error: Unable to load image " + imageName);
            pizzaImageView.setImageDrawable(null);
        } else {
            pizzaImageView.setImageResource(resId);
        }
    }

    private void logMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onToppingClick(String topping, boolean isFromAvailable) {
        // Handle clicking on toppings in the RecyclerView
        String sizeStr = sizeSpinner.getSelectedItem() == null ? null : sizeSpinner.getSelectedItem().toString();
        if (sizeStr == null || sizeStr.equals(getString(R.string.select_type))) {
            logMessage(getString(R.string.error_select_all_options));
            return;
        }

        if (isFromAvailable) {
            // Add topping
            if (!selectedToppings.contains(topping) && selectedToppings.size() < 7) {
                selectedToppings.add(topping);
                selectedToppingsAdapter.notifyDataSetChanged();
                updateSubtotal();
                logMessage(getString(R.string.topping_added) + " " + topping);
            } else if (selectedToppings.contains(topping)) {
                logMessage(String.format(getString(R.string.topping_already_in_list), topping));
            } else if (selectedToppings.size() >= 7) {
                logMessage(getString(R.string.topping_exceed));
            }
        } else {
            // Remove topping
            int index = selectedToppings.indexOf(topping);
            if (index != -1) {
                selectedToppings.remove(index);
                selectedToppingsAdapter.notifyDataSetChanged();
                updateSubtotal();
                logMessage(getString(R.string.topping_removed) + " " + topping);
            } else {
                logMessage(getString(R.string.error_select_topping));
            }
        }
    }

    private class SimpleItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {

        private final Runnable callback;

        public SimpleItemSelectedListener(Runnable callback) {
            this.callback = callback;
        }

        @Override
        public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
            callback.run();
        }

        @Override
        public void onNothingSelected(android.widget.AdapterView<?> parent) {
            // Do nothing
        }
    }
}
