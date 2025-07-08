package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;

public class Dashboard {
    // This is the main content area where other forms will be loaded
    public AnchorPane loadFormContent;

    @FXML
    void btnBookManagmentonAction(ActionEvent event) throws IOException {
        // Load the BookManagement.fxml file
        URL resource = this.getClass().getResource("/view/BookManagement.fxml");

        // Make sure the FXML file was found
        assert resource != null;

        // Load the content from the FXML file
        Parent load = FXMLLoader.load(resource);

        // Clear anything already in the content area
        loadFormContent.getChildren().clear();

        // Add the new content to the main pane
        loadFormContent.getChildren().add(load);
    }

    @FXML
    void btnBorrowingRecodesOnAction(ActionEvent event) throws IOException {
        // Load the BorrowingRecords.fxml file
        URL resource = this.getClass().getResource("/view/BorrowingRecords.fxml");

        // Make sure the FXML file was found
        assert resource != null;

        // Load the content
        Parent load = FXMLLoader.load(resource);

        // Clear anything already in the content area
        loadFormContent.getChildren().clear();

        // Add the new content to the main pane
        loadFormContent.getChildren().add(load);
    }

    @FXML
    void btnusermanagementOnAction(ActionEvent event) throws IOException {
        // Load the UserManagement.fxml file
        URL resource = this.getClass().getResource("/view/UserManagement.fxml");

        // Make sure the FXML file was found
        assert resource != null;

        // Load the content
        Parent load = FXMLLoader.load(resource);

        // Clear anything already in the content area
        loadFormContent.getChildren().clear();

        // Add the new content to the main pane
        loadFormContent.getChildren().add(load);
    }
}
