package org.project.autoserviceapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ResourceBundle;

public class AdminsWindowController{

    @FXML
    private TextField nameField;

    public void setUserInfo(String usernameField) {
        nameField.setText(usernameField);
    }

    public void actionAdminSecondWindow(ActionEvent event) {
        openAdminSecondWindow();
    }

    public void actionBackButton(ActionEvent event) {

    }

    public void actionForwardButton(ActionEvent event) {

    }

    public void actionExitButton(ActionEvent event) {
        ((Stage) nameField.getScene().getWindow()).close();
        openLoginController();
    }

    public void openAdminSecondWindow() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("second-admin-window.fxml"));
            Stage stage = new Stage();
            stage.setTitle("База данных");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void openLoginController() {
        try {
           Parent root = FXMLLoader.load(getClass().getResource("autoservice_login.fxml"));
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