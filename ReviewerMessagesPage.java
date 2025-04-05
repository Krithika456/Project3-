package application;

import databasePart1.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class ReviewerMessagesPage {

    private final DatabaseHelper databaseHelper;
    private final String reviewerName;
    private List<Review> reviews;

    public ReviewerMessagesPage(DatabaseHelper databaseHelper, String reviewerName) {
        this.databaseHelper = databaseHelper;
        this.reviewerName = reviewerName;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));

        Label titleLabel = new Label("Your Reviews and Messages:");
        ListView<String> reviewListView = new ListView<>();
        ListView<String> messageListView = new ListView<>();
        TextArea replyBox = new TextArea();
        replyBox.setPromptText("Write your reply here...");
        Button sendReplyButton = new Button("Send Reply");

        try {
            reviews = databaseHelper.getReviewsByReviewer(reviewerName);
            ObservableList<String> reviewItems = FXCollections.observableArrayList();
            for (Review review : reviews) {
                String type = review.getReviewType() ? "[Q]" : "[A]";
                reviewItems.add(type + " Review ID: " + review.getId() + " - " + review.getReviewText());
            }
            reviewListView.setItems(reviewItems);

            reviewListView.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal.intValue() >= 0 && newVal.intValue() < reviews.size()) {
                    int reviewId = reviews.get(newVal.intValue()).getId();
                    try {
                        List<String> messages = databaseHelper.getMessagesForReview(reviewId);
                        messageListView.setItems(FXCollections.observableArrayList(messages));
                        sendReplyButton.setOnAction(e -> {
                            String reply = replyBox.getText();
                            if (!reply.isEmpty()) {
                                try {
                                    databaseHelper.sendReplyToAuthor(reviewId, reviewerName, reply);
                                    replyBox.clear();
                                    messageListView.getItems().add("You: " + reply);
                                } catch (SQLException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Buttons
        Button backButton = new Button("Back");
        Button createReviewButton = new Button("Create Review");
        Button editReviewButton = new Button("Edit Selected Review");
        Button deleteReviewButton = new Button("Delete Selected Review");

        // Back to home
        backButton.setOnAction(e -> new ReviewerHomePage(databaseHelper, reviewerName).show(primaryStage));

        // Create
        createReviewButton.setOnAction(e -> {
            new CreateReviewPage(databaseHelper, reviewerName).show(primaryStage);
        });

        // Edit
        editReviewButton.setOnAction(e -> {
            int index = reviewListView.getSelectionModel().getSelectedIndex();
            if (index >= 0 && index < reviews.size()) {
                Review selectedReview = reviews.get(index);
                new EditReviewPage(databaseHelper, selectedReview).show(primaryStage);
            } else {
                showWarning("Please select a review to edit.");
            }
        });

        // Delete
        deleteReviewButton.setOnAction(e -> {
            int index = reviewListView.getSelectionModel().getSelectedIndex();
            if (index >= 0 && index < reviews.size()) {
                Review selectedReview = reviews.get(index);
                try {
                    databaseHelper.deleteReview(selectedReview.getId());
                    reviews.remove(index);
                    reviewListView.getItems().remove(index);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else {
                showWarning("Please select a review to delete.");
            }
        });

        layout.getChildren().addAll(
                titleLabel,
                reviewListView,
                createReviewButton,
                editReviewButton,
                deleteReviewButton,
                new Label("Messages:"),
                messageListView,
                new Label("Reply:"),
                replyBox,
                sendReplyButton,
                backButton
        );

        Scene scene = new Scene(layout, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Reviewer Messages");
        primaryStage.show();
    }

    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING, message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
