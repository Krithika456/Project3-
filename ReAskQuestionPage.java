package application;

import java.sql.SQLException;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * ReAskQuestionPage provides a form for students to modify a previous question
 * and submit an improved version that is linked to the original question.
 */
public class ReAskQuestionPage extends UserHomePage{

    private final int parentQuestionId;         // ID of the original question
    private final String originalQuestionText;  // Original question text to be pre-populated

    /**
     * Constructor for ReAskQuestionPage.
     *
     * @param databaseHelper       The DatabaseHelper instance for database operations.
     * @param parentQuestionId     The ID of the original question.
     * @param originalQuestionText The text of the original question.
     * @param currentUser      	   The user of the student re-asking the question.
     */
    public ReAskQuestionPage(DatabaseHelper databaseHelper, int parentQuestionId, String originalQuestionText, User currentUser) {
    	super(databaseHelper, currentUser);
        this.parentQuestionId = parentQuestionId;
        this.originalQuestionText = originalQuestionText;

    }

    /**
     * Displays the Re-Ask Question page on the provided primary stage.
     *
     * @param primaryStage The primary stage where the scene will be displayed.
     */
    @Override
	public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label titleLabel = new Label("Re-Ask Question");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label instructionsLabel = new Label("Modify the question below and submit your improved version:");
        instructionsLabel.setStyle("-fx-font-size: 14px;");

        // TextArea pre-populated with the original question text
        TextArea questionTextArea = new TextArea(originalQuestionText);
        questionTextArea.setWrapText(true);
        questionTextArea.setPrefWidth(600);
        questionTextArea.setPrefHeight(200);

        Button submitButton = new Button("Submit Re-Asked Question");
        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        submitButton.setOnAction(e -> {
            String newQuestionText = questionTextArea.getText().trim();
            if (newQuestionText.isEmpty()) {
                messageLabel.setText("The question text cannot be empty.");
                return;
            }
            try {
                // Store the improved question with the parent question ID
                databaseHelper.createQuestion(newQuestionText, parentQuestionId, currentUser.getUserName());
                messageLabel.setStyle("-fx-text-fill: green; -fx-font-size: 12px;");
                messageLabel.setText("Question submitted successfully!");
            } catch (SQLException ex) {
                ex.printStackTrace();
                messageLabel.setText("Error submitting question. Please try again.");
            }
        });

        // Back button to return to the student's home page
        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
			try {
				new UserHomePage(databaseHelper, currentUser).show(primaryStage);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

        layout.getChildren().addAll(titleLabel, instructionsLabel, questionTextArea, submitButton, messageLabel, backButton);
        Scene scene = new Scene(layout, 800, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Re-Ask Question");
        primaryStage.show();
    }
}
