package com.example.pizzaapp.pizza;

/**
 * ChicagoPizza class is responsible for creating specific types of pizzas using Chicago-style crusts.
 * It implements the PizzaFactory interface to create different kinds of pizzas including Deluxe, BBQ Chicken, Meatzza, and Build Your Own.
 * @author Kyungmin Lee, Jack Lin
 */
public class ChicagoPizza implements PizzaFactory {

    /**
     * Creates a Deluxe pizza with a deep-dish crust.
     * @return A new Deluxe pizza with Chicago-style deep-dish crust.
     */
    @Override
    public Pizza createDeluxe() {
        return new Deluxe(Crust.DEEP_DISH);
    }

    /**
     * Creates a BBQ Chicken pizza with a pan crust.
     * @return A new BBQ Chicken pizza with Chicago-style pan crust.
     */
    @Override
    public Pizza createBBQChicken() {
        return new BBQChicken(Crust.PAN);
    }

    /**
     * Creates a Meatzza pizza with a stuffed crust.
     * @return A new Meatzza pizza with Chicago-style stuffed crust.
     */
    @Override
    public Pizza createMeatzza() {
        return new Meatzza(Crust.STUFFED);
    }

    /**
     * Creates a Build Your Own pizza with a pan crust.
     * @return A new Build Your Own pizza with Chicago-style pan crust.
     */
    @Override
    public Pizza createBuildYourOwn() {
        return new BuildYourOwn(Crust.PAN);
    }
}
