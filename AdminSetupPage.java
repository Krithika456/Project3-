package application;

import java.sql.SQLException;
import java.util.ArrayList;
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
 * The AdminSetupPage class handles the setup process for creating an administrator
 * account. This is intended to be used by the first user to initialize the system with admin credentials.
 */
public class AdminSetupPage {

    private final DatabaseHelper databaseHelper;

    /**
     * Constructor to initialize AdminSetupPage with a DatabaseHelper.
     *
     * @param databaseHelper The DatabaseHelper instance for database operations.
     */
    public AdminSetupPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Displays the Administrator Setup page on the provided primary stage.
     *
     * @param primaryStage The primary stage where the scene will be displayed.
     */
    public void show(Stage primaryStage) {
        // Input fields for username and password.
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter Admin Username");
        userNameField.setMaxWidth(250);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);

        // Label to display error messages for invalid input or registration issues.
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        // Labels for validation hints.
        Label validUserName = new Label();
        validUserName.setStyle("-fx-text-fill: black; -fx-font-size: 12px;");
        Label validPassword = new Label();
        validPassword.setStyle("-fx-text-fill: black; -fx-font-size: 12px;");

        // Display the username and password requirements.
        validUserName.setText(
                "Username must have at least 4 and less than 16 characters, and the first character must be alphabetic.");
        validPassword.setText(
                "Password must have at least 8 characters, including an uppercase letter, a lowercase letter, a number, and a special character.");

        // Button to initiate setup.
        Button setupButton = new Button("Setup");

        setupButton.setOnAction(a -> {
            // Retrieve user input.
            String userName = userNameField.getText();
            String password = passwordField.getText();

            // Validate username and password, capturing error messages if any.
            String errMessage = UserNameRecognizer.checkForValidUserName(userName);
            String resultText = PasswordEvaluator.evaluatePassword(password);

            try {
                // If both username and password validations pass, register the admin.
                if (errMessage.isEmpty() && resultText.isEmpty()) {
                    List<String> userRole = new ArrayList<>();
                    userRole.add("admin");
                    // Create a new User object with the admin role and register in the database.
                    User user = new User(userName, password, userRole);
                    databaseHelper.register(user);
                    System.out.println("Administrator setup completed.");

                    // Navigate to the Welcome Login Page.
                    new WelcomeLoginPage(databaseHelper).show(primaryStage, user);
                }
                // If username validation returned an error, display it.
                else if (!errMessage.isEmpty()) {
                    System.out.println(errMessage);
                    errorLabel.setText(errMessage);
                }
                // If password validation returned an error, display it.
                else if (!resultText.isEmpty()) {
                    System.out.println(resultText);
                    errorLabel.setText(resultText);
                }
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                errorLabel.setText("Database error occurred. Please try again.");
                e.printStackTrace();
            }
        });

        // Arrange all UI elements in a vertical layout.
        VBox layout = new VBox(10, userNameField, passwordField, setupButton, errorLabel, validUserName, validPassword);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Create and set the scene on the primary stage.
        Scene scene = new Scene(layout, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Administrator Setup");
        primaryStage.show();
    }
}
