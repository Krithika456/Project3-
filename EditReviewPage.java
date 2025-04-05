package application;

import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

public class EditReviewPage {

    private final DatabaseHelper databaseHelper;
    private final Review review;

    public EditReviewPage(DatabaseHelper databaseHelper, Review review) {
        this.databaseHelper = databaseHelper;
        this.review = review;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));

        Label titleLabel = new Label("Edit Review ID: " + review.getId());

        TextArea reviewTextArea = new TextArea();
        reviewTextArea.setText(review.getReviewText());

        Button updateButton = new Button("Update Review");
        Label statusLabel = new Label();

        updateButton.setOnAction(e -> {
            String updatedText = reviewTextArea.getText().trim();

            if (updatedText.isEmpty()) {
                statusLabel.setText("Review cannot be empty.");
                return;
            }

            try {
                databaseHelper.updateReview(review.getId(), updatedText);
                review.setReviewText(updatedText);
                statusLabel.setText("Review updated successfully!");
            } catch (SQLException ex) {
                ex.printStackTrace();
                statusLabel.setText("Error updating review.");
            }
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> new ReviewerMessagesPage(databaseHelper, review.getReviewerName()).show(primaryStage));

        layout.getChildren().addAll(titleLabel, reviewTextArea, updateButton, backButton, statusLabel);
        Scene scene = new Scene(layout, 500, 350);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Edit Review");
        primaryStage.show();
    }
}
