package controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Book;
import service.BOFactory;
import service.custom.BookService;
import util.BOType;

import java.net.URL;
import java.sql.SQLException;
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
    private TextField txtSearch;

    private final BookService bookService = BOFactory.getInstance().getBO(BOType.BOOK);

    @FXML
    void btnAddOnAction(ActionEvent event) {
        if (validateFields()) {
            try {
                Book book = new Book(txtbookID.getText(), txtISBN.getText(), txtTitel.getText(),
                        txtAuthor.getText(), txtGenre.getText(), txtAvailability.getValue().toString());
                boolean saved = bookService.addBook(book);
                if (saved) {
                    showInfo("Added Successfully");
                    loadTable();
                }
            } catch (SQLException e) {
                showError("Error adding book: " + e.getMessage());
            }
            clearTextFields();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        if (txtbookID.getText().isEmpty()) {
            showWarning("Please enter a Book ID to delete.");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure to delete?");
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.get() == ButtonType.OK) {
            try {
                boolean deleted = bookService.deleteBook(txtbookID.getText());
                if (deleted) {
                    showInfo("Deleted Successfully");
                    loadTable();
                } else {
                    showError("No book found with that ID.");
                }
            } catch (SQLException e) {
                showError("Error deleting book: " + e.getMessage());
            }
        }
        clearTextFields();
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String keyword = txtSearch != null ? txtSearch.getText().trim() : txtbookID.getText().trim();
        if (keyword.isEmpty()) {
            showWarning("Please enter a keyword to search.");
            return;
        }
        try {
            List<Book> books = bookService.searchBooks(keyword);
            ObservableList<Book> list = FXCollections.observableArrayList(books);
            tblBookTable.setItems(list);
            if (!books.isEmpty()) {
                Book b = books.get(0);
                txtbookID.setText(b.getBookID());
                txtISBN.setText(b.getISBN());
                txtTitel.setText(b.getTitle());
                txtAuthor.setText(b.getAuthor());
                txtGenre.setText(b.getGenre());
                txtAvailability.setValue(b.getAvailabilityStatus());
            } else {
                showWarning("No book found.");
            }
        } catch (SQLException e) {
            showError("Error searching book: " + e.getMessage());
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (txtbookID.getText().isEmpty()) {
            showWarning("Please enter a Book ID to update.");
            return;
        }
        if (!validateFields()) return;
        try {
            Book book = new Book(txtbookID.getText(), txtISBN.getText(), txtTitel.getText(),
                    txtAuthor.getText(), txtGenre.getText(), txtAvailability.getValue().toString());
            boolean updated = bookService.updateBook(book);
            if (updated) {
                showInfo("Updated Successfully!");
                loadTable();
                clearTextFields();
            } else {
                showError("No matching book found with that ID.");
            }
        } catch (SQLException e) {
            showError("Update failed: " + e.getMessage());
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
            colbookID.setCellValueFactory(new PropertyValueFactory<>("bookID"));
            colISBN.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
            colTitel.setCellValueFactory(new PropertyValueFactory<>("title"));
            colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
            colGenre.setCellValueFactory(new PropertyValueFactory<>("genre"));
            colAvailability.setCellValueFactory(new PropertyValueFactory<>("availabilityStatus"));

            List<Book> books = bookService.getAllBooks();
            ObservableList<Book> bookObservableList = FXCollections.observableArrayList(books);
            tblBookTable.setItems(bookObservableList);
        } catch (SQLException e) {
            showError("Error loading books: " + e.getMessage());
        }
    }

    public boolean validateFields() {
        if (txtbookID.getText().isEmpty() || txtISBN.getText().isEmpty() ||
                txtTitel.getText().isEmpty() || txtAuthor.getText().isEmpty() ||
                txtGenre.getText().isEmpty() || txtAvailability.getValue() == null) {
            showWarning("Please fill all the fields.");
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
        if (txtSearch != null) txtSearch.clear();
    }

    public void comboBox() {
        ObservableList<String> comboboxList = FXCollections.observableArrayList();
        comboboxList.add("Available");
        comboboxList.add("Checked Out");
        txtAvailability.setItems(comboboxList);
    }

    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showWarning(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
