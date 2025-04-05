package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The SetupLoginSelectionPage class allows users to choose between setting up a new account
 * or logging into an existing account. It provides two buttons for navigation to the respective pages.
 */
public class SetupLoginSelectionPage {

    private final DatabaseHelper databaseHelper;

    /**
     * Constructor to initialize the SetupLoginSelectionPage with a DatabaseHelper.
     *
     * @param databaseHelper The DatabaseHelper instance for database operations.
     */
    public SetupLoginSelectionPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Displays the setup/login selection page on the provided primary stage.
     *
     * @param primaryStage The primary stage where the scene will be displayed.
     */
    public void show(Stage primaryStage) {
        // Create a label with instructions for the user.
        Label instructionLabel = new Label("Welcome! Please choose an option:");
        instructionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Create buttons for account setup and login.
        Button setupButton = new Button("Set Up Account");
        Button loginButton = new Button("Login");

        // Set action for the setup button to navigate to the account setup page.
        setupButton.setOnAction(e -> new SetupAccountPage(databaseHelper).show(primaryStage));

        // Set action for the login button to navigate to the login page.
        loginButton.setOnAction(e -> new UserLoginPage(databaseHelper).show(primaryStage));

        // Arrange the label and buttons in a vertical layout.
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(instructionLabel, setupButton, loginButton);

        Scene scene = new Scene(layout, 800, 400);
        scene.getStylesheets().add(getClass().getResource("/application/style.css").toExternalForm());


        // Set the scene on the primary stage.
        primaryStage.setScene(scene);
        primaryStage.setTitle("Account Setup / Login");
        primaryStage.show();
    }
}
