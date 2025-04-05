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
 * SetupAccountPage class handles the account setup process for new users.
 * Users provide their username, password, and a valid invitation code to register.
 */
public class SetupAccountPage {

    private final DatabaseHelper databaseHelper;

    /**
     * Constructor to initialize SetupAccountPage with a DatabaseHelper.
     *
     * @param databaseHelper The DatabaseHelper instance for database operations.
     */
    public SetupAccountPage(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    /**
     * Displays the Setup Account page in the provided stage.
     * This version uses a SceneManager to enable dynamic "Back" navigation.
     *
     * @param primaryStage The primary stage where the scene will be displayed.
     */
    public void show(Stage primaryStage) {


        // Create input field for username.
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter Username");
        userNameField.setMaxWidth(250);

        // Create input field for password.
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter Password");
        passwordField.setMaxWidth(250);

        // Create input field for invitation code.
        TextField inviteCodeField = new TextField();
        inviteCodeField.setPromptText("Enter Invitation Code");
        inviteCodeField.setMaxWidth(250);

        // Label to display error messages.
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        // Labels to provide validation hints.
        Label validUserName = new Label("Username must have at least 4 and less than 16 characters, and the first character must be alphabetic.");
        validUserName.setStyle("-fx-text-fill: black; -fx-font-size: 12px;");
        Label validPassword = new Label("Password must have at least 8 characters, including an uppercase letter, a lowercase letter, a number, and a special character.");
        validPassword.setStyle("-fx-text-fill: black; -fx-font-size: 12px;");

        // Button to trigger account setup.
        Button setupButton = new Button("Setup");

        setupButton.setOnAction(a -> {
            // Retrieve user inputs.
            String userName = userNameField.getText().trim();
            String password = passwordField.getText().trim();
            String code = inviteCodeField.getText().trim();

            // Validate username and password.
            String errMessage = UserNameRecognizer.checkForValidUserName(userName);
            String resultText = PasswordEvaluator.evaluatePassword(password);

            try {
                // Check if this is the first user (i.e., the database is empty).
                boolean isFirstUser = databaseHelper.isDatabaseEmpty();

                if (!databaseHelper.doesUserExist(userName)) {
                    // Proceed if there are no validation errors.
                    if (errMessage.isEmpty() && resultText.isEmpty()) {
                        List<String> userRole = new ArrayList<>();
                        if (isFirstUser) {
                            userRole.add("admin");
                            // First user is automatically assigned the "admin" role.
                            User adminUser = new User(userName, password, userRole);
                            databaseHelper.register(adminUser);
                            errorLabel.setText("Admin account created successfully. Please log in again.");
                            // Close the stage to force re-login.
                            primaryStage.close();
                        } else if (databaseHelper.validateInvitationCode(code)) {
                            userRole.add("student");
                            // Regular users require a valid invitation code.
                            User newUser = new User(userName, password, userRole);
                            databaseHelper.register(newUser);
                            new WelcomeLoginPage(databaseHelper).show(primaryStage, newUser);
                        } else {
                            errorLabel.setText("Invalid invitation code.");
                        }
                    } else {
                        // Display validation errors.
                        errorLabel.setText(!errMessage.isEmpty() ? errMessage : resultText);
                    }
                } else {
                    errorLabel.setText("This username is already taken! Please choose another.");
                }
            } catch (SQLException e) {
                System.err.println("Database error: " + e.getMessage());
                errorLabel.setText("Database error occurred. Please try again.");
                e.printStackTrace();
            }
        });

        // Add a "Back" button that uses the SceneManager to return to the previous scene.
        Button backButton = new Button("Back");
        backButton.setOnAction(a -> {
        	new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
        });

        // Arrange all UI elements in a vertical layout.
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(userNameField, passwordField, inviteCodeField, setupButton, errorLabel, validUserName, validPassword, backButton);

        // Create and set the new scene.
        Scene scene = new Scene(layout, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Account Setup");
        primaryStage.show();
    }
}
