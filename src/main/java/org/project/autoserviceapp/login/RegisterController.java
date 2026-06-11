package org.project.autoserviceapp.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.project.autoserviceapp.DatabaseConnection;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ResourceBundle;


public class RegisterController implements Initializable {

    @FXML
    private ImageView brandingImageView;

    @FXML
    private TextField nameField;

    @FXML
    private TextField familyField;

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
        InputStream is = getClass().getResourceAsStream("/images/loginMenu.png");
        Image brandingImage = new Image(is);
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
        if (usernameField.getText().isBlank() == true || passwordField.getText().isBlank() == true || phoneNumberField.getText().isBlank() == true || emailField.getText().isBlank() == true || nameField.getText().isBlank() == true || familyField.getText().isBlank() == true){
            confirmPasswordLabel.setText("Пожалуйста заполните все поля");
        } else if(!passwordField.getText().equals(confirmPasswordField.getText())){
            confirmPasswordLabel.setText("Пароли не совпадают");
        } else if (!emailField.getText().contains("@") || !emailField.getText().contains(".")) {
                confirmPasswordLabel.setText("Введите корректный email");
        } else if (passwordField.getText().length() < 4) {
            confirmPasswordLabel.setText("Пароль должен содержать не менее 4 символов");
        }else {
            registerUser();
        }
    }

    private void registerUser(){

        DatabaseConnection connectionNow = new DatabaseConnection();
        Connection connectDB = connectionNow.getConnection();

        String firstname = nameField.getText();
        String family = familyField.getText();
        String phoneNumber = phoneNumberField.getText();
        String email = emailField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();

        String insertFields = "INSERT INTO Client(client_name, client_family, client_login, client_password, client_phoneNumber, client_email) VALUES ('";
        String insertValues = firstname + "','" + family + "','" + username + "','" + password + "','" + phoneNumber + "','" + email + "')";
        String insertToRegister = insertFields + insertValues;

        try{
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(insertToRegister);
            ((Stage) usernameField.getScene().getWindow()).close();
            openClientLogin();
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }
}