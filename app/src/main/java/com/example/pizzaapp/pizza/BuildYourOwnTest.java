package com.example.pizzaapp.pizza;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the BuildYourOwn class.
 * This class tests the pricing logic for different sizes and toppings of the Build Your Own pizza.
 * @author Kyungmin Lee, Jack Lin
 */
public class BuildYourOwnTest {

    /**
     * Tests the price of a small Build Your Own pizza with no toppings.
     * Verifies that the base price is correctly set.
     */
    @Test
    public void testPriceSmallNoToppings() {
        BuildYourOwn pizza = new BuildYourOwn(Crust.HAND_TOSSED);
        pizza.changeSize(Size.SMALL);

        double expectedPrice = 8.99;
        assertEquals(expectedPrice, pizza.price(), 0.01);
        assertTrue(pizza.price() == expectedPrice);
        assertFalse(pizza.price() != expectedPrice);
    }

    /**
     * Tests the price of a medium Build Your Own pizza with three toppings.
     * Verifies that the price includes the base price plus the cost of the toppings.
     */
    @Test
    public void testPriceMediumWithThreeToppings() {
        BuildYourOwn pizza = new BuildYourOwn(Crust.HAND_TOSSED);
        pizza.changeSize(Size.MEDIUM);

        pizza.getToppings().add(Topping.MUSHROOM);
        pizza.getToppings().add(Topping.ONION);
        pizza.getToppings().add(Topping.HAM);

        double expectedPrice = 10.99 + (1.69 * 3);
        assertEquals(expectedPrice, pizza.price(), 0.01);
        assertTrue(pizza.price() > 10.99);
        assertFalse(pizza.price() < 10.99);
    }

    /**
     * Tests the price of a large Build Your Own pizza with the maximum number of toppings.
     * Verifies that the price includes the base price plus the cost of all toppings.
     */
    @Test
    public void testPriceLargeWithMaxToppings() {
        BuildYourOwn pizza = new BuildYourOwn(Crust.HAND_TOSSED);
        pizza.changeSize(Size.LARGE);

        pizza.getToppings().add(Topping.MUSHROOM);
        pizza.getToppings().add(Topping.ONION);
        pizza.getToppings().add(Topping.BBQ_CHICKEN);
        pizza.getToppings().add(Topping.CHEDDAR);
        pizza.getToppings().add(Topping.SAUSAGE);
        pizza.getToppings().add(Topping.PEPPERONI);
        pizza.getToppings().add(Topping.HAM);

        double expectedPrice = 12.99 + (1.69 * 7);
        assertEquals(expectedPrice, pizza.price(), 0.01);
        assertTrue(pizza.price() >= expectedPrice);
        assertFalse(pizza.price() < expectedPrice);
    }

    /**
     * Tests the price calculation when the size is not set.
     * Verifies that an IllegalStateException is thrown with the appropriate message.
     */
    @Test
    public void testPriceInvalidSize() {
        BuildYourOwn pizza = new BuildYourOwn(Crust.HAND_TOSSED);
        try {
            pizza.price();
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("Size must be set"));
        }
    }

    /**
     * Tests the price calculation when adding and removing toppings from a small Build Your Own pizza.
     * Verifies that the price is updated correctly when toppings are added or removed.
     */
    @Test
    public void testPriceEdgeCaseAddRemoveTopping() {
        BuildYourOwn pizza = new BuildYourOwn(Crust.HAND_TOSSED);
        pizza.changeSize(Size.SMALL);

        pizza.getToppings().add(Topping.MUSHROOM);
        double priceWithTopping = 8.99 + 1.69;
        assertEquals(priceWithTopping, pizza.price(), 0.01);
        assertTrue(pizza.price() > 8.99);

        pizza.getToppings().remove(Topping.MUSHROOM);
        double priceWithoutTopping = 8.99;
        assertEquals(priceWithoutTopping, pizza.price(), 0.01);
        assertTrue(pizza.price() == 8.99);
        assertFalse(pizza.price() > 8.99);
    }
}
