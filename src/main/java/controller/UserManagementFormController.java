package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.User;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserManagementFormController implements Initializable {

    @FXML
    private TableColumn colContactInformation;

    @FXML
    private TableColumn colID;

    @FXML
    private TableColumn colMembershipDate;

    @FXML
    private TableColumn colName;

    @FXML
    private TableView tblUserTable;

    @FXML
    private TextField txtContactInformation;

    @FXML
    private TextField txtID;

    @FXML
    private TextField txtName;

    @FXML
    private DatePicker txtDatepicker;

    @FXML
    void btnAddOnAction(ActionEvent event) throws SQLException {
        // Check if all required fields are filled
        if (validateFields()) {

            // SQL command to insert a new user into the library_users table
            String SQL = "INSERT INTO library_users (id,name,contact_information,membership_date) VALUES (?,?,?,?)";

            // Get a connection to the database
            Connection connection = DBConnection.getInstance().getConnection();

            // Prepare the SQL statement to insert data safely
            PreparedStatement pstm = connection.prepareStatement(SQL);

            // Set values from input fields to the SQL statement
            pstm.setString(1, txtID.getText()); // Set ID
            pstm.setString(2, txtName.getText()); // Set Name
            pstm.setString(3, txtContactInformation.getText()); // Set Contact Information
            pstm.setDate(4,java.sql.Date.valueOf(txtDatepicker.getValue())); // Set Membership Date

            // Execute the SQL insert command
            pstm.executeUpdate();

            // Show success message
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Added Successfully");
            alert.showAndWait();
        }
        // Clear all text fields after adding the data
        clearTextFields();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure to delete?");
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.get() == ButtonType.OK) {
            String SQL = "DELETE FROM library_users WHERE " +
                    "id = ? AND " +
                    "name = ? AND " +
                    "contact_information = ? AND " +
                    "membership_date = ?";

            Connection connection = DBConnection.getInstance().getConnection();

            PreparedStatement pstm = connection.prepareStatement(SQL);
            pstm.setString(1, txtID.getText());
            pstm.setString(2, txtName.getText());
            pstm.setString(3, txtContactInformation.getText());
            pstm.setDate(4,java.sql.Date.valueOf(txtDatepicker.getValue()));

            pstm.executeUpdate();
        }
        //clear text field after adding data
        clearTextFields();
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String searchID = txtID.getText().trim();
        if (searchID.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter an ID to search.").show();
        }

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String SQL = "SELECT * FROM library_users WHERE id = ?";
            PreparedStatement pstm = connection.prepareStatement(SQL);
            pstm.setString(1, searchID);
            ResultSet resultSet = pstm.executeQuery();

            ObservableList<User> observableUserList = FXCollections.observableArrayList();

            if (resultSet.next()) {
                // Fill text fields
                txtID.setText(resultSet.getString("id"));
                txtName.setText(resultSet.getString("name"));
                txtContactInformation.setText(resultSet.getString("contact_information"));
                txtDatepicker.setValue(LocalDate.parse(resultSet.getString("membership_date")));

                // Add to TableView
                observableUserList.add(new User(
                        resultSet.getString("id"),
                        resultSet.getString("name"),
                        resultSet.getString("contact_information"),
                        resultSet.getDate("membership_date").toLocalDate()
                ));
            } else {
                new Alert(Alert.AlertType.WARNING, "No user found with this ID.").show();
            }
            tblUserTable.setItems(observableUserList);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error searching user: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (txtID.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter an ID to update.").show();
            return;
        }

        try {
            String SQL = "UPDATE library_users SET name = ?, contact_information = ?, membership_date = ? WHERE id = ?";
            Connection connection = DBConnection.getInstance().getConnection();

            PreparedStatement pstm = connection.prepareStatement(SQL);
            pstm.setString(1, txtName.getText());
            pstm.setString(2, txtContactInformation.getText());
            pstm.setDate(4, java.sql.Date.valueOf(txtDatepicker.getValue()));
            pstm.setString(4, txtID.getText());

            int updateCount = pstm.executeUpdate();

            if (updateCount > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Update Successfully..!");
                alert.showAndWait();

                // Optionally clear fields
                clearTextFields();

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText(null);
                alert.setContentText("No matching user found with that ID.");
                alert.showAndWait();
            }

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Update failed: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    @FXML
    void btnReloadOnAction(ActionEvent event) {
        loadTable();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTable();
    }

    public void loadTable() {
        try {
            colID.setCellValueFactory(new PropertyValueFactory<>("id"));
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colContactInformation.setCellValueFactory(new PropertyValueFactory<>("contactInfomation"));
            colMembershipDate.setCellValueFactory(new PropertyValueFactory<>("membershipDate"));

            ObservableList<User> userObservableList = FXCollections.observableArrayList();
            Connection connection = DBConnection.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM library_users");

            while (resultSet.next()) {
                userObservableList.add(new User(
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getDate(4).toLocalDate()

                ));
            }

            tblUserTable.setItems(userObservableList);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean validateFields() {
        if (txtID.getText().isEmpty() | txtName.getText().isEmpty()
                | txtContactInformation.getText().isEmpty() | txtDatepicker.getValue()==null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validate Fields");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Into The Fields");
            alert.showAndWait();

            return false;
        }
        return true;
    }

    public void clearTextFields() {
        txtID.clear();
        txtName.clear();
        txtContactInformation.clear();
        txtDatepicker.setValue(null);
    }
}