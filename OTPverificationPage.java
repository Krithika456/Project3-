package application;

import databasePart1.DatabaseHelper;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The OTPVerificationPage class allows users to enter an OTP and reset their password.
 */
public class OTPverificationPage {

    private final DatabaseHelper databaseHelper;
    private final String userName;

    /**
     * Constructor to initialize OTPVerificationPage with a DatabaseHelper and the user's username.
     *
     * @param databaseHelper The DatabaseHelper instance for database operations.
     * @param userName       The username of the user whose OTP is being verified.
     */
    public OTPverificationPage(DatabaseHelper databaseHelper, String userName) {
        this.databaseHelper = databaseHelper;
        this.userName = userName;
    }

    /**
     * Displays the OTP Verification page on the provided primary stage.
     *
     * @param primaryStage The primary stage where the scene will be displayed.
     */
    public void show(Stage primaryStage) {
        // Field for OTP input.
        TextField otpField = new TextField();
        otpField.setPromptText("Enter OTP");
        otpField.setMaxWidth(250);

        // Field for new password input.
        PasswordField newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Enter New Password");
        newPasswordField.setMaxWidth(250);

        // Button to verify OTP and reset the password.
        Button verifyButton = new Button("Verify OTP");

        // Label for displaying messages (error or success).
        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        // OTP verification logic.
        verifyButton.setOnAction(a -> {
            String otp = otpField.getText().trim();
            String newPassword = newPasswordField.getText().trim();

            if (otp.isEmpty() || newPassword.isEmpty()) {
                messageLabel.setText("Please enter OTP and new password.");
                return;
            }

            if (databaseHelper.validateOneTimePassword(userName, otp)) {
                databaseHelper.updatePassword(userName, newPassword);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Password Reset Successful");
                alert.setHeaderText(null);
                alert.setContentText("Your password has been reset successfully. Please log in.");
                alert.showAndWait();

                // Redirect to the login page.
                new UserLoginPage(databaseHelper).show(primaryStage);
            } else {
                messageLabel.setText("Invalid OTP or expired. Try again.");
            }
        });

        // Arrange UI elements in a vertical layout.
        VBox layout = new VBox(10);
        layout.setStyle("-fx-padding: 20; -fx-alignment: center;");
        layout.getChildren().addAll(otpField, newPasswordField, verifyButton, messageLabel);

        // Create and set the scene on the primary stage.
        Scene scene = new Scene(layout, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("OTP Verification");
        primaryStage.show();
    }
}
