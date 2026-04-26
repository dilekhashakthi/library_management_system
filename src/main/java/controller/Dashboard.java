package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;

public class Dashboard {

    public AnchorPane loadFormContent;

    @FXML
    void btnBookManagmentonAction(ActionEvent event) throws IOException {
        URL resource = this.getClass().getResource("/view/BookManagement.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        loadFormContent.getChildren().clear();
        loadFormContent.getChildren().add(load);
    }

    @FXML
    void btnBorrowingRecodesOnAction(ActionEvent event) throws IOException {
        URL resource = this.getClass().getResource("/view/BorrowingRecords.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        loadFormContent.getChildren().clear();
        loadFormContent.getChildren().add(load);
    }

    @FXML
    void btnusermanagementOnAction(ActionEvent event) throws IOException {
        URL resource = this.getClass().getResource("/view/UserManagement.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        loadFormContent.getChildren().clear();
        loadFormContent.getChildren().add(load);
    }

    @FXML
    void btnFineManagementOnAction(ActionEvent event) throws IOException {
        URL resource = this.getClass().getResource("/view/FineManagement.fxml");
        assert resource != null;
        Parent load = FXMLLoader.load(resource);
        loadFormContent.getChildren().clear();
        loadFormContent.getChildren().add(load);
    }
}
