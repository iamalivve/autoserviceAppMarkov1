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

public class LoginAdminController implements Initializable {

    @FXML
    private Label loginMessageLabel;
    @FXML
    private ImageView brandingImageView;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private Map<String, String> adminDatabase = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        adminDatabase.put("Бережной","11111");//данные админа
        File brandingFile = new File("images/loginMenu.png");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);
    }

    public void adminButtonLogin(ActionEvent event){
        if (adminDatabase.containsKey(usernameField.getText()) && adminDatabase.get(usernameField.getText()).equals(passwordField.getText())){
            validateAdminLogin();
        }else {
            loginMessageLabel.setText("Пожалуйста, введите корректные логин и пароль");
        }
    }

    public void actionBackAdmin(ActionEvent event){
        ((Stage) usernameField.getScene().getWindow()).close();
        openLoginWindow();
    }

    public void validateAdminLogin(){
        ((Stage) usernameField.getScene().getWindow()).close();
        openAdminWindow();
    }

    private void openAdminWindow() {
        try {
            String login = usernameField.getText();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-window.fxml"));
            Parent root = loader.load();

            AdminsWindowController controller = loader.getController();
            controller.setUserInfo(login);

            Stage stage = new Stage();
            stage.setTitle("Администратор");
            stage.setScene(new Scene(root, 550, 400));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

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

