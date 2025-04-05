package application;

import java.sql.SQLException;
import java.util.List;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MyQuestionsPage extends UserHomePage{

	private ListView<String> userQuestionsList = new ListView<>();

	public MyQuestionsPage(DatabaseHelper databaseHelper, User currentUser) {
		super(databaseHelper, currentUser);
		this.databaseHelper = databaseHelper;
		this.currentUser = currentUser;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void show(Stage primaryStage) {
        VBox layout = new VBox(10); // Spacing of 10 pixels between elements
        layout.setStyle("-fx-alignment: top-center; -fx-padding: 20;");

        // Label to display the heading
        Label questionLabel = new Label("Your Questions:");
        questionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        loadUserQuestions();

        Button backButton = new Button("Back");
        backButton.setOnAction(a -> {
            try {
				new UserHomePage(databaseHelper, currentUser).show(primaryStage);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });

        userQuestionsList.setOnMouseClicked(event -> {
            String selectedQuestion = userQuestionsList.getSelectionModel().getSelectedItem();
            if (selectedQuestion != null) {
                String questionText = selectedQuestion.split(" \\(", 2)[0]; // Extract question text
                showResolutionPrompt(questionText);
            }
        });

        // Add UI elements to the layout
        layout.getChildren().addAll(questionLabel, userQuestionsList, backButton);

        // Create the scene and set it on the primary stage
        Scene questionsScene = new Scene(layout, 800, 400);
        questionsScene.getStylesheets().add(getClass().getResource("/application/style.css").toExternalForm());
        primaryStage.setScene(questionsScene);
        primaryStage.setTitle("Your Questions");
        primaryStage.show();
    }

    // Load User's Questions
    private void loadUserQuestions() {
        userQuestionsList.getItems().clear();
        try {
            List<String> userQuestions = databaseHelper.readUserQuestions(currentUser.getUserName()); // Query questions by user
            System.out.println(userQuestions);
            userQuestionsList.getItems().addAll(userQuestions);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Show a prompt to mark the question as resolved
    private void showResolutionPrompt(String questionText) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Mark as Resolved");
        alert.setHeaderText("Do you want to mark the question as resolved?");
        alert.setContentText("Question: " + questionText);

        ButtonType markResolvedButton = new ButtonType("Mark Resolved");
        ButtonType cancelButton = new ButtonType("Cancel");

        alert.getButtonTypes().setAll(markResolvedButton, cancelButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == markResolvedButton) {
                try {
                    int questionId = databaseHelper.getQuestionIdByText(questionText); // Get the question ID by text
                    databaseHelper.markQuestionResolved(questionId); // Mark the question as resolved
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
