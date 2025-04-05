package application;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import databasePart1.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The {@code ReviewerRequestsPage} class represents the interface for managing reviewer requests.
 * Instructors can review pending requests, view user questions and answers, and grant reviewer access.
 */
public class ReviewerRequestsPage extends InstructorHomePage{

	/**
	 * Constructs a {@code ReviewerRequestsPage} instance.
	 *
	 * @param databaseHelper The database helper instance for database interactions.
	 * @param currentUser    The currently logged-in instructor user.
	 */
	public ReviewerRequestsPage(DatabaseHelper databaseHelper, User currentUser) {
		super(databaseHelper, currentUser);
		this.databaseHelper = databaseHelper;
		this.currentUser = currentUser;
		// TODO Auto-generated constructor stub
	}

	/**
	 * Displays the reviewer requests page, allowing the instructor to review pending requests
	 * and grant reviewer access to selected users.
	 *
	 * @param primaryStage The primary stage where the reviewer requests page is displayed.
	 */
	@Override
	public void show(Stage primaryStage) {

		// Label to display the user's questions heading
		Label questionLabel = new Label("User's Questions: ");
		questionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
		// Label to display the user's answers heading
		Label answerLabel = new Label("User's Answers: ");
		answerLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

		ListView<String> questionListView = new ListView<>();
		questionListView.setPlaceholder(new Label("No questions available"));

		ListView<String> answerListView = new ListView<>();
		answerListView.setPlaceholder(new Label("No answers available"));

		Button backButton = new Button("Back");
		backButton.setOnAction(a -> {
			new InstructorHomePage(databaseHelper, currentUser).show(primaryStage);
		});

		// ComboBox to select a user for deletion.
		ComboBox<String> userComboBox = new ComboBox<>();
		userComboBox.setPromptText("Select a user to give reviewer role.");
		List<String> userNameList = databaseHelper.getAllReviewerRequests();
		userComboBox.setItems(FXCollections.observableArrayList(userNameList));

		// Button to give reviewer access for a selected user.
		Button reviewerAccessButton = new Button("Give Access");
		reviewerAccessButton.setOnAction(a -> {
			String selectedUser = userComboBox.getValue();
			if (selectedUser == null) {
				showAlert("No User Selected", "Please select a user before giving access.");
			} else {
				showAccessConfirmation(selectedUser, userComboBox);
			}
		});

		// Update ListView when a user is selected
		userComboBox.setOnAction(e -> {
			String selectedUser = userComboBox.getValue();
			if (selectedUser != null) {
				List<String> userQuestions = null;
				try {
					userQuestions = databaseHelper.readUserQuestions(selectedUser);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				ObservableList<String> questionItems = FXCollections.observableArrayList(userQuestions);
				questionListView.setItems(questionItems);

				List<String> userAnswers = null;
				try {
					userAnswers = databaseHelper.getUserAnswers(selectedUser);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				ObservableList<String> answerItems = FXCollections.observableArrayList(userAnswers);
				answerListView.setItems(answerItems);
			}
		});

		answerListView.setOnMouseClicked(event -> {
			String selectedItem = answerListView.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				String answerText = selectedItem.split(" \\(", 2)[0]; // Extract answer text
				openAnswerPage(answerText);
			}
		});

		VBox layout = new VBox(10, userComboBox, questionLabel, questionListView, new Separator(),
				answerLabel, answerListView, new Separator(), reviewerAccessButton, backButton);
		layout.setPadding(new Insets(10));

		// Create the scene and set it on the primary stage
		primaryStage.setScene(new Scene(layout, 500, 600));
		//primaryStage.setScene(questionsScene);
		primaryStage.setTitle("Pending Reviewer Requests");
		primaryStage.show();
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

	/**
	 * Displays a confirmation dialog before granting reviewer access to a user.
	 * If confirmed, grants the reviewer role and removes the request from the database.
	 *
	 * @param username     The username of the user to grant reviewer access to.
	 * @param userComboBox The ComboBox containing the list of users.
	 */
	private void showAccessConfirmation(String username, ComboBox<String> userComboBox) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Access Confirmation");
		alert.setHeaderText("Are you sure you want to give reviewer access for " + username + "?");
		alert.setContentText("Click 'OK' to confirm or 'Cancel' to abort.");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			boolean success = addRole(username);
			if (success) {
				databaseHelper.removeReviewerRequest(username);
				List<String> updatedUserList = databaseHelper.getAllReviewerRequests();
				userComboBox.setItems(FXCollections.observableArrayList(updatedUserList));
			}
			else {
				showAlert("Error", "Access to reviewer for " + username + " was not given!");
			}
		}
	}

	/**
	 * Grants the "reviewer" role to the specified user.
	 *
	 * @param username The username of the user to receive reviewer access.
	 */
	private boolean addRole(String username) {
		boolean success = databaseHelper.addUserRoles(username, "reviewer");
		if (success) {
			showAlert("Success", "Access to reviewer for " + username + " has been given successfully!");
			return true;
		} else {
			showAlert("Error", "Access to reviewer for " + username + " was not given!");
			return false;
		}
	}

	/**
	 * Opens a new window displaying the original question for a selected answer.
	 *
	 * @param answer The selected answer text.
	 */
	private void openAnswerPage(String answer) {
		Stage answerStage = new Stage();
		VBox layout = new VBox(10);
		layout.setAlignment(Pos.CENTER); // Aligns all children to the center
		Label questionLabel = null;

		try {
			int answerId = databaseHelper.getAnswerIdByText(answer); //  Get question ID
			String existingQuestion = databaseHelper.getQuestionByAnswer(answerId);

			questionLabel = new Label("Original Question: " + existingQuestion);
			questionLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold;");
			questionLabel.setWrapText(true); // Enable text wrapping
			questionLabel.setMaxWidth(600); // Adjust width as needed

		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		layout.getChildren().addAll(questionLabel);
		answerStage.setScene(new Scene(layout, 500, 300));
		answerStage.setTitle("Original Question");
		answerStage.show();
	}
}
