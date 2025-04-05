package application;

import java.sql.SQLException;
import java.util.List;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UserHomePage extends WelcomeLoginPage{

	public UserHomePage(DatabaseHelper databaseHelper, User currentUser) {
		super(databaseHelper);
		this.currentUser = currentUser;
	}

	public void show(Stage primaryStage) throws SQLException {
		VBox layout = new VBox(5);
		layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

		// Welcome message that includes the student's username
		Label userLabel = new Label("Hello, Student " + currentUser.getUserName() + "!");
		userLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

		// Label to display error messages.
		Label errorLabel = new Label();
		errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

		// Label to display success messages.
		Label successLabel = new Label();
		successLabel.setStyle("-fx-text-fill: green; -fx-font-size: 12px;");

		// Log Out button
		Button logOutButton = new Button("Log Out");
		logOutButton.setOnAction(a -> {
			new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
		});

		// Button to show ask questions
		Button askQuestionButton = new Button("Ask Questions");
		askQuestionButton.setOnAction(a -> {
			new StudentQuestionPage(databaseHelper, currentUser).show(primaryStage);
		});

		// Button to show all questions
		Button questionListButton = new Button("All Questions");
		questionListButton.setOnAction(a -> {
			try {
				new QuestionsListGUI(databaseHelper, currentUser).show(primaryStage);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});

		// Button to show student's own questions
		Button studentQuestionsButton = new Button("My Questions");
		studentQuestionsButton.setOnAction(a -> {
			new MyQuestionsPage(databaseHelper, currentUser).show(primaryStage);
		});

		// New button: Manage Trusted Reviewers
		Button manageReviewersButton = new Button("Manage Trusted Reviewers");
		manageReviewersButton.setOnAction(a -> {
			// Navigate to TrustedReviewersPage, passing the logged-in student's username
			new TrustedReviewersPage(databaseHelper, currentUser).show(primaryStage);
		});

		// Button to request to become a reviewer
		Button requestReviewerButton = new Button("Request to become a Reviewer");
		requestReviewerButton.setOnAction(a -> {
		    List<String> roles = databaseHelper.getUserRole(currentUser.getUserName());
		    if (roles.contains("reviewer")) {
		        // Already a reviewer — go directly to reviewer homepage
		        new ReviewerHomePage(databaseHelper, currentUser.getUserName()).show(primaryStage);
		    } else {
		        // Not a reviewer yet — submit request
		        String resultMessage = databaseHelper.addReviewerRequest(currentUser.getUserName());
		        if (resultMessage.startsWith("Success")) {
		            errorLabel.setText("");
		            successLabel.setText("");
		            new ReviewerPendingPage(currentUser.getUserName()).show(primaryStage);
		        } else {
		            errorLabel.setText(resultMessage);
		            successLabel.setText("");
		        }
		    }
		});

		Button backButton = new Button("Back");
		backButton.setOnAction(a -> {
			WelcomeLoginPage welcomeLoginPage = new WelcomeLoginPage(databaseHelper);
			welcomeLoginPage.show(primaryStage, currentUser);
		});

		layout.getChildren().addAll(userLabel, askQuestionButton, questionListButton,
				studentQuestionsButton, manageReviewersButton, requestReviewerButton, backButton, logOutButton,
				successLabel, errorLabel);
		Scene userScene = new Scene(layout, 800, 400);
		userScene.getStylesheets().add(getClass().getResource("/application/style.css").toExternalForm());
		primaryStage.setScene(userScene);
		primaryStage.setTitle("Student Page");
		primaryStage.show();
	}
}
