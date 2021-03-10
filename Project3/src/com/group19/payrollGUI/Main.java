package com.group19.payrollGUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static com.group19.payrollGUI.Consts.*;

/**
 * Driver class, calls Client JavaFX class, Controller().
 * @author Sagnik Mukherjee, Michael Choe
 */

public class Main extends Application
{
    /**
     * Method to create top-level JavaFX container. Creates an instance
     * of a display window.
     * @param primaryStage top-level container
     * @throws Exception handles any unpredictable, invalid inputs
     */
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Parent root =
                FXMLLoader.load(getClass().getResource("View3.fxml"));
        primaryStage.setTitle("Payroll Processing System");
        primaryStage.setScene(new Scene(root, DEFAULT_WIDTH,
                DEFAULT_HEIGHT));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * Main method to create instance of client class.
     * @param args The command line arguments.
     */
    public static void main(String[] args)
    {
        launch(args);
    }
}
