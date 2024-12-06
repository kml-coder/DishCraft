package com.example.pizzaapp.pizza;

/**
 * PizzaFactory interface defines methods to create different types of pizzas.
 * Classes implementing this interface must provide implementations for creating Deluxe, BBQ Chicken, Meatzza, and Build Your Own pizzas.
 * @author Kyungmin Lee, Jack Lin
 */
public interface PizzaFactory {

    /**
     * Creates a Deluxe pizza.
     * @return A new Deluxe pizza.
     */
    Pizza createDeluxe();

    /**
     * Creates a BBQ Chicken pizza.
     * @return A new BBQ Chicken pizza.
     */
    Pizza createBBQChicken();

    /**
     * Creates a Meatzza pizza.
     * @return A new Meatzza pizza.
     */
    Pizza createMeatzza();

    /**
     * Creates a Build Your Own pizza.
     * @return A new Build Your Own pizza.
     */
    Pizza createBuildYourOwn();
}