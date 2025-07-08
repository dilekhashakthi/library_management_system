package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Logs;
import org.jasypt.util.text.BasicTextEncryptor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginFormController {

    @FXML
    public TextField txtEmail;

    @FXML
    private PasswordField txtpassword;

    @FXML
    void btnLoginOnAction(ActionEvent event) throws SQLException, IOException {
        // Secret key used for encrypting/decrypting password
        String key = "#0w3bHa";

        // Create a BasicTextEncryptor object and set the secret key
        BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
        basicTextEncryptor.setPassword(key);

        // SQL query to find user by email
        String SQL = "SELECT * FROM logs WHERE email = ?";
        Connection connection = DBConnection.getInstance().getConnection();
        PreparedStatement pstm = connection.prepareStatement(SQL);

        // Set email from text field
        pstm.setString(1, txtEmail.getText());

        // Execute query
        ResultSet resultSet = pstm.executeQuery();

        // If a user with that email exists
        if (resultSet.next()) {

            // Create a Logs object from the database result
            Logs logs = new Logs(
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("password")
            );

            try {
                // Decrypt the password stored in the database
                String decryptedPassword = basicTextEncryptor.decrypt(logs.getPassword());

                // Compare it with the password user entered
                if (decryptedPassword.equals(txtpassword.getText())) {
                    Stage stage = new Stage();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Dashboard.fxml"));
                    stage.setScene(new Scene(loader.load()));
                    stage.show();
                } else {
                    // If passwords do not match, show error
                    showError("Incorrect password.");
                }
            } catch (Exception e) {
                // If decryption fails show error
                showError("Decryption failed. Possibly corrupted password.");
            }

        } else {
            // If email not found in the database
            showError("User not found.");
        }
    }

    // This method runs when the "Create Account" link is clicked
    @FXML
    void linkOnAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/Signup.fxml"))));
        stage.show();
    }

    // Show an error alert with a custom message
    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}