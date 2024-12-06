package com.example.pizzaapp.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * MainApp class is the entry point of the JavaFX application.
 * It loads the FXML file and displays the main window of the RU Pizzeria application.
 * Extends the Application class to use JavaFX's lifecycle methods.
 * Provides the start() method to set up the primary stage, and a main() method to launch the application.
 * @author Kyungmin Lee, Jack Lin
 */
public class MainApp extends Application {

    /**
     * The main entry point for the JavaFX application.
     * This method is called after the application is launched, and it is responsible for loading the main FXML view and setting the scene.
     * @param primaryStage The primary stage for this application, onto which the application scene can be set.
     * @throws Exception if the FXML file cannot be loaded.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/resources/view_main.fxml"));
        primaryStage.setTitle("RU Pizzeria");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    /**
     * The main method to launch the JavaFX application.
     * This is the entry point of the program that initiates the JavaFX runtime and calls the start() method.
     * @param args command line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}