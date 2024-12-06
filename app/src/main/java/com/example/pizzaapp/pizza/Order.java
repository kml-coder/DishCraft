package com.example.pizzaapp.pizza;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Order class represents an order which contains multiple pizzas.
 * It allows adding and removing pizzas, generating an order number, and calculating the total price.
 * @author Kyungmin Lee, Jack Lin
 */
public class Order {
    private int number;
    private ArrayList<Pizza> pizzas;

    /**
     * Default constructor to create an Order object with an empty list of pizzas and an initial order number of 0.
     */
    public Order(){
        pizzas = new ArrayList<Pizza>(Arrays.asList());
        number = 0;
    }

    /**
     * Retrieves the order number.
     * @return The order number.
     */
    public int getNumber() {
        return number;
    }

    /**
     * Retrieves the list of pizzas in the order.
     * @return The list of pizzas.
     */
    public ArrayList<Pizza> getPizzas(){
        return pizzas;
    }

    /**
     * Adds a pizza to the order.
     * @param pizza The pizza to be added to the order.
     */
    public void addPizza(Pizza pizza){
        pizzas.add(pizza);
    }

    /**
     * Removes a pizza from the order.
     * @param pizza The pizza to be removed from the order.
     */
    public void removePizza(Pizza pizza){
        pizzas.remove(pizza);
    }

    /**
     * Calculates the total price of the order, including sales tax.
     * Sales tax is assumed to be 6.625%.
     * @return The total price of the order, including sales tax.
     */
    public double getTotalPrice() {
        return pizzas.stream().mapToDouble(Pizza::price).sum() * 1.06625;
    }

    /**
     * Generates an order number based on the given order count.
     * @param orderCount The order count to be used as the order number.
     */
    public void generateOrderNumber(int orderCount) {
        this.number = orderCount;
    }
}