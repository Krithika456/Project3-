package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ReviewerHomePage {

    private final DatabaseHelper databaseHelper;
    private final String reviewerName;

    public ReviewerHomePage(DatabaseHelper databaseHelper, String reviewerName) {
        this.databaseHelper = databaseHelper;
        this.reviewerName = reviewerName;
    }
    
    

    public void show(Stage primaryStage) {
        VBox layout = new VBox(10);
        layout.setStyle("-fx-alignment: center; -fx-padding: 20;");

        Label reviewerLabel = new Label("Hello, Reviewer!");
        reviewerLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Log Out button to return to the login selection page
        Button logOutButton = new Button("Log Out");
        logOutButton.setOnAction(a -> {
            new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
        });

        // Optional Back button (if additional navigation is needed)
        Button backButton = new Button("Back");
        backButton.setOnAction(a -> {
            new SetupLoginSelectionPage(databaseHelper).show(primaryStage);
        });
        
        Button viewMessagesButton = new Button("View Messages");
        viewMessagesButton.setOnAction(e -> {
            new ReviewerMessagesPage(databaseHelper, reviewerName).show(primaryStage);
        });

        layout.getChildren().addAll(reviewerLabel, viewMessagesButton, logOutButton, backButton);
        Scene reviewerScene = new Scene(layout, 800, 400);
        primaryStage.setScene(reviewerScene);
        primaryStage.setTitle("Reviewer Page");
        primaryStage.show();
    }
}
