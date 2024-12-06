package com.example.pizzaapp.controller;

import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import pizza.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * ViewOrdersController class is responsible for managing the view where users can see their orders.
 * It allows users to cancel orders and export order details to a text file.
 * This controller also manages the display of order details in a table view.
 * @author Kyungmin Lee, Jack Lin
 */
public class ViewOrdersController {
    @FXML
    private TableView<Order> ordersTableView;

    @FXML
    private TableColumn<Order, Integer> orderNumberColumn;

    @FXML
    private TableColumn<Order, Double> orderTotalColumn;

    @FXML
    private TableColumn<Order, String> listofPizzasColumn;

    @FXML
    private TextArea outputArea;

    private ObservableList<Order> ordersList = FXCollections.observableArrayList();

    /**
     * Initializes the ViewOrdersController by setting up the table columns and populating the orders list.
     */
    @FXML
    public void initialize() {
        orderNumberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        orderTotalColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        orderTotalColumn.setCellFactory(column -> new javafx.scene.control.TableCell<Order, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item));
                }
            }
        });
        listofPizzasColumn.setCellValueFactory(cellData -> {
            Order order = cellData.getValue();
            String pizzaList = order.getPizzas().stream()
                    .map(Pizza::toString)
                    .reduce((pizza1, pizza2) -> pizza1 + "\n" + pizza2)
                    .orElse("No pizzas");
            return new javafx.beans.property.SimpleStringProperty(pizzaList);
        });
        ordersList = OrderManager.getInstance().getOrders();
        ordersTableView.setItems(ordersList);
    }

    /**
     * Handles the cancellation of a selected order.
     * Removes the selected order from the OrderManager and updates the orders list.
     */
    @FXML
    private void handleCancelOrder() {
        Order selectedOrder = ordersTableView.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            OrderManager.getInstance().removeOrder(selectedOrder);
            logMessage("Order cancelled: " + selectedOrder.getNumber());
        }
    }

    /**
     * Handles exporting all orders to a text file.
     * Exports the details of each order, including order number, total price, and list of pizzas.
     */
    @FXML
    private void handleExportOrders() {
        List<Order> orders = OrderManager.getInstance().getOrders();
        if (orders.isEmpty()) {
            logMessage("No orders to export.");
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("orders_export.txt"))) {
            for (Order order : orders) {
                writer.write("Order Number: " + order.getNumber() + "\n");
                writer.write("Total Price: " + String.format("%.2f", order.getTotalPrice()) + "\n");
                writer.write("Pizzas:\n");
                for (Pizza pizza : order.getPizzas()) {
                    writer.write(pizza.toString() + "\n");
                }
                writer.write("\n");
            }
            logMessage("Orders exported successfully to orders_export.txt");
        } catch (IOException e) {
            logMessage("Error exporting orders: " + e.getMessage());
        }
    }

    /**
     * Logs a message to the output area.
     * @param message The message to be logged.
     */
    @FXML
    private void logMessage(String message) {
        outputArea.appendText(message + "\n");
    }
}
