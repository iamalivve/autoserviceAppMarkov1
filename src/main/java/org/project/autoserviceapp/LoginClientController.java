package org.project.autoserviceapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.util.HashMap;
import java.util.Map;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginClientController implements Initializable {

    @FXML
    private Label loginMessageLabel;
    @FXML
    private ImageView brandingImageView;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private Map<String, String> userDatabase = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        userDatabase.put("Марков", "12345");//данные пользователя
        File brandingFile = new File("images/loginMenu.png");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);
    }

    public void loginButtonOnAction(ActionEvent event){
        if (userDatabase.containsKey(usernameField.getText()) && userDatabase.get(usernameField.getText()).equals(passwordField.getText())){
            validateLogin();
        }else {
            loginMessageLabel.setText("Пожалуйста, введите корректные логин и пароль");
        }
    }

    public void actionBackClient(ActionEvent event) {
        ((Stage) usernameField.getScene().getWindow()).close();
        openLoginWindow();
    }

    public void validateLogin(){
        ((Stage) usernameField.getScene().getWindow()).close();
        openClientWindow();
    }

    public void createAccountForm(){

        try {
            Parent root = FXMLLoader.load(getClass().getResource("autoservice_register.fxml"));
            Stage registrerStage = new Stage();
            registrerStage.setTitle("AVTO67");
            registrerStage.setScene(new Scene(root, 550, 400));
            registrerStage.show();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    private void openClientWindow(){}

    private void openLoginWindow() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("autoservice_login.fxml"));
            Stage stage = new Stage();
            stage.setTitle("AVTO67");
            stage.setScene(new Scene(root, 600, 400));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}