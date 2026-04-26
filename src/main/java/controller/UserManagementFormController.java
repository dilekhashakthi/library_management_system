package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.User;
import service.BOFactory;
import service.custom.UserService;
import util.BOType;

import java.net.URL;
import java.sql.SQLException;
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
    private TextField txtSearch;

    private final UserService userService = BOFactory.getInstance().getBO(BOType.USER);

    @FXML
    void btnAddOnAction(ActionEvent event) {
        if (validateFields()) {
            try {
                User user = new User(txtID.getText(), txtName.getText(),
                        txtContactInformation.getText(), txtDatepicker.getValue());
                boolean saved = userService.addUser(user);
                if (saved) {
                    showInfo("Added Successfully");
                    loadTable();
                }
            } catch (SQLException e) {
                showError("Error adding user: " + e.getMessage());
            }
            clearTextFields();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        if (txtID.getText().isEmpty()) {
            showWarning("Please enter an ID to delete.");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure to delete?");
        Optional<ButtonType> optionalButtonType = alert.showAndWait();

        if (optionalButtonType.get() == ButtonType.OK) {
            try {
                boolean deleted = userService.deleteUser(txtID.getText());
                if (deleted) {
                    showInfo("Deleted Successfully");
                    loadTable();
                } else {
                    showError("No user found with that ID.");
                }
            } catch (SQLException e) {
                showError("Error deleting user: " + e.getMessage());
            }
        }
        clearTextFields();
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String keyword = txtSearch != null ? txtSearch.getText().trim() : txtID.getText().trim();
        if (keyword.isEmpty()) {
            showWarning("Please enter a keyword to search.");
            return;
        }
        try {
            List<User> users = userService.searchUsers(keyword);
            ObservableList<User> list = FXCollections.observableArrayList(users);
            tblUserTable.setItems(list);
            if (!users.isEmpty()) {
                User u = users.get(0);
                txtID.setText(u.getId());
                txtName.setText(u.getName());
                txtContactInformation.setText(u.getContactInfomation());
                txtDatepicker.setValue(u.getMembershipDate());
            } else {
                showWarning("No user found.");
            }
        } catch (SQLException e) {
            showError("Error searching user: " + e.getMessage());
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        if (txtID.getText().isEmpty()) {
            showWarning("Please enter an ID to update.");
            return;
        }
        if (!validateFields()) return;
        try {
            User user = new User(txtID.getText(), txtName.getText(),
                    txtContactInformation.getText(), txtDatepicker.getValue());
            boolean updated = userService.updateUser(user);
            if (updated) {
                showInfo("Updated Successfully!");
                loadTable();
                clearTextFields();
            } else {
                showError("No matching user found with that ID.");
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
    }

    public void loadTable() {
        try {
            colID.setCellValueFactory(new PropertyValueFactory<>("id"));
            colName.setCellValueFactory(new PropertyValueFactory<>("name"));
            colContactInformation.setCellValueFactory(new PropertyValueFactory<>("contactInfomation"));
            colMembershipDate.setCellValueFactory(new PropertyValueFactory<>("membershipDate"));

            List<User> users = userService.getAllUsers();
            ObservableList<User> userObservableList = FXCollections.observableArrayList(users);
            tblUserTable.setItems(userObservableList);
        } catch (SQLException e) {
            showError("Error loading users: " + e.getMessage());
        }
    }

    public boolean validateFields() {
        if (txtID.getText().isEmpty() || txtName.getText().isEmpty()
                || txtContactInformation.getText().isEmpty() || txtDatepicker.getValue() == null) {
            showWarning("Please fill all the fields.");
            return false;
        }
        return true;
    }

    public void clearTextFields() {
        txtID.clear();
        txtName.clear();
        txtContactInformation.clear();
        txtDatepicker.setValue(null);
        if (txtSearch != null) txtSearch.clear();
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
