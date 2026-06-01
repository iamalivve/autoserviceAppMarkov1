package org.project.autoserviceapp.login;

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
import org.project.autoserviceapp.DatabaseConnection;
import org.project.autoserviceapp.admin.AdminHomeController;
import org.project.autoserviceapp.admin.SceneNavigator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        File brandingFile = new File("images/loginMenu.png");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);
    }

    public void actionBackAdmin(ActionEvent event){
        ((Stage) usernameField.getScene().getWindow()).close();
        openLoginWindow();
    }

    public void adminButtonLogin(ActionEvent event){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String verifyLogin = "SELECT count(1) FROM Admin WHERE admin_login = '" + usernameField.getText() + "' AND admin_password = '" + passwordField.getText() + "'";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while (queryResult.next()){
                if (queryResult.getInt(1) == 1){
                    ((Stage) usernameField.getScene().getWindow()).close();
                    openAdminHome();
                }else {
                    loginMessageLabel.setText("Неверный логин или пароль");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    private void openAdminHome() {
        try {
            String login = usernameField.getText();
            Stage stage = (Stage) usernameField.getScene().getWindow();

            SceneNavigator.setAdminName(login);
            SceneNavigator.goToHome(stage);

        } catch (Exception e) {
            e.printStackTrace();
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