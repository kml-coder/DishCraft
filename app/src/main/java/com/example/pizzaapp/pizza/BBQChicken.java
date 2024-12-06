package com.example.pizzaapp.pizza;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * BBQChicken class represents a BBQ Chicken pizza, which is a specific type of pizza.
 * It is characterized by its predefined set of toppings, including BBQ Chicken, Green Pepper, Provolone, and Cheddar.
 * The price varies based on the size of the pizza.
 * @author Kyungmin Lee, Jack Lin
 */
public class BBQChicken extends Pizza {

    /**
     * Constructor to create a BBQChicken pizza with a specified crust.
     * The toppings are predefined as BBQ Chicken, Green Pepper, Provolone, and Cheddar.
     * @param crust The crust type of the pizza.
     */
    public BBQChicken(Crust crust){
        super(crust, new ArrayList<>(Arrays.asList(Topping.BBQ_CHICKEN, Topping.GREEN_PEPPER, Topping.PROVOLONE, Topping.CHEDDAR)));
    }

    /**
     * Calculates the price of the BBQ Chicken pizza based on its size.
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
                return 14.99;
            case MEDIUM:
                return 16.99;
            case LARGE:
                return 19.99;
            default:
                throw new IllegalStateException("Unexpected value: " + getSize());
        }
    }
}
