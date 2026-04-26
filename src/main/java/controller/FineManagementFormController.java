package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Fine;
import service.BOFactory;
import service.custom.FineService;
import util.BOType;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class FineManagementFormController implements Initializable {

    @FXML
    private TableColumn colFineID;

    @FXML
    private TableColumn colRecordID;

    @FXML
    private TableColumn colUserID;

    @FXML
    private TableColumn colFineAmount;

    @FXML
    private TableColumn colStatus;

    @FXML
    private TableColumn colPaidDate;

    @FXML
    private TableView tblFineTable;

    @FXML
    private TextField txtFineID;

    @FXML
    private TextField txtRecordID;

    @FXML
    private TextField txtUserID;

    @FXML
    private TextField txtFineAmount;

    @FXML
    private TextField txtStatus;

    private final FineService fineService = BOFactory.getInstance().getBO(BOType.FINE);

    @FXML
    void btnAddOnAction(ActionEvent event) {
        if (validateFields()) {
            try {
                Fine fine = new Fine(txtFineID.getText(), txtRecordID.getText(), txtUserID.getText(),
                        Integer.parseInt(txtFineAmount.getText()), "Unpaid", null);
                boolean saved = fineService.addFine(fine);
                if (saved) {
                    showInfo("Fine added successfully.");
                    loadTable();
                }
            } catch (NumberFormatException e) {
                showError("Fine amount must be a number.");
            } catch (SQLException e) {
                showError("Error adding fine: " + e.getMessage());
            }
            clearTextFields();
        }
    }

    @FXML
    void btnPayFineOnAction(ActionEvent event) {
        String fineID = txtFineID.getText().trim();
        if (fineID.isEmpty()) {
            showWarning("Please enter a Fine ID to mark as paid.");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Payment");
        alert.setHeaderText(null);
        alert.setContentText("Mark fine as paid?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            try {
                boolean paid = fineService.payFine(fineID);
                if (paid) {
                    showInfo("Fine marked as paid.");
                    loadTable();
                } else {
                    showError("Could not find fine with that ID.");
                }
            } catch (SQLException e) {
                showError("Error processing payment: " + e.getMessage());
            }
            clearTextFields();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        if (txtFineID.getText().isEmpty()) {
            showWarning("Please enter a Fine ID to delete.");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure to delete?");
        Optional<ButtonType> optionalButtonType = alert.showAndWait();
        if (optionalButtonType.get() == ButtonType.OK) {
            try {
                boolean deleted = fineService.deleteFine(txtFineID.getText());
                if (deleted) {
                    showInfo("Deleted Successfully");
                    loadTable();
                } else {
                    showError("No fine found with that ID.");
                }
            } catch (SQLException e) {
                showError("Error deleting fine: " + e.getMessage());
            }
        }
        clearTextFields();
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String fineID = txtFineID.getText().trim();
        if (fineID.isEmpty()) {
            showWarning("Please enter a Fine ID to search.");
            return;
        }
        try {
            Fine fine = fineService.getFine(fineID);
            ObservableList<Fine> list = FXCollections.observableArrayList();
            if (fine != null) {
                txtFineID.setText(fine.getFineID());
                txtRecordID.setText(fine.getRecordID());
                txtUserID.setText(fine.getUserID());
                txtFineAmount.setText(String.valueOf(fine.getFineAmount()));
                txtStatus.setText(fine.getStatus());
                list.add(fine);
            } else {
                showWarning("No fine found with this ID.");
            }
            tblFineTable.setItems(list);
        } catch (SQLException e) {
            showError("Error searching fine: " + e.getMessage());
        }
    }

    @FXML
    void btnShowUnpaidOnAction(ActionEvent event) {
        try {
            List<Fine> unpaid = fineService.getUnpaidFines();
            tblFineTable.setItems(FXCollections.observableArrayList(unpaid));
        } catch (SQLException e) {
            showError("Error loading unpaid fines: " + e.getMessage());
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
            colFineID.setCellValueFactory(new PropertyValueFactory<>("fineID"));
            colRecordID.setCellValueFactory(new PropertyValueFactory<>("recordID"));
            colUserID.setCellValueFactory(new PropertyValueFactory<>("userID"));
            colFineAmount.setCellValueFactory(new PropertyValueFactory<>("fineAmount"));
            colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
            colPaidDate.setCellValueFactory(new PropertyValueFactory<>("paidDate"));

            List<Fine> fines = fineService.getAllFines();
            tblFineTable.setItems(FXCollections.observableArrayList(fines));
        } catch (SQLException e) {
            showError("Error loading fines: " + e.getMessage());
        }
    }

    public boolean validateFields() {
        if (txtFineID.getText().isEmpty() || txtRecordID.getText().isEmpty() ||
                txtUserID.getText().isEmpty() || txtFineAmount.getText().isEmpty()) {
            showWarning("Please fill all the fields.");
            return false;
        }
        return true;
    }

    public void clearTextFields() {
        txtFineID.clear();
        txtRecordID.clear();
        txtUserID.clear();
        txtFineAmount.clear();
        txtStatus.clear();
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
