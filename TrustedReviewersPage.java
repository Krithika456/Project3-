package application;

import java.sql.SQLException;
import java.util.List;

import databasePart1.DatabaseHelper;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * UI for managing trusted reviewers and their weightage values
 */
public class TrustedReviewersPage extends UserHomePage {
    /**
     * Updates weightage for an existing trusted reviewer
     * @param studentUsername Student modifying the weightage
     * @param reviewerUsername Reviewer being updated
     * @param newWeightage Updated influence value (0.0-5.0)
     * @throws NumberFormatException If invalid numeric format
     */


    private ListView<String> trustedReviewersList = new ListView<>();

    public TrustedReviewersPage(DatabaseHelper databaseHelper, User currentUser) {
        super(databaseHelper, currentUser);
    }

    @Override
	public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Label titleLabel = new Label("Manage Trusted Reviewers");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Highlight: Fetch potential reviewers from database
        List<String> potentialReviewers = getPotentialReviewers();
        ComboBox<String> reviewerComboBox = new ComboBox<>(FXCollections.observableArrayList(potentialReviewers));
        reviewerComboBox.setPromptText("Select a reviewer");

        // Highlight: Added weightage input field
        TextField weightageField = new TextField();
        weightageField.setPromptText("Enter weightage (0.0-5.0)");

        Button addButton = new Button("Add Reviewer");
        addButton.setOnAction(a -> {
            String reviewer = reviewerComboBox.getValue();
            String weightageText = weightageField.getText();

            if (reviewer != null && !weightageText.isEmpty()) {
                try {
                    double weightage = Double.parseDouble(weightageText);
                    if (weightage >= 0.0 && weightage <= 5.0) {
                        databaseHelper.addTrustedReviewer(
                            currentUser.getUserName(),
                            reviewer,
                            weightage
                        );
                        refreshTrustedReviewersList();
                        weightageField.clear();
                    } else {
                        showAlert("Weightage must be between 0.0 and 5.0");
                    }
                } catch (NumberFormatException e) {
                    showAlert("Invalid weightage format. Please enter a number.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Highlight: Added update weightage section
        TextField updateWeightageField = new TextField();
        updateWeightageField.setPromptText("New weightage");

        Button updateButton = new Button("Update Weightage");
        updateButton.setOnAction(a -> {
            String selected = trustedReviewersList.getSelectionModel().getSelectedItem();
            if (selected != null && !updateWeightageField.getText().isEmpty()) {
                try {
                    String reviewer = selected.split(" \\(")[0]; // Extract username from list item
                    double newWeightage = Double.parseDouble(updateWeightageField.getText());

                    if (newWeightage >= 0.0 && newWeightage <= 5.0) {
                        databaseHelper.updateReviewerWeightage(
                            currentUser.getUserName(),
                            reviewer,
                            newWeightage
                        );
                        refreshTrustedReviewersList();
                        updateWeightageField.clear();
                    } else {
                        showAlert("Weightage must be between 0.0 and 5.0");
                    }
                } catch (NumberFormatException e) {
                    showAlert("Invalid weightage format. Please enter a number.");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button removeButton = new Button("Remove Reviewer");
        removeButton.setOnAction(a -> {
            String selected = trustedReviewersList.getSelectionModel().getSelectedItem();
            if (selected != null) {
                try {
                    String reviewer = selected.split(" \\(")[0]; // Extract username from list item
                    databaseHelper.removeTrustedReviewer(
                        currentUser.getUserName(),
                        reviewer
                    );
                    refreshTrustedReviewersList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            try {
                new UserHomePage(databaseHelper, currentUser).show(primaryStage);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        // Highlight: Configure trusted reviewers list
        trustedReviewersList.setPrefHeight(150);
        refreshTrustedReviewersList();

        layout.getChildren().addAll(
            titleLabel,
            reviewerComboBox,
            new Label("Weightage:"),
            weightageField,
            addButton,
            trustedReviewersList,
            new Label("Update Weightage:"),
            updateWeightageField,
            updateButton,
            removeButton,
            backButton
        );

        Scene scene = new Scene(layout, 800, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Trusted Reviewers Management");
        primaryStage.show();
    }

    // Highlight: New method to refresh the list of trusted reviewers
    private void refreshTrustedReviewersList() {
        try {
            List<String> reviewers = databaseHelper.getTrustedReviewers(currentUser.getUserName());
            trustedReviewersList.setItems(FXCollections.observableArrayList(reviewers));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Highlight: New method to get potential reviewers (replace with actual DB call)
    private List<String> getPotentialReviewers() {
        // In a real implementation, query database for users with reviewer role
        return List.of("reviewer1", "reviewer2", "reviewer3");
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}