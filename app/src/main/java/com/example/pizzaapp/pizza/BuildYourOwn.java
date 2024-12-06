package com.example.pizzaapp.pizza;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * BuildYourOwn class represents a customizable pizza where users can choose their desired toppings.
 * The price is determined based on the base price of the pizza size and the number of additional toppings.
 * @author Kyungmin Lee, Jack Lin
 */
public class BuildYourOwn extends Pizza {

    /**
     * Constructor to create a BuildYourOwn pizza with a specified crust.
     * Initially, no toppings are set.
     * @param crust The crust type of the pizza.
     */
    public BuildYourOwn(Crust crust){
        super(crust, new ArrayList<>(Arrays.asList()));
    }

    /**
     * Calculates the price of the Build Your Own pizza based on its size and number of toppings.
     * The base price varies depending on the size, and each additional topping adds $1.69.
     * @return The price of the pizza.
     * @throws IllegalStateException if the size is not set.
     */
    public double price(){
        if (getSize() == null) {
            throw new IllegalStateException("Size must be set before calculating price.");
        }
        double basePrice;
        switch (getSize()){
            case SMALL:
                 basePrice = 8.99;
                 break;
            case MEDIUM:
                basePrice = 10.99;
                break;
            case LARGE:
                basePrice = 12.99;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + getSize());
        }
        return basePrice + (1.69 * getToppings().size());
    }
}
