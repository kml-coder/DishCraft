package com.example.pizzaapp.pizza;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Deluxe class represents a Deluxe pizza, which is a specific type of pizza.
 * It is characterized by its predefined set of toppings, including Sausage, Pepperoni, Green Pepper, Onion, and Mushroom.
 * The price varies based on the size of the pizza.
 * @author Kyungmin Lee, Jack Lin
 */
public class Deluxe extends Pizza {

    /**
     * Constructor to create a Deluxe pizza with a specified crust.
     * The toppings are predefined as Sausage, Pepperoni, Green Pepper, Onion, and Mushroom.
     * @param crust The crust type of the pizza.
     */
    public Deluxe(Crust crust) {
        super(crust,new ArrayList<>(Arrays.asList(Topping.SAUSAGE, Topping.PEPPERONI, Topping.GREEN_PEPPER, Topping.ONION, Topping.MUSHROOM)));
    }

    /**
     * Calculates the price of the Deluxe pizza based on its size.
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
                return 16.99;
            case MEDIUM:
                return 18.99;
            case LARGE:
                return 20.99;
            default:
                throw new IllegalStateException("Unexpected value: " + getSize());
        }
    }
}
