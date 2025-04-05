package application;

import databasePart1.DatabaseHelper;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;

public class CreateReviewPage {

    private final DatabaseHelper databaseHelper;
    private final String reviewerName;

    public CreateReviewPage(DatabaseHelper databaseHelper, String reviewerName) {
        this.databaseHelper = databaseHelper;
        this.reviewerName = reviewerName;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(15));

        Label titleLabel = new Label("Create a New Review");

        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("Question", "Answer");
        typeComboBox.setPromptText("Select Type");

        TextField idField = new TextField();
        idField.setPromptText("Enter Question/Answer ID");

        TextArea reviewBox = new TextArea();
        reviewBox.setPromptText("Write your review here...");

        Button submitButton = new Button("Submit Review");
        Label statusLabel = new Label();

        submitButton.setOnAction(e -> {
            String type = typeComboBox.getValue();
            String idText = idField.getText();
            String reviewText = reviewBox.getText();

            if (type == null || idText.isEmpty() || reviewText.isEmpty()) {
                statusLabel.setText("All fields are required.");
                return;
            }

            try {
                int idOfReviewed = Integer.parseInt(idText);
                boolean isQuestion = type.equals("Question");

                databaseHelper.addReview(reviewerName, reviewText, isQuestion, idOfReviewed);
                statusLabel.setText("Review submitted successfully!");
                reviewBox.clear();
                idField.clear();
                typeComboBox.setValue(null);
            } catch (NumberFormatException ex) {
                statusLabel.setText("Invalid ID. Please enter a number.");
            } catch (SQLException ex) {
                ex.printStackTrace();
                statusLabel.setText("Database error while submitting review.");
            }
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> new ReviewerHomePage(databaseHelper, reviewerName).show(primaryStage));

        layout.getChildren().addAll(titleLabel, typeComboBox, idField, reviewBox, submitButton, backButton, statusLabel);
        Scene scene = new Scene(layout, 500, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Create Review");
        primaryStage.show();
    }
}
