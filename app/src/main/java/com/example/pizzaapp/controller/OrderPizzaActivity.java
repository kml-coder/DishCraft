package com.example.pizzaapp.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pizzaapp.R;
import com.example.pizzaapp.pizza.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class OrderPizzaActivity extends AppCompatActivity {

    private Spinner styleSpinner;
    private Spinner typeSpinner;
    private Spinner sizeSpinner;

    private TextView sizeLabel;
    private TextView typeLabel;

    private ListView toppingsListView;
    private ListView selectedToppingsListView;
    private TextView TotalLabel;
    private ImageView pizzaImageView;
    private ListView addedPizzasListView;
    private Button removeSelectedPizzaButton;
    private TextView subtotalLabel;
    private TextView crustLabel;

    private ArrayAdapter<String> sizeAdapter;

    private ArrayAdapter<String> selectedToppingsAdapter;
    private ArrayAdapter<String> addedPizzasAdapter;

    private final ArrayList<String> availableToppings = new ArrayList<>(
            Arrays.stream(Topping.values()).map(Enum::name).collect(Collectors.toList())
    );
    private final ArrayList<String> selectedToppings = new ArrayList<>();
    private final ArrayList<String> addedPizzas = new ArrayList<>();

    private Order currentOrder = new Order();

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

        toppingsListView = findViewById(R.id.toppingsList);
        selectedToppingsListView = findViewById(R.id.addedtoppingsList);
        TotalLabel = findViewById(R.id.totalpriceValue);
        pizzaImageView = findViewById(R.id.pizzaImageview);
        addedPizzasListView = findViewById(R.id.addedpizzaList);
        removeSelectedPizzaButton = findViewById(R.id.removepizzaButton);
        subtotalLabel = findViewById(R.id.priceValue);
        crustLabel = findViewById(R.id.crustValue);

        initialize();
        setupListeners();
    }

    public void initialize() {
        // Add a prompt as the first item in style and type lists
        List<String> styleOptions = new ArrayList<>();
        styleOptions.add("Select Style"); // prompt
        styleOptions.add("Chicago");
        styleOptions.add("NY");

        ArrayAdapter<String> styleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, styleOptions);
        styleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        styleSpinner.setAdapter(styleAdapter);
        // Set selection to the prompt without triggering listener
        styleSpinner.setSelection(0, false);

        List<String> typeOptions = new ArrayList<>();
        typeOptions.add("Select Type"); // prompt
        typeOptions.add("Deluxe");
        typeOptions.add("BBQ Chicken");
        typeOptions.add("Meatzza");
        typeOptions.add("Build Your Own");

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, typeOptions);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
        // Set selection to the prompt without triggering listener
        typeSpinner.setSelection(0, false);

        sizeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Arrays.asList("Small", "Medium", "Large"));
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(sizeAdapter);
        sizeSpinner.setSelection(0, false); // optional, if you want no default size

        ArrayAdapter<String> toppingsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, availableToppings);
        toppingsListView.setAdapter(toppingsAdapter);
        toppingsListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        selectedToppingsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, selectedToppings);
        selectedToppingsListView.setAdapter(selectedToppingsAdapter);
        selectedToppingsListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        addedPizzasAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, addedPizzas);
        addedPizzasListView.setAdapter(addedPizzasAdapter);

        typeLabel.setVisibility(View.GONE);
        typeSpinner.setVisibility(View.GONE);
        sizeLabel.setVisibility(View.GONE);
        sizeSpinner.setVisibility(View.GONE);

        // Initially disable toppings if not "Build Your Own"
        toppingsListView.setEnabled(false);
        selectedToppingsListView.setEnabled(false);
    }


    private void setupListeners() {
        // Spinner listeners
        styleSpinner.setOnItemSelectedListener(new SimpleItemSelectedListener(this::handleStyleSelection));
        typeSpinner.setOnItemSelectedListener(new SimpleItemSelectedListener(this::handleTypeSelection));
        sizeSpinner.setOnItemSelectedListener(new SimpleItemSelectedListener(this::handleSizeSelection));

        // Buttons
        findViewById(R.id.addpizzaButton).setOnClickListener(v -> handleAddPizza());
        removeSelectedPizzaButton.setOnClickListener(v -> handleRemoveSelectedPizza());
        findViewById(R.id.finalizeButton).setOnClickListener(v -> handleFinalizeOrder());
        findViewById(R.id.addtoppingButton).setOnClickListener(v -> handleAddTopping());
        findViewById(R.id.removetoppingButton).setOnClickListener(v -> handleRemoveTopping());
    }

    private Size getSize() {
        try {
            return Size.valueOf(sizeSpinner.getSelectedItem().toString().toUpperCase());
        } catch (Exception e) {
            return null;
        }
    }

    private PizzaFactory createPizzaFactory(String style) {
        switch (style) {
            case "Chicago":
                return new ChicagoPizza();
            case "NY":
                return new NYPizza();
            default:
                return null;
        }
    }

    private Pizza createPizza(PizzaFactory pizzaFactory, String type) {
        switch (type) {
            case "Deluxe":
                return pizzaFactory.createDeluxe();
            case "BBQ Chicken":
                return pizzaFactory.createBBQChicken();
            case "Meatzza":
                return pizzaFactory.createMeatzza();
            case "Build Your Own":
                return pizzaFactory.createBuildYourOwn();
            default:
                return null;
        }
    }

    private void handleAddPizza() {
        String style = (String) styleSpinner.getSelectedItem();
        String type = (String) typeSpinner.getSelectedItem();
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
            for (String topping : selectedToppings) {
                pizza.getToppings().add(Topping.valueOf(topping));
            }
            selectedToppings.clear();
            selectedToppingsAdapter.notifyDataSetChanged();
        }

        pizza.changeSize(size);
        currentOrder.addPizza(pizza);
        addedPizzas.add(getPizzaDescription(pizza));
        addedPizzasAdapter.notifyDataSetChanged();
        updateTotal();
        logMessage("Pizza added to order: " + getPizzaDescription(pizza));
        updateSubtotal();
    }

    private void handleRemoveSelectedPizza() {
        int selectedIndex = addedPizzasListView.getCheckedItemPosition();
        if (selectedIndex == ListView.INVALID_POSITION || selectedIndex >= currentOrder.getPizzas().size()) {
            logMessage("No pizza selected to remove.");
            return;
        }
        Pizza pizzaToRemove = currentOrder.getPizzas().get(selectedIndex);
        currentOrder.removePizza(pizzaToRemove);
        addedPizzas.remove(selectedIndex);
        addedPizzasAdapter.notifyDataSetChanged();
        double total = 0.0;
        for (Pizza p : currentOrder.getPizzas()) {
            total += p.price();
        }
        TotalLabel.setText(String.format("%.2f", total * 1.06625));
        logMessage("Removed pizza from order: " + pizzaToRemove.toString());
    }

    private void handleStyleSelection() {
        String style = (String) styleSpinner.getSelectedItem();
        if (style != null) {
            typeLabel.setVisibility(View.VISIBLE);
            typeSpinner.setVisibility(View.VISIBLE);
        }

        String type = (String) typeSpinner.getSelectedItem();
        selectedToppings.clear();
        selectedToppingsAdapter.notifyDataSetChanged();

        if (type != null && style != null) {
            boolean isCustomizable = "Build Your Own".equals(type);
            toppingsListView.setEnabled(isCustomizable);
            selectedToppingsListView.setEnabled(isCustomizable);

            Pizza pizza = createPizza(style, type);
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

    private Pizza createPizza(String style, String type) {
        PizzaFactory pizzaFactory = createPizzaFactory(style);
        if (pizzaFactory == null) {
            return null;
        }

        switch (type) {
            case "Meatzza":
                return pizzaFactory.createMeatzza();
            case "BBQ Chicken":
                return pizzaFactory.createBBQChicken();
            case "Deluxe":
                return pizzaFactory.createDeluxe();
            case "Build Your Own":
                return pizzaFactory.createBuildYourOwn();
            default:
                return null;
        }
    }

    private void handleSizeSelection() {
        updateSubtotal();
    }

    private void handleTypeSelection() {
        String type = (String) typeSpinner.getSelectedItem();
        String style = (String) styleSpinner.getSelectedItem();
        if (type != null) {
            sizeLabel.setVisibility(View.VISIBLE);
            sizeSpinner.setVisibility(View.VISIBLE);
            toppingsListView.setVisibility(View.VISIBLE);
            selectedToppingsListView.setVisibility(View.VISIBLE);
        }

        selectedToppings.clear();
        selectedToppingsAdapter.notifyDataSetChanged();

        if (type != null && style != null) {
            boolean isCustomizable = "Build Your Own".equals(type);
            toppingsListView.setEnabled(isCustomizable);
            selectedToppingsListView.setEnabled(isCustomizable);

            Pizza pizza = createPizza(style, type);
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
        if (!currentOrder.getPizzas().isEmpty()) {
            OrderManager.getInstance().addOrder(currentOrder);
            logMessage("Order finalized with " + currentOrder.getPizzas().size() + " pizzas.");
            currentOrder = new Order();
            addedPizzas.clear();
            addedPizzasAdapter.notifyDataSetChanged();
            TotalLabel.setText("0.00");
        } else {
            logMessage("No pizzas in the order to finalize.");
        }
    }

    private void handleAddTopping() {
        String sizeStr = sizeSpinner.getSelectedItem() == null ? null : sizeSpinner.getSelectedItem().toString();
        if (sizeStr == null) {
            logMessage("Error: Please finish selecting all pizza options. (Style, Type, Size)");
            return;
        }

        int selectedIndex = toppingsListView.getCheckedItemPosition();
        if (selectedIndex == ListView.INVALID_POSITION) {
            logMessage("Please select a topping first.");
            return;
        }

        String selectedTopping = availableToppings.get(selectedIndex);

        if (!selectedToppings.contains(selectedTopping) && selectedToppings.size() < 7) {
            selectedToppings.add(selectedTopping);
            selectedToppingsAdapter.notifyDataSetChanged();
            updateSubtotal();
            logMessage("Topping added: " + selectedTopping);
        } else if (selectedToppings.contains(selectedTopping)) {
            logMessage(selectedTopping + " is already in your list");
        } else if (selectedToppings.size() >= 7) {
            logMessage("Toppings exceed the maximum of 7.");
        }
    }

    private void handleRemoveTopping() {
        String sizeStr = sizeSpinner.getSelectedItem() == null ? null : sizeSpinner.getSelectedItem().toString();
        if (sizeStr == null) {
            logMessage("Error: Please finish selecting all pizza options. (Style, Type, Size)");
            return;
        }

        // Now we use the checked item from selectedToppingsListView
        int selectedIndex = selectedToppingsListView.getCheckedItemPosition();
        if (selectedIndex == ListView.INVALID_POSITION || selectedIndex >= selectedToppings.size()) {
            logMessage("Error: Please choose the topping to remove.");
            return;
        }
        String toppingToRemove = selectedToppings.get(selectedIndex);
        selectedToppings.remove(selectedIndex);
        selectedToppingsAdapter.notifyDataSetChanged();
        updateSubtotal();
        logMessage("Topping removed: " + toppingToRemove);
    }

    private void updateSubtotal() {
        String type = (String) typeSpinner.getSelectedItem();
        String style = (String) styleSpinner.getSelectedItem();
        if (type == null || style == null) {
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

        Pizza pizza;
        switch (type) {
            case "Deluxe":
                pizza = pizzaFactory.createDeluxe();
                break;
            case "BBQ Chicken":
                pizza = pizzaFactory.createBBQChicken();
                break;
            case "Meatzza":
                pizza = pizzaFactory.createMeatzza();
                break;
            case "Build Your Own":
                pizza = pizzaFactory.createBuildYourOwn();
                for (String topping : selectedToppings) {
                    pizza.getToppings().add(Topping.valueOf(topping));
                }
                break;
            default:
                subtotalLabel.setText("0.00");
                return;
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
