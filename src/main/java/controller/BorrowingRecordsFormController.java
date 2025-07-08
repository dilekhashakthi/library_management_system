package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.BorrowingRecords;

import java.net.URL;
import java.sql.*;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.ResourceBundle;

public class BorrowingRecordsFormController implements Initializable {

    @FXML
    private TableColumn colBookID;

    @FXML
    private TableColumn colBorrowDate;

    @FXML
    private TableColumn colFine;

    @FXML
    private TableColumn colRecordID;

    @FXML
    private TableColumn colReturnDate;

    @FXML
    private TableColumn colUserID;

    @FXML
    private TableView tblBorrowingRecordsTable;

    @FXML
    private TextField txtBookID;

    @FXML
    private DatePicker txtBorrowDate;

    @FXML
    private TextField txtFine;

    @FXML
    private TextField txtRecodeID;

    @FXML
    private TextField txtUserID;

    @FXML
    private DatePicker txtReturnDate;

    @FXML
    void btnAddOnAction(ActionEvent event) {
        // Check if all input fields are filled correctly
        if (validateFields()) {
            try {
                // SQL command to insert data into the table
                String SQL = "INSERT INTO library_borrow_records (RecordID, UserID, BookID, BorrowDate, ReturnDate, Fine) VALUES (?,?,?,?,?,?)";

                // Get connection to the database
                Connection connection = DBConnection.getInstance().getConnection();

                // Set the fine value in the text box
                txtFine.setText(String.valueOf(fine()));

                // Prepare the SQL command
                PreparedStatement pstm = connection.prepareStatement(SQL);

                // Add values to the SQL command
                pstm.setString(1, txtRecodeID.getText()); // Add Record ID
                pstm.setString(2, txtUserID.getText()); // Add User ID
                pstm.setString(3, txtBookID.getText()); // Add Book ID
                pstm.setDate(4, java.sql.Date.valueOf(txtBorrowDate.getValue())); // Add Borrow Date
                pstm.setDate(5, java.sql.Date.valueOf(txtReturnDate.getValue())); // Add Return Date
                pstm.setInt(6, fine()); // Add Fine

                // Run the SQL command
                pstm.executeUpdate();

            } catch (SQLException e) {

                // If error happens, show the error
                throw new RuntimeException(e);
            }
            // Show a message that data was added successfully
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText(null);
            alert.setContentText("Added Successfully");
            alert.showAndWait();
        }
        // Clear all text fields after saving
        clearTextFields();
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        // Show a confirmation message to the user
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure to delete?");

        // Wait for the user's answer (OK or Cancel)
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        // If user clicks OK, then delete the record
        if (optionalButtonType.get() == ButtonType.OK) {

            // SQL command to delete a record from the table
            String SQL = "DELETE FROM library_borrow_records WHERE RecordID = ? AND UserID = ? AND BookID = ? AND BorrowDate = ? AND ReturnDate = ? AND Fine = ?";
            try {
                // Get the database connection
                Connection connection = DBConnection.getInstance().getConnection();

                // Prepare the SQL delete command
                PreparedStatement pstm = connection.prepareStatement(SQL);
                pstm.setString(1, txtRecodeID.getText()); // Record ID
                pstm.setString(2, txtUserID.getText()); // User ID
                pstm.setString(3, txtBookID.getText()); // Book ID
                pstm.setDate(4, java.sql.Date.valueOf(txtBorrowDate.getValue())); // Borrow Date
                pstm.setDate(5, java.sql.Date.valueOf(txtReturnDate.getValue())); // Return Date
                pstm.setString(6, txtFine.getText()); // Fine

                // Run the SQL command to delete the record
                pstm.executeUpdate();

            } catch (SQLException e) {
                // Show error if something goes wrong
                throw new RuntimeException(e);
            }
        }
        // Clear all text fields after deletion
        clearTextFields();
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        // Get the Record ID entered by the user
        String searchID = txtRecodeID.getText().trim();

        // If the ID field is empty, show a warning message
        if (searchID.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter an ID to search.").show();
        }

        try {
            // Get connection to the database
            Connection connection = DBConnection.getInstance().getConnection();

            // SQL query to search by RecordID
            String SQL = "SELECT * FROM library_borrow_records WHERE RecordID = ?";

            // Prepare the SQL statement
            PreparedStatement pstm = connection.prepareStatement(SQL);
            pstm.setString(1, searchID);

            // Run the query
            ResultSet resultSet = pstm.executeQuery();

            // Create a list to store search results
            ObservableList<BorrowingRecords> borrowingRecordsObservableList = FXCollections.observableArrayList();

            // If a matching record is found
            if (resultSet.next()) {

                // Fill the input fields with data from the record
                txtRecodeID.setText(resultSet.getString("RecordID"));
                txtUserID.setText(resultSet.getString("UserID"));
                txtBookID.setText(resultSet.getString("BookID"));
                txtBorrowDate.setValue(resultSet.getDate("BorrowDate").toLocalDate());
                txtReturnDate.setValue(resultSet.getDate("ReturnDate").toLocalDate());
                txtFine.setText(resultSet.getString("Fine"));

                // Add the record to the table list
                borrowingRecordsObservableList.add(new BorrowingRecords(
                        resultSet.getString("RecordID"),
                        resultSet.getString("UserID"),
                        resultSet.getString("BookID"),
                        resultSet.getDate("BorrowDate").toLocalDate(),
                        resultSet.getDate("ReturnDate").toLocalDate(),
                        resultSet.getInt("Fine")
                ));
            } else {
                // If no record is found, show a warning
                new Alert(Alert.AlertType.WARNING, "No user found with this ID.").show();
            }

            // Show the found record(s) in the table
            tblBorrowingRecordsTable.setItems(borrowingRecordsObservableList);

        } catch (Exception e) {
            // Show an error alert if something goes wrong
            new Alert(Alert.AlertType.ERROR, "Error searching user: " + e.getMessage()).show();
            e.printStackTrace(); // Print error for debugging
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        // Check if Record ID field is empty
        if (txtRecodeID.getText().isEmpty()) {
            // Show warning if no ID is entered
            new Alert(Alert.AlertType.WARNING, "Please enter an ID to update.").show();
            return; // Stop the update process
        }

        try {
            // SQL command to update a record in the table
            String SQL = "UPDATE library_borrow_records SET RecordID = ?, UserID = ?, BookID = ?, BorrowDate = ?, ReturnDate = ?, Fine = ? WHERE RecordID = ?";

            // Get database connection
            Connection connection = DBConnection.getInstance().getConnection();

            // Prepare the SQL statement
            PreparedStatement pstm = connection.prepareStatement(SQL);

            // Set new values for the record
            pstm.setString(1, txtRecodeID.getText()); // New Record ID
            pstm.setString(2, txtUserID.getText()); // New User ID
            pstm.setString(3, txtBookID.getText()); // New Book ID
            pstm.setDate(4, java.sql.Date.valueOf(txtBorrowDate.getValue())); // New Borrow Date
            pstm.setDate(5, java.sql.Date.valueOf(txtReturnDate.getValue())); // New Borrow Date
            pstm.setInt(6, Integer.parseInt(txtFine.getText())); // New Fine

            // Set condition to match the existing Record ID for updating
            pstm.setString(7, txtRecodeID.getText());

            // Execute the update command
            int updateCount = pstm.executeUpdate();

            // If update was successful (1 or more rows affected)
            if (updateCount > 0) {
                // Show success message
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Update Successfully..!");
                alert.showAndWait();

                // Clear all input fields
                clearTextFields();

            } else {
                // Show error if no matching record was found
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText(null);
                alert.setContentText("No matching user found with that ID.");
                alert.showAndWait();
            }

        } catch (Exception e) {
            // Show error message if something goes wrong
            new Alert(Alert.AlertType.ERROR, "Update failed: " + e.getMessage()).show();
            e.printStackTrace(); // Print error for debugging
        }
    }

    @FXML
    void btnReloadOnAction(ActionEvent event) {
        // Reload the table with all data from the database
        loadTable();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load and show all data in the table when the screen opens
        loadTable();

        // When the return date is changed, recalculate the fine
        txtReturnDate.valueProperty().addListener((observableValue, oldDate, newDate) -> fine());
    }

    public void loadTable() {
        try {
            // Link table columns with the properties of the BorrowingRecords class
            colRecordID.setCellValueFactory(new PropertyValueFactory<>("recordID"));
            colUserID.setCellValueFactory(new PropertyValueFactory<>("userID"));
            colBookID.setCellValueFactory(new PropertyValueFactory<>("bookID"));
            colBorrowDate.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
            colReturnDate.setCellValueFactory(new PropertyValueFactory<>("return_Date"));
            colFine.setCellValueFactory(new PropertyValueFactory<>("Fine"));

            // Create a list to hold all borrowing records
            ObservableList<BorrowingRecords> borrowingRecordsObservableList = FXCollections.observableArrayList();

            // Connect to the database
            Connection connection = DBConnection.getInstance().getConnection();

            // Create a SQL statement to get all records
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM library_borrow_records");

            // Read each record from the result set and add to the list
            while (resultSet.next()) {
                borrowingRecordsObservableList.add(new BorrowingRecords(
                        resultSet.getString(1), // Record ID
                        resultSet.getString(2), // User ID
                        resultSet.getString(3), // Book ID
                        resultSet.getDate(4).toLocalDate(), // Borrow Date
                        resultSet.getDate(5).toLocalDate(), // Return Date
                        resultSet.getInt(6) // Fine
                ));
            }

            // Show all records in the TableView
            tblBorrowingRecordsTable.setItems(borrowingRecordsObservableList);

        } catch (SQLException e) {
            // If something goes wrong, throw an error
            throw new RuntimeException(e);
        }
    }

    public boolean validateFields() {
        // Check if any input field is empty or date is not selected
        if (txtRecodeID.getText().isEmpty() | txtUserID.getText().isEmpty() | txtBookID.getText().isEmpty()
                | txtBorrowDate.getValue() == null | txtReturnDate.getValue() == null | txtFine.getText().isEmpty()) {

            // Show a warning alert if any field is missing
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Validate Fields");
            alert.setHeaderText(null);
            alert.setContentText("Please Enter Into The Fields");
            alert.showAndWait();

            return false; // Validation failed
        }
        return true; // All fields are filled
    }

    public void clearTextFields() {
        // Clear all text fields and date pickers
        txtRecodeID.clear();
        txtBookID.clear();
        txtUserID.clear();
        txtBorrowDate.setValue(null);
        txtReturnDate.setValue(null);
        txtFine.clear();
    }

    public int fine() {
        int fineCharges = 0;

        // Calculate fine only if both borrow and return dates are selected
        if (txtBorrowDate.getValue() != null && txtReturnDate.getValue() != null) {

            // Calculate days between borrow date and return date
            long fineDays = ChronoUnit.DAYS.between(txtBorrowDate.getValue(), txtReturnDate.getValue());

            // Fine is 5 currency units per day
            fineCharges = (int) fineDays * 5;

            // Display fine in the txtFine field
            txtFine.setText(String.valueOf(fineCharges));
        }
        return fineCharges; // Return the calculated fine
    }
}