package org.project.autoserviceapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable{

    @FXML
    private ImageView ImageViewLogin;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        File brandingFile = new File("images/logo.png");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        ImageViewLogin.setImage(brandingImage);
    }

    public void actionAdminLogin(ActionEvent event) {
        ((Stage) ImageViewLogin.getScene().getWindow()).close();
        openAdminLogin();
    }

    public void actionClientLogin(ActionEvent event) {
        ((Stage) ImageViewLogin.getScene().getWindow()).close();
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

    private void openAdminLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("autoservice_loginAdmin.fxml"));
            Stage registrerStage = new Stage();
            registrerStage.setTitle("Вход");
            registrerStage.setScene(new Scene(root, 600, 400));
            registrerStage.show();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }
}
