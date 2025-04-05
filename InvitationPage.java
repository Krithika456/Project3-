package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * InvitationPage represents the page where an admin can generate an invitation code.
 * The invitation code is displayed upon clicking the "Generate Invitation Code" button.
 */
public class InvitationPage extends AdminHomePage{

    public InvitationPage(DatabaseHelper databaseHelper, User currentUser) {
		super(databaseHelper, currentUser);
		// TODO Auto-generated constructor stub
	}

	/**
     * Displays the Invitation Page in the provided primary stage.
     *
     * @param databaseHelper An instance of DatabaseHelper to handle database operations.
     * @param primaryStage   The primary stage where the scene will be displayed.
     * @param currentUsser
     */
    public void show(DatabaseHelper databaseHelper, Stage primaryStage) {
        VBox layout = new VBox();
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");
        layout.setSpacing(10);

        // Title label for the page.
        Label titleLabel = new Label("Invitation Page");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Button to generate the invitation code.
        Button generateInviteButton = new Button("Generate Invitation Code");
        // Button to go back a page
        Button continueButton = new Button("Continue");

        // Label to display the generated invitation code.
        Label inviteCodeLabel = new Label("");
        inviteCodeLabel.setStyle("-fx-font-size: 14px; -fx-font-style: italic;");

        // Set up the button's action event.
        generateInviteButton.setOnAction(event -> {
            // Generate the invitation code using the database helper.
            String invitationCode = databaseHelper.generateInvitationCode();
            inviteCodeLabel.setText(invitationCode);
        });

        continueButton.setOnAction(a -> {
        	new AdminHomePage(databaseHelper, currentUser).show(primaryStage);
        });

        // Add all UI elements to the layout.
        layout.getChildren().addAll(titleLabel, generateInviteButton, continueButton, inviteCodeLabel);

        // Create and set the scene on the primary stage.
        Scene inviteScene = new Scene(layout, 800, 400);
        primaryStage.setScene(inviteScene);
        primaryStage.setTitle("Invitation Page");
        primaryStage.show();
    }
}
