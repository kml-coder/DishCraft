package com.example.pizzaapp.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * MainController class is responsible for handling user interactions in the main view.
 * This controller manages the navigation to different parts of the application, such as ordering pizza and viewing orders.
 * It interacts with JavaFX elements such as buttons, labels, and text areas.
 * @author Kyungmin Lee, Jack Lin
 */
public class MainController {
    @FXML
    private Label welcomeLabel;

    @FXML
    private Button orderPizzaButton;

    @FXML
    private Button viewOrdersButton;

    @FXML
    private TextArea outputArea;

    /**
     * Logs a message to the output area.
     * Appends the given message to the output area, followed by a new line.
     * @param message The message to be logged.
     */
    @FXML
    private void logMessage(String message) {
        outputArea.appendText(message + "\n");
    }

    /**
     * Handles the "Order Pizza" button action.
     * Navigates the user to the Order Pizza view and logs the action.
     */
    @FXML
    private void handleOrderPizza() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/view_orderpizza.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Order Pizza");
            stage.setScene(new Scene(root));
            stage.show();
            logMessage("Navigating to Order Pizza view...");
        } catch (IOException e) {
            logMessage("Error navigating to Order Pizza view: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handles the "View Orders" button action.
     * Navigates the user to the View Orders view and logs the action.
     */
    @FXML
    private void handleViewOrders() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/view_orders.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("View Orders");
            stage.setScene(new Scene(root));
            stage.show();
            logMessage("Navigating to View Orders view...");
        } catch (IOException e) {
            logMessage("Error navigating to View Orders view: " + e.getMessage());
        }
    }
}