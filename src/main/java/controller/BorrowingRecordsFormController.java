package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.BorrowingRecords;
import service.BOFactory;
import service.custom.BorrowingRecordsService;
import util.BOType;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
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

    private final BorrowingRecordsService borrowingService = BOFactory.getInstance().getBO(BOType.BORROWING_RECORDS);

    @FXML
    void btnAddOnAction(ActionEvent event) {
        if (validateFields()) {
            try {
                int calculatedFine = fine();
                BorrowingRecords record = new BorrowingRecords(
                        txtRecodeID.getText(), txtUserID.getText(), txtBookID.getText(),
                        txtBorrowDate.getValue(), txtReturnDate.getValue(), calculatedFine);
                boolean saved = borrowingService.addRecord(record);
                if (saved) {
                    showInfo("Added Successfully");
                    loadTable();
                }
            } catch (SQLException e) {
                showError("Error adding record: " + e.getMessage());
            }
            clearTextFields();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        if (txtRecodeID.getText().isEmpty()) {
            showWarning("Please enter a Record ID to delete.");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure to delete?");
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.get() == ButtonType.OK) {
            try {
                boolean deleted = borrowingService.deleteRecord(txtRecodeID.getText());
                if (deleted) {
                    showInfo("Deleted Successfully");
                    loadTable();
                } else {
                    showError("No record found with that ID.");
                }
            } catch (SQLException e) {
                showError("Error deleting record: " + e.getMessage());
            }
        }
        clearTextFields();
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String searchID = txtRecodeID.getText().trim();
        if (searchID.isEmpty()) {
            showWarning("Please enter an ID to search.");
            return;
        }
        try {
            BorrowingRecords record = borrowingService.getRecord(searchID);
            ObservableList<BorrowingRecords> list = FXCollections.observableArrayList();
            if (record != null) {
                txtRecodeID.setText(record.getRecordID());
                txtUserID.setText(record.getUserID());
                txtBookID.setText(record.getBookID());
                txtBorrowDate.setValue(record.getBorrowDate());
                txtReturnDate.setValue(record.getReturn_Date());
                txtFine.setText(String.valueOf(record.getFine()));
                list.add(record);
            } else {
                showWarning("No record found with this ID.");
            }
            tblBorrowingRecordsTable.setItems(list);
        } catch (SQLException e) {
            showError("Error searching record: " + e.getMessage());
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (txtRecodeID.getText().isEmpty()) {
            showWarning("Please enter an ID to update.");
            return;
        }
        if (!validateFields()) return;
        try {
            int calculatedFine = fine();
            BorrowingRecords record = new BorrowingRecords(
                    txtRecodeID.getText(), txtUserID.getText(), txtBookID.getText(),
                    txtBorrowDate.getValue(), txtReturnDate.getValue(), calculatedFine);
            boolean updated = borrowingService.updateRecord(record);
            if (updated) {
                showInfo("Updated Successfully!");
                loadTable();
                clearTextFields();
            } else {
                showError("No matching record found with that ID.");
            }
        } catch (SQLException e) {
            showError("Update failed: " + e.getMessage());
        }
    }

    @FXML
    void btnReloadOnAction(ActionEvent event) {
        loadTable();
    }

    @FXML
    void btnShowOverdueOnAction(ActionEvent event) {
        try {
            List<BorrowingRecords> overdueList = borrowingService.getOverdueBooks();
            tblBorrowingRecordsTable.setItems(FXCollections.observableArrayList(overdueList));
        } catch (SQLException e) {
            showError("Error loading overdue records: " + e.getMessage());
        }
    }

    @FXML
    void btnShowBorrowedOnAction(ActionEvent event) {
        try {
            List<BorrowingRecords> borrowedList = borrowingService.getBorrowedBooks();
            tblBorrowingRecordsTable.setItems(FXCollections.observableArrayList(borrowedList));
        } catch (SQLException e) {
            showError("Error loading borrowed records: " + e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTable();
        txtReturnDate.valueProperty().addListener((observableValue, oldDate, newDate) -> fine());
    }

    public void loadTable() {
        try {
            colRecordID.setCellValueFactory(new PropertyValueFactory<>("recordID"));
            colUserID.setCellValueFactory(new PropertyValueFactory<>("userID"));
            colBookID.setCellValueFactory(new PropertyValueFactory<>("bookID"));
            colBorrowDate.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
            colReturnDate.setCellValueFactory(new PropertyValueFactory<>("return_Date"));
            colFine.setCellValueFactory(new PropertyValueFactory<>("Fine"));

            List<BorrowingRecords> records = borrowingService.getAllRecords();
            tblBorrowingRecordsTable.setItems(FXCollections.observableArrayList(records));
        } catch (SQLException e) {
            showError("Error loading records: " + e.getMessage());
        }
    }

    public boolean validateFields() {
        if (txtRecodeID.getText().isEmpty() || txtUserID.getText().isEmpty() || txtBookID.getText().isEmpty()
                || txtBorrowDate.getValue() == null || txtReturnDate.getValue() == null) {
            showWarning("Please fill all the fields.");
            return false;
        }
        return true;
    }

    public void clearTextFields() {
        txtRecodeID.clear();
        txtBookID.clear();
        txtUserID.clear();
        txtBorrowDate.setValue(null);
        txtReturnDate.setValue(null);
        txtFine.clear();
    }

    public int fine() {
        int fineCharges = 0;
        if (txtBorrowDate.getValue() != null && txtReturnDate.getValue() != null) {
            long overdueDays = ChronoUnit.DAYS.between(txtReturnDate.getValue(), LocalDate.now());
            if (overdueDays > 0) {
                fineCharges = (int) overdueDays * 10;
            }
            txtFine.setText(String.valueOf(fineCharges));
        }
        return fineCharges;
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
