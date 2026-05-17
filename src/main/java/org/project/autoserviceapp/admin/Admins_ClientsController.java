package org.project.autoserviceapp.admin;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class Admins_ClientsController {

    @FXML
    private TextField nameField;

    public void setUserInfo(String usernameField) {}

    public void actionBackButton(ActionEvent event) {

    }

    public void actionForwardButton(ActionEvent event) {

    }

    public void actionExitButton(ActionEvent event) {
        ((Stage) nameField.getScene().getWindow()).close();
        openLoginController();
    }

    public void openLoginController() {
        try {
           Parent root = FXMLLoader.load(getClass().getResource("/org/project/autoserviceapp/login/autoservice_login.fxml"));
            Stage stage = new Stage();
            stage.setTitle("AVTO67");
            stage.setScene(new Scene(root, 550, 400));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}