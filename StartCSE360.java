package application;

import java.sql.SQLException;

import databasePart1.DatabaseHelper;
import javafx.application.Application;
import javafx.stage.Stage;

public class StartCSE360 extends Application {

    private static final DatabaseHelper databaseHelper = new DatabaseHelper();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Connect to the database
            databaseHelper.connectToDatabase();

            // Check if the database is empty
            if (databaseHelper.isDatabaseEmpty()) {
                // If the database is empty, show the FirstPage for initial setup.
                new FirstPage(databaseHelper).show(primaryStage);
            } else {
                // Otherwise, show the login selection page.
                new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}
