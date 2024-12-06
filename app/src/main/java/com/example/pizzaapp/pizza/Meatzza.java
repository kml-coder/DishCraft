package com.example.pizzaapp.pizza;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Meatzza class represents a Meatzza pizza, which is a specific type of pizza.
 * It is characterized by its predefined set of toppings, including Sausage, Pepperoni, Beef, and Ham.
 * The price varies based on the size of the pizza.
 * @author Kyungmin Lee, Jack Lin
 */
public class Meatzza extends Pizza {

    /**
     * Constructor to create a Meatzza pizza with a specified crust.
     * The toppings are predefined as Sausage, Pepperoni, Beef, and Ham.
     * @param crust The crust type of the pizza.
     */
    public Meatzza(Crust crust) {
        super(crust, new ArrayList<>(Arrays.asList(Topping.SAUSAGE, Topping.PEPPERONI, Topping.BEEF, Topping.HAM)));
    }

    /**
     * Calculates the price of the Meatzza pizza based on its size.
     * @return The price of the pizza.
     * @throws IllegalStateException if the size is not set or if an unexpected value is encountered.
     */
    @Override
    public double price() {
        if (getSize() == null) {
            throw new IllegalStateException("Size must be set before calculating price.");
        }
        switch (getSize()) {
            case SMALL:
                return 17.99;
            case MEDIUM:
                return 19.99;
            case LARGE:
                return 21.99;
            default:
                throw new IllegalStateException("Unexpected value: " + getSize());
        }
    }
}
