package application;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * QuestionsListGUI class displays a list of all questions retrieved from the database.
 */
public class QuestionsListGUI extends UserHomePage{
	private ListView<String> allQuestionsList = new ListView<>(); //  Shows all questions
	private int filterState = 0; // 0 = Show All, 1 = Show Unresolved, 2 = Show Resolved

	/**
	 * Constructor to initialize QuestionsListGUI with a DatabaseHelper.
	 *
	 * @param databaseHelper The DatabaseHelper instance for database operations.
	 * @param currentUser
	 * @param currentUser
	 */
	public QuestionsListGUI(DatabaseHelper databaseHelper, User currentUser) {
		super(databaseHelper, currentUser);
	}

	/**
	 * Displays the Questions List page on the provided primary stage.
	 *
	 * @param primaryStage The primary stage where the scene will be displayed.
	 * @throws SQLException
	 */
	@Override
	public void show(Stage primaryStage) throws SQLException {
		VBox layout = new VBox(10); // Spacing of 10 pixels between elements
		layout.setStyle("-fx-alignment: top-center; -fx-padding: 20;");

		ComboBox<String> filterComboBox = new ComboBox<>();
		filterComboBox.getItems().addAll("Show All", "Show Unresolved", "Show Resolved");
		filterComboBox.setValue("Show All"); // Default selection

		// Label to display the heading
		Label questionLabel = new Label("Questions:");
		questionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

		loadallQuestions();

		Button backButton = new Button("Back");
		backButton.setOnAction(a -> {
			try {
				new UserHomePage(databaseHelper, currentUser).show(primaryStage);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});

		Button refreshButton = new Button("Refresh");
		refreshButton.setOnAction(a -> {
			loadallQuestions();
		});

		filterComboBox.setOnAction(a -> {
			String selectedOption = filterComboBox.getValue();
			if (selectedOption.equals("Show All")) {
				filterState = 0;
			} else if (selectedOption.equals("Show Unresolved")) {
				filterState = 1;
			} else if (selectedOption.equals("Show Resolved")) {
				filterState = 2;
			}
			loadallQuestions();
		});

		allQuestionsList.setOnMouseClicked(event -> {
			String selectedItem = allQuestionsList.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				String questionText = selectedItem.split(" \\(", 2)[0]; // Extract question text
				openAnswerPage(questionText);
			}
		});

		// Add UI elements to the layout
		layout.getChildren().addAll(questionLabel, allQuestionsList, new Separator(), filterComboBox,
				refreshButton, backButton);

		// Create the scene and set it on the primary stage
		Scene questionsScene = new Scene(layout, 800, 400);
		questionsScene.getStylesheets().add(getClass().getResource("/application/style.css").toExternalForm());
		primaryStage.setScene(questionsScene);
		primaryStage.setTitle("List of all Questions");
		primaryStage.show();
	}

	//  Load Unresolved Questions
	private void loadallQuestions() {
		allQuestionsList.getItems().clear();
		try {
			List<String> allQuestions = databaseHelper.readAllQuestions();
			List<String> unresolvedQ = databaseHelper.filterUnansweredQuestions();
			List<String> resolvedQ = databaseHelper.filterAnsweredQuestions();
			System.out.println(resolvedQ);
			List<String> displayQuestions = new ArrayList<>();

			System.out.println("Filter Mode: " + filterState); // Debugging log to check filter state

			// If "Show All" is selected, make sure it includes all questions
			if (filterState == 0) {
				for (String question : allQuestions) {
					int unreadCount = databaseHelper.getUnreadAnswerCount(question);
					String formattedQuestion = question + " (" + unreadCount + " unread answers)";
					displayQuestions.add(formattedQuestion);
				}
			}
			// Show only unresolved questions
			else if (filterState == 1) {
				for (String question : unresolvedQ) {
					int unreadCount = databaseHelper.getUnreadAnswerCount(question);
					String formattedQuestion = question + " (" + unreadCount + " unread answers)";
					displayQuestions.add(formattedQuestion);
				}
			}
			// Show only resolved questions
			else if (filterState == 2) {
				for (String question : resolvedQ) {
					int unreadCount = databaseHelper.getUnreadAnswerCount(question);
					String formattedQuestion = question + " (" + unreadCount + " unread answers)";
					displayQuestions.add(formattedQuestion);
				}
			}

			allQuestionsList.getItems().setAll(displayQuestions);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//  Open Answer Page with Answer List and Redirect to Feedback Page
	private void openAnswerPage(String question) {
		Stage answerStage = new Stage();
		VBox layout = new VBox(10);
		layout.setPadding(new Insets(10));

		Label questionLabel = new Label("Question: " + question);

		// Display Existing Answers
		ListView<String> answersListView = new ListView<>();
		try {
			int questionId = databaseHelper.getQuestionIdByText(question); //  Get question ID
			List<String> existingAnswers = databaseHelper.readAnswersForQuestion(questionId);

			if (!existingAnswers.isEmpty()) {
				answersListView.getItems().addAll(existingAnswers);
			} else {
				answersListView.getItems().add("No answers yet.");
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		//  Answer Input Section
		TextArea answerInput = new TextArea();
		answerInput.setPromptText("Enter your answer here...");
		Button submitAnswerButton = new Button("Submit Answer");

		//  Submit Answer Logic - CLOSES THE WINDOW & REDIRECTS TO FEEDBACK PAGE
		submitAnswerButton.setOnAction(e -> {
			String answerText = answerInput.getText().trim();
			if (!answerText.isEmpty()) {
				try {
					int questionId = databaseHelper.getQuestionIdByText(question);
					databaseHelper.createAnswer(questionId, answerText, currentUser.getUserName());

					//  Close the Answer Page and Redirect to Feedback Page
					answerStage.close();
					openFeedbackPage(answerText);

				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		});

		layout.getChildren().addAll(questionLabel, answersListView, answerInput, submitAnswerButton);
		answerStage.setScene(new Scene(layout, 450, 450));
		answerStage.setTitle("Answer Question");
		answerStage.show();
	}

	//  Open Feedback Page After Answer Submission
	private void openFeedbackPage(String submittedAnswer) {
		Stage feedbackStage = new Stage();
		VBox layout = new VBox(10);
		layout.setPadding(new Insets(10));

		Label feedbackLabel = new Label("Answer Submitted Successfully!");
		Label correctnessLabel = new Label();
		Button returnButton = new Button("Return to Questions");

		//  Determine if the answer is correct (Placeholder Logic)
		if (submittedAnswer.toLowerCase().contains("correct")) {
			correctnessLabel.setText(" Your answer is likely correct.");
		} else {
			correctnessLabel.setText(" Your answer may need improvement.");
		}

		returnButton.setOnAction(e -> {
			feedbackStage.close();
			loadallQuestions();
		});

		layout.getChildren().addAll(feedbackLabel, correctnessLabel, returnButton);
		feedbackStage.setScene(new Scene(layout, 350, 200));
		feedbackStage.setTitle("Answer Feedback");
		feedbackStage.show();
	}

}
