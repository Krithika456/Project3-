package application;

import java.util.Arrays;
import java.util.List;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * ListOfUsers class displays a list of all user accounts retrieved from the database.
 */
public class ListOfUsers extends AdminHomePage {

    /**
     * Constructor to initialize ListOfUsers with a DatabaseHelper.
     *
     * @param databaseHelper The DatabaseHelper instance for database operations.
     * @param currentUser
     */
    public ListOfUsers(DatabaseHelper databaseHelper, User currentUser) {
        super(databaseHelper, currentUser);
    }

    /**
     * Displays the List of Users page on the provided primary stage.
     *
     * @param primaryStage The primary stage where the scene will be displayed.
     */
    @Override
	public void show(Stage primaryStage) {
        VBox layout = new VBox(10); // Spacing of 10 pixels between elements
        layout.setStyle("-fx-alignment: top-center; -fx-padding: 20;");

        // Label to display the heading
        Label userLabel = new Label("List of all Users!");
        userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Button to trigger the listing of user accounts
        Button userListButton = new Button("List User Accounts");

        // TextArea to display the list of users
        TextArea list = new TextArea();
        list.setEditable(false);
        list.setStyle("-fx-font-family: monospace;");

        // Header for the user list table
        String header = String.format("%-15s%-15s%-15s\n", "Username", "Password", "Role");

        // Set action for the button to fetch and display the users
        userListButton.setOnAction(a -> {
            List<String> users = databaseHelper.getAllUsers();
            list.clear();
            list.appendText(header);

            // Check if the user list is not null or empty
            if (users != null && !users.isEmpty()) {
                for (String user : users) {
                    String[] userData = user.split(" ");
                    if (userData.length >= 3) {
                        // Join any additional parts as the role field
                        String roles = String.join(" ", Arrays.copyOfRange(userData, 2, userData.length));
                        list.appendText(String.format("%-15s%-15s%-15s\n", userData[0], userData[1], roles));
                    }
                }
            } else {
                list.appendText("No users found.\n");
            }
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(a -> {
        	new AdminHomePage(databaseHelper, currentUser).show(primaryStage);
        });

        // Add UI elements to the layout
        layout.getChildren().addAll(userLabel, userListButton, list, backButton);

        // Create the scene and set it on the primary stage
        Scene userScene = new Scene(layout, 800, 400);
        primaryStage.setScene(userScene);
        primaryStage.setTitle("List of all Users");
        primaryStage.show();
    }
}
