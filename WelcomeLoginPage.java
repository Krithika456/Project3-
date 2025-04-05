package application;

import java.sql.SQLException;
import java.util.List;

import databasePart1.DatabaseHelper;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The WelcomeLoginPage class displays a welcome screen for authenticated users.
 * It allows users to navigate to their respective pages based on their role or quit the application.
 */
public class WelcomeLoginPage {

    protected DatabaseHelper databaseHelper;
    protected User currentUser;

    /**
     * Constructor to initialize WelcomeLoginPage with a DatabaseHelper.
     *
     * @param databaseHelper The DatabaseHelper instance for database operations.
     */
    public WelcomeLoginPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Displays the welcome screen on the primary stage for the authenticated user.
     *
     * @param primaryStage The primary stage where the scene is displayed.
     * @param user         The authenticated user.
     */
    public void show(Stage primaryStage, User currentUser) {
        VBox layout = new VBox(5);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label welcomeLabel = new Label("Welcome!!");
        welcomeLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        List<String> roles = currentUser.getRole();

        // Button to navigate to admin page
    	Button adminButton = new Button("Continue to Admin Page");
    	adminButton.setOnAction(a -> {
        	new AdminHomePage(databaseHelper, currentUser).show(primaryStage);
        });
    	// Button to navigate to instructor page
    	Button instructorButton = new Button("Continue to Instructor Page");
    	instructorButton.setOnAction(a -> {
        	new InstructorHomePage(databaseHelper, currentUser).show(primaryStage);
        });
    	// Button to navigate to staff page
    	Button staffButton = new Button("Continue to Staff Page");
    	staffButton.setOnAction(a -> {
        	new StaffHomePage(databaseHelper).show(primaryStage);
        });
    	// Button to navigate to reviewer page
    	Button reviewerButton = new Button("Continue to Reviewer Page");
    	reviewerButton.setOnAction(a -> {
    	    new ReviewerHomePage(databaseHelper, currentUser.getUserName()).show(primaryStage);
        });
    	// Button to navigate to student page
    	Button studentButton = new Button("Continue to Student Page");
    	studentButton.setOnAction(a -> {
        	try {
				new UserHomePage(databaseHelper, currentUser).show(primaryStage);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });

        // Display the buttons based on user's roles
        if (roles.contains("admin")) {
        	layout.getChildren().add(adminButton);
        }
        if (roles.contains("instructor")) {
        	layout.getChildren().add(instructorButton);
        }
        if (roles.contains("staff")) {
        	layout.getChildren().add(staffButton);
        }
        if (roles.contains("reviewer")) {
        	layout.getChildren().add(reviewerButton);
        }
        if (roles.contains("student")) {
        	layout.getChildren().add(studentButton);
        }

        // Button to quit the application.
        Button quitButton = new Button("Quit");
        quitButton.setOnAction(a -> {
            databaseHelper.closeConnection();
            Platform.exit(); // Exit the JavaFX application.
        });

        layout.getChildren().addAll(welcomeLabel, quitButton);
        Scene welcomeScene = new Scene(layout, 800, 400);

        // Load the CSS stylesheet for improved styling.
        welcomeScene.getStylesheets().add(getClass().getResource("/application/style.css").toExternalForm());

        // Set the scene to the primary stage and display it.
        primaryStage.setScene(welcomeScene);
        primaryStage.setTitle("Welcome Page");
        primaryStage.show();
    }
}
