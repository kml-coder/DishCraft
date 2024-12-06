package com.example.pizzaapp.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.example.pizzaapp.pizza.*;

public class OrderPizzaActivity extends AppCompatActivity {

    private Spinner styleSpinner;
    private Spinner typeSpinner;
    private Spinner sizeSpinner;
    private RecyclerView toppingsRecyclerView;
    private RecyclerView selectedToppingsRecyclerView;
    private RecyclerView addedPizzasRecyclerView;
    private TextView subtotalTextView;
    private TextView totalTextView;
    private TextView crustTextView;
    private ImageView pizzaImageView;
    private Button removeSelectedPizzaButton;
    private Button finalizeOrderButton;
    private Button addToppingButton;
    private Button removeToppingButton;
    private Button addPizzaButton;

    private ArrayAdapter<String> styleAdapter;
    private ArrayAdapter<String> typeAdapter;
    private ArrayAdapter<String> sizeAdapter;
    private ToppingAdapter availableToppingsAdapter;
    private ToppingAdapter selectedToppingsAdapter;
    private PizzaAdapter addedPizzasAdapter;

    private final List<String> availableToppings = Arrays.stream(pizza.Topping.values()).map(Enum::name).collect(Collectors.toList());
    private final List<String> selectedToppings = new ArrayList<>();
    private final List<String> addedPizzas = new ArrayList<>();
    private Order currentOrder = new Order();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_pizza);

        initUIComponents();
        setupSpinners();
        setupRecyclerViews();
        setupEventHandlers();
    }

    private void initUIComponents() {
        styleSpinner = findViewById(R.id.styleSpinner);
        typeSpinner = findViewById(R.id.typeSpinner);
        sizeSpinner = findViewById(R.id.sizeSpinner);
        toppingsRecyclerView = findViewById(R.id.toppingsRecyclerView);
        selectedToppingsRecyclerView = findViewById(R.id.selectedToppingsRecyclerView);
        addedPizzasRecyclerView = findViewById(R.id.addedPizzasRecyclerView);
        subtotalTextView = findViewById(R.id.subtotalTextView);
        totalTextView = findViewById(R.id.totalTextView);
        crustTextView = findViewById(R.id.crustTextView);
        pizzaImageView = findViewById(R.id.pizzaImageView);
        removeSelectedPizzaButton = findViewById(R.id.removeSelectedPizzaButton);
        finalizeOrderButton = findViewById(R.id.finalizeOrderButton);
        addToppingButton = findViewById(R.id.addToppingButton);
        removeToppingButton = findViewById(R.id.removeToppingButton);
        addPizzaButton = findViewById(R.id.addPizzaButton);
    }

    private void setupSpinners() {
        styleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Arrays.asList("Chicago", "NY"));
        styleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        styleSpinner.setAdapter(styleAdapter);

        typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Arrays.asList("Deluxe", "BBQ Chicken", "Meatzza", "Build Your Own"));
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);

        sizeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Arrays.asList("Small", "Medium", "Large"));
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(sizeAdapter);
    }

    private void setupRecyclerViews() {
        availableToppingsAdapter = new ToppingAdapter(availableToppings);
        toppingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        toppingsRecyclerView.setAdapter(availableToppingsAdapter);

        selectedToppingsAdapter = new ToppingAdapter(selectedToppings);
        selectedToppingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        selectedToppingsRecyclerView.setAdapter(selectedToppingsAdapter);

        addedPizzasAdapter = new PizzaAdapter(addedPizzas);
        addedPizzasRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        addedPizzasRecyclerView.setAdapter(addedPizzasAdapter);
    }

    private void setupEventHandlers() {
        styleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                handleStyleSelection();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                handleTypeSelection();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateSubtotal();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        addPizzaButton.setOnClickListener(v -> handleAddPizza());
        removeSelectedPizzaButton.setOnClickListener(v -> handleRemoveSelectedPizza());
        finalizeOrderButton.setOnClickListener(v -> handleFinalizeOrder());
        addToppingButton.setOnClickListener(v -> handleAddTopping());
        removeToppingButton.setOnClickListener(v -> handleRemoveTopping());
    }

    private void handleAddPizza() {
        String style = styleSpinner.getSelectedItem().toString();
        String type = typeSpinner.getSelectedItem().toString();
        Size size = getSize();

        if (style == null || type == null || size == null) {
            logMessage("Error: Please select valid style, type, and size for the pizza.");
            return;
        }

        PizzaFactory pizzaFactory = createPizzaFactory(style);
        if (pizzaFactory == null) {
            logMessage("Error: Please select a valid style for the pizza.");
            return;
        }

        Pizza pizza = createPizza(pizzaFactory, type);
        if (pizza == null) {
            logMessage("Error: Please select a valid type for the pizza.");
            return;
        }

        if (pizza instanceof BuildYourOwn) {
            selectedToppings.forEach(topping -> pizza.getToppings().add(Topping.valueOf(topping)));
            selectedToppings.clear();
        }

        pizza.changeSize(size);
        currentOrder.addPizza(pizza);
        addedPizzas.add(getPizzaDescription(pizza));
        addedPizzasAdapter.notifyDataSetChanged();
        updateTotal();

        logMessage("Pizza added to order: " + getPizzaDescription(pizza));
        updateSubtotal();
    }

    private Size getSize() {
        try {
            return Size.valueOf(sizeSpinner.getSelectedItem().toString().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }

    private PizzaFactory createPizzaFactory(String style) {
        return switch (style) {
            case "Chicago" -> new ChicagoPizza();
            case "NY" -> new NYPizza();
            default -> null;
        };
    }

    private Pizza createPizza(PizzaFactory pizzaFactory, String type) {
        return switch (type) {
            case "Deluxe" -> pizzaFactory.createDeluxe();
            case "BBQ Chicken" -> pizzaFactory.createBBQChicken();
            case "Meatzza" -> pizzaFactory.createMeatzza();
            case "Build Your Own" -> pizzaFactory.createBuildYourOwn();
            default -> null;
        };
    }

    private void updateTotal() {
        double total = currentOrder.getPizzas().stream().mapToDouble(Pizza::price).sum();
        totalTextView.setText(String.format("%.2f", total * 1.06625));
    }

    private String getPizzaDescription(Pizza pizza) {
        return String.format("%s - $%.2f", pizza.toString(), pizza.price());
    }

    private void handleRemoveSelectedPizza() {
        int selectedIndex = addedPizzasRecyclerView.getChildAdapterPosition(addedPizzasRecyclerView.getFocusedChild());
        if (selectedIndex >= 0) {
            Pizza pizzaToRemove = currentOrder.getPizzas().get(selectedIndex);
            currentOrder.removePizza(pizzaToRemove);
            addedPizzas.remove(selectedIndex);
            addedPizzasAdapter.notifyDataSetChanged();
            updateTotal();
            logMessage("Removed pizza from order: " + pizzaToRemove);
        } else {
            logMessage("No pizza selected to remove.");
        }
    }

    private void handleFinalizeOrder() {
        if (!currentOrder.getPizzas().isEmpty()) {
            OrderManager.getInstance().addOrder(currentOrder);
            logMessage("Order finalized with " + currentOrder.getPizzas().size() + " pizzas.");
            currentOrder = new Order();
            addedPizzas.clear();
            addedPizzasAdapter.notifyDataSetChanged();
            totalTextView.setText("0.00");
        } else {
            logMessage("No pizzas in the order to finalize.");
        }
    }

    private void handleAddTopping() {
        String selectedTopping = availableToppingsAdapter.getSelectedTopping();
        if (selectedTopping != null && !selectedToppings.contains(selectedTopping) && selectedToppings.size() < 7) {
            selectedToppings.add(selectedTopping);
            selectedToppingsAdapter.notifyDataSetChanged();
            updateSubtotal();
            logMessage("Topping added: " + selectedTopping);
        } else if (selectedToppings.contains(selectedTopping)) {
            logMessage(selectedTopping + " is already in your list");
        } else if (!(selectedToppings.size() < 7)) {
            logMessage("Topping is more than 7, please change it");
        }
    }

    private void handleRemoveTopping() {
        String selectedTopping = selectedToppingsAdapter.getSelectedTopping();
        if (selectedTopping != null) {
            selectedToppings.remove(selectedTopping);
            selectedToppingsAdapter.notifyDataSetChanged();
            updateSubtotal();
            logMessage("Topping removed: " + selectedTopping);
        } else {
            logMessage("Error: Please choose the topping to remove.");
        }
    }

    private void updateSubtotal() {
        String type = typeSpinner.getSelectedItem().toString();
        Size size;
        try {
            size = Size.valueOf(sizeSpinner.getSelectedItem().toString().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            subtotalTextView.setText("0.00");
            return;
        }
        PizzaFactory pizzaFactory = createPizzaFactory(styleSpinner.getSelectedItem().toString());
        if (pizzaFactory == null) {
            subtotalTextView.setText("0.00");
            return;
        }
        Pizza pizza = createPizza(pizzaFactory, type);
        if (pizza == null) {
            subtotalTextView.setText("0.00");
            return;
        }
        if ("Build Your Own".equals(type)) {
            for (String topping : selectedToppings) {
                pizza.getToppings().add(Topping.valueOf(topping));
            }
        }
        pizza.changeSize(size);
        double subtotal = pizza.price();
        subtotalTextView.setText(String.format("%.2f", subtotal));
    }

    private void handleStyleSelection() {
        // Logic to handle style selection and update UI components accordingly
    }

    private void handleTypeSelection() {
        // Logic to handle type selection and update toppings or images
    }

    private void logMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}

// Note: You will need to create XML layouts and adapters (e.g., ToppingAdapter and PizzaAdapter) to fully implement the Android version of this application.
// Also, the pizza business logic should be refactored to match Android's context and lifecycle as necessary.
