package controller;

import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Logs;
import org.jasypt.util.text.BasicTextEncryptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignUpFormController {

    @FXML
    private TextField txtEmail;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtpassword;

    @FXML
    void btnSignUoOnAction(ActionEvent event) throws SQLException {
        // Secret key used for encrypting/decrypting password
        String key = "#0w3bHa";

        // Create a BasicTextEncryptor object and set the secret key
        BasicTextEncryptor basicTextEncryptor = new BasicTextEncryptor();
        basicTextEncryptor.setPassword(key);

        // Check if the password and confirm password are the same
        if (!txtpassword.getText().equals(txtConfirmPassword.getText())) {
            new Alert(Alert.AlertType.ERROR, "Passwords do not match!").show();
            return; // Stop here if passwords don't match
        }

        // Get a database connection
        Connection connection = DBConnection.getInstance().getConnection();

        // Check if user already exists by email
        String checkSQL = "SELECT * FROM logs WHERE email = ?";
        PreparedStatement checkStmt = connection.prepareStatement(checkSQL);
        checkStmt.setString(1, txtEmail.getText());
        ResultSet resultSet = checkStmt.executeQuery();

        // If user does not already exist
        if (!resultSet.next()) {
            // Encrypt the password before storing
            String encryptedPassword = basicTextEncryptor.encrypt(txtpassword.getText());

            // Save to DB,Insert the new user into the database
            String insertSQL = "INSERT INTO logs(name, email, password) VALUES (?, ?, ?)";
            PreparedStatement insertStmt = connection.prepareStatement(insertSQL);
            insertStmt.setString(1, txtUsername.getText());
            insertStmt.setString(2, txtEmail.getText());
            insertStmt.setString(3, encryptedPassword);
            insertStmt.executeUpdate();

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Login Error");
            alert.setHeaderText(null);
            alert.setContentText("Signup successful!");
            alert.showAndWait();

            // Clear all text fields in the form
            txtUsername.clear();
            txtEmail.clear();
            txtpassword.clear();
            txtConfirmPassword.clear();

        }else {
            // If user already exists, show error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Signup Error");
            alert.setHeaderText(null);
            alert.setContentText("User already exists!");
            alert.showAndWait();
        }
    }
}