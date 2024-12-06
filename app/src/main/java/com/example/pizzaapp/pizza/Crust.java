package com.example.pizzaapp.pizza;

/**
 * This enum class represents the available crust types for a pizza.
 * It includes constants such as DEEP_DISH, PAN, STUFFED, BROOKLYN, etc.
 * @author Kyungmin Lee, Jack Lin
 */
public enum Crust {
    DEEP_DISH,
    PAN,
    STUFFED,
    BROOKLYN,
    THIN,
    HAND_TOSSED;

    /**
     * Returns the name of the crust type as a string.
     * @return The name of the crust type.
     */
    @Override
    public String toString() {
        return this.name();
    }
}

