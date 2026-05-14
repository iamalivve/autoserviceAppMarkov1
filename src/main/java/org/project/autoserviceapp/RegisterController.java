package org.project.autoserviceapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ResourceBundle;


public class RegisterController implements Initializable {

    @FXML
    private ImageView brandingImageView;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private TextField emailField;

    @FXML
    private Label confirmPasswordLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        File brandingFile = new File("images/loginMenu.png");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);
    }

    public void actionBackRegister(ActionEvent event) {
        ((Stage) usernameField.getScene().getWindow()).close();
        openClientLogin();
    }

    private void openClientLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("autoservice_loginClient.fxml"));
            Stage registrerStage = new Stage();
            registrerStage.setTitle("Вход");
            registrerStage.setScene(new Scene(root, 550, 400));
            registrerStage.show();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    public void registerButtonOnAction(ActionEvent e){
        if (usernameField.getText().isBlank() == true || passwordField.getText().isBlank() == true || phoneNumberField.getText().isBlank() == true || emailField.getText().isBlank() == true){
            confirmPasswordLabel.setText("Пожалуйста заполните все поля");
        }else {
            registerUser();
        }
    }

    private void registerUser(){
        if (passwordField.getText().equals(confirmPasswordField.getText())){
            //confirmPasswordLabel.setText("");
        }else {
            confirmPasswordLabel.setText("Пароли не совпадают");
        }
    }
}