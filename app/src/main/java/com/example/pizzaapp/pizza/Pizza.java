package com.example.pizzaapp.pizza;

import java.util.ArrayList;

/**
 * Pizza abstract class represents a generic pizza with a specified crust, size, and toppings.
 * It provides methods to get the crust, size, and toppings, as well as to change the size of the pizza.
 * This class also includes a method to calculate the price, which must be implemented by subclasses.
 * @author Kyungmin Lee, Jack Lin
 */
public abstract class Pizza {
    private ArrayList<Topping> toppings;
    private Crust crust;
    private Size size;

    /**
     * Abstract method to calculate the price of the pizza.
     * This method must be implemented by subclasses.
     * @return The price of the pizza.
     */
    public abstract double price();

    /**
     * Constructor to create a Pizza object with specified crust and toppings.
     * The size is initially set to null.
     * @param crust The crust type of the pizza.
     * @param toppings The list of toppings for the pizza.
     */
    public Pizza(Crust crust, ArrayList<Topping> toppings) {
        this.crust = crust;
        this.size = null;
        this.toppings = toppings;
    }

    /**
     * Retrieves the size of the pizza.
     * @return The size of the pizza.
     */
    public Size getSize() {
        return size;
    }

    /**
     * Retrieves the crust type of the pizza.
     * @return The crust type of the pizza.
     */
    public Crust getCrust() {
        return crust;
    }

    /**
     * Retrieves the list of toppings for the pizza.
     * @return The list of toppings.
     */
    public ArrayList<Topping> getToppings() {
        return toppings;
    }

    /**
     * Changes the size of the pizza.
     * @param size The new size of the pizza.
     */
    public void changeSize(Size size) {
        this.size = size;
    }

    /**
     * Returns a string representation of the Pizza object.
     * Includes details about the type of pizza, style, crust, size, and toppings.
     * @return A string representation of the pizza.
     */
    @Override
    public String toString() {
        String pizzaType;
        String pizzaCY;
        if (this instanceof Deluxe) {
            pizzaType = "Deluxe";
            if(this.crust.toString().equals("DEEP_DISH")){
                pizzaCY = "Chicago";
            } else {
                pizzaCY = "NY";
            }
        } else if (this instanceof BBQChicken) {
            pizzaType = "BBQ Chicken";
            if(this.crust.toString().equals("PAN")){
                pizzaCY = "Chicago";
            } else {
                pizzaCY = "NY";
            }
        } else if (this instanceof Meatzza) {
            pizzaType = "Meatzza";
            if(this.crust.toString().equals("STUFFED")){
                pizzaCY = "Chicago";
            } else {
                pizzaCY = "NY";
            }
        } else if (this instanceof BuildYourOwn) {
            pizzaType = "Build Your Own";
            if(this.crust.toString().equals("PAN")){
                pizzaCY = "Chicago";
            } else {
                pizzaCY = "NY";
            }
        } else {
            pizzaType = "Unknown";
            pizzaCY = "Unknown";
        }
        return String.format("%s Pizza %s style (%s crust) (%s size) with toppings: %s", pizzaType, pizzaCY, crust,
                size, String.join(", ", toppings.stream().map(Enum::name).toList()));
    }
}




