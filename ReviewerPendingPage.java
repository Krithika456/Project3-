package application;

import java.util.List;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ReviewerPendingPage {

    private final String userName;

    public ReviewerPendingPage(String userName) {
        this.userName = userName;
    }

    public void show(Stage primaryStage) {
        VBox layout = new VBox(15);
        layout.setStyle("-fx-alignment: center; -fx-padding: 30;");

        Label headingLabel = new Label("Reviewer Request Sent!");
        headingLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label infoLabel = new Label("Hi " + userName + ", your request to become a reviewer has been submitted.");
        Label statusLabel = new Label("Please wait for instructor approval before you can access reviewer features.");
        infoLabel.setStyle("-fx-font-size: 14px;");
        statusLabel.setStyle("-fx-font-size: 13px;");

        Button backButton = new Button("Back to Home");
        backButton.setOnAction(e -> {
            try {
                // Use a new instance of DatabaseHelper and create a temp User with just the student role
                DatabaseHelper db = new DatabaseHelper();
                db.connectToDatabase();
                new UserHomePage(db, new User(userName, "", List.of("student"))).show(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        layout.getChildren().addAll(headingLabel, infoLabel, statusLabel, backButton);

        Scene scene = new Scene(layout, 600, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Pending Reviewer Request");
        primaryStage.show();
    }
}
