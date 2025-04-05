package application;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import databasePart1.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * AdminHomePage class represents the user interface for the admin user.
 * This page displays a welcome message and provides administrative options such as
 * listing users, updating roles, deleting user access, and logging out.
 */
public class AdminHomePage extends WelcomeLoginPage {

    /**
     * Constructor to initialize AdminHomePage with a DatabaseHelper.
     *
     * @param databaseHelper The DatabaseHelper instance for database operations.
     */
	/*
    public AdminHomePage(DatabaseHelper databaseHelper) {
    	super(databaseHelper);
    }
    */

    /**
     * Constructor to initialize AdminHomePage with a DatabaseHelper and String.
     *
     * @param databaseHelper The DatabaseHelper instance for database operations.
     */
    public AdminHomePage(DatabaseHelper databaseHelper, User currentUser) {
    	super(databaseHelper);
        this.currentUser = currentUser;
    }

    /**
     * Displays the admin page on the provided primary stage.
     *
     * @param primaryStage The primary stage where the scene will be displayed.
     */
    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        // Label to display the welcome message for the admin.
        Label adminLabel = new Label("Hello, Admin!");
        adminLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // ComboBox to select a user for deletion.
        ComboBox<String> userComboBox = new ComboBox<>();
        userComboBox.setPromptText("Select a user to delete");
        List<String> userList = fetchUserList();
        // Breaks the userName password role string into only userName
        List<String> userNameList = userList.stream().map(user -> user.split(" ")[0].trim()).collect(Collectors.toList());

        userComboBox.setItems(FXCollections.observableArrayList(userNameList));

        // Button to log out and return to the login selection page.
        Button logOutButton = new Button("Log Out");
        logOutButton.setOnAction(a -> new SetupLoginSelectionPage(databaseHelper).show(primaryStage));

        // Button to navigate to the list of all user accounts.
        Button listOfUsersButton = new Button("List User Accounts");
        listOfUsersButton.setOnAction(a -> new ListOfUsers(databaseHelper, currentUser).show(primaryStage));

        // Button to navigate to the update roles page.
        Button updateRoleButton = new Button("Update Roles");
        updateRoleButton.setOnAction(a -> new updateRoles(databaseHelper, currentUser).show(primaryStage));

        // Button to delete access for a selected user.
        Button deleteAccessButton = new Button("Delete Access");
        deleteAccessButton.setOnAction(a -> {
            String selectedUser = userComboBox.getValue();
            if (selectedUser == null) {
                showAlert("No User Selected", "Please select a user before deleting.");
            } else {
                showDeleteConfirmation(selectedUser);
            }
        });

        // "Invite" button for admin to generate invitation codes.
        Button inviteButton = new Button("Invite");
        inviteButton.setOnAction(a -> {
            new InvitationPage(databaseHelper, currentUser).show(databaseHelper, primaryStage);
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(a -> {
        	WelcomeLoginPage welcomeLoginPage = new WelcomeLoginPage(databaseHelper);
        	System.out.println("Current USer Admin: " + currentUser);
        	welcomeLoginPage.show(primaryStage, currentUser);
        });

        // Add all UI elements to the layout.
        layout.getChildren().addAll(adminLabel, listOfUsersButton, updateRoleButton, deleteAccessButton,
                                    userComboBox, inviteButton, backButton, logOutButton);

        // Create the scene and add the stylesheet.
        Scene adminScene = new Scene(layout, 800, 400);
        adminScene.getStylesheets().add(getClass().getResource("/application/style.css").toExternalForm());


        primaryStage.setScene(adminScene);
        primaryStage.setTitle("Admin Page");
        primaryStage.show();
    }

    /**
     * Fetches a list of users from the database.
     *
     * @return a List of user strings.
     */
    private List<String> fetchUserList() {
        try {
            return databaseHelper.getAllUsers();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to fetch users from the database.");
            return List.of();
        }
    }

    /**
     * Displays a confirmation dialog for deleting a user's access.
     *
     * @param username The username of the user to delete.
     */
    private void showDeleteConfirmation(String username) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure you want to delete access for " + username + "?");
        alert.setContentText("Click 'OK' to confirm or 'Cancel' to abort.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            deleteUser(username);
        }
    }

    /**
     * Deletes the specified user from the database.
     *
     * @param username The username of the user to delete.
     */
    private void deleteUser(String username) {
        try {
            boolean success = databaseHelper.deleteUser(currentUser, username);
            if (success) {
            	showAlert("Success", "Access for " + username + " deleted successfully!");
            } else {
            	showAlert("Error", "Cannot delete an admin user: " + username);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to delete user: " + username);
        }
    }

    /**
     * Displays an informational alert with the specified title and message.
     *
     * @param title   The title of the alert.
     * @param message The message to display.
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
