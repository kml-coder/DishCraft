package com.example.pizzaapp.pizza;

/**
 * NYPizza class is responsible for creating specific types of pizzas using New York-style crusts.
 * It implements the PizzaFactory interface to create different kinds of pizzas including Deluxe, BBQ Chicken, Meatzza, and Build Your Own.
 * @author Kyungmin Lee, Jack Lin
 */
public class NYPizza implements PizzaFactory {

    /**
     * Creates a Deluxe pizza with a Brooklyn crust.
     * The size is not set initially and must be defined later.
     * @return A new Deluxe pizza with New York-style Brooklyn crust.
     */
    @Override
    public Pizza createDeluxe() {
        return new Deluxe(Crust.BROOKLYN);
    }

    /**
     * Creates a BBQ Chicken pizza with a thin crust.
     * @return A new BBQ Chicken pizza with New York-style thin crust.
     */
    @Override
    public Pizza createBBQChicken() {
        return new BBQChicken(Crust.THIN);
    }

    /**
     * Creates a Meatzza pizza with a hand-tossed crust.
     * @return A new Meatzza pizza with New York-style hand-tossed crust.
     */
    @Override
    public Pizza createMeatzza() {
        return new Meatzza(Crust.HAND_TOSSED);
    }

    /**
     * Creates a Build Your Own pizza with a hand-tossed crust.
     * Initially, no toppings are set, allowing toppings to be chosen later.
     * @return A new Build Your Own pizza with New York-style hand-tossed crust.
     */
    @Override
    public Pizza createBuildYourOwn() {
        return new BuildYourOwn(Crust.HAND_TOSSED);
    }
}
