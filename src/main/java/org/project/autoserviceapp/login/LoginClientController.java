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
import org.project.autoserviceapp.client.ClientController;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        InputStream is = getClass().getResourceAsStream("/images/loginMenu.png");
        Image brandingImage = new Image(is);
        brandingImageView.setImage(brandingImage);
    }

    public void actionBackClient(ActionEvent event) {
        ((Stage) usernameField.getScene().getWindow()).close();
        openLoginWindow();
    }

    public void loginButtonOnAction(ActionEvent event){

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String verifyLogin = "SELECT client_name, client_family FROM Client WHERE client_login = '" + usernameField.getText() + "' AND client_password = '" + passwordField.getText() + "'";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            if (queryResult.next()){
                String name = queryResult.getString("client_name");
                String family = queryResult.getString("client_family");
                String login = usernameField.getText();
                String password = passwordField.getText();

                ((Stage) usernameField.getScene().getWindow()).close();
                openClientWindow(login, password);
            } else {
                loginMessageLabel.setText("Неверный логин или пароль");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createAccountForm(){

        ((Stage) usernameField.getScene().getWindow()).close();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("autoservice_register.fxml"));
            Stage registrerStage = new Stage();
            registrerStage.setTitle("AVTO67");
            registrerStage.setScene(new Scene(root, 672, 592));
            registrerStage.show();

        }catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
    }

    private void openClientWindow(String username, String password){
        try {
            FXMLLoader loader = new FXMLLoader((getClass().getResource("/org/project/autoserviceapp/client/client_menu.fxml")));
            Parent root = loader.load();

            ClientController clientController = loader.getController();
            clientController.setUserCredentials(username, password);

            Stage stage = new Stage();
            stage.setTitle("AVTO67");
            stage.setScene(new Scene(root, 1000, 600));
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