package application;

import java.sql.SQLException;
import java.util.List;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The UserLoginPage class provides a login interface for users to access their accounts.
 * It validates the user's credentials and navigates to the appropriate page upon successful login.
 */
public class UserLoginPage {

    private final DatabaseHelper databaseHelper;

    /**
     * Constructor to initialize UserLoginPage with a DatabaseHelper.
     *
     * @param databaseHelper The DatabaseHelper instance for database operations.
     */
    public UserLoginPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Displays the user login page on the provided primary stage.
     *
     * @param primaryStage The primary stage where the scene will be displayed.
     */
    public void show(Stage primaryStage) {
        // Input field for the user's username
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter Username");
        userNameField.setMaxWidth(250);

        // Input field for the user's password
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);

        // Label to display error messages
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        // Button to attempt login (Second Login Button)
        Button loginButton = new Button("Login");
        loginButton.setOnAction(a -> {
            // Retrieve user inputs (trimmed to remove extra whitespace)
            String userName = userNameField.getText().trim();
            String password = passwordField.getText().trim();

            try {
                System.out.println("Attempting login for user: " + userName);
                // Retrieve the user's roles from the database
                List<String> roles = databaseHelper.getUserRole(userName);
                WelcomeLoginPage welcomeLoginPage = new WelcomeLoginPage(databaseHelper);

                if (roles != null && !roles.isEmpty()) {
                    User user = new User(userName, password, roles);

                    if (databaseHelper.login(user)) {
                        System.out.println(" Login successful: " + user);
                        // Check if user has only 1 role
                        if (roles.size() == 1) {
                        	String role = roles.get(0).toLowerCase();
                        	System.out.println("User has a single role: " + role);

                        	if (role.equalsIgnoreCase("admin")) {
                        		new AdminHomePage(databaseHelper, user).show(primaryStage);
                        	} else if (role.equalsIgnoreCase("student")) {
                        		new UserHomePage(databaseHelper, user).show(primaryStage);
                        	} else if (role.equalsIgnoreCase("instructor")) {
                        		new InstructorHomePage(databaseHelper, user).show(primaryStage);
                        	} else if (role.equalsIgnoreCase("staff")) {
                        		new StaffHomePage(databaseHelper).show(primaryStage);
                        	} else if (role.equalsIgnoreCase("reviewer")) {
                        	    new ReviewerHomePage(databaseHelper, userName).show(primaryStage);
                        	} else {
                        		System.out.println("Unkown role: " + role);
                        		welcomeLoginPage.show(primaryStage, user);
                        	}
                        } else {
                        	// If there are multiple roles go to select screen
                        	welcomeLoginPage.show(primaryStage, user);
                        }
                    } else {
                        System.out.println(" Login failed: Incorrect password");
                        errorLabel.setText("Invalid credentials. Please try again.");
                    }
                } else {
                    System.out.println(" Login failed: User does not exist or has no role.");
                    errorLabel.setText("User account doesn't exist.");
                }
            } catch (SQLException e) {
                System.err.println(" Database error: " + e.getMessage());
                errorLabel.setText("An error occurred while logging in. Please try again.");
                e.printStackTrace();
            } catch (Exception e) {
                System.err.println(" Unexpected error: " + e.getMessage());
                e.printStackTrace();
            }
        });

        // Create a Back button that navigates to the previous scene
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
        });

        // Create the layout and add the UI elements
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(userNameField, passwordField, loginButton, errorLabel, backButton);

        // Create the scene
        Scene scene = new Scene(layout, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("User Login");
        primaryStage.show();
    }
}
