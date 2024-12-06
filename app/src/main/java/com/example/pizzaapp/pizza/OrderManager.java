package com.example.pizzaapp.pizza;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

/**
 * OrderManager class is responsible for managing all orders within the application.
 * It maintains a singleton instance, allowing centralized management of orders.
 * It allows adding, removing, and retrieving orders, and assigns unique order numbers.
 */
public class OrderManager {

    private static OrderManager instance;
    private static int nextOrderNumber = 1;
    private final MutableLiveData<List<Order>> ordersLiveData;

    /**
     * Private constructor to prevent instantiation.
     * Initializes the list of orders as an observable list.
     */
    private OrderManager() {
        ordersLiveData = new MutableLiveData<>(new ArrayList<>());
    }

    /**
     * Retrieves the singleton instance of OrderManager.
     * If the instance does not exist, it creates one.
     * @return The singleton instance of OrderManager.
     */
    public static synchronized OrderManager getInstance() {
        if (instance == null) {
            instance = new OrderManager();
        }
        return instance;
    }

    /**
     * Adds an order to the list of orders and assigns a unique order number.
     * @param order The order to be added.
     */
    public void addOrder(Order order) {
        assignOrderNumber(order); // Assign a unique number to the order
        List<Order> currentOrders = ordersLiveData.getValue();
        if (currentOrders != null) {
            currentOrders.add(order);
            ordersLiveData.setValue(currentOrders);
        }
    }

    /**
     * Retrieves the list of orders as an observable list.
     * @return The observable list of orders.
     */
    public LiveData<List<Order>> getOrders() {
        return ordersLiveData;
    }

    /**
     * Removes an order from the list of orders.
     * @param order The order to be removed.
     */
    public void removeOrder(Order order) {
        List<Order> currentOrders = ordersLiveData.getValue();
        if (currentOrders != null) {
            currentOrders.remove(order);
            ordersLiveData.setValue(currentOrders);
        }
    }

    /**
     * Assigns a unique order number to the given order.
     * @param order The order to which the number will be assigned.
     */
    private void assignOrderNumber(Order order) {
        order.generateOrderNumber(nextOrderNumber);
        nextOrderNumber++;
    }
}
