package controller;

import com.jfoenix.controls.JFXComboBox;
import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Book;
import model.User;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class BookManagementFormController implements Initializable {


    @FXML
    private TableColumn colAuthor;

    @FXML
    private TableColumn colAvailability;

    @FXML
    private TableColumn colGenre;

    @FXML
    private TableColumn colISBN;

    @FXML
    private TableColumn colTitel;

    @FXML
    private TableColumn colbookID;

    @FXML
    private TableView tblBookTable;

    @FXML
    private TextField txtAuthor;

    @FXML
    private JFXComboBox txtAvailability;

    @FXML
    private TextField txtGenre;

    @FXML
    private TextField txtISBN;

    @FXML
    private TextField txtTitel;

    @FXML
    private TextField txtbookID;

    @FXML
    void btnAddOnAction(ActionEvent event) {
        if (validateFields()) {
            try {
                String SQL = "INSERT INTO library_books (bookID,ISBN,title,author,genre,availabilityStatus) VALUES (?,?,?,?,?,?)";
                Connection connection = DBConnection.getInstance().getConnection();

                PreparedStatement pstm = connection.prepareStatement(SQL);
                pstm.setString(1, txtbookID.getText());
                pstm.setString(2, txtISBN.getText());
                pstm.setString(3, txtTitel.getText());
                pstm.setString(4, txtAuthor.getText());
                pstm.setString(5, txtGenre.getText());
                pstm.setString(6, txtAvailability.getValue().toString());
                pstm.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Added Successfully");
                alert.showAndWait();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            clearTextFields();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure to delete?");
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.get() == ButtonType.OK) {
            String SQL = "DELETE FROM library_books WHERE " +
                    "bookID = ? AND " +
                    "ISBN = ? AND " +
                    "title = ? AND " +
                    "author = ? AND " +
                    "genre = ? AND " +
                    "availabilityStatus = ?" ;
            try {
                Connection connection = DBConnection.getInstance().getConnection();

                PreparedStatement pstm = connection.prepareStatement(SQL);
                pstm.setString(1, txtbookID.getText());
                pstm.setString(2, txtISBN.getText());
                pstm.setString(3, txtTitel.getText());
                pstm.setString(4, txtAuthor.getText());
                pstm.setString(5, txtGenre.getText());
                pstm.setString(6, txtAvailability.getValue().toString());

                pstm.executeUpdate();

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        //clear text field after adding data
        clearTextFields();
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String searchID = txtbookID.getText().trim();
        if (searchID.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter an ID to search.").show();
        }

        try {
            Connection connection = DBConnection.getInstance().getConnection();
            String SQL = "SELECT * FROM library_books WHERE bookID = ?";
            PreparedStatement pstm = connection.prepareStatement(SQL);
            pstm.setString(1, searchID);
            ResultSet resultSet = pstm.executeQuery();

            ObservableList<Book> observableUserList = FXCollections.observableArrayList();

            if (resultSet.next()) {
                // Fill text fields
                txtbookID.setText(resultSet.getString("bookID"));
                txtISBN.setText(resultSet.getString("ISBN"));
                txtTitel.setText(resultSet.getString("title"));
                txtAuthor.setText(resultSet.getString("author"));
                txtGenre.setText(resultSet.getString("genre"));
                txtAvailability.setValue(resultSet.getString("availabilityStatus"));


                // Add to TableView
                observableUserList.add(new Book(
                        resultSet.getString("bookID"),
                        resultSet.getString("ISBN"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("genre"),
                        resultSet.getString("availabilityStatus")
                ));
            } else {
                new Alert(Alert.AlertType.WARNING, "No user found with this ID.").show();
            }
            tblBookTable.setItems(observableUserList);

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Error searching user: " + e.getMessage()).show();
            e.printStackTrace();
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (txtbookID.getText().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter an ID to update.").show();
            return;
        }

        try {
            String SQL = "UPDATE library_books SET bookID = ?, ISBN = ?, title = ?, author = ?, genre = ?, availabilityStatus = ? WHERE bookID = ?";
            Connection connection = DBConnection.getInstance().getConnection();

            PreparedStatement pstm = connection.prepareStatement(SQL);
            pstm.setString(1, txtbookID.getText());
            pstm.setString(2, txtISBN.getText());
            pstm.setString(3, txtTitel.getText());
            pstm.setString(4, txtAuthor.getText());
            pstm.setString(5, txtGenre.getText());
            pstm.setString(6, txtAvailability.getValue().toString());

            pstm.setString(7, txtbookID.getText());

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
        comboBox();
    }

    public void loadTable() {
        try {
            // Bind table columns to Book properties
            colbookID.setCellValueFactory(new PropertyValueFactory<>("bookID"));
            colISBN.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
            colTitel.setCellValueFactory(new PropertyValueFactory<>("title"));
            colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
            colGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
            colAvailability.setCellValueFactory(new PropertyValueFactory<>("availabilityStatus"));

            // Use ONE list
            ObservableList<Book> bookObservableList = FXCollections.observableArrayList();

            // DB connection and data load
            Connection connection = DBConnection.getInstance().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM library_books");

            // Load result set into observable list
            while (resultSet.next()) {
                bookObservableList.add(new Book(
                        resultSet.getString("bookID"),
                        resultSet.getString("ISBN"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("genre"),
                        resultSet.getString("availabilityStatus")
                ));
            }

            // Set the data to the table
            tblBookTable.setItems(bookObservableList);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean validateFields() {
        if (txtbookID.getText().isEmpty() | txtISBN.getText().isEmpty() |
                txtTitel.getText().isEmpty() | txtAuthor.getText().isEmpty() |
                txtGenre.getText().isEmpty() | txtAvailability.getValue()==null) {
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
        txtbookID.clear();
        txtISBN.clear();
        txtTitel.clear();
        txtAuthor.clear();
        txtGenre.clear();
        txtAvailability.setValue(null);
    }

    public void comboBox(){
        ObservableList<String> comboboxList = FXCollections.observableArrayList();
        comboboxList.add("Available");
        comboboxList.add("Checked Out");
        txtAvailability.setItems(comboboxList);
    }
}