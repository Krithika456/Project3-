package application;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Handles student question interface with weightage-based review sorting
 */
public class StudentQuestionPage extends UserHomePage {
    /**
     * Helper class storing review metadata for sorting
     */

    private static class ReviewData {
        String reviewer;
        String text;
        double weightage;

        /**
         * @param reviewer Review author's username
         * @param text Review content
         * @param weightage Sorting weight (higher = more prominent)
         */

        ReviewData(String reviewer, String text, double weightage) {
            this.reviewer = reviewer;
            this.text = text;
            this.weightage = weightage;
        }
    }

    private VBox questionListBox = new VBox(10);
    private ListView<String> unresolvedQuestionsList = new ListView<>();
    private Map<String, Double> trustedReviewersMap = new HashMap<>();

    public StudentQuestionPage(DatabaseHelper databaseHelper, User currentUser) {
        super(databaseHelper, currentUser);
    }

    @Override
	public void show(Stage primaryStage) {
        Label askLabel = new Label("Ask a Question:");
        TextField questionInput = new TextField();
        ListView<String> relatedQuestionsList = new ListView<>();
        Button submitQuestionButton = new Button("Submit");

        questionInput.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.trim().isEmpty()) {
                List<String> relatedQuestions = databaseHelper.searchQuestions(newValue);
                relatedQuestionsList.getItems().setAll(relatedQuestions);
            } else {
                relatedQuestionsList.getItems().clear();
            }
        });

        relatedQuestionsList.setOnMouseClicked(event -> {
            String selectedQuestion = relatedQuestionsList.getSelectionModel().getSelectedItem();
            if (selectedQuestion != null) {
                openAnswerPage(selectedQuestion);
            }
        });

        Label unresolvedLabel = new Label("Your Unresolved Questions:");
        loadUnresolvedQuestions(currentUser.getUserName());

        unresolvedQuestionsList.setOnMouseClicked(event -> {
            String selectedItem = unresolvedQuestionsList.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                String questionText = selectedItem.split(" \\(", 2)[0];
                openAnswerPage(questionText);
            }
        });

        VBox layout = new VBox(10, askLabel, questionInput, relatedQuestionsList, submitQuestionButton,
                              new Separator(), unresolvedLabel, unresolvedQuestionsList, new Separator());
        layout.setPadding(new Insets(10));

        submitQuestionButton.setOnAction(e -> {
            String questionText = questionInput.getText().trim();
            if (!questionText.isEmpty()) {
                try {
                    databaseHelper.createQuestion(questionText, currentUser.getUserName());
                    refreshQuestions();
                    loadUnresolvedQuestions(currentUser.getUserName());
                    questionInput.clear();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    showAlert("Error", "Failed to submit the question.");
                }
            }
        });

        refreshQuestions();

        primaryStage.setScene(new Scene(layout, 500, 600));
        primaryStage.setTitle("Student Questions");
        primaryStage.show();
    }

    private void loadUnresolvedQuestions(String studentUsername) {
        unresolvedQuestionsList.getItems().clear();
        try {
            List<String> unresolvedQuestions = databaseHelper.getUnresolvedQuestions(studentUsername);
            for (String question : unresolvedQuestions) {
                int unreadCount = databaseHelper.getUnreadAnswerCount(question);
                unresolvedQuestionsList.getItems().add(question + " (" + unreadCount + " unread answers)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshQuestions() {
        questionListBox.getChildren().clear();
        try {
            List<String> questions = databaseHelper.readAllQuestions();
            for (String question : questions) {
                Button questionButton = new Button(question);
                questionButton.setOnAction(e -> openAnswerPage(question));
                questionListBox.getChildren().add(questionButton);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load questions.");
        }
    }

    private void openAnswerPage(String question) {
        Stage answerStage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label questionLabel = new Label("Question: " + question);
        ListView<String> answersListView = new ListView<>();
        Label reviewsLabel = new Label("Reviews (Sorted by Weightage):");
        ListView<String> reviewsListView = new ListView<>();

        try {
            int questionId = databaseHelper.getQuestionIdByText(question);
            List<String> existingAnswers = databaseHelper.readAnswersForQuestion(questionId);

            if (!existingAnswers.isEmpty()) {
                answersListView.getItems().addAll(existingAnswers);
                loadTrustedReviewers();

                answersListView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal != null && !newVal.equals("No answers yet.")) {
                        try {
                            int answerId = databaseHelper.getAnswerIdByText(newVal);
                            List<String> rawReviews = databaseHelper.readReviewsForReviewed(answerId);
                            List<ReviewData> sortedReviews = processAndSortReviews(rawReviews);
                            displaySortedReviews(sortedReviews, reviewsListView);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                answersListView.getItems().add("No answers yet.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        TextArea answerInput = new TextArea();
        answerInput.setPromptText("Enter your answer here...");
        Button submitAnswerButton = new Button("Submit Answer");

        submitAnswerButton.setOnAction(e -> {
            String answerText = answerInput.getText().trim();
            if (!answerText.isEmpty()) {
                try {
                    int questionId = databaseHelper.getQuestionIdByText(question);
                    databaseHelper.createAnswer(questionId, answerText, currentUser.getUserName());
                    answerStage.close();
                    openFeedbackPage(answerText);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        layout.getChildren().addAll(
            questionLabel,
            answersListView,
            new Separator(),
            reviewsLabel,
            reviewsListView,
            new Separator(),
            answerInput,
            submitAnswerButton
        );

        answerStage.setScene(new Scene(layout, 600, 600));
        answerStage.setTitle("Answer Question");
        answerStage.show();
    }

    private void loadTrustedReviewers() {
        try {
            List<String> trustedReviewers = databaseHelper.getTrustedReviewers(currentUser.getUserName());
            trustedReviewersMap.clear();
            for (String entry : trustedReviewers) {
                String[] parts = entry.split(" \\(");
                if (parts.length < 2) {
					continue;
				}

                String reviewer = parts[0];
                String weightPart = parts[1].replace("Weightage: ", "").replace(")", "");
                try {
                    double weightage = Double.parseDouble(weightPart);
                    trustedReviewersMap.put(reviewer, weightage);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid weightage format for " + reviewer);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Processes and sorts reviews by trusted reviewer weightage
     * @param rawReviews Unsorted list of reviews from database
     * @return List sorted descending by weightage, then by review text
     */
    private List<ReviewData> processAndSortReviews(List<String> rawReviews) {
        List<ReviewData> reviewDataList = new ArrayList<>();

        for (String review : rawReviews) {
            String[] parts = review.split(": ", 2);
            if (parts.length < 2) {
				continue;
			}

            String reviewer = parts[0];
            String text = parts[1];
            double weightage = trustedReviewersMap.getOrDefault(reviewer, 0.0);
            reviewDataList.add(new ReviewData(reviewer, text, weightage));
        }

        reviewDataList.sort((a, b) -> Double.compare(b.weightage, a.weightage));
        return reviewDataList;
    }

    /**
     * Formats sorted reviews for display with weightage values
     * @param sortedReviews Weightage-ordered review data
     * @param listView UI component to populate with formatted entries
     */
    private void displaySortedReviews(List<ReviewData> sortedReviews, ListView<String> listView) {
        List<String> displayItems = new ArrayList<>();
        for (ReviewData rd : sortedReviews) {
            String entry = rd.reviewer + ": " + rd.text;
            if (rd.weightage > 0) {
                entry += " (Weightage: " + String.format("%.1f", rd.weightage) + ")";
            }
            displayItems.add(entry);
        }
        listView.getItems().setAll(displayItems);
    }

    private void openFeedbackPage(String submittedAnswer) {
        Stage feedbackStage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label feedbackLabel = new Label("Answer Submitted Successfully!");
        Label correctnessLabel = new Label();
        Button returnButton = new Button("Return to Questions");

        if (submittedAnswer.toLowerCase().contains("correct")) {
            correctnessLabel.setText(" Your answer is likely correct.");
        } else {
            correctnessLabel.setText(" Your answer may need improvement.");
        }

        returnButton.setOnAction(e -> {
            feedbackStage.close();
            refreshQuestions();
            loadUnresolvedQuestions(currentUser.getUserName());
        });

        layout.getChildren().addAll(feedbackLabel, correctnessLabel, returnButton);
        feedbackStage.setScene(new Scene(layout, 350, 200));
        feedbackStage.setTitle("Answer Feedback");
        feedbackStage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}