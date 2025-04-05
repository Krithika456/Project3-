package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
/**
 * FirstPage class represents the initial screen for the first user.
 * It prompts the user to set up administrator access and navigate to the setup screen.
 */
public class FirstPage {

    // Reference to the DatabaseHelper for database interactions.
    private final DatabaseHelper databaseHelper;

    /**
     * Constructor to initialize FirstPage with a DatabaseHelper.
     *
     * @param databaseHelper The DatabaseHelper instance for database operations.
     */
    public FirstPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Displays the first page in the provided primary stage.
     * It shows a welcome message and a "Continue" button that navigates to the admin setup page.
     *
     * @param primaryStage The primary stage where the scene will be displayed.
     */
    public void show(Stage primaryStage) {
        // Create a vertical layout with spacing of 5 pixels between elements.
        VBox layout = new VBox(5);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        // Label to display the welcome message for the first user.
        Label userLabel = new Label("Hello..You are the first person here.\nPlease select continue to setup administrator access");
        userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Button to navigate to the AdminSetupPage.
        Button continueButton = new Button("Continue");
        continueButton.setOnAction(_ -> {
            new AdminSetupPage(databaseHelper).show(primaryStage);
        });

        // Add the label and button to the layout.
        layout.getChildren().addAll(userLabel, continueButton);

        // Create the scene with the layout and set it to the primary stage.
        Scene firstPageScene = new Scene(layout, 800, 400);

        // Load the CSS stylesheet.
        firstPageScene.getStylesheets().add(getClass().getResource("/application/style.css").toExternalForm());
        // In FirstPage.java (or wherever you load CSS):
        primaryStage.setScene(firstPageScene);
        primaryStage.setTitle("First Page");
        primaryStage.show();
    }
}
