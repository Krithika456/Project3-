package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * updateRoles class provides an interface to add or remove roles for existing users.
 */
public class updateRoles extends AdminHomePage {

    /**
     * Constructor to initialize updateRoles with a DatabaseHelper.
     *
     * @param databaseHelper The DatabaseHelper instance for database operations.
     * @param currentUser
     */
    public updateRoles(DatabaseHelper databaseHelper, User currentUser) {
        super(databaseHelper, currentUser);
    }

    /**
     * Displays the Update Roles page on the provided primary stage.
     *
     * @param primaryStage The primary stage where the scene will be displayed.
     */
    @Override
	public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-alignment: top-center; -fx-padding: 20;");

        // Label to display the heading.
        Label userLabel = new Label("Update the Roles of Users!");
        userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Text field for username.
        TextField userNameField = new TextField();
        userNameField.setPromptText("Enter Username");
        userNameField.setMaxWidth(250);

        // Text field for the new role.
        TextField roleField = new TextField();
        roleField.setPromptText("Enter New Role");
        roleField.setMaxWidth(250);

        // Label to display error messages.
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        // Label to display success messages.
        Label successLabel = new Label();
        successLabel.setStyle("-fx-text-fill: green; -fx-font-size: 12px;");

        // Labels to display instructions.
        Label validUserName = new Label("Enter Username");
        validUserName.setStyle("-fx-text-fill: black; -fx-font-size: 12px;");
        Label validRole = new Label("Enter Role: admin, student, instructor, staff, or reviewer");
        validRole.setStyle("-fx-text-fill: black; -fx-font-size: 12px;");

        // Button to add a role.
        Button addButton = new Button("Add Role");
        addButton.setOnAction(a -> {
            // Retrieve and trim user input.
            String userName = userNameField.getText().trim();
            String newRole = roleField.getText().trim();

            // Check if both fields are filled.
            if (userName.isEmpty() || newRole.isEmpty()) {
                errorLabel.setText("Error: All fields must be filled!");
                successLabel.setText("");
                return;
            }
            // Attempt to add the new role.
            boolean updated = databaseHelper.addUserRoles(userName, newRole);
            if (updated) {
                successLabel.setText("Success: Role added!");
                errorLabel.setText("");
            } else {
                errorLabel.setText("Error: Could not update role.");
                successLabel.setText("");
            }
        });

        // Button to remove a role.
        Button removeButton = new Button("Remove Role");
        removeButton.setOnAction(a -> {
            // Retrieve and trim user input.
            String userName = userNameField.getText().trim();
            String newRole = roleField.getText().trim();

            // Check if both fields are filled.
            if (userName.isEmpty() || newRole.isEmpty()) {
                errorLabel.setText("Error: All fields must be filled!");
                successLabel.setText("");
                return;
            }
            // Attempt to remove the specified role.
            boolean updated = databaseHelper.removeUserRoles(userName, newRole);
            if (updated) {
                successLabel.setText("Success: Role removed!");
                errorLabel.setText("");
            } else {
                errorLabel.setText("Error: Could not update role.");
                successLabel.setText("");
            }
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(a -> {
        	new AdminHomePage(databaseHelper, currentUser).show(primaryStage);
        });

        // Add all UI elements to the layout.
        layout.getChildren().addAll(userLabel, userNameField, roleField, addButton, removeButton, errorLabel,
                                    successLabel, validUserName, validRole, backButton);

        // Create the scene and set it on the primary stage.
        Scene userScene = new Scene(layout, 800, 400);
        primaryStage.setScene(userScene);
        primaryStage.setTitle("UpdateRoles");
        primaryStage.show();
    }
}
