package application;


import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * InstructorHomePage class represents the user interface for the instructor user.
 * This page displays a simple welcome message for the instructor.
 */

public class InstructorHomePage extends WelcomeLoginPage{

	/**
     * Constructs an InstructorHomePage instance.
     *
     * @param databaseHelper The database helper instance for database interactions.
     * @param currentUser    The currently logged-in instructor user.
     */
	public InstructorHomePage(DatabaseHelper databaseHelper, User currentUser) {
		super(databaseHelper);
		this.currentUser = currentUser;
	}

	/**
     * Displays the instructor home page with navigation options.
     *
     * @param primaryStage The primary stage where the instructor home page is displayed.
     */
	public void show(Stage primaryStage) {
		VBox layout = new VBox();
		layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

		//Button to log out
		Button logOutButton = new Button("Log Out");
		//Button functionality
		logOutButton.setOnAction(a -> {
			// Redirect to log in page
			new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
		});

		Button backButton = new Button("Back");
		backButton.setOnAction(a -> {
			WelcomeLoginPage welcomeLoginPage = new WelcomeLoginPage(databaseHelper);
			welcomeLoginPage.show(primaryStage, currentUser);
		});

		// Button to redirect Instructor to Reviewer Requests
		Button reviewerRequestsButton = new Button("Pending Reviewer Requests");
		reviewerRequestsButton.setOnAction(a -> {
			new ReviewerRequestsPage(databaseHelper, currentUser).show(primaryStage);
		});

		// Label to display Hello Instructor
		Label instructorLabel = new Label("Hello, Instructor!");
		instructorLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

		layout.getChildren().addAll(instructorLabel, reviewerRequestsButton, backButton, logOutButton);
		Scene instructorScene = new Scene(layout, 800, 400);
		instructorScene.getStylesheets().add(getClass().getResource("/application/style.css").toExternalForm());

		// Set the scene to primary stage
		primaryStage.setScene(instructorScene);
		primaryStage.setTitle("Instructor Page");

	}
}