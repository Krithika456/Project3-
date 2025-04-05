package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
/**
 * StaffPage class represents the user interface for the staff user.
 * This page displays a simple welcome message for the staff.
 */

public class StaffHomePage {
	/**
     * Displays the staff page in the provided primary stage.
     * @param primaryStage The primary stage where the scene will be displayed.
     */

	private final DatabaseHelper databaseHelper;

    public StaffHomePage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public void show(Stage primaryStage) {
    	VBox layout = new VBox();

	    layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

	    // label to display the welcome message for the staff
	    Label staffLabel = new Label("Hello, Staff!");

	    staffLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

	    //Button to log out
	    Button logOutButton = new Button("Log Out");
	    //Button functionality
	    logOutButton.setOnAction(a -> {
        	// Redirect to log in page
	    	new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
        });

	    layout.getChildren().addAll(staffLabel, logOutButton);
	    Scene staffScene = new Scene(layout, 800, 400);

	    // Set the scene to primary stage
	    primaryStage.setScene(staffScene);
	    primaryStage.setTitle("Staff Page");
    }
}